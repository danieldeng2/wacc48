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

object ConstantIdentifierVisitor : ASTVisitor{
    private val declaredVars = mutableMapOf<String,BaseLiteral>()
    private val assignedVars = mutableListOf<String>()
    private val constantVars = mutableMapOf<ASTNode,Map<String,BaseLiteral>>()

    fun identifyConstants(programNode: ASTNode): MutableMap<ASTNode, Map<String, BaseLiteral>> {
        constantVars.clear()
        visitNode(programNode)
        return constantVars
    }

    private fun newScope(){
        declaredVars.clear()
        assignedVars.clear()
    }

    override fun visitNode(node: ASTNode) {
        node.acceptVisitor(this)
    }

    override fun visitProgram(node: ProgNode) {
        node.functions.forEach { func ->
            newScope()
            func.acceptVisitor(this)
            constantVars[func] = declaredVars.filterKeys { !assignedVars.contains(it) }
        }

        newScope()
        node.main.acceptVisitor(this)
        constantVars[node.main] = declaredVars.filterKeys { !assignedVars.contains(it) }
    }

    override fun visitMain(node: MainNode) {
        node.body.acceptVisitor(this)
    }

    override fun visitFunction(node: FuncNode) {
        node.paramList.forEach {
            it.acceptVisitor(this)
        }
        node.body.acceptVisitor(this)
    }

    override fun visitDeclaration(node: DeclarationNode) {
        if (node.value is BaseLiteral){
            declaredVars[node.name.text] = node.value as BaseLiteral
        }
    }

    override fun visitAssignment(node: AssignmentNode) {
        if (node.name is IdentifierNode &&
            declaredVars.contains(node.name.name)) {
            assignedVars.add(node.name.name)
        }
    }

    override fun visitIf(node: IfNode) {
        node.falseStat.acceptVisitor(this)
        node.trueStat.acceptVisitor(this)
    }

    override fun visitBegin(node: BeginNode) {
        node.stat.acceptVisitor(this)
    }

    override fun visitSeq(node: SeqNode) {
        node.sequence.forEach{
            it.acceptVisitor(this)
        }
    }

    override fun visitWhile(node: WhileNode) {
        node.body.acceptVisitor(this)
    }

    override fun visitExit(node: ExitNode) {
    }

    override fun visitFuncCall(node: FuncCallNode) {
    }

    override fun visitParam(node: ParamNode) {

    }

    override fun visitNewPair(node: NewPairNode) {

    }


    override fun visitArgList(node: ArgListNode) {
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