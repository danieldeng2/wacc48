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
import shell.*

data class ArrayElement(
    val name: String,
    val arrIndices: List<ExprNode>,
    val ctx: ParserRuleContext?
) : ExprNode, LHSNode {
    lateinit var st: SymbolTable
    override var mode: AccessMode = AccessMode.READ
    override lateinit var type: Type

    override fun reduceToLiteral(mt: MemoryTable?): Literal {
        return if (arrIndices.size > 1) { //Deep array access
            var array: DeepArrayLiteral = mt?.getLiteral(name) as DeepArrayLiteral

            for (i in arrIndices.subList(0, arrIndices.size - 2)) {
                val index = (i.reduceToLiteral(mt) as IntLiteral).value
                checkIndexBounds(index, array.values.size)
                array = mt.getLiteral(array.values[(i.reduceToLiteral(mt) as IntLiteral).value]) as DeepArrayLiteral
            }

            val index = (arrIndices[arrIndices.size - 2].reduceToLiteral(mt) as IntLiteral).value
            checkIndexBounds(index, array.values.size)
            var lastArray = mt.getLiteral(array.values[index]) as ArrayLiteral
            lastArray.values[(arrIndices.last().reduceToLiteral(mt) as IntLiteral).value].reduceToLiteral(mt)
        } else {
            val array: ArrayLiteral = mt?.getLiteral(name) as ArrayLiteral
            val index = (arrIndices.last().reduceToLiteral(mt) as IntLiteral).value
            checkIndexBounds(index, array.values.size)
            array.values[index].reduceToLiteral(mt)
        }
    }

    fun getArrayRef(mt: MemoryTable): String {
        var array: DeepArrayLiteral = mt.getLiteral(name) as DeepArrayLiteral
        for (i in arrIndices.subList(0, arrIndices.size - 2)) {
            val index = (i.reduceToLiteral(mt) as IntLiteral).value
            checkIndexBounds(index, array.values.size)
            array = mt.getLiteral(array.values[index]) as DeepArrayLiteral
        }
        val index = (arrIndices[arrIndices.size - 2] as IntLiteral).value
        checkIndexBounds(index, array.values.size)
        return array.values[(arrIndices[arrIndices.size - 2] as IntLiteral).value]
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