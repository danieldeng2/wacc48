package tree.nodes.expr

import shell.MemoryTable
import shell.ShellRunTimeException
import tree.ASTVisitor
import tree.SymbolTable
import tree.nodes.function.FuncNode
import tree.type.ArrayType
import tree.type.CharType
import tree.type.Type
import tree.type.VoidType

//To be used in the evaluator memory table to represent arrays of other arrays
//Values is a list of the names of the subarrays being referenced in the memory table
class DeepArrayLiteral(var values: List<String>, elemType: Type) : Literal {

    var elemType: Type = VoidType
    override var type: Type = ArrayType(elemType, null)
    var nameInMemTable: String? = null

    override fun literalToString(mt: MemoryTable?): String =
        if (elemType is CharType)
            values.map { (mt?.getLiteral(it) as CharLiteral).literalToString() }.joinToString("")
        else
            "[" + values.joinToString(", ") {
                mt?.getLiteral(it)?.literalToString(mt)
                    ?: throw ShellRunTimeException("Error reading array from memory")
            } + "]"

    override fun reduceToLiteral(mt: MemoryTable?): Literal =
        this


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitDeepArrayLiteral(this)
    }

}
