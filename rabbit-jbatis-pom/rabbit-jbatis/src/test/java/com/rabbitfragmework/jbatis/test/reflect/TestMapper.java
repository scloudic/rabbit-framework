package com.rabbitfragmework.jbatis.test.reflect;

import com.rabbitframework.jbatis.mapping.BaseMapper;

import java.util.List;

public interface TestMapper extends BaseMapper<TestBean> {

    int batchUpdate(List<TestBean> testBeans);
}
