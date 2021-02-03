package analyser

class ErrorReporter {

    val semanticErrors = mutableListOf<String>()
    val syntaxErrors = mutableListOf<String>()

    fun logError(error: String, type: ErrorType) = when (type) {
        ErrorType.SYNTAX -> syntaxErrors.add(error)
        ErrorType.SEMANTIC -> semanticErrors.add(error)
    }
}

enum class ErrorType {
    SYNTAX,
    SEMANTIC
}
