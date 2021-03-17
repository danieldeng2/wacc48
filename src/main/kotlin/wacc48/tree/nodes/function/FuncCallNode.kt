package wacc48.tree.nodes.function

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.analyser.exceptions.addSemantic
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
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
        var functionIsDeclared = !inShellAndFuncNodeCtx
        if (name !in funTable) {
            if (!inShellAndFuncNodeCtx) {
                issues.addSemantic("Cannot find function $name", ctx)
                return
            } else
                functionIsDeclared = false
        }
        if (functionIsDeclared) {
            this.functionNode = funTable[name]!!
        }

        argList.validate(st, funTable, issues)

        if (functionIsDeclared) {
            val args = argList.args
            val params = functionNode.paramList

            if (args.size != params.size) {
                issues.addSemantic("Number of arguments do not match parameter: $name", ctx)
                return
            }

            for (i in args.indices) {
                if (args[i].type != params[i].type) {
                    issues.addSemantic("argument ${i + 1} of $name has wrong type", ctx)
                    return
                }
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


