package wacc48.tree.nodes.function

import wacc48.analyser.exceptions.SemanticsException
import org.antlr.v4.runtime.ParserRuleContext
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.statement.*
import wacc48.tree.type.Type

data class FuncNode(
    val identifier: String,
    val paramList: List<ParamNode>,
    val retType: Type,
    var body: StatNode,
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


    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitFunction(this)
    }

}
