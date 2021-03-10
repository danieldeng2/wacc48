package shell

import WACCShell
import WalkDirectory
import analyser.exceptions.SemanticsException
import analyser.exceptions.SyntaxException
import org.junit.Test
import java.io.File

class ShellParsingTest {
    @Test
    fun validProgramsShouldNotFailToParse() {
        WalkDirectory("valid").run {
            try {
                runFileTestInShell(it)
            } catch (e: SemanticsException) {
                //Test fails only when syntax exception thrown
            }
        }
    }

    //@Test
    //fun syntaxErrorsShouldThrowSyntaxException() {
    //    WalkDirectory("invalid/syntaxErr").run {
    //        try {
    //            runFileTestInShell(it)
    //            error("This program contains syntax error")
    //        } catch (e: SyntaxException) {
    //            //Test passes
    //        }
    //    }
    //}
}

fun runFileTestInShell(file: File) {
    /* Format the file so that the prog rule is not there
    *  (so only command rules are present)
    *  Assumes: that the file is in the style of the wacc_examples repository */
    val program = file.readLines().dropWhile {
        //Drop comments and blank lines at beginning
        it.startsWith("#") || it.trim() == ""
    }.dropLastWhile { it.trim() == "" } //cut blank lines at end

    //Drop enclosing begin and end lines if there to avoid prog rule
    val formattedTestString =
        if (program[0].startsWith("begin") && program.last().endsWith("end")) {
            program.drop(1).dropLast(1).joinToString("\n")
        } else {
            program.joinToString("\n")
        }

    //println("=============================")
    //println("formattedTestString:")
    //println(formattedTestString)
    //println("=============================")

    val testShell = WACCShell(formattedTestString.byteInputStream().bufferedReader(), testMode = true)

    testShell.runInteractiveShell()
}