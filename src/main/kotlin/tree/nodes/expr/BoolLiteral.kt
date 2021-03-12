package tree.nodes.expr

import tree.SymbolTable
import tree.nodes.function.FuncNode
import tree.type.BoolType
import tree.type.Type
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext
import shell.CodeEvaluatorVisitor
import shell.MemoryTable

data class BoolLiteral(
    val value: Boolean,
    val ctx: ParserRuleContext?
) : Literal {
    override var type: Type = BoolType

    override fun literalToString(mt: MemoryTable?): String = value.toString()

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateBoolLiteral(this)
    }

    override fun acceptCodeEvalVisitor(visitor: CodeEvaluatorVisitor) {
        visitor.translateBoolLiteral(this)
    }
}