package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.type.IntType
import analyser.nodes.type.Type
import exceptions.SemanticsException
import generator.TranslatorContext
import generator.armInstructions.Instruction
import generator.armInstructions.LDRInstr
import generator.armInstructions.PUSHInstr
import generator.armInstructions.operands.ImmOp
import generator.armInstructions.operands.Register
import org.antlr.v4.runtime.ParserRuleContext

data class IntLiteral(
    val value: Int,
    override val ctx: ParserRuleContext?
) : ExprNode {
    override var type: Type = IntType
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
        if (value > IntType.max || value < IntType.min)
            throw SemanticsException("IntLiteral $value is out of range", ctx)
    }

    override fun translate(ctx: TranslatorContext): List<Instruction> =
        listOf(LDRInstr(Register.R0, ImmOp(value)))
}