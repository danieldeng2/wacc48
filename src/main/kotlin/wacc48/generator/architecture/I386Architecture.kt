package wacc48.generator.architecture

import wacc48.generator.translator.CodeGeneratorVisitor
import wacc48.tree.nodes.ASTNode
import java.util.concurrent.TimeUnit

object I386Architecture : Architecture {

    private val assembler = "nasm"
    private val linker = "gcc"

    override fun compile(astNode: ASTNode) =
        CodeGeneratorVisitor(astNode).translateTox86()


    override fun createExecutable(asmPath: String, execPath: String) {
        val objectFileName = "$execPath.o"
        ProcessBuilder(
            assembler,
            "-f",
            "elf32",
            "-o",
            objectFileName,
            asmPath
        ).start().waitFor(10, TimeUnit.SECONDS)

        ProcessBuilder(
            linker,
            "-m32",
            "-o",
            execPath,
            objectFileName
        ).start().waitFor(10, TimeUnit.SECONDS)
    }
}