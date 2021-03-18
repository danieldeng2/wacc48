package wacc48

import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import wacc48.analyser.ASTGeneratorVisitor
import wacc48.analyser.exceptions.Issue
import wacc48.analyser.exceptions.IssueType
import wacc48.analyser.exceptions.ParserException
import wacc48.analyser.exceptions.ThrowingErrorListener
import wacc48.analyser.optimisations.ConstantEvaluationVisitor
import wacc48.analyser.optimisations.ControlFlowVisitor
import wacc48.antlr.WACCLexer
import wacc48.antlr.WACCParser
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.ProgNode
import wacc48.tree.nodes.function.FuncNode
import java.io.File
import java.io.FileWriter
import kotlin.system.exitProcess

fun runAnalyser(
    input: CharStream,
    issues: MutableList<Issue>,
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
        funTable = funTable,
        issues = issues
    )

    if (issues.isEmpty()) {
        // Constant Evaluation Analysis
        ConstantEvaluationVisitor.visitNode(programNode)

        // Control Flow Analysis
        ControlFlowVisitor.visitNode(programNode)
    }

    return programNode
}

fun runAnalyserCatchError(sourceFile: File): ProgNode {
    val issues = mutableListOf<Issue>()
    val programNode: ASTNode

    try {
        programNode = runAnalyser(CharStreams.fromPath(sourceFile.toPath()), issues)
    } catch (e: ParserException) {
        println("Syntax Error: ${e.message}")
        exitProcess(100)
    }

    checkAnalyserIssues(issues)

    return programNode as ProgNode
}


fun checkAnalyserIssues(issues: MutableList<Issue>) {
    val syntaxIssues = issues.filter { it.type == IssueType.SYNTAX }

    if (syntaxIssues.isNotEmpty()) {
        syntaxIssues.forEach {
            System.err.println(it)
        }
        exitProcess(100)
    }

    if (issues.isNotEmpty()) {
        issues.forEach {
            System.err.println(it)
        }
        exitProcess(200)
    }
}


fun writeToFile(output: List<String>, outFileName: String) {
    val writer = FileWriter(outFileName)
    output.forEach { writer.appendLine(it) }
    writer.close()
}