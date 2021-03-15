package tree.nodes.expr

import analyser.exceptions.SemanticsException
import tree.SymbolTable
import tree.nodes.function.FuncNode
import tree.type.ArrayType
import tree.type.Type
import tree.type.VoidType
import org.antlr.v4.runtime.ParserRuleContext
import tree.ASTVisitor


data class ArrayLiteral(
    var values: List<ExprNode>,
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

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitArrayLiteral(this)
    }
}