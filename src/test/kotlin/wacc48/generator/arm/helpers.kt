package wacc48.generator.arm

import WalkDirectory
import wacc48.entrypoint.ArmFormatter
import wacc48.entrypoint.WaccCompiler
import wacc48.generator.reference.EmulatorResult
import wacc48.generator.reference.RefCompiler
import wacc48.generator.reference.RefEmulator
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
            compilerPipeline(f, File(compilerASM), stdin = stdin)

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
    stdin: String = ""
): EmulatorResult {

    val armCompiler =
        WaccCompiler(ArmFormatter(), srcFile, asmOutputFile.parentFile)
    armCompiler.start()

    return executeAssembly(
        assembly = armCompiler.instructions,
        stdin = stdin,
        file = asmOutputFile
    )
}


fun referencePipeline(
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

fun executeAssembly(
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
