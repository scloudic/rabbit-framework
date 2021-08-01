package com.rabbitframework.core.test;

import com.alibaba.excel.context.AnalysisContext;
import com.rabbitframework.core.export.exl.ExcelListener;

public class ExcelListenerTest extends ExcelListener<Test> {
    @Override
    public void invoke(Test test, AnalysisContext analysisContext) {
        System.out.println(test.getName() + "," + test.getContent());
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("doAfterAllAnalysed");
    }
}
