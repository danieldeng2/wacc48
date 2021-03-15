// Generated from /home/daniel/Projects/wacc_48/src/main/antlr/WACCParser.g4 by ANTLR 4.9.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link WACCParser}.
 */
public interface WACCParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link WACCParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(WACCParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(WACCParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCParser#func}.
	 * @param ctx the parse tree
	 */
	void enterFunc(WACCParser.FuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCParser#func}.
	 * @param ctx the parse tree
	 */
	void exitFunc(WACCParser.FuncContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCParser#paramList}.
	 * @param ctx the parse tree
	 */
	void enterParamList(WACCParser.ParamListContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCParser#paramList}.
	 * @param ctx the parse tree
	 */
	void exitParamList(WACCParser.ParamListContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCParser#param}.
	 * @param ctx the parse tree
	 */
	void enterParam(WACCParser.ParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCParser#param}.
	 * @param ctx the parse tree
	 */
	void exitParam(WACCParser.ParamContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ifStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterIfStat(WACCParser.IfStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ifStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitIfStat(WACCParser.IfStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code readStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterReadStat(WACCParser.ReadStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code readStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitReadStat(WACCParser.ReadStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code printStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterPrintStat(WACCParser.PrintStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code printStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitPrintStat(WACCParser.PrintStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code seqCompositionStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterSeqCompositionStat(WACCParser.SeqCompositionStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code seqCompositionStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitSeqCompositionStat(WACCParser.SeqCompositionStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignmentStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentStat(WACCParser.AssignmentStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignmentStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentStat(WACCParser.AssignmentStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code printlnStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterPrintlnStat(WACCParser.PrintlnStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code printlnStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitPrintlnStat(WACCParser.PrintlnStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exitStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterExitStat(WACCParser.ExitStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exitStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitExitStat(WACCParser.ExitStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code freeStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterFreeStat(WACCParser.FreeStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code freeStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitFreeStat(WACCParser.FreeStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code beginStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterBeginStat(WACCParser.BeginStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code beginStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitBeginStat(WACCParser.BeginStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code skipStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterSkipStat(WACCParser.SkipStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code skipStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitSkipStat(WACCParser.SkipStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code returnStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterReturnStat(WACCParser.ReturnStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code returnStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitReturnStat(WACCParser.ReturnStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code declarationStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterDeclarationStat(WACCParser.DeclarationStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code declarationStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitDeclarationStat(WACCParser.DeclarationStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code whileStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterWhileStat(WACCParser.WhileStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code whileStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitWhileStat(WACCParser.WhileStatContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCParser#assignLhs}.
	 * @param ctx the parse tree
	 */
	void enterAssignLhs(WACCParser.AssignLhsContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCParser#assignLhs}.
	 * @param ctx the parse tree
	 */
	void exitAssignLhs(WACCParser.AssignLhsContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCParser#assignRhs}.
	 * @param ctx the parse tree
	 */
	void enterAssignRhs(WACCParser.AssignRhsContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCParser#assignRhs}.
	 * @param ctx the parse tree
	 */
	void exitAssignRhs(WACCParser.AssignRhsContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCParser#argList}.
	 * @param ctx the parse tree
	 */
	void enterArgList(WACCParser.ArgListContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCParser#argList}.
	 * @param ctx the parse tree
	 */
	void exitArgList(WACCParser.ArgListContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(WACCParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(WACCParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCParser#baseType}.
	 * @param ctx the parse tree
	 */
	void enterBaseType(WACCParser.BaseTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCParser#baseType}.
	 * @param ctx the parse tree
	 */
	void exitBaseType(WACCParser.BaseTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCParser#pairType}.
	 * @param ctx the parse tree
	 */
	void enterPairType(WACCParser.PairTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCParser#pairType}.
	 * @param ctx the parse tree
	 */
	void exitPairType(WACCParser.PairTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCParser#pairElemType}.
	 * @param ctx the parse tree
	 */
	void enterPairElemType(WACCParser.PairElemTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCParser#pairElemType}.
	 * @param ctx the parse tree
	 */
	void exitPairElemType(WACCParser.PairElemTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code strLiteral}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterStrLiteral(WACCParser.StrLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code strLiteral}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitStrLiteral(WACCParser.StrLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code identifier}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(WACCParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code identifier}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(WACCParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unaryOpExpr}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOpExpr(WACCParser.UnaryOpExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unaryOpExpr}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOpExpr(WACCParser.UnaryOpExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code pairLiteral}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterPairLiteral(WACCParser.PairLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code pairLiteral}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitPairLiteral(WACCParser.PairLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intLiteral}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIntLiteral(WACCParser.IntLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intLiteral}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIntLiteral(WACCParser.IntLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binOpExpr}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBinOpExpr(WACCParser.BinOpExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binOpExpr}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBinOpExpr(WACCParser.BinOpExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code charLiteral}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCharLiteral(WACCParser.CharLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code charLiteral}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCharLiteral(WACCParser.CharLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayElemExpr}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterArrayElemExpr(WACCParser.ArrayElemExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayElemExpr}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitArrayElemExpr(WACCParser.ArrayElemExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code boolLiteral}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBoolLiteral(WACCParser.BoolLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code boolLiteral}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBoolLiteral(WACCParser.BoolLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bracketedExpr}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBracketedExpr(WACCParser.BracketedExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bracketedExpr}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBracketedExpr(WACCParser.BracketedExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCParser#unaryOperator}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOperator(WACCParser.UnaryOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCParser#unaryOperator}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOperator(WACCParser.UnaryOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCParser#funcCall}.
	 * @param ctx the parse tree
	 */
	void enterFuncCall(WACCParser.FuncCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCParser#funcCall}.
	 * @param ctx the parse tree
	 */
	void exitFuncCall(WACCParser.FuncCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCParser#newPair}.
	 * @param ctx the parse tree
	 */
	void enterNewPair(WACCParser.NewPairContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCParser#newPair}.
	 * @param ctx the parse tree
	 */
	void exitNewPair(WACCParser.NewPairContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCParser#pairElem}.
	 * @param ctx the parse tree
	 */
	void enterPairElem(WACCParser.PairElemContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCParser#pairElem}.
	 * @param ctx the parse tree
	 */
	void exitPairElem(WACCParser.PairElemContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCParser#arrayLiter}.
	 * @param ctx the parse tree
	 */
	void enterArrayLiter(WACCParser.ArrayLiterContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCParser#arrayLiter}.
	 * @param ctx the parse tree
	 */
	void exitArrayLiter(WACCParser.ArrayLiterContext ctx);
	/**
	 * Enter a parse tree produced by {@link WACCParser#arrayElem}.
	 * @param ctx the parse tree
	 */
	void enterArrayElem(WACCParser.ArrayElemContext ctx);
	/**
	 * Exit a parse tree produced by {@link WACCParser#arrayElem}.
	 * @param ctx the parse tree
	 */
	void exitArrayElem(WACCParser.ArrayElemContext ctx);
}