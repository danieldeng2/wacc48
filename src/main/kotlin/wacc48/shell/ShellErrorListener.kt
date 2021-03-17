package wacc48.shell

import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer

class ShellErrorListener(val endOfInput: Boolean = false) : BaseErrorListener() {
    override fun syntaxError(
        recognizer: Recognizer<*, *>?,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String?,
        e: RecognitionException?
    ) {
        if (!endOfInput && (msg!!.startsWith("mismatched input '<EOF>'") ||
            "missing '.*' at '<EOF>'".toRegex().containsMatchIn(msg))
        ) {
            throw IncompleteRuleException("line $line:$charPositionInLine $msg")
        }

        var underline = ""
        for (i in 1..(charPositionInLine + 4))
            underline += " "
        underline += "^\n"

        throw ShellSyntaxException(underline + "Syntax error: line $line:$charPositionInLine $msg")
    }
}

class IncompleteRuleException(message: String) : Exception(message)

class ShellSyntaxException(s: String) : Exception(s)

class ShellSemanticException(s: String) : Exception(s)
