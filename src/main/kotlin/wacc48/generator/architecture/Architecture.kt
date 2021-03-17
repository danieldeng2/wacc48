package wacc48.generator.architecture

import wacc48.tree.nodes.ASTNode


interface Architecture {

    fun compile(astNode: ASTNode): List<String>

    fun createExecutable(asmPath: String, execPath: String)

}