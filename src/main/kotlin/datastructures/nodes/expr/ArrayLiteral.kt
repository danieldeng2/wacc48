package datastructures.nodes.expr

import datastructures.SymbolTable
import datastructures.nodes.function.FuncNode
import datastructures.type.ArrayType
import datastructures.type.Type
import datastructures.type.VoidType
import analyser.exceptions.SemanticsException
import generator.instructions.Instruction
import generator.instructions.branch.BLInstr
import generator.instructions.move.MOVInstr
import generator.instructions.operands.MemAddr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.instructions.store.STRInstr
import generator.translator.TranslatorContext
import generator.translator.helpers.*
import org.antlr.v4.runtime.ParserRuleContext


data class ArrayLiteral(
    val values: List<ExprNode>,
    val ctx: ParserRuleContext?
) : ExprNode {
    var elemType: Type = VoidType
    override var type: Type = ArrayType(elemType, ctx)


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {

        if (values.isNotEmpty()) {
            values[0].validate(st, funTable)
            elemType = values[0].type
            type = ArrayType(elemType, ctx)
        }

        values.forEach {
            it.validate(st, funTable)
            if (it.type != elemType)
                throw SemanticsException(
                    "Array elements are of different type: $this",
                    ctx
                )
        }
    }

    override fun translate(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            add(
                MOVInstr(
                    Register.R0,
                    NumOp(values.size * elemType.reserveStackSize + 4)
                )
            )
            add(BLInstr("malloc"))
            add(MOVInstr(Register.R3, Register.R0))

            values.forEachIndexed { index, arrayElem ->
                addAll(arrayElem.translate(ctx))
                add(
                    storeLocalVar(
                        elemType,
                        index * elemType.reserveStackSize + 4,
                        rd = Register.R3,
                        rn = Register.R0
                    )
                )
            }

            add(MOVInstr(Register.R0, NumOp(values.size)))
            add(STRInstr(Register.R0, MemAddr(Register.R3)))
            add(MOVInstr(Register.R0, Register.R3))
        }
}