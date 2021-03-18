package wacc48.generator.optimisations

import WalkDirectory
import org.antlr.v4.runtime.CharStreams
import wacc48.generator.architecture.ArmArchitecture
import wacc48.generator.arm.executeAssembly
import wacc48.generator.arm.referencePipeline
import wacc48.generator.reference.EmulatorResult
import wacc48.runAnalyser
import wacc48.runOptimiser
import java.io.File
import kotlin.test.fail

fun checkAllMatches(label: String, optimisationLevel: Int) {
    WalkDirectory(label).run { f ->
        val refASM = f.path.replace(".wacc", "_ref.s")
        val compilerASM = f.path.replace(".wacc", "opt_$optimisationLevel.s")
        val inputFile = File(f.path.replace(".wacc", ".input"))
        val stdin = if (inputFile.exists()) inputFile.readLines()[0] else ""

        val referenceResult = referencePipeline(f.path, refASM, stdin = stdin)
        val compilerResult =
            compilerPipeline(f, File(compilerASM), optimisationLevel,stdin = stdin)

        if (compilerResult.emulatorOut != referenceResult.emulatorOut)
            fail(
                "====Expected Output====\n"
                        + referenceResult.emulatorOut
                        + "==== Actual Output ==== \n"
                        + compilerResult.emulatorOut
                        + "=======================\n"
                        + "Compiler assembly:  $compilerASM\n"
                        + "Reference assembly: $refASM\n"
                        + "=======================\n"
            )

        if (compilerResult.emulatorExit != referenceResult.emulatorExit)
            fail(
                "====Expected Exit Code====\n"
                        + referenceResult.emulatorExit
                        + "\n==== Actual Exit Code ====\n"
                        + compilerResult.emulatorExit
                        + "\n==========================\n"
                        + "Compiler assembly:  $compilerASM\n"
                        + "Reference assembly: $refASM\n"
                        + "==========================\n"
            )
    }
}

private fun compilerPipeline(
    srcFile: File,
    asmOutputFile: File,
    optimisationLevel: Int,
    stdin: String = ""
): EmulatorResult {
    val pNode = runAnalyser(CharStreams.fromFileName(srcFile.path), mutableListOf())
    runOptimiser(pNode, optimisationLevel)
    val architecture = ArmArchitecture.compile(pNode)

    return executeAssembly(
        assembly = architecture,
        stdin = stdin,
        file = asmOutputFile
    )
}