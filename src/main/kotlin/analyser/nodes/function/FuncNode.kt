package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.type.Type
import analyser.nodes.ASTNode
import analyser.nodes.statement.*
import exceptions.SemanticsException
import generator.TranslatorContext
import generator.armInstructions.*
import generator.armInstructions.operands.NumOp
import generator.armInstructions.operands.Register
import org.antlr.v4.runtime.ParserRuleContext
import java.lang.Math.min
import javax.management.Query.div

data class FuncNode(
    val identifier: String,
    val paramList: ParamListNode,
    val retType: Type,
    val body: StatNode,
    override val ctx: ParserRuleContext?
) : ASTNode {

    private val MAX_IMMEDIATE_VALUE = 1024

    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable

    lateinit var paramListTable: SymbolTable
    lateinit var bodyTable: SymbolTable

    fun validatePrototype(ft: SymbolTable) {
        if (ft.containsInCurrentScope(identifier))
            throw SemanticsException(
                "Illegal re-declaration of function $identifier",
                ctx
            )

        ft.add(identifier, this)
    }

    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable

        this.paramListTable = SymbolTable(st)
        this.bodyTable = SymbolTable(paramListTable)

        retType.validate(st, funTable)
        paramList.validate(paramListTable, funTable)
        body.validate(bodyTable, funTable)

        if (identifier != "main")
            validateReturnType(body)
    }

    private fun validateReturnType(body: StatNode) {
        when (body) {
            is SeqNode -> validateReturnType(body.last())
            is BeginNode -> validateReturnType(body.stat)
            is IfNode -> {
                validateReturnType(body.trueStat)
                validateReturnType(body.falseStat)
            }
            is ReturnNode ->
                if (body.value.type != retType)
                    throw SemanticsException(
                        "The expected return type of Function $identifier is: $retType," +
                                " actual return type: ${body.value.type}", ctx
                    )
        }
    }

    override fun translate(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            val localStackSize = bodyTable.getLocalVariablesSize()

            add(LabelInstr(identifier))
            add(PUSHInstr(Register.LR))

            for (size in localStackSize downTo 1 step MAX_IMMEDIATE_VALUE) {
                add(
                    SUBInstr(
                        Register.SP,
                        Register.SP,
                        NumOp(minOf(size, MAX_IMMEDIATE_VALUE))
                    )
                )
            }

            addAll(body.translate(ctx))

            for (size in localStackSize downTo 1 step MAX_IMMEDIATE_VALUE) {
                add(
                    ADDInstr(
                        Register.SP,
                        Register.SP,
                        NumOp(minOf(size, MAX_IMMEDIATE_VALUE))
                    )
                )

            }
            if (identifier == "main") {
                add(MOVInstr(Register.R0, NumOp(0)))
            }
            add(POPInstr(Register.PC))
        }

}
