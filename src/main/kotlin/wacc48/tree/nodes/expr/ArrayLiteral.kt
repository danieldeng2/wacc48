package wacc48.tree.nodes.expr

import wacc48.analyser.exceptions.SemanticsException
import org.antlr.v4.runtime.ParserRuleContext
import wacc48.shell.MemoryTable
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.type.ArrayType
import wacc48.tree.type.CharType
import wacc48.tree.type.Type
import wacc48.tree.type.VoidType


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