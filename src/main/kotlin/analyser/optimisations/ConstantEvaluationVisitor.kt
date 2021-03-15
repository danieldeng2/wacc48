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
    override fun visitNode(node: ASTNode) {
        TODO("Not yet implemented")
    }

    override fun visitProgram(node: ProgNode) {
        TODO("Not yet implemented")
    }

    override fun visitMain(node: MainNode) {
        TODO("Not yet implemented")
    }

    override fun visitExit(node: ExitNode) {
        TODO("Not yet implemented")
    }

    override fun visitFunction(node: FuncNode) {
        TODO("Not yet implemented")
    }

    override fun visitFuncCall(node: FuncCallNode) {
        TODO("Not yet implemented")
    }

    override fun visitParam(node: ParamNode) {
        TODO("Not yet implemented")
    }

    override fun visitNewPair(node: NewPairNode) {
        TODO("Not yet implemented")
    }

    override fun visitDeclaration(node: DeclarationNode) {
        TODO("Not yet implemented")
    }

    override fun visitArgList(node: ArgListNode) {
        TODO("Not yet implemented")
    }

    override fun visitAssignment(node: AssignmentNode) {
        TODO("Not yet implemented")
    }

    override fun visitBinOp(node: BinOpNode) {
        TODO("Not yet implemented")
    }

    override fun visitUnOp(node: UnOpNode) {
        TODO("Not yet implemented")
    }

    override fun visitPairElem(node: PairElemNode) {
        TODO("Not yet implemented")
    }

    override fun visitArrayElement(elem: ArrayElement) {
        TODO("Not yet implemented")
    }

    override fun visitArrayLiteral(literal: ArrayLiteral) {
        TODO("Not yet implemented")
    }

    override fun visitBoolLiteral(literal: BoolLiteral) {
        TODO("Not yet implemented")
    }

    override fun visitCharLiteral(literal: CharLiteral) {
        TODO("Not yet implemented")
    }

    override fun visitIdentifier(node: IdentifierNode) {
        TODO("Not yet implemented")
    }

    override fun visitIntLiteral(literal: IntLiteral) {
        TODO("Not yet implemented")
    }

    override fun visitPairLiteral(literal: PairLiteral) {
        TODO("Not yet implemented")
    }

    override fun visitStringLiteral(literal: StringLiteral) {
        TODO("Not yet implemented")
    }

    override fun visitBegin(node: BeginNode) {
        TODO("Not yet implemented")
    }

    override fun visitFree(node: FreeNode) {
        TODO("Not yet implemented")
    }

    override fun visitIf(node: IfNode) {
        TODO("Not yet implemented")
    }

    override fun visitPrint(node: PrintNode) {
        TODO("Not yet implemented")
    }

    override fun visitRead(node: ReadNode) {
        TODO("Not yet implemented")
    }

    override fun visitReturn(node: ReturnNode) {
        TODO("Not yet implemented")
    }

    override fun visitSeq(node: SeqNode) {
        TODO("Not yet implemented")
    }

    override fun visitWhile(node: WhileNode) {
        TODO("Not yet implemented")
    }
}