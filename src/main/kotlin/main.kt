import org.antlr.v4.runtime.*

import antlr.*
import java.util.*

fun main(args: Array<String>) {
    val input: CharStream = when {
        args.isEmpty() -> CharStreams.fromString(Scanner(System.`in`).nextLine())
        else -> CharStreams.fromFileName(args[0])
    }
    val lexer = WACCLexer(input)
    val tokens = CommonTokenStream(lexer)
    val parser = WACCParser(tokens)

    val semanticAnalyser = SemanticAnalyser()
    semanticAnalyser.visitProg(parser.prog())

    println(semanticAnalyser.errorReporter.syntaxErrors)
    println(semanticAnalyser.errorReporter.semanticErrors)
}