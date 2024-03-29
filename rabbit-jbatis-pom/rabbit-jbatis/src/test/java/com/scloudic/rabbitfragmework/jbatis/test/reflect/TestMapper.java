package com.scloudic.rabbitfragmework.jbatis.test.reflect;

import com.scloudic.rabbitframework.jbatis.mapping.BaseMapper;

import java.util.List;

public interface TestMapper extends BaseMapper<TestBean> {

    List<TestBean> batchUpdate(List<TestBean> testBeans);

}
