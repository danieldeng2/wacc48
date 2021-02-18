import analyser.nodes.ASTNode
import org.antlr.v4.runtime.*
import exceptions.SemanticsException
import exceptions.SyntaxException
import java.io.FileWriter
import java.nio.file.Paths
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val input: CharStream = when {
        args.isEmpty() -> CharStreams.fromStream(System.`in`)
        else -> CharStreams.fromFileName(args[0])
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

    val output = runGenerator(pNode, args[0])

    when {
        args.isEmpty() -> println(output)
        else -> {
            val p = Paths.get(args[0])
            val outName = p.fileName.toString().replace(".wacc", ".s")
            val writer = FileWriter(outName)
            output.forEach { writer.write(it + System.lineSeparator()) }
            writer.close()
        }
    }
}

