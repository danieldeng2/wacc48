import org.antlr.v4.runtime.CharStreams
import org.junit.Test
import reference.EmulatorResult
import reference.RefCompiler
import reference.RefEmulator
import java.io.File
import java.io.FileWriter
import kotlin.test.assertEquals

class GeneratorIntegrationTest {
    var totalTests = 0
    var passedTests = 0

    @Test
    fun basicProgramsShouldMatchReferenceOutput() {
        checkAllMatches("valid/basic")
    }

    private fun checkAllMatches(label: String) {
        val dir = File(object {}.javaClass.getResource(label).file)
        dir.walk().forEach { f ->
            if (f.isFile) {
                totalTests++
                println("${f.path}:")
                try {
                    val compilerResult = compilerPipeline(f.path)
                    val referenceResult = referencePipeline(f.path)
                    when {
                        compilerResult.emulatorOut != referenceResult.emulatorOut ->
                            println("Expected ${referenceResult.emulatorOut} but got ${compilerResult.emulatorOut}")
                        compilerResult.emulatorExit != referenceResult.emulatorExit ->
                            println("Expected exit code ${referenceResult.emulatorExit} but got ${compilerResult.emulatorExit}")
                        else ->
                            passedTests++
                    }
                } catch (e: NotImplementedError) {
                    e.printStackTrace()
                }

            }
        }
        println("$passedTests/$totalTests tests passed for programs in $label")
        assertEquals(totalTests, passedTests)
    }

    private fun compilerPipeline(
        path: String,
        stdin: String = ""
    ): EmulatorResult {
        val input = CharStreams.fromFileName(path)
        val pNode = runAnalyser(input)
        val assembly = runGenerator(pNode)

        return executeAssembly(assembly, stdin)
    }

    private fun referencePipeline(
        path: String,
        stdin: String = ""
    ): EmulatorResult {
        val assembly = RefCompiler(File(path)).run()

        return executeAssembly(assembly, stdin)
    }

    private fun executeAssembly(
        assembly: List<String>,
        stdin: String
    ): EmulatorResult {
        val assemFile = File("tmp.s")
        val writer = FileWriter(assemFile)
        assembly.forEach { writer.appendLine(it) }
        writer.close()

        val res = RefEmulator(assemFile).execute(stdin)
        assemFile.delete()
        return res
    }
}