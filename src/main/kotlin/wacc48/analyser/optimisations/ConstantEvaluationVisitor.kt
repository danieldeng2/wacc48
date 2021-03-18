package wacc48.analyser.optimisations

import wacc48.tree.ASTBaseVisitor
import wacc48.tree.nodes.ASTNode
import wacc48.tree.nodes.assignment.AssignmentNode
import wacc48.tree.nodes.assignment.NewPairNode
import wacc48.tree.nodes.assignment.PairElemNode
import wacc48.tree.nodes.expr.*
import wacc48.tree.nodes.expr.operators.BinOpNode
import wacc48.tree.nodes.expr.operators.UnOpNode
import wacc48.tree.nodes.expr.operators.operation.binary.*
import wacc48.tree.nodes.expr.operators.operation.unary.*
import wacc48.tree.nodes.function.ArgListNode
import wacc48.tree.nodes.statement.*

object ConstantEvaluationVisitor : ASTBaseVisitor<Unit>() {

    var optimisations = 0

    fun optimise(node: ASTNode): Int {
        optimisations = 0
        visitNode(node)
        return optimisations
    }

    override fun defaultResult() {}

    private fun analyseExpression(expr: ExprNode): ExprNode {
        return when (expr) {
            is ArrayElement -> {
                expr.arrIndices = expr.arrIndices.map { analyseExpression(it) }
                expr
            }
            is ArrayLiteral -> {
                expr.values = expr.values.map { analyseExpression(it) }
                expr
            }
            is BinOpNode -> analyseBinOp(expr)
            is UnOpNode -> analyseUnOp(expr)
            else -> expr
        }
    }

    private fun analyseBinOp(binExpr: BinOpNode): ExprNode {
        val firstExprResult = analyseExpression(binExpr.firstExpr)
        val secondExprResult = analyseExpression(binExpr.secondExpr)

        if (firstExprResult !is BaseLiteral || secondExprResult !is BaseLiteral) {
            return unOptimiseExpression(
                binExpr,
                firstExprResult,
                secondExprResult
            )
        }

        if (firstExprResult is StringLiteral && secondExprResult is StringLiteral) {
            return analyseStringOp(firstExprResult, secondExprResult, binExpr)
        }

        val firstLiteral = analyseBaseLiteral(firstExprResult)
        val secondLiteral = analyseBaseLiteral(secondExprResult)

        if (firstLiteral !is Int || secondLiteral !is Int) {
            return unOptimiseExpression(
                binExpr,
                firstExprResult,
                secondExprResult
            )
        }

        return optimiseBinOp(
            binExpr,
            firstLiteral,
            secondLiteral,
            firstExprResult,
            secondExprResult
        )
    }

    private fun optimiseBinOp(
        binExpr: BinOpNode,
        firstLiteral: Int,
        secondLiteral: Int,
        firstExprResult: ExprNode,
        secondExprResult: ExprNode
    ): ExprNode {
        optimisations++
        return when (binExpr.operator) {
            BinaryOperator.PLUS -> IntLiteral(
                value = firstLiteral + secondLiteral,
                ctx = binExpr.ctx,
                isOutOfBounds = outOfBoundsError(firstLiteral.toLong() + secondLiteral.toLong())
            )
            BinaryOperator.MINUS -> IntLiteral(
                value = firstLiteral - secondLiteral,
                ctx = binExpr.ctx,
                isOutOfBounds = outOfBoundsError(firstLiteral.toLong() - secondLiteral.toLong())
            )
            BinaryOperator.MULTIPLY -> IntLiteral(
                value = firstLiteral * secondLiteral,
                ctx = binExpr.ctx,
                isOutOfBounds = outOfBoundsError(firstLiteral.toLong() * secondLiteral.toLong())
            )
            BinaryOperator.DIVIDE -> {
                if (secondLiteral == 0) {
                    optimisations--
                    return unOptimiseExpression(
                        binExpr,
                        firstExprResult,
                        secondExprResult
                    )
                } else {
                    IntLiteral(
                        value = firstLiteral / secondLiteral,
                        ctx = binExpr.ctx,
                        isOutOfBounds = outOfBoundsError(firstLiteral.toLong() / secondLiteral.toLong())
                    )
                }
            }
            BinaryOperator.MODULUS -> {
                if (secondLiteral == 0) {
                    optimisations--
                    return unOptimiseExpression(
                        binExpr,
                        firstExprResult,
                        secondExprResult
                    )
                } else {
                    IntLiteral(
                        value = firstLiteral % secondLiteral,
                        ctx = binExpr.ctx,
                        isOutOfBounds = outOfBoundsError((firstLiteral.toLong() % secondLiteral.toLong()))
                    )
                }
            }
            BinaryOperator.GT -> BoolLiteral(
                value = firstLiteral > secondLiteral,
                ctx = binExpr.ctx
            )
            BinaryOperator.LT -> BoolLiteral(
                value = firstLiteral < secondLiteral,
                ctx = binExpr.ctx
            )
            BinaryOperator.GE -> BoolLiteral(
                value = firstLiteral >= secondLiteral,
                ctx = binExpr.ctx
            )
            BinaryOperator.LE -> BoolLiteral(
                value = firstLiteral <= secondLiteral,
                ctx = binExpr.ctx
            )
            BinaryOperator.EQ -> BoolLiteral(
                value = firstLiteral == secondLiteral,
                ctx = binExpr.ctx
            )
            BinaryOperator.NEQ -> BoolLiteral(
                value = firstLiteral != secondLiteral,
                ctx = binExpr.ctx
            )
            BinaryOperator.AND -> BoolLiteral(
                value = firstLiteral == 1 && secondLiteral == 1,
                ctx = binExpr.ctx
            )
            BinaryOperator.OR -> BoolLiteral(
                value = firstLiteral == 1 || secondLiteral == 1,
                ctx = binExpr.ctx
            )
        }
    }

