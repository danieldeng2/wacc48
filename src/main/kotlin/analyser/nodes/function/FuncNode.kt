package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.type.Type
import analyser.nodes.ASTNode
import analyser.nodes.statement.*
import exceptions.SemanticsException
import exceptions.SyntaxException
import org.antlr.v4.runtime.ParserRuleContext

data class FuncNode(
    val identifier: String,
    val paramList: ParamListNode,
    val retType: Type,
    val body: StatNode,
    override val ctx: ParserRuleContext?
) : ASTNode {

    fun validatePrototype(ft: SymbolTable) {
        if (ft.containsInCurrentScope(identifier))
            throw SemanticsException("Illegal re-declaration of function $identifier", ctx)

        ft.add(identifier, this)
    }

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        val paramST = SymbolTable(st)

        retType.validate(st, funTable)
        paramList.validate(paramST, funTable)
        body.validate(SymbolTable(paramST), funTable)

        if (!correctReturnType(body))
            throw SemanticsException("Function $identifier must end with either a return or exit", ctx)
    }

    private fun correctReturnType(body: StatNode): Boolean {
        var lastStat = body
        while (lastStat is SeqNode) {
            lastStat = lastStat.secondStat
        }

        return when (lastStat) {
            is BeginNode -> correctReturnType(lastStat.stat)
            is IfNode -> correctReturnType(lastStat.trueStat)
                    && correctReturnType(lastStat.falseStat)
            is ReturnNode ->
                if (lastStat.value.type == retType) true
                else throw SemanticsException(
                    "The expected return type of Function $identifier is: ${lastStat.value.type.toString()}" +
                            " Actual return type: ${retType.toString()}", ctx
                )
            else -> true
        }
    }
}
