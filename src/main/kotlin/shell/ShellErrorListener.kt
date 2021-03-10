package shell

import analyser.exceptions.SyntaxException
import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer

class ShellErrorListener : BaseErrorListener() {
    override fun syntaxError(
        recognizer: Recognizer<*, *>?,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String?,
        e: RecognitionException?
    ) {
        if (msg!!.startsWith("mismatched input '<EOF>'") ||
            "missing '.*' at '<EOF>'".toRegex().containsMatchIn(msg)
        ) {
            throw IncompleteRuleException("line $line:$charPositionInLine $msg")
        }

        //TODO(Make syntax exception underline the offending char)
        throw SyntaxException("line $line:$charPositionInLine $msg")
    }
}

class IncompleteRuleException(message: String) : Exception(message)
