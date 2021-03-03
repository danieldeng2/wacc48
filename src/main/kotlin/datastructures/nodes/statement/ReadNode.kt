package datastructures.nodes.statement

import datastructures.SymbolTable
import datastructures.nodes.assignment.AccessMode
import datastructures.nodes.assignment.LHSNode
import datastructures.nodes.function.FuncNode
import datastructures.type.CharType
import datastructures.type.IntType
import datastructures.type.StringType
import datastructures.type.Type
import analyser.exceptions.SemanticsException
import generator.instructions.Instruction
import generator.instructions.branch.BLInstr
import generator.translator.CodeGeneratorVisitor
import generator.translator.TranslatorContext
import generator.translator.lib.read.ReadChar
import generator.translator.lib.read.ReadInt
import org.antlr.v4.runtime.ParserRuleContext

data class ReadNode(
    val value: LHSNode,
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

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateRead(this)
    }
}