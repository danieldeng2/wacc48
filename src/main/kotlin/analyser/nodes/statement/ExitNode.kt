package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.expr.ExprNode
import analyser.nodes.type.IntType
import exceptions.SemanticsException
import generator.TranslatorContext
import generator.armInstructions.BLInstr
import generator.armInstructions.Instruction
import generator.armInstructions.MOVInstr
import generator.armInstructions.POPInstr
import generator.armInstructions.operands.Register
import org.antlr.v4.runtime.ParserRuleContext

data class ExitNode(
    val value: ExprNode,
    override val ctx: ParserRuleContext?
) : StatNode {
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
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
