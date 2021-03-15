package wacc48.shell

import WalkDirectory
import wacc48.analyser.exceptions.SemanticsException
import wacc48.analyser.exceptions.SyntaxException
import org.junit.Test
import java.io.File

class ShellParsingTest {
    @Test
    fun validProgramsShouldNotFailToParse() {
        WalkDirectory("valid").run {
            runFileInShellRemoveProgRule(it, evaluateCode = false)
        }
    }

    @Test
    fun shellSyntaxErrorsShouldThrowSyntaxException() {
        WalkDirectory("shell/shellSyntaxErr").run {
            try {
                runFileInShell(it, evaluateCode = false)
                error("This program contains syntax error")
            } catch (e: SyntaxException) {
                //Test passes
            }
        }
    }

    @Test
    fun shellSemanticErrorsShouldThrowSemanticsException() {
        WalkDirectory("shell/shellSemanticErr").run {
            try {
                runFileInShell(it, evaluateCode = false)
                error("This program contains semantic error")
            } catch (e: SemanticsException) {
                //Test passes
            }
        }
    }

    @Test
    fun validProgramsShouldParseAsProgramFiles() {
        WalkDirectory("valid").run {
            runStringInShellWithProgramFile("", it.toPath(), evaluateCode = false,)
        }
    }

    @Test
    fun functionsFromImportedProgramFileCanBeCalled() {
        runStringInShellWithProgramFile(
            "int test = call inc(0)",
            File(javaClass.getResource("/shell/simpleFuncImport.wacc").file).toPath(),
            evaluateCode = false,
        )
    }

    @Test
    fun variableFromImportedProgramFileCanBeUsed() {
        runStringInShellWithProgramFile(
            "test = 1",
            File(javaClass.getResource("/shell/varImport.wacc").file).toPath(),
            evaluateCode = false,
        )
    }

    @Test
    fun variableFromImportedProgramFileCanNotBeRedeclared() {
        try {
            runStringInShellWithProgramFile(
                "char test = 'a'",
                File(javaClass.getResource("/shell/varImport.wacc").file).toPath(),
                evaluateCode = false,
            )
            error("This program contains semantic error")
        } catch (e: SemanticsException) {
            //Test passes
        }
    }
}

