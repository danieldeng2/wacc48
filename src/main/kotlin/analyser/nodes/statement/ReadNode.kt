package analyser.nodes.statement

import analyser.SymbolTable
import analyser.nodes.assignment.LHSNode
import analyser.nodes.expr.IdentifierNode
import analyser.nodes.type.*
import exceptions.SemanticsException
import generator.instructions.Instruction
import generator.instructions.arithmetic.ADDInstr
import generator.instructions.branch.BLInstr
import generator.instructions.move.MOVInstr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.TranslatorContext
import generator.translator.lib.print.PrintBool
import generator.translator.lib.print.PrintInt
import generator.translator.lib.print.PrintLn
import generator.translator.lib.print.PrintStr
import generator.translator.lib.read.ReadInt
import org.antlr.v4.runtime.ParserRuleContext
import java.rmi.UnexpectedException

data class ReadNode(
    private val value: LHSNode,
    override val ctx: ParserRuleContext?,
) : StatNode {
    private val expectedExprTypes: List<Type> = listOf(IntType, StringType, CharType)
    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
        value.validate(st, funTable)
        if (value.type !in expectedExprTypes)
            throw SemanticsException("Cannot read from type ${value.type}", ctx)
    }

    override fun translate(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            addAll(value.translate(ctx))

            if (value is IdentifierNode) {
                add(ADDInstr(Register.R0, Register.SP, NumOp(ctx.getOffsetOfLocalVar(value.name, st))))
            } else {
                TODO("Implement getting the memory address of ${value.type}")
            }

            when (value.type) {
                IntType, CharType -> {
                    val readFunc = when (value.type) {
                        IntType -> ReadInt
                        else -> TODO("Implement read for ${value.type}")
                    }
                    ctx.addLibraryFunction(readFunc)
                    add(BLInstr(readFunc.label))
                }
            }
        }
}