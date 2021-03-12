package shell

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

    //@Test
    //fun functionShouldMatchReferenceOutput() {
    //    checkAllMatches("valid/function")
    //}

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

    //@Test
    //fun runtimeErrShouldMatchReferenceOutput() {
    //    checkAllMatches("valid/runtimeErr")
    //}

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