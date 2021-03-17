package wacc48.generator.instructions.operands

/** This interface corresponds to Operand2 in ARM manual. */
interface LoadableOp {
    fun tox86(): String = ""

    fun toArm(): String
}