package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.assignment.RHSNode
import analyser.nodes.function.ParamNode
import exceptions.SemanticsException
import generator.translator.TranslatorContext
import generator.armInstructions.*
import org.antlr.v4.runtime.ParserRuleContext

data class DeclarationNode(
    val name: ParamNode,
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

        if (value.type != name.type)
            throw SemanticsException("Type mismatch in declaration $name", ctx)
    }

    override fun translate(ctx: TranslatorContext): List<Instruction> =
        mutableListOf<Instruction>().apply {
            addAll(value.translate(ctx))
            addAll(name.translate(ctx))
        }

}

