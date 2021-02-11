import analyser.nodes.ASTNode
import org.antlr.v4.runtime.*
import exceptions.SemanticsException
import exceptions.SyntaxException
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val input: CharStream = when {
        args.isEmpty() -> CharStreams.fromStream(System.`in`)
        else -> CharStreams.fromFileName(args[0])
    }
    val pNode: ASTNode

    try {
        pNode = runCompiler(input)
    } catch (e: SyntaxException) {
        println("Syntax Error: ${e.message}")
        exitProcess(100)
    } catch (e: SemanticsException) {
        println("Semantics Error: ${e.message}")
        exitProcess(200)
    }

    // Print out AST tree
    println(pNode)
}

