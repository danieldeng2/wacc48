package datastructures.nodes.function

import datastructures.SymbolTable
import datastructures.nodes.ASTNode
import datastructures.nodes.expr.ExprNode
import generator.instructions.Instruction
import generator.translator.CodeGeneratorVisitor
import generator.translator.TranslatorContext
import generator.translator.helpers.storeLocalVar
import org.antlr.v4.runtime.ParserRuleContext

data class ArgListNode(
    val args: List<ExprNode>,
    val ctx: ParserRuleContext?
) : ASTNode {


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        args.forEach {
            it.validate(st, funTable)
        }
    }

    override fun translate(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            val stackPtrTemp = ctx.stackPtrOffset

            args.asReversed().forEach {
                addAll(it.translate(ctx))
                add(
                    storeLocalVar(
                        varType = it.type,
                        stackOffset = -it.type.reserveStackSize,
                        isArgLoad = true
                    )
                )
                ctx.stackPtrOffset += it.type.reserveStackSize
            }

            ctx.stackPtrOffset = stackPtrTemp
        }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateArgList(this)
    }
}