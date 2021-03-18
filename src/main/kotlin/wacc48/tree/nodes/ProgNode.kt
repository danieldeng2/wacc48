package wacc48.tree.nodes

import org.antlr.v4.runtime.ParserRuleContext
import wacc48.analyser.exceptions.Issue
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.nodes.function.MainNode

data class ProgNode(
    val functions: List<FuncNode>,
    val main: MainNode,
    val ctx: ParserRuleContext?
) : ASTNode {
    override val children: List<ASTNode>
        get() = mutableListOf<ASTNode>().apply {
            addAll(functions)
            add(main)
        }

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
        functions.forEach { it.validatePrototype(funTable, issues) }
        functions.forEach { it.validate(st, funTable, issues) }

        main.validate(st, funTable, issues)
    }

    override fun <T> acceptVisitor(visitor: ASTVisitor<T>): T {
        return visitor.visitProgram(this)
    }
}



