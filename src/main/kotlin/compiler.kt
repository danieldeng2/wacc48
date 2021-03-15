import analyser.ASTGeneratorVisitor
import analyser.exceptions.SemanticsException
import analyser.exceptions.SyntaxException
import analyser.exceptions.ThrowingErrorListener
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import tree.SymbolTable
import tree.nodes.ASTNode
import java.io.File
import java.io.FileWriter
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

interface Application {
    fun start()
}

interface CompilerFormatter {

    fun compile(astNode: ASTNode): List<String>

    fun createExecutable(asmPath: String, execPath: String)

}

class WaccCompiler(
    val formatter: CompilerFormatter,
    val sourceFile: File,
    val outDir: File,
    val writeExecutable: Boolean
) : Application {

    lateinit var programNode: ASTNode
    lateinit var instructions: List<String>

    override fun start() {
        runAnalyserCatchError()
        instructions = formatter.compile(programNode)

        val srcNoExtension = sourceFile.name.removeSuffix(".wacc")
        val asmFile = File(outDir, "$srcNoExtension.s")
        writeToFile(instructions, asmFile.path)

        if (writeExecutable) {
            val execFile = File(outDir, srcNoExtension)
            formatter.createExecutable(asmFile.path, execFile.path)
        }

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

class ArmFormatter : CompilerFormatter {

    private val compiler = "arm-linux-gnueabi-gcc"
    private val architecture = "arm1176jzf-s"

    override fun compile(astNode: ASTNode): List<String> =
        CodeGeneratorVisitor(astNode).translateToArm()


    override fun createExecutable(asmPath: String, execPath: String) {
        ProcessBuilder(
            compiler,
            "-o",
            execPath,
            "-mcpu=$architecture",
            "-mtune=$architecture",
            asmPath
        ).start().waitFor(10, TimeUnit.SECONDS)
    }
}

class I386Formatter : CompilerFormatter {

    private val assembler = "nasm"
    private val linker = "gcc"

    override fun compile(astNode: ASTNode) =
        CodeGeneratorVisitor(astNode).translateTox86()


    override fun createExecutable(asmPath: String, execPath: String) {
        val objectFileName = "$execPath.o"
        ProcessBuilder(
            assembler,
            "-f",
            "elf32",
            "-o",
            objectFileName,
            asmPath
        ).start().waitFor(10, TimeUnit.SECONDS)

        ProcessBuilder(
            linker,
            "-m32",
            "-o",
            execPath,
            objectFileName
        ).start().waitFor(10, TimeUnit.SECONDS)
    }
}