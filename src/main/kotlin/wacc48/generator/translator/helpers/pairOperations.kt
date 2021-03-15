package wacc48.generator.translator.helpers

import wacc48.generator.instructions.Syscall
import wacc48.generator.instructions.arithmetic.ADDInstr
import wacc48.generator.instructions.branch.BLInstr
import wacc48.generator.instructions.load.LDRInstr
import wacc48.generator.instructions.move.MOVInstr
import wacc48.generator.instructions.operands.ArgumentAddr
import wacc48.generator.instructions.operands.MemAddr
import wacc48.generator.instructions.operands.NumOp
import wacc48.generator.instructions.operands.Register
import wacc48.generator.instructions.store.STRInstr
import wacc48.generator.translator.CodeGeneratorVisitor
import wacc48.generator.translator.lib.errors.CheckNullPointer
import wacc48.tree.nodes.assignment.AccessMode
import wacc48.tree.nodes.assignment.PairElemNode
import wacc48.tree.nodes.expr.ExprNode
import wacc48.tree.nodes.expr.IdentifierNode
import wacc48.tree.type.Type

/** Evaluates the expression [elem], allocate memory for the result and store
 * the result of the evaluation into allocated memory.
 */
fun CodeGeneratorVisitor.storeElemInHeap(elem: ExprNode) {
    visitNode(elem)

    ctx.text.addAll(
        listOf(
            pushAndIncrement(ctx, Register.R0),
            MOVInstr(Register.R0, NumOp(elem.type.reserveStackSize)),
            Syscall("malloc"),
            popAndDecrement(ctx, Register.R1),
            storeLocalVar(
                varType = elem.type,
                stackOffset = 0,
                rn = Register.R1,
                rd = Register.R0
            ),
            pushAndIncrement(ctx, Register.R0)
        )
    )
}

/** Loads an element in a pair [node] into register R0 from memory location
 * calculated by adding the value inside register [Register.R0] and [memOffset].
 *
 * Pre-condition: the memory address of the pair is already in register R0. */
fun CodeGeneratorVisitor.loadFromPosition(node: PairElemNode, memOffset: Int) {
    visitNode(node.expr)

    ctx.text.addAll(
        listOf(
            BLInstr(CheckNullPointer.label),
            LDRInstr(
                Register.R0,
                MemAddr(Register.R0, NumOp(memOffset))
            ),
            loadLocalVar(
                varType = node.type,
                stackOffset = 0,
                rd = Register.R0,
                rn = Register.R0
            )
        )
    )
}

/** Assigns a new value to a pair element [node] (i.e. either the 1st or 2nd
 * element in a pair).
 *
 *  First check if the address of the pair is [ArmConstants.NULL_ADDRESS].
 *  If false, then we free the old pair element.
 *
 *  Pre-condition: the value to be stored already in R0
 */
fun CodeGeneratorVisitor.assignToPosition(node: PairElemNode, memOffset: Int) {
    ctx.addLibraryFunction(CheckNullPointer)
    ctx.text.add(pushAndIncrement(ctx, Register.R0))

    val (offset, isArg) =
        ctx.getOffsetOfVar(
            (node.expr as IdentifierNode).name,
            node.st
        )

    val memoryLocation = if (isArg)
        ArgumentAddr(Register.SP, NumOp(offset))
    else
        MemAddr(Register.SP, NumOp(offset))

    ctx.text.addAll(
        listOf(

            // Loads address of pair into R0 and check for NULL
            LDRInstr(Register.R0, memoryLocation),
            BLInstr(CheckNullPointer.label),

            // Loads address of the element in context into R0 and saves it
            ADDInstr(Register.R0, Register.R0, NumOp(memOffset)),
            pushAndIncrement(ctx, Register.R0),

            LDRInstr(Register.R0, MemAddr(Register.R0)),

            //Free existing value
            Syscall("free"),

            // Allocate memory for new value
            MOVInstr(
                Register.R0,
                NumOp(node.type.reserveStackSize)
            ),
            Syscall("malloc"),

            // Stores the new value into the memory address of the pair element
            popAndDecrement(ctx, Register.R1),
            STRInstr(Register.R0, MemAddr(Register.R1)),
            MOVInstr(Register.R1, Register.R0),
            popAndDecrement(ctx, Register.R0),

            readOrAssign(
                varType = node.type,
                stackOffset = 0,
                mode = node.mode,
                rn = Register.R0,
                rd = Register.R1
            )
        )
    )

}

fun readOrAssign(
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
