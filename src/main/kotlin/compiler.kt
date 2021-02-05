import analyser.ASTGenerator
import org.antlr.v4.runtime.*

import antlr.*
import analyser.SymbolTable
import analyser.nodes.ASTNode
import exceptions.ThrowingErrorListener


fun runCompiler(input: CharStream): ASTNode {
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
    programNode.validate(SymbolTable(null))

    return programNode
}

