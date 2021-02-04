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

    override fun validate(st: SymbolTable) {
        if (st.containsInCurrentScope(name))
            throw SemanticsException("Cannot find function $name")
        val functionNode = st[name] as FuncNode

        argList.validate(st)

        val args = argList.args
        val params = functionNode.paramList.params

        if (args.size != params.size)
            throw SemanticsException("Number of arguments do not match parameter: $name")

        for (i in args.indices) {
            if (args[i].type != params[i].type)
                throw SemanticsException("${i}th argument of $name has wrong type")
        }

        type = functionNode.retType
    }

}