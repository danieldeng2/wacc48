package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.assignment.LHSNode
import analyser.nodes.type.*
import exceptions.SemanticsException
import generator.TranslatorContext
import generator.armInstructions.STRBInstr
import generator.armInstructions.Instruction
import generator.armInstructions.LDRInstr
import generator.armInstructions.STRInstr
import generator.armInstructions.operands.*
import org.antlr.v4.runtime.ParserRuleContext

data class IdentifierNode(
    val name: String,
    override val ctx: ParserRuleContext?,
) : LHSNode, ExprNode {
    override var type: Type = VoidType
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable

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
            val offset = st.getOffsetOfVar(name)

            if (ctx.isDeclaring) {
                when (type) {
                    is BoolType, CharType -> add(
                        STRBInstr(
                            Register.R0,
                            MemAddr(Register.SP, NumOp(offset))
                        )
                    )
                    is IntType -> add(
                        STRInstr(
                            Register.R0,
                            MemAddr(Register.SP, NumOp(offset))
                        )
                    )
                }
            } else {
                when (type) {
                    is BoolType, CharType -> add(
                        LDRInstr(
                            Register.R0,
                            MemAddr(Register.SP, NumOp(offset))
                        )
                    )
                    is IntType -> add(
                        LDRInstr(
                            Register.R0,
                            MemAddr(Register.SP, NumOp(offset))
                        )
                    )
                }
            }



        }

}