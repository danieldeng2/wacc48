package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.assignment.AccessMode
import analyser.nodes.assignment.LHSNode
import analyser.nodes.function.FuncNode
import analyser.nodes.type.Type
import analyser.nodes.type.VoidType
import exceptions.SemanticsException
import generator.instructions.Instruction
import generator.instructions.arithmetic.ADDInstr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.TranslatorContext
import generator.translator.loadLocalVar
import generator.translator.storeLocalVar
import org.antlr.v4.runtime.ParserRuleContext

data class IdentifierNode(
    val name: String,
    override val ctx: ParserRuleContext?,
) : LHSNode, ExprNode {
    override var type: Type = VoidType
    override lateinit var st: SymbolTable

    override var mode: AccessMode = AccessMode.READ

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
        if (!st.containsInAnyScope(name))
            throw SemanticsException("Unknown identifier $name", ctx)

        type = st[name]!!
    }

    override fun translate(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            val offset = ctx.getOffsetOfVar(name, st)

            add(
                when (mode) {
                    AccessMode.ASSIGN -> storeLocalVar(type, offset)
                    AccessMode.READ -> loadLocalVar(type, offset)
                    else -> ADDInstr(Register.R0, Register.SP, NumOp(offset))
                }
            )
        }

}