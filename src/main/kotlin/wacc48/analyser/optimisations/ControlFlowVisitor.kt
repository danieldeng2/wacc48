package wacc48.analyser.optimisations

import wacc48.tree.ASTBaseVisitor
import wacc48.tree.ASTVisitor
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.ProgNode
import wacc48.tree.nodes.assignment.AssignmentNode
import wacc48.tree.nodes.assignment.NewPairNode
import wacc48.tree.nodes.assignment.PairElemNode
import wacc48.tree.nodes.expr.*
import wacc48.tree.nodes.expr.operators.BinOpNode
import wacc48.tree.nodes.expr.operators.UnOpNode
import wacc48.tree.nodes.function.*
import wacc48.tree.nodes.statement.*

object ControlFlowVisitor : ASTBaseVisitor<Unit>() {
    var optimisations = 0

    fun optimise(node: ASTNode): Int {
        optimisations = 0
        visitNode(node)
        return optimisations
    }

    override fun defaultResult() {}

    private fun analyseStat(node: StatNode): StatNode {
        return when (node) {
            is IfNode -> analyseIf(node)
            is WhileNode -> analyseWhile(node)
            is SeqNode -> {
                node.sequence = node.sequence.map { analyseStat(it) }
                node
            }
            is BeginNode -> {
                node.stat = analyseStat(node.stat)
                node
            }
            else -> node
        }
    }

    private fun analyseIf(node: IfNode): StatNode {
        val proposition = node.proposition
        if (proposition is BoolLiteral) {
            optimisations++
            if (proposition.value) {
                return analyseStat(node.trueStat)
            }
            return analyseStat(node.falseStat)
        }
        return node

    }

    private fun analyseWhile(node: WhileNode): StatNode {
        val proposition = node.proposition
        if (proposition is BoolLiteral && !proposition.value) {
            optimisations++
            return SkipNode
        }
        node.body = analyseStat(node.body)
        return node
    }

    override fun visitMain(node: MainNode) {
        node.body = analyseStat(node.body)
    }

    override fun visitFunction(node: FuncNode) {
        node.body = analyseStat(node.body)
    }

}