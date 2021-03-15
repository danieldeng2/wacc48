package wacc48

import wacc48.antlr.WACCLexer
import wacc48.antlr.WACCParser
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CommonTokenStream
import wacc48.analyser.ASTGeneratorVisitor
import wacc48.analyser.exceptions.ThrowingErrorListener
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.function.FuncNode

fun runAnalyser(
    input: CharStream,
    st: SymbolTable = SymbolTable(null),
    funTable: MutableMap<String, FuncNode> = mutableMapOf()
): ASTNode {
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
    val programNode = ASTGeneratorVisitor().visitProg(parser.prog())
    programNode.validate(
        st = st,
        funTable = funTable
    )

    return programNode
}