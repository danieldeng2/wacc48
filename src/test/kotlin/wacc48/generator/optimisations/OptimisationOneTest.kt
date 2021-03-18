package wacc48.generator.optimisations

import org.junit.Test

class OptimisationOneTest {

    @Test
    fun arraysShouldMatchReferenceOutputOptimisation1() {
        checkAllMatches("valid/array", 1)
    }

    @Test
    fun basicProgramsShouldMatchReferenceOutputOptimisation1() {
        checkAllMatches("valid/basic", 1)
    }

    @Test
    fun expressionsShouldMatchReferenceOutputOptimisation1() {
        checkAllMatches("valid/expressions", 1)
    }

    @Test
    fun functionShouldMatchReferenceOutputOptimisation1() {
        checkAllMatches("valid/function", 1)
    }

    @Test
    fun ifStatementsShouldMatchReferenceOutputOptimisation1() {
        checkAllMatches("valid/if", 1)
    }

    @Test
    fun inputOutputShouldMatchReferenceOutputOptimisation1() {
        checkAllMatches("valid/IO", 1)
    }

    @Test
    fun pairsShouldMatchReferenceOutputOptimisation1() {
        checkAllMatches("valid/pairs", 1)
    }

    @Test
    fun runtimeErrShouldMatchReferenceOutputOptimisation1() {
        checkAllMatches("valid/runtimeErr", 1)
    }

    @Test
    fun scopeTestsShouldMatchReferenceOutputOptimisation1() {
        checkAllMatches("valid/scope", 1)
    }

    @Test
    fun sequencesShouldMatchReferenceOutputOptimisation1() {
        checkAllMatches("valid/sequence", 1)
    }

    @Test
    fun variablesShouldMatchReferenceOutputOptimisation1() {
        checkAllMatches("valid/variables", 1)
    }

    @Test
    fun whileLoopsShouldMatchReferenceOutputOptimisation1() {
        checkAllMatches("valid/while", 1)
    }

}