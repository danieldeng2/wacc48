package datastructures.nodes.expr

import datastructures.SymbolTable
import analyser.exceptions.SemanticsException
import datastructures.nodes.assignment.AccessMode
import datastructures.nodes.assignment.LHSNode
import datastructures.nodes.function.FuncNode
import datastructures.type.Type
import datastructures.type.VoidType
import generator.instructions.Instruction
import generator.instructions.arithmetic.ADDInstr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.TranslatorContext
import generator.translator.helpers.loadLocalVar
import generator.translator.helpers.storeLocalVar
import org.antlr.v4.runtime.ParserRuleContext

data class IdentifierNode(
    val name: String,
    val ctx: ParserRuleContext?,
) : LHSNode, ExprNode {
    override var type: Type = VoidType
    private lateinit var st: SymbolTable
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