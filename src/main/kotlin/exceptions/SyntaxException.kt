package exceptions

import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import kotlin.system.exitProcess

class ThrowingErrorListener : BaseErrorListener() {
    override fun syntaxError(
        recognizer: Recognizer<*, *>?,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String?,
        e: RecognitionException?
    ) {
        throw SyntaxException("line $line:$charPositionInLine $msg")
    }
}

class SyntaxException(message: String) : Exception(message) {
    init {
        println("Syntax Error: $message")
        exitProcess(100)
    }
}