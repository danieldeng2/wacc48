package generator.translator.helpers

import generator.instructions.arithmetic.ADDInstr
import generator.instructions.branch.BLInstr
import generator.instructions.load.LDRInstr
import generator.instructions.move.MOVInstr
import generator.instructions.operands.*
import generator.translator.ArmConstants.NUM_BYTE_ADDRESS
import generator.translator.CodeGeneratorVisitor
import generator.translator.lib.errors.CheckArrayBounds
import tree.nodes.expr.ArrayElement
import tree.type.ArrayType
import tree.type.BoolType
import tree.type.CharType
import tree.type.Type

/** An example of a line of code that calls this:
 *  ```
 *   int[][][] a = ...;
 *   a[2][3][1] = 1;
 *  ```
 *
 *  Pre-condition: the value to assign is already in register R0
 *  (i.e. value 1 in this case).
 *
 *  Iterate through the list of indices, at each step load the corresponding
 *  array into memory after checking for invalid array access.
 *
 */
fun CodeGeneratorVisitor.translateArrayAssignment(elem: ArrayElement) {
    ctx.addLibraryFunction(CheckArrayBounds)

    ctx.text.apply {

        add(pushAndIncrement(ctx, Register.R0, Register.R4))

        val (offset, isArg) = ctx.getOffsetOfVar(elem.name, elem.st)

        add(
            loadLocalVar(
                varType = ArrayType(elem.type, null),
                stackOffset = offset,
                rd = Register.R4,
                isArgument = isArg
            )
        )

        elem.arrIndices.dropLast(1).forEach {
            visitNode(it)
            checkArrayBounds(elem.type)
            add(LDRInstr(Register.R4, MemAddr(Register.R4)))
        }
        visitNode(elem.arrIndices.last())
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

/** This function is called when we are trying to read the value of an
 * element in an array.
 *
 * First, we load the address of the array into register R0, then R4.
 * An example of a statement that invoke this function is:
 * ```
 *  int[][][] a = ...;
 *  int b = a[2][3][1];
 * ```
 *
 * Then we iterate through the list of indices, in this case [2, 3, 1],
 * checking array bounds and loading the correct address of the child array
 * into memory up to the last one, and load the value (i.e 5) into the correct
 * address.
 * */
fun CodeGeneratorVisitor.translateArrayRead(elem: ArrayElement) {
    ctx.addLibraryFunction(CheckArrayBounds)

    ctx.text.apply {

        val (offset, isArg) = ctx.getOffsetOfVar(elem.name, elem.st)
        val memoryLocation = if (isArg)
            ArgumentAddr(Register.SP, NumOp(offset))
        else
            MemAddr(Register.SP, NumOp(offset))

        add(LDRInstr(Register.R0, memoryLocation))
        add(pushAndIncrement(ctx, Register.R4))
        add(MOVInstr(Register.R4, Register.R0))

        // Load and check bounds for each dereference
        elem.arrIndices.forEach {
            visitNode(it)
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

/** Precondition: register R4 contains the address of the array in context
 *
 * The first element at the address of the array is the size of the array
 * itself. Therefore, invoking [CheckArrayBounds] will do the bounds checking.
 * Then, if valid, move the address in Register R4 to actually point at the first
 * element in the array.
 */
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
