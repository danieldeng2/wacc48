package wacc48.generator.x86

import WalkDirectory
import wacc48.entrypoint.I386Formatter
import wacc48.entrypoint.WaccCompiler
import wacc48.generator.reference.EmulatorResult
import wacc48.generator.reference.RefCompiler
import wacc48.generator.reference.RefEmulator
import wacc48.writeResult
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.test.fail

fun runAllTests(subdir: String) {
    WalkDirectory(subdir).run { f ->
        val outx86 = f.path.replace(".wacc", "_x86.s")
        val outArm = f.path.replace(".wacc", "_arm.s")

        val results = runTestFile(f)
        val x86Result = results.first
        val armResult = results.second

        if (!compareOutputIgnoreAddress(
                armResult.emulatorOut,
                x86Result.emulatorOut
            )
        )
            fail(
                "====Expected Output====\n"
                        + armResult.emulatorOut
                        + "==== Actual Output ==== \n"
                        + x86Result.emulatorOut
                        + "=======================\n"
                        + "Compiler assembly:  $outx86\n"
                        + "Reference assembly: $outArm\n"
                        + "=======================\n"
            )

        if (x86Result.emulatorExit != armResult.emulatorExit)
            fail(
                "====Expected Exit Code====\n"
                        + armResult.emulatorExit
                        + "\n==== Actual Exit Code ====\n"
                        + x86Result.emulatorExit
                        + "\n==========================\n"
                        + "Compiler assembly:  $outx86\n"
                        + "Reference assembly: $outArm\n"
                        + "==========================\n"
            )
    }
}

private fun runTestFile(f: File): Pair<EmulatorResult, EmulatorResult> {
    val outx86 = f.path.replace(".wacc", "_x86.s")
    val outArm = f.path.replace(".wacc", "_arm.s")

    val inputFile = File(f.path.replace(".wacc", ".input"))
    val stdin = if (inputFile.exists()) inputFile.readLines()[0] else ""

    val i386Compiler =
        WaccCompiler(I386Formatter(), File(f.path), File(f.parent))
    i386Compiler.start()

    val instructionsx86 = i386Compiler.instructions
    val instructionsArm = RefCompiler(f).run()

    writeResult(outx86, instructionsx86)
    writeResult(outArm, instructionsArm)

    assembleAndLinkx86(outx86)

    try {
        val x86Executable = outx86.removeSuffix(".s")
        val x86EmulationProc =
            if (inputFile.exists())
                ProcessBuilder(x86Executable)
                    .redirectInput(inputFile)
                    .redirectOutput(ProcessBuilder.Redirect.PIPE)
                    .start()
            else
                ProcessBuilder(x86Executable)
                    .redirectOutput(ProcessBuilder.Redirect.PIPE)
                    .start()

        x86EmulationProc.waitFor(30, TimeUnit.SECONDS)

        val x86EmulatorResult = EmulatorResult(
            test = outx86,
            upload = "",
            emulatorExit = x86EmulationProc.exitValue(),
            emulatorOut = x86EmulationProc.inputStream.bufferedReader()
                .readText()
        )

        return Pair(
            x86EmulatorResult,
            RefEmulator(File(outArm)).execute(stdin)
        )


    } catch (e: IOException) {
        e.printStackTrace()
        throw IOException("Emulation failed for $outx86!")
    }

}

private fun assembleAndLinkx86(outputPathPrefix: String) {

    ProcessBuilder(
        "nasm",
        "-f",
        "elf32",
        "-o",
        outputPathPrefix.replace(".s", ".o"),
        outputPathPrefix
    ).start().waitFor(5, TimeUnit.SECONDS)


    println("Successfully assembled. Now attempting to link...")

    ProcessBuilder(
        "gcc",
        "-m32",
        "-o",
        outputPathPrefix.removeSuffix(".s"),
        outputPathPrefix.replace(".s", ".o")
    ).start().waitFor(5, TimeUnit.SECONDS)
    println("Successfully linked. Initiating emulation...")
}

private fun compareOutputIgnoreAddress(
    refOutput: String,
    output: String
): Boolean {
    val refOutputSplit: List<String> = refOutput
        .split(" ", "\\n")
        .filterNot { it.contains("0x") }
    val outputSplit: List<String> = output
        .split(" ", "\\n")
        .filterNot { it.contains("0x") }

    return refOutputSplit.toTypedArray() contentEquals outputSplit.toTypedArray()
}
