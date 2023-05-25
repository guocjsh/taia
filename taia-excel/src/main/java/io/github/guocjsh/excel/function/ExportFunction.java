package io.github.guocjsh.excel.function;

import java.util.List;

public interface ExportFunction<P, T> {
    /**
     * 分页查询方法
     *
     * @param param
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<T> page(P param, int pageNum, int pageSize);

    /**
     * 集合内对象转换
     *
     * @param queryResult
     * @return
     */
    Object convert(T queryResult);

}