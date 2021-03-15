package wacc48.analyser

import WalkDirectory
import org.antlr.v4.runtime.CharStreams
import org.junit.Test
import wacc48.analyser.exceptions.SemanticsException
import wacc48.analyser.exceptions.SyntaxException
import wacc48.runAnalyser

class AnalyserIntegrationTest {

    @Test
    fun validProgramsShouldNotProduceException() {
        WalkDirectory("valid").run {
            runAnalyser(CharStreams.fromFileName(it.path))
        }
    }

    @Test
    fun syntaxErrorsShouldThrowSyntaxException() {
        WalkDirectory("invalid/syntaxErr").run { f ->
            try {
                runAnalyser(CharStreams.fromFileName(f.path))
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
                runAnalyser(CharStreams.fromFileName(f.path))
                error("This program contains semantic error")
            } catch (e: SemanticsException) {
                //Test passes
            }
        }
    }
}