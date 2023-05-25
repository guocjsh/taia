package io.github.guocjsh.excel.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface TExcel {

    /**
     * 列名称
     * @return
     */
    String value();

    /**
     * 日期格式 默认 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    String dateFormat() default "yyyy-MM-dd HH:mm:ss";

    /**
     * BigDecimal精度 默认:-1(默认不开启BigDecimal格式化)
     *
     * @return
     */
    int scale() default -1;

    /**
     * 正则表达式校验
     *
     * @return
     */
    String regex() default "";

    /**
     * 正则表达式校验失败返回的错误信息,regex配置后生效
     *
     * @return
     */
    String regexMessage() default "正则表达式验证失败";

}
