package com.scloudic.rabbitframework.core.export.exl;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class ExcelUtils {
    /**
     * 异步读入excel文件
     *
     * @param inputStream
     */
    public static <T> void simpleReadExcel(InputStream inputStream, Class<?> model,
                                           ExcelListener excelListener, Integer sheetNo, Integer headRowNumber) {
        ExcelReaderBuilder excelReaderBuilder = EasyExcel.read(inputStream, model, excelListener);
        if (headRowNumber == null) {
            headRowNumber = 1;
        }
        if (sheetNo == null) {
            excelReaderBuilder.sheet().headRowNumber(headRowNumber).doRead();
        } else {
            excelReaderBuilder.sheet(sheetNo).headRowNumber(headRowNumber).doRead();
        }
    }

    /**
     * 同步导入excel
     *
     * @param inputStream
     * @param model
     * @param sheetNo
     * @param <T>
     * @return
     */
    public static <T> List<T> simpleReadExcelSync(InputStream inputStream, Class<T> model, Integer sheetNo) {
        ExcelReaderBuilder excelReaderBuilder = EasyExcel.read(inputStream, model, null);
        if (sheetNo == null) {
            ExcelReaderSheetBuilder builder = excelReaderBuilder.sheet();
            return builder.doReadSync();
        }
        ExcelReaderSheetBuilder builder = excelReaderBuilder.sheet(sheetNo);
        return builder.doReadSync();
    }


    public static void writeExcel(OutputStream outputStream, Class modelClass,
                                  String sheetName, List<?> data) {
        EasyExcel.write(outputStream, modelClass).sheet(sheetName)
                .doWrite(data);
    }

    public static void writeExcel(HttpServletResponse response, Class modelClass,
                                  String sheetName, List<?> data, String fileName) {
        OutputStream outputStream = null;
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        try {
            outputStream = response.getOutputStream();
            writeExcel(outputStream, modelClass, sheetName, data);
        } catch (IOException e) {

        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (Exception e) {

            }
        }
    }
}
