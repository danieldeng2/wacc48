package datastructures.nodes.expr

import analyser.exceptions.SemanticsException
import datastructures.SymbolTable
import datastructures.nodes.function.FuncNode
import datastructures.type.ArrayType
import datastructures.type.Type
import datastructures.type.VoidType
import generator.translator.CodeGeneratorVisitor
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