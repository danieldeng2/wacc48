package generator

import ResourceWalker
import org.antlr.v4.runtime.CharStreams
import generator.reference.RefCompiler
import generator.reference.RefEmulator
import java.io.File
import java.io.FileWriter
import generator.reference.EmulatorResult
import runAnalyser
import runGenerator
import kotlin.test.assertEquals
import kotlin.test.fail

fun checkAllMatches(label: String) {
    ResourceWalker().walkDirectory(label) { f ->
        startTest(f)
        val compilerResult = compilerPipeline(f.path)
        val referenceResult = referencePipeline(f.path)
        if (compilerResult.emulatorOut != referenceResult.emulatorOut)
            error(
                "\nExpected output: \n"
                        + referenceResult.emulatorOut
                        + "\nActual output: \n"
                        + compilerResult.emulatorOut
            )

        if (compilerResult.emulatorExit != referenceResult.emulatorExit)
            error(
                "\nExpected exit code:\n"
                        + referenceResult.emulatorExit
                        + "\nActual exit code:\n"
                        + compilerResult.emulatorExit
            )

        passTest()
    }
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

private fun executeAssembly(assembly: List<String>, stdin: String): EmulatorResult {
    val assemFile = File("tmp.s")
    val writer = FileWriter(assemFile)
    assembly.forEach {
        writer.appendLine(it)
    }
    writer.close()

    val res = RefEmulator(assemFile).execute(stdin)
    assemFile.delete()
    return res
}
