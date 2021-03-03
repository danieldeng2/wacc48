package datastructures.nodes.statement

import datastructures.SymbolTable
import analyser.exceptions.SemanticsException
import datastructures.nodes.expr.ExprNode
import datastructures.nodes.function.FuncNode
import datastructures.type.BoolType
import generator.instructions.Instruction
import generator.instructions.branch.BEQInstr
import generator.instructions.branch.BInstr
import generator.instructions.compare.CMPInstr
import generator.instructions.directives.LabelInstr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.translator.CodeGeneratorVisitor
import generator.translator.TranslatorContext
import generator.translator.helpers.newScope
import org.antlr.v4.runtime.ParserRuleContext

data class IfNode(
    val proposition: ExprNode,
    val trueStat: StatNode,
    val falseStat: StatNode,
    val ctx: ParserRuleContext?,
) : StatNode {
    lateinit var trueST: SymbolTable
    lateinit var falseST: SymbolTable

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.trueST = SymbolTable(st)
        this.falseST = SymbolTable(st)

        if (proposition.type != BoolType)
            throw SemanticsException(
                "If statement proposition must be boolean",
                ctx
            )

        proposition.validate(st, funTable)
        trueStat.validate(trueST, funTable)
        falseStat.validate(falseST, funTable)
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateIf(this)
    }
}