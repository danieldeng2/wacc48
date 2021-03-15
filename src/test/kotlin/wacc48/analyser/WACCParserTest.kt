package wacc48.analyser

import WACCLexer
import WACCParser
import junit.framework.TestCase.fail
import org.antlr.v4.runtime.*
import org.junit.Test

class WACCParserTest {

    private class TestingErrorListener : BaseErrorListener() {
        override fun syntaxError(
            recognizer: Recognizer<*, *>?,
            offendingSymbol: Any?,
            line: Int,
            charPositionInLine: Int,
            msg: String?,
            e: RecognitionException?
        ) {
            throw Exception("Syntax Error")
        }
    }

    private val errorListener = TestingErrorListener()

    private fun parse(str: String): WACCParser {
        val byteStream = str.byteInputStream()
        val charStream = CharStreams.fromStream(byteStream)
        val lexer = WACCLexer(charStream)
        val tokenStream = CommonTokenStream(lexer)
        val parser = WACCParser(tokenStream)

        parser.removeErrorListeners()
        parser.addErrorListener(errorListener)

        return parser
    }

    @Test
    fun simpleDeclarationParsesCorrectly() {
        parse("int x = 1").stat()
    }

    @Test
    fun invalidDeclarationGivesSyntaxError() {
        try {
            parse("int x a = 32").stat()
            fail()
        } catch (e: Exception) {
            // Successful error
        }
    }

    @Test
    fun invalidIdentifierDeclarationSyntaxError() {
        try {
            parse("int _abc++ = 32").stat()
            fail()
        } catch (e: Exception) {
            // Successful error
        }
    }

    @Test
    fun functionWithoutParamDeclaration() {
        parse("int dec() is println \'x\' end").func()
    }

    @Test
    fun functionWithParamDeclarationSuccessful() {
        parse("int dec(int x) is println x end").func()
    }

    @Test
    fun functionDeclarationWithWhitespaceSuccessful() {
        parse("char test() is skip end").func()
    }

    @Test
    fun functionDeclarationWithKeywordFailsCorrectly() {
        try {
            parse("int ord() is skip end").func()
            fail()
        } catch (e: Exception) {
            // Success
        }
    }
}