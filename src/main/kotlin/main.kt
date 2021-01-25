import org.antlr.v4.runtime.*

import antlr.*

fun main(args: Array<String>) {
    val input: CharStream = CharStreams.fromStream(System.`in`)
    val lexer = BasicLexer(input)
    val tokens = CommonTokenStream(lexer)
    val parser = BasicParser(tokens)
    val tree = parser.prog()

    println(tree.toStringTree(parser))
}