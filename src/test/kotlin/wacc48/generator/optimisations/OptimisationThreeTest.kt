package wacc48.generator.optimisations

import org.junit.Test

class OptimisationThreeTest {

    @Test
    fun arraysShouldMatchReferenceOutputOptimisation3() {
        checkAllMatches("valid/array", 3)
    }

    @Test
    fun basicProgramsShouldMatchReferenceOutputOptimisation3() {
        checkAllMatches("valid/basic", 3)
    }

    @Test
    fun expressionsShouldMatchReferenceOutputOptimisation3() {
        checkAllMatches("valid/expressions", 3)
    }

    @Test
    fun functionShouldMatchReferenceOutputOptimisation3() {
        checkAllMatches("valid/function", 3)
    }

    @Test
    fun ifStatementsShouldMatchReferenceOutputOptimisation3() {
        checkAllMatches("valid/if", 3)
    }

    @Test
    fun inputOutputShouldMatchReferenceOutputOptimisation3() {
        checkAllMatches("valid/IO", 3)
    }

    @Test
    fun pairsShouldMatchReferenceOutputOptimisation3() {
        checkAllMatches("valid/pairs", 3)
    }

    @Test
    fun runtimeErrShouldMatchReferenceOutputOptimisation3() {
        checkAllMatches("valid/runtimeErr", 3)
    }

    @Test
    fun scopeTestsShouldMatchReferenceOutputOptimisation3() {
        checkAllMatches("valid/scope", 3)
    }

    @Test
    fun sequencesShouldMatchReferenceOutputOptimisation3() {
        checkAllMatches("valid/sequence", 3)
    }

    @Test
    fun variablesShouldMatchReferenceOutputOptimisation3() {
        checkAllMatches("valid/variables", 3)
    }

    @Test
    fun whileLoopsShouldMatchReferenceOutputOptimisation3() {
        checkAllMatches("valid/while", 3)
    }

}