import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import java.io.FileWriter

class ArgParse : CliktCommand() {
    private val arm by option("-arm", help = "Generate code for ARM11").flag()
    private val sourcePath by argument(help = "Path to WACC file").path(
        mustExist = true,
        canBeFile = true,
        canBeDir = false
    )

    override fun run() {
        echo("WACC Compiler - Group 48")

        val input: CharStream = CharStreams.fromPath(sourcePath)
        val pNode = runAnalyserCatchError(input)
        val output = runGenerator(pNode, armAssembly = arm)
        writeResult(sourcePath.fileName.toString(), output)
    }
}

fun main(args: Array<String>) = ArgParse().main(args)

fun writeResult(inputName: String, output: List<String>) {
    val outName = inputName.replace(".wacc", ".s")
    val writer = FileWriter(outName)
    output.forEach { writer.appendLine(it) }
    writer.close()
}



