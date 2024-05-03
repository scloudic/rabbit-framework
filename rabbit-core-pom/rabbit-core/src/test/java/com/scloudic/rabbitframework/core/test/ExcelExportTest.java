package com.scloudic.rabbitframework.core.test;

import com.scloudic.rabbitframework.core.export.exl.ExcelUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelExportTest {
    @org.junit.Test
    public void simpleReadExcelSync() throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(new File("/Users/liangjy/test.xlsx"));
        List<TestBean> tests = ExcelUtils.simpleReadExcelSync(fileInputStream, TestBean.class, null);
        for (TestBean test : tests) {
            System.out.println(test.getName() + "," + test.getContent());
        }
    }

    @org.junit.Test
    public void simpleReadExcel() throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(new
                File("/Users/liangjy/test.xlsx"));
        ExcelUtils.simpleReadExcel(fileInputStream, TestBean.class,
                new ExcelListenerTest(), null, null);
    }

    @org.junit.Test
    public void export() throws FileNotFoundException {
        FileOutputStream fileOutputStream = new FileOutputStream(new
                File("/Users/liangjy/testExport.xlsx"));
        ExcelUtils.writeExcel(fileOutputStream, TestBean.class, "测试", data());
    }

    protected List<?> data() {
        List<TestBean> rowList = new ArrayList<>();
        TestBean test = new TestBean();
        test.setContent("导出内容");
        test.setName("导出名称");
        rowList.add(test);
        return rowList;
    }
}
