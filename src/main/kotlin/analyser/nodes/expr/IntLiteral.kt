package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.function.FuncNode
import analyser.nodes.type.IntType
import analyser.nodes.type.Type
import exceptions.SemanticsException
import generator.instructions.Instruction
import generator.instructions.load.LDRInstr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext

data class IntLiteral(
    val value: Int,
    override val ctx: ParserRuleContext?
) : ExprNode {
    override var type: Type = IntType
    override lateinit var st: SymbolTable
    override lateinit var funTable: MutableMap<String, FuncNode>

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
        this.funTable = funTable
        if (value > IntType.max || value < IntType.min)
            throw SemanticsException("IntLiteral $value is out of range", ctx)
    }

    override fun translate(ctx: TranslatorContext): List<Instruction> =
        listOf(LDRInstr(Register.R0, NumOp(value, isLoad = true)))
}