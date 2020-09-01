package org.clever.dynamic.sql.ognl;

import lombok.extern.slf4j.Slf4j;
import ognl.Ognl;
import ognl.OgnlException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public final class OgnlCache {
    private static final OgnlMemberAccess MEMBER_ACCESS = new OgnlMemberAccess();
    private static final OgnlClassResolver CLASS_RESOLVER = new OgnlClassResolver();
    private static final Map<String, Object> expressionCache = new ConcurrentHashMap<>();

    private OgnlCache() {
    }

    @SuppressWarnings("rawtypes")
    public static Object getValue(String expression, Object root) {
        try {
            Map context = Ognl.createDefaultContext(root, MEMBER_ACCESS, CLASS_RESOLVER, null);
            return Ognl.getValue(parseExpression(expression), context, root);
        } catch (Throwable e) {
            log.warn("Error evaluating expression '{}'.", expression);
            // log.warn("Error evaluating expression '{}'. Cause: ", expression, e);
            // throw new BuilderException("Error evaluating expression '" + expression + "'. Cause: " + e, e);
            return null;
        }
    }

    private static Object parseExpression(String expression) throws OgnlException {
        Object node = expressionCache.get(expression);
        if (node == null) {
            node = Ognl.parseExpression(expression);
            expressionCache.put(expression, node);
        }
        return node;
    }
}
