// Generated from D:/SourceCode/clever/clever-dynamic-sql/src/main/resources\SqlFuncParser.g4 by ANTLR 4.9.2
package org.clever.dynamic.sql.dialect.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SqlFuncParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SqlFuncParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SqlFuncParser#funcDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncDeclaration(SqlFuncParser.FuncDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlFuncParser#parameterList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterList(SqlFuncParser.ParameterListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SqlFuncParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter(SqlFuncParser.ParameterContext ctx);
}