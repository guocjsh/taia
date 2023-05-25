package io.github.guocjsh.excel.factory;

import io.github.guocjsh.excel.annotation.TExcel;
import io.github.guocjsh.excel.entity.ExcelPropsEntity;
import io.github.guocjsh.excel.exception.TaiaExcelException;
import io.github.guocjsh.excel.entity.ExcelEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ExcelMappingFactory {

    /**
     * 根据指定Excel实体获取导入Excel文件相关信息
     *
     * @param clazz
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static ExcelEntity loadImportExcelClass(Class clazz) {
        List<ExcelPropsEntity> propertyList = new ArrayList<ExcelPropsEntity>();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            TExcel importField = field.getAnnotation(TExcel.class);
            if (null != importField) {
                field.setAccessible(true);
                ExcelPropsEntity excelPropertyEntity = ExcelPropsEntity.builder()
                        .fieldEntity(field)
                        .dateFormat(importField.dateFormat().trim())
                        .regex(importField.regex().trim())
                        .regexMessage(importField.regexMessage().trim())
                        .scale(importField.scale())
                        .build();
                propertyList.add(excelPropertyEntity);
            }
        }
        if (propertyList.isEmpty()) {
            throw new TaiaExcelException("[{}] 类未找到标注@ImportField注解的属性!", clazz.getName());
        }
        ExcelEntity excelMapping = new ExcelEntity();
        excelMapping.setPropertyList(propertyList);
        return excelMapping;

    }

    /**
     * 根据指定Excel实体获取导出Excel文件相关信息
     *
     * @param clazz
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static ExcelEntity loadExportExcelClass(Class<?> clazz, String fileName) {
        List<ExcelPropsEntity> propertyList = new ArrayList<ExcelPropsEntity>();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            TExcel exportField = field.getAnnotation(TExcel.class);
            if (null != exportField) {
                field.setAccessible(true);
                ExcelPropsEntity excelPropertyEntity = ExcelPropsEntity.builder()
                        .fieldEntity(field)
                        .scale(exportField.scale())
                        .value(exportField.value())
                        .dateFormat(exportField.dateFormat().trim())
                        .build();
                propertyList.add(excelPropertyEntity);
            }
        }
        if (propertyList.isEmpty()) {
            throw new TaiaExcelException("[{}]类未找到标注@ExportField注解的属性!", clazz.getName());
        }
        ExcelEntity excelMapping = new ExcelEntity();
        excelMapping.setPropertyList(propertyList);
        excelMapping.setFileName(fileName);
        return excelMapping;
    }


}