// Generated from D:/SourceCode/clever/clever-dynamic-sql/src/main/resources\SqlFuncParser.g4 by ANTLR 4.9.2
package org.clever.dynamic.sql.dialect.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SqlFuncParser}.
 */
public interface SqlFuncParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SqlFuncParser#funcDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterFuncDeclaration(SqlFuncParser.FuncDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlFuncParser#funcDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitFuncDeclaration(SqlFuncParser.FuncDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlFuncParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void enterParameterList(SqlFuncParser.ParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlFuncParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void exitParameterList(SqlFuncParser.ParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlFuncParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(SqlFuncParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlFuncParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(SqlFuncParser.ParameterContext ctx);
}