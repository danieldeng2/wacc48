package analyser

import ResourceWalker
import exceptions.SemanticsException
import exceptions.SyntaxException
import org.antlr.v4.runtime.CharStreams
import org.junit.Test
import runAnalyser

class AnalyserIntegrationTest {
    @Test
    fun validProgramsShouldNotProduceException() {
        ResourceWalker().walkDirectory("valid") { f ->
            startTest(f)
            runAnalyser(CharStreams.fromFileName(f.path))
            passTest()
        }
    }

    @Test
    fun syntaxErrorsShouldThrowSyntaxException() {
        ResourceWalker().walkDirectory("invalid/syntaxErr") { f ->
            startTest(f)
            try {
                runAnalyser(CharStreams.fromFileName(f.path))
                error("This program contains syntax error")
            } catch (e: SyntaxException) {
                passTest()
            }
        }
    }

    @Test
    fun semanticErrorsShouldThrowSemanticsException() {
        ResourceWalker().walkDirectory("invalid/semanticErr") { f ->
            startTest(f)
            try {
                runAnalyser(CharStreams.fromFileName(f.path))
                error("This program contains semantic error")
            } catch (e: SemanticsException) {
                passTest()
            }
        }
    }
}