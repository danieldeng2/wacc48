package wacc48

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.NoSuchOption
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.path
import wacc48.generator.architecture.ArmArchitecture
import wacc48.generator.architecture.I386Architecture
import wacc48.shell.WACCShell
import java.io.File
import java.nio.file.Path

class Shell : CliktCommand() {
    private val sourceFile by option(
        "-s",
        "--src",
        help = "Path to WACC file to be loaded into interactive shell"
    ).path(
        mustExist = true,
        canBeFile = true,
        canBeDir = false
    )

    private val welcomeMessage = """
    >>> WACC Interactive Shell <<<
    Instructions: "
        Exit wacc48.shell: use Ctrl-d (EOF) or 'quit' in normal scope
        Cancel multiline command: use Ctrl-d"
        '>>>' is the prompt for a new command"
        '...' is the prompt to continue the current command (multiple lines)
    """.trimIndent()

    override fun run() {
        echo(welcomeMessage)
        WACCShell(programPath = sourceFile).runInteractiveShell()
    }
}

class Compiler : CliktCommand(printHelpOnEmptyArgs = true) {

    private val createExecutable by option(
        "-x",
        "--executable",
        help = "Create executable from assembly file"
    ).flag()

    private val app: String by option(
        "-a",
        "--architecture",
        help = "Choose an architecture for your compiler"
    ).choice("arm", "x86").default("arm")

    private val sourceFile by argument(help = "Path to WACC file").file(
        mustExist = true,
        canBeFile = true,
        canBeDir = false
    )

    override fun run() {

        val architecture = when (app) {
            "x86" -> I386Architecture
            "arm" -> ArmArchitecture
            else -> throw NoSuchOption("No such option exists!")
        }

        val programNode = runAnalyserCatchError(sourceFile)
        val instructions = architecture.compile(programNode)

        echo("Generating assembly file for $app architecture")

        val srcNoExtension = sourceFile.name.removeSuffix(".wacc")
        val asmFile = File("$srcNoExtension.s")
        writeToFile(instructions, asmFile.path)

        echo("Assembly file written to \'${asmFile.name}\'")

        if (createExecutable) {
            architecture.createExecutable(asmFile.path, srcNoExtension)
            echo("Executable created at \'$srcNoExtension\'")
        }
    }
}

class Cli : CliktCommand(printHelpOnEmptyArgs = true) {

    override fun run() {}
}

fun main(args: Array<String>) =
    Cli().subcommands(Shell(), Compiler()).main(args)
