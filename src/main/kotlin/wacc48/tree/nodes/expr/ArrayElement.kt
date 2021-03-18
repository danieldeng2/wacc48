package wacc48.tree.nodes.expr

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.analyser.exceptions.addSemantic
import wacc48.shell.MemoryTable
import wacc48.shell.checkIndexBounds
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.assignment.AccessMode
import wacc48.tree.nodes.assignment.LHSNode
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.type.ArrayType
import wacc48.tree.type.Type
import wacc48.tree.type.VoidType

data class ArrayElement(
    val name: String,
    val arrIndices: List<ExprNode>,
    val ctx: ParserRuleContext?
) : ExprNode, LHSNode {
    lateinit var st: SymbolTable
    override var mode: AccessMode = AccessMode.READ

    override val children: List<ASTNode>
        get() = arrIndices

    override var type: Type = VoidType

    override fun reduceToLiteral(mt: MemoryTable?): Literal {
        return if (arrIndices.size > 1) { //Deep array access
            var array: DeepArrayLiteral =
                mt?.getLiteral(name) as DeepArrayLiteral

            for (i in arrIndices.subList(0, arrIndices.size - 2)) {
                val index = (i.reduceToLiteral(mt) as IntLiteral).value
                checkIndexBounds(index, array.values.size)
                array =
                    mt.getLiteral(array.values[(i.reduceToLiteral(mt) as IntLiteral).value]) as DeepArrayLiteral
            }

            val index =
                (arrIndices[arrIndices.size - 2].reduceToLiteral(mt) as IntLiteral).value
            checkIndexBounds(index, array.values.size)
            val lastArray = mt.getLiteral(array.values[index]) as ArrayLiteral
            lastArray.values[(arrIndices.last()
                .reduceToLiteral(mt) as IntLiteral).value].reduceToLiteral(mt)
        } else {
            val array: ArrayLiteral = mt?.getLiteral(name) as ArrayLiteral
            val index =
                (arrIndices.last().reduceToLiteral(mt) as IntLiteral).value
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
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
        this.st = st
        if (!st.containsInAnyScope(name)) {
            issues.addSemantic("Cannot find array $name", ctx)
            return
        }
        arrIndices.forEach { it.validate(st, funTable, issues) }
        var identityType = st[name]!!
        if (identityType !is ArrayType) {
            issues.addSemantic("$name is not an array", null)
            return
        }

        for (i in arrIndices.indices) {
            try {
                identityType = (identityType as ArrayType).elementType
            } catch (e: ClassCastException) {
                issues.addSemantic(
                    "Invalid de-referencing of array $name",
                    ctx
                )
                return
            }
        }
        type = identityType
    }

    override fun <T> acceptVisitor(visitor: ASTVisitor<T>): T {
        return visitor.visitArrayElement(this)
    }

}