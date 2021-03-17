package com.github.danieldeng2.waccplugin.action

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.process.OSProcessHandler
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import org.apache.commons.io.FileUtils
import java.io.File

object WaccShellHelpers {
    fun newShellWindow(project: Project, toolWindow: ToolWindow) {

        val consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).console
        val content =
            toolWindow.contentManager.factory.createContent(consoleView.component, "Wacc Shell", false)
        createShellJarFile(project)
        content.isCloseable = true
        val handle = OSProcessHandler(
            GeneralCommandLine("java", "-jar", "out/shell/shell.jar", "shell").withWorkDirectory(project.basePath)
        )
        consoleView.attachToProcess(handle)
        handle.startNotify()
        toolWindow.contentManager.addContent(content)
    }

    private fun createShellJarFile(project: Project) {
        File("${project.basePath}/out").mkdir()
        val jarStream = javaClass.classLoader.getResourceAsStream("shell/shell.jar")
        val targetFile = File("${project.basePath}/out/shell/shell.jar")
        FileUtils.copyInputStreamToFile(jarStream, targetFile)
    }
}
