package analyser.nodes

import analyser.SymbolTable
import analyser.nodes.function.FuncNode
import analyser.nodes.statement.*
import exceptions.SemanticsException
import exceptions.SyntaxException
import generator.translator.TranslatorContext
import generator.armInstructions.*
import org.antlr.v4.runtime.ParserRuleContext

data class ProgNode(
    private val functions: List<FuncNode>,
    override val ctx: ParserRuleContext?
) : ASTNode {
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable

        val (main, funcs) = functions.partition { it.identifier == "main" }

        funcs.forEach {
            if (!allPathsTerminated(it.body))
                throw SyntaxException("Function ${it.identifier} must end with either a return or exit")
        }

        funcs.forEach { it.validatePrototype(funTable) }
        functions.forEach { it.validate(st, funTable) }

        if (hasGlobalReturn(main[0].body)) {
            throw SemanticsException("Cannot return in global context", ctx)
        }

    }

    private fun allPathsTerminated(body: StatNode): Boolean =
        when (body) {
            is SeqNode -> allPathsTerminated(body.last())
            is BeginNode -> allPathsTerminated(body.stat)
            is IfNode -> allPathsTerminated(body.trueStat)
                    && allPathsTerminated(body.falseStat)
            is ReturnNode -> true
            is ExitNode -> true
            else -> false
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

    override fun translate(ctx: TranslatorContext): List<Instruction> {
        ctx.text.addAll(
            functions.flatMap {
                it.translate(ctx)
            }
        )
        return ctx.assemble()
    }

}