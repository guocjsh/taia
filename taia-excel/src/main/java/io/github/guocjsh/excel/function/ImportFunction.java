package io.github.guocjsh.excel.function;

import io.github.guocjsh.excel.entity.ErrorEntity;

public interface ImportFunction<T> {

    /**
     * 导入后执行的操作
     *
     * @param sheetIndex
     * @param rowIndex
     * @param entity
     */
    void execute(int sheetIndex, int rowIndex, T entity);

    /**
     * 错误回调
     */
    void error(ErrorEntity errorEntity);
}