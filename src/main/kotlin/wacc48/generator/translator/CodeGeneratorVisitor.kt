package wacc48.generator.translator

import wacc48.generator.instructions.FunctionEnd
import wacc48.generator.instructions.FunctionStart
import wacc48.generator.instructions.Syscall
import wacc48.generator.instructions.arithmetic.ADDInstr
import wacc48.generator.instructions.branch.BEQInstr
import wacc48.generator.instructions.branch.BInstr
import wacc48.generator.instructions.branch.BLInstr
import wacc48.generator.instructions.compare.CMPInstr
import wacc48.generator.instructions.directives.Directive
import wacc48.generator.instructions.directives.LabelInstr
import wacc48.generator.instructions.load.LDRInstr
import wacc48.generator.instructions.move.MOVInstr
import wacc48.generator.instructions.operands.*
import wacc48.generator.instructions.store.STRInstr
import wacc48.generator.translator.ArmConstants.FALSE_VALUE
import wacc48.generator.translator.ArmConstants.NULL_ADDRESS
import wacc48.generator.translator.ArmConstants.NUM_BYTE_ADDRESS
import wacc48.generator.translator.ArmConstants.TRUE_VALUE
import wacc48.generator.translator.helpers.*
import wacc48.generator.translator.lib.FreePair
import wacc48.generator.translator.lib.errors.CheckNullPointer
import wacc48.generator.translator.lib.print.PrintLn
import wacc48.generator.translator.lib.read.ReadChar
import wacc48.generator.translator.lib.read.ReadInt
import wacc48.tree.ASTBaseVisitor
import wacc48.tree.ASTVisitor
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.ProgNode
import wacc48.tree.nodes.assignment.AccessMode
import wacc48.tree.nodes.assignment.AssignmentNode
import wacc48.tree.nodes.assignment.NewPairNode
import wacc48.tree.nodes.assignment.PairElemNode
import wacc48.tree.nodes.expr.*
import wacc48.tree.nodes.expr.operators.BinOpNode
import wacc48.tree.nodes.expr.operators.BinaryOperator
import wacc48.tree.nodes.expr.operators.UnOpNode
import wacc48.tree.nodes.expr.operators.UnaryOperator
import wacc48.tree.nodes.function.*
import wacc48.tree.nodes.statement.*
import wacc48.tree.type.CharType
import wacc48.tree.type.IntType

/** The visitor that traverses the AST wacc48.tree top down, starting from
 * [rootNode]. At each node, it generates the intermediate representation
 * of the assembly code and add it to the corresponding accumulator
 * field in [ctx] */

