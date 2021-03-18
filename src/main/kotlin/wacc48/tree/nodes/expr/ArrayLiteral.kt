package wacc48.tree.nodes.expr

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.analyser.exceptions.addSemantic
import wacc48.shell.MemoryTable
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.ASTNode
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
    override var type: Type = ArrayType(elemType)

    override fun literalToString(mt: MemoryTable?): String =
        if (elemType is CharType)
            values.joinToString("") { (it as CharLiteral).literalToString() }
        else
            "[" + values.joinToString(", ") { it.reduceToLiteral(mt).literalToString() } + "]"

    override fun reduceToLiteral(mt: MemoryTable?): Literal =
        this

    override val children: List<ASTNode>
        get() = values

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {

        if (values.isNotEmpty()) {
            values[0].validate(st, funTable, issues)
            elemType = values[0].type
            type = ArrayType(elemType)
        }

        values.forEach {
            it.validate(st, funTable, issues)
            if (it.type != elemType)
                issues.addSemantic(
                    "Array elements has type ${it.type} and $elemType",
                    ctx
                )
        }
    }

    override fun <T> acceptVisitor(visitor: ASTVisitor<T>): T {
        return visitor.visitArrayLiteral(this)
    }

}