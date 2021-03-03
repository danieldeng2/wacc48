import analyser.ASTGeneratorVisitor
import datastructures.SymbolTable
import datastructures.nodes.ASTNode
import analyser.exceptions.SemanticsException
import analyser.exceptions.SyntaxException
import analyser.exceptions.ThrowingErrorListener
import datastructures.nodes.ProgNode
import generator.translator.CodeGeneratorVisitor
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CommonTokenStream
import org.stringtemplate.v4.compiler.CodeGenerator
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

fun runGenerator(pNode: ASTNode): List<String> {
//    val translatorCtx = TranslatorContext()
//    val programInstructions = pNode.translate(translatorCtx)
//
//    return programInstructions.map { it.toString() }
    val codeGen = CodeGeneratorVisitor(pNode as ProgNode)
    return codeGen.translate().map { it.toString() }
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