package com.rabbitfragmework.dbase.test.reflect;

import com.rabbitframework.jade.mapping.BaseMapper;

import java.util.List;

public interface TestMapper extends BaseMapper<TestBean> {

    int batchUpdate(List<TestBean> testBeans);
}
