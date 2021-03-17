package wacc48.analyser

import WalkDirectory
import org.antlr.v4.runtime.CharStreams
import org.junit.Test
import wacc48.analyser.exceptions.Issue
import wacc48.analyser.exceptions.IssueType
import wacc48.analyser.exceptions.ParserException
import wacc48.runAnalyser
import kotlin.test.fail

class AnalyserIntegrationTest {

    @Test
    fun validProgramsShouldNotProduceException() {
        WalkDirectory("valid").run { f ->
            val issues = mutableListOf<Issue>()
            runAnalyser(CharStreams.fromFileName(f.path), issues)

            issues.forEach {
                System.err.println(it)
            }

            if (issues.isNotEmpty())
                fail("This program should be error free")
        }
    }

    @Test
    fun syntaxErrorsShouldThrowSyntaxException() {
        WalkDirectory("invalid/syntaxErr").run { f ->
            val issues = mutableListOf<Issue>()
            try {
                runAnalyser(CharStreams.fromFileName(f.path), issues)
                if (issues.none { it.type == IssueType.SYNTAX })
                    fail("This program contains syntax error")
            } catch (e: ParserException) {
                // Tests pass
            }
        }
    }

    @Test
    fun semanticErrorsShouldThrowSemanticsException() {
        WalkDirectory("invalid/semanticErr").run { f ->
            val issues = mutableListOf<Issue>()
            runAnalyser(CharStreams.fromFileName(f.path), issues)
            if (issues.none { it.type == IssueType.SEMANTICS })
                fail("This program contains semantics error")
        }
    }
}