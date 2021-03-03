package generator.translator

import datastructures.nodes.ProgNode
import datastructures.nodes.assignment.AccessMode
import datastructures.nodes.assignment.AssignmentNode
import datastructures.nodes.assignment.NewPairNode
import datastructures.nodes.assignment.PairElemNode
import datastructures.nodes.expr.*
import datastructures.nodes.expr.operators.BinOpNode
import datastructures.nodes.expr.operators.BinaryOperator
import datastructures.nodes.expr.operators.UnOpNode
import datastructures.nodes.expr.operators.UnaryOperator
import datastructures.nodes.function.*
import datastructures.nodes.statement.*
import datastructures.type.*
import generator.instructions.Instruction
import generator.instructions.arithmetic.*
import generator.instructions.branch.*
import generator.instructions.compare.CMPInstr
import generator.instructions.directives.Directive
import generator.instructions.directives.LabelInstr
import generator.instructions.load.LDRInstr
import generator.instructions.logical.EORInstr
import generator.instructions.move.*
import generator.instructions.operands.*
import generator.instructions.stack.POPInstr
import generator.instructions.stack.PUSHInstr
import generator.instructions.store.STRInstr
import generator.translator.helpers.*
import generator.translator.lib.FreePair
import generator.translator.lib.errors.CheckArrayBounds
import generator.translator.lib.errors.CheckNullPointer
import generator.translator.lib.errors.DivideByZeroError
import generator.translator.lib.errors.OverflowError
import generator.translator.lib.print.*
import generator.translator.lib.read.ReadChar
import generator.translator.lib.read.ReadInt
import java.rmi.UnexpectedException

class CodeGeneratorVisitor(val program: ProgNode) {
    private val ctx = TranslatorContext()

    fun translate(): List<Instruction> {
        translateProgram(program)
        return ctx.assemble()
    }

    fun translateProgram(node: ProgNode) {
        node.functions.forEach {
            translateFunction(it)
        }
        translateMain(node.main)
    }

    fun translateMain(node: MainNode) {
        ctx.stackPtrOffset = 0
        ctx.text.apply {
            add(LabelInstr("main"))
            add(PUSHInstr(Register.LR))

            newScope(node.st) {
                node.body.acceptCodeGenVisitor(this@CodeGeneratorVisitor)
            }
            add(MOVInstr(Register.R0, NumOp(0)))
            add(POPInstr(Register.PC))
        }
    }

    fun translateExit(node: ExitNode) {
        node.expr.acceptCodeGenVisitor(this)
        ctx.text.add(BLInstr("exit"))
    }


    fun translateFunction(node: FuncNode) {
        node.paramList.forEach {
            node.paramListTable.declareVariable(it.text)
        }

        ctx.stackPtrOffset = 0

        ctx.text.apply {
            add(LabelInstr("f_${node.identifier}"))
            add(PUSHInstr(Register.LR))

            startScope(node.bodyTable)

            node.body.acceptCodeGenVisitor(this@CodeGeneratorVisitor)

            add(Directive(".ltorg"))
        }
    }

    fun translateFuncCall(node: FuncCallNode) {
        node.argList.acceptCodeGenVisitor(this@CodeGeneratorVisitor)

        ctx.text.apply {
            add(BLInstr("f_${node.name}"))

            if (node.argListSize != 0)
                add(ADDInstr(Register.SP, Register.SP, NumOp(node.argListSize)))
        }
    }

    fun translateParam(node: ParamNode) {
        val symbolTable = node.st
        symbolTable.declareVariable(node.text)
        val offset = ctx.getOffsetOfVar(node.text, symbolTable)

        ctx.text.add(storeLocalVar(node.type, offset))
    }

    fun translateNewPair(node: NewPairNode) {
        ctx.text.apply {
            // Mallocs for 2 elements of pair
            storeElemInHeap(node.firstElem)
            storeElemInHeap(node.secondElem)

            // Malloc for the pair itself
            add(MOVInstr(Register.R0, NumOp(8)))
            add(BLInstr("malloc"))
            add(popAndDecrement(ctx, Register.R1, Register.R2))

            add(STRInstr(Register.R2, MemAddr(Register.R0)))
            add(STRInstr(Register.R1, MemAddr(Register.R0, NumOp(4))))
        }
    }

    fun translateDeclaration(node: DeclarationNode) {
        node.value.acceptCodeGenVisitor(this)
        node.name.acceptCodeGenVisitor(this)
    }

    fun translateArgList(node: ArgListNode) {

        ctx.text.apply {
            val stackPtrTemp = ctx.stackPtrOffset

            node.args.asReversed().forEach {
                it.acceptCodeGenVisitor(this@CodeGeneratorVisitor)
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

    fun translateAssignment(node: AssignmentNode) {
        node.value.acceptCodeGenVisitor(this)
        node.name.acceptCodeGenVisitor(this)
    }

    fun translateBinOp(node: BinOpNode) {
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

    private fun translateComparator(node: BinOpNode) {
        ctx.text.apply {
            loadOperandsIntoRegister(node)

            add(CMPInstr(Register.R0, Register.R1))

            when (node.operator) {
                BinaryOperator.GT -> {
                    add(MOVGTInstr(Register.R0, NumOp(1)))
                    add(MOVLEInstr(Register.R0, NumOp(0)))
                }

                BinaryOperator.GE -> {
                    add(MOVGEInstr(Register.R0, NumOp(1)))
                    add(MOVLTInstr(Register.R0, NumOp(0)))
                }

                BinaryOperator.LT -> {
                    add(MOVLTInstr(Register.R0, NumOp(1)))
                    add(MOVGEInstr(Register.R0, NumOp(0)))
                }

                BinaryOperator.LE -> {
                    add(MOVLEInstr(Register.R0, NumOp(1)))
                    add(MOVGTInstr(Register.R0, NumOp(0)))
                }

                else -> throw UnexpectedException(
                    "Unexpected fall through to 'else' branch in " +
                            "${object {}.javaClass.enclosingMethod.name} with operator ${node.operator}"
                )
            }

        }
    }

    private fun translateModulo(node: BinOpNode) {
        ctx.text.apply {
            ctx.addLibraryFunction(DivideByZeroError)

            loadOperandsIntoRegister(node)
            add(BLInstr(DivideByZeroError.label))
            add(BLInstr("__aeabi_idivmod"))
            add(MOVInstr(Register.R0, Register.R1))
        }
    }

    private fun translateDivide(node: BinOpNode) {
        ctx.text.apply {
            ctx.addLibraryFunction(DivideByZeroError)

            loadOperandsIntoRegister(node)

            add(BLInstr(DivideByZeroError.label))
            add(BLInstr("__aeabi_idiv"))
        }
    }

    private fun translateMultiply(node: BinOpNode) {
        ctx.text.apply {
            ctx.addLibraryFunction(OverflowError)

            loadOperandsIntoRegister(node)

            add(SMULLInstr(Register.R0, Register.R1, Register.R0, Register.R1))
            add(
                CMPInstr(
                    Register.R1,
                    ShiftOp(Register.R0, ShiftType.ASR, NumOp(31))
                )
            )
            add(BLNEInstr(OverflowError.label))
        }
    }

    private fun translatePlusMinus(node: BinOpNode, isPlus: Boolean) {
        ctx.text.apply {
            ctx.addLibraryFunction(OverflowError)

            loadOperandsIntoRegister(node)

            if (isPlus)
                add(ADDSInstr(Register.R0, Register.R0, Register.R1))
            else
                add(SUBSInstr(Register.R0, Register.R0, Register.R1))

            add(BLVSInstr(OverflowError.label))
        }
    }

    private fun translateLogical(node: BinOpNode, isAnd: Boolean) {
        ctx.text.apply {
            node.firstExpr.acceptCodeGenVisitor(this@CodeGeneratorVisitor)

            if (isAnd)
                add(CMPInstr(Register.R0, NumOp(0)))
            else
                add(CMPInstr(Register.R0, NumOp(1)))

            val branchFirstOp = ctx.labelCounter
            add(BEQInstr("L$branchFirstOp"))
            node.secondExpr.acceptCodeGenVisitor(this@CodeGeneratorVisitor)

            add(LabelInstr("L$branchFirstOp"))
            if (isAnd)
                add(CMPInstr(Register.R0, NumOp(0)))

        }
    }

    fun translateUnOp(node: UnOpNode) {
        when (node.operator) {
            UnaryOperator.NEGATE -> translateNegate(node)
            UnaryOperator.CHR, UnaryOperator.ORD ->
                node.expr.acceptCodeGenVisitor(this)
            UnaryOperator.MINUS -> translateMinus(node)
            UnaryOperator.LEN -> ctx.text.apply {
                node.expr.acceptCodeGenVisitor(this@CodeGeneratorVisitor)
                add(LDRInstr(Register.R0, MemAddr(Register.R0)))
            }
        }
    }

    private fun translateNegate(node: UnOpNode) =
        ctx.text.apply {
            node.expr.acceptCodeGenVisitor(this@CodeGeneratorVisitor)
            add(EORInstr(Register.R0, Register.R0, NumOp(1)))
        }

    private fun translateMinus(node: UnOpNode) =
        ctx.text.apply {
            ctx.addLibraryFunction(OverflowError)
            node.expr.acceptCodeGenVisitor(this@CodeGeneratorVisitor)
            add(RSBSInstr(Register.R0, Register.R0, NumOp(0)))
            add(BLVSInstr(OverflowError.label))
        }

    fun translatePairElem(node: PairElemNode) {
        ctx.addLibraryFunction(CheckNullPointer)

        val memOffset = if (node.isFirst) 0 else 4

        if (node.mode == AccessMode.READ)
            loadFromPosition(node, memOffset)
        else
            assignToPosition(node, memOffset)
    }

    fun translateArrayElement(elem: ArrayElement) {
        when (elem.mode) {
            AccessMode.READ -> translateArrayRead(elem)
            else -> translateArrayAssignment(elem)
        }
    }

    private fun translateArrayAssignment(elem: ArrayElement) {
        ctx.text.apply {
            ctx.addLibraryFunction(CheckArrayBounds)

            add(pushAndIncrement(ctx, Register.R0, Register.R4))
            add(
                loadLocalVar(
                    varType = ArrayType(elem.type, null),
                    stackOffset = ctx.getOffsetOfVar(elem.name, elem.st),
                    rd = Register.R4
                )
            )

            elem.arrIndices.dropLast(1).forEach {
                it.acceptCodeGenVisitor(this@CodeGeneratorVisitor)
                checkArrayBounds(elem.type)
                add(LDRInstr(Register.R4, MemAddr(Register.R4)))
            }
            elem.arrIndices.last()
                .acceptCodeGenVisitor(this@CodeGeneratorVisitor)
            checkArrayBounds(elem.type)

            add(MOVInstr(Register.R1, Register.R4))
            add(popAndDecrement(ctx, Register.R0, Register.R4))
            add(
                readOrAssign(
                    varType = elem.type,
                    stackOffset = 0,
                    mode = elem.mode,
                    rn = Register.R0,
                    rd = Register.R1
                )
            )
        }
    }

    private fun translateArrayRead(elem: ArrayElement) {
        ctx.text.apply {
            ctx.addLibraryFunction(CheckArrayBounds)

            val offset = ctx.getOffsetOfVar(elem.name, elem.st)
            add(
                LDRInstr(
                    Register.R0,
                    MemAddr(Register.SP, NumOp(offset))
                )
            )
            add(pushAndIncrement(ctx, Register.R4))
            add(MOVInstr(Register.R4, Register.R0))

            // Load and check bounds for each dereference
            elem.arrIndices.forEach {
                it.acceptCodeGenVisitor(this@CodeGeneratorVisitor)
                checkArrayBounds(elem.type)
                add(
                    loadLocalVar(
                        varType = elem.type,
                        stackOffset = 0,
                        rn = Register.R4,
                        rd = Register.R4
                    )
                )
            }

            add(MOVInstr(Register.R0, Register.R4))
            add(popAndDecrement(ctx, Register.R4))
        }
    }

    private fun checkArrayBounds(type: Type) {
        ctx.text.apply {
            add(BLInstr(CheckArrayBounds.label))
            add(ADDInstr(Register.R4, Register.R4, NumOp(4)))

            add(
                when (type) {
                    is CharType, is BoolType -> ADDInstr(
                        Register.R4,
                        Register.R4,
                        Register.R0
                    )
                    else -> ADDInstr(
                        Register.R4,
                        Register.R4,
                        ShiftOp(Register.R0, ShiftType.LSL, NumOp(2))
                    )
                }
            )
        }
    }

    fun translateArrayLiteral(literal: ArrayLiteral) {
        ctx.text.apply {
            add(
                MOVInstr(
                    Register.R0,
                    NumOp(literal.values.size * literal.elemType.reserveStackSize + 4)
                )
            )
            add(BLInstr("malloc"))
            add(MOVInstr(Register.R3, Register.R0))

            literal.values.forEachIndexed { index, arrayElem ->
                arrayElem.acceptCodeGenVisitor(this@CodeGeneratorVisitor)
                add(
                    storeLocalVar(
                        literal.elemType,
                        index * literal.elemType.reserveStackSize + 4,
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

    fun translateBoolLiteral(literal: BoolLiteral) {
        ctx.text.add(
            when (literal.value) {
                true -> MOVInstr(Register.R0, NumOp(1))
                false -> MOVInstr(Register.R0, NumOp(0))
            }
        )
    }

    fun translateCharLiteral(literal: CharLiteral) {
        ctx.text.add(MOVInstr(Register.R0, CharOp(literal.value)))
    }

    fun translateIdentifier(node: IdentifierNode) {
        ctx.text.apply {
            val offset = ctx.getOffsetOfVar(node.name, node.st)

            add(
                when (node.mode) {
                    AccessMode.ASSIGN -> storeLocalVar(node.type, offset)
                    AccessMode.READ -> loadLocalVar(node.type, offset)
                    else -> ADDInstr(
                        Register.R0,
                        Register.SP,
                        NumOp(offset)
                    )
                }
            )
        }
    }

    fun translateIntLiteral(literal: IntLiteral) {
        ctx.text.add(
            LDRInstr(
                Register.R0,
                NumOp(literal.value, isLoad = true)
            )
        )
    }

    fun translatePairLiteral(literal: PairLiteral) {
        ctx.text.add(MOVInstr(Register.R0, NumOp(0)))
    }

    fun translateStringLiteral(literal: StringLiteral) {
        ctx.text.add(
            LDRInstr(
                Register.R0,
                LabelOp(ctx.addMessage(literal.value))
            )
        )
    }

    private fun storeElemInHeap(elem: ExprNode) {
        ctx.text.apply {
            elem.acceptCodeGenVisitor(this@CodeGeneratorVisitor)
            add(pushAndIncrement(ctx, Register.R0))
            add(MOVInstr(Register.R0, NumOp(elem.type.reserveStackSize)))
            add(BLInstr("malloc"))
            add(popAndDecrement(ctx, Register.R1))
            add(
                storeLocalVar(
                    varType = elem.type,
                    stackOffset = 0,
                    rn = Register.R1,
                    rd = Register.R0
                )
            )
            add(pushAndIncrement(ctx, Register.R0))
        }
    }

    private fun loadFromPosition(node: PairElemNode, memOffset: Int) {
        ctx.text.apply {
            node.expr.acceptCodeGenVisitor(this@CodeGeneratorVisitor)
            add(BLInstr(CheckNullPointer.label))
            add(
                LDRInstr(
                    Register.R0,
                    MemAddr(Register.R0, NumOp(memOffset))
                )
            )
            add(
                loadLocalVar(
                    varType = node.type,
                    stackOffset = 0,
                    rd = Register.R0,
                    rn = Register.R0
                )
            )
        }
    }

    private fun assignToPosition(node: PairElemNode, memOffset: Int) =
        ctx.text.apply {
            add(pushAndIncrement(ctx, Register.R0))

            val stackOffset =
                ctx.getOffsetOfVar(
                    (node.expr as IdentifierNode).name,
                    node.st
                )
            add(
                LDRInstr(
                    Register.R0,
                    MemAddr(Register.SP, NumOp(stackOffset))
                )
            )

            ctx.addLibraryFunction(CheckNullPointer)
            add(BLInstr(CheckNullPointer.label))
            add(ADDInstr(Register.R0, Register.R0, NumOp(memOffset)))

            add(pushAndIncrement(ctx, Register.R0))
            add(LDRInstr(Register.R0, MemAddr(Register.R0)))

            //Free existing value
            add(BLInstr("free"))

            // Allocate new value
            add(MOVInstr(Register.R0, NumOp(node.type.reserveStackSize)))
            add(BLInstr("malloc"))
            add(popAndDecrement(ctx, Register.R1))
            add(STRInstr(Register.R0, MemAddr(Register.R1)))
            add(MOVInstr(Register.R1, Register.R0))
            add(popAndDecrement(ctx, Register.R0))
            add(
                readOrAssign(
                    varType = node.type,
                    stackOffset = 0,
                    mode = node.mode,
                    rn = Register.R0,
                    rd = Register.R1
                )
            )
        }

    private fun readOrAssign(
        varType: Type,
        stackOffset: Int,
        mode: AccessMode,
        rn: Register,
        rd: Register
    ) =
        if (mode == AccessMode.ASSIGN)
            storeLocalVar(
                varType = varType,
                stackOffset = stackOffset,
                rn = rn,
                rd = rd
            )
        else
            ADDInstr(rn, rd, NumOp(0))

    private fun translateEquality(node: BinOpNode, isEqual: Boolean) {
        ctx.text.apply {
            loadOperandsIntoRegister(node)

            add(CMPInstr(Register.R0, Register.R1))

            if (isEqual) {
                add(MOVEQInstr(Register.R0, NumOp(1)))
                add(MOVNEInstr(Register.R0, NumOp(0)))
            } else {
                add(MOVNEInstr(Register.R0, NumOp(1)))
                add(MOVEQInstr(Register.R0, NumOp(0)))
            }
        }
    }

    private fun loadOperandsIntoRegister(node: BinOpNode) {
        ctx.text.apply {
            ctx.addLibraryFunction(DivideByZeroError)

            node.firstExpr.acceptCodeGenVisitor(this@CodeGeneratorVisitor)
            add(pushAndIncrement(ctx, Register.R0))

            node.secondExpr.acceptCodeGenVisitor(this@CodeGeneratorVisitor)
            add(MOVInstr(Register.R1, Register.R0))
            add(popAndDecrement(ctx, Register.R0))
        }
    }

    fun translateBegin(node: BeginNode) {
        ctx.text.apply {
            newScope(node.currST) {
                node.stat.acceptCodeGenVisitor(this@CodeGeneratorVisitor)
            }
        }
    }

    fun translateFree(node: FreeNode) {
        ctx.text.apply {
            ctx.addLibraryFunction(FreePair)

            node.value.acceptCodeGenVisitor(this@CodeGeneratorVisitor)
            add(BLInstr(FreePair.label))
        }
    }

    fun translateIf(node: IfNode) {
        ctx.text.apply {
            node.proposition.acceptCodeGenVisitor(this@CodeGeneratorVisitor)
            add(CMPInstr(Register.R0, NumOp(0)))

            val falseBranchIndex = ctx.labelCounter
            val continueBranch = ctx.labelCounter

            add(BEQInstr("L$falseBranchIndex"))
            newScope(node.trueST) {
                node.trueStat.acceptCodeGenVisitor(this@CodeGeneratorVisitor)
            }
            add(BInstr("L$continueBranch"))

            add(LabelInstr("L$falseBranchIndex"))
            newScope(node.falseST) {
                node.falseStat.acceptCodeGenVisitor(this@CodeGeneratorVisitor)
            }
            add(LabelInstr("L$continueBranch"))
        }
    }

    fun translatePrint(node: PrintNode) {
        val value = node.value
        ctx.text.apply {
            value.acceptCodeGenVisitor(this@CodeGeneratorVisitor)
            if (value.type == CharType) {
                add(BLInstr("putchar"))
            } else {

                val printFunc = when {
                    value.type == ArrayType(
                        CharType,
                        null
                    ) -> getPrintOption(
                        StringType
                    )
                    value is ArrayElement -> {

                        if (value.type is ArrayType && (value.type as ArrayType).elementType == CharType)
                            getPrintOption(StringType)
                        else
                            getPrintOption(value.type)
                    }

                    else -> getPrintOption(value.type)
                }

                ctx.addLibraryFunction(printFunc)
                add(BLInstr(printFunc.label))
            }

            if (node.returnAfterPrint) {
                ctx.addLibraryFunction(PrintLn)
                add(BLInstr(PrintLn.label))
            }
        }
    }

    private fun getPrintOption(exprType: Type) =
        when (exprType) {
            IntType -> PrintInt
            StringType -> PrintStr
            BoolType -> PrintBool
            is GenericPair, is ArrayType -> PrintReference
            else -> throw UnexpectedException(
                "Else branch should not be reached for operator $exprType"
            )
        }

    fun translateRead(node: ReadNode) {
        ctx.text.apply {
            node.value.acceptCodeGenVisitor(this@CodeGeneratorVisitor)

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

    fun translateReturn(node: ReturnNode) {
        ctx.text.apply {
            node.value.acceptCodeGenVisitor(this@CodeGeneratorVisitor)
            endAllScopes(node.st)
            add(POPInstr(Register.PC))
        }
    }

    fun translateSeq(node: SeqNode) {
        node.sequence.forEach { it.acceptCodeGenVisitor(this) }
    }

    fun translateSkip(node: SkipNode) {}

    fun translateWhile(node: WhileNode) {
        ctx.text.apply {
            val bodyIndex = ctx.labelCounter
            val propositionIndex = ctx.labelCounter

            add(BInstr("L$propositionIndex"))

            add(LabelInstr("L$bodyIndex"))

            newScope(node.bodyST) {
                node.body.acceptCodeGenVisitor(this@CodeGeneratorVisitor)
            }

            add(LabelInstr("L$propositionIndex"))
            node.proposition.acceptCodeGenVisitor(this@CodeGeneratorVisitor)
            add(CMPInstr(Register.R0, NumOp(1)))
            add(BEQInstr("L$bodyIndex"))
        }
    }

}

