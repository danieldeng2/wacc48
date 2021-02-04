import org.antlr.v4.runtime.*

import antlr.*
import analyser.SymbolTable
import exceptions.ThrowingErrorListener
import java.util.*

fun main(args: Array<String>) {
    val input: CharStream = when {
        args.isEmpty() -> CharStreams.fromStream(System.`in`)
        else -> CharStreams.fromFileName(args[0])
    }

    // Lexical Analysis
    val lexer = WACCLexer(input)
    lexer.removeErrorListeners()
    lexer.addErrorListener(ThrowingErrorListener())

    val tokens = CommonTokenStream(lexer)

    // Syntax Analysis
    val parser = WACCParser(tokens)
    parser.removeErrorListeners()
    parser.addErrorListener(ThrowingErrorListener())

    // Semantic Analysis
    val programNode = ASTGenerator().visitProg(parser.prog())
    programNode.validate(SymbolTable(null))

    // Print out Abstract Syntax Tree
    println(programNode)
}

