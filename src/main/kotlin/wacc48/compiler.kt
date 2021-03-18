package wacc48

import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import wacc48.analyser.ASTGeneratorVisitor
import wacc48.analyser.exceptions.Issue
import wacc48.analyser.exceptions.IssueType
import wacc48.analyser.exceptions.ParserException
import wacc48.analyser.exceptions.ThrowingErrorListener
import wacc48.analyser.optimisations.*
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

fun runOptimiser(programNode: ASTNode, optimiseLevel: Int) {
    if(optimiseLevel == 0)
        return
    do {
        var optimisations = 0

        // Constant Evaluation Analysis
        optimisations += ConstantEvaluationVisitor.optimise(programNode)
        if (optimiseLevel == 1)
            continue

        // Control Flow Analysis
        optimisations += ControlFlowVisitor.optimise(programNode)
        if (optimiseLevel == 2)
            continue

        // Constant Propagation
        val funcConstants = ConstantIdentifierVisitor.identifyConstants(programNode)

        funcConstants.forEach{ func ->

            optimisations += PropagationVisitor.optimise(func)

            // Dead Code Elimination
            if (optimiseLevel == 4){
                optimisations += DeadCodeVisitor.optimise(
                    node = func.key,
                    inactiveVariables = func.value.keys
                )
            }
        }
    } while (optimisations > 0)
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