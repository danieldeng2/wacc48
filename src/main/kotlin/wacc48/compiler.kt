package wacc48

import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import wacc48.analyser.ASTGeneratorVisitor
import wacc48.analyser.exceptions.SemanticsException
import wacc48.analyser.exceptions.SyntaxException
import wacc48.analyser.exceptions.ThrowingErrorListener
import wacc48.antlr.WACCLexer
import wacc48.antlr.WACCParser
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.function.FuncNode
import java.io.File
import java.io.FileWriter
import kotlin.system.exitProcess

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

fun runAnalyserCatchError(sourceFile: File) =
    try {
        runAnalyser(CharStreams.fromPath(sourceFile.toPath()))
    } catch (e: SyntaxException) {
        println("Syntax Error: ${e.message}")
        exitProcess(100)
    } catch (e: SemanticsException) {
        println("Semantics Error: ${e.message}")
        exitProcess(200)
    }

fun writeToFile(output: List<String>, outFileName: String) {
    val writer = FileWriter(outFileName)
    output.forEach { writer.appendLine(it) }
    writer.close()
}