import analyser.ASTGeneratorVisitor
import analyser.exceptions.SemanticsException
import analyser.exceptions.SyntaxException
import analyser.exceptions.ThrowingErrorListener
import tree.SymbolTable
import tree.nodes.ASTNode
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CommonTokenStream
import kotlin.system.exitProcess

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
    val programNode = ASTGeneratorVisitor().visitProg(parser.prog())
    programNode.validate(
        st = SymbolTable(null),
        funTable = mutableMapOf()
    )

    return programNode
}

fun runGenerator(pNode: ASTNode, armAssembly: Boolean): List<String> {
    val codeGen = CodeGeneratorVisitor(pNode)
    return when (armAssembly) {
        true -> codeGen.translateToArm()
        false -> codeGen.translateTox86()
    }
}

fun runAnalyserCatchError(input: CharStream): ASTNode =
    try {
        runAnalyser(input)
    } catch (e: SyntaxException) {
        println("Syntax Error: ${e.message}")
        exitProcess(100)
    } catch (e: SemanticsException) {
        println("Semantics Error: ${e.message}")
        exitProcess(200)
    }