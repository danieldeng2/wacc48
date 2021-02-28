package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.assignment.AccessMode
import analyser.nodes.assignment.LHSNode
import analyser.nodes.function.ParamNode
import analyser.nodes.type.Typable
import analyser.nodes.type.Type
import analyser.nodes.type.VoidType
import exceptions.SemanticsException
import generator.translator.TranslatorContext
import generator.instructions.Instruction
import generator.instructions.arithmetic.ADDInstr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.loadLocalVar
import generator.translator.storeLocalVar
import org.antlr.v4.runtime.ParserRuleContext

data class IdentifierNode(
    val name: String,
    override val ctx: ParserRuleContext?,
) : LHSNode, ExprNode {
    override var type: Type = VoidType
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable
    override var mode: AccessMode = AccessMode.READ

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
        if (!st.containsInAnyScope(name))
            throw SemanticsException("Unknown identifier $name", ctx)

        val assignedNode = st[name]
        if (assignedNode !is Typable)
            throw SemanticsException("Unknown type $name", ctx)

        type = assignedNode.type
    }

    override fun translate(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            val paramNode = st[name] as ParamNode
            val offset = if (paramNode.isDeclared) {
                ctx.getOffsetOfLocalVar(name, st)
            } else {
                /* If the variable of this name in this scope is not defined,
                *  then this identifier is referring to a variable of the same
                * name in a higher scope */
                ctx.getOffsetOfLocalVar(name, st.getParent()!!) + st.getLocalVariablesSize()
            }

            add(
                when (mode) {
                    AccessMode.ASSIGN -> storeLocalVar(type, offset)
                    AccessMode.READ -> loadLocalVar(type, offset)
                    else -> ADDInstr(Register.R0, Register.SP, NumOp(offset))
                }
            )
        }

}