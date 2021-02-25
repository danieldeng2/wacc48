package generator.armInstructions

class LabelInstr(
    private val label: String,
    private val isSection: Boolean = false,
    private val isGlobalHeader: Boolean = false
) : Instruction {
    override fun toString() =
        when {
            isSection -> ".$label\n"
            isGlobalHeader -> ".$label"
            else -> "$label:"
        }
}