package io.github.guocjsh.i18n.context;

import io.github.guocjsh.i18n.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class I18nContextHandler {

    public static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>();

    public static void set(String key, Object value) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            threadLocal.set(map);
        }
        map.put(key, value);
    }

    public static Object get(String key){
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            threadLocal.set(map);
        }
        return map.get(key);
    }

    public static String getLang(){
        Object value = get("lang");
        return StringUtils.getObjectValue(value);
    }

    public static void setLang(String lang){
        set("lang",lang);
    }

    public static void remove(){
        threadLocal.remove();
    }

}