package com.github.danieldeng2.waccplugin.annotator

import com.github.danieldeng2.waccplugin.language.WaccLanguage
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor
import org.antlr.intellij.adaptor.lexer.TokenIElementType
import wacc48.antlr.WACCLexer

class WaccSyntaxHighlighterFactory : SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter =
        WaccSyntaxHighlighter()
}

class WaccSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer =
        ANTLRLexerAdaptor(WaccLanguage, WACCLexer(null))

    override fun getTokenHighlights(tokenType: IElementType): Array<out TextAttributesKey?> {
        if (tokenType !is TokenIElementType) return EMPTY_KEYS
        val attrKey: TextAttributesKey = when (tokenType.antlrTokenType) {
            WACCLexer.INT,
            WACCLexer.BOOL,
            WACCLexer.CHAR,
            WACCLexer.STRING,
            WACCLexer.BEGIN,
            WACCLexer.END,
            WACCLexer.RETURN,
            WACCLexer.IF,
            WACCLexer.THEN,
            WACCLexer.ELSE,
            WACCLexer.FI,
            WACCLexer.PAIR,
            WACCLexer.WHILE,
            WACCLexer.DO,
            WACCLexer.NULL,
            WACCLexer.BOOL_LITER,
            WACCLexer.SKIP_,
            WACCLexer.DONE ->
                KEYWORD

            WACCLexer.READ,
            WACCLexer.FREE,
            WACCLexer.PRINT,
            WACCLexer.PRINTLN,
            WACCLexer.NEWPAIR,
            WACCLexer.FST,
            WACCLexer.SND,
            WACCLexer.IS,
            WACCLexer.EXIT ->
                KEYWORD

            WACCLexer.CALL ->
                FUNC_CALL

            WACCLexer.COMMA,
            WACCLexer.EQUAL,
            WACCLexer.PLUS,
            WACCLexer.MINUS,
            WACCLexer.NOT,
            WACCLexer.LEN,
            WACCLexer.ORD,
            WACCLexer.CHR,
            WACCLexer.MUL,
            WACCLexer.DIV,
            WACCLexer.MOD,
            WACCLexer.GT,
            WACCLexer.GE,
            WACCLexer.LT,
            WACCLexer.LE,
            WACCLexer.EQ,
            WACCLexer.NEQ,
            WACCLexer.AND,
            WACCLexer.OR ->
                OPERATOR

            WACCLexer.OPEN_PAREN,
            WACCLexer.CLOSE_PAREN,
            WACCLexer.OPEN_SQR_PAREN,
            WACCLexer.CLOSE_SQR_PAREN ->
                BRACKETS

            WACCLexer.COMMENT ->
                COMMENT

            WACCLexer.SEMICOLON ->
                COLON

            WACCLexer.STR_LITER, WACCLexer.CHAR_LITER ->
                STRING

            WACCLexer.IDENT ->
                IDENT

            WACCLexer.INT_LITER ->
                NUMBER

            else -> {
                return EMPTY_KEYS
            }
        }

        return arrayOf(attrKey)
    }

    companion object {
        private val KEYWORD =
            TextAttributesKey.createTextAttributesKey("WACC_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        private val STRING =
            TextAttributesKey.createTextAttributesKey("WACC_STRING", DefaultLanguageHighlighterColors.STRING)

        private val NUMBER =
            TextAttributesKey.createTextAttributesKey("WACC_INT", DefaultLanguageHighlighterColors.NUMBER)

        private val COMMENT =
            TextAttributesKey.createTextAttributesKey("WACC_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)

        private val IDENT =
            TextAttributesKey.createTextAttributesKey("WACC_IDENT", DefaultLanguageHighlighterColors.IDENTIFIER)

        private val BRACKETS =
            TextAttributesKey.createTextAttributesKey("WACC_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS)

        private val COLON =
            TextAttributesKey.createTextAttributesKey("WACC_COLON", DefaultLanguageHighlighterColors.SEMICOLON)

        private val FUNC_CALL =
            TextAttributesKey.createTextAttributesKey(
                "WACC_FUNC_CALL",
                DefaultLanguageHighlighterColors.FUNCTION_DECLARATION
            )

        private val OPERATOR =
            TextAttributesKey.createTextAttributesKey(
                "WACC_OPERATOR",
                DefaultLanguageHighlighterColors.OPERATION_SIGN
            )

        private val EMPTY_KEYS = arrayOfNulls<TextAttributesKey>(0)
    }
}
