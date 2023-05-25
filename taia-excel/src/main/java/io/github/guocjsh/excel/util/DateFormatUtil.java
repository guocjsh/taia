package io.github.guocjsh.excel.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class DateFormatUtil {

    private final static LoadingCache<String, SimpleDateFormat> LOAD_CACHE =
            CacheBuilder.newBuilder()
                    .maximumSize(5)
                    .build(new CacheLoader<String, SimpleDateFormat>() {
                        @Override
                        public SimpleDateFormat load(String pattern) {
                            return new SimpleDateFormat(pattern);
                        }
                    });

    public static Date parse(String pattern, String value) throws ExecutionException, ParseException {
        return LOAD_CACHE.get(pattern).parse(value);
    }

    public static String format(String pattern, Date value) throws ExecutionException {
        return LOAD_CACHE.get(pattern).format(value);
    }
}