import analyser.nodes.ASTNode
import org.antlr.v4.runtime.*
import exceptions.SemanticsException
import exceptions.SyntaxException
import reference.RefCompiler
import reference.RefEmulator
import java.io.File
import java.io.FileWriter
import java.nio.file.Paths
import kotlin.system.exitProcess

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import java.lang.UnsupportedOperationException
import java.nio.file.Path

class Cli : CliktCommand() {

    private val reference by option(
        "-r",
        "--reference",
        help = "Run with reference compiler"
    ).flag()

    private val stdin: String by option(
        "-s",
        "--stdin",
        help = "Run with stdin value"
    ).default("")

    private val sourceFile: Path by argument().path(mustExist = true)

    override fun run() {
        val input: CharStream = CharStreams.fromPath(sourceFile)

        val pNode: ASTNode =
            try {
                runAnalyser(input)
            } catch (e: SyntaxException) {
                println("Syntax Error: ${e.message}")
                exitProcess(100)
            } catch (e: SemanticsException) {
                println("Semantics Error: ${e.message}")
                exitProcess(200)
            }

        val output =
            if (reference)
                RefCompiler(sourceFile.toFile()).run()
            else
                runGenerator(pNode)

        val outName = writeResult(sourceFile.fileName.toString(), output)
        runEmulator(outName, stdin)
    }

    private fun writeResult(inputName: String, output: List<String>): String {
        val outName = inputName.replace(".wacc", ".s")
        val writer = FileWriter(outName)
        output.forEach { writer.appendLine(it) }
        writer.close()
        return outName
    }

    private fun runEmulator(outName: String, stdin: String) {
        val emulator = RefEmulator(File(outName)).execute(stdin)
        println(emulator.emulatorOut)
        println("exit ${emulator.emulatorExit}")
    }
}

fun main(args: Array<String>) = Cli().main(args)

