package datastructures.nodes.function

import datastructures.SymbolTable
import analyser.exceptions.SemanticsException
import datastructures.nodes.ASTNode
import datastructures.type.Typable
import datastructures.type.Type
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext

data class ParamNode(
    override var type: Type,
    val text: String,
    val ctx: ParserRuleContext?
) : ASTNode, Typable {
    lateinit var st: SymbolTable

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
        if (st.containsInCurrentScope(text))
            throw SemanticsException(
                "Illegal re-declaration of parameter $text",
                ctx
            )
        st[text] = type
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateParam(this)
    }

}