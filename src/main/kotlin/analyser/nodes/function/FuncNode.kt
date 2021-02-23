package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.type.Type
import analyser.nodes.ASTNode
import analyser.nodes.statement.*
import exceptions.SemanticsException
import generator.TranslatorContext
import generator.armInstructions.*
import generator.armInstructions.operands.ImmOp
import generator.armInstructions.operands.Register
import org.antlr.v4.runtime.ParserRuleContext

data class FuncNode(
    val identifier: String,
    val paramList: ParamListNode,
    val retType: Type,
    val body: StatNode,
    override val ctx: ParserRuleContext?
) : ASTNode {
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

    override fun translate(ctx: TranslatorContext): List<Instruction> {
        val instructions = mutableListOf<Instruction>().apply {
            add(LabelInstr("main"))
            add(PUSHInstr(Register.LR))

        }

        val localStackSize = st.getLocalVariablesSize()

        if (localStackSize > 0)
            instructions.add(
                SUBInstr(
                    Register.SP,
                    Register.SP,
                    ImmOp(st.getLocalVariablesSize())
                )
            )

        instructions.addAll(body.translate(ctx))

        if (localStackSize > 0)
            instructions.add(
                ADDInstr(
                    Register.SP,
                    Register.SP,
                    ImmOp(st.getLocalVariablesSize())
                )
            )

        return instructions.apply {
            if (identifier == "main") {
                add(LDRInstr(Register.R0, ImmOp(0)))
                add(POPInstr(Register.PC))
                add(Directive(".ltorg"))
            } else {
                add(POPInstr(Register.PC))
            }
        }
    }
}
