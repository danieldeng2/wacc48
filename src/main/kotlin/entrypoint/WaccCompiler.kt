package entrypoint

import WACCLexer
import WACCParser
import analyser.ASTGeneratorVisitor
import analyser.exceptions.SemanticsException
import analyser.exceptions.SyntaxException
import analyser.exceptions.ThrowingErrorListener
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import tree.SymbolTable
import tree.nodes.ASTNode
import java.io.File
import java.io.FileWriter
import kotlin.system.exitProcess

class WaccCompiler(
    val formatter: CompilerFormatter,
    val sourceFile: File,
    val outDir: File
) {

    lateinit var programNode: ASTNode
    lateinit var instructions: List<String>

    fun start() {
        runAnalyserCatchError()
        instructions = formatter.compile(programNode)

        val srcNoExtension = sourceFile.name.removeSuffix(".wacc")
        val asmFile = File(outDir, "$srcNoExtension.s")
        writeToFile(instructions, asmFile.path)
    }

    fun analyse() {
        val input = CharStreams.fromPath(sourceFile.toPath())

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
        programNode = ASTGeneratorVisitor().visitProg(parser.prog())
        programNode.validate(
            st = SymbolTable(null),
            funTable = mutableMapOf()
        )
    }

    fun assembleExecutable(asmFile: File, execFile: File) {
        formatter.createExecutable(asmFile.path, execFile.path)
    }

    private fun runAnalyserCatchError() =
        try {
            analyse()
        } catch (e: SyntaxException) {
            println("Syntax Error: ${e.message}")
            exitProcess(100)
        } catch (e: SemanticsException) {
            println("Semantics Error: ${e.message}")
            exitProcess(200)
        }

    private fun writeToFile(output: List<String>, outFileName: String) {
        val writer = FileWriter(outFileName)
        output.forEach { writer.appendLine(it) }
        writer.close()
    }
}