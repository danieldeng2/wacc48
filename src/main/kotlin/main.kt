import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.NoSuchOption
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.file
import java.io.FileWriter
import java.nio.file.Paths

class ArgParse : CliktCommand() {

    override val commandHelp = """
        Welcome to group 48 WACC compiler.
        
        You can: 
        ```
            - Compile .wacc file to x86 assembly
            - Compile .wacc file to ARM assembly
            - Runs an interactive shell
        ```
        
        Run --help for more information.
    """.trimIndent()

    private val createExecutable by option(
        "-x",
        "--executable",
        help = "Create executable from assembly file"
    ).flag()

    private val outDirectory by option(
        "-o",
        "--dir",
        "-d",
        help = "Directory for the output file"
    ).file(mustExist = true, canBeDir = true, canBeFile = false)
        .default(Paths.get(".").toFile())

    private val sourceFile by argument(help = "Path to WACC file").file(
        mustExist = true,
        canBeFile = true,
        canBeDir = false
    )

    private val app: String by option(
        "-a",
        "--app",
        help = "Choose an application you'd like to run"
    ).choice("arm", "x86", "shell").default("arm")


    override fun run() {
        val application = when (app) {
            "x86" -> WaccCompiler(
                I386Formatter(),
                sourceFile,
                outDirectory,
                createExecutable
            )
            "arm" -> WaccCompiler(
                ArmFormatter(),
                sourceFile,
                outDirectory,
                createExecutable
            )
            else -> throw NoSuchOption("No such option exists!")
        }
        application.start()
    }
}

fun main(args: Array<String>) = ArgParse().main(args)

fun writeResult(inputName: String, output: List<String>) {
    val outName = inputName.replace(".wacc", ".s")
    val writer = FileWriter(outName)
    output.forEach { writer.appendLine(it) }
    writer.close()
}



