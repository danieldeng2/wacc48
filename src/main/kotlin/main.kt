import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import java.io.File
import java.io.FileWriter
import java.nio.file.Path

fun main(args: Array<String>) {
    if (args[0] == "-i") {
        val shell: WACCShell
        if (args.size == 3) {
            if (args[1] == "-p")
                shell = WACCShell(programPath = Path.of(args[2]))
            else {
                println("Interpreter usage: ./interpret -p <WACC File>")
                return
            }
        } else {
            shell = WACCShell()
        }
        shell.runInteractiveShell()
        return
    }

    if (args.isEmpty() || !File(args[0]).exists()) {
        println("Usage: ./compile <WACC Source>")
        return
    }

    val sourceFile = Path.of(args[0])
    val input: CharStream = CharStreams.fromPath(sourceFile)
    val pNode = runAnalyserCatchError(input)
    val output = runGenerator(pNode)
    writeResult(sourceFile.fileName.toString(), output)
}

private fun writeResult(inputName: String, output: List<String>) {
    val outName = inputName.replace(".wacc", ".s")
    val writer = FileWriter(outName)
    output.forEach { writer.appendLine(it) }
    writer.close()
}
