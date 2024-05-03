package com.scloudic.rabbitframework.core.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.scloudic.rabbitframework.core.utils.JsonUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: juyang.liang
 * @since: 2024-05-03 23:07
 * @updatedUser:
 * @updatedDate:
 * @updatedRemark:
 * @version:
 */
public class JsonTest {
    @Test
    public void all() {
        toJson();
        getObject();
        getMap();
        getList();
        getListMap();
    }

    @Test
    public void toJson() {
        TestBean test = new TestBean();
        test.setName("wwww");
        System.out.println(JsonUtils.toJson(test));
    }

    @Test
    public void getObject() {
        String a = "{\"name\":\"wwww\"}";
        TestBean t = JsonUtils.getObject(a, TestBean.class);
        System.out.println(t.getName());
    }

    @Test
    public void getMap() {
        String a = "{\"name\":\"wwww\"}";
        Map<String, Object> testBean = JsonUtils.getMap(a);
        System.out.println(testBean.get("name"));
    }

    @Test
    public void getList() {
        String json = "[{\"name\":\"wwww\",\"content\":null}]";
        List<TestBean> testBeans = JsonUtils.getList(json, TestBean.class);
        testBeans.forEach(testBean -> System.out.println(testBean.getName()));
    }


    @Test
    public void getListMap() {
        String json = "[{\"name\":\"wwww\",\"content\":1}]";
        List<Map<String, Object>> testBeans = JsonUtils.getListMap(json);
        testBeans.forEach(map -> {
            System.out.println(map.get("name"));
            System.out.println(map.get("content"));
        });
    }
}
