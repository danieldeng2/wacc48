package tree.nodes.function

import analyser.exceptions.SemanticsException
import tree.SymbolTable
import tree.nodes.assignment.RHSNode
import tree.type.Type
import tree.type.VoidType
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext

data class FuncCallNode(
    val name: String,
    val argList: ArgListNode,
    val ctx: ParserRuleContext?,
    val inFuncNodeCtx: Boolean = false,
) : RHSNode {
    override var type: Type = VoidType
    lateinit var functionNode: FuncNode
    var argListSize: Int = 0

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        if (!inFuncNodeCtx) {
            if (name !in funTable)
                throw SemanticsException("Cannot find function $name", ctx)
            this.functionNode = funTable[name]!!

            argList.validate(st, funTable)

            val args = argList.args
            val params = functionNode.paramList

            if (args.size != params.size)
                throw SemanticsException("Number of arguments do not match parameter: $name", ctx)

            for (i in args.indices) {
                if (args[i].type != params[i].type)
                    throw SemanticsException("argument ${i + 1} of $name has wrong type", ctx)
            }

            type = functionNode.retType
            argListSize = argList.args.sumBy {
                it.type.reserveStackSize
            }
        }
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateFuncCall(this)
    }
}


