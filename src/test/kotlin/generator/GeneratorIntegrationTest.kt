package generator

import org.junit.Test


class GeneratorIntegrationTest {

    //TODO advanced

    @Test
    fun arraysShouldMatchReferenceOutput() {
        checkAllMatches("valid/array")
    }

    @Test
    fun basicProgramsShouldMatchReferenceOutput() {
        checkAllMatches("valid/basic")
    }

    @Test
    fun expressionsShouldMatchReferenceOutput() {
        checkAllMatches("valid/expressions")
    }

    //TODO function

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
        checkAllMatches("valid/pairs")
    }

    //TODO runtimeErr

    //TODO scope

    @Test
    fun sequencesShouldMatchReferenceOutput() {
        checkAllMatches("valid/sequence")
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
