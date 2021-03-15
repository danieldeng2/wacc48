package wacc48.generator.architecture

import wacc48.generator.translator.CodeGeneratorVisitor
import wacc48.tree.nodes.ASTNode
import java.util.concurrent.TimeUnit

object ArmArchitecture : Architecture {

    private val compiler = "arm-linux-gnueabi-gcc"
    private val architecture = "arm1176jzf-s"

    override fun compile(astNode: ASTNode): List<String> =
        CodeGeneratorVisitor(astNode).translateToArm()


    override fun createExecutable(asmPath: String, execPath: String) {
        ProcessBuilder(
            compiler,
            "-o",
            execPath,
            "-mcpu=$architecture",
            "-mtune=$architecture",
            asmPath
        ).start().waitFor(10, TimeUnit.SECONDS)
    }
}