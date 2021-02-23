import analyser.ASTGenerator
import org.antlr.v4.runtime.*

import analyser.SymbolTable
import analyser.nodes.ASTNode
import exceptions.ThrowingErrorListener
import generator.TranslatorContext

fun runAnalyser(input: CharStream): ASTNode {
    // Lexical Analysis
    val lexer = WACCLexer(input)
    lexer.removeErrorListeners()
    lexer.addErrorListener(ThrowingErrorListener())

    val tokens = CommonTokenStream(lexer)

    // Syntax Analysis
    val parser = WACCParser(tokens)
    parser.removeErrorListeners()
    parser.addErrorListener(ThrowingErrorListener())

    // Semantic Analysis
    val programNode = ASTGenerator().visitProg(parser.prog())
    programNode.validate(SymbolTable(null), SymbolTable(null))

    return programNode
}

fun runGenerator(pNode: ASTNode) =
    mutableListOf<String>().apply {
        add(".text")
        add("")
        add(".global main")

        val programInstructions = pNode.translate(TranslatorContext())

        addAll(
            programInstructions.map { it.toString() }
        )
    }




