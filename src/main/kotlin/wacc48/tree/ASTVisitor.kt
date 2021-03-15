package wacc48.tree

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
    fun visitDeepArrayLiteral(node: DeepArrayLiteral)
    fun visitPairMemoryLiteral(node: PairMemoryLiteral)
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