package analyser.nodes

import analyser.SymbolTable
import analyser.nodes.function.FuncNode
import analyser.nodes.statement.*
import exceptions.SemanticsException
import exceptions.SyntaxException
import org.antlr.v4.runtime.ParserRuleContext

data class ProgNode(
    private val body: StatNode,
    private val functions: List<FuncNode>,
    override val ctx: ParserRuleContext?
) : ASTNode {

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        functions.forEach {
            if (!allPathsTerminated(it.body))
                throw SyntaxException("Function ${it.identifier} must end with either a return or exit")
        }

        functions.forEach { it.validatePrototype(funTable) }
        functions.forEach { it.validate(st, funTable) }

        if (hasGlobalReturn(body))
            throw SemanticsException("Cannot return in global context", ctx)

        body.validate(st, funTable)
    }

    private fun allPathsTerminated(body: StatNode): Boolean {
        var lastStat = body
        while (lastStat is SeqNode) {
            lastStat = lastStat.secondStat
        }
        return when (lastStat) {
            is BeginNode -> allPathsTerminated(lastStat.stat)
            is IfNode -> allPathsTerminated(lastStat.trueStat)
                    && allPathsTerminated(lastStat.falseStat)
            is ReturnNode -> true
            is ExitNode -> true
            else -> false
        }
    }

    private fun hasGlobalReturn(body: StatNode): Boolean =
        when (body) {
            is IfNode -> hasGlobalReturn(body.trueStat) || hasGlobalReturn(body.falseStat)
            is SeqNode -> hasGlobalReturn(body.firstStat) || hasGlobalReturn(body.secondStat)
            is WhileNode -> hasGlobalReturn(body.body)
            is BeginNode -> hasGlobalReturn(body.stat)
            is ReturnNode -> true
            else -> false
        }
}