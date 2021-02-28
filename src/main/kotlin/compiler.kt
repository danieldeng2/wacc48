import analyser.ASTGenerator
import analyser.SymbolTable
import analyser.nodes.ASTNode
import exceptions.SemanticsException
import exceptions.SyntaxException
import exceptions.ThrowingErrorListener
import generator.translator.TranslatorContext
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
    val programNode = ASTGenerator().visitProg(parser.prog())
    programNode.validate(
        st = SymbolTable(null),
        funTable = mutableMapOf()
    )

    return programNode
}

fun runGenerator(pNode: ASTNode): List<String> {
    val translatorCtx = TranslatorContext()
    val programInstructions = pNode.translate(translatorCtx)

    return programInstructions.map { it.toString() }
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