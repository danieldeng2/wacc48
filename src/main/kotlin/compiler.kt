import analyser.ASTGenerator
import org.antlr.v4.runtime.*

import analyser.SymbolTable
import analyser.nodes.ASTNode
import exceptions.ThrowingErrorListener
import generator.TranslatorContext
import generator.armInstructions.Instruction

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

fun runGenerator(pNode: ASTNode): List<String> {
    val translatorCtx = TranslatorContext()
    val programInstructions = pNode.translate(translatorCtx)

    return mutableListOf<String>().apply {
        addAll(translatorCtx.data.map { it.toString() })
        addAll(programInstructions.map { it.toString() })
        addAll(translatorCtx.translateSyscall().map { it.toString() })
    }
}




