package analyser

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

@Suppress("UNREACHABLE_CODE")
class ControlFlowVisitor : ASTVisitor {

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
        return when (node.proposition) {
            is BoolLiteral -> {
                if (node.proposition.value) {
                    return node.trueStat
                }
                return node.falseStat
            }
            else -> return node
        }
    }

    private fun analyseWhile(node: WhileNode): StatNode {
        if (node.proposition is BoolLiteral && !node.proposition.value) {
            return SkipNode
        }
        return node
    }

    override fun visitNode(node: ASTNode) {
        node.acceptVisitor(this)
    }

    override fun visitProgram(node: ProgNode) {
        node.functions.forEach {
            visitFunction(it)
        }
        visitMain(node.main)
    }

    override fun visitMain(node: MainNode) {
        node.body = analyseStat(node.body)
    }

    override fun visitExit(node: ExitNode) {

    }

    override fun visitFunction(node: FuncNode) {
        node.body = analyseStat(node.body)
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

    override fun visitStringLiteral(literal: StringLiteral) {

    }

    override fun visitBegin(node: BeginNode) {
        node.stat = analyseStat(node.stat)
    }

    override fun visitFree(node: FreeNode) {

    }

    override fun visitIf(node: IfNode) {
        node.falseStat = analyseStat(node.falseStat)
        node.trueStat = analyseStat(node.trueStat)
    }

    override fun visitPrint(node: PrintNode) {

    }

    override fun visitRead(node: ReadNode) {

    }

    override fun visitReturn(node: ReturnNode) {

    }

    override fun visitSeq(node: SeqNode) {
        node.sequence.map { analyseStat(it) }
    }

    override fun visitWhile(node: WhileNode) {
        node.body = analyseStat(node.body)
    }


}