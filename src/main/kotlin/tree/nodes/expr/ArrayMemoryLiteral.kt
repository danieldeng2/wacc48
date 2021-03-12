package tree.nodes.expr

import generator.translator.CodeGeneratorVisitor
import shell.CodeEvaluatorVisitor
import shell.MemoryTable
import tree.SymbolTable
import tree.nodes.function.FuncNode
import tree.type.ArrayType
import tree.type.CharType
import tree.type.Type
import tree.type.VoidType

//To be used in the evaluator memory table to represent arrays of other arrays
//Values is a list of the names of the subarrays being referenced in the memory table
class DeepArrayLiteral(val values: List<String>, elemType: Type) : Literal {
    var elemType: Type = VoidType
    override var type: Type = ArrayType(elemType, null)

    override fun literalToString(mt: MemoryTable?): String =
        if (elemType is CharType)
            values.map { (it as CharLiteral).literalToString() }.joinToString("")
        else
            "[" + values.joinToString(", ") { mt?.getLiteral(it)?.reduceToLiteral(mt)?.literalToString()!! } + "]"

    override fun reduceToLiteral(mt: MemoryTable?): Literal =
        this


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateDeepArrayLiteral(this)
    }

    override fun acceptCodeEvalVisitor(visitor: CodeEvaluatorVisitor): Literal? {
        return visitor.translateDeepArrayLiteral(this)
    }
}
