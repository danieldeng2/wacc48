package datastructures.nodes.statement

import analyser.exceptions.SemanticsException
import datastructures.SymbolTable
import datastructures.nodes.assignment.RHSNode
import datastructures.nodes.function.FuncNode
import datastructures.nodes.function.ParamNode
import generator.instructions.Instruction
import generator.translator.TranslatorContext
import generator.translator.helpers.storeLocalVar
import org.antlr.v4.runtime.ParserRuleContext

data class DeclarationNode(
    val name: ParamNode,
    val value: RHSNode,
    val ctx: ParserRuleContext?
) : StatNode {
    private lateinit var st: SymbolTable

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
        name.validate(st, funTable)
        value.validate(st, funTable)

        if (value.type != name.type)
            throw SemanticsException("Type mismatch in declaration $name", ctx)
    }

    override fun translate(ctx: TranslatorContext): List<Instruction> =
        mutableListOf<Instruction>().apply {
            addAll(value.translate(ctx))
            addAll(name.translate(ctx))

            val offset = ctx.getOffsetOfVar(name.text, st)
            add(storeLocalVar(name.type, offset))
        }

}

