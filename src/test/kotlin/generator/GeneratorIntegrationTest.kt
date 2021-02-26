package generator
import org.junit.Test


class GeneratorIntegrationTest {
    @Test
    fun basicProgramsShouldMatchReferenceOutput() {
        checkAllMatches("valid/basic")
    }

    @Test
    fun sequencesShouldMatchReferenceOutput() {
        checkAllMatches("valid/sequence")
    }

    @Test
    fun variablesShouldMatchReferenceOutput() {
        checkAllMatches("valid/variables")
    }

    @Test
    fun ifStatementsShouldMatchReferenceOutput() {
        checkAllMatches("valid/if")
    }

    @Test
    fun expressionsShouldMatchReferenceOutput() {
        checkAllMatches("valid/expressions")
    }

    @Test
    fun whileLoopsShouldMatchReferenceOutput() {
        checkAllMatches("valid/while")
    }
}
