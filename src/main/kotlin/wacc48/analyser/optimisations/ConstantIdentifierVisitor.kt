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

object ConstantIdentifierVisitor :  ASTBaseVisitor<Unit>() {
    private val declaredVars = mutableMapOf<String,BaseLiteral>()
    private val assignedVars = mutableListOf<String>()
    private val constantVars = mutableMapOf<ASTNode,Map<String,BaseLiteral>>()

    override fun defaultResult() {}

    fun identifyConstants(programNode: ASTNode): MutableMap<ASTNode, Map<String, BaseLiteral>> {
        constantVars.clear()
        visitNode(programNode)
        return constantVars
    }

    private fun newScope(){
        declaredVars.clear()
        assignedVars.clear()
    }

    override fun visitProgram(node: ProgNode) {
        node.functions.forEach { func ->
            newScope()
            visitNode(func)
            constantVars[func] = declaredVars.filterKeys { !assignedVars.contains(it) }
        }

        newScope()
        visitNode(node.main)
        constantVars[node.main] = declaredVars.filterKeys { !assignedVars.contains(it) }
    }

    override fun visitDeclaration(node: DeclarationNode) {
        if (node.value is BaseLiteral){
            if (declaredVars.contains(node.name.text)){
                assignedVars.add(node.name.text)
            } else {
                declaredVars[node.name.text] = node.value as BaseLiteral
            }

        }
    }

    override fun visitAssignment(node: AssignmentNode) {
        if (node.name is IdentifierNode &&
            declaredVars.contains(node.name.name)) {
            assignedVars.add(node.name.name)
        }
    }

    override fun visitRead(node: ReadNode) {
        if (node.value is IdentifierNode &&
            declaredVars.contains(node.value.name)) {
            assignedVars.add(node.value.name)
        }
    }

}