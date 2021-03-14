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

abstract class Compiler(
    private val sourceFile: File,
    private val outDir: File,
    private val writeExecutable: Boolean
) :
    Application {

    lateinit var programNode: ASTNode
    lateinit var instructions: List<String>

    abstract fun createExecutable(asmFileName: String, execFileName: String)

    abstract fun compile()

    override fun start() {
        runAnalyserCatchError()
        compile()

        val srcNoExtension = sourceFile.name.removeSuffix(".wacc")
        val asmFile = File(outDir, "$srcNoExtension.s")
        writeToFile(instructions, asmFile.path)

        if (writeExecutable) {
            val execFile = File(outDir, srcNoExtension)
            createExecutable(asmFile.path, execFile.path)
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

class ArmCompiler(
    sourceFile: File,
    outDir: File,
    writeExecutable: Boolean
) : Compiler(sourceFile, outDir, writeExecutable) {

    private val compiler = "arm-linux-gnueabi-gcc"
    private val architecture = "arm1176jzf-s"


    override fun compile() {
        instructions = CodeGeneratorVisitor(programNode).translateToArm()
    }

    override fun createExecutable(asmFileName: String, execFileName: String) {
        ProcessBuilder(
            compiler,
            "-o",
            execFileName,
            "-mcpu=$architecture",
            "-mtune=$architecture",
            asmFileName
        ).start().waitFor(10, TimeUnit.SECONDS)
    }
}

class I386Compiler(
    sourceFile: File,
    outDir: File,
    writeExecutable: Boolean
) : Compiler(sourceFile, outDir, writeExecutable) {

    private val assembler = "nasm"
    private val linker = "gcc"

    override fun compile() {
        instructions = CodeGeneratorVisitor(programNode).translateTox86()
    }

    override fun createExecutable(asmFileName: String, execFileName: String) {
        val objectFileName = "$execFileName.o"
        ProcessBuilder(
            assembler,
            "-f",
            "elf32",
            "-o",
            objectFileName,
            asmFileName
        ).start().waitFor(10, TimeUnit.SECONDS)

        ProcessBuilder(
            linker,
            "-m32",
            "-o",
            execFileName,
            objectFileName
        ).start().waitFor(10, TimeUnit.SECONDS)
    }
}