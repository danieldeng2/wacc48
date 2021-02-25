package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.type.BoolType
import exceptions.SemanticsException
import generator.TranslatorContext
import generator.armInstructions.*
import generator.armInstructions.operands.NumOp
import generator.armInstructions.operands.Register
import org.antlr.v4.runtime.ParserRuleContext

data class IfNode(
    val proposition: ExprNode,
    val trueStat: StatNode,
    val falseStat: StatNode,
    override val ctx: ParserRuleContext?,
) : StatNode {
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable

        if (proposition.type != BoolType)
            throw SemanticsException(
                "If statement proposition must be boolean",
                ctx
            )

        proposition.validate(st, funTable)
        trueStat.validate(SymbolTable(st), funTable)
        falseStat.validate(SymbolTable(st), funTable)
    }

    override fun translate(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            addAll(proposition.translate(ctx))
            add(CMPInstr(Register.R0, NumOp(0)))

            val falseBranchIndex = ctx.getAndIncLabelCnt()
            val continueBranch = ctx.getAndIncLabelCnt()

            add(BEQInstr("L$falseBranchIndex"))
            addAll(trueStat.translate(ctx))
            add(BInstr("L$continueBranch"))

            add(LabelInstr("L$falseBranchIndex"))
            addAll(falseStat.translate(ctx))

            add(LabelInstr("L$continueBranch"))
        }
}