    private fun unOptimiseExpression(
        binExpr: BinOpNode,
        firstExprResult: ExprNode,
        secondExprResult: ExprNode
    ): BinOpNode {
        binExpr.firstExpr = firstExprResult
        binExpr.secondExpr = secondExprResult
        return binExpr
    }

    private fun analyseStringOp(
        firstExprResult: StringLiteral,
        secondExprResult: StringLiteral,
        binExpr: BinOpNode
    ): BoolLiteral {
        optimisations++
        var result = firstExprResult.value == secondExprResult.value
        if (binExpr.operator == BinaryOperator.NEQ) {
            result = !result
        }
        return BoolLiteral(
            value = result,
            ctx = binExpr.ctx
        )
    }

    private fun analyseBaseLiteral(expr: BaseLiteral): Int? {
        return when (expr) {
            is IntLiteral -> expr.value
            is CharLiteral -> expr.value.toInt()
            is BoolLiteral -> expr.value.compareTo(false)
            else -> null
        }

    }

    private fun analyseUnOp(unExpr: UnOpNode): ExprNode {
        val expression = analyseExpression(unExpr.expr)
        when (unExpr.operator) {
            UnaryOperator.MINUS -> {
                if (expression is IntLiteral) {
                    optimisations++
                    return IntLiteral(
                        value = -expression.value,
                        ctx = expression.ctx,
                        isOutOfBounds = outOfBoundsError((-(expression.value.toLong())))
                    )
                }
            }
            UnaryOperator.NEGATE -> {
                if (expression is BoolLiteral) {
                    optimisations++
                    expression.value = !expression.value
                    return expression
                }
            }
            UnaryOperator.CHR -> {
                if (expression is CharLiteral) {
                    optimisations++
                    return IntLiteral(
                        value = expression.value.toInt(),
                        ctx = expression.ctx,
                        isOutOfBounds = false
                    )
                }
            }
            UnaryOperator.ORD -> {
                if (expression is IntLiteral) {
                    optimisations++
                    return CharLiteral(
                        value = expression.value.toChar(),
                        ctx = expression.ctx
                    )
                }
            }
            else -> {
            }
        }
        unExpr.expr = expression
        return unExpr
    }

    override fun visitExit(node: ExitNode) {
        node.expr = analyseExpression(node.expr)
    }

    override fun visitNewPair(node: NewPairNode) {
        node.firstElem = analyseExpression(node.firstElem)
        node.secondElem = analyseExpression(node.secondElem)
    }

    override fun visitDeclaration(node: DeclarationNode) {
        when (node.value) {
            is ExprNode -> node.value =
                analyseExpression(node.value as ExprNode)
            else -> visitNode(node.value)
        }
    }

    override fun visitArgList(node: ArgListNode) {
        node.args = node.args.map { analyseExpression(it) }
    }

    override fun visitAssignment(node: AssignmentNode) {
        when (node.value) {
            is ExprNode -> node.value =
                analyseExpression(node.value as ExprNode)
            else -> visitNode(node.value)
        }
        visitNode(node.name)
    }

    override fun visitBinOp(node: BinOpNode) {
        node.firstExpr = analyseExpression(node.firstExpr)
        node.secondExpr = analyseExpression(node.secondExpr)
    }

    override fun visitUnOp(node: UnOpNode) {
        node.expr = analyseExpression(node.expr)
    }

    override fun visitPairElem(node: PairElemNode) {
        node.expr = analyseExpression(node.expr)
    }

    override fun visitArrayElement(elem: ArrayElement) {
        elem.arrIndices = elem.arrIndices.map { analyseExpression(it) }
    }

    override fun visitArrayLiteral(literal: ArrayLiteral) {
        literal.values = literal.values.map { analyseExpression(it) }
    }

    override fun visitIf(node: IfNode) {
        node.proposition = analyseExpression(node.proposition)
        visitNode(node.trueStat)
        visitNode(node.falseStat)
    }

    override fun visitPrint(node: PrintNode) {
        node.value = analyseExpression(node.value)
    }

    override fun visitRead(node: ReadNode) {
        visitNode(node.value)
    }

    override fun visitReturn(node: ReturnNode) {
        node.value = analyseExpression(node.value)
    }

    override fun visitWhile(node: WhileNode) {
        node.proposition = analyseExpression(node.proposition)
        visitNode(node.body)
    }

    override fun visitFree(node: FreeNode) {
        node.value = analyseExpression(node.value)
    }

    private fun outOfBoundsError(number: Long): Boolean {
        return number > Integer.MAX_VALUE || number < Integer.MIN_VALUE
    }


}