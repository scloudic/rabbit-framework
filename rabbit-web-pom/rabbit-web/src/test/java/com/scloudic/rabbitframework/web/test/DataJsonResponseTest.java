package com.scloudic.rabbitframework.web.test;

import com.scloudic.rabbitframework.web.DataJsonResponse;

public class DataJsonResponseTest {
    public static void main(String[] args) {
        DataJsonResponse dataJsonResponse = new DataJsonResponse();
        dataJsonResponse.setData("test", null);
        dataJsonResponse.setData("test1", "test1");
        System.out.println(dataJsonResponse.toJson());
    }
}
