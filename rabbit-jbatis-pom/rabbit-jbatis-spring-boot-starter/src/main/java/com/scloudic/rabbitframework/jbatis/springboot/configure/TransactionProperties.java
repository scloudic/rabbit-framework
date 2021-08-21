package com.scloudic.rabbitframework.jbatis.springboot.configure;

import java.util.HashMap;
import java.util.Map;

public class TransactionProperties {
    private TransactionType transactionType = TransactionType.SIMPLE;
    private String defaultDataSourceBean = "dataSource";
    private int timeOut = -1;
    private Map<String,String> multiTran = new HashMap<>();

    public String getDefaultDataSourceBean() {
        return defaultDataSourceBean;
    }

    public void setDefaultDataSourceBean(String defaultDataSourceBean) {
        this.defaultDataSourceBean = defaultDataSourceBean;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public Map<String, String> getMultiTran() {
        return multiTran;
    }

    public void setMultiTran(Map<String, String> multiTran) {
        this.multiTran = multiTran;
    }

    public enum TransactionType {
        SIMPLE, JTAATOMIKOS, MULTI
    }
}
