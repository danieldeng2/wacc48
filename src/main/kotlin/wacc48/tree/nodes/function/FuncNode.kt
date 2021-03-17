package wacc48.tree.nodes.function

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.analyser.exceptions.addSemantic
import wacc48.analyser.exceptions.addSyntax
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.statement.BeginNode
import wacc48.tree.nodes.statement.ExitNode
import wacc48.tree.nodes.statement.IfNode
import wacc48.tree.nodes.statement.ReturnNode
import wacc48.tree.nodes.statement.SeqNode
import wacc48.tree.nodes.statement.StatNode
import wacc48.tree.nodes.statement.WhileNode
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

    fun validatePrototype(ft: MutableMap<String, FuncNode>, issues: MutableList<Issue>) {
        if (identifier in ft)
            issues.addSemantic(
                "Illegal re-declaration of function $identifier",
                ctx
            )
        ft[identifier] = this
    }

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
        this.paramListTable = SymbolTable(st, isParamListST = true)
        this.bodyTable = SymbolTable(paramListTable)

        if (!allPathsTerminated(body)) {
            issues.addSyntax("Function $identifier must end with either a return or exit", ctx)
            return
        }

        paramList.asReversed().forEach {
            it.validate(paramListTable, funTable, issues)
        }
        body.validate(bodyTable, funTable, issues)

        validateReturnType(body, issues)
    }

    private fun validateReturnType(body: StatNode, issues: MutableList<Issue>) {
        when (body) {
            is SeqNode -> body.forEach { validateReturnType(it, issues) }
            is BeginNode -> validateReturnType(body.stat, issues)
            is IfNode -> {
                validateReturnType(body.trueStat, issues)
                validateReturnType(body.falseStat, issues)
            }
            is WhileNode -> validateReturnType(body.body, issues)
            is ReturnNode -> {
                if (body.value.type != retType)
                    issues.addSemantic(
                        "The expected return type of Function $identifier is: $retType," +
                                " actual return type: ${body.value.type}", ctx
                    )
            }
        }
    }


    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitFunction(this)
    }

    fun allPathsTerminated(body: StatNode): Boolean =
        when (body) {
            is SeqNode -> allPathsTerminated(body.last())
            is BeginNode -> allPathsTerminated(body.stat)
            is IfNode -> allPathsTerminated(body.trueStat)
                    && allPathsTerminated(body.falseStat)
            is ReturnNode -> true
            is ExitNode -> true
            else -> false
        }
}
