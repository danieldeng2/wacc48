package tree

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

interface ASTVisitor {
    /** Wrapper method to tell [node] to invoke its corresponding
     *  'visit' method
     */
    fun visitNode(node: ASTNode)

    fun visitProgram(node: ProgNode)

    fun visitMain(node: MainNode)

    fun visitExit(node: ExitNode)

    fun visitFunction(node: FuncNode)

    fun visitFuncCall(node: FuncCallNode)

    fun visitParam(node: ParamNode)

    fun visitNewPair(node: NewPairNode)

    fun visitDeclaration(node: DeclarationNode)

    fun visitArgList(node: ArgListNode)

    fun visitAssignment(node: AssignmentNode)

    fun visitBinOp(node: BinOpNode)
    fun visitUnOp(node: UnOpNode)

    fun visitPairElem(node: PairElemNode)
    fun visitArrayElement(elem: ArrayElement)

    fun visitArrayLiteral(literal: ArrayLiteral)
    fun visitBoolLiteral(literal: BoolLiteral)
    fun visitCharLiteral(literal: CharLiteral)

    fun visitIdentifier(node: IdentifierNode)
    fun visitIntLiteral(literal: IntLiteral)
    fun visitPairLiteral(literal: PairLiteral)
    fun visitStringLiteral(literal: StringLiteral)

    fun visitBegin(node: BeginNode)

    fun visitFree(node: FreeNode)

    fun visitIf(node: IfNode)

    fun visitPrint(node: PrintNode)

    fun visitRead(node: ReadNode)

    fun visitReturn(node: ReturnNode)

    fun visitSeq(node: SeqNode)

    fun visitWhile(node: WhileNode)
}