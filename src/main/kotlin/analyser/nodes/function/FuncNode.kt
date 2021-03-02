package analyser.nodes.function

import analyser.SymbolTable
import analyser.exceptions.SemanticsException
import analyser.nodes.ASTNode
import analyser.nodes.expr.ExprNode
import analyser.nodes.statement.*
import analyser.nodes.type.Type
import generator.instructions.Instruction
import generator.instructions.directives.Directive
import generator.instructions.directives.LabelInstr
import generator.instructions.operands.Register
import generator.instructions.stack.PUSHInstr
import generator.translator.TranslatorContext
import generator.translator.helpers.startScope
import org.antlr.v4.runtime.ParserRuleContext

data class FuncNode(
    val identifier: String,
    val paramList: List<ParamNode>,
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

        paramList.asReversed().forEach {
            it.validate(paramListTable, funTable)
        }
        body.validate(bodyTable, funTable)

        validateReturnType(body)
    }

    private fun validateReturnType(body: StatNode) {
        when (body) {
            is SeqNode -> body.forEach { validateReturnType(it) }
            is BeginNode -> validateReturnType(body.stat)
            is IfNode -> {
                validateReturnType(body.trueStat)
                validateReturnType(body.falseStat)
            }
            is WhileNode -> validateReturnType(body.body)
            is ReturnNode -> {
                if (body.value.type != retType)
                    throw SemanticsException(
                        "The expected return type of Function $identifier is: $retType," +
                                " actual return type: ${body.value.type}", ctx
                    )
            }
        }
    }


    override fun translate(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            paramList.forEach {
                paramListTable.declareVariable(it.text)
            }

            ctx.stackPtrOffset = 0

            add(LabelInstr("f_$identifier"))
            add(PUSHInstr(Register.LR))

            startScope(bodyTable)

            addAll(body.translate(ctx))

            add(Directive(".ltorg"))
        }

}
