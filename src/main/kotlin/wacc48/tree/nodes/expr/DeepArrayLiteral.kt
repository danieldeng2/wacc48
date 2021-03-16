package wacc48.tree.nodes.expr

import wacc48.analyser.exceptions.Issue
import wacc48.shell.MemoryTable
import wacc48.shell.ShellRunTimeException
import wacc48.tree.ASTVisitor
import wacc48.tree.SymbolTable
import wacc48.tree.nodes.function.FuncNode
import wacc48.tree.type.ArrayType
import wacc48.tree.type.CharType
import wacc48.tree.type.Type
import wacc48.tree.type.VoidType

//To be used in the evaluator memory table to represent arrays of other arrays
//Values is a list of the names of the subarrays being referenced in the memory table
class DeepArrayLiteral(var values: List<String>, elemType: Type) : Literal {

    var elemType: Type = VoidType
    override var type: Type = ArrayType(elemType, null)
    var nameInMemTable: String? = null

    override fun literalToString(mt: MemoryTable?): String =
        if (elemType is CharType)
            values.joinToString("") { (mt?.getLiteral(it) as CharLiteral).literalToString() }
        else
            "[" + values.joinToString(", ") {
                mt?.getLiteral(it)?.literalToString(mt)
                    ?: throw ShellRunTimeException("Error reading array from memory")
            } + "]"

    override fun reduceToLiteral(mt: MemoryTable?): Literal =
        this


    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>,
        issues: MutableList<Issue>
    ) {
    }

    override fun acceptVisitor(visitor: ASTVisitor) {
        visitor.visitDeepArrayLiteral(this)
    }

}
