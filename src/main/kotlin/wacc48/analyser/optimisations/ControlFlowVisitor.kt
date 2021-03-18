package wacc48.analyser.optimisations

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

object ControlFlowVisitor : ASTVisitor {
    var optimisations = 0

    fun optimise(node: ASTNode) : Int {
        optimisations = 0
        node.acceptVisitor(this)
        return optimisations
    }

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
        if (proposition is BoolLiteral && !proposition.value
        ) {
            optimisations++
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

    override fun visitFunction(node: FuncNode) {
        node.body = analyseStat(node.body)
    }

    override fun visitBegin(node: BeginNode) {

    }

    override fun visitSeq(node: SeqNode) {

    }

    override fun visitWhile(node: WhileNode) {

    }

    override fun visitIf(node: IfNode) {

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