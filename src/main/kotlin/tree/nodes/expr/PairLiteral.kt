package tree.nodes.expr

import tree.SymbolTable
import tree.nodes.function.FuncNode
import tree.type.EmptyPair
import tree.type.Type
import generator.translator.CodeGeneratorVisitor
import shell.MemoryTable

object PairLiteral : Literal {
    override var type: Type = EmptyPair

    override fun literalToString(mt: MemoryTable?): String = "null"

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
