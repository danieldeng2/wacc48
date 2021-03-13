package shell

import generator.instructions.operands.*
import generator.translator.helpers.*
import tree.nodes.ASTNode
import tree.nodes.ProgNode
import tree.nodes.assignment.AccessMode
import tree.nodes.assignment.AssignmentNode
import tree.nodes.assignment.NewPairNode
import tree.nodes.assignment.PairElemNode
import tree.nodes.expr.*
import tree.nodes.expr.operators.BinOpNode
import tree.nodes.expr.operators.UnOpNode
import tree.nodes.function.*
import tree.nodes.statement.*
import tree.type.*
import java.io.BufferedReader
import java.io.PrintStream
import java.util.*
import kotlin.system.exitProcess

/** The visitor that traverses the AST tree top down from a start node.
 * At each node, it evaluates any statements and expressions
 * editing and adding to the memory when needed. */
class CodeEvaluatorVisitor(
    var mt: MemoryTable,
    private val input: BufferedReader = System.`in`.bufferedReader(),
    val output: PrintStream = System.`out`,
    private val testMode: Boolean = false,
    var exitCode: Int? = null
) {
    private var returnFromFuncCall: Boolean = false
    private var inSeqCtx: Boolean = false
    private val argListStack: Stack<Literal> = Stack()
    private val evalLiteralStack: Stack<Literal> = Stack()

    fun printEvalLiteralStack(prompt: String) {
        evalLiteralStack.forEach { output.println("$prompt${it.literalToString(mt)}") }
        evalLiteralStack.clear()
    }

    /** Wrapper method to tell [node] to invoke its corresponding
     *  'translate' method to evaluate code */
    fun visitAndTranslate(node: ASTNode) {
        node.acceptCodeEvalVisitor(this)
    }

    /** Evaluate whole program by calling evaluate on each of its children */
    fun translateProgram(node: ProgNode) {
        node.functions.forEach {
            translateFunction(it)
        }
        translateMain(node.main)
    }

    /** Evaluate the 'main' function */
    fun translateMain(node: MainNode) {
        visitAndTranslate(node.body)
    }

    /** Evaluates expression to get exit code, then invoke syscall 'exit'. */
    fun translateExit(node: ExitNode) {
        visitAndTranslate(node.expr)
        val exitCode = (evalLiteralStack.pop() as IntLiteral).value
        if (!testMode) {
            output.println("Exit code: $exitCode")
            exitProcess(exitCode)
        } else {
            this.exitCode = ((exitCode % 256) + 256) % 256
        }
    }

    /** Adds the parameters to the function represented by [node] to a list of
     * 'declared' variables. See more in [SymbolTable.declareVariable].
     *
     *  No need to evaluate the body as its just declaring the function
     */
    fun translateFunction(node: FuncNode) {
        node.paramList.forEach {
            node.paramListTable.declareVariable(it.text)
        }
    }

    /** Makes a new memory table for the function body evaluation, adding the arguments
     *  into this memory table, and then evaluating the body and returning its resulting
     *  literal. */
    fun translateFuncCall(node: FuncCallNode) {
        newMemoryTableScope()

        //push args' evaluated literals onto argListStack
        visitAndTranslate(node.argList)

        //now put these evaluated args into the new memory table with correct names
        node.functionNode.paramList.forEach {
            val arg = argListStack.pop()
            mt.set(it.text, arg.type, arg)
        }

        visitAndTranslate(node.functionNode.body)
        returnFromFuncCall = false

        endMemoryTableScope()
    }

    /** Declares the name of variable. See [SymbolTable.declareVariable] for more. */
    fun translateParam(node: ParamNode) {
        node.st.declareVariable(node.text)
        mt[node.text] = node.type
    }

    /** Returns a pairMemoryLiteral to be stored in the memory table */
    fun translateNewPair(node: NewPairNode) =
        evalLiteralStack.add(
            PairMemoryLiteral(
                node.firstElem.reduceToLiteral(mt),
                node.secondElem.reduceToLiteral(mt),
                PairType(node.firstElem.type, node.secondElem.type, null)
            )
        )

    /** Add declaration to memory table and evaluated assignment literal */
    fun translateDeclaration(node: DeclarationNode) {
        visitAndTranslate(node.name)
        visitAndTranslate(node.value)
        mt[node.name.text] = evalLiteralStack.pop()
    }

    /** Loads the arguments in a function call onto the argListStack to load it
     * into a new memory table in translateFuncCall before evaluating the function body */
    fun translateArgList(node: ArgListNode) {
        node.args.reversed().forEach {
            visitAndTranslate(it)
            argListStack.push(evalLiteralStack.pop())
        }
    }

    fun translateAssignment(node: AssignmentNode) {
        when (node.name) {
            is IdentifierNode -> {
                visitAndTranslate(node.value)
                mt[node.name.name] = evalLiteralStack.pop()
            }
            is ArrayElement -> {
                //ArrayRef is the name of the array we are assigning stuff to
                val arrayRef = if (node.name.arrIndices.size > 1) node.name.getArrayRef(mt) else node.name.name

                if (mt.getLiteral(arrayRef) is DeepArrayLiteral) { //Assigning to multidimensional arrayliteral
                    val arrayInMemory = mt.getLiteral(arrayRef) as DeepArrayLiteral
                    var index = (node.name.arrIndices.last().reduceToLiteral(mt) as IntLiteral).value
                    val arrayCopy = arrayInMemory.values.toMutableList().apply {
                        checkIndexBounds(index, size)
                        removeAt(index)
                        if (node.value is IdentifierNode) {
                            add(index, node.value.name)
                        } else if (node.value is ArrayElement) {
                            add(index, node.value.getArrayRef(mt))
                        } else if (node.value is PairElemNode) {
                            visitAndTranslate(node.value)
                            val arrFromPair = evalLiteralStack.pop()
                            if (arrFromPair is ArrayLiteral) {
                                add(
                                    index,
                                    arrFromPair.nameInMemTable
                                        ?: throw ShellRunTimeException(
                                            "Missing reference of array to memory table. " +
                                                    "Cannot assign new array from pair"
                                        )
                                )
                            } else if (arrFromPair is DeepArrayLiteral)
                                add(
                                    index,
                                    arrFromPair.nameInMemTable
                                        ?: throw ShellRunTimeException(
                                            "Missing reference of array to memory table. " +
                                                    "Cannot assign new array from pair"
                                        )
                                )
                        }
                    }
                    arrayInMemory.values = arrayCopy
                } else {
                    //When assigning to one dimensional array:
                    //copy the values of the array and change the element we need to, then put this into memory
                    val arrayInMemory = mt.getLiteral(arrayRef) as ArrayLiteral
                    var index = (node.name.arrIndices.last().reduceToLiteral(mt) as IntLiteral).value
                    val arrayCopy = arrayInMemory.values.toMutableList().apply {
                        checkIndexBounds(index, size)
                        removeAt(index)
                        visitAndTranslate(node.value)
                        add(index, evalLiteralStack.pop())
                    }
                    arrayInMemory.values = arrayCopy
                }
            }
            is PairElemNode -> { //node is a PairElem
                if (mt.getLiteral((node.name.expr as IdentifierNode).name) !is PairMemoryLiteral)
                    throw ShellNullDereferenceError("cannot write into null pair literal")

                val pairMemLiteral = (mt.getLiteral(node.name.expr.name) as PairMemoryLiteral)
                if (node.name.isFirst) {
                    visitAndTranslate(node.value)
                    pairMemLiteral.firstLiteral = evalLiteralStack.pop()
                } else {
                    visitAndTranslate(node.value)
                    pairMemLiteral.secondLiteral = evalLiteralStack.pop()
                }
            }
        }
    }

    fun translateBinOp(node: BinOpNode) = evalLiteralStack.add(node.reduceToLiteral(mt))

    fun translateUnOp(node: UnOpNode) = evalLiteralStack.add(node.reduceToLiteral(mt))

    /** Choose what operation to carry out based on [AccessMode]. */
    fun translatePairElem(node: PairElemNode) {
        if (node.mode == AccessMode.READ) {
            when (node.expr) {
                is IdentifierNode -> {
                    when (val literal = mt.getLiteral(node.expr.name)) {
                        is PairLiteral -> evalLiteralStack.add(literal)
                        is PairMemoryLiteral ->
                            if (node.isFirst)
                                evalLiteralStack.add(literal.firstLiteral)
                            else
                                evalLiteralStack.add(literal.secondLiteral)
                        else -> {
                        }
                    }
                }
                is ArrayElement -> visitAndTranslate(node.expr)
                else -> {
                }
            }
        }
    }

    fun translateArrayElement(elem: ArrayElement) =
        evalLiteralStack.add(elem.reduceToLiteral(mt))

    fun translateArrayLiteral(literal: ArrayLiteral) =
        if (literal.elemType is ArrayType) {//multidimensional array
            val newLiteral = DeepArrayLiteral(literal.values.map { (it as IdentifierNode).name }, literal.elemType)
            newLiteral.nameInMemTable = literal.nameInMemTable
            evalLiteralStack.add(newLiteral)
        } else
            evalLiteralStack.add(literal)

    fun translateBoolLiteral(literal: BoolLiteral) = evalLiteralStack.add(literal)

    fun translateCharLiteral(literal: CharLiteral) = evalLiteralStack.add(literal)

    fun translateIdentifier(node: IdentifierNode) =
        when (node.mode) {
            AccessMode.ASSIGN -> null
            else -> evalLiteralStack.add(mt.getLiteral(node.name))
        }

    fun translateIntLiteral(literal: IntLiteral) = evalLiteralStack.add(literal)

    fun translatePairLiteral(literal: PairLiteral) = evalLiteralStack.add(literal)

    fun translateStringLiteral(literal: StringLiteral) = evalLiteralStack.add(literal)

    /** Opens new memory table scope. */
    fun translateBegin(node: BeginNode) {
        if (returnFromFuncCall)
            return

        newMemoryTableScope()
        visitAndTranslate(node.stat)
        endMemoryTableScope()
    }

    private fun newMemoryTableScope() {
        mt = MemoryTable(mt)
    }

    private fun endMemoryTableScope() {
        mt = mt.parent!!
    }

    fun translateFree(node: FreeNode) {
        //May seem redundant but needed for casting
        if (node.value is IdentifierNode)
            mt.remove(node.value.name)
        else if (node.value is ArrayElement)
            mt.remove(node.value.name)
    }

    fun translateIf(node: IfNode) {
        if (returnFromFuncCall)
            return

        newMemoryTableScope()
        visitAndTranslate(node.proposition)
        if ((evalLiteralStack.pop() as BoolLiteral).value)
            visitAndTranslate(node.trueStat)
        else
            visitAndTranslate(node.falseStat)
        endMemoryTableScope()
    }

    /** Literally print the evaluated value out */
    fun translatePrint(node: PrintNode) {
        visitAndTranslate(node.value)
        val value = evalLiteralStack.pop().literalToString(mt)

        if (node.returnAfterPrint) output.println(value) else output.print(value)

        //If we are printing as the last print statement in a sequence, add new line
        if (!testMode && !inSeqCtx && !node.returnAfterPrint)
            output.println("")
    }

    fun translateRead(node: ReadNode) {
        if (node.value is PairElemNode && node.value.expr is IdentifierNode)
            if (mt.getLiteral(node.value.expr.name) !is PairMemoryLiteral)
                throw ShellNullDereferenceError(" cannot read into null pair literal")
        visitAndTranslate(AssignmentNode(node.value, readType(node.value.type), null))
    }

    private fun readType(type: Type) =
        when (type) {
            IntType -> readAnInt()
            CharType -> readAChar()
            else -> throw ShellRunTimeException("Cannot read type $type in WACC")
        }

    private fun readAChar(): CharLiteral =
        CharLiteral(input.readLine().trim()[0], null)

    private fun readAnInt(): IntLiteral {
        var readInt = input.readLine()?.toInt()
        while (readInt == null) {
            readInt = readLine()?.toInt()
        }
        return IntLiteral(readInt, null)
    }

    /** Evaluates return value and returns as literal */
    fun translateReturn(node: ReturnNode) {
        visitAndTranslate(node.value)
        returnFromFuncCall = true
    }

    fun translateSeq(node: SeqNode) {
        if (returnFromFuncCall)
            return

        val wasAlreadyInSeqCtx = inSeqCtx
        inSeqCtx = true

        node.sequence.subList(0, node.sequence.size - 1).forEach {
            visitAndTranslate(it)
        }

        if (!wasAlreadyInSeqCtx)
            inSeqCtx = false

        visitAndTranslate(node.sequence.last())
    }

    fun translateWhile(node: WhileNode) {
        if (returnFromFuncCall)
            return

        newMemoryTableScope()
        visitAndTranslate(node.proposition)
        var proposition = evalLiteralStack.pop()
        while ((proposition as BoolLiteral).value) {
            visitAndTranslate(node.body)
            visitAndTranslate(node.proposition)
            proposition = evalLiteralStack.pop()
        }
        endMemoryTableScope()
    }

    fun translatePairLiteral(literal: PairMemoryLiteral) = evalLiteralStack.add(literal)

    fun translateDeepArrayLiteral(literal: DeepArrayLiteral) = evalLiteralStack.add(literal)
}