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

interface ASTVisitor<T> {
    /** Wrapper method to tell [node] to invoke its corresponding
     *  'visit' method
     */
    
    fun defaultResult(): T
    
    fun visitNode(node: ASTNode): T

    fun visitChildren(node : ASTNode): T

    fun visitProgram(node: ProgNode): T

    fun visitMain(node: MainNode): T

    fun visitExit(node: ExitNode): T

    fun visitFunction(node: FuncNode): T

    fun visitFuncCall(node: FuncCallNode): T

    fun visitParam(node: ParamNode): T

    fun visitNewPair(node: NewPairNode): T

    fun visitDeclaration(node: DeclarationNode): T

    fun visitArgList(node: ArgListNode): T

    fun visitAssignment(node: AssignmentNode): T

    fun visitBinOp(node: BinOpNode): T
    fun visitUnOp(node: UnOpNode): T

    fun visitPairElem(node: PairElemNode): T
    fun visitArrayElement(elem: ArrayElement): T

    fun visitArrayLiteral(literal: ArrayLiteral): T
    fun visitBoolLiteral(literal: BoolLiteral): T
    fun visitCharLiteral(literal: CharLiteral): T

    fun visitIdentifier(node: IdentifierNode): T

    fun visitIntLiteral(literal: IntLiteral): T
    fun visitPairLiteral(literal: PairLiteral): T
    fun visitDeepArrayLiteral(node: DeepArrayLiteral): T
    fun visitPairMemoryLiteral(node: PairMemoryLiteral): T
    fun visitStringLiteral(literal: StringLiteral): T

    fun visitBegin(node: BeginNode): T

    fun visitFree(node: FreeNode): T

    fun visitIf(node: IfNode): T

    fun visitPrint(node: PrintNode): T

    fun visitRead(node: ReadNode): T

    fun visitReturn(node: ReturnNode): T
    fun visitSeq(node: SeqNode): T
    fun visitWhile(node: WhileNode): T
}