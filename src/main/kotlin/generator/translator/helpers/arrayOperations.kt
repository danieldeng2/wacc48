package generator.translator.helpers

import datastructures.nodes.expr.ArrayElement
import datastructures.type.ArrayType
import datastructures.type.BoolType
import datastructures.type.CharType
import datastructures.type.Type
import generator.instructions.arithmetic.ADDInstr
import generator.instructions.branch.BLInstr
import generator.instructions.load.LDRInstr
import generator.instructions.move.MOVInstr
import generator.instructions.operands.*
import generator.translator.ArmConstants.NUM_BYTE_ADDRESS
import generator.translator.CodeGeneratorVisitor
import generator.translator.lib.errors.CheckArrayBounds

fun CodeGeneratorVisitor.translateArrayAssignment(elem: ArrayElement) {
    ctx.addLibraryFunction(CheckArrayBounds)

    ctx.text.apply {

        add(pushAndIncrement(ctx, Register.R0, Register.R4))
        add(
            loadLocalVar(
                varType = ArrayType(elem.type, null),
                stackOffset = ctx.getOffsetOfVar(elem.name, elem.st),
                rd = Register.R4
            )
        )

        elem.arrIndices.dropLast(1).forEach {
            visitAndTranslate(it)
            checkArrayBounds(elem.type)
            add(LDRInstr(Register.R4, MemAddr(Register.R4)))
        }
        visitAndTranslate(elem.arrIndices.last())
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

fun CodeGeneratorVisitor.translateArrayRead(elem: ArrayElement) {
    ctx.addLibraryFunction(CheckArrayBounds)

    ctx.text.apply {

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
            visitAndTranslate(it)
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

fun CodeGeneratorVisitor.checkArrayBounds(type: Type) {
    ctx.text.addAll(
        listOf(
            BLInstr(CheckArrayBounds.label),
            ADDInstr(Register.R4, Register.R4, NumOp(NUM_BYTE_ADDRESS)),

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
    )
}
