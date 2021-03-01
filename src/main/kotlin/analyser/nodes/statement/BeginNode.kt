package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.function.FuncNode
import generator.instructions.Instruction
import generator.instructions.arithmetic.ADDInstr
import generator.instructions.arithmetic.SUBInstr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.TranslatorContext
import org.antlr.v4.runtime.ParserRuleContext

data class BeginNode(
    val stat: StatNode,
    override val ctx: ParserRuleContext?,
) : StatNode {
    override lateinit var st: SymbolTable

    private lateinit var currST: SymbolTable

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
        this.currST = SymbolTable(st)
        stat.validate(currST, funTable)
    }

    override fun translate(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            val variableSize = currST.totalVarSize

            if (variableSize != 0) {
                add(SUBInstr(Register.SP, Register.SP, NumOp(variableSize)))
            }

            addAll(stat.translate(ctx))

            if (variableSize != 0) {
                add(ADDInstr(Register.SP, Register.SP, NumOp(variableSize)))
            }
        }
}