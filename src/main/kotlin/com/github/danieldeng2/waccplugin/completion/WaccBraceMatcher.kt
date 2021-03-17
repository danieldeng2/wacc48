package com.github.danieldeng2.waccplugin.completion

import com.github.danieldeng2.waccplugin.language.WaccLanguage
import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory
import org.antlr.intellij.adaptor.lexer.TokenIElementType
import wacc48.antlr.WACCLexer

class WaccBraceMatcher : PairedBraceMatcher {
    private var tokenIElementTypes: MutableList<TokenIElementType> =
        PSIElementTypeFactory.getTokenIElementTypes(WaccLanguage)

    override fun getPairs(): Array<BracePair> = arrayOf(
        BracePair(
            tokenIElementTypes[WACCLexer.OPEN_PAREN],
            tokenIElementTypes[WACCLexer.CLOSE_PAREN],
            false
        ),
        BracePair(
            tokenIElementTypes[WACCLexer.OPEN_SQR_PAREN],
            tokenIElementTypes[WACCLexer.CLOSE_SQR_PAREN],
            false
        ),
    )

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean = true

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int =
        openingBraceOffset
}
