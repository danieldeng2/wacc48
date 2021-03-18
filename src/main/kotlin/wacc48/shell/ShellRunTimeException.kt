package wacc48.shell

import wacc48.tree.nodes.expr.operators.operation.binary.BinaryOperation
import wacc48.tree.nodes.expr.operators.operation.binary.PlusOperation
import wacc48.tree.nodes.expr.operators.operation.binary.MinusOperation
import wacc48.tree.nodes.expr.operators.operation.binary.MultiplyOperation

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

fun detectIntegerOverflow(n1: Int, n2: Int, op: BinaryOperation) {
    if (n1 >= 0 && n2 >= 0) {
        when (op) {
            is PlusOperation -> if ((n1 + n2) < 0) throw ShellIntegerOverflowException("")
            is MinusOperation -> return
            is MultiplyOperation -> if ((n1 * n2) < 0) throw ShellIntegerOverflowException("")
            else -> return
        }
    } else if (n1 < 0 && n2 < 0) {
        when (op) {
            is PlusOperation -> if ((n1 + n2) > 0) throw ShellIntegerOverflowException("")
            is MinusOperation -> return
            is MultiplyOperation -> if ((n1 * n2) < 0) throw ShellIntegerOverflowException("")
            else -> return
        }
    } else if (n1 < 0 && n2 >= 0) {
        when (op) {
            is PlusOperation -> return
            is MinusOperation -> if ((n1 - n2) >= 0) throw ShellIntegerOverflowException("")
            is MultiplyOperation -> if ((n1 * n2) >= 0) throw ShellIntegerOverflowException("")
            else -> return
        }
    } else if (n1 >= 0 && n2 < 0) {
        when (op) {
            is PlusOperation -> return
            is MinusOperation -> if ((n1 - n2) <= 0) throw ShellIntegerOverflowException("")
            is MultiplyOperation-> if ((n1 * n2) >= 0) throw ShellIntegerOverflowException("")
            else -> return
        }
    }
}

class ShellNullDereferenceError(s: String) : ShellRunTimeException("ShellNullDereferenceError $s")