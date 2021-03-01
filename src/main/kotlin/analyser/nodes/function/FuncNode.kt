package analyser.nodes.function

import analyser.SymbolTable
import analyser.nodes.ASTNode
import analyser.nodes.statement.*
import analyser.nodes.type.Type
import exceptions.SemanticsException
import generator.instructions.Instruction
import generator.instructions.directives.LabelInstr
import generator.instructions.move.MOVInstr
import generator.instructions.operands.NumOp
import generator.instructions.operands.Register
import generator.instructions.stack.POPInstr
import generator.instructions.stack.PUSHInstr
import generator.translator.TranslatorContext
import generator.translator.newScope
import org.antlr.v4.runtime.ParserRuleContext

data class FuncNode(
    val identifier: String,
    val paramList: ParamListNode,
    val retType: Type,
    val body: StatNode,
    override val ctx: ParserRuleContext?
) : ASTNode {
    override lateinit var st: SymbolTable

    lateinit var paramListTable: SymbolTable
    lateinit var bodyTable: SymbolTable

    fun validatePrototype(ft: MutableMap<String, FuncNode>) {
        if (identifier in ft)
            throw SemanticsException(
                "Illegal re-declaration of function $identifier",
                ctx
            )
        ft[identifier] = this
    }

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        this.st = st
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
            add(LabelInstr(identifier))
            add(PUSHInstr(Register.LR))

            newScope(bodyTable) {
                addAll(body.translate(ctx))
            }

            if (identifier == "main") {
                add(MOVInstr(Register.R0, NumOp(0)))
            }
            add(POPInstr(Register.PC))
        }

}
