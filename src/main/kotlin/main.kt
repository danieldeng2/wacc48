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
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import java.lang.UnsupportedOperationException
import java.nio.file.Path

class Cli : CliktCommand() {

    val reference: Boolean by option(
        "-r",
        "--reference",
        help = "Run with reference compiler"
    ).flag()

    val sourceFile: Path? by argument().path(mustExist = true)

    override fun run() {
        val input: CharStream = when (sourceFile) {
            null -> CharStreams.fromStream(System.`in`)
            else -> CharStreams.fromPath(sourceFile)
        }
        val pNode: ASTNode

        try {
            pNode = runAnalyser(input)
        } catch (e: SyntaxException) {
            println("Syntax Error: ${e.message}")
            exitProcess(100)
        } catch (e: SemanticsException) {
            println("Semantics Error: ${e.message}")
            exitProcess(200)
        }

        val output =
            if (reference)
                when (sourceFile) {
                    null -> throw UnsupportedOperationException("Unsupported functionality! Pass in a source-file!")
                    else -> RefCompiler(sourceFile!!.toFile()).run()

                }
            else runGenerator(pNode)

        when (sourceFile) {
            null -> println(output)
            else -> {
                val outName =
                    sourceFile!!.fileName.toString().replace(".wacc", ".s")
                val writer = FileWriter(outName)
                output.forEach { writer.write(it + System.lineSeparator()) }
                writer.close()

                val emulator = RefEmulator(File(outName)).execute("")
                println(emulator.emulatorOut)
                println("exit ${emulator.emulatorExit}")
            }
        }
    }

}

fun main(args: Array<String>) = Cli().main(args)

