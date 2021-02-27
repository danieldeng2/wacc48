package analyser.nodes.assignment

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.type.PairType
import analyser.nodes.type.Type
import analyser.nodes.type.VoidType
import exceptions.SemanticsException
import generator.instructions.Instruction
import generator.instructions.branch.BLInstr
import generator.instructions.load.LDRInstr
import generator.instructions.operands.MemAddr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.TranslatorContext
import generator.translator.lib.errors.CheckNullPointer
import generator.translator.loadLocalVar
import org.antlr.v4.runtime.ParserRuleContext

data class PairElemNode(
    private val expr: ExprNode,
    private val isFirst: Boolean,
    override val ctx: ParserRuleContext?
) : LHSNode, RHSNode {
    override var type: Type = VoidType
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable
    override var mode: AccessMode = AccessMode.READ

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
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

            addAll(expr.translate(ctx))
            add(BLInstr(CheckNullPointer.label))

            val memOffset = if (isFirst) 0 else 4

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

}