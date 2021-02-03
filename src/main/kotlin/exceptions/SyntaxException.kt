package exceptions

import kotlin.system.exitProcess

class SyntaxException(message: String) : Exception(message) {
    init {
        println("Syntax Error : $message")
        exitProcess(100)
    }
}