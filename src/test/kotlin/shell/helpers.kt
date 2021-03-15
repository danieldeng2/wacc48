package shell

import WACCShell
import WalkDirectory
import generator.arm.referencePipeline
import generator.reference.EmulatorResult
import java.io.File
import java.io.PrintStream
import java.nio.file.Path
import kotlin.test.fail

fun runFileInShell(test: File, evaluateCode: Boolean, outputStream: PrintStream = System.`out`) =
    runStringInShell(test.readText(), evaluateCode, outputStream)

fun runFileInShellRemoveProgRule(file: File, evaluateCode: Boolean, outputStream: PrintStream = System.`out`) {
    /* Format the file so that the prog rule is not there
    *  (so only command rules are present)
    *  Assumes: that the file is in the style of the wacc_examples repository */
    val program = file.readLines().dropWhile {
        //Drop comments and blank lines at beginning
        it.startsWith("#") || it.trim() == ""
    }.dropLastWhile { it.trim() == "" } //cut blank lines at end

    //Drop enclosing begin and end lines if there to avoid prog rule
    if (program[0].startsWith("begin") && program.last().endsWith("end")) {
        runStringInShell(program.drop(1).dropLast(1).joinToString("\n"), evaluateCode = evaluateCode, outputStream)
    } else {
        runStringInShell(program.joinToString("\n"), evaluateCode, outputStream)
    }
}

fun runStringInShell(testString: String, evaluateCode: Boolean, outputStream: PrintStream = System.`out`) {
    val testShell = WACCShell(
        testString.byteInputStream().bufferedReader(),
        testMode = true,
        evaluateCode = evaluateCode,
        output = outputStream
    )
    testShell.runInteractiveShell()
}

fun runStringInShellWithProgramFile(
    testString: String,
    path: Path,
    evaluateCode: Boolean,
    outputStream: PrintStream = System.`out`
) {
    val testShell = WACCShell(
        testString.byteInputStream().bufferedReader(),
        testMode = true,
        programPath = path,
        evaluateCode = evaluateCode,
        output = outputStream
    )
    testShell.runInteractiveShell()
}

fun checkAllMatches(label: String) {
    WalkDirectory(label).run { f ->
        val refASM = f.path.replace(".wacc", "_ref.s")
        val shellOutput = f.path.replace(".wacc", ".out")
        val inputFile = File(f.path.replace(".wacc", ".input"))
        val stdin = if (inputFile.exists()) inputFile.readLines()[0] else ""

        val referenceResult = referencePipeline(f.path, refASM, stdin = stdin)
        val shellResult = shellPipeline(f.path, shellOutput, stdin = stdin.replace(" ", "\n"))

        if (shellResult.emulatorOut != referenceResult.emulatorOut)
            fail(
                "====Expected Output====\n"
                        + referenceResult.emulatorOut
                        + "==== Actual Output ==== \n"
                        + shellResult.emulatorOut
            )

        if (shellResult.emulatorExit != referenceResult.emulatorExit)
            fail(
                "====Expected Exit Code====\n"
                        + referenceResult.emulatorExit
                        + "\n==== Actual Exit Code ====\n"
                        + shellResult.emulatorExit
            )
    }
}

private fun shellPipeline(
    path: String,
    outputName: String,
    stdin: String = ""
): EmulatorResult {
    val emulatorOutputStream = PrintStream(outputName)
    runStringInShellWithProgramFile(stdin, Path.of(path), true, emulatorOutputStream)
    emulatorOutputStream.close()

    val output = File(outputName).readText()

    val exitCode = output.substring(output.lastIndexOf("Exit code: ") + 11).trim().toInt()

    return EmulatorResult(
        "",
        "",
        output.substring(0, output.lastIndexOf("Exit code: ")),
        exitCode
    )
}