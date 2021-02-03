package exceptions

import kotlin.system.exitProcess

class SemanticsException(message: String) : Exception(message) {
    init {
        println("Semantics Error : $message")
        exitProcess(200)
    }
}