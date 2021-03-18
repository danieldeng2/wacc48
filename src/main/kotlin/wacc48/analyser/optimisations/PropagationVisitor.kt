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

class PropagationVisitor(private val constants : Map<String,BaseLiteral>) : ASTVisitor {

    private fun propagate(expr: ExprNode): ExprNode {
        return when(expr) {
            is IdentifierNode -> {
                if (constants.contains(expr.name)) {
                    constants[expr.name]!!
                } else {
                    expr
                }
            }
            is ArrayElement -> {
                expr.acceptVisitor(this)
                expr
            }
            is BinOpNode -> {
                expr.firstExpr = propagate(expr.firstExpr)
                expr.secondExpr = propagate(expr.secondExpr)
                expr
            }
            is UnOpNode -> {
                expr.expr = propagate(expr.expr)
                expr
            }
            else -> expr
        }
    }

    override fun visitNode(node: ASTNode) {
        node.acceptVisitor(this)
    }

    override fun visitProgram(node: ProgNode) {

    }

    override fun visitMain(node: MainNode) {
        node.body.acceptVisitor(this)
    }

    override fun visitExit(node: ExitNode) {
        node.expr = propagate(node.expr)
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
        node.firstElem = propagate(node.firstElem)
        node.secondElem = propagate(node.secondElem)
    }

    override fun visitDeclaration(node: DeclarationNode) {
        when(node.value){
            is ExprNode -> node.value = propagate(node.value as ExprNode)
            else -> node.value.acceptVisitor(this)
        }
    }

    override fun visitArgList(node: ArgListNode) {
        node.args = node.args.map {
            propagate(it)
        }
    }

    override fun visitAssignment(node: AssignmentNode) {
        when(node.value){
            is ExprNode -> node.value = propagate(node.value as ExprNode)
            else -> node.value.acceptVisitor(this)
        }
    }

    override fun visitBinOp(node: BinOpNode) {

    }

    override fun visitUnOp(node: UnOpNode) {

    }

    override fun visitPairElem(node: PairElemNode) {
        node.expr = propagate(node.expr)
    }

    override fun visitArrayElement(elem: ArrayElement) {

    }

    override fun visitArrayLiteral(literal: ArrayLiteral) {
        literal.values = literal.values.map {
            propagate(it)
        }
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

    override fun visitBegin(node: BeginNode) {
        node.stat.acceptVisitor(this)
    }

    override fun visitFree(node: FreeNode) {
        node.value = propagate(node.value)
    }

    override fun visitIf(node: IfNode) {
        node.proposition = propagate(node.proposition)
        node.trueStat.acceptVisitor(this)
        node.falseStat.acceptVisitor(this)
    }

    override fun visitPrint(node: PrintNode) {
        node.value = propagate(node.value)
    }

    override fun visitRead(node: ReadNode) {

    }

    override fun visitReturn(node: ReturnNode) {
        node.value = propagate(node.value)
    }

    override fun visitSeq(node: SeqNode) {
        node.sequence.forEach {
            it.acceptVisitor(this)
        }
    }

    override fun visitWhile(node: WhileNode) {
        node.body.acceptVisitor(this)
        node.proposition = propagate(node.proposition)
    }
}