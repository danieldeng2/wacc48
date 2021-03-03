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
import generator.translator.CodeGeneratorVisitor
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

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateArrayLiteral(this)
    }
}