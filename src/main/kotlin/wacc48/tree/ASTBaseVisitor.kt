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

abstract class ASTBaseVisitor<T> : ASTVisitor<T> {

    override fun visitNode(node: ASTNode): T {
        return node.acceptVisitor(this)
    }

    override fun visitChildren(node: ASTNode): T {
        var res: T? = null
        for (child in node.children) {
            res = visitNode(child)
        }
        return when (res) {
            null -> defaultResult()
            else -> res
        }
    }

    override fun visitProgram(node: ProgNode): T {
        node.functions.forEach { visitNode(it) }
        visitNode(node.main)
        return defaultResult()
    }

    override fun visitMain(node: MainNode): T {
        visitNode(node.body)
        return defaultResult()
    }

    override fun visitExit(node: ExitNode): T {
        visitNode(node.expr)
        return defaultResult()
    }

    override fun visitFunction(node: FuncNode): T {
        node.paramList.forEach { visitNode(it) }
        visitNode(node.body)
        return defaultResult()
    }

    override fun visitFuncCall(node: FuncCallNode): T {
        visitNode(node.argList)
        return defaultResult()
    }

    override fun visitParam(node: ParamNode): T {
        return defaultResult()
    }

    override fun visitNewPair(node: NewPairNode): T {
        visitNode(node.firstElem)
        visitNode(node.secondElem)
        return defaultResult()
    }

    override fun visitDeclaration(node: DeclarationNode): T {
        visitNode(node.value)
        visitNode(node.name)
        return defaultResult()
    }

    override fun visitArgList(node: ArgListNode): T {
        node.args.forEach { visitNode(it) }
        return defaultResult()
    }

    override fun visitAssignment(node: AssignmentNode): T {
        visitNode(node.value)
        visitNode(node.name)
        return defaultResult()
    }

    override fun visitBinOp(node: BinOpNode): T {
        visitNode(node.firstExpr)
        visitNode(node.secondExpr)
        return defaultResult()
    }

    override fun visitUnOp(node: UnOpNode): T {
        visitNode(node.expr)
        return defaultResult()
    }

    override fun visitPairElem(node: PairElemNode): T {
        visitNode(node.expr)
        return defaultResult()
    }

    override fun visitArrayElement(elem: ArrayElement): T {
        elem.arrIndices.forEach { visitNode(it) }
        return defaultResult()
    }

    override fun visitArrayLiteral(literal: ArrayLiteral): T {
        literal.values.forEach { visitNode(it) }
        return defaultResult()
    }

    override fun visitBoolLiteral(literal: BoolLiteral): T {
        return defaultResult()
    }

    override fun visitCharLiteral(literal: CharLiteral): T {
        return defaultResult()
    }

    override fun visitIntLiteral(literal: IntLiteral): T {
        return defaultResult()
    }

    override fun visitPairLiteral(literal: PairLiteral): T {
        return defaultResult()
    }

    override fun visitDeepArrayLiteral(node: DeepArrayLiteral): T {
        return defaultResult()
    }

    override fun visitPairMemoryLiteral(node: PairMemoryLiteral): T {
        return defaultResult()
    }

    override fun visitStringLiteral(literal: StringLiteral): T {
        return defaultResult()
    }

    override fun visitIdentifier(node: IdentifierNode): T {
        return defaultResult()
    }

    override fun visitBegin(node: BeginNode): T {
        visitNode(node.stat)
        return defaultResult()
    }

    override fun visitFree(node: FreeNode): T {
        visitNode(node.value)
        return defaultResult()
    }

    override fun visitIf(node: IfNode): T {
        visitNode(node.proposition)
        visitNode(node.falseStat)
        visitNode(node.trueStat)
        return defaultResult()
    }

    override fun visitPrint(node: PrintNode): T {
        visitNode(node.value)
        return defaultResult()
    }

    override fun visitRead(node: ReadNode): T {
        visitNode(node.value)
        return defaultResult()
    }

    override fun visitReturn(node: ReturnNode): T {
        visitNode(node.value)
        return defaultResult()
    }

    override fun visitSeq(node: SeqNode): T {
        node.sequence.forEach { visitNode(it) }
        return defaultResult()
    }

    override fun visitWhile(node: WhileNode): T {
        visitNode(node.proposition)
        visitNode(node.body)
        return defaultResult()
    }

}