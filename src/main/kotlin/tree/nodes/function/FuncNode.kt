package tree.nodes.function

import analyser.exceptions.SemanticsException
import tree.SymbolTable
import tree.nodes.ASTNode
import tree.nodes.statement.*
import tree.type.Type
import generator.translator.CodeGeneratorVisitor
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


    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateFunction(this)
    }

}