class CodeGeneratorVisitor(private val rootNode: ASTNode) :
    ASTBaseVisitor<Unit>() {
    val ctx = TranslatorContext()

    /** Translates the given AST wacc48.tree [rootNode] into a list of assembly
     *  instructions in its intermediate representation
     *
     *  @return List of intermediate ARM instructions
     */
    fun translateToArm(): List<String> {
        visitNode(rootNode)
        return ctx.assembleArm().map { it.toArm() }
    }

    fun translateTox86(): List<String> {
        visitNode(rootNode)
        return ctx.assemblex86().map { it.tox86() }.flatten()

    }

    override fun defaultResult() {}

    /** Translate whole program by calling translate on each of its children */
    override fun visitProgram(node: ProgNode) {
        node.functions.forEach {
            visitFunction(it)
        }
        visitMain(node.main)
    }

    /** Translate the 'main' function, default to exit with status '0'
     * if not otherwise overwritten */
    override fun visitMain(node: MainNode) {
        ctx.stackPtrOffset = 0
        ctx.text.apply {
            add(LabelInstr("main"))
            add(FunctionStart())

            newScope(node.st) {
                visitNode(node.body)
            }
            add(MOVInstr(Register.R0, NumOp(0)))
            add(FunctionEnd())
        }
    }

    /** Evaluates expression to get exit code, then invoke syscall 'exit'. */
    override fun visitExit(node: ExitNode) {
        visitNode(node.expr)
        ctx.text.add(Syscall("exit"))

    }


    /** Adds the parameters to the function represented by [node] to a list of
     * 'declared' variables. See more in [SymbolTable.declareVariable].
     *
     *  Generates the appropriate instructions then 'visit' the function body.
     */
    override fun visitFunction(node: FuncNode) {
        node.paramList.forEach {
            node.paramListTable.declareVariable(it.text)
        }

        ctx.stackPtrOffset = 0

        ctx.text.apply {
            add(LabelInstr("f_${node.identifier}"))
            add(FunctionStart())

            startScope(node.bodyTable)
            visitNode(node.body)

            add(Directive(".ltorg"))
        }
    }

    /** Visit list of arguments [node.argList] and generate a Branch instruction.
     *  Moves stack pointer back to its position before this call if required.
     */
    override fun visitFuncCall(node: FuncCallNode) {
        visitNode(node.argList)

        ctx.text.apply {
            add(BLInstr("f_${node.name}"))

            if (node.argListSize != 0)
                add(
                    ADDInstr(
                        Register.SP,
                        Register.SP,
                        NumOp(node.argListSize)
                    )
                )
        }
    }

    /** Declares the name of variable. See [SymbolTable.declareVariable] for more. */
    override fun visitParam(node: ParamNode) {
        val symbolTable = node.st
        symbolTable.declareVariable(node.text)
    }

    /** Allocates the memory for each element of the pair, then store them there.
     *
     *  Allocates memory for 2 addresses - the addresses of each element of the pair.
     *  Then store the addresses of the pair elements into the newly allocated
     *  memory.
     * */
    override fun visitNewPair(node: NewPairNode) {
        ctx.text.apply {
            // Mallocs for 2 elements of pair
            storeElemInHeap(node.firstElem)
            storeElemInHeap(node.secondElem)

            // Malloc for the pair itself
            add(MOVInstr(Register.R0, NumOp(2 * NUM_BYTE_ADDRESS)))
            add(Syscall("malloc"))
            add(popAndDecrement(ctx, Register.R1, Register.R2))

            add(STRInstr(Register.R2, MemAddr(Register.R0)))
            add(
                STRInstr(
                    Register.R1,
                    MemAddr(Register.R0, NumOp(NUM_BYTE_ADDRESS))
                )
            )
        }
    }

    /** Visit the RHS of the declaration to evaluate it, putting the answer
     *  into register R0 (principle of accumulator method). Then visit
     *  the LHS of the declaration to declare the variable into current scope.
     *  Then store the local variable onto allocated space on the stack.
     */
    override fun visitDeclaration(node: DeclarationNode) {
        visitNode(node.value)
        visitNode(node.name)

        val (offset, isArg) = ctx.getOffsetOfVar(node.name.text, node.st)
        ctx.text.add(
            storeLocalVar(
                node.name.type,
                offset,
                isArgument = isArg
            )
        )
    }

    /** Loads the arguments in a function call onto the stack in reversed order.
     *  This is done by first evaluating each argument, putting it onto
     *  the stack at the location pointed to by the SP, then decrement SP by
     *  the size of the loaded element.
     */
    override fun visitArgList(node: ArgListNode) {

        ctx.text.apply {
            val stackPtrTemp = ctx.stackPtrOffset

            node.args.asReversed().forEach {
                visitNode(it)
                add(
                    storeLocalVar(
                        varType = it.type,
                        stackOffset = -it.type.reserveStackSize,
                        isArgLoad = true
                    )
                )
                ctx.stackPtrOffset += it.type.reserveStackSize
            }

            ctx.stackPtrOffset = stackPtrTemp
        }
    }

    /** */
    override fun visitAssignment(node: AssignmentNode) {
        visitNode(node.value)
        visitNode(node.name)
    }

    /** Select appropriate methods to generate code */
    override fun visitBinOp(node: BinOpNode) {
        when (node.operator) {
            BinaryOperator.EQ -> translateEquality(node, isEqual = true)
            BinaryOperator.NEQ -> translateEquality(node, isEqual = false)
            BinaryOperator.AND -> translateLogical(node, isAnd = true)
            BinaryOperator.OR -> translateLogical(node, isAnd = false)
            BinaryOperator.PLUS -> translatePlusMinus(node, isPlus = true)
            BinaryOperator.MINUS -> translatePlusMinus(node, isPlus = false)
            BinaryOperator.MULTIPLY -> translateMultiply(node)
            BinaryOperator.DIVIDE -> translateDivide(node)
            BinaryOperator.MODULUS -> translateModulo(node)
            else -> translateComparator(node)
        }
    }

    /** Select appropriate methods to generate code */
    override fun visitUnOp(node: UnOpNode) {
        when (node.operator) {
            UnaryOperator.NEGATE -> translateNegate(node)
            UnaryOperator.CHR, UnaryOperator.ORD -> visitNode(node.expr)
            UnaryOperator.MINUS -> translateMinus(node)
            UnaryOperator.LEN -> ctx.text.apply {
                visitNode(node.expr)
                add(LDRInstr(Register.R0, MemAddr(Register.R0)))
            }
        }
    }

    /** Choose what operation to carry out based on [AccessMode]. */
    override fun visitPairElem(node: PairElemNode) {
        ctx.addLibraryFunction(CheckNullPointer)

        val memOffset = if (node.isFirst) 0 else NUM_BYTE_ADDRESS

        if (node.mode == AccessMode.READ)
            loadFromPosition(node, memOffset)
        else
            assignToPosition(node, memOffset)
    }


    override fun visitArrayElement(elem: ArrayElement) {
        when (elem.mode) {
            AccessMode.READ -> translateArrayRead(elem)
            else -> translateArrayAssignment(elem)
        }
    }

    /** Allocate memory for all the elements of the array based on its type, plus
     * the required memory to store the size of the array. */
    override fun visitArrayLiteral(literal: ArrayLiteral) {
        ctx.text.apply {
            val mallocSize =
                literal.values.size * literal.elemType.reserveStackSize + IntType.reserveStackSize
            add(MOVInstr(Register.R0, NumOp(mallocSize)))
            add(Syscall("malloc"))
            add(MOVInstr(Register.R3, Register.R0))

            literal.values.forEachIndexed { index, arrayElem ->
                visitNode(arrayElem)
                add(
                    storeLocalVar(
                        literal.elemType,
                        index * literal.elemType.reserveStackSize + IntType.reserveStackSize,
                        rd = Register.R3,
                        rn = Register.R0
                    )
                )
            }

            add(MOVInstr(Register.R0, NumOp(literal.values.size)))
            add(STRInstr(Register.R0, MemAddr(Register.R3)))
            add(MOVInstr(Register.R0, Register.R3))
        }
    }

    override fun visitBoolLiteral(literal: BoolLiteral) {
        ctx.text.add(
            when (literal.value) {
                true -> MOVInstr(Register.R0, NumOp(TRUE_VALUE))
                false -> MOVInstr(Register.R0, NumOp(FALSE_VALUE))
            }
        )
    }

    override fun visitCharLiteral(literal: CharLiteral) {
        ctx.text.add(MOVInstr(Register.R0, CharOp(literal.value)))
    }

    /** Looks up variable in the symbol table and calculate offset from current
     * stack pointer position. Then choose an operation based on its [AccessMode]*/
    override fun visitIdentifier(node: IdentifierNode) {
        val (offset, isArg) = ctx.getOffsetOfVar(node.name, node.st)

        ctx.text.add(
            when (node.mode) {
                AccessMode.ASSIGN -> storeLocalVar(
                    node.type,
                    offset,
                    isArgument = isArg
                )
                AccessMode.READ -> loadLocalVar(
                    node.type,
                    offset,
                    isArgument = isArg
                )
                else -> ADDInstr(
                    Register.R0,
                    Register.SP,
                    NumOp(offset)
                )
            }
        )
    }

    override fun visitIntLiteral(literal: IntLiteral) {
        ctx.text.add(
            LDRInstr(
                Register.R0,
                NumOp(literal.value, isLoad = true)
            )
        )
    }

    override fun visitPairLiteral(literal: PairLiteral) {
        ctx.text.add(MOVInstr(Register.R0, NumOp(NULL_ADDRESS)))
    }

    override fun visitDeepArrayLiteral(node: DeepArrayLiteral) {
    }

    override fun visitPairMemoryLiteral(node: PairMemoryLiteral) {
    }

    override fun visitStringLiteral(literal: StringLiteral) {
        ctx.text.add(
            LDRInstr(
                Register.R0,
                LabelOp(ctx.addMessage(literal.value))
            )
        )
    }

    /** Opens new scope. */
    override fun visitBegin(node: BeginNode) {
        ctx.text.apply {
            newScope(node.currST) {
                visitNode(node.stat)
            }
        }
    }

    /** Visit the child expression to be freed, then generate Branch instruction
     * to invoke 'free' syscall. */
    override fun visitFree(node: FreeNode) {
        ctx.text.apply {
            ctx.addLibraryFunction(FreePair)

            visitNode(node.value)
            add(BLInstr(FreePair.label))
        }
    }

    /** Get 2 indices for labels for:
     *    - False: False branch
     *    - Rest: Everything after the 'if' statement
     *
     *  Operation:
     *    - Evaluates the conditionals inside the 'if' statement
     *    - If this evaluates to false, then jump to the 'False' label
     *    - Otherwise continue, jumping to the 'Rest' label after the code
     *      for the true branch
     */
    override fun visitIf(node: IfNode) {
        ctx.text.apply {
            visitNode(node.proposition)
            add(CMPInstr(Register.R0, NumOp(0)))

            val falseBranchIndex = ctx.labelCounter
            val continueBranch = ctx.labelCounter

            add(BEQInstr("L$falseBranchIndex"))
            newScope(node.trueST) {
                visitNode(node.trueStat)
            }
            add(BInstr("L$continueBranch"))

            add(LabelInstr("L$falseBranchIndex"))
            newScope(node.falseST) {
                visitNode(node.falseStat)
            }
            add(LabelInstr("L$continueBranch"))
        }
    }

    /** Evaluates the expression passed to 'print'. Then select the appropriate
     *  built-in print functions [printFunc] based on the type of the expression.
     */
    override fun visitPrint(node: PrintNode) {
        val value = node.value
        visitNode(value)

        ctx.text.apply {
            if (value.type == CharType) {
                add(Syscall("putchar"))
            } else {

                val printFunc = getPrintOption(value.type)

                ctx.addLibraryFunction(printFunc)
                add(BLInstr(printFunc.label))
            }

            if (node.returnAfterPrint) {
                ctx.addLibraryFunction(PrintLn)
                add(BLInstr(PrintLn.label))
            }
        }
    }

    override fun visitRead(node: ReadNode) {
        ctx.text.apply {
            visitNode(node.value)

            val readFunc = when (node.value.type) {
                IntType -> ReadInt
                CharType -> ReadChar
                else -> throw NotImplementedError(
                    "Implement read for ${node.value.type}"
                )
            }
            ctx.addLibraryFunction(readFunc)
            add(BLInstr(readFunc.label))
        }
    }

    /** Evaluates expression, putting result in register R0.
     *  Destroys all scopes up the hierarchy by moving the stack pointer up
     *  the appropriate amount. Then POP the program counter PC. */
    override fun visitReturn(node: ReturnNode) {
        ctx.text.apply {
            visitNode(node.value)
            endAllScopes(node.st)
            add(FunctionEnd())
        }
    }

    override fun visitSeq(node: SeqNode) {
        node.sequence.forEach { visitNode(it) }
    }

    /** Gets 2 index for the labels for the body of the while loop and for
     * the conditional.
     *
     * First jump to the the conditional and evaluate it. If it is true,
     * then jump to the [bodyIndex] label, i.e. the body of the function.
     * Otherwise, the code for everything after the 'while' statement comes
     * after the conditional.
     * */
    override fun visitWhile(node: WhileNode) {
        ctx.text.apply {
            val bodyIndex = ctx.labelCounter
            val propositionIndex = ctx.labelCounter

            add(BInstr("L$propositionIndex"))

            add(LabelInstr("L$bodyIndex"))

            newScope(node.bodyST) {
                visitNode(node.body)
            }

            add(LabelInstr("L$propositionIndex"))
            visitNode(node.proposition)
            add(CMPInstr(Register.R0, NumOp(1)))
            add(BEQInstr("L$bodyIndex"))
        }
    }

}