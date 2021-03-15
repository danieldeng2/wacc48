// Generated from /home/daniel/Projects/wacc_48/src/main/antlr/WACCShellParser.g4 by ANTLR 4.9.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse wacc48.tree produced by
 * {@link WACCShellParser}.
 */
public interface WACCShellParserListener extends ParseTreeListener {
	/**
	 * Enter a parse wacc48.tree produced by {@link WACCShellParser#command}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterCommand(WACCShellParser.CommandContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by {@link WACCShellParser#command}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitCommand(WACCShellParser.CommandContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by {@link WACCShellParser#func}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterFunc(WACCShellParser.FuncContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by {@link WACCShellParser#func}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitFunc(WACCShellParser.FuncContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by {@link WACCShellParser#paramList}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterParamList(WACCShellParser.ParamListContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by {@link WACCShellParser#paramList}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitParamList(WACCShellParser.ParamListContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by {@link WACCShellParser#param}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterParam(WACCShellParser.ParamContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by {@link WACCShellParser#param}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitParam(WACCShellParser.ParamContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by the {@code ifStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterIfStat(WACCShellParser.IfStatContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by the {@code ifStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitIfStat(WACCShellParser.IfStatContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by the {@code readStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterReadStat(WACCShellParser.ReadStatContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by the {@code readStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitReadStat(WACCShellParser.ReadStatContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by the {@code printStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterPrintStat(WACCShellParser.PrintStatContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by the {@code printStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitPrintStat(WACCShellParser.PrintStatContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by the {@code seqCompositionStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterSeqCompositionStat(WACCShellParser.SeqCompositionStatContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by the {@code seqCompositionStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitSeqCompositionStat(WACCShellParser.SeqCompositionStatContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by the {@code assignmentStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterAssignmentStat(WACCShellParser.AssignmentStatContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by the {@code assignmentStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitAssignmentStat(WACCShellParser.AssignmentStatContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by the {@code printlnStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterPrintlnStat(WACCShellParser.PrintlnStatContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by the {@code printlnStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitPrintlnStat(WACCShellParser.PrintlnStatContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by the {@code exitStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterExitStat(WACCShellParser.ExitStatContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by the {@code exitStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitExitStat(WACCShellParser.ExitStatContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by the {@code freeStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterFreeStat(WACCShellParser.FreeStatContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by the {@code freeStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitFreeStat(WACCShellParser.FreeStatContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by the {@code beginStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterBeginStat(WACCShellParser.BeginStatContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by the {@code beginStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitBeginStat(WACCShellParser.BeginStatContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by the {@code skipStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterSkipStat(WACCShellParser.SkipStatContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by the {@code skipStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitSkipStat(WACCShellParser.SkipStatContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by the {@code returnStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterReturnStat(WACCShellParser.ReturnStatContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by the {@code returnStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitReturnStat(WACCShellParser.ReturnStatContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by the {@code declarationStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterDeclarationStat(WACCShellParser.DeclarationStatContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by the {@code declarationStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitDeclarationStat(WACCShellParser.DeclarationStatContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by the {@code whileStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterWhileStat(WACCShellParser.WhileStatContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by the {@code whileStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitWhileStat(WACCShellParser.WhileStatContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by {@link WACCShellParser#assignLhs}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterAssignLhs(WACCShellParser.AssignLhsContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by {@link WACCShellParser#assignLhs}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitAssignLhs(WACCShellParser.AssignLhsContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by {@link WACCShellParser#assignRhs}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterAssignRhs(WACCShellParser.AssignRhsContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by {@link WACCShellParser#assignRhs}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitAssignRhs(WACCShellParser.AssignRhsContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by {@link WACCShellParser#argList}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterArgList(WACCShellParser.ArgListContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by {@link WACCShellParser#argList}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitArgList(WACCShellParser.ArgListContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by {@link WACCShellParser#type}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterType(WACCShellParser.TypeContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by {@link WACCShellParser#type}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitType(WACCShellParser.TypeContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by {@link WACCShellParser#baseType}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterBaseType(WACCShellParser.BaseTypeContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by {@link WACCShellParser#baseType}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitBaseType(WACCShellParser.BaseTypeContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by {@link WACCShellParser#pairType}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterPairType(WACCShellParser.PairTypeContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by {@link WACCShellParser#pairType}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitPairType(WACCShellParser.PairTypeContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by {@link WACCShellParser#pairElemType}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterPairElemType(WACCShellParser.PairElemTypeContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by {@link WACCShellParser#pairElemType}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitPairElemType(WACCShellParser.PairElemTypeContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by the {@code strLiteral}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterStrLiteral(WACCShellParser.StrLiteralContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by the {@code strLiteral}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitStrLiteral(WACCShellParser.StrLiteralContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by the {@code identifier}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterIdentifier(WACCShellParser.IdentifierContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by the {@code identifier}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitIdentifier(WACCShellParser.IdentifierContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by the {@code unaryOpExpr}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterUnaryOpExpr(WACCShellParser.UnaryOpExprContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by the {@code unaryOpExpr}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitUnaryOpExpr(WACCShellParser.UnaryOpExprContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by the {@code pairLiteral}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterPairLiteral(WACCShellParser.PairLiteralContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by the {@code pairLiteral}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitPairLiteral(WACCShellParser.PairLiteralContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by the {@code intLiteral}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterIntLiteral(WACCShellParser.IntLiteralContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by the {@code intLiteral}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitIntLiteral(WACCShellParser.IntLiteralContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by the {@code binOpExpr}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterBinOpExpr(WACCShellParser.BinOpExprContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by the {@code binOpExpr}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitBinOpExpr(WACCShellParser.BinOpExprContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by the {@code charLiteral}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterCharLiteral(WACCShellParser.CharLiteralContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by the {@code charLiteral}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitCharLiteral(WACCShellParser.CharLiteralContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by the {@code arrayElemExpr}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterArrayElemExpr(WACCShellParser.ArrayElemExprContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by the {@code arrayElemExpr}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitArrayElemExpr(WACCShellParser.ArrayElemExprContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by the {@code boolLiteral}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterBoolLiteral(WACCShellParser.BoolLiteralContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by the {@code boolLiteral}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitBoolLiteral(WACCShellParser.BoolLiteralContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by the {@code bracketedExpr}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterBracketedExpr(WACCShellParser.BracketedExprContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by the {@code bracketedExpr}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitBracketedExpr(WACCShellParser.BracketedExprContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by {@link WACCShellParser#unaryOperator}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterUnaryOperator(WACCShellParser.UnaryOperatorContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by {@link WACCShellParser#unaryOperator}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitUnaryOperator(WACCShellParser.UnaryOperatorContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by {@link WACCShellParser#funcCall}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterFuncCall(WACCShellParser.FuncCallContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by {@link WACCShellParser#funcCall}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitFuncCall(WACCShellParser.FuncCallContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by {@link WACCShellParser#newPair}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterNewPair(WACCShellParser.NewPairContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by {@link WACCShellParser#newPair}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitNewPair(WACCShellParser.NewPairContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by {@link WACCShellParser#pairElem}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterPairElem(WACCShellParser.PairElemContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by {@link WACCShellParser#pairElem}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitPairElem(WACCShellParser.PairElemContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by {@link WACCShellParser#arrayLiter}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterArrayLiter(WACCShellParser.ArrayLiterContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by {@link WACCShellParser#arrayLiter}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitArrayLiter(WACCShellParser.ArrayLiterContext ctx);
	/**
	 * Enter a parse wacc48.tree produced by {@link WACCShellParser#arrayElem}.
	 * @param ctx the parse wacc48.tree
	 */
	void enterArrayElem(WACCShellParser.ArrayElemContext ctx);
	/**
	 * Exit a parse wacc48.tree produced by {@link WACCShellParser#arrayElem}.
	 * @param ctx the parse wacc48.tree
	 */
	void exitArrayElem(WACCShellParser.ArrayElemContext ctx);
}