package datastructures.nodes.expr

import datastructures.SymbolTable
import datastructures.nodes.assignment.AccessMode
import datastructures.nodes.assignment.LHSNode

import datastructures.nodes.function.FuncNode
import datastructures.type.ArrayType
import datastructures.type.BoolType
import datastructures.type.CharType
import datastructures.type.Type
import analyser.exceptions.SemanticsException
import generator.instructions.Instruction
import generator.instructions.arithmetic.ADDInstr
import generator.instructions.branch.BLInstr
import generator.instructions.load.LDRInstr
import generator.instructions.move.MOVInstr
import generator.instructions.operands.*
import generator.translator.CodeGeneratorVisitor
import generator.translator.TranslatorContext
import generator.translator.lib.errors.CheckArrayBounds
import generator.translator.helpers.*
import org.antlr.v4.runtime.ParserRuleContext

data class ArrayElement(
    val name: String,
    val arrIndices: List<ExprNode>,
    val ctx: ParserRuleContext?
) : ExprNode, LHSNode {
    lateinit var st: SymbolTable
    override var mode: AccessMode = AccessMode.READ
    override lateinit var type: Type


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
        if (!st.containsInAnyScope(name))
            throw SemanticsException("Cannot find array $name", ctx)
        arrIndices.forEach { it.validate(st, funTable) }
        var identityType = st[name]!!
        if (identityType !is ArrayType)
            throw SemanticsException("$name is not an array", null)

        for (i in arrIndices.indices) {
            try {
                identityType = (identityType as ArrayType).elementType
            } catch (e: ClassCastException) {
                throw SemanticsException(
                    "Invalid de-referencing of array $name",
                    ctx
                )
            }
        }
        type = identityType
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateArrayElement(this)
    }
}