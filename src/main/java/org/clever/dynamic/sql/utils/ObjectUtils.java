package org.clever.dynamic.sql.utils;

import java.util.*;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/09/02 13:22 <br/>
 */
@SuppressWarnings("DuplicatedCode")
public class ObjectUtils {
    public static final ObjectUtils Instance = new ObjectUtils();

    private ObjectUtils() {
    }

    public boolean isIn(final Object obj, final String[] array) {
        if (array == null || array.length <= 0) {
            return false;
        }
        String str1 = obj == null ? null : String.valueOf(obj);
        for (String str2 : array) {
            if (Objects.equals(str1, str2)) {
                return true;
            }
        }
        return false;
    }

    public boolean isIn(final Object obj, final String arrStr) {
        if (arrStr == null) {
            return false;
        }
        String str1 = obj == null ? null : String.valueOf(obj);
        String[] array = split(arrStr, ",");
        return isIn(str1, array);
    }

    public boolean isIn(final Object obj, final String arrStr, String separatorChars) {
        if (arrStr == null) {
            return false;
        }
        if (separatorChars == null) {
            separatorChars = ",";
        }
        String str1 = obj == null ? null : String.valueOf(obj);
        String[] array = split(arrStr, separatorChars);
        return isIn(str1, array);
    }

    public boolean isInIgnoreCase(final Object obj, final CharSequence[] array) {
        if (array == null || array.length <= 0) {
            return false;
        }
        String str1 = obj == null ? null : String.valueOf(obj);
        for (CharSequence str2 : array) {
            if (StringUtils.Instance.equalsIgnoreCase(str1, str2)) {
                return true;
            }
        }
        return false;
    }

    public boolean isInIgnoreCase(final Object obj, final String arrStr) {
        if (arrStr == null) {
            return false;
        }
        String str1 = obj == null ? null : String.valueOf(obj);
        String[] array = split(arrStr, ",");
        return isInIgnoreCase(str1, array);
    }

    public boolean isInIgnoreCase(final Object obj, final String arrStr, String separatorChars) {
        if (arrStr == null) {
            return false;
        }
        if (separatorChars == null) {
            separatorChars = ",";
        }
        String str1 = obj == null ? null : String.valueOf(obj);
        String[] array = split(arrStr, separatorChars);
        return isInIgnoreCase(str1, array);
    }

    @SuppressWarnings("rawtypes")
    public boolean notEmpty(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof CharSequence) {
            return StringUtils.Instance.isNotBlank((CharSequence) obj);
        } else if (obj instanceof Collection) {
            return !((Collection) obj).isEmpty();
        } else if (obj instanceof Map) {
            return !((Map) obj).isEmpty();
        } else if (obj instanceof char[]) {
            return ((char[]) obj).length > 0;
        } else if (obj instanceof byte[]) {
            return ((byte[]) obj).length > 0;
        } else if (obj instanceof short[]) {
            return ((short[]) obj).length > 0;
        } else if (obj instanceof int[]) {
            return ((int[]) obj).length > 0;
        } else if (obj instanceof float[]) {
            return ((float[]) obj).length > 0;
        } else if (obj instanceof double[]) {
            return ((double[]) obj).length > 0;
        } else if (obj instanceof long[]) {
            return ((long[]) obj).length > 0;
        } else if (obj instanceof boolean[]) {
            return ((boolean[]) obj).length > 0;
        } else if (obj.getClass().isArray()) {
            return ((Object[]) obj).length > 0;
        }
        return true;
    }

    public boolean hasValue(final Object obj) {
        return notEmpty(obj);
    }

    public boolean exists(final Object obj) {
        return notEmpty(obj);
    }

    // -----------------------------------------------------------------------------------------------------------------------------------

    private static String[] split(final String str, final String separatorChars) {
        return splitWorker(str, separatorChars, -1, false);
    }

    private static String[] split(final String str, final String separatorChars, final int max) {
        return splitWorker(str, separatorChars, max, false);
    }

    @SuppressWarnings("DuplicatedCode")
    private static String[] splitWorker(final String str, final String separatorChars, final int max, final boolean preserveAllTokens) {
        // Performance tuned for 2.0 (JDK1.4)
        // Direct code is quicker than StringTokenizer.
        // Also, StringTokenizer uses isSpace() not isWhitespace()

        if (str == null) {
            return null;
        }
        final int len = str.length();
        if (len == 0) {
            return new String[0];
        }
        final List<String> list = new ArrayList<>();
        int sizePlus1 = 1;
        int i = 0, start = 0;
        boolean match = false;
        boolean lastMatch = false;
        if (separatorChars == null) {
            // Null separator means use whitespace
            while (i < len) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else if (separatorChars.length() == 1) {
            // Optimise 1 character case
            final char sep = separatorChars.charAt(0);
            while (i < len) {
                if (str.charAt(i) == sep) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else {
            // standard case
            while (i < len) {
                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        }
        if (match || preserveAllTokens && lastMatch) {
            list.add(str.substring(start, i));
        }
        return list.toArray(new String[0]);
    }
}
