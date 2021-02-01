import org.antlr.v4.runtime.*

import antlr.*

fun main(args: Array<String>) {
    val input: CharStream = CharStreams.fromStream(System.`in`)
    val lexer = WACCLexer(input)
    val tokens = CommonTokenStream(lexer)
    val parser = WACCParser(tokens)
    val tree = parser.prog()

    val visitor = ParserVisitor()
    visitor.visit(tree)

}