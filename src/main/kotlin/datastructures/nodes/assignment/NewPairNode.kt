package datastructures.nodes.assignment

import datastructures.SymbolTable
import datastructures.nodes.expr.ExprNode
import datastructures.nodes.function.FuncNode
import datastructures.type.PairType
import datastructures.type.Type
import datastructures.type.VoidType
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext

data class NewPairNode(
    val firstElem: ExprNode,
    val secondElem: ExprNode,
    val ctx: ParserRuleContext?
) : RHSNode {
    override var type: Type = VoidType


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {

        firstElem.validate(st, funTable)
        secondElem.validate(st, funTable)

        type = PairType(firstElem.type, secondElem.type, ctx)
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateNewPair(this)
    }

}