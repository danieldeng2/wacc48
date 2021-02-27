package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.assignment.AccessMode
import analyser.nodes.assignment.LHSNode
import analyser.nodes.type.Typable
import analyser.nodes.type.Type
import analyser.nodes.type.VoidType
import exceptions.SemanticsException
import generator.translator.TranslatorContext
import generator.instructions.Instruction
import generator.translator.addressVar
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
            val offset = ctx.getOffsetOfLocalVar(name, st)
            add(
                when (mode) {
                    AccessMode.ASSIGN -> storeLocalVar(type, offset)
                    AccessMode.READ -> loadLocalVar(type, offset)
                    else -> addressVar(offset)
                }
            )
        }

}