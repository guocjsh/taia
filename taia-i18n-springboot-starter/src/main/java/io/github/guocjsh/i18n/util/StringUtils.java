package io.github.guocjsh.i18n.util;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static String getObjectValue(Object obj) {
        return obj == null ? "" : obj.toString();
    }

}