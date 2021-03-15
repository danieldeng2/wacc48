package wacc48.tree.nodes

import wacc48.analyser.exceptions.SyntaxException
import org.antlr.v4.runtime.ParserRuleContext
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.nodes.function.MainNode
import wacc48.tree.nodes.statement.*

data class ProgNode(
    val functions: List<FuncNode>,
    val main: MainNode,
    val ctx: ParserRuleContext?
) : ASTNode {

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        functions.forEach {
            checkFunctionTerminates(it)
        }

        functions.forEach { it.validatePrototype(funTable) }
        functions.forEach { it.validate(st, funTable) }

        main.validate(st, funTable)
    }


    private fun allPathsTerminated(body: StatNode): Boolean =
        when (body) {
            is SeqNode -> allPathsTerminated(body.last())
            is BeginNode -> allPathsTerminated(body.stat)
            is IfNode -> allPathsTerminated(body.trueStat)
                    && allPathsTerminated(body.falseStat)
            is ReturnNode -> true
            is ExitNode -> true
            else -> false
        }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitProgram(this)
    }

}

fun checkFunctionTerminates(func: FuncNode) {
    if (!allPathsTerminated(func.body))
        throw SyntaxException("Function ${func.identifier} must end with either a return or exit")
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
