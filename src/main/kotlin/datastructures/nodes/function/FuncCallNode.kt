package datastructures.nodes.function

import datastructures.SymbolTable
import analyser.exceptions.SemanticsException
import datastructures.nodes.assignment.RHSNode
import datastructures.nodes.type.Type
import datastructures.nodes.type.VoidType
import generator.instructions.Instruction
import generator.instructions.arithmetic.ADDInstr
import generator.instructions.branch.BLInstr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext

data class FuncCallNode(
    val name: String,
    val argList: ArgListNode,
    val ctx: ParserRuleContext?,
) : RHSNode {
    override var type: Type = VoidType
    lateinit var functionNode: FuncNode
    var argListSize: Int = 0

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {

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

    override fun translate(ctx: TranslatorContext) = mutableListOf<Instruction>().apply {

        addAll(argList.translate(ctx))
        add(BLInstr("f_$name"))

        if (argListSize != 0)
            add(ADDInstr(Register.SP, Register.SP, NumOp(argListSize)))

    }

}


