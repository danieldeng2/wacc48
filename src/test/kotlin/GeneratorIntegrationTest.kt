import org.antlr.v4.runtime.CharStreams
import org.junit.Test
import reference.EmulatorResult
import reference.RefCompiler
import reference.RefEmulator
import java.io.File
import kotlin.io.createTempFile
import java.io.FileWriter
import kotlin.test.assertEquals

class GeneratorIntegrationTest {
    var totalTests = 0
    var passedTests = 0

    private fun compilerPipeline(path: String, stdin: String): EmulatorResult {
        val input = CharStreams.fromFileName(path)
        val pNode = runAnalyser(input)
        val assembly = runGenerator(pNode, path)

        val assemFile = File("tmp.s")
        val writer = FileWriter(assemFile)
        assembly.forEach { writer.write(it + System.lineSeparator()) }
        writer.close()

        val res = RefEmulator(assemFile).execute(stdin)
        assemFile.delete()

        return res
    }

    private fun referencePipeline(path: String, stdin: String): EmulatorResult {
        val assembly = RefCompiler(File(path)).run()

        val assemFile = File("tmp_ref.s")
        val writer = FileWriter(assemFile)
        assembly.forEach { writer.write(it + System.lineSeparator()) }
        writer.close()

        val res = RefEmulator(assemFile).execute(stdin)
        assemFile.delete()

        return res
    }


    @Test
    fun validProgramsShouldNotProduceException() {
        val dir = File(object {}.javaClass.getResource("valid/basic").file)

        dir.walk().forEach { f ->
            if (f.isFile) {
                totalTests++
                println("${f.path}:")
                val compilerResult = compilerPipeline(f.path, "")
                val referenceResult = referencePipeline(f.path, "")

                when {
                    compilerResult.emulatorOut != referenceResult.emulatorOut -> {
                        println("Expected ${referenceResult.emulatorOut} but got ${compilerResult.emulatorOut}")
                    }
                    compilerResult.emulatorExit != referenceResult.emulatorExit -> {
                        println("Expected exit code ${referenceResult.emulatorExit} but got ${compilerResult.emulatorExit}")
                    }
                    else -> {
                        passedTests++
                    }
                }
            }
        }
        println("$passedTests/$totalTests tests passed for valid products")
        assertEquals(totalTests, passedTests)
    }
}