package generator.instructions

interface Instruction {

    fun tox86(): List<String> = TODO()

    fun toArm(): String
}