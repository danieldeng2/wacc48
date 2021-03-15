package tree.nodes.expr

import analyser.exceptions.SemanticsException
import org.antlr.v4.runtime.ParserRuleContext
import shell.MemoryTable
import tree.ASTVisitor
import tree.SymbolTable
import tree.nodes.function.FuncNode
import tree.type.ArrayType
import tree.type.CharType
import tree.type.Type
import tree.type.VoidType


data class ArrayLiteral(
    var values: List<ExprNode>,
    val ctx: ParserRuleContext?,
    val nameInMemTable: String? = null
) : Literal {
    var elemType: Type = VoidType
    override var type: Type = ArrayType(elemType, ctx)

    override fun literalToString(mt: MemoryTable?): String =
        if (elemType is CharType)
            values.map { (it as CharLiteral).literalToString() }.joinToString("")
        else
            "[" + values.joinToString(", ") { it.reduceToLiteral(mt).literalToString() } + "]"

    override fun reduceToLiteral(mt: MemoryTable?): Literal =
        this

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