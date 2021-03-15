package analyser.optimisations
//
//import analyser.exceptions.SemanticsException
//import tree.ASTVisitor
//import tree.nodes.ASTNode
//import tree.nodes.ProgNode
//import tree.nodes.assignment.AssignmentNode
//import tree.nodes.assignment.NewPairNode
//import tree.nodes.assignment.PairElemNode
//import tree.nodes.expr.*
//import tree.nodes.expr.operators.BinOpNode
//import tree.nodes.expr.operators.BinaryOperator
//import tree.nodes.expr.operators.UnOpNode
//import tree.nodes.expr.operators.UnaryOperator
//import tree.nodes.function.*
//import tree.nodes.statement.*
//
//object ConstantEvaluationVisitor : ASTVisitor {
//
//    private fun analyseExpression(expr: ExprNode): ExprNode {
//        return when (expr) {
//            is ArrayElement -> {
//                expr.arrIndices = expr.arrIndices.map { analyseExpression(it) }
//                expr
//            }
//            is ArrayLiteral -> {
//                expr.values = expr.values.map { analyseExpression(it) }
//                expr
//            }
//            is BinOpNode -> analyseBinOp(expr)
//            is UnOpNode -> analyseUnOp(expr)
//            else -> expr
//        }
//    }
//
//    private fun analyseBinOp(binExpr: BinOpNode): ExprNode {
//        val firstExprResult = analyseExpression(binExpr.firstExpr)
//        val secondExprResult = analyseExpression(binExpr.secondExpr)
//
//        if (firstExprResult !is Literal ||
//            secondExprResult !is Literal
//        ) {
//            binExpr.firstExpr = firstExprResult
//            binExpr.secondExpr = secondExprResult
//            return binExpr
//        }
//
//        if (firstExprResult is StringLiteral &&
//            secondExprResult is StringLiteral
//        ) {
//            var result = firstExprResult.value == secondExprResult.value
//            if (binExpr.operator == BinaryOperator.NEQ) {
//                result = !result
//            }
//            return BoolLiteral(
//                value = result,
//                ctx = binExpr.ctx
//            )
//        }
//
//        val firstLiteral = analyseLiteral(firstExprResult)
//        val secondLiteral = analyseLiteral(secondExprResult)
//
//        return when (binExpr.operator) {
//            BinaryOperator.PLUS -> IntLiteral(value = firstLiteral + secondLiteral, ctx = binExpr.ctx)
//            BinaryOperator.MINUS -> IntLiteral(value = firstLiteral - secondLiteral, ctx = binExpr.ctx)
//            BinaryOperator.MULTIPLY -> IntLiteral(value = firstLiteral * secondLiteral, ctx = binExpr.ctx)
//            BinaryOperator.DIVIDE -> {
//                if (secondLiteral != 0)
//                    return IntLiteral(value = firstLiteral / secondLiteral, ctx = binExpr.ctx)
//                binExpr.firstExpr = firstExprResult
//                binExpr.secondExpr = secondExprResult
//                return binExpr
//            }
//            BinaryOperator.MODULUS -> IntLiteral(value = firstLiteral % secondLiteral, ctx = binExpr.ctx)
//            BinaryOperator.GT -> BoolLiteral(value = firstLiteral > secondLiteral, ctx = binExpr.ctx)
//            BinaryOperator.LT -> BoolLiteral(value = firstLiteral < secondLiteral, ctx = binExpr.ctx)
//            BinaryOperator.GE -> BoolLiteral(value = firstLiteral >= secondLiteral, ctx = binExpr.ctx)
//            BinaryOperator.LE -> BoolLiteral(value = firstLiteral <= secondLiteral, ctx = binExpr.ctx)
//            BinaryOperator.EQ -> BoolLiteral(value = firstLiteral == secondLiteral, ctx = binExpr.ctx)
//            BinaryOperator.NEQ -> BoolLiteral(value = firstLiteral != secondLiteral, ctx = binExpr.ctx)
//            BinaryOperator.AND -> BoolLiteral(
//                value = firstLiteral == 1 && secondLiteral == 1,
//                ctx = binExpr.ctx
//            )
//            BinaryOperator.OR -> BoolLiteral(
//                value = firstLiteral == 1 || secondLiteral == 1,
//                ctx = binExpr.ctx
//            )
//        }
//    }
//
//    private fun analyseLiteral(expr: Literal): Int {
//        return when (expr) {
//            is IntLiteral -> expr.value
//            is CharLiteral -> expr.value.toInt()
//            is BoolLiteral -> expr.value.compareTo(false)
//            else -> throw SemanticsException("Invalid literal in binary expression", null)
//        }
//
//    }
//
//    private fun analyseUnOp(unExpr: UnOpNode): ExprNode {
//        val expression = analyseExpression(unExpr.expr)
//        when (unExpr.operator) {
//            UnaryOperator.MINUS -> {
//                if (expression is IntLiteral) {
//                    expression.value = -expression.value
//                    return expression
//                }
//            }
//            UnaryOperator.NEGATE -> {
//                if (expression is BoolLiteral) {
//                    expression.value = !expression.value
//                    return expression
//                }
//            }
//            UnaryOperator.CHR -> {
//                if (expression is CharLiteral) {
//                    return IntLiteral(
//                        value = expression.value.toInt(),
//                        ctx = expression.ctx
//                    )
//                }
//            }
//            UnaryOperator.ORD -> {
//                if (expression is IntLiteral) {
//                    return CharLiteral(
//                        value = expression.value.toChar(),
//                        ctx = expression.ctx
//                    )
//                }
//            }
//            else -> {
//            }
//        }
//        unExpr.expr = expression
//        return unExpr
//    }
//
//    override fun visitNode(node: ASTNode) {
//        node.acceptVisitor(this)
//    }
//
//    override fun visitProgram(node: ProgNode) {
//        node.functions.forEach {
//            it.acceptVisitor(this)
//        }
//        node.main.acceptVisitor(this)
//    }
//
//    override fun visitMain(node: MainNode) {
//        node.body.acceptVisitor(this)
//    }
//
//    override fun visitExit(node: ExitNode) {
//        node.expr = analyseExpression(node.expr)
//    }
//
//    override fun visitFunction(node: FuncNode) {
//        node.body.acceptVisitor(this)
//    }
//
//    override fun visitFuncCall(node: FuncCallNode) {
//        node.argList.acceptVisitor(this)
//    }
//
//    override fun visitParam(node: ParamNode) {
//
//    }
//
//    override fun visitNewPair(node: NewPairNode) {
//        node.firstElem = analyseExpression(node.firstElem)
//        node.secondElem = analyseExpression(node.secondElem)
//    }
//
//    override fun visitDeclaration(node: DeclarationNode) {
//        when (node.value) {
//            is ExprNode -> node.value = analyseExpression(node.value as ExprNode)
//            else -> node.value.acceptVisitor(this)
//        }
//    }
//
//    override fun visitArgList(node: ArgListNode) {
//        node.args = node.args.map { analyseExpression(it) }
//    }
//
//    override fun visitAssignment(node: AssignmentNode) {
//        when (node.value) {
//            is ExprNode -> node.value = analyseExpression(node.value as ExprNode)
//            else -> node.value.acceptVisitor(this)
//        }
//        node.name.acceptVisitor(this)
//
//    }
//
//    override fun visitBinOp(node: BinOpNode) {
//        node.firstExpr = analyseExpression(node.firstExpr)
//        node.secondExpr = analyseExpression(node.secondExpr)
//    }
//
//    override fun visitUnOp(node: UnOpNode) {
//        node.expr = analyseExpression(node.expr)
//    }
//
//    override fun visitPairElem(node: PairElemNode) {
//        node.expr = analyseExpression(node.expr)
//    }
//
//    override fun visitArrayElement(elem: ArrayElement) {
//        elem.arrIndices = elem.arrIndices.map { analyseExpression(it) }
//    }
//
//    override fun visitArrayLiteral(literal: ArrayLiteral) {
//        literal.values = literal.values.map { analyseExpression(it) }
//    }
//
//    override fun visitBoolLiteral(literal: BoolLiteral) {
//
//    }
//
//    override fun visitCharLiteral(literal: CharLiteral) {
//
//    }
//
//    override fun visitIdentifier(node: IdentifierNode) {
//
//    }
//
//    override fun visitIntLiteral(literal: IntLiteral) {
//
//    }
//
//    override fun visitPairLiteral(literal: PairLiteral) {
//
//    }
//
//    override fun visitDeepArrayLiteral(node: DeepArrayLiteral) {
//    }
//
//    override fun visitPairMemoryLiteral(node: PairMemoryLiteral) {
//        TODO("Not yet implemented")
//    }
//
//    override fun visitStringLiteral(literal: StringLiteral) {
//
//    }
//
//    override fun visitBegin(node: BeginNode) {
//        node.stat.acceptVisitor(this)
//    }
//
//    override fun visitFree(node: FreeNode) {
//        node.value = analyseExpression(node.value)
//    }
//
//    override fun visitIf(node: IfNode) {
//        node.proposition = analyseExpression(node.proposition)
//        node.trueStat.acceptVisitor(this)
//        node.falseStat.acceptVisitor(this)
//    }
//
//    override fun visitPrint(node: PrintNode) {
//        node.value = analyseExpression(node.value)
//    }
//
//    override fun visitRead(node: ReadNode) {
//        node.value.acceptVisitor(this)
//    }
//
//    override fun visitReturn(node: ReturnNode) {
//        node.value = analyseExpression(node.value)
//    }
//
//    override fun visitSeq(node: SeqNode) {
//        node.sequence.forEach {
//            it.acceptVisitor(this)
//        }
//    }
//
//    override fun visitWhile(node: WhileNode) {
//        node.proposition = analyseExpression(node.proposition)
//        node.body.acceptVisitor(this)
//    }
//}