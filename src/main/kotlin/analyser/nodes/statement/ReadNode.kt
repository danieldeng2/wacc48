package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.assignment.AccessMode
import analyser.nodes.assignment.LHSNode
import analyser.nodes.function.FuncNode
import analyser.nodes.type.CharType
import analyser.nodes.type.IntType
import analyser.nodes.type.StringType
import analyser.nodes.type.Type
import exceptions.SemanticsException
import generator.instructions.Instruction
import generator.instructions.branch.BLInstr
import generator.translator.TranslatorContext
import generator.translator.lib.read.ReadChar
import generator.translator.lib.read.ReadInt
import org.antlr.v4.runtime.ParserRuleContext

data class ReadNode(
    private val value: LHSNode,
    val ctx: ParserRuleContext?,
) : StatNode {
    private val expectedExprTypes: List<Type> = listOf(IntType, StringType, CharType)


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {

        value.mode = AccessMode.ADDRESS
        value.validate(st, funTable)
        if (value.type !in expectedExprTypes)
            throw SemanticsException("Cannot read from type ${value.type}", ctx)
    }

    override fun translate(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            addAll(value.translate(ctx))

            val readFunc = when (value.type) {
                IntType -> ReadInt
                CharType -> ReadChar
                else -> throw NotImplementedError(
                    "Implement read for ${value.type}"
                )
            }
            ctx.addLibraryFunction(readFunc)
            add(BLInstr(readFunc.label))
        }
}