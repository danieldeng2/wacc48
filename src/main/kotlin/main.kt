import org.antlr.v4.runtime.*

import antlr.*
import analyser.SymbolTable
import exceptions.ThrowingErrorListener
import java.util.*

fun main(args: Array<String>) {
    val input: CharStream = when {
        args.isEmpty() -> CharStreams.fromString(Scanner(System.`in`).nextLine())
        else -> CharStreams.fromFileName(args[0])
    }

    val lexer = WACCLexer(input)
    lexer.removeErrorListeners()
    lexer.addErrorListener(ThrowingErrorListener())

    val tokens = CommonTokenStream(lexer)

    val parser = WACCParser(tokens)
    parser.removeErrorListeners()
    parser.addErrorListener(ThrowingErrorListener())

    val programNode = ASTGenerator().visitProg(parser.prog())
    programNode.validate(SymbolTable(null))

    println(programNode)
}

