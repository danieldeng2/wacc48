package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.assignment.RHSNode
import analyser.nodes.type.Type
import analyser.nodes.type.VoidType
import exceptions.SemanticsException
import org.antlr.v4.runtime.ParserRuleContext

data class FuncCallNode(
    val name: String,
    val argList: ArgListNode,
    override val ctx: ParserRuleContext?,
) : RHSNode {
    override var type: Type = VoidType
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
        if (!funTable.containsInAnyScope(name))
            throw SemanticsException("Cannot find function $name", ctx)
        val functionNode = funTable[name] as FuncNode

        argList.validate(st, funTable)

        val args = argList.args
        val params = functionNode.paramList.params

        if (args.size != params.size)
            throw SemanticsException("Number of arguments do not match parameter: $name", ctx)

        for (i in args.indices) {
            if (args[i].type != params[i].type)
                throw SemanticsException("argument ${i + 1} of $name has wrong type", ctx)
        }

        type = functionNode.retType
    }

}