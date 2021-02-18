import exceptions.SemanticsException
import exceptions.SyntaxException
import org.antlr.v4.runtime.CharStreams
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class AnalyserIntegrationTest {
    var totalTests = 0
    var passedTests = 0

    @Test
    fun validProgramsShouldNotProduceException() {
        val dir = File(object {}.javaClass.getResource("valid").file)

        dir.walk().forEach { f ->
            if (f.isFile) {
                val input = CharStreams.fromFileName(f.path)
                println("${f.path}:")
                try {
                    runAnalyser(input)
                    println("PASSED")
                    passedTests++
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                totalTests++
            }
        }
        println("$passedTests/$totalTests tests passed for valid products")
        assertEquals(totalTests, passedTests)
    }

    @Test
    fun syntaxErrorsShouldThrowSyntaxException() {
        val dir = File(object {}.javaClass.getResource("invalid/syntaxErr").file)

        dir.walk().forEach { f ->
            if (f.isFile) {
                val input = CharStreams.fromFileName(f.path)
                println("${f.path}:")
                try {
                    runAnalyser(input)
                    error("This program contains syntax error")
                } catch (e: SyntaxException) {
                    passedTests++
                    println("PASSED")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                totalTests++
            }
        }
        println("$passedTests/$totalTests syntax errors catched")
        assertEquals(totalTests, passedTests)
    }

    @Test
    fun semanticErrorsShouldThrowSemanticsException() {
        val dir = File(object {}.javaClass.getResource("invalid/semanticErr").file)

        dir.walk().forEach { f ->
            if (f.isFile) {
                val input = CharStreams.fromFileName(f.path)
                println("${f.path}:")
                try {
                    runAnalyser(input)
                    error("This program contains semantic error")
                } catch (e: SemanticsException) {
                    passedTests++
                    println("PASSED")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                totalTests++
            }
        }
        println("$passedTests/$totalTests SemanticsException errors catched")
        assertEquals(totalTests, passedTests)
    }

}