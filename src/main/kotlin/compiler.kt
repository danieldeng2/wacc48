import analyser.ASTGenerator
import org.antlr.v4.runtime.*

import antlr.*
import analyser.SymbolTable
import analyser.nodes.ASTNode
import exceptions.ThrowingErrorListener
import reference.RefCompiler
import java.io.File


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

// TODO(Implement Generator and remove filename)
fun runGenerator(pNode: ASTNode, filename: String): List<String> {
    return RefCompiler(File(filename)).run()
}


