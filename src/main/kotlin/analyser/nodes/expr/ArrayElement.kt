package analyser.nodes.expr

import analyser.SymbolTable
import analyser.nodes.assignment.AccessMode
import analyser.nodes.assignment.LHSNode
import analyser.nodes.function.ParamNode
import analyser.nodes.type.*
import exceptions.SemanticsException
import generator.instructions.Instruction
import generator.instructions.arithmetic.ADDInstr
import generator.instructions.branch.BLInstr
import generator.instructions.load.LDRInstr
import generator.instructions.move.MOVInstr
import generator.instructions.operands.*
import generator.instructions.store.STRInstr
import generator.translator.*
import generator.translator.lib.errors.CheckArrayBounds
import org.antlr.v4.runtime.ParserRuleContext
import java.lang.ClassCastException

data class ArrayElement(
    val name: String,
    val arrIndices: List<ExprNode>,
    override val ctx: ParserRuleContext?
) : ExprNode, LHSNode {

    override lateinit var st: SymbolTable
    override lateinit var funTable: SymbolTable
    override var mode: AccessMode = AccessMode.READ

    override lateinit var type: Type


    override fun validate(st: SymbolTable, funTable: SymbolTable) {
        this.st = st
        this.funTable = funTable
        if (!st.containsInAnyScope(name))
            throw SemanticsException("Cannot find array $name", ctx)
        arrIndices.forEach { it.validate(st, funTable) }
        val typedElem = st[name] as ParamNode

        var identTypeTemp = typedElem.type
        if (identTypeTemp !is ArrayType)
            throw SemanticsException("$name is not an array", null)

        for (i in arrIndices.indices) {
            try {
                identTypeTemp = (identTypeTemp as ArrayType).elementType
            } catch (e: ClassCastException) {
                throw SemanticsException(
                    "Invalid de-referencing of array $name",
                    ctx
                )
            }
        }
        type = identTypeTemp
    }

    override fun translate(ctx: TranslatorContext) =
        when (mode) {
            AccessMode.ASSIGN -> translateAssignment(ctx)
            AccessMode.READ -> translateRead(ctx)
            AccessMode.ADDRESS -> TODO("translate ArrayElement with mode $mode")
        }


    private fun translateAssignment(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            ctx.addLibraryFunction(CheckArrayBounds)

            add(pushAndIncrement(ctx, Register.R0, Register.R4))
            add(
                loadLocalVar(
                    varType = ArrayType(type, null),
                    stackOffset = ctx.getOffsetOfLocalVar(name, st),
                    rd = Register.R4
                )
            )

            arrIndices.dropLast(1).forEach {
                addAll(it.translate(ctx))
                addAll(checkArrayBounds())
                add(LDRInstr(Register.R4, MemAddr(Register.R4)))
            }
            addAll(arrIndices.last().translate(ctx))
            addAll(checkArrayBounds())

            add(MOVInstr(Register.R1, Register.R4))
            add(popAndDecrement(ctx, Register.R0, Register.R4))
            add(
                storeLocalVar(
                    varType = type,
                    stackOffset = 0,
                    rn = Register.R0,
                    rd = Register.R1
                )
            )
        }

    private fun translateRead(ctx: TranslatorContext) =
        mutableListOf<Instruction>().apply {
            ctx.addLibraryFunction(CheckArrayBounds)

            val offset = ctx.getOffsetOfLocalVar(name, st)
            add(
                LDRInstr(
                    Register.R0,
                    MemAddr(Register.SP, NumOp(offset))
                )
            )
            add(pushAndIncrement(ctx, Register.R4))
            add(MOVInstr(Register.R4, Register.R0))

            // Load and check bounds for each dereference
            arrIndices.forEach {
                addAll(it.translate(ctx))
                addAll(checkArrayBounds())
                add(LDRInstr(Register.R4, MemAddr(Register.R4)))
            }

            add(MOVInstr(Register.R0, Register.R4))
            add(popAndDecrement(ctx, Register.R4))
        }

    private fun checkArrayBounds() = listOf(
        BLInstr(CheckArrayBounds.label),
        ADDInstr(Register.R4, Register.R4, NumOp(4)),
        when (type) {
            is CharType, is BoolType -> ADDInstr(
                Register.R4,
                Register.R4,
                Register.R0
            )
            else -> ADDInstr(
                Register.R4,
                Register.R4,
                ShiftOp(Register.R0, ShiftType.LSL, NumOp(2))
            )
        }

    )
}