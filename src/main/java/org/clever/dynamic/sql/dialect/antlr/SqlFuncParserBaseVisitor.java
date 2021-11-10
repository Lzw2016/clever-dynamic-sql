// Generated from C:/Users/lizw/Desktop/SourceCode/github/clever-dynamic-sql/src/main/resources\SqlFuncParser.g4 by ANTLR 4.9.2
package org.clever.dynamic.sql.dialect.antlr;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

/**
 * This class provides an empty implementation of {@link SqlFuncParserVisitor},
 * which can be extended to create a visitor which only needs to handle a subset
 * of the available methods.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public class SqlFuncParserBaseVisitor<T> extends AbstractParseTreeVisitor<T> implements SqlFuncParserVisitor<T> {
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitFuncDeclaration(SqlFuncParser.FuncDeclarationContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitParameterList(SqlFuncParser.ParameterListContext ctx) { return visitChildren(ctx); }
}