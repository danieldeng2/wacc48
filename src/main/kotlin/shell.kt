import analyser.ASTGeneratorShellVisitor
import analyser.exceptions.SemanticsException
import analyser.exceptions.SyntaxException
import shell.IncompleteRuleException
import shell.ShellErrorListener
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext
import shell.MemoryTable
import tree.SymbolTable
import tree.nodes.ASTNode
import tree.nodes.checkFunctionTerminates
import tree.nodes.function.FuncNode
import java.io.BufferedReader
import java.io.PrintStream
import java.nio.file.Path

//TODO(maybe printing in colours)
//TODO(import command maybe)

class WACCShell(
    private val input: BufferedReader = System.`in`.bufferedReader(),
    private val output: PrintStream = System.`out`,
    private val prompt: String = ">>> ",
    private val multiLinePrompt: String = "... ",
    private val testMode: Boolean = false,
    private val programPath: Path? = null
) {

    fun runInteractiveShell() {
        val st: SymbolTable = SymbolTable(null)
        val ft: MutableMap<String, FuncNode> = mutableMapOf()
        var mt: MemoryTable = MemoryTable(null)

        parseAndRunProgramFile(st, ft, mt)

        printIntro()

        var currLine: String? = readNewLine()

        //TODO(clean this while condition)
        //TODO(check that quit is never used as an identifier)
        //TODO(make sure return/exit work only when called in global scope)
        //TODO(make calling exit in normal scope be validated first, print code, then exit)
        while (currLine != null && currLine.trim() != "quit" && currLine.trim() != "return") {
            if (currLine.trim() == "") {
                currLine = readNewLine()
                continue
            }

            val parserContext: ParserRuleContext? = parseStdinRule(currLine)
            if (parserContext == null) {
                currLine = readNewLine()
                continue
            }

            //Semantic Analysis
            val node: ASTNode = parserContext.accept(ASTGeneratorShellVisitor())
            try {
                if (node is FuncNode) {
                    checkFunctionTerminates(node)
                    node.validatePrototype(ft)
                }
                node.validate(st, ft)
            } catch (e: SyntaxException) {
                output.println("Syntax Error: ${e.message}")
                if (testMode) {
                    input.close()
                    throw e
                }
                currLine = readNewLine()
                continue
            } catch (e: SemanticsException) {
                output.println("Semantics Error: ${e.message}")
                if (testMode) {
                    input.close()
                    throw e
                }
                currLine = readNewLine()
                continue
            }

            /* TODO(evaluation visitor - evaluate node updating memory, print the result literal
            *  Unimplemented printing list:
            *       expressions using only literals
            *       expressions using variables (requires memory handling)
            *       print/println statement
            *       prints within while statements
            *       prints within if statements
            *       prints within begin/end segments)
            * */

            //TODO(make readnextline only used once in the while loop / consider cleaner way of reading)
            currLine = readNewLine()
        }

        input.close()
        return
    }

    /** Parse lines from stdin until a valid rule is found, or return null if
     * no viable rules can be extracted from what has been entered in stdin. */
    fun parseStdinRule(currLine: String): ParserRuleContext? {
        var stdinBuffer = currLine

        do {
            try {
                val inputStream: CharStream = CharStreams.fromString(stdinBuffer)
                // Lexical Analysis
                val lexer = WACCLexer(inputStream)
                lexer.removeErrorListeners()
                lexer.addErrorListener(ShellErrorListener())

                val tokens = CommonTokenStream(lexer)

                //Syntax Analysis
                val parser = WACCShellParser(tokens)
                parser.removeErrorListeners()
                parser.addErrorListener(ShellErrorListener())

                //Attempt to match any rule using current built up input from stdin
                return parser.command()
            } catch (e: SyntaxException) {
                output.println("${e.message}")
                if (testMode) {
                    input.close()
                    throw e
                }
                return null
            } catch (e: IncompleteRuleException) {
            }
            /*If the parse is incomplete, there is still a chance it will be
                * later on*/
            stdinBuffer += "\n" + readNextLine()
        } while (true)
    }

    fun parseAndRunProgramFile(
        st: SymbolTable,
        ft: MutableMap<String, FuncNode>,
        memory: MemoryTable
    ) {
        if (programPath == null) {
            return
        }
        if (!programPath.toFile().exists()) {
            output.println("Error: wacc file $programPath does not exist")
            return
        }

        runAnalyserPrintError(CharStreams.fromPath(programPath), st, ft)

        //TODO(evaluate program body and change memory)
    }

    private fun printIntro() {
        //TODO(help page maybe)
        if (!testMode) {
            output.println(">>> WACC Interactive Shell <<<")
            output.println("Instructions: ")
            output.println("\tExit shell: use Ctrl-d (EOF) or 'quit' in normal scope")
            output.println("\tCancel multiline command: use Ctrl-d")
            output.println("\t'>>>' is the prompt for a new command")
            output.println("\t'...' is the prompt to continue the current command (multiple lines)")
            output.println("\tUse ./interpret -p <PATH TO .wacc FILE> to import functions and program body")
        }
    }

    private fun readNewLine(): String? = getLine(prompt)

    /** Read for when a rule spans multiple lines in stdin */
    private fun readNextLine(): String? = getLine(multiLinePrompt)

    private fun getLine(promptToPrint: String): String? {
        if (!testMode)
            output.print(promptToPrint)
        return input.readLine()
    }
}
