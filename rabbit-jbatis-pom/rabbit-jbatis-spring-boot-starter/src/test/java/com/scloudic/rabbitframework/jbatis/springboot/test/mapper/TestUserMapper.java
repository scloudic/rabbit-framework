package com.scloudic.rabbitframework.jbatis.springboot.test.mapper;

import com.scloudic.rabbitframework.jbatis.annontations.*;
import com.scloudic.rabbitframework.jbatis.mapping.BaseMapper;
import com.scloudic.rabbitframework.jbatis.mapping.RowBounds;
import com.scloudic.rabbitframework.jbatis.mapping.param.Where;
import com.scloudic.rabbitframework.jbatis.springboot.test.model.TestUser;
import com.scloudic.rabbitframework.jbatis.springboot.test.model.User;

import java.util.List;
import java.util.Map;

@Mapper
public interface TestUserMapper extends BaseMapper<TestUser> {

    @Create("create table test_user (id int primary key auto_increment,test_name varchar(200),create_time datetime)")
    public int createTestUser();


    public List<TestUser> selectUserList(@SQL String sql);

    @Update("update test_user set test_name=#{testName} where id=#{id}")
    public int updateTest(@Param("id") long id, @Param("testName") String testName);

    @Select("select * from test_user")
    //@CacheNamespace(pool = "defaultCache", key = {"seltestuser"})
    public List<User> selectTestUser();

    @Select("select * from test_user")
    @MapKey("id")
    public Map<Long, User> selectTestUserToMap();

    @Select("select * from test_user")
    public List<User> selectTestUserByPage(RowBounds rowBounds);

    @Select("select * from test_user where 1=1 ")
    public List<User> selectTestUserWhere(Where where);

    @Update("update test_user set test_name=#{testName} where id in "
            + "<foreach collection=\"ids\" item=\"listItem\" open=\"(\" close=\")\" separator=\",\" >#{listItem}</foreach>")
    public int updateTestUserByParamType(@Param("testName") String testName, @Param("ids") Object obj);

    @Update("update test_user set test_name=#{params.testName} where 1=1 ")
    public int updateTestUserByWhereParam(Where whereParamType);

    @Delete("delete from test_user where id=#{id}")
    public int delTestUserById(@Param("id") Long id);

    @Delete("delete from test_user where 1=1 ")
    public int delTestUserWhere(Where where);
}
