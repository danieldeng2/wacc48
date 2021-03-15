package wacc48.tree.nodes.function

import wacc48.analyser.exceptions.SemanticsException
import org.antlr.v4.runtime.ParserRuleContext
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.assignment.RHSNode
import wacc48.tree.type.Type
import wacc48.tree.type.VoidType

data class FuncCallNode(
    val name: String,
    val argList: ArgListNode,
    val ctx: ParserRuleContext?,
    val inShellAndFuncNodeCtx: Boolean = false,
) : RHSNode {
    override var type: Type = VoidType
    lateinit var functionNode: FuncNode
    var argListSize: Int = 0

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        var functionIsDeclared = !inShellAndFuncNodeCtx
        if (name !in funTable) {
            if (!inShellAndFuncNodeCtx)
                throw SemanticsException("Cannot find function $name", ctx)
            else
                functionIsDeclared = false
        }
        if (functionIsDeclared) {
            this.functionNode = funTable[name]!!
        }

        argList.validate(st, funTable)

        if (functionIsDeclared) {
            val args = argList.args
            val params = functionNode.paramList

            if (args.size != params.size)
                throw SemanticsException("Number of arguments do not match parameter: $name", ctx)

            for (i in args.indices) {
                if (args[i].type != params[i].type)
                    throw SemanticsException("argument ${i + 1} of $name has wrong type", ctx)
            }

            type = functionNode.retType
        }

        argListSize = argList.args.sumBy {
            it.type.reserveStackSize
        }
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitFuncCall(this)
    }

}


