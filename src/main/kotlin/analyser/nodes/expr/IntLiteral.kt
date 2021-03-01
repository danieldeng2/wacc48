package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.function.FuncNode
import analyser.nodes.type.IntType
import analyser.nodes.type.Type
import analyser.exceptions.SemanticsException
import generator.instructions.Instruction
import generator.instructions.load.LDRInstr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext

data class IntLiteral(
    val value: Int,
    val ctx: ParserRuleContext?
) : ExprNode {
    override var type: Type = IntType


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {

        if (value > IntType.max || value < IntType.min)
            throw SemanticsException("IntLiteral $value is out of range", ctx)
    }

    override fun translate(ctx: TranslatorContext): List<Instruction> =
        listOf(LDRInstr(Register.R0, NumOp(value, isLoad = true)))
}