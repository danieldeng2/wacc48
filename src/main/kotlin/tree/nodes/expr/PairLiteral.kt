package tree.nodes.expr

import tree.SymbolTable
import tree.nodes.function.FuncNode
import tree.type.EmptyPair
import tree.type.Type
import generator.translator.CodeGeneratorVisitor
import shell.CodeEvaluatorVisitor
import shell.MemoryTable

object PairLiteral : Literal {
    override var type: Type = EmptyPair

    override fun literalToString(mt: MemoryTable?): String = "(nil)"

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

    override fun acceptCodeEvalVisitor(visitor: CodeEvaluatorVisitor): Literal? {
        return visitor.translatePairLiteral(this)
    }
}

class PairMemoryLiteral(var firstLiteral: Literal, var secondLiteral: Literal, override var type: Type) : Literal {
    //Spec says to print the hex address but kotlin doesn't allow you to
    override fun literalToString(mt: MemoryTable?): String =
        "<$firstLiteral,$secondLiteral>"

    override fun validate(st: SymbolTable, funTable: MutableMap<String, FuncNode>) {
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
    }

    override fun acceptCodeEvalVisitor(visitor: CodeEvaluatorVisitor): Literal? {
        return visitor.translatePairLiteral(this)
    }
}