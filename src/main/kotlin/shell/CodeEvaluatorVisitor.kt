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

//TODO(evaluated literal stack member to remove returning literal)
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

    /** Wrapper method to tell [node] to invoke its corresponding
     *  'translate' method to evaluate code */
    fun visitAndTranslate(node: ASTNode): Literal? {
        return node.acceptCodeEvalVisitor(this)
    }

    /** Evaluate whole program by calling evaluate on each of its children */
    fun translateProgram(node: ProgNode): Literal? {
        node.functions.forEach {
            translateFunction(it)
        }
        translateMain(node.main)
        return null
    }

    /** Evaluate the 'main' function */
    fun translateMain(node: MainNode): Literal? {
        return visitAndTranslate(node.body)
    }

    /** Evaluates expression to get exit code, then invoke syscall 'exit'. */
    fun translateExit(node: ExitNode): Literal? {
        val exitCode = (visitAndTranslate(node.expr) as IntLiteral).value
        if (!testMode) {
            output.println("Exit code: $exitCode")
            exitProcess(exitCode)
        } else {
            this.exitCode = ((exitCode % 256) + 256) % 256
            return null
        }
    }

    /** Adds the parameters to the function represented by [node] to a list of
     * 'declared' variables. See more in [SymbolTable.declareVariable].
     *
     *  No need to evaluate the body as its just declaring the function
     */
    fun translateFunction(node: FuncNode): Literal? {
        node.paramList.forEach {
            node.paramListTable.declareVariable(it.text)
        }
        return null
    }

    /** Makes a new memory table for the function body evaluation, adding the arguments
     *  into this memory table, and then evaluating the body and returning its resulting
     *  literal. */
    fun translateFuncCall(node: FuncCallNode): Literal? {
        newMemoryTableScope()

        //push args' evaluated literals onto argListStack
        visitAndTranslate(node.argList)

        //now put these evaluated args into the new memory table with correct names
        node.functionNode.paramList.forEach {
            val arg = argListStack.pop()
            mt.set(it.text, arg.type, arg)
        }

        val result = visitAndTranslate(node.functionNode.body)
        returnFromFuncCall = false

        endMemoryTableScope()

        return result
    }

    /** Declares the name of variable. See [SymbolTable.declareVariable] for more. */
    fun translateParam(node: ParamNode): Literal? {
        node.st.declareVariable(node.text)
        mt[node.text] = node.type
        return null
    }

    /** Returns a pairMemoryLiteral to be stored in the memory table */
    fun translateNewPair(node: NewPairNode): Literal? =
        PairMemoryLiteral(
            node.firstElem.reduceToLiteral(mt),
            node.secondElem.reduceToLiteral(mt),
            PairType(node.firstElem.type, node.secondElem.type, null)
        )

    /** Add declaration to memory table and evaluated assignment literal */
    fun translateDeclaration(node: DeclarationNode): Literal? {
        visitAndTranslate(node.name)
        mt[node.name.text] = visitAndTranslate(node.value)!!
        return null
    }

    /** Loads the arguments in a function call onto the argListStack to load it
     * into a new memory table in translateFuncCall before evaluating the function body */
    fun translateArgList(node: ArgListNode): Literal? {
        node.args.reversed().forEach {
            argListStack.push(visitAndTranslate(it))
        }
        return null
    }

    fun translateAssignment(node: AssignmentNode): Literal? {
        when (node.name) {
            is IdentifierNode -> {
                mt[node.name.name] = visitAndTranslate(node.value)!!
            }
            is ArrayElement -> {
                //ArrayRef is the name of the array we are assigning stuff to
                val arrayRef = if (node.name.arrIndices.size > 1) node.name.getArrayRef(mt) else node.name.name

                if (mt.getLiteral(arrayRef) is DeepArrayLiteral)
                    TODO("Setting elements of multidimensional arrays (except for last) not supported yet")

                //copy the values of the array and change the element we need to, then put this into memory
                val arrayInMemory = mt.getLiteral(arrayRef) as ArrayLiteral
                var index = (node.name.arrIndices.last().reduceToLiteral(mt) as IntLiteral).value
                val arrayCopy = arrayInMemory.values.toMutableList().apply {
                    checkIndexBounds(index, size)
                    removeAt(index)
                    add(index, visitAndTranslate(node.value)!!)
                }
                arrayInMemory.values = arrayCopy
            }
            is PairElemNode -> { //node is a PairElem
                if (mt.getLiteral((node.name.expr as IdentifierNode).name) !is PairMemoryLiteral)
                    throw ShellNullDereferenceError("cannot write into null pair literal")

                val pairMemLiteral = (mt.getLiteral(node.name.expr.name) as PairMemoryLiteral)
                if (node.name.isFirst)
                    pairMemLiteral.firstLiteral = visitAndTranslate(node.value)!!
                else
                    pairMemLiteral.secondLiteral = visitAndTranslate(node.value)!!
            }
        }
        return null
    }

    fun translateBinOp(node: BinOpNode): Literal? = node.reduceToLiteral(mt)

    fun translateUnOp(node: UnOpNode): Literal? = node.reduceToLiteral(mt)

    /** Choose what operation to carry out based on [AccessMode]. */
    fun translatePairElem(node: PairElemNode): Literal? =
        if (node.mode == AccessMode.READ) {
            when (node.expr) {
                is IdentifierNode -> {
                    when (val literal = mt.getLiteral(node.expr.name)) {
                        is PairLiteral -> literal
                        is PairMemoryLiteral -> if (node.isFirst) literal.firstLiteral else literal.secondLiteral
                        else -> null
                    }
                }
                is ArrayElement -> visitAndTranslate(node.expr)
                else -> null
            }
        } else null

    fun translateArrayElement(elem: ArrayElement): Literal? =
        elem.reduceToLiteral(mt)

    fun translateArrayLiteral(literal: ArrayLiteral): Literal? =
        if (literal.elemType is ArrayType) //multidimensional array
            DeepArrayLiteral(literal.values.map { (it as IdentifierNode).name }, literal.elemType)
        else
            literal

    fun translateBoolLiteral(literal: BoolLiteral): Literal? = literal

    fun translateCharLiteral(literal: CharLiteral): Literal? = literal

    fun translateIdentifier(node: IdentifierNode): Literal? =
        when (node.mode) {
            AccessMode.ASSIGN -> null
            else -> mt.getLiteral(node.name)
        }

    fun translateIntLiteral(literal: IntLiteral): Literal? = literal

    fun translatePairLiteral(literal: PairLiteral): Literal? = literal

    fun translateStringLiteral(literal: StringLiteral): Literal? = literal

    /** Opens new memory table scope. */
    fun translateBegin(node: BeginNode): Literal? {
        if (returnFromFuncCall)
            return null

        newMemoryTableScope()
        var resultOfBody: Literal? = visitAndTranslate(node.stat)
        endMemoryTableScope()

        return resultOfBody
    }

    private fun newMemoryTableScope() {
        mt = MemoryTable(mt)
    }

    private fun endMemoryTableScope() {
        mt = mt.parent!!
    }

    fun translateFree(node: FreeNode): Literal? {
        //May seem redundant but needed for casting
        if (node.value is IdentifierNode)
            mt.remove(node.value.name)
        else if (node.value is ArrayElement)
            mt.remove(node.value.name)

        return null
    }

    fun translateIf(node: IfNode): Literal? {
        if (returnFromFuncCall)
            return null

        newMemoryTableScope()
        var resultOfIf =
            if ((visitAndTranslate(node.proposition) as BoolLiteral).value)
                visitAndTranslate(node.trueStat)
            else
                visitAndTranslate(node.falseStat)
        endMemoryTableScope()

        return resultOfIf
    }

    /** Literally print the evaluated value out */
    fun translatePrint(node: PrintNode): Literal? {
        val value = visitAndTranslate(node.value)?.literalToString()

        if (node.returnAfterPrint) output.println(value) else output.print(value)

        //If we are printing as the last print statement in a sequence, add new line
        if (!testMode && !inSeqCtx && !node.returnAfterPrint)
            output.println("")

        return null
    }

    fun translateRead(node: ReadNode): Literal? {
        visitAndTranslate(node.value)

        if (node.value is PairElemNode && node.value.expr is IdentifierNode)
            if (mt.getLiteral(node.value.expr.name) !is PairMemoryLiteral)
                throw ShellNullDereferenceError(" cannot read into null pair literal")
        visitAndTranslate(AssignmentNode(node.value, readType(node.value.type), null))

        return null
    }

    private fun readType(type: Type): Literal =
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
    fun translateReturn(node: ReturnNode): Literal? {
        val returnVal = visitAndTranslate(node.value)?.reduceToLiteral(mt)!!
        returnFromFuncCall = true
        return returnVal
    }

    fun translateSeq(node: SeqNode): Literal? {
        if (returnFromFuncCall)
            return null

        val wasAlreadyInSeqCtx = inSeqCtx
        inSeqCtx = true

        node.sequence.subList(0, node.sequence.size - 1).forEach { visitAndTranslate(it) }

        if (!wasAlreadyInSeqCtx)
            inSeqCtx = false

        return visitAndTranslate(node.sequence.last())?.reduceToLiteral(mt)
    }

    fun translateWhile(node: WhileNode): Literal? {
        if (returnFromFuncCall)
            return null

        newMemoryTableScope()
        var resultOfBody: Literal? = null
        while ((node.proposition.reduceToLiteral(mt) as BoolLiteral).value) {
            resultOfBody = visitAndTranslate(node.body)
        }
        endMemoryTableScope()

        return resultOfBody
    }

    fun translatePairLiteral(literal: PairMemoryLiteral): Literal? = literal

    fun translateDeepArrayLiteral(literal: DeepArrayLiteral): Literal? = literal
}