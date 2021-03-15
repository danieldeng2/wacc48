package wacc48.shell

import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.ASTGeneratorShellVisitor
import wacc48.analyser.exceptions.SemanticsException
import wacc48.analyser.exceptions.SyntaxException
import wacc48.antlr.WACCLexer
import wacc48.antlr.WACCShellParser
import wacc48.runAnalyser
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.checkFunctionTerminates
import wacc48.tree.nodes.function.FuncNode
import java.io.BufferedReader
import java.io.PrintStream
import java.nio.file.Path

class WACCShell(
    private val input: BufferedReader = System.`in`.bufferedReader(),
    private val output: PrintStream = System.`out`,
    private val prompt: String = ">>> ",
    private val resultPrompt: String = "<<< ",
    private val multiLinePrompt: String = "... ",
    private val testMode: Boolean = false,
    private val evaluateCode: Boolean = true,
    private val programPath: Path? = null
) {

    fun runInteractiveShell() {
        var st = SymbolTable(null)
        val ft: MutableMap<String, FuncNode> = mutableMapOf()
        val mt = MemoryTable(null)

        val evalVisitor = CodeEvaluatorVisitor(mt, input, output, testMode)

        parseAndRunProgramFile(st, ft, evalVisitor)

        printIntro()

        var currLine: String? = readNewLine()

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
                val stCopy = st.clone()
                node.validate(stCopy, ft)
                st = stCopy
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

            if (evaluateCode) {
                evalVisitor.visitNode(node)
                evalVisitor.printEvalLiteralStack(resultPrompt)
            }
            if (evalVisitor.exitCode != null) {
                input.close()
                output.println("Exit code: ${evalVisitor.exitCode}")
                return
            }

            currLine = readNewLine()
        }

        input.close()
        output.println("Exit code: ${evalVisitor.exitCode ?: 0}")
        return
    }

    /** Parse lines from stdin until a valid rule is found, or return null if
     * no viable rules can be extracted from what has been entered in stdin. */
    private fun parseStdinRule(currLine: String): ParserRuleContext? {
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

    private fun parseAndRunProgramFile(
        st: SymbolTable,
        ft: MutableMap<String, FuncNode>,
        evalVisitor: CodeEvaluatorVisitor
    ) {
        if (programPath == null)
            return

        if (!programPath.toFile().exists()) {
            output.println("Error: wacc file $programPath does not exist")
            return
        }

        val node = runAnalyser(CharStreams.fromPath(programPath), st, ft)
        if (evaluateCode)
            evalVisitor.visitNode(node)
    }

    private fun printIntro() {
        if (!testMode) {
            output.println(">>> WACC Interactive Shell <<<")
            output.println("Instructions: ")
            output.println("\tExit wacc48.shell: use Ctrl-d (EOF) or 'quit' in normal scope")
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
