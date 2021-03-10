import analyser.ASTGeneratorShellVisitor
import analyser.exceptions.SemanticsException
import analyser.exceptions.SyntaxException
import shell.IncompleteRuleException
import shell.ShellErrorListener
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext
import tree.SymbolTable
import tree.nodes.ASTNode
import tree.nodes.checkFunctionTerminates
import tree.nodes.function.FuncNode
import java.io.BufferedReader
import java.nio.file.Path

//TODO(maybe printing in colours)
//TODO(import command maybe)

class WACCShell(
    private val input: BufferedReader = System.`in`.bufferedReader(),
    private val prompt: String = ">>> ",
    private val multiLinePrompt: String = "... ",
    private val testMode: Boolean = false,
    private val programPath: Path? = null
) {

    fun runInteractiveShell(debugFlag: Boolean = false) {
        val st: SymbolTable = SymbolTable(null)
        //TODO(reading functions from file)
        val ft: MutableMap<String, FuncNode> = mutableMapOf()
        var memory: MutableMap<String, Nothing> = mutableMapOf()

        parseAndRunProgramFile(st, ft, memory)

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

            val parserContext: ParserRuleContext? = parseStdinRule(currLine, debugFlag)
            if (parserContext == null) {
                currLine = readNewLine()
                continue
            }
            if (debugFlag) println("[successfully parsed rule]")

            //Semantic Analysis
            val node: ASTNode = parserContext.accept(ASTGeneratorShellVisitor())
            try {
                if (node is FuncNode) {
                    checkFunctionTerminates(node)
                    node.validatePrototype(ft)
                }
                node.validate(st, ft)
                //TODO(reduce duplication in the catch statements)
            } catch (e: SyntaxException) {
                println("Syntax Error: ${e.message}")
                currLine = readNewLine()
                if (testMode) {
                    input.close()
                    throw e
                }
                continue
            } catch (e: SemanticsException) {
                println("Semantics Error: ${e.message}")
                currLine = readNewLine()
                if (testMode) {
                    input.close()
                    throw e
                }
                continue
            } catch (e: SyntaxException) {
                println("Syntax Error: ${e.message}")
                currLine = readNewLine()
                if (testMode) {
                    input.close()
                    throw e
                }
                continue
            }
            if (debugFlag) println("[successfully semantically analysed rule]")

            //TODO(adding to symbol table or function table if necessary (or is that already done?))

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
    fun parseStdinRule(currLine: String, debugFlag: Boolean = false): ParserRuleContext? {
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
                println("Syntax Error: ${e.message}")
                if (debugFlag) println("stdinBuffer: $stdinBuffer")
                if (testMode) {
                    input.close()
                    throw e
                }
                return null
            } catch (e: IncompleteRuleException) {
                if (debugFlag) {
                    println("stdinBuffer: $stdinBuffer")
                    println("incomplete rule error: ${e.message}")
                }
                /*If the parse is incomplete, there is still a chance it will be
                * later on*/
                stdinBuffer += "\n" + readNextLine()
            }
        } while (true)
    }

    fun parseAndRunProgramFile(
        st: SymbolTable,
        ft: MutableMap<String, FuncNode>,
        memory: MutableMap<String, Nothing>
    ) {
        if (programPath == null) {
            return
        }
        if (!programPath.toFile().exists()) {
            println("Error: wacc file $programPath does not exist")
            return
        }

        runAnalyserPrintError(CharStreams.fromPath(programPath), st, ft)

        //TODO(evaluate program body and change memory)
    }

    private fun printIntro() {
        //TODO(help page maybe)
        println(">>> WACC Interactive Shell <<<")
        println("Instructions: ")
        println("\tExit shell: use Ctrl-d (EOF) or 'quit' in normal scope")
        println("\tCancel multiline command: use Ctrl-d")
        println("\t'>>>' is the prompt for a new command")
        println("\t'...' is the prompt to continue the current command (multiple lines)")
    }

    private fun readNewLine(): String? = getLine(prompt)

    /** Read for when a rule spans multiple lines in stdin */
    private fun readNextLine(): String? = getLine(multiLinePrompt)

    private fun getLine(promptToPrint: String): String? {
        print(promptToPrint)
        return input.readLine()
    }
}
