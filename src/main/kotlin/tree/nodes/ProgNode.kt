package tree.nodes

import analyser.exceptions.SyntaxException
import org.antlr.v4.runtime.ParserRuleContext
import tree.ASTVisitor
import tree.SymbolTable
import tree.nodes.function.FuncNode
import tree.nodes.function.MainNode
import tree.nodes.statement.*

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
