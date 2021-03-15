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

class ConstantEvaluationVisitor : ASTVisitor {

    private fun analyse(expr: ExprNode): ExprNode {
        return expr
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
        node.body.acceptVisitor(this)
    }

    override fun visitExit(node: ExitNode) {
        node.expr = analyse(node.expr)
    }

    override fun visitFunction(node: FuncNode) {
        node.body.acceptVisitor(this)
    }

    override fun visitFuncCall(node: FuncCallNode) {
        node.argList.acceptVisitor(this)
    }

    override fun visitParam(node: ParamNode) {

    }

    override fun visitNewPair(node: NewPairNode) {
        node.firstElem = analyse(node.firstElem)
        node.secondElem = analyse(node.secondElem)
    }

    override fun visitDeclaration(node: DeclarationNode) {
        when (node.value) {
            is ExprNode -> node.value = analyse(node.value as ExprNode)
            else -> node.value.acceptVisitor(this)
        }
    }

    override fun visitArgList(node: ArgListNode) {
        node.args = node.args.map { analyse(it) }
    }

    override fun visitAssignment(node: AssignmentNode) {
        when (node.value) {
            is ExprNode -> node.value = analyse(node.value as ExprNode)
            else -> node.value.acceptVisitor(this)
        }
        node.name.acceptVisitor(this)

    }

    override fun visitBinOp(node: BinOpNode) {
        node.firstExpr = analyse(node.firstExpr)
        node.secondExpr = analyse(node.secondExpr)
    }

    override fun visitUnOp(node: UnOpNode) {
        node.expr = analyse(node.expr)
    }

    override fun visitPairElem(node: PairElemNode) {
        node.expr = analyse(node.expr)
    }

    override fun visitArrayElement(elem: ArrayElement) {
        elem.arrIndices = elem.arrIndices.map { analyse(it) }
    }

    override fun visitArrayLiteral(literal: ArrayLiteral) {
        literal.values = literal.values.map { analyse(it) }
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
        node.stat.acceptVisitor(this)
    }

    override fun visitFree(node: FreeNode) {
        node.value = analyse(node.value)
    }

    override fun visitIf(node: IfNode) {
        node.proposition = analyse(node.proposition)
        node.trueStat.acceptVisitor(this)
        node.falseStat.acceptVisitor(this)
    }

    override fun visitPrint(node: PrintNode) {
        node.value = analyse(node.value)
    }

    override fun visitRead(node: ReadNode) {
        node.value.acceptVisitor(this)
    }

    override fun visitReturn(node: ReturnNode) {
        node.value = analyse(node.value)
    }

    override fun visitSeq(node: SeqNode) {
        node.sequence.forEach {
            it.acceptVisitor(this)
        }
    }

    override fun visitWhile(node: WhileNode) {
        node.proposition = analyse(node.proposition)
        node.body.acceptVisitor(this)
    }
}