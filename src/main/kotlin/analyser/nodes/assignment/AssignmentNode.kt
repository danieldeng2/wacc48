package analyser.nodes.assignment

import analyser.SymbolTable
import analyser.nodes.statement.StatNode
import exceptions.SemanticsException
import generator.translator.TranslatorContext
import generator.armInstructions.Instruction
import org.antlr.v4.runtime.ParserRuleContext

data class AssignmentNode(
    val name: LHSNode,
    val value: RHSNode,
    override val ctx: ParserRuleContext?
) : StatNode {
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
        name.validate(st, funTable)
        value.validate(st, funTable)

        if (name.type != value.type)
            throw SemanticsException(
                "Attempt to assign ${value.type} to ${name.type}",
                ctx
            )
    }

    override fun translate(ctx: TranslatorContext): List<Instruction> {
        val instructions = value.translate(ctx).toMutableList()
        ctx.isDeclaring = true
        instructions.addAll(name.translate(ctx))
        ctx.isDeclaring = false

        return instructions
    }
}