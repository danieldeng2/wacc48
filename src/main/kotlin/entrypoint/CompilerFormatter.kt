package entrypoint

import tree.nodes.ASTNode


interface CompilerFormatter {

    fun compile(astNode: ASTNode): List<String>

    fun createExecutable(asmPath: String, execPath: String)

}