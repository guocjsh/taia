/**
 * Copyright (c) 2021-2028, iron.guo 郭成杰 (jiedreams@sina.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.guocjsh.excel;


import io.github.guocjsh.excel.annotation.TExcelAlias;
import io.github.guocjsh.excel.constant.Constant;
import io.github.guocjsh.excel.entity.ExcelEntity;
import io.github.guocjsh.excel.factory.ExcelReader;
import io.github.guocjsh.excel.factory.ExcelWriter;
import io.github.guocjsh.excel.exception.TaiaExcelException;
import io.github.guocjsh.excel.factory.ExcelMappingFactory;
import io.github.guocjsh.excel.function.ExportFunction;
import io.github.guocjsh.excel.function.ImportFunction;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import org.xml.sax.SAXException;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * @Author iron.guo
 * @Date 2022/5/21
 * @Description
 */
@Slf4j
public class TaiaExcel {

    private HttpServletResponse httpServletResponse;
    private OutputStream outputStream;
    private InputStream inputStream;
    private String fileName;
    private Class excelClass;
    private Integer pageSize;
    private Integer rowAccessWindowSize;
    private Integer recordCountPerSheet;
    private Boolean openAutoColumWidth;

    /**
     * 用于浏览器导出
     *
     * @param
     * @param <T>
     */
    public <T> void exportResponse(List<T> data) {
        exportOutputStream(true, data, null, null);
    }

    /**
     * 用于浏览器导出
     *
     * @param <R>
     * @param <T>
     */
    public <R, T> void exportResponse(R param, ExportFunction<R, T> exportFunction) {
        exportOutputStream(false, null, param, exportFunction);
    }


    /**
     * 用于浏览器导出
     *
     * @param param
     * @param exportFunction
     * @param <R>
     * @param <T>
     */
    public <R, T> void exportOutputStream(Boolean hasData, List<T> data, R param, ExportFunction<R, T> exportFunction) {
        SXSSFWorkbook sxssfWorkbook = null;
        try {
            try {
                verifyResponse();
                sxssfWorkbook = hasData ? singleSheet(data) : singleSheet(param, exportFunction);
                download(sxssfWorkbook, httpServletResponse, URLEncoder.encode(fileName + ".xlsx", "UTF-8"));
            } finally {
                if (sxssfWorkbook != null) {
                    sxssfWorkbook.close();
                }
                if (httpServletResponse != null && httpServletResponse.getOutputStream() != null) {
                    httpServletResponse.getOutputStream().close();
                }
            }
        } catch (Exception e) {
            throw new TaiaExcelException(e);
        }
    }

    private <R, T> SXSSFWorkbook singleSheet(R param, ExportFunction<R, T> exportFunction) throws Exception {
        return commonSingleSheet(false, null, param, exportFunction);
    }

    private <T> SXSSFWorkbook singleSheet(List<T> data) throws Exception {
        return commonSingleSheet(true, data, null, null);
    }


    private <R, T> SXSSFWorkbook commonSingleSheet(Boolean hasData, List<T> data, R param, ExportFunction<R, T> exportFunction) throws Exception {
        verifyParams();
        ExcelEntity excelMapping = ExcelMappingFactory.loadExportExcelClass(excelClass, fileName);
        ExcelWriter excelWriter = new ExcelWriter(excelMapping, pageSize, rowAccessWindowSize, recordCountPerSheet, openAutoColumWidth);
        return hasData ? excelWriter.export(data) : excelWriter.export(param, exportFunction);
    }


    /**
     * 导入excel全部sheet
     *
     * @throws SAXException
     * @throws IOException
     */
    public List<Object> importExcel() {
        if (inputStream == null) {
            throw new TaiaExcelException("inputStream参数为空!");
        }
        ExcelEntity excelMapping = ExcelMappingFactory.loadImportExcelClass(excelClass);
        ExcelReader excelReader = new ExcelReader(excelClass, excelMapping, null);
        return excelReader.importExcel(inputStream);
    }

