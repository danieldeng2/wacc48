package datastructures.nodes.statement

import analyser.exceptions.SemanticsException
import datastructures.SymbolTable
import datastructures.nodes.assignment.RHSNode
import datastructures.nodes.function.FuncNode
import datastructures.nodes.function.ParamNode
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext

data class DeclarationNode(
    val name: ParamNode,
    val value: RHSNode,
    val ctx: ParserRuleContext?
) : StatNode {

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        name.validate(st, funTable)
        value.validate(st, funTable)

        if (value.type != name.type)
            throw SemanticsException("Type mismatch in declaration $name", ctx)
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateDeclaration(this)
    }

}

