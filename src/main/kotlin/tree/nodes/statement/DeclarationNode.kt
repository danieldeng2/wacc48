package tree.nodes.statement

import analyser.exceptions.SemanticsException
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext
import tree.SymbolTable
import tree.nodes.assignment.RHSNode
import tree.nodes.function.FuncNode
import tree.nodes.function.ParamNode

data class DeclarationNode(
    val name: ParamNode,
    val value: RHSNode,
    val ctx: ParserRuleContext?
) : StatNode {
    lateinit var st: SymbolTable

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
        name.validate(st, funTable)
        value.validate(st, funTable)

        if (value.type != name.type)
            throw SemanticsException("Type mismatch in declaration $name", ctx)
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateDeclaration(this)
    }

}

