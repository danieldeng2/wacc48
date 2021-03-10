package tree.nodes

import analyser.exceptions.SyntaxException
import tree.SymbolTable
import tree.nodes.function.FuncNode
import tree.nodes.function.MainNode
import tree.nodes.statement.*
import generator.translator.CodeGeneratorVisitor
import org.antlr.v4.runtime.ParserRuleContext

data class ProgNode(
    val functions: List<FuncNode>,
    val main: MainNode,
    val ctx: ParserRuleContext?
) : ASTNode {

    override fun validate(
        st: SymbolTable,
        funTable: MutableMap<String, FuncNode>
    ) {
        functions.forEach {
            checkFunctionTerminates(it)
        }

        functions.forEach { it.validatePrototype(funTable) }
        functions.forEach { it.validate(st, funTable) }

        main.validate(st, funTable)
    }

    override fun acceptCodeGenVisitor(visitor: CodeGeneratorVisitor) {
        visitor.translateProgram(this)
    }
}

fun checkFunctionTerminates(func: FuncNode) {
    if (!allPathsTerminated(func.body))
        throw SyntaxException("Function ${func.identifier} must end with either a return or exit")
}

fun allPathsTerminated(body: StatNode): Boolean =
    when (body) {
        is SeqNode -> allPathsTerminated(body.last())
        is BeginNode -> allPathsTerminated(body.stat)
        is IfNode -> allPathsTerminated(body.trueStat)
                && allPathsTerminated(body.falseStat)
        is ReturnNode -> true
        is ExitNode -> true
        else -> false
    }
