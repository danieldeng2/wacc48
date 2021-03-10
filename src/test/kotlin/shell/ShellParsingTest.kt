package shell

import WACCShell
import WalkDirectory
import analyser.exceptions.SemanticsException
import analyser.exceptions.SyntaxException
import org.junit.Test
import java.io.File
import java.nio.file.Path

class ShellParsingTest {
    //TODO(Make the testing not print the shell stuff to system out)

    @Test
    fun validProgramsShouldNotFailToParse() {
        WalkDirectory("valid").run {
            runFileInShellRemoveProgRule(it)
        }
    }

    @Test
    fun shellSyntaxErrorsShouldThrowSyntaxException() {
        WalkDirectory("invalid/shellSyntaxErr").run {
            try {
                runFileInShell(it)
                error("This program contains syntax error")
            } catch (e: SyntaxException) {
                //Test passes
            }
        }
    }

    @Test
    fun shellSemanticErrorsShouldThrowSemanticsException() {
        WalkDirectory("invalid/shellSemanticErr").run {
            try {
                runFileInShell(it)
                error("This program contains semantic error")
            } catch (e: SemanticsException) {
                //Test passes
            }
        }
    }

    @Test
    fun validProgramsShouldParseAsProgramFiles() {
        WalkDirectory("valid").run {
            try {
                runFileInShellRemoveProgRule(it)
            } catch (e: SemanticsException) {
                //Test fails only when syntax exception thrown
            }
        }
    }
}

fun runFileInShell(test: File) = runStringInShell(test.readText())

fun runFileInShellRemoveProgRule(file: File) {
    /* Format the file so that the prog rule is not there
    *  (so only command rules are present)
    *  Assumes: that the file is in the style of the wacc_examples repository */
    val program = file.readLines().dropWhile {
        //Drop comments and blank lines at beginning
        it.startsWith("#") || it.trim() == ""
    }.dropLastWhile { it.trim() == "" } //cut blank lines at end

    //Drop enclosing begin and end lines if there to avoid prog rule
    if (program[0].startsWith("begin") && program.last().endsWith("end")) {
        runStringInShell(program.drop(1).dropLast(1).joinToString("\n"))
    } else {
        runStringInShell(program.joinToString("\n"))
    }
}

fun runStringInShell(testString: String) {
    val testShell = WACCShell(testString.byteInputStream().bufferedReader(), testMode = true)
    testShell.runInteractiveShell()
}

fun runStringInShellWithProgramFile(testString: String, path: Path) {
    val testShell = WACCShell(
        testString.byteInputStream().bufferedReader(),
        testMode = true,
        programPath = path
    )
    testShell.runInteractiveShell()
}
