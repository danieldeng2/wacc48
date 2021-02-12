package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.type.Type
import analyser.nodes.ASTNode
import analyser.nodes.statement.*
import exceptions.SemanticsException
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

        validateReturnType(body)
    }

    private fun validateReturnType(body: StatNode) {
        var lastStat = body
        while (lastStat is SeqNode) {
            lastStat = lastStat.secondStat
        }

        when (lastStat) {
            is BeginNode -> validateReturnType(lastStat.stat)
            is IfNode -> {
                validateReturnType(lastStat.trueStat)
                validateReturnType(lastStat.falseStat)
            }
            is ReturnNode ->
                if (lastStat.value.type != retType)
                    throw SemanticsException(
                        "The expected return type of Function $identifier is: $retType," +
                                " actual return type: ${lastStat.value.type}", ctx
                    )
        }
    }
}
