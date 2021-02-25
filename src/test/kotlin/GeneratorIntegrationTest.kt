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

    private fun checkAllMatches(label: String, printAll: Boolean = false) {
        val dir = File(javaClass.getResource(label).file)
        dir.walk().forEach { f ->
            if (f.isFile) {
                totalTests++
                println("${f.path}:")
                try {
                    if (printAll) println("Compiler output: ")
                    val compilerResult = compilerPipeline(f.path, printAll)
                    if (printAll) println("\nReference output: ")
                    val referenceResult = referencePipeline(f.path, printAll)
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
        printAll: Boolean,
        stdin: String = ""
    ): EmulatorResult {
        val input = CharStreams.fromFileName(path)
        val pNode = runAnalyser(input)
        val assembly = runGenerator(pNode)

        return executeAssembly(assembly, stdin, printAll)
    }

    private fun referencePipeline(
        path: String,
        printAll: Boolean,
        stdin: String = ""
    ): EmulatorResult {
        val assembly = RefCompiler(File(path)).run()

        return executeAssembly(assembly, stdin, printAll)
    }

    private fun executeAssembly(
        assembly: List<String>,
        stdin: String,
        printAll: Boolean
    ): EmulatorResult {
        val assemFile = File("tmp.s")
        val writer = FileWriter(assemFile)
        assembly.forEach {
            if (printAll) println(it)
            writer.appendLine(it)
        }
        writer.close()

        val res = RefEmulator(assemFile).execute(stdin)
        assemFile.delete()
        return res
    }
}