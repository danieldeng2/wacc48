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
    val testMode: Boolean = false,
    var exitCode: Int? = null
) {
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
        val prevMt = mt
        mt = MemoryTable(mt)

        //push args' evaluated literals onto argListStack
        visitAndTranslate(node.argList)

        //now put these evaluated args into the new memory table
        for (i in 0..node.argListSize) {
            mt[node.functionNode.paramList[i].text] = argListStack.pop()
        }

        val result = visitAndTranslate(node.functionNode.body)

        mt = prevMt

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
                //TODO(CLEAN THIS)
                //TODO(evaluated literal stack member to remove returning literal)
                if (node.name.arrIndices.size <= 1) {
                    if (mt[node.name.name]?.second is ArrayLiteral) {
                        val arrayCopy = (mt[node.name.name]?.second as ArrayLiteral).values.toMutableList()
                        var index = (node.name.arrIndices[0].reduceToLiteral(mt) as IntLiteral).value
                        var elem = visitAndTranslate(node.value)!!
                        arrayCopy.removeAt(index)
                        arrayCopy.add(index, elem)
                        val newArr = ArrayLiteral(arrayCopy, null)
                        newArr.type = (mt.getLiteral(node.name.name) as ArrayLiteral).type
                        newArr.elemType = (mt.getLiteral(node.name.name) as ArrayLiteral).elemType
                        mt[node.name.name] = newArr
                    } else if (mt[node.name.name]?.second is DeepArrayLiteral) {
                        TODO("Setting elements of multidimensional arrays (except for last) not supported yet")
                    }
                } else {
                    val name = node.name.getArrayRef(mt)
                    val arrayCopy = (mt[name]?.second as ArrayLiteral).values.toMutableList()
                    var index = (node.name.arrIndices.last().reduceToLiteral(mt) as IntLiteral).value
                    var elem = visitAndTranslate(node.value)!!
                    arrayCopy.removeAt(index)
                    arrayCopy.add(index, elem)
                    val newArr = ArrayLiteral(arrayCopy, null)
                    newArr.type = (mt.getLiteral(name) as ArrayLiteral).type
                    newArr.elemType = (mt.getLiteral(name) as ArrayLiteral).elemType
                    mt[name] = newArr
                }
            }
            is PairElemNode -> { //node is a PairElem
                if (node.name.isFirst) {
                    (mt.getLiteral((node.name.expr as IdentifierNode).name) as PairMemoryLiteral).firstLiteral =
                        visitAndTranslate(node.value)!!
                } else {
                    (mt.getLiteral((node.name.expr as IdentifierNode).name) as PairMemoryLiteral).secondLiteral =
                        visitAndTranslate(node.value)!!
                }
            }
        }
        return null
    }

    fun translateBinOp(node: BinOpNode): Literal? {
        return node.reduceToLiteral(mt)
    }

    fun translateUnOp(node: UnOpNode): Literal? {
        return node.reduceToLiteral(mt)
    }

    /** Choose what operation to carry out based on [AccessMode]. */
    fun translatePairElem(node: PairElemNode): Literal? {
        if (node.mode == AccessMode.READ) {
            when (node.expr) {
                is IdentifierNode -> {
                    return when (val literal = mt.getLiteral(node.expr.name)) {
                        is PairLiteral -> literal
                        is PairMemoryLiteral -> if (node.isFirst) literal.firstLiteral else literal.secondLiteral
                        else -> {
                            null
                        }
                    }
                }
                is ArrayElement -> {
                    return visitAndTranslate(node.expr)
                }
                else -> {
                    return null
                }
            }
        } else {
            //TODO("figure out how to handle when writing to pair")
            return null
        }
    }

    fun translateArrayElement(elem: ArrayElement): Literal? {
        return when (elem.mode) {
            AccessMode.READ -> elem.reduceToLiteral(mt)
            else -> TODO("work out what to do with this")
        }
    }

    fun translateArrayLiteral(literal: ArrayLiteral): Literal? {
        if (literal.elemType is ArrayType) { //multidimensional array
            return DeepArrayLiteral(literal.values.map { (it as IdentifierNode).name}, literal.elemType)
        }
        return literal
    }

    fun translateBoolLiteral(literal: BoolLiteral): Literal? {
        return literal
    }

    fun translateCharLiteral(literal: CharLiteral): Literal? {
        return literal
    }

    fun translateIdentifier(node: IdentifierNode): Literal? {
        return when (node.mode) {
            AccessMode.ASSIGN -> null
            else -> mt.getLiteral(node.name)
        }
    }

    fun translateIntLiteral(literal: IntLiteral): Literal? {
        return literal
    }

    fun translatePairLiteral(literal: PairLiteral): Literal? {
        return literal
    }

    fun translateStringLiteral(literal: StringLiteral): Literal? {
        return literal
    }

    /** Opens new memory table scope. */
    fun translateBegin(node: BeginNode): Literal? {
        //TODO(maybe have a similar method to newScope for mt)
        val prevMt = mt
        mt = MemoryTable(mt)
        var resultOfBody: Literal? = visitAndTranslate(node.stat)
        mt = prevMt
        return resultOfBody
    }

    fun translateFree(node: FreeNode): Literal? {
        if (node.value is IdentifierNode) {
            mt.remove(node.value.name)
        } else if (node.value is ArrayElement) {
            mt.remove(node.value.name)
        }
        return null
    }

    fun translateIf(node: IfNode): Literal? {
        val prevMt = mt
        mt = MemoryTable(mt)
        var resultOfIf = if ((visitAndTranslate(node.proposition) as BoolLiteral).value) {
            visitAndTranslate(node.trueStat)
        } else {
            visitAndTranslate(node.falseStat)
        }
        mt = prevMt
        return resultOfIf
    }

    /** Literally print the evaluated value out */
    fun translatePrint(node: PrintNode): Literal? {
        val value = visitAndTranslate(node.value)?.literalToString()

        if (node.returnAfterPrint)
            output.println(value)
        else
            output.print(value)

        //If we are printing as the last print statement in a sequence, add new line
        if (!testMode && !inSeqCtx && !node.returnAfterPrint)
            output.println("")

        return null
    }

    fun translateRead(node: ReadNode): Literal? {
        visitAndTranslate(node.value)

        //TODO(Clean this mess up)
        //TODO(Fix the declaring a variable even tho it semantic errors bug)
        when (node.value.type) {
            IntType -> {
                var readInt = input.readLine()?.toInt()
                while (readInt == null) {
                    readInt = readLine()?.toInt()
                }
                when (node.value) {
                    is IdentifierNode -> mt[node.value.name] = IntLiteral(readInt, null)
                    is PairElemNode -> {
                        if (node.value.isFirst)
                            (mt.getLiteral((node.value.expr as IdentifierNode).name) as PairMemoryLiteral).firstLiteral =
                                IntLiteral(readInt, null)
                        else
                            (mt.getLiteral((node.value.expr as IdentifierNode).name) as PairMemoryLiteral).secondLiteral =
                                IntLiteral(readInt, null)
                    }
                    else -> {
                        TODO()
                    }
                }
            }
            CharType -> {
                var readChar = input.readLine().trim()[0]
                when (node.value) {
                    is IdentifierNode -> mt[node.value.name] = CharLiteral(readChar, null)
                    is PairElemNode -> {
                        if (node.value.isFirst)
                            (mt.getLiteral((node.value.expr as IdentifierNode).name) as PairMemoryLiteral).firstLiteral =
                                CharLiteral(readChar, null)
                        else
                            (mt.getLiteral((node.value.expr as IdentifierNode).name) as PairMemoryLiteral).secondLiteral =
                                CharLiteral(readChar, null)
                    }
                    else -> {
                        TODO()
                    }
                }
            }
            else -> throw NotImplementedError(
                "Implement read for ${node.value.type}"
            )
        }

        return null
    }

    /** Evaluates return value and returns as literal */
    fun translateReturn(node: ReturnNode): Literal? {
        return visitAndTranslate(node.value)?.reduceToLiteral(mt)!!
    }

    fun translateSeq(node: SeqNode): Literal? {
        val wasAlreadyInSeqCtx = inSeqCtx
        inSeqCtx = true
        node.sequence.subList(0, node.sequence.size - 1).forEach { visitAndTranslate(it) }
        if (!wasAlreadyInSeqCtx)
            inSeqCtx = false
        return visitAndTranslate(node.sequence.last())?.reduceToLiteral(mt)
    }

    fun translateWhile(node: WhileNode): Literal? {
        val prevMt = mt
        mt = MemoryTable(mt)
        var resultOfBody: Literal? = null

        while ((node.proposition.reduceToLiteral(mt) as BoolLiteral).value) {
            resultOfBody = visitAndTranslate(node.body)
        }

        mt = prevMt

        return resultOfBody
    }

    fun translatePairLiteral(literal: PairMemoryLiteral): Literal? {
        return literal
    }

    fun translateDeepArrayLiteral(deepArrayLiteral: DeepArrayLiteral): Literal? {
        return deepArrayLiteral
    }
}