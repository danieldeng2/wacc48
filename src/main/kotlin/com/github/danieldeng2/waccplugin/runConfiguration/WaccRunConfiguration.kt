package com.github.danieldeng2.waccplugin.runConfiguration

import com.intellij.execution.DefaultExecutionResult
import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.LocatableConfigurationBase
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessHandlerFactory
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import org.antlr.v4.runtime.CharStreams
import wacc48.analyser.exceptions.Issue
import wacc48.analyser.exceptions.ParserException
import wacc48.generator.architecture.I386Architecture
import wacc48.runAnalyser
import wacc48.writeToFile
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class WaccRunConfiguration constructor(project: Project, factory: ConfigurationFactory, name: String) :
    LocatableConfigurationBase<WaccRunConfigurationOptions>(project, factory, name) {

    var waccFileName: String? = null
        get() = options.waccFileName
        set(value) {
            options.waccFileName = value
            field = value
        }

    override fun getOptions(): WaccRunConfigurationOptions =
        super.getOptions() as WaccRunConfigurationOptions

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration?> =
        WaccSettingsEditor()

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState? {
        val waccFileName = waccFileName
        if (waccFileName == null || !File(waccFileName).exists()) return null

        val waccFile = File(waccFileName)
        File("${project.basePath}/out").mkdir()
        val execName = "${project.basePath}/out/${waccFile.name.removeSuffix(".wacc")}"

        val messages = compileWaccProgram(waccFile, execName)

        return object : CommandLineState(environment) {
            override fun startProcess(): ProcessHandler {
                val command = if (messages.isEmpty()) execName else "ls"

                val commandLine =
                    GeneralCommandLine(command).withWorkDirectory(project.basePath)
                commandLine.charset = Charset.forName("UTF-8")
                val processHandler = ProcessHandlerFactory.getInstance().createColoredProcessHandler(commandLine)
                ProcessTerminatedListener.attach(processHandler)
                return processHandler
            }

            override fun execute(executor: Executor, runner: ProgramRunner<*>): ExecutionResult {
                val processHandler = startProcess()
                val console = createConsole(executor)
                messages.forEach {
                    console?.print("$it\n", ConsoleViewContentType.ERROR_OUTPUT)
                }
                if (messages.isEmpty()) console?.attachToProcess(processHandler)
                return DefaultExecutionResult(
                    console,
                    processHandler,
                    *createActions(console, processHandler, executor)
                )
            }
        }
    }

    private fun compileWaccProgram(waccFile: File, execName: String): List<String> {
        val dependencyNeeded = "Please ensure that gcc, gcc-multilib / glibc-devel.i686, and nasm are all " +
            "installed in order to compile wacc programs for x86."

        val asmFileName = "$execName.s"

        val issues: MutableList<Issue> = mutableListOf()
        val messages: MutableList<String> = mutableListOf()
        try {
            val programNode = runAnalyser(CharStreams.fromPath(waccFile.toPath()), issues)
            val instructions = I386Architecture.compile(programNode)
            writeToFile(instructions, asmFileName)
            I386Architecture.createExecutable(asmFileName, execName)
        } catch (e: ParserException) {
            e.message?.let { messages.add("SYNTAX ERROR: \n$it") }
        } catch (e: IOException) {
            messages.add(dependencyNeeded)
        }

        messages.apply {
            addAll(issues.map { it.toString() })
            if (!File(execName).exists() && messages.isEmpty()) add(dependencyNeeded)
        }

        return messages
    }
}
