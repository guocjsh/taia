/*
 * Copyright 2018 NingWei (ningww1@126.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */
/**
 * @author NingWei
 */
package io.github.guocjsh.excel.factory;


import io.github.guocjsh.excel.constant.Constant;
import io.github.guocjsh.excel.entity.ExcelEntity;
import io.github.guocjsh.excel.entity.ExcelPropsEntity;
import io.github.guocjsh.excel.function.ExportFunction;
import io.github.guocjsh.excel.util.DateFormatUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 导出具体实现类
 *
 * @author NingWei
 */
@Slf4j
public class ExcelWriter {

    private Integer rowAccessWindowSize;
    private ExcelEntity excelEntity;
    private Integer pageSize;
    private Integer nullCellCount = 0;
    private Integer recordCountPerSheet;
    private XSSFCellStyle headCellStyle;
    private Map<Integer, Integer> columnWidthMap = new HashMap<Integer, Integer>();
    private Boolean openAutoColumWidth;

    public ExcelWriter(ExcelEntity excelEntity, Integer pageSize, Integer rowAccessWindowSize, Integer recordCountPerSheet, Boolean openAutoColumWidth) {
        this.excelEntity = excelEntity;
        this.pageSize = pageSize;
        this.rowAccessWindowSize = rowAccessWindowSize;
        this.recordCountPerSheet = recordCountPerSheet;
        this.openAutoColumWidth = openAutoColumWidth;
    }


    @SneakyThrows
    public <T> SXSSFWorkbook export(List<T> data) {
        return generateWorkbook(true,data,null,null);
    }

    @SneakyThrows
    public <P, T> SXSSFWorkbook export(P param, ExportFunction<P, T> exportFunction) {
        return generateWorkbook(false,null,param,exportFunction);
    }

    /**
     * @param data
     * @param <T>
     * @return
     * @throws Exception
     */
    public <P, T> SXSSFWorkbook generateWorkbook(Boolean hasData, List<T> data, P param, ExportFunction<P, T> exportFunction) throws Exception {
        SXSSFWorkbook workbook = new SXSSFWorkbook(rowAccessWindowSize);
        int sheetNo = 1;
        int rowNum = 1;
        List<ExcelPropsEntity> propertyList = excelEntity.getPropertyList();
        //初始化第一行
        SXSSFSheet sheet = generateHeader(workbook, propertyList, excelEntity.getFileName());
        //生成其他行
        int firstPageNo = 1;
        while (true) {
            if (!hasData) {
                data = exportFunction.page(param, firstPageNo, pageSize);
            }
            if (data == null || data.isEmpty()) {
                if (rowNum != 1) {
                    sizeColumWidth(sheet, propertyList.size());
                }
                log.warn("查询结果为空,结束查询!");
                break;
            }
            int dataSize = data.size();
            for (int i = 1; i <= dataSize; i++, rowNum++) {
                T queryResult = data.get(i - 1);
                Object convertResult = hasData ? queryResult : exportFunction.convert(queryResult);
                if (rowNum > Constant.MAX_RECORD_COUNT_PEER_SHEET) {
                    sizeColumWidth(sheet, propertyList.size());
                    sheet = generateHeader(workbook, propertyList, excelEntity.getFileName() + "_" + sheetNo);
                    sheetNo++;
                    rowNum = 1;
                    columnWidthMap.clear();
                }
                SXSSFRow row = sheet.createRow(rowNum);
                for (int j = 0; j < propertyList.size(); j++) {
                    SXSSFCell cell = row.createCell(j);
                    buildCellValue(cell, convertResult, propertyList.get(j));
                    calculateColumWidth(cell, j);
                }
                if (nullCellCount == propertyList.size()) {
                    log.warn("忽略一行空数据!");
                    sheet.removeRow(row);
                    rowNum--;
                }
                nullCellCount = 0;
            }
            if (data.size() < pageSize) {
                sizeColumWidth(sheet, propertyList.size());
                log.warn("查询结果数量小于pageSize,结束查询!");
                break;
            }
            firstPageNo++;
        }
        return workbook;
    }




