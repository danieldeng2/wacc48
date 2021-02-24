package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.ASTNode
import analyser.nodes.type.*
import exceptions.SemanticsException
import generator.TranslatorContext
import generator.armInstructions.*
import generator.armInstructions.operands.MemAddr
import generator.armInstructions.operands.NumOp
import generator.armInstructions.operands.Register
import org.antlr.v4.runtime.ParserRuleContext

data class ParamNode(
    override var type: Type,
    val text: String,
    override val ctx: ParserRuleContext?
) : ASTNode, Typable {
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
        if (st.containsInCurrentScope(text))
            throw SemanticsException(
                "Illegal re-declaration of parameter $text",
                ctx
            )
        st.add(text, this)
    }

    override fun translate(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            val offset = st.getOffsetOfVar(text)

            when (type) {
                is BoolType, CharType -> add(
                    STRBInstr(
                        Register.R0,
                        MemAddr(Register.SP, NumOp(offset))
                    )
                )
                is IntType, StringType -> add(
                    STRInstr(Register.R0, MemAddr(Register.SP, NumOp(offset)))
                )
            }
        }
}