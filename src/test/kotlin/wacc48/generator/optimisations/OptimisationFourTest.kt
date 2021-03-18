package wacc48.generator.optimisations

import org.junit.Test

class OptimisationFourTest {

    @Test
    fun arraysShouldMatchReferenceOutputOptimisation4() {
        checkAllMatches("valid/array", 4)
    }

    @Test
    fun basicProgramsShouldMatchReferenceOutputOptimisation4() {
        checkAllMatches("valid/basic", 4)
    }

    @Test
    fun expressionsShouldMatchReferenceOutputOptimisation4() {
        checkAllMatches("valid/expressions", 4)
    }

    @Test
    fun functionShouldMatchReferenceOutputOptimisation4() {
        checkAllMatches("valid/function", 4)
    }

    @Test
    fun ifStatementsShouldMatchReferenceOutputOptimisation4() {
        checkAllMatches("valid/if", 4)
    }

    @Test
    fun inputOutputShouldMatchReferenceOutputOptimisation4() {
        checkAllMatches("valid/IO", 4)
    }

    @Test
    fun pairsShouldMatchReferenceOutputOptimisation4() {
        checkAllMatches("valid/pairs", 4)
    }

    @Test
    fun runtimeErrShouldMatchReferenceOutputOptimisation4() {
        checkAllMatches("valid/runtimeErr", 4)
    }

    @Test
    fun scopeTestsShouldMatchReferenceOutputOptimisation4() {
        checkAllMatches("valid/scope", 4)
    }

    @Test
    fun sequencesShouldMatchReferenceOutputOptimisation4() {
        checkAllMatches("valid/sequence", 4)
    }

    @Test
    fun variablesShouldMatchReferenceOutputOptimisation4() {
        checkAllMatches("valid/variables", 4)
    }

    @Test
    fun whileLoopsShouldMatchReferenceOutputOptimisation4() {
        checkAllMatches("valid/while", 4)
    }

}