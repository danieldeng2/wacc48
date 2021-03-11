package generator.translator.helpers

import generator.instructions.Syscall
import tree.nodes.assignment.AccessMode
import tree.nodes.assignment.PairElemNode
import tree.nodes.expr.ExprNode
import tree.nodes.expr.IdentifierNode
import tree.type.Type
import generator.instructions.arithmetic.ADDInstr
import generator.instructions.branch.BLInstr
import generator.instructions.load.LDRInstr
import generator.instructions.move.MOVInstr
import generator.instructions.operands.MemAddr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.instructions.store.STRInstr
import generator.translator.CodeGeneratorVisitor
import generator.translator.lib.errors.CheckNullPointer

/** Evaluates the expression [elem], allocate memory for the result and store
 * the result of the evaluation into allocated memory.
 */
fun CodeGeneratorVisitor.storeElemInHeap(elem: ExprNode) {
    visitAndTranslate(elem)

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
    visitAndTranslate(node.expr)

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

    val stackOffset =
        ctx.getOffsetOfVar(
            (node.expr as IdentifierNode).name,
            node.st
        )

    ctx.text.addAll(
        listOf(

            // Loads address of pair into R0 and check for NULL
            LDRInstr(
                Register.R0,
                MemAddr(Register.SP, NumOp(stackOffset))
            ),
            BLInstr(CheckNullPointer.label),

            // Loads address of the element in context into R0 and saves it
            ADDInstr(Register.R0, Register.R0, NumOp(memOffset)),
            pushAndIncrement(ctx, Register.R0),

            LDRInstr(Register.R0, MemAddr(Register.R0)),

            //Free existing value
            BLInstr("free"),

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
