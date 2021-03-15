package shell

import tree.nodes.expr.operators.BinaryOperator

open class ShellRunTimeException(s: String) : Exception(s)

class ShellArrayIndexOutOfBoundsException(s: String) : ShellRunTimeException("ArrayIndexOutOfBoundsError: $s")

fun checkIndexBounds(index: Int, arraySize: Int) {
    if (index < 0)
        throw ShellArrayIndexOutOfBoundsException("negative index $index")
    else if (index >= arraySize)
        throw ShellArrayIndexOutOfBoundsException("index $index is out of bounds for length $arraySize")
}

class ShellDivideByZeroException(s: String) : ShellRunTimeException("DivideByZeroException: $s")

class ShellIntegerOverflowException(s: String) : ShellRunTimeException("IntegerOverflowException $s")

fun detectIntegerOverflow(n1: Int, n2: Int, op: BinaryOperator) {
    if (n1 >= 0 && n2 >= 0) {
        when (op) {
            BinaryOperator.PLUS -> if ((n1 + n2) < 0) throw ShellIntegerOverflowException("")
            BinaryOperator.MINUS -> return
            BinaryOperator.MULTIPLY -> if ((n1 * n2) < 0) throw ShellIntegerOverflowException("")
            else -> return
        }
    } else if (n1 < 0 && n2 < 0) {
        when (op) {
            BinaryOperator.PLUS -> if ((n1 + n2) > 0) throw ShellIntegerOverflowException("")
            BinaryOperator.MINUS -> return
            BinaryOperator.MULTIPLY -> if ((n1 * n2) < 0) throw ShellIntegerOverflowException("")
            else -> return
        }
    } else if (n1 < 0 && n2 >= 0) {
        when (op) {
            BinaryOperator.PLUS -> return
            BinaryOperator.MINUS -> if ((n1 - n2) >= 0) throw ShellIntegerOverflowException("")
            BinaryOperator.MULTIPLY -> if ((n1 * n2) >= 0) throw ShellIntegerOverflowException("")
            else -> return
        }
    } else if (n1 >= 0 && n2 < 0) {
        when (op) {
            BinaryOperator.PLUS -> return
            BinaryOperator.MINUS -> if ((n1 - n2) <= 0) throw ShellIntegerOverflowException("")
            BinaryOperator.MULTIPLY -> if ((n1 * n2) >= 0) throw ShellIntegerOverflowException("")
            else -> return
        }
    }
}

class ShellNullDereferenceError(s: String) : ShellRunTimeException("ShellNullDereferenceError $s")