package generator.arm

import ArmFormatter
import WaccCompiler
import WalkDirectory
import generator.reference.EmulatorResult
import generator.reference.RefCompiler
import generator.reference.RefEmulator
import java.io.File
import java.io.FileWriter
import kotlin.test.fail

fun checkAllMatches(label: String) {
    WalkDirectory(label).run { f ->
        val refASM = f.path.replace(".wacc", "_ref.s")
        val compilerASM = f.path.replace(".wacc", ".s")
        val inputFile = File(f.path.replace(".wacc", ".input"))
        val stdin = if (inputFile.exists()) inputFile.readLines()[0] else ""

        val referenceResult = referencePipeline(f.path, refASM, stdin = stdin)
        val compilerResult =
            compilerPipeline(f.path, compilerASM, stdin = stdin)

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
    path: String,
    outputName: String,
    stdin: String = ""
): EmulatorResult {

    ArmFormatter()

    val armCompiler =
        WaccCompiler(ArmFormatter(), File(path), File(outputName), true)
    armCompiler.start()

    return executeAssembly(
        assembly = armCompiler.instructions,
        stdin = stdin,
        file = File(outputName)
    )
}

private fun referencePipeline(
    path: String,
    outputName: String,
    stdin: String = ""
): EmulatorResult {
    val assembly = RefCompiler(File(path)).run()

    return executeAssembly(
        assembly = assembly,
        stdin = stdin,
        file = File(outputName)
    )
}

private fun executeAssembly(
    assembly: List<String>,
    file: File,
    stdin: String
): EmulatorResult {
    val writer = FileWriter(file)

    assembly.forEach {
        writer.appendLine(it)
    }

    writer.close()
    return RefEmulator(file).execute(stdin)
}
