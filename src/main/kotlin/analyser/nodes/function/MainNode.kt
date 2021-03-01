package analyser.nodes.function

import analyser.SymbolTable
import analyser.exceptions.SemanticsException
import analyser.nodes.ASTNode
import analyser.nodes.statement.*
import generator.instructions.Instruction
import generator.instructions.move.MOVInstr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.TranslatorContext
import generator.translator.helpers.declareFunction
import generator.translator.helpers.newScope
import org.antlr.v4.runtime.ParserRuleContext

class MainNode(
    val body: StatNode,
    val ctx: ParserRuleContext?
) : ASTNode {
    private lateinit var st: SymbolTable

    override fun validate(st: SymbolTable, funTable: MutableMap<String, FuncNode>) {
        this.st = st
        body.validate(st, funTable)

        if (hasGlobalReturn(body))
            throw SemanticsException("Cannot return in global context", ctx)
    }

    override fun translate(ctx: TranslatorContext): List<Instruction> =
        mutableListOf<Instruction>().apply {
            ctx.stackPtrOffset = 0

            declareFunction("main") {
                newScope(st) {
                    addAll(body.translate(ctx))
                }
                add(MOVInstr(Register.R0, NumOp(0)))
            }

        }

    private fun hasGlobalReturn(body: StatNode): Boolean =
        when (body) {
            is IfNode -> hasGlobalReturn(body.trueStat) || hasGlobalReturn(body.falseStat)
            is SeqNode -> body.any { hasGlobalReturn(it) }
            is WhileNode -> hasGlobalReturn(body.body)
            is BeginNode -> hasGlobalReturn(body.stat)
            is ReturnNode -> true
            else -> false
        }

}