package wacc48.generator.optimisations

import org.junit.Test

class OptimisationTwoTest {

    @Test
    fun arraysShouldMatchReferenceOutputOptimisation2() {
        checkAllMatches("valid/array", 2)
    }

    @Test
    fun basicProgramsShouldMatchReferenceOutputOptimisation2() {
        checkAllMatches("valid/basic", 2)
    }

    @Test
    fun expressionsShouldMatchReferenceOutputOptimisation2() {
        checkAllMatches("valid/expressions", 2)
    }

    @Test
    fun functionShouldMatchReferenceOutputOptimisation2() {
        checkAllMatches("valid/function", 2)
    }

    @Test
    fun ifStatementsShouldMatchReferenceOutputOptimisation2() {
        checkAllMatches("valid/if", 2)
    }

    @Test
    fun inputOutputShouldMatchReferenceOutputOptimisation2() {
        checkAllMatches("valid/IO", 2)
    }

    @Test
    fun pairsShouldMatchReferenceOutputOptimisation2() {
        checkAllMatches("valid/pairs", 2)
    }

    @Test
    fun runtimeErrShouldMatchReferenceOutputOptimisation2() {
        checkAllMatches("valid/runtimeErr", 2)
    }

    @Test
    fun scopeTestsShouldMatchReferenceOutputOptimisation2() {
        checkAllMatches("valid/scope", 2)
    }

    @Test
    fun sequencesShouldMatchReferenceOutputOptimisation2() {
        checkAllMatches("valid/sequence", 2)
    }

    @Test
    fun variablesShouldMatchReferenceOutputOptimisation2() {
        checkAllMatches("valid/variables", 2)
    }

    @Test
    fun whileLoopsShouldMatchReferenceOutputOptimisation2() {
        checkAllMatches("valid/while", 2)
    }

}