package tree.nodes.statement

import org.antlr.v4.runtime.ParserRuleContext
import tree.ASTVisitor
import tree.SymbolTable
import tree.nodes.function.FuncNode

data class BeginNode(
    var stat: StatNode,
    val ctx: ParserRuleContext?,
) : StatNode {
    lateinit var currST: SymbolTable

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.currST = SymbolTable(st)
        stat.validate(currST, funTable)
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitBegin(this)
    }
}