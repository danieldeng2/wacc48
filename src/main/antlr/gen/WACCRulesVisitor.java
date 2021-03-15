// Generated from /home/daniel/Projects/wacc_48/src/main/antlr/WACCRules.g4 by ANTLR 4.9.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link WACCRules}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface WACCRulesVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link WACCRules#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc(WACCRules.FuncContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCRules#paramList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParamList(WACCRules.ParamListContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCRules#param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam(WACCRules.ParamContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ifStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStat(WACCRules.IfStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code readStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReadStat(WACCRules.ReadStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code printStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrintStat(WACCRules.PrintStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code seqCompositionStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeqCompositionStat(WACCRules.SeqCompositionStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignmentStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentStat(WACCRules.AssignmentStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code printlnStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrintlnStat(WACCRules.PrintlnStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exitStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExitStat(WACCRules.ExitStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code freeStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFreeStat(WACCRules.FreeStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code beginStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBeginStat(WACCRules.BeginStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code skipStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSkipStat(WACCRules.SkipStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code returnStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStat(WACCRules.ReturnStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code declarationStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarationStat(WACCRules.DeclarationStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code whileStat}
	 * labeled alternative in {@link WACCRules#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStat(WACCRules.WhileStatContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCRules#assignLhs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignLhs(WACCRules.AssignLhsContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCRules#assignRhs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignRhs(WACCRules.AssignRhsContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCRules#argList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgList(WACCRules.ArgListContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCRules#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(WACCRules.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCRules#baseType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBaseType(WACCRules.BaseTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCRules#pairType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPairType(WACCRules.PairTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCRules#pairElemType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPairElemType(WACCRules.PairElemTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code strLiteral}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStrLiteral(WACCRules.StrLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code identifier}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(WACCRules.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unaryOpExpr}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOpExpr(WACCRules.UnaryOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code pairLiteral}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPairLiteral(WACCRules.PairLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code intLiteral}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntLiteral(WACCRules.IntLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code binOpExpr}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinOpExpr(WACCRules.BinOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code charLiteral}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharLiteral(WACCRules.CharLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayElemExpr}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayElemExpr(WACCRules.ArrayElemExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code boolLiteral}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolLiteral(WACCRules.BoolLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bracketedExpr}
	 * labeled alternative in {@link WACCRules#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBracketedExpr(WACCRules.BracketedExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCRules#unaryOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOperator(WACCRules.UnaryOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCRules#funcCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncCall(WACCRules.FuncCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCRules#newPair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewPair(WACCRules.NewPairContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCRules#pairElem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPairElem(WACCRules.PairElemContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCRules#arrayLiter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayLiter(WACCRules.ArrayLiterContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCRules#arrayElem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayElem(WACCRules.ArrayElemContext ctx);
}