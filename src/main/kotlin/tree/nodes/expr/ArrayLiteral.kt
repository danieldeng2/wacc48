package tree.nodes.expr

import analyser.exceptions.SemanticsException
import tree.SymbolTable
import tree.nodes.function.FuncNode
import tree.type.ArrayType
import tree.type.Type
import tree.type.VoidType
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext
import shell.CodeEvaluatorVisitor
import shell.MemoryTable
import tree.type.CharType


data class ArrayLiteral(
    val values: List<ExprNode>,
    val ctx: ParserRuleContext?
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

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateArrayLiteral(this)
    }

    override fun acceptCodeEvalVisitor(visitor: CodeEvaluatorVisitor): Literal? {
        return visitor.translateArrayLiteral(this)
    }
}