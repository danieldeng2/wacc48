import org.antlr.v4.runtime.*

import antlr.*
import org.antlr.v4.runtime.misc.ParseCancellationException


fun main(args: Array<String>) {
    val input: CharStream = CharStreams.fromStream(System.`in`)
    val lexer = WACCLexer(input)
    lexer.removeErrorListeners();
    lexer.addErrorListener(ThrowingErrorListener());
    val tokens = CommonTokenStream(lexer)
    val parser = WACCParser(tokens)
    parser.removeErrorListeners()
    parser.addErrorListener(ThrowingErrorListener())
    val tree = parser.prog()

    println(tree.toStringTree(parser))
}

class ThrowingErrorListener() : BaseErrorListener() {
    override fun syntaxError(
        recognizer: Recognizer<*, *>?,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String?,
        e: RecognitionException?
    ) {
        throw ParseCancellationException("line $line:$charPositionInLine $msg");
    }
}

