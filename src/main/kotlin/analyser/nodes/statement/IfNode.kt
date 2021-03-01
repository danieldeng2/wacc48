package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.function.FuncNode
import analyser.nodes.type.BoolType
import exceptions.SemanticsException
import generator.instructions.Instruction
import generator.instructions.branch.BEQInstr
import generator.instructions.branch.BInstr
import generator.instructions.compare.CMPInstr
import generator.instructions.directives.LabelInstr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.TranslatorContext
import generator.translator.newScope
import org.antlr.v4.runtime.ParserRuleContext

data class IfNode(
    val proposition: ExprNode,
    val trueStat: StatNode,
    val falseStat: StatNode,
    val ctx: ParserRuleContext?,
) : StatNode {
    lateinit var trueST: SymbolTable
    lateinit var falseST: SymbolTable

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.trueST = SymbolTable(st)
        this.falseST = SymbolTable(st)

        if (proposition.type != BoolType)
            throw SemanticsException(
                "If statement proposition must be boolean",
                ctx
            )

        proposition.validate(st, funTable)
        trueStat.validate(trueST, funTable)
        falseStat.validate(falseST, funTable)
    }

    override fun translate(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            addAll(proposition.translate(ctx))
            add(CMPInstr(Register.R0, NumOp(0)))

            val falseBranchIndex = ctx.labelCounter
            val continueBranch = ctx.labelCounter

            add(BEQInstr("L$falseBranchIndex"))
            newScope(trueST) {
                addAll(trueStat.translate(ctx))
            }
            add(BInstr("L$continueBranch"))

            add(LabelInstr("L$falseBranchIndex"))
            newScope(falseST) {
                addAll(falseStat.translate(ctx))
            }
            add(LabelInstr("L$continueBranch"))
        }
}