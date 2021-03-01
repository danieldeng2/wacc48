package analyser.nodes

import analyser.SymbolTable
import analyser.nodes.function.FuncNode
import analyser.nodes.function.MainNode
import analyser.nodes.statement.*
import exceptions.SyntaxException
import generator.instructions.Instruction
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext

data class ProgNode(
    val functions: List<FuncNode>,
    val main: MainNode,
    val ctx: ParserRuleContext?
) : ASTNode {

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        functions.forEach {
            if (!allPathsTerminated(it.body))
                throw SyntaxException("Function ${it.identifier} must end with either a return or exit")
        }

        functions.forEach { it.validatePrototype(funTable) }
        functions.forEach { it.validate(st, funTable) }

        main.validate(st, funTable)
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

    override fun translate(ctx: TranslatorContext): List<Instruction> {
        ctx.text.addAll(
            functions.flatMap {
                it.translate(ctx)
            }
        )
        return ctx.assemble()
    }

}