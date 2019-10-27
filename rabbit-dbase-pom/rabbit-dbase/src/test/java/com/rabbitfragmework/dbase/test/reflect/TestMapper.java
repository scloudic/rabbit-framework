package com.rabbitfragmework.dbase.test.reflect;

import com.rabbitframework.dbase.mapping.BaseMapper;

import java.util.List;

public interface TestMapper extends BaseMapper<TestBean> {

    int batchUpdate(List<TestBean> testBeans);
}
