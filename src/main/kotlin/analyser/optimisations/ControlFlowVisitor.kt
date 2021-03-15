package analyser.optimisations

import tree.ASTVisitor
import tree.nodes.ASTNode
import tree.nodes.ProgNode
import tree.nodes.assignment.AssignmentNode
import tree.nodes.assignment.NewPairNode
import tree.nodes.assignment.PairElemNode
import tree.nodes.expr.*
import tree.nodes.expr.operators.BinOpNode
import tree.nodes.expr.operators.UnOpNode
import tree.nodes.function.*
import tree.nodes.statement.*

object ControlFlowVisitor : ASTVisitor {

    private fun analyseStat(node: StatNode): StatNode {
        return when (node) {
            is IfNode -> analyseIf(node)
            is WhileNode -> analyseWhile(node)
            is SeqNode -> {
                node.sequence = node.sequence.map { analyseStat(it) }
                return node
            }
            else -> node
        }
    }

    private fun analyseIf(node: IfNode): StatNode {
        val proposition = node.proposition
        if (proposition is BoolLiteral) {
            if (proposition.value) {
                return analyseStat(node.trueStat)
            }
            return analyseStat(node.falseStat)
        }
        return node

    }

    private fun analyseWhile(node: WhileNode): StatNode {
        val proposition = node.proposition
        if (proposition is BoolLiteral && !proposition.value
        ) {
            return SkipNode
        }
        node.body = analyseStat(node.body)
        return node
    }

    override fun visitNode(node: ASTNode) {
        node.acceptVisitor(this)
    }

    override fun visitProgram(node: ProgNode) {
        node.functions.forEach {
            it.acceptVisitor(this)
        }
        node.main.acceptVisitor(this)
    }

    override fun visitMain(node: MainNode) {
        node.body = analyseStat(node.body)
    }

    override fun visitBegin(node: BeginNode) {
        node.stat = analyseStat(node.stat)
    }

    override fun visitFunction(node: FuncNode) {
        node.body = analyseStat(node.body)
    }

    override fun visitSeq(node: SeqNode) {
        node.sequence = node.sequence.map { analyseStat(it) }
    }

    override fun visitWhile(node: WhileNode) {
        node.body = analyseStat(node.body)
    }

    override fun visitIf(node: IfNode) {
        node.falseStat = analyseStat(node.falseStat)
        node.trueStat = analyseStat(node.trueStat)
    }

    override fun visitExit(node: ExitNode) {

    }

    override fun visitFuncCall(node: FuncCallNode) {

    }

    override fun visitParam(node: ParamNode) {

    }

    override fun visitNewPair(node: NewPairNode) {

    }

    override fun visitDeclaration(node: DeclarationNode) {

    }

    override fun visitArgList(node: ArgListNode) {

    }

    override fun visitAssignment(node: AssignmentNode) {

    }

    override fun visitBinOp(node: BinOpNode) {

    }

    override fun visitUnOp(node: UnOpNode) {

    }

    override fun visitPairElem(node: PairElemNode) {

    }

    override fun visitArrayElement(elem: ArrayElement) {

    }

    override fun visitArrayLiteral(literal: ArrayLiteral) {

    }

    override fun visitBoolLiteral(literal: BoolLiteral) {

    }

    override fun visitCharLiteral(literal: CharLiteral) {

    }

    override fun visitIdentifier(node: IdentifierNode) {

    }

    override fun visitIntLiteral(literal: IntLiteral) {

    }

    override fun visitPairLiteral(literal: PairLiteral) {

    }

    override fun visitDeepArrayLiteral(node: DeepArrayLiteral) {

    }

    override fun visitPairMemoryLiteral(node: PairMemoryLiteral) {
        TODO("Not yet implemented")
    }

    override fun visitStringLiteral(literal: StringLiteral) {

    }

    override fun visitFree(node: FreeNode) {

    }

    override fun visitPrint(node: PrintNode) {

    }

    override fun visitRead(node: ReadNode) {

    }

    override fun visitReturn(node: ReturnNode) {

    }


}