import antlr.WACCLexer
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class WACCLexerTest {

    private fun getTokensFromString(string: String): MutableList<Token>? {

        val byteStream = string.byteInputStream()
        val charStream = CharStreams.fromStream(byteStream)
        val lexer = WACCLexer(charStream)
        val tokenStream = CommonTokenStream(lexer)
        tokenStream.fill()

        return tokenStream.tokens
    }

    @Test
    fun emptyStringReturnsZeroToken() {
        val tokens = getTokensFromString("")

        // token[0] = EOF
        assertEquals(1, tokens!!.size)
    }

    @Test
    fun simpleAdditionExpr() {
        val tokens = getTokensFromString("1 + 3")

        assertNotNull(tokens)
        assertEquals(WACCLexer.INT_LITER, tokens[0].type)
        assertEquals(WACCLexer.BINARY_OPERATOR, tokens[1].type)
        assertEquals(WACCLexer.INT_LITER, tokens[2].type)
    }

    @Test
    fun whitespacesSkippedSimpleAdditionExpr() {
        val tokens = getTokensFromString("1 \t\t + \n 2 \$ asdf3")

        assertNotNull(tokens)
        assertEquals(WACCLexer.INT_LITER, tokens[0].type)
        assertEquals(WACCLexer.BINARY_OPERATOR, tokens[1].type)
        assertEquals(WACCLexer.INT_LITER, tokens[2].type)
    }

    @Test
    fun identifierStartsWithUnderscoreAccepted() {
        val tokens = getTokensFromString("_testIdentifier")
        assertEquals(WACCLexer.IDENT, tokens!![0].type)
    }

    @Test
    fun identifierStartsWithUppercaseLetterAccepted() {
        val tokens = getTokensFromString("Identifier")
        assertEquals(WACCLexer.IDENT, tokens!![0].type)
    }

    @Test
    fun identifierCanContainDigit() {
        val tokens = getTokensFromString("_123digit")
        assertEquals(WACCLexer.IDENT, tokens!![0].type)
    }

    @Test
    fun invalidIdentifierGeneratesErrorCorrectly() {
        val tokens = getTokensFromString("_false@@")
        assertEquals(2, tokens!!.size)
    }

    @Test
    fun intLiteralWithoutLeadingSignTokenisedCorrectly() {
        val tokens = getTokensFromString("123456")
        assertEquals(WACCLexer.INT_LITER, tokens!![0].type)
        assertEquals(123456, tokens[0].text.toInt())
    }

    @Test
    fun intLiteralWithPlusSignTokenisedCorrectly() {
        val tokens = getTokensFromString("+123")
        assertEquals(WACCLexer.INT_LITER, tokens!![0].type)
        assertEquals(123, tokens[0].text.toInt())
    }

    @Test
    fun intLiteralWithMinusSignTokenisedCorrectly() {
        val tokens = getTokensFromString("-123")
        assertEquals(WACCLexer.INT_LITER, tokens!![0].type)
        assertEquals(-123, tokens[0].text.toInt())
    }

    @Test
    fun intLiteralWithSignInExprTokenisedCorrectly() {
        val tokens = getTokensFromString("-123 + +456")

        assertNotNull(tokens)
        assertEquals(4, tokens.size)
        assertEquals(-123, tokens[0].text.toInt())
        assertEquals(WACCLexer.BINARY_OPERATOR, tokens[1].type)
        assertEquals(456, tokens[2].text.toInt())
    }

    @Test
    fun simpleStringTokenisedCorrectly() {
        val tokens = getTokensFromString("\"abcdef123\"")

        assertEquals(2, tokens!!.size)
        assertEquals("\"abcdef123\"", tokens[0].text)
    }

    @Test
    fun stringWithEscapedCharsTokenisedCorrectly() {
        val tokens = getTokensFromString("\"abc\b\t\n\rabc\"")
        assertEquals(2, tokens!!.size)
        assertEquals("\"abc\b\t\n\rabc\"", tokens[0].text)
    }
}