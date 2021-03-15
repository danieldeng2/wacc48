package generator.instructions.directives

import generator.instructions.Instruction

class LabelInstr(
    private val label: String,
    private val isSection: Boolean = false,
    private val isGlobalHeader: Boolean = false
) : Instruction {

    override fun tox86() =
        listOf(
            when {
                isSection -> "section .$label\n"
                isGlobalHeader -> "\t$label"
                else -> "$label:"
            }
        )

    override fun toArm() =
        when {
            isSection -> ".$label\n"
            isGlobalHeader -> ".$label"
            else -> "$label:"
        }
}