package analyser.nodes.function

import analyser.SymbolTable
import analyser.exceptions.SemanticsException
import analyser.nodes.ASTNode
import analyser.nodes.statement.*
import analyser.nodes.type.Type
import generator.instructions.Instruction
import generator.instructions.operands.Register
import generator.instructions.stack.POPInstr
import generator.translator.TranslatorContext
import generator.translator.helpers.declareFunction
import generator.translator.helpers.newScope
import org.antlr.v4.runtime.ParserRuleContext

data class FuncNode(
    val identifier: String,
    val paramList: ParamListNode,
    val retType: Type,
    val body: StatNode,
    val ctx: ParserRuleContext?
) : ASTNode {
    lateinit var paramListTable: SymbolTable
    lateinit var bodyTable: SymbolTable

    fun validatePrototype(ft: MutableMap<String, FuncNode>) {
        if (identifier in ft)
            throw SemanticsException(
                "Illegal re-declaration of function $identifier",
                ctx
            )
        ft[identifier] = this
    }

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.paramListTable = SymbolTable(st, isParamListST = true)
        this.bodyTable = SymbolTable(paramListTable)

        retType.validate(st, funTable)
        paramList.validate(paramListTable, funTable)
        body.validate(bodyTable, funTable)

        validateReturnType(body)
    }

    private fun validateReturnType(body: StatNode) {
        when (body) {
            is SeqNode -> validateReturnType(body.last())
            is BeginNode -> validateReturnType(body.stat)
            is IfNode -> {
                validateReturnType(body.trueStat)
                validateReturnType(body.falseStat)
            }
            is ReturnNode ->
                if (body.value.type != retType)
                    throw SemanticsException(
                        "The expected return type of Function $identifier is: $retType," +
                                " actual return type: ${body.value.type}", ctx
                    )
        }
    }

    override fun translate(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            paramList.translate(ctx)

            ctx.stackPtrOffset = 0

            declareFunction("f_$identifier") {
                newScope(bodyTable) {
                    addAll(body.translate(ctx))
                }
            }

            add(POPInstr(Register.PC))
        }

}
