package datastructures.nodes.expr

import datastructures.SymbolTable
import datastructures.nodes.function.FuncNode
import datastructures.type.EmptyPair
import datastructures.type.Type
import generator.translator.CodeGeneratorVisitor

object PairLiteral : ExprNode {
    override var type: Type = EmptyPair

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
    }

    override fun toString(): String {
        return "Null"
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translatePairLiteral(this)
    }
}
