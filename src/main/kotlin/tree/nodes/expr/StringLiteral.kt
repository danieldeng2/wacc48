package tree.nodes.expr

import tree.SymbolTable
import tree.nodes.function.FuncNode
import tree.type.StringType
import tree.type.Type
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext
import shell.CodeEvaluatorVisitor
import shell.MemoryTable


data class StringLiteral(
    val value: String,
    val ctx: ParserRuleContext?
) : Literal {
    override var type: Type = StringType

    override fun literalToString(mt: MemoryTable?): String {
        return value
            .replace("\\n", "\n")
            .replace("\\0", "\u0000")
            .replace("\\b", "\b")
            .replace("\\t", "\t")
            .replace("\\f", "\u000c")
            .replace("\\r", "\r")
            .replace("\\\"", "\"")
            .replace("\\'", "'")
            .replace("\\\\", "\\")
    }

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateStringLiteral(this)
    }

    override fun acceptCodeEvalVisitor(visitor: CodeEvaluatorVisitor): Literal? {
        return visitor.translateStringLiteral(this)
    }
}