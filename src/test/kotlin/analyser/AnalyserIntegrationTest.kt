package analyser

import ArmCompiler
import WalkDirectory
import analyser.exceptions.SemanticsException
import analyser.exceptions.SyntaxException
import org.junit.Test

class AnalyserIntegrationTest {

    @Test
    fun validProgramsShouldNotProduceException() {
        WalkDirectory("valid").run {
            ArmCompiler(it, it.parentFile, false).analyse()
        }
    }

    @Test
    fun syntaxErrorsShouldThrowSyntaxException() {
        WalkDirectory("invalid/syntaxErr").run { f ->
            try {
                ArmCompiler(f, f.parentFile, false).analyse()
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
                ArmCompiler(f, f.parentFile, false).analyse()
                error("This program contains semantic error")
            } catch (e: SemanticsException) {
                //Test passes
            }
        }
    }
}