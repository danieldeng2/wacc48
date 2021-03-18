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

object DeadCodeVisitor : ASTBaseVisitor<Unit>() {
    private var optimisations = 0
    private var inactiveVariables = emptySet<String>()

    fun optimise(node: ASTNode, inactiveVariables: Set<String>) : Int {
        optimisations = 0
        this.inactiveVariables = inactiveVariables
        node.acceptVisitor(this)
        return optimisations
    }

    override fun defaultResult() {}

    private fun eliminate(node : StatNode) : StatNode {
        return when(node){
            is AssignmentNode -> {
                if (node.name is IdentifierNode &&
                    inactiveVariables.contains(node.name.name)) {
                        optimisations++
                    SkipNode
                } else {
                    node
                }
            }
            is BeginNode -> {
                node.stat = eliminate(node.stat)
                node
            }
            is DeclarationNode -> {
                if(inactiveVariables.contains(node.name.text)) {
                    optimisations++
                    SkipNode
                } else {
                    node
                }
            }
            is IfNode -> {
                node.falseStat = eliminate(node.falseStat)
                node.trueStat = eliminate(node.trueStat)
                node
            }
            is ReadNode -> {
                if (node.value is IdentifierNode &&
                    inactiveVariables.contains(node.value.name)) {
                    optimisations++
                    SkipNode
                } else {
                    node
                }
            }
            is SeqNode -> {
                node.sequence = node.sequence.map { eliminate(it) }
                node
            }
            is WhileNode -> {
                node.body = eliminate(node.body)
                node
            }
            else -> node
        }
    }

    override fun visitProgram(node: ProgNode) {
        node.functions.forEach {
            it.acceptVisitor(this)
        }
        node.main.acceptVisitor(this)
    }

    override fun visitMain(node: MainNode) {
        node.body = eliminate(node.body)
    }

    override fun visitFunction(node: FuncNode) {
        node.body = eliminate(node.body)
    }
}