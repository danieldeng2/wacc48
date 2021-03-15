// Generated from /home/daniel/Projects/wacc_48/src/main/antlr/WACCShellParser.g4 by ANTLR 4.9.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse wacc48.tree produced
 * by {@link WACCShellParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface WACCShellParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse wacc48.tree produced by {@link WACCShellParser#command}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitCommand(WACCShellParser.CommandContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by {@link WACCShellParser#func}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitFunc(WACCShellParser.FuncContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by {@link WACCShellParser#paramList}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitParamList(WACCShellParser.ParamListContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by {@link WACCShellParser#param}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitParam(WACCShellParser.ParamContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by the {@code ifStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitIfStat(WACCShellParser.IfStatContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by the {@code readStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitReadStat(WACCShellParser.ReadStatContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by the {@code printStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitPrintStat(WACCShellParser.PrintStatContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by the {@code seqCompositionStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitSeqCompositionStat(WACCShellParser.SeqCompositionStatContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by the {@code assignmentStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitAssignmentStat(WACCShellParser.AssignmentStatContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by the {@code printlnStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitPrintlnStat(WACCShellParser.PrintlnStatContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by the {@code exitStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitExitStat(WACCShellParser.ExitStatContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by the {@code freeStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitFreeStat(WACCShellParser.FreeStatContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by the {@code beginStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitBeginStat(WACCShellParser.BeginStatContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by the {@code skipStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitSkipStat(WACCShellParser.SkipStatContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by the {@code returnStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitReturnStat(WACCShellParser.ReturnStatContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by the {@code declarationStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitDeclarationStat(WACCShellParser.DeclarationStatContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by the {@code whileStat}
	 * labeled alternative in {@link WACCShellParser#stat}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitWhileStat(WACCShellParser.WhileStatContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by {@link WACCShellParser#assignLhs}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitAssignLhs(WACCShellParser.AssignLhsContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by {@link WACCShellParser#assignRhs}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitAssignRhs(WACCShellParser.AssignRhsContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by {@link WACCShellParser#argList}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitArgList(WACCShellParser.ArgListContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by {@link WACCShellParser#type}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitType(WACCShellParser.TypeContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by {@link WACCShellParser#baseType}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitBaseType(WACCShellParser.BaseTypeContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by {@link WACCShellParser#pairType}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitPairType(WACCShellParser.PairTypeContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by {@link WACCShellParser#pairElemType}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitPairElemType(WACCShellParser.PairElemTypeContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by the {@code strLiteral}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitStrLiteral(WACCShellParser.StrLiteralContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by the {@code identifier}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitIdentifier(WACCShellParser.IdentifierContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by the {@code unaryOpExpr}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitUnaryOpExpr(WACCShellParser.UnaryOpExprContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by the {@code pairLiteral}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitPairLiteral(WACCShellParser.PairLiteralContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by the {@code intLiteral}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitIntLiteral(WACCShellParser.IntLiteralContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by the {@code binOpExpr}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitBinOpExpr(WACCShellParser.BinOpExprContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by the {@code charLiteral}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitCharLiteral(WACCShellParser.CharLiteralContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by the {@code arrayElemExpr}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitArrayElemExpr(WACCShellParser.ArrayElemExprContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by the {@code boolLiteral}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitBoolLiteral(WACCShellParser.BoolLiteralContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by the {@code bracketedExpr}
	 * labeled alternative in {@link WACCShellParser#expr}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitBracketedExpr(WACCShellParser.BracketedExprContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by {@link WACCShellParser#unaryOperator}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitUnaryOperator(WACCShellParser.UnaryOperatorContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by {@link WACCShellParser#funcCall}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitFuncCall(WACCShellParser.FuncCallContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by {@link WACCShellParser#newPair}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitNewPair(WACCShellParser.NewPairContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by {@link WACCShellParser#pairElem}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitPairElem(WACCShellParser.PairElemContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by {@link WACCShellParser#arrayLiter}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitArrayLiter(WACCShellParser.ArrayLiterContext ctx);
	/**
	 * Visit a parse wacc48.tree produced by {@link WACCShellParser#arrayElem}.
	 * @param ctx the parse wacc48.tree
	 * @return the visitor result
	 */
	T visitArrayElem(WACCShellParser.ArrayElemContext ctx);
}