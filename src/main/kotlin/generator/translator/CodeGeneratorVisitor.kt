package generator.translator

import generator.instructions.Instruction
import generator.instructions.arithmetic.ADDInstr
import generator.instructions.branch.BEQInstr
import generator.instructions.branch.BInstr
import generator.instructions.branch.BLInstr
import generator.instructions.compare.CMPInstr
import generator.instructions.directives.Directive
import generator.instructions.directives.LabelInstr
import generator.instructions.load.LDRInstr
import generator.instructions.move.MOVInstr
import generator.instructions.operands.*
import generator.instructions.stack.POPInstr
import generator.instructions.stack.PUSHInstr
import generator.instructions.store.STRInstr
import generator.translator.ArmConstants.NULL_ADDRESS
import generator.translator.ArmConstants.NUM_BYTE_ADDRESS
import generator.translator.helpers.*
import generator.translator.lib.FreePair
import generator.translator.lib.errors.CheckNullPointer
import generator.translator.lib.print.PrintLn
import generator.translator.lib.read.ReadChar
import generator.translator.lib.read.ReadInt
import tree.nodes.ASTNode
import tree.nodes.ProgNode
import tree.nodes.assignment.AccessMode
import tree.nodes.assignment.AssignmentNode
import tree.nodes.assignment.NewPairNode
import tree.nodes.assignment.PairElemNode
import tree.nodes.expr.*
import tree.nodes.expr.operators.BinOpNode
import tree.nodes.expr.operators.BinaryOperator
import tree.nodes.expr.operators.UnOpNode
import tree.nodes.expr.operators.UnaryOperator
import tree.nodes.function.*
import tree.nodes.statement.*
import tree.type.ArrayType
import tree.type.CharType
import tree.type.IntType
import tree.type.StringType

class CodeGeneratorVisitor(private val rootNode: ASTNode) {
    val ctx = TranslatorContext()

    fun translate(): List<Instruction> {
        visitAndTranslate(rootNode)
        return ctx.assemble()
    }

    fun visitAndTranslate(node: ASTNode) {
        node.acceptCodeGenVisitor(this)
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
                visitAndTranslate(node.body)
            }
            add(MOVInstr(Register.R0, NumOp(0)))
            add(POPInstr(Register.PC))
        }
    }

    fun translateExit(node: ExitNode) {
        visitAndTranslate(node.expr)
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
            visitAndTranslate(node.body)

            add(Directive(".ltorg"))
        }
    }

    fun translateFuncCall(node: FuncCallNode) {
        visitAndTranslate(node.argList)

        ctx.text.apply {
            add(BLInstr("f_${node.name}"))

            if (node.argListSize != 0)
                add(ADDInstr(Register.SP, Register.SP, NumOp(node.argListSize)))
        }
    }

    fun translateParam(node: ParamNode) {
        val symbolTable = node.st
        symbolTable.declareVariable(node.text)
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
        visitAndTranslate(node.value)
        visitAndTranslate(node.name)

        val offset = ctx.getOffsetOfVar(node.name.text, node.st)
        ctx.text.add(storeLocalVar(node.name.type, offset))
    }

    fun translateArgList(node: ArgListNode) {

        ctx.text.apply {
            val stackPtrTemp = ctx.stackPtrOffset

            node.args.asReversed().forEach {
                visitAndTranslate(it)
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
        visitAndTranslate(node.value)
        visitAndTranslate(node.name)
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

    fun translateUnOp(node: UnOpNode) {
        when (node.operator) {
            UnaryOperator.NEGATE -> translateNegate(node)
            UnaryOperator.CHR, UnaryOperator.ORD -> visitAndTranslate(node.expr)
            UnaryOperator.MINUS -> translateMinus(node)
            UnaryOperator.LEN -> ctx.text.apply {
                visitAndTranslate(node.expr)
                add(LDRInstr(Register.R0, MemAddr(Register.R0)))
            }
        }
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

    fun translateArrayLiteral(literal: ArrayLiteral) {
        ctx.text.apply {
            add(
                MOVInstr(
                    Register.R0,
                    NumOp(literal.values.size * literal.elemType.reserveStackSize + NUM_BYTE_ADDRESS)
                )
            )
            add(BLInstr("malloc"))
            add(MOVInstr(Register.R3, Register.R0))

            literal.values.forEachIndexed { index, arrayElem ->
                visitAndTranslate(arrayElem)
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
        val offset = ctx.getOffsetOfVar(node.name, node.st)
        ctx.text.add(
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

    fun translateIntLiteral(literal: IntLiteral) {
        ctx.text.add(
            LDRInstr(
                Register.R0,
                NumOp(literal.value, isLoad = true)
            )
        )
    }

    fun translatePairLiteral(literal: PairLiteral) {
        ctx.text.add(MOVInstr(Register.R0, NumOp(NULL_ADDRESS)))
    }

    fun translateStringLiteral(literal: StringLiteral) {
        ctx.text.add(
            LDRInstr(
                Register.R0,
                LabelOp(ctx.addMessage(literal.value))
            )
        )
    }

    fun translateBegin(node: BeginNode) {
        ctx.text.apply {
            newScope(node.currST) {
                visitAndTranslate(node.stat)
            }
        }
    }

    fun translateFree(node: FreeNode) {
        ctx.text.apply {
            ctx.addLibraryFunction(FreePair)

            visitAndTranslate(node.value)
            add(BLInstr(FreePair.label))
        }
    }

    fun translateIf(node: IfNode) {
        ctx.text.apply {
            visitAndTranslate(node.proposition)
            add(CMPInstr(Register.R0, NumOp(0)))

            val falseBranchIndex = ctx.labelCounter
            val continueBranch = ctx.labelCounter

            add(BEQInstr("L$falseBranchIndex"))
            newScope(node.trueST) {
                visitAndTranslate(node.trueStat)
            }
            add(BInstr("L$continueBranch"))

            add(LabelInstr("L$falseBranchIndex"))
            newScope(node.falseST) {
                visitAndTranslate(node.falseStat)
            }
            add(LabelInstr("L$continueBranch"))
        }
    }

    fun translatePrint(node: PrintNode) {
        val value = node.value
        visitAndTranslate(value)

        ctx.text.apply {
            if (value.type == CharType) {
                add(BLInstr("putchar"))
            } else {

                val printFunc = when {
                    value.type == ArrayType(CharType, null) -> getPrintOption(
                        StringType
                    )
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

    fun translateRead(node: ReadNode) {
        ctx.text.apply {
            visitAndTranslate(node.value)

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
            visitAndTranslate(node.value)
            endAllScopes(node.st)
            add(POPInstr(Register.PC))
        }
    }

    fun translateSeq(node: SeqNode) {
        node.sequence.forEach { visitAndTranslate(it) }
    }

    fun translateWhile(node: WhileNode) {
        ctx.text.apply {
            val bodyIndex = ctx.labelCounter
            val propositionIndex = ctx.labelCounter

            add(BInstr("L$propositionIndex"))

            add(LabelInstr("L$bodyIndex"))

            newScope(node.bodyST) {
                visitAndTranslate(node.body)
            }

            add(LabelInstr("L$propositionIndex"))
            visitAndTranslate(node.proposition)
            add(CMPInstr(Register.R0, NumOp(1)))
            add(BEQInstr("L$bodyIndex"))
        }
    }

}