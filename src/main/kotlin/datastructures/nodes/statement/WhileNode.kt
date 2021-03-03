package datastructures.nodes.statement

import datastructures.SymbolTable
import analyser.exceptions.SemanticsException
import datastructures.nodes.expr.ExprNode
import datastructures.nodes.function.FuncNode
import datastructures.type.BoolType
import generator.instructions.Instruction
import generator.instructions.branch.BEQInstr
import generator.instructions.branch.BInstr
import generator.instructions.compare.CMPInstr
import generator.instructions.directives.LabelInstr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.TranslatorContext
import generator.translator.helpers.newScope
import org.antlr.v4.runtime.ParserRuleContext

data class WhileNode(
    val proposition: ExprNode,
    val body: StatNode,
    val ctx: ParserRuleContext?,
) : StatNode {
    private lateinit var bodyST: SymbolTable

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.bodyST = SymbolTable(st)
        proposition.validate(st, funTable)

        if (proposition.type != BoolType)
            throw SemanticsException("While statement proposition must be boolean", ctx)

        body.validate(bodyST, funTable)
    }

    override fun translate(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            val bodyIndex = ctx.labelCounter
            val propositionIndex = ctx.labelCounter

            add(BInstr("L$propositionIndex"))

            add(LabelInstr("L$bodyIndex"))

            newScope(bodyST) {
                addAll(body.translate(ctx))
            }

            add(LabelInstr("L$propositionIndex"))
            addAll(proposition.translate(ctx))
            add(CMPInstr(Register.R0, NumOp(1)))
            add(BEQInstr("L$bodyIndex"))
        }
}

