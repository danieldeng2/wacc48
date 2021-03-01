package analyser.nodes.assignment

import analyser.SymbolTable
import analyser.nodes.function.FuncNode
import analyser.nodes.statement.StatNode
import exceptions.SemanticsException
import generator.instructions.Instruction
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext

data class AssignmentNode(
    val name: LHSNode,
    val value: RHSNode,
    val ctx: ParserRuleContext?
) : StatNode {

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        name.mode = AccessMode.ASSIGN
        name.validate(st, funTable)
        value.validate(st, funTable)

        if (name.type != value.type)
            throw SemanticsException(
                "Attempt to assign ${value.type} to ${name.type}",
                ctx
            )
    }

    override fun translate(ctx: TranslatorContext): List<Instruction> =
        mutableListOf<Instruction>().apply {
            addAll(value.translate(ctx))
            addAll(name.translate(ctx))
        }
}