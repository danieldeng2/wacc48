package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.assignment.RHSNode
import analyser.nodes.type.Type
import analyser.nodes.type.VoidType
import exceptions.SemanticsException

data class FuncCallNode(
    private val name: String,
    private val argList: ArgListNode,
) : RHSNode {
    override var type: Type = VoidType

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        if (!funTable.containsInAnyScope(name))
            throw SemanticsException(".*", null)
        val functionNode = funTable[name] as FuncNode

        argList.validate(st, funTable)

        val args = argList.args
        val params = functionNode.paramList.params

        if (args.size != params.size)
            throw SemanticsException(".*", null)

        for (i in args.indices) {
            if (args[i].type != params[i].type)
                throw SemanticsException(".*", null)
        }

        type = functionNode.retType
    }

}