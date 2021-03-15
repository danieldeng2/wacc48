package wacc48

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.NoSuchOption
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.file
import wacc48.architecture.ArmArchitecture
import wacc48.architecture.I386Architecture
import wacc48.shell.WACCShell
import java.io.File

class ArgParse : CliktCommand() {

    private val createExecutable by option(
        "-x",
        "--executable",
        help = "Create executable from assembly file"
    ).flag()

    private val app: String by option(
        "-a",
        "--app",
        help = "Choose an application you'd like to run"
    ).choice("arm", "x86", "shell").default("arm")

    private val sourceFile by argument(help = "Path to WACC file").file(
        mustExist = true,
        canBeFile = true,
        canBeDir = false
    )

    override fun run() {
        val architecture = when (app) {
            "x86" -> I386Architecture
            "arm" -> ArmArchitecture
            "shell" -> {
                WACCShell().runInteractiveShell()
                return
            }
            else -> throw NoSuchOption("No such option exists!")
        }

        val programNode = runAnalyserCatchError(sourceFile)
        val instructions = architecture.compile(programNode)

        val srcNoExtension = sourceFile.name.removeSuffix(".wacc")
        val asmFile = File("$srcNoExtension.s")
        writeToFile(instructions, asmFile.path)
    }
}

fun main(args: Array<String>) = ArgParse().main(args)
