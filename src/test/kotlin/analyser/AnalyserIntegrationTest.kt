package analyser

import ArmFormatter
import WaccCompiler
import WalkDirectory
import analyser.exceptions.SemanticsException
import analyser.exceptions.SyntaxException
import org.junit.Test

class AnalyserIntegrationTest {

    @Test
    fun validProgramsShouldNotProduceException() {
        WalkDirectory("valid").run {
            WaccCompiler(ArmFormatter(), it, it.parentFile).analyse()
        }
    }

    @Test
    fun syntaxErrorsShouldThrowSyntaxException() {
        WalkDirectory("invalid/syntaxErr").run { f ->
            try {
                WaccCompiler(ArmFormatter(), f, f.parentFile).analyse()
                error("This program contains syntax error")
            } catch (e: SyntaxException) {
                //Test passes
            }
        }
    }

    @Test
    fun semanticErrorsShouldThrowSemanticsException() {
        WalkDirectory("invalid/semanticErr").run { f ->
            try {
                WaccCompiler(ArmFormatter(), f, f.parentFile).analyse()
                error("This program contains semantic error")
            } catch (e: SemanticsException) {
                //Test passes
            }
        }
    }
}