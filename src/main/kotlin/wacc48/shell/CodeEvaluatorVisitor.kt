package wacc48.shell

import wacc48.tree.ASTVisitor
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.ProgNode
import wacc48.tree.nodes.assignment.*
import wacc48.tree.nodes.expr.*
import wacc48.tree.nodes.expr.operators.BinOpNode
import wacc48.tree.nodes.expr.operators.UnOpNode
import wacc48.tree.nodes.function.*
import wacc48.tree.nodes.statement.*
import wacc48.tree.type.*
import java.io.BufferedReader
import java.io.PrintStream
import java.util.*
import kotlin.system.exitProcess

/** The visitor that traverses the AST wacc48.tree top down from a start node.
 * At each node, it evaluates any statements and expressions
 * editing and adding to the memory when needed. */
class CodeEvaluatorVisitor(
    var mt: MemoryTable,
    private val input: BufferedReader = System.`in`.bufferedReader(),
    val output: PrintStream = System.`out`,
    private val testMode: Boolean = false,
    var exitCode: Int? = null
) : ASTVisitor {
    private var returnFromFuncCall: Boolean = false
    private var inSeqCtx: Boolean = false
    private val argListStack: Stack<Literal> = Stack()
    private val evalLiteralStack: Stack<Literal> = Stack()

    fun printEvalLiteralStack(prompt: String) {
        evalLiteralStack.forEach {
            output.println("$prompt${it.literalToString(mt)}")
        }
        evalLiteralStack.clear()
    }

    /** Wrapper method to tell [node] to invoke its corresponding
     *  'translate' method to evaluate code */
    override fun visitNode(node: ASTNode) {
        node.acceptVisitor(this)
    }

    /** Evaluate whole program by calling evaluate on each of its children */
    override fun visitProgram(node: ProgNode) {
        node.functions.forEach {
            visitFunction(it)
        }
        visitMain(node.main)
    }

    /** Evaluate the 'main' function */
    override fun visitMain(node: MainNode) {
        visitNode(node.body)
    }

    /** Evaluates expression to get exit code, then invoke syscall 'exit'. */
    override fun visitExit(node: ExitNode) {
        visitNode(node.expr)
        val exitCode = (evalLiteralStack.pop() as IntLiteral).value
        if (!testMode) {
            output.println("Exit code: $exitCode")
            exitProcess(exitCode)
        } else {
            this.exitCode = ((exitCode % 256) + 256) % 256
        }
    }

    /** Adds the parameters to the function represented by [node] to a list of
     * 'declared' variables.
     *
     *  No need to evaluate the body as its just declaring the function
     */
    override fun visitFunction(node: FuncNode) {
        node.paramList.forEach {
            node.paramListTable.declareVariable(it.text)
        }
    }

    /** Makes a new memory table for the function body evaluation, adding the arguments
     *  into this memory table, and then evaluating the body and returning its resulting
     *  literal. */
    override fun visitFuncCall(node: FuncCallNode) {
        newMemoryTableScope()

        //push args' evaluated literals onto argListStack
        visitNode(node.argList)

        //now put these evaluated args into the new memory table with correct names
        node.functionNode.paramList.forEach {
            val arg = argListStack.pop()
            mt[it.text, arg.type] = arg
        }

        visitNode(node.functionNode.body)
        returnFromFuncCall = false

        endMemoryTableScope()
    }

    /** Declares the name of variable.*/
    override fun visitParam(node: ParamNode) {
        node.st.declareVariable(node.text)
        mt[node.text] = node.type
    }

    /** Returns a pairMemoryLiteral to be stored in the memory table */
    override fun visitNewPair(node: NewPairNode) {
        evalLiteralStack.add(
            PairMemoryLiteral(
                node.firstElem.reduceToLiteral(mt),
                node.secondElem.reduceToLiteral(mt),
                PairType(node.firstElem.type, node.secondElem.type, null)
            )
        )
    }

    /** Add declaration to memory table and evaluated assignment literal */
    override fun visitDeclaration(node: DeclarationNode) {
        visitNode(node.name)
        visitNode(node.value)
        mt[node.name.text] = evalLiteralStack.pop()
    }

    /** Loads the arguments in a function call onto the argListStack to load it
     * into a new memory table in visitFuncCall before evaluating the function body */
    override fun visitArgList(node: ArgListNode) {
        node.args.reversed().forEach {
            visitNode(it)
            argListStack.push(evalLiteralStack.pop())
        }
    }

    override fun visitAssignment(node: AssignmentNode) {
        when (node.name) {
            is IdentifierNode -> visitIdentifierAssignment(node)
            is ArrayElement -> visitArrayElemAssignment(node)
            is PairElemNode -> visitPairElemAssignment(node)
        }
    }

    private fun visitPairElemAssignment(node: AssignmentNode) {
        val pairElemNode = node.name as PairElemNode

        if (mt.getLiteral((pairElemNode.expr as IdentifierNode).name) !is PairMemoryLiteral)
            throw ShellNullDereferenceError("cannot write into null pair literal")

        val pairMemLiteral = (mt.getLiteral((pairElemNode.expr as IdentifierNode).name) as PairMemoryLiteral)

        visitNode(node.value)

        if (pairElemNode.isFirst) {
            pairMemLiteral.firstLiteral = evalLiteralStack.pop()
        } else {
            pairMemLiteral.secondLiteral = evalLiteralStack.pop()
        }
    }

    private fun visitIdentifierAssignment(node: AssignmentNode) {
        visitNode(node.value)
        mt[(node.name as IdentifierNode).name] = evalLiteralStack.pop()
    }

    private fun visitArrayElemAssignment(node: AssignmentNode) {
        //ArrayRef is the name of the array we are assigning stuff to
        val arrElem = node.name as ArrayElement
        //get the identifier of the referenced array so we can access it through the memory table
        val arrayRef = if (arrElem.arrIndices.size > 1) arrElem.getArrayRef(mt) else arrElem.name
        //different procedures depending on whether its multidimensional array or one dimensional
        if (mt.getLiteral(arrayRef) is DeepArrayLiteral) {
            visitMultidimArrayAssignment(arrayRef, arrElem, node.value)
        } else {
            visitOneDimArrayAssignment(arrayRef, arrElem, node.value)
        }
    }

    //for assigning a value to a one dimensional array in the memory table
    private fun visitOneDimArrayAssignment(arrayRef: String, arrElem: ArrayElement, value: RHSNode) {
        //copy the values of the array and change the element we need to, then put this into memory
        val arrayInMemory = mt.getLiteral(arrayRef) as ArrayLiteral
        val index = (arrElem.arrIndices.last()
            .reduceToLiteral(mt) as IntLiteral).value
        val arrayCopy = arrayInMemory.values.toMutableList().apply {
            checkIndexBounds(index, size)
            removeAt(index)
            visitNode(value)
            add(index, evalLiteralStack.pop())
        }
        arrayInMemory.values = arrayCopy
    }

    private fun visitMultidimArrayAssignment(arrayRef: String, arrElem: ArrayElement, value: RHSNode) {
        val arrayInMemory = mt.getLiteral(arrayRef) as DeepArrayLiteral
        val index = (arrElem.arrIndices.last().reduceToLiteral(mt) as IntLiteral).value

        val arrayCopy = arrayInMemory.values.toMutableList().apply {
            checkIndexBounds(index, size)
            removeAt(index)
            when (value) {
                is IdentifierNode -> add(index, value.name)
                is ArrayElement -> add(index, value.getArrayRef(mt))
                is PairElemNode -> {
                    visitNode(value)
                    //duplication here is unavoidable
                    when (val arrFromPair = evalLiteralStack.pop()) {
                        is ArrayLiteral -> {
                            add(
                                index,
                                arrFromPair.nameInMemTable
                                    ?: throw ShellRunTimeException(
                                        "Missing reference of array to memory table. Cannot assign new array from pair"
                                    )
                            )
                        }
                        is DeepArrayLiteral -> add(
                            index,
                            arrFromPair.nameInMemTable
                                ?: throw ShellRunTimeException(
                                    "Missing reference of array to memory table. Cannot assign new array from pair"
                                )
                        )
                    }
                }
            }
        }

        arrayInMemory.values = arrayCopy
    }

    override fun visitBinOp(node: BinOpNode) {
        evalLiteralStack.add(node.reduceToLiteral(mt))
    }

    override fun visitUnOp(node: UnOpNode) {
        evalLiteralStack.add(node.reduceToLiteral(mt))
    }

    override fun visitPairElem(node: PairElemNode) {
        if (node.mode == AccessMode.READ) {
            when (node.expr) {
                is IdentifierNode -> {
                    when (val literal = mt.getLiteral((node.expr as IdentifierNode).name)) {
                        is PairLiteral -> evalLiteralStack.add(literal)
                        is PairMemoryLiteral ->
                            if (node.isFirst)
                                evalLiteralStack.add(literal.firstLiteral)
                            else
                                evalLiteralStack.add(literal.secondLiteral)
                        else -> {}
                    }
                }
                is ArrayElement -> visitNode(node.expr)
                else -> {}
            }
        }
    }

    override fun visitArrayElement(elem: ArrayElement) {
        evalLiteralStack.add(elem.reduceToLiteral(mt))
    }

    override fun visitArrayLiteral(literal: ArrayLiteral) {
        if (literal.elemType is ArrayType) {//multidimensional array
            val newLiteral = DeepArrayLiteral(
                literal.values.map { (it as IdentifierNode).name },
                literal.elemType
            )
            newLiteral.nameInMemTable = literal.nameInMemTable
            evalLiteralStack.add(newLiteral)
        } else
            evalLiteralStack.add(literal)
    }

    override fun visitBoolLiteral(literal: BoolLiteral) {
        evalLiteralStack.add(literal)
    }

    override fun visitCharLiteral(literal: CharLiteral) {
        evalLiteralStack.add(literal)
    }

    override fun visitIdentifier(node: IdentifierNode) {
        when (node.mode) {
            AccessMode.ASSIGN -> return
            else -> evalLiteralStack.add(mt.getLiteral(node.name))
        }
    }

    override fun visitIntLiteral(literal: IntLiteral) {
        evalLiteralStack.add(literal)
    }

    override fun visitPairLiteral(literal: PairLiteral) {
        evalLiteralStack.add(literal)
    }

    override fun visitStringLiteral(literal: StringLiteral) {
        evalLiteralStack.add(literal)
    }

    /** Opens new memory table scope. */
    override fun visitBegin(node: BeginNode) {
        if (returnFromFuncCall)
            return

        newMemoryTableScope()
        visitNode(node.stat)
        endMemoryTableScope()
    }

    private fun newMemoryTableScope() {
        mt = MemoryTable(mt)
    }

    private fun endMemoryTableScope() {
        mt = mt.parent!!
    }

    override fun visitFree(node: FreeNode) {
        //May seem redundant but needed for casting
        when (val expr = node.value) {
            is IdentifierNode -> mt.remove(expr.name)
            is ArrayElement -> mt.remove(expr.name)
        }
    }

    override fun visitIf(node: IfNode) {
        if (returnFromFuncCall)
            return

        newMemoryTableScope()
        visitNode(node.proposition)

        if ((evalLiteralStack.pop() as BoolLiteral).value)
            visitNode(node.trueStat)
        else
            visitNode(node.falseStat)
        endMemoryTableScope()
    }

    /** Literally print the evaluated value out */
    override fun visitPrint(node: PrintNode) {
        visitNode(node.value)
        val value = evalLiteralStack.pop().literalToString(mt)

        if (node.returnAfterPrint) output.println(value) else output.print(value)

        //If we are printing as the last print statement in a sequence, add new line
        if (!testMode && !inSeqCtx && !node.returnAfterPrint)
            output.println("")
    }

    override fun visitRead(node: ReadNode) {
        if (node.value is PairElemNode && node.value.expr is IdentifierNode)
            if (mt.getLiteral((node.value.expr as IdentifierNode).name) !is PairMemoryLiteral)
                throw ShellNullDereferenceError(" cannot read into null pair literal")
        visitNode(AssignmentNode(node.value, readType(node.value.type), null))
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
        return IntLiteral(readInt, false, null)
    }

    /** Evaluates return value and returns as literal */
    override fun visitReturn(node: ReturnNode) {
        visitNode(node.value)
        returnFromFuncCall = true
    }

    override fun visitSeq(node: SeqNode) {
        if (returnFromFuncCall)
            return

        val wasAlreadyInSeqCtx = inSeqCtx
        inSeqCtx = true

        node.sequence.subList(0, node.sequence.size - 1).forEach {
            visitNode(it)
        }

        if (!wasAlreadyInSeqCtx)
            inSeqCtx = false

        visitNode(node.sequence.last())
    }

    override fun visitWhile(node: WhileNode) {
        if (returnFromFuncCall)
            return

        newMemoryTableScope()
        visitNode(node.proposition)
        var proposition = evalLiteralStack.pop()
        while ((proposition as BoolLiteral).value) {
            visitNode(node.body)
            visitNode(node.proposition)
            proposition = evalLiteralStack.pop()
        }
        endMemoryTableScope()
    }

    override fun visitPairMemoryLiteral(node: PairMemoryLiteral) {
        evalLiteralStack.add(node)
    }

    override fun visitDeepArrayLiteral(node: DeepArrayLiteral) {
        evalLiteralStack.add(node)
    }
}