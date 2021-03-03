package datastructures.nodes.assignment

import datastructures.SymbolTable
import datastructures.nodes.function.FuncNode
import datastructures.nodes.statement.StatNode
import analyser.exceptions.SemanticsException
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext

data class AssignmentNode(
    val name: LHSNode,
    val value: RHSNode,
    val ctx: ParserRuleContext?
) : StatNode {

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        name.mode = AccessMode.ASSIGN
        name.validate(st, funTable)
        value.validate(st, funTable)

        if (name.type != value.type)
            throw SemanticsException(
                "Attempt to assign ${value.type} to ${name.type}",
                ctx
            )
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateAssignment(this)
    }
}