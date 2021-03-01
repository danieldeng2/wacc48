package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.assignment.RHSNode
import analyser.nodes.type.Type
import analyser.nodes.type.VoidType
import exceptions.SemanticsException
import generator.instructions.Instruction
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext

data class FuncCallNode(
    val name: String,
    val argList: ArgListNode,
    override val ctx: ParserRuleContext?,
) : RHSNode {
    override var type: Type = VoidType
    override lateinit var st: SymbolTable
    lateinit var functionNode: FuncNode

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
        if (name !in funTable)
            throw SemanticsException("Cannot find function $name", ctx)
        this.functionNode = funTable[name]!!

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

    override fun translate(ctx: TranslatorContext): List<Instruction> {
        TODO("Not yet implemented")
    }
}