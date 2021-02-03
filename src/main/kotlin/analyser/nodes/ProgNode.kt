package analyser.nodes

import analyser.SymbolTable
import analyser.nodes.function.FuncNode
import analyser.nodes.statement.StatNode

class ProgNode(
    private val body: StatNode,
    private val functions: List<FuncNode>
) : ASTNode {

    override fun validate(st: SymbolTable) {
        TODO("Not yet implemented")
    }

}