    /**
     * 自动适配中文单元格
     *
     * @param sheet
     */
    private void sizeColumWidth(SXSSFSheet sheet, Integer columnSize) {
        if (openAutoColumWidth) {
            for (int j = 0; j < columnSize; j++) {
                if (columnWidthMap.get(j) != null) {
                    sheet.setColumnWidth(j, columnWidthMap.get(j) * 256);
                }
            }
        }
    }

    /**
     * 自动适配中文单元格
     *
     * @param cell
     * @param columnIndex
     */
    private void calculateColumWidth(SXSSFCell cell, Integer columnIndex) {
        if (openAutoColumWidth) {
            String cellValue = cell.getStringCellValue();
            int length = cellValue.getBytes().length;
            length += (int) Math.ceil((double) ((cellValue.length() * 3 - length) / 2) * 0.1D);
            length = Math.max(length, Constant.CHINESES_ATUO_SIZE_COLUMN_WIDTH_MIN);
            length = Math.min(length, Constant.CHINESES_ATUO_SIZE_COLUMN_WIDTH_MAX);
            if (columnWidthMap.get(columnIndex) == null || columnWidthMap.get(columnIndex) < length) {
                columnWidthMap.put(columnIndex, length);
            }
        }
    }

    /**
     * 初始化第一行的属性
     *
     * @param workbook
     * @param propertyList
     * @param sheetName
     * @return
     */
    private SXSSFSheet generateHeader(SXSSFWorkbook workbook, List<ExcelPropsEntity> propertyList, String sheetName) {
        SXSSFSheet sheet = workbook.createSheet(sheetName);
        SXSSFRow headerRow = sheet.createRow(0);
        headerRow.setHeight((short) 600);
        CellStyle headCellStyle = getHeaderCellStyle(workbook);
        for (int i = 0; i < propertyList.size(); i++) {
            SXSSFCell cell = headerRow.createCell(i);
            cell.setCellStyle(headCellStyle);
            cell.setCellValue(propertyList.get(i).getValue());
            calculateColumWidth(cell, i);
        }
        return sheet;
    }

    /**
     * 构造 除第一行以外的其他行的列值
     *
     * @param cell
     * @param entity
     * @param property
     */
    private void buildCellValue(SXSSFCell cell, Object entity, ExcelPropsEntity property) throws Exception {
        Field field = property.getFieldEntity();
        Object cellValue = field.get(entity);
        if (StringUtils.isBlank((CharSequence) cellValue) || "0".equals(cellValue.toString()) || "0.0".equals(cellValue.toString()) || "0.00".equals(cellValue.toString())) {
            nullCellCount++;
        }
        if (cellValue == null) {
            cell.setCellValue("");
        } else if (cellValue instanceof BigDecimal) {
            if (-1 == property.getScale()) {
                cell.setCellValue(cellValue.toString());
            } else {
                cell.setCellValue((((BigDecimal) cellValue).setScale(property.getScale(), RoundingMode.HALF_UP)).toString());
            }
        } else if (cellValue instanceof Date) {
            cell.setCellValue(DateFormatUtil.format(property.getDateFormat(), (Date) cellValue));
        } else {
            cell.setCellValue(cellValue.toString());
        }
    }


    public CellStyle getHeaderCellStyle(SXSSFWorkbook workbook) {
        if (headCellStyle == null) {
            headCellStyle = workbook.getXSSFWorkbook().createCellStyle();
            headCellStyle.setBorderTop(BorderStyle.NONE);
            headCellStyle.setBorderRight(BorderStyle.NONE);
            headCellStyle.setBorderBottom(BorderStyle.NONE);
            headCellStyle.setBorderLeft(BorderStyle.NONE);
            headCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            XSSFColor color = new XSSFColor(new java.awt.Color(217, 217, 217));
            headCellStyle.setFillForegroundColor(color);
            headCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = workbook.createFont();
            font.setFontName("微软雅黑");
            font.setColor(IndexedColors.ROYAL_BLUE.index);
            font.setBold(true);
            headCellStyle.setFont(font);
            headCellStyle.setDataFormat(workbook.createDataFormat().getFormat("@"));
        }
        return headCellStyle;
    }
}
