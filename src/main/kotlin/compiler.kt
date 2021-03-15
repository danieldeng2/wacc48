import analyser.ASTGeneratorVisitor
import analyser.exceptions.SemanticsException
import analyser.exceptions.SyntaxException
import analyser.exceptions.ThrowingErrorListener
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CommonTokenStream
import tree.SymbolTable
import tree.nodes.ASTNode
import tree.nodes.function.FuncNode
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

fun runAnalyserCatchError(
    input: CharStream,
    st: SymbolTable = SymbolTable(null),
    funTable: MutableMap<String, FuncNode> = mutableMapOf()
): ASTNode =
    try {
        runAnalyser(input, st, funTable)
    } catch (e: SyntaxException) {
        println("Syntax Error: ${e.message}")
        exitProcess(100)
    } catch (e: SemanticsException) {
        println("Semantics Error: ${e.message}")
        exitProcess(200)
    }

fun runAnalyserPrintError(
    input: CharStream,
    st: SymbolTable = SymbolTable(null),
    funTable: MutableMap<String, FuncNode> = mutableMapOf()
): ASTNode? =
    try {
        runAnalyser(input, st, funTable)
    } catch (e: SyntaxException) {
        println("Syntax Error: ${e.message}")
        null
    } catch (e: SemanticsException) {
        println("Semantics Error: ${e.message}")
        null
    }