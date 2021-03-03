package datastructures.nodes.assignment

import datastructures.SymbolTable
import datastructures.nodes.expr.ExprNode
import datastructures.nodes.expr.IdentifierNode
import datastructures.nodes.function.FuncNode
import datastructures.type.PairType
import datastructures.type.Type
import datastructures.type.VoidType
import analyser.exceptions.SemanticsException
import generator.instructions.Instruction
import generator.instructions.arithmetic.ADDInstr
import generator.instructions.branch.BLInstr
import generator.instructions.load.LDRInstr
import generator.instructions.move.MOVInstr
import generator.instructions.operands.MemAddr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.instructions.store.STRInstr
import generator.translator.CodeGeneratorVisitor
import generator.translator.TranslatorContext
import generator.translator.lib.errors.CheckNullPointer
import generator.translator.helpers.*
import org.antlr.v4.runtime.ParserRuleContext

data class PairElemNode(
    val expr: ExprNode,
    val isFirst: Boolean,
    val ctx: ParserRuleContext?
) : LHSNode, RHSNode {
    override var type: Type = VoidType
    lateinit var st: SymbolTable
    override var mode: AccessMode = AccessMode.READ

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
        expr.validate(st, funTable)

        if (expr.type !is PairType)
            throw SemanticsException("Cannot dereference pair $expr", ctx)

        val nameType = expr.type
        if (nameType is PairType) {
            type = when {
                isFirst -> nameType.firstType
                else -> nameType.secondType
            }
        }
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translatePairElem(this)
    }

}