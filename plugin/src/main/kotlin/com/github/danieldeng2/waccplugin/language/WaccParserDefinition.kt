package com.github.danieldeng2.waccplugin.language

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.ParserDefinition.SpaceRequirements
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory
import org.antlr.intellij.adaptor.parser.ANTLRParserAdaptor
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.tree.ParseTree
import wacc48.antlr.WACCLexer
import wacc48.antlr.WACCParser

class WaccParserDefinition : ParserDefinition {

    override fun createLexer(project: Project): Lexer =
        ANTLRLexerAdaptor(WaccLanguage, WACCLexer(null))

    override fun createParser(project: Project?): PsiParser =
        object : ANTLRParserAdaptor(WaccLanguage, WACCParser(null)) {
            override fun parse(parser: Parser, root: IElementType): ParseTree =
                (parser as WACCParser).prog()
        }

    override fun createFile(viewProvider: FileViewProvider): PsiFile = WaccFile(viewProvider)

    override fun createElement(node: ASTNode): PsiElement = ANTLRPsiNode(node)

    override fun spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode): SpaceRequirements =
        SpaceRequirements.MAY

    /* returns static objects */
    override fun getWhitespaceTokens() = WHITE_SPACES

    override fun getCommentTokens(): TokenSet = COMMENTS

    override fun getStringLiteralElements(): TokenSet = STRING

    override fun getFileNodeType(): IFileElementType = FILE

    companion object {
        init {
            PSIElementTypeFactory.defineLanguageIElementTypes(WaccLanguage, WACCParser.tokenNames, WACCParser.ruleNames)
        }

        val WHITE_SPACES: TokenSet = PSIElementTypeFactory.createTokenSet(
            WaccLanguage,
            WACCLexer.WS
        )
        val COMMENTS: TokenSet = PSIElementTypeFactory.createTokenSet(
            WaccLanguage,
            WACCLexer.COMMENT
        )
        val STRING: TokenSet = PSIElementTypeFactory.createTokenSet(
            WaccLanguage,
            WACCLexer.STR_LITER
        )
        val FILE = IFileElementType(WaccLanguage)
    }
}
