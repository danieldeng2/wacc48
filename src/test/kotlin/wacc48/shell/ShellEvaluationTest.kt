package wacc48.shell

import WalkDirectory
import org.junit.Test

class ShellEvaluationTest {
    @Test
    fun arraysShouldMatchReferenceOutput() {
        checkAllMatches("shell/valid/array")
    }

    @Test
    fun basicProgramsShouldMatchReferenceOutput() {
        checkAllMatches("valid/basic")
    }

    @Test
    fun expressionsShouldMatchReferenceOutput() {
        checkAllMatches("valid/expressions")
    }

    @Test
    fun functionShouldMatchReferenceOutput() {
        checkAllMatches("shell/valid/function")
    }

    @Test
    fun ifStatementsShouldMatchReferenceOutput() {
        checkAllMatches("valid/if")
    }

    @Test
    fun inputOutputShouldMatchReferenceOutput() {
        checkAllMatches("valid/IO")
    }

    @Test
    fun pairsShouldMatchReferenceOutput() {
        checkAllMatches("shell/valid/pairs")
    }

    @Test
    fun arrayOutOfBoundsErrorsCaught() {
        WalkDirectory("valid/runtimeErr/arrayOutOfBounds").run {
            try {
                runFileInShell(it, evaluateCode = true)
                error("This program contains array out of bounds error")
            } catch (e: ShellArrayIndexOutOfBoundsException) {
                //Test passes
            }
        }
    }

    @Test
    fun divideByZeroErrorsCaught() {
        WalkDirectory("valid/runtimeErr/divideByZero").run {
            try {
                runFileInShell(it, evaluateCode = true)
                error("This program contains a divide by zeroes error")
            } catch (e: ShellDivideByZeroException) {
                //Test passes
            }
        }
    }

    @Test
    fun integerOverflowErrorsCaught() {
        WalkDirectory("valid/runtimeErr/integerOverflow").run {
            try {
                runFileInShell(it, evaluateCode = true)
                error("This program contains a integer overflow error")
            } catch (e: ShellIntegerOverflowException) {
                //Test passes
            }
        }
    }

    @Test
    fun nullDereferenceErrorsCaught() {
        WalkDirectory("valid/runtimeErr/nullDereference").run {
            try {
                runFileInShell(it, evaluateCode = true)
                error("This program contains a null dereference error")
            } catch (e: ShellNullDereferenceError) {
                //Test passes
            }
        }
    }

    @Test
    fun scopeTestsShouldMatchReferenceOutput() {
        checkAllMatches("shell/valid/scope")
    }

    @Test
    fun sequencesShouldMatchReferenceOutput() {
        checkAllMatches("shell/valid/sequence")
    }

    @Test
    fun variablesShouldMatchReferenceOutput() {
        checkAllMatches("valid/variables")
    }

    @Test
    fun whileLoopsShouldMatchReferenceOutput() {
        checkAllMatches("valid/while")
    }
}