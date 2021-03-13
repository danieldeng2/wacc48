package generator.x86

import org.junit.Test

class GeneratorIntegrationx86Test {

    @Test
    fun arraysShouldMatchReferenceOutput() {
        runAllTests("valid/array")
    }

    @Test
    fun basicProgramsShouldMatchReferenceOutput() {
        runAllTests("valid/basic")
    }

    @Test
    fun expressionsShouldMatchReferenceOutput() {
        runAllTests("valid/expressions")
    }

    @Test
    fun functionShouldMatchReferenceOutput() {
        runAllTests("valid/function/")
    }

    @Test
    fun ifStatementsShouldMatchReferenceOutput() {
        runAllTests("valid/if")
    }

    @Test
    fun inputOutputShouldMatchReferenceOutput() {
        runAllTests("valid/IO")
    }

    @Test
    fun pairsShouldMatchReferenceOutput() {
        runAllTests("valid/pairs")
    }

    @Test
    fun runtimeErrShouldMatchReferenceOutput() {
        runAllTests("valid/runtimeErr")
    }

    @Test
    fun scopeTestsShouldMatchReferenceOutput() {
        runAllTests("valid/scope")
    }

    @Test
    fun sequencesShouldMatchReferenceOutput() {
        runAllTests("valid/sequence")
    }

    @Test
    fun variablesShouldMatchReferenceOutput() {
        runAllTests("valid/variables")
    }

    @Test
    fun whileLoopsShouldMatchReferenceOutput() {
        runAllTests("valid/while")
    }

}