    /**
     * 导入excel全部sheet
     *
     * @param importFunction
     * @throws OpenXML4JException
     * @throws SAXException
     * @throws IOException
     */
    public void importExcel(ImportFunction importFunction) {
        try {
            if (importFunction == null) {
                throw new TaiaExcelException("excelReadHandler参数为空!");
            }
            if (inputStream == null) {
                throw new TaiaExcelException("inputStream参数为空!");
            }
            ExcelEntity excelMapping = ExcelMappingFactory.loadImportExcelClass(excelClass);
            ExcelReader excelReader = new ExcelReader(excelClass, excelMapping, importFunction);
            excelReader.process(inputStream);
        } catch (Exception e) {
            throw new TaiaExcelException(e);
        }

    }


    /**
     * 导入Excel文件数据
     *
     * @param inputStreamm
     * @param clazz
     * @return
     */
    public static TaiaExcel ImportBuilder(InputStream inputStreamm, Class clazz) {
        return new TaiaExcel(inputStreamm, clazz);
    }

    /**
     * 导入构造器
     *
     * @param inputStream
     * @param excelClass
     */
    protected TaiaExcel(InputStream inputStream, Class excelClass) {
        this(null, null, inputStream, null, excelClass, null, null, null, null);
    }

    /**
     * 通过HttpServletResponse,一般用于在浏览器中导出excel
     *
     * @param httpServletResponse
     * @param clazz
     * @return
     */
    public static TaiaExcel ExportBuilder(HttpServletResponse httpServletResponse, Class clazz) {
        // 获取类注解
        String fileName = "Excel文件名";
        TExcelAlias alias = (TExcelAlias) clazz.getAnnotation(TExcelAlias.class);
        if (alias != null) {
            fileName = alias.value();
        }
        return new TaiaExcel(httpServletResponse, fileName, clazz);
    }

    /**
     * HttpServletResponse导出构造器,一般用于浏览器导出
     *
     * @param response
     * @param fileName
     * @param excelClass
     */
    protected TaiaExcel(HttpServletResponse response, String fileName, Class excelClass) {
        this(response, null, null, fileName, excelClass, Constant.DEFAULT_PAGE_SIZE, Constant.DEFAULT_ROW_ACCESS_WINDOW_SIZE, Constant.DEFAULT_RECORD_COUNT_PEER_SHEET, Constant.OPEN_AUTO_COLUM_WIDTH);
    }


    /**
     * 构造器
     *
     * @param response
     * @param outputStream
     * @param inputStream
     * @param fileName
     * @param excelClass
     * @param pageSize
     * @param rowAccessWindowSize
     * @param recordCountPerSheet
     * @param openAutoColumWidth
     */
    protected TaiaExcel(HttpServletResponse response, OutputStream outputStream, InputStream inputStream
            , String fileName, Class excelClass, Integer pageSize, Integer rowAccessWindowSize, Integer recordCountPerSheet, Boolean openAutoColumWidth) {
        this.httpServletResponse = response;
        this.outputStream = outputStream;
        this.inputStream = inputStream;
        this.fileName = fileName;
        this.excelClass = excelClass;
        this.pageSize = pageSize;
        this.rowAccessWindowSize = rowAccessWindowSize;
        this.recordCountPerSheet = recordCountPerSheet;
        this.openAutoColumWidth = openAutoColumWidth;
    }

    /**
     * 构建Excel服务器响应格式
     *
     * @param wb
     * @param response
     * @param filename
     * @throws IOException
     */
    private void download(SXSSFWorkbook wb, HttpServletResponse response, String filename) throws IOException {
        OutputStream out = response.getOutputStream();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-disposition",
                String.format("attachment; filename=%s", filename));
        if (null != out) {
            wb.write(out);
            out.flush();
        }
    }

    private void verifyResponse() {
        if (httpServletResponse == null) {
            throw new TaiaExcelException("httpServletResponse参数为空!");
        }
    }

    private void verifyParams() {
        if (excelClass == null) {
            throw new TaiaExcelException("excelClass参数为空!");
        }
        if (fileName == null) {
            throw new TaiaExcelException("fileName参数为空!");
        }
    }


}
