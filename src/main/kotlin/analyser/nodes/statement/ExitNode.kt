package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.function.FuncNode
import analyser.nodes.type.IntType
import exceptions.SemanticsException
import generator.instructions.Instruction
import generator.instructions.branch.BLInstr
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext

data class ExitNode(
    val value: ExprNode,
    override val ctx: ParserRuleContext?
) : StatNode {
    override lateinit var st: SymbolTable


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
        value.validate(st, funTable)
        if (value.type != IntType)
            throw SemanticsException(
                "Exit must take integer as input, got ${value.type} instead",
                ctx
            )
    }

    override fun translate(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            addAll(value.translate(ctx))
            add(BLInstr("exit"))
        }

}
