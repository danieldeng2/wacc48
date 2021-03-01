package analyser.nodes.assignment

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.expr.IdentifierNode
import analyser.nodes.function.FuncNode
import analyser.nodes.type.PairType
import analyser.nodes.type.Type
import analyser.nodes.type.VoidType
import exceptions.SemanticsException
import generator.instructions.Instruction
import generator.instructions.arithmetic.ADDInstr
import generator.instructions.branch.BLInstr
import generator.instructions.load.LDRInstr
import generator.instructions.move.MOVInstr
import generator.instructions.operands.MemAddr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.instructions.store.STRInstr
import generator.translator.TranslatorContext
import generator.translator.lib.errors.CheckNullPointer
import generator.translator.loadLocalVar
import generator.translator.popAndDecrement
import generator.translator.pushAndIncrement
import org.antlr.v4.runtime.ParserRuleContext

data class PairElemNode(
    private val expr: ExprNode,
    private val isFirst: Boolean,
    val ctx: ParserRuleContext?
) : LHSNode, RHSNode {
    override var type: Type = VoidType
    lateinit var st: SymbolTable
    override var mode: AccessMode = AccessMode.READ

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
        expr.validate(st, funTable)

        if (expr.type !is PairType)
            throw SemanticsException("Cannot dereference pair $expr", ctx)

        val nameType = expr.type
        if (nameType is PairType) {
            type = when {
                isFirst -> nameType.firstType
                else -> nameType.secondType
            }
        }
    }

    override fun translate(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            ctx.addLibraryFunction(CheckNullPointer)

            val memOffset = if (isFirst) 0 else 4

            if (mode == AccessMode.READ)
                addAll(loadFromPosition(ctx, memOffset))
            else
                addAll(assignToPosition(ctx, memOffset))

        }

    private fun loadFromPosition(ctx: TranslatorContext, memOffset: Int) =
        mutableListOf<Instruction>().apply {
            addAll(expr.translate(ctx))
            add(BLInstr(CheckNullPointer.label))
            add(LDRInstr(Register.R0, MemAddr(Register.R0, NumOp(memOffset))))
            add(
                loadLocalVar(
                    varType = type,
                    stackOffset = 0,
                    rd = Register.R0,
                    rn = Register.R0
                )
            )
        }

    private fun assignToPosition(ctx: TranslatorContext, memOffset: Int) =
        mutableListOf<Instruction>().apply {
            add(pushAndIncrement(ctx, Register.R0))

            val stackOffset = ctx.getOffsetOfVar((expr as IdentifierNode).name, st)
            add(LDRInstr(Register.R0, MemAddr(Register.SP, NumOp(stackOffset))))

            ctx.addLibraryFunction(CheckNullPointer)
            add(BLInstr(CheckNullPointer.label))
            add(ADDInstr(Register.R0, Register.R0, NumOp(memOffset)))

            add(pushAndIncrement(ctx, Register.R0))
            add(LDRInstr(Register.R0, MemAddr(Register.R0)))

            //Free existing value
            add(BLInstr("free"))

            // Allocate new value
            add(MOVInstr(Register.R0, NumOp(type.reserveStackSize)))
            add(BLInstr("malloc"))
            add(popAndDecrement(ctx, Register.R1))
            add(STRInstr(Register.R0, MemAddr(Register.R1)))
            add(MOVInstr(Register.R1, Register.R0))
            add(popAndDecrement(ctx, Register.R0))
            add(
                readOrAssign(
                    varType = type,
                    stackOffset = 0,
                    rn = Register.R0,
                    rd = Register.R1
                )
            )
        }

}