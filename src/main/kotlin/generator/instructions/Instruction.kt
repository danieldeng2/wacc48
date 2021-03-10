package generator.instructions

interface Instruction {

    fun tox86(): List<String> = emptyList()

    fun toArm(): String
}