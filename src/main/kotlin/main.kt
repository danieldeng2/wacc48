import org.antlr.v4.runtime.*

import antlr.*
import org.antlr.v4.runtime.misc.ParseCancellationException
import analyser.SymbolTable
import java.util.*

fun main(args: Array<String>) {
    val input: CharStream = when {
        args.isEmpty() -> CharStreams.fromString(Scanner(System.`in`).nextLine())
        else -> CharStreams.fromFileName(args[0])
    }
    val lexer = WACCLexer(input)
    lexer.removeErrorListeners();
    lexer.addErrorListener(ThrowingErrorListener());
    val tokens = CommonTokenStream(lexer)
    val parser = WACCParser(tokens)

    parser.removeErrorListeners()
    parser.addErrorListener(ThrowingErrorListener())
    
    val progNode = ASTGenerator().visitProg(parser.prog())
    progNode.validate(SymbolTable(null))
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
}
