package analyser.nodes

import analyser.SymbolTable
import analyser.nodes.function.FuncNode
import analyser.nodes.statement.*
import exceptions.SemanticsException

data class ProgNode(
    private val body: StatNode,
    private val functions: List<FuncNode>
) : ASTNode {

    override fun validate(st: SymbolTable) {
        functions.forEach {
            it.validatePrototype(st)
        }

        functions.forEach {
            it.validate(st)
        }

        if (hasGlobalReturn(body))
            throw SemanticsException("Cannot return in global context")

        body.validate(st)
    }

    private fun hasGlobalReturn(body: StatNode): Boolean =
        when (body) {
            is ReturnNode -> true
            is IfNode -> hasGlobalReturn(body.trueStat) || hasGlobalReturn(body.falseStat)
            is WhileNode -> hasGlobalReturn(body.body)
            is BeginNode -> hasGlobalReturn(body.stat)
            is SeqNode -> hasGlobalReturn(body.firstStat) || hasGlobalReturn(body.secondStat)
            else -> false
        }

}