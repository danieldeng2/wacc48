package analyser.nodes

import analyser.SymbolTable
import analyser.nodes.function.FuncNode
import analyser.nodes.statement.*
import exceptions.SemanticsException
import exceptions.SyntaxException
import generator.TranslatorContext
import generator.armInstructions.*
import generator.armInstructions.operands.ImmOp
import generator.armInstructions.operands.Register
import org.antlr.v4.runtime.ParserRuleContext

data class ProgNode(
    private val body: StatNode,
    private val functions: List<FuncNode>,
    override val ctx: ParserRuleContext?
) : ASTNode {
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
        functions.forEach {
            if (!allPathsTerminated(it.body))
                throw SyntaxException("Function ${it.identifier} must end with either a return or exit")
        }

        functions.forEach { it.validatePrototype(funTable) }
        functions.forEach { it.validate(st, funTable) }

        if (hasGlobalReturn(body))
            throw SemanticsException("Cannot return in global context", ctx)

        body.validate(st, funTable)
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

    override fun translate(ctx: TranslatorContext): List<Instruction> =
        mutableListOf<Instruction>().apply {
            addAll(
                functions.flatMap {
                    it.translate(ctx)
                }
            )
            add(LabelInstr("main"))
            add(PUSHInstr(Register.LR))

            addAll(body.translate(ctx))

            add(LDRInstr(Register.R0, ImmOp(0)))
            add(POPInstr(Register.PC))
            add(Directive(".ltorg"))

        }

}