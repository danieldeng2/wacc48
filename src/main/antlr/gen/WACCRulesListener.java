// Generated from /home/daniel/Projects/wacc_48/src/main/antlr/WACCRules.g4 by ANTLR 4.9.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link WACCRules}.
 */
public interface WACCRulesListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link WACCRules#func}.
	 * @param ctx the parse tree
	 */
	void enterFunc(WACCRules.FuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCRules#func}.
	 * @param ctx the parse tree
	 */
	void exitFunc(WACCRules.FuncContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCRules#paramList}.
	 * @param ctx the parse tree
	 */
	void enterParamList(WACCRules.ParamListContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCRules#paramList}.
	 * @param ctx the parse tree
	 */
	void exitParamList(WACCRules.ParamListContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCRules#param}.
	 * @param ctx the parse tree
	 */
	void enterParam(WACCRules.ParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCRules#param}.
	 * @param ctx the parse tree
	 */
	void exitParam(WACCRules.ParamContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ifStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 */
	void enterIfStat(WACCRules.IfStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ifStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 */
	void exitIfStat(WACCRules.IfStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code readStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 */
	void enterReadStat(WACCRules.ReadStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code readStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 */
	void exitReadStat(WACCRules.ReadStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code printStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 */
	void enterPrintStat(WACCRules.PrintStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code printStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 */
	void exitPrintStat(WACCRules.PrintStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code seqCompositionStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 */
	void enterSeqCompositionStat(WACCRules.SeqCompositionStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code seqCompositionStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 */
	void exitSeqCompositionStat(WACCRules.SeqCompositionStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignmentStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentStat(WACCRules.AssignmentStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignmentStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentStat(WACCRules.AssignmentStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code printlnStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 */
	void enterPrintlnStat(WACCRules.PrintlnStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code printlnStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 */
	void exitPrintlnStat(WACCRules.PrintlnStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exitStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 */
	void enterExitStat(WACCRules.ExitStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exitStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 */
	void exitExitStat(WACCRules.ExitStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code freeStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 */
	void enterFreeStat(WACCRules.FreeStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code freeStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 */
	void exitFreeStat(WACCRules.FreeStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code beginStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 */
	void enterBeginStat(WACCRules.BeginStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code beginStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 */
	void exitBeginStat(WACCRules.BeginStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code skipStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 */
	void enterSkipStat(WACCRules.SkipStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code skipStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 */
	void exitSkipStat(WACCRules.SkipStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code returnStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 */
	void enterReturnStat(WACCRules.ReturnStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code returnStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 */
	void exitReturnStat(WACCRules.ReturnStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code declarationStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 */
	void enterDeclarationStat(WACCRules.DeclarationStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code declarationStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 */
	void exitDeclarationStat(WACCRules.DeclarationStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code whileStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 */
	void enterWhileStat(WACCRules.WhileStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code whileStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 */
	void exitWhileStat(WACCRules.WhileStatContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCRules#assignLhs}.
	 * @param ctx the parse tree
	 */
	void enterAssignLhs(WACCRules.AssignLhsContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCRules#assignLhs}.
	 * @param ctx the parse tree
	 */
	void exitAssignLhs(WACCRules.AssignLhsContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCRules#assignRhs}.
	 * @param ctx the parse tree
	 */
	void enterAssignRhs(WACCRules.AssignRhsContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCRules#assignRhs}.
	 * @param ctx the parse tree
	 */
	void exitAssignRhs(WACCRules.AssignRhsContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCRules#argList}.
	 * @param ctx the parse tree
	 */
	void enterArgList(WACCRules.ArgListContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCRules#argList}.
	 * @param ctx the parse tree
	 */
	void exitArgList(WACCRules.ArgListContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCRules#type}.
	 * @param ctx the parse tree
	 */
	void enterType(WACCRules.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCRules#type}.
	 * @param ctx the parse tree
	 */
	void exitType(WACCRules.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCRules#baseType}.
	 * @param ctx the parse tree
	 */
	void enterBaseType(WACCRules.BaseTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCRules#baseType}.
	 * @param ctx the parse tree
	 */
	void exitBaseType(WACCRules.BaseTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCRules#pairType}.
	 * @param ctx the parse tree
	 */
	void enterPairType(WACCRules.PairTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCRules#pairType}.
	 * @param ctx the parse tree
	 */
	void exitPairType(WACCRules.PairTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCRules#pairElemType}.
	 * @param ctx the parse tree
	 */
	void enterPairElemType(WACCRules.PairElemTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCRules#pairElemType}.
	 * @param ctx the parse tree
	 */
	void exitPairElemType(WACCRules.PairElemTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code strLiteral}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 */
	void enterStrLiteral(WACCRules.StrLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code strLiteral}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 */
	void exitStrLiteral(WACCRules.StrLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code identifier}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(WACCRules.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code identifier}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(WACCRules.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unaryOpExpr}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOpExpr(WACCRules.UnaryOpExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unaryOpExpr}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOpExpr(WACCRules.UnaryOpExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code pairLiteral}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 */
	void enterPairLiteral(WACCRules.PairLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code pairLiteral}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 */
	void exitPairLiteral(WACCRules.PairLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intLiteral}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 */
	void enterIntLiteral(WACCRules.IntLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intLiteral}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 */
	void exitIntLiteral(WACCRules.IntLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binOpExpr}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 */
	void enterBinOpExpr(WACCRules.BinOpExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binOpExpr}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 */
	void exitBinOpExpr(WACCRules.BinOpExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code charLiteral}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 */
	void enterCharLiteral(WACCRules.CharLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code charLiteral}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 */
	void exitCharLiteral(WACCRules.CharLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayElemExpr}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 */
	void enterArrayElemExpr(WACCRules.ArrayElemExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayElemExpr}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 */
	void exitArrayElemExpr(WACCRules.ArrayElemExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code boolLiteral}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 */
	void enterBoolLiteral(WACCRules.BoolLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code boolLiteral}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 */
	void exitBoolLiteral(WACCRules.BoolLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bracketedExpr}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 */
	void enterBracketedExpr(WACCRules.BracketedExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bracketedExpr}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 */
	void exitBracketedExpr(WACCRules.BracketedExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCRules#unaryOperator}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOperator(WACCRules.UnaryOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCRules#unaryOperator}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOperator(WACCRules.UnaryOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCRules#funcCall}.
	 * @param ctx the parse tree
	 */
	void enterFuncCall(WACCRules.FuncCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCRules#funcCall}.
	 * @param ctx the parse tree
	 */
	void exitFuncCall(WACCRules.FuncCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCRules#newPair}.
	 * @param ctx the parse tree
	 */
	void enterNewPair(WACCRules.NewPairContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCRules#newPair}.
	 * @param ctx the parse tree
	 */
	void exitNewPair(WACCRules.NewPairContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCRules#pairElem}.
	 * @param ctx the parse tree
	 */
	void enterPairElem(WACCRules.PairElemContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCRules#pairElem}.
	 * @param ctx the parse tree
	 */
	void exitPairElem(WACCRules.PairElemContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCRules#arrayLiter}.
	 * @param ctx the parse tree
	 */
	void enterArrayLiter(WACCRules.ArrayLiterContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCRules#arrayLiter}.
	 * @param ctx the parse tree
	 */
	void exitArrayLiter(WACCRules.ArrayLiterContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCRules#arrayElem}.
	 * @param ctx the parse tree
	 */
	void enterArrayElem(WACCRules.ArrayElemContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCRules#arrayElem}.
	 * @param ctx the parse tree
	 */
	void exitArrayElem(WACCRules.ArrayElemContext ctx);
}