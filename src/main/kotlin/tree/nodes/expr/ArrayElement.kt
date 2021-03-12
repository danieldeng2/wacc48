package tree.nodes.expr

import analyser.exceptions.SemanticsException
import tree.SymbolTable
import tree.nodes.assignment.AccessMode
import tree.nodes.assignment.LHSNode
import tree.nodes.function.FuncNode
import tree.type.ArrayType
import tree.type.Type
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext
import shell.CodeEvaluatorVisitor
import shell.MemoryTable

data class ArrayElement(
    val name: String,
    val arrIndices: List<ExprNode>,
    val ctx: ParserRuleContext?
) : ExprNode, LHSNode {
    lateinit var st: SymbolTable
    override var mode: AccessMode = AccessMode.READ
    override lateinit var type: Type

    override fun reduceToLiteral(mt: MemoryTable?): Literal {
        var array: ArrayLiteral = mt?.getLiteral(name) as ArrayLiteral
        for (i in arrIndices.subList(0, arrIndices.size - 1)) {
            array = array.values[(i.reduceToLiteral(mt) as IntLiteral).value].reduceToLiteral() as ArrayLiteral
        }
        return array.values[(arrIndices.last().reduceToLiteral(mt) as IntLiteral).value].reduceToLiteral(mt)
    }

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
        if (!st.containsInAnyScope(name))
            throw SemanticsException("Cannot find array $name", ctx)
        arrIndices.forEach { it.validate(st, funTable) }
        var identityType = st[name]!!
        if (identityType !is ArrayType)
            throw SemanticsException("$name is not an array", null)

        for (i in arrIndices.indices) {
            try {
                identityType = (identityType as ArrayType).elementType
            } catch (e: ClassCastException) {
                throw SemanticsException(
                    "Invalid de-referencing of array $name",
                    ctx
                )
            }
        }
        type = identityType
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateArrayElement(this)
    }

    override fun acceptCodeEvalVisitor(visitor: CodeEvaluatorVisitor): Literal? {
        return visitor.translateArrayElement(this)
    }
}