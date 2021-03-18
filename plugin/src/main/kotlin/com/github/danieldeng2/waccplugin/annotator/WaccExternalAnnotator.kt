package com.github.danieldeng2.waccplugin.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.ExternalAnnotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import wacc48.analyser.ASTGeneratorVisitor
import wacc48.analyser.exceptions.Issue
import wacc48.antlr.WACCLexer
import wacc48.antlr.WACCParser
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.ASTNode

/** An external annotator is an object that analyzes code in a document
 * and annotates the PSI elements with errors or warnings. Because such
 * analysis can be expensive, we don't want it in the GUI event loop. Jetbrains
 * provides this external annotator mechanism to run these analyzers out of band.
 */
class WaccExternalAnnotator : ExternalAnnotator<CharStream, List<Issue>>() {

    /** Called first  */
    override fun collectInformation(file: PsiFile): CharStream =
        CharStreams.fromString(file.text)

    /** Called 2nd; look for trouble in file and return list of issues.
     */

    override fun doAnnotate(stream: CharStream): List<Issue> {
        val issues = mutableListOf<Issue>()
        val programNode: ASTNode
        try {
            val lexer = WACCLexer(stream)
            val tokens = CommonTokenStream(lexer)
            val parser = WACCParser(tokens)
            programNode = ASTGeneratorVisitor().visitProg(parser.prog())

            programNode.validate(
                st = SymbolTable(null),
                funTable = mutableMapOf(),
                issues = issues
            )
        } catch (e: Exception) {
            return issues
        }
        return issues
    }

    /** Called 3rd to actually annotate the editor window  */
    override fun apply(file: PsiFile, issues: List<Issue>, holder: AnnotationHolder) {
        for (issue in issues) {
            if (issue.ctx != null) {
                val textRange = TextRange(
                    issue.ctx!!.start.startIndex, issue.ctx!!.stop.stopIndex + 1
                )
                holder.newAnnotation(HighlightSeverity.ERROR, issue.msg).range(textRange).create()
            }
        }
    }
}
