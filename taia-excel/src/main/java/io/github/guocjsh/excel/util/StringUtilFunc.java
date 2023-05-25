
package io.github.guocjsh.excel.util;

public class StringUtilFunc {
    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isBlank(Object str) {
        return str == null || "".equals(str.toString().trim()) || "null".equalsIgnoreCase(str.toString().trim());
    }

    /**
     * 格式化null为空
     *
     * @param str
     * @return
     */
    public static String convertNull(Object str) {
        if (str == null) {
            return "";
        }
        return str.toString();
    }

    /**
     * 格式化null为0
     *
     * @param str
     * @return
     */
    public static String convertNullTOZERO(Object str) {
        if (str == null || "".equals(str.toString().trim()) || "null".equalsIgnoreCase(str.toString().trim())) {
            return "0";
        }
        return str.toString();
    }

    public static boolean isNumeric(CharSequence cs) {
        if (isBlank(cs)) {
            return false;
        } else {
            int sz = cs.length();

            for (int i = 0; i < sz; ++i) {
                if (!Character.isDigit(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }
}
