package com.scloudic.rabbitfragmework.jbatis.test.mapper;

import java.util.List;
import java.util.Map;

import com.scloudic.rabbitfragmework.jbatis.test.model.TestUser;
import com.scloudic.rabbitframework.jbatis.annontations.CacheNamespace;
import com.scloudic.rabbitframework.jbatis.annontations.Create;
import com.scloudic.rabbitframework.jbatis.annontations.Insert;
import com.scloudic.rabbitframework.jbatis.annontations.MapKey;
import com.scloudic.rabbitframework.jbatis.annontations.Mapper;
import com.scloudic.rabbitframework.jbatis.annontations.Param;
import com.scloudic.rabbitframework.jbatis.annontations.Select;
import com.scloudic.rabbitframework.jbatis.annontations.Update;
import com.scloudic.rabbitframework.jbatis.mapping.BaseMapper;
import com.scloudic.rabbitframework.jbatis.mapping.RowBounds;
import com.scloudic.rabbitframework.jbatis.mapping.param.Where;

@Mapper
public interface TestUserMapper extends BaseMapper<TestUser> {

    @Create("create table test_user (id int primary key auto_increment,test_name varchar(200))")
    public int createTestUser();

    @Update("update test_user set test_name=#{testName} where id=#{id}")
    public int updateTest(@Param("id") long id, @Param("testName") String testName);

    @Select("select * from test_user")
    @CacheNamespace(pool = "defaultCache", key = {"seltestuser"})
    public List<TestUser> selectTestUser();

    @Select("select * from test_user")
    @MapKey("id")
    public Map<Long, TestUser> selectTestUserToMap();

    @Select("select * from test_user")
    public List<TestUser> selectTestUserByPage(RowBounds rowBounds);

    @Update("update test_user set test_name=#{testName} where id in "
            + "<foreach collection=\"ids\" item=\"listItem\" open=\"(\" close=\")\" separator=\",\" >#{listItem}</foreach>")
    public int updateTestUserByParamType(@Param("testName") String testName, @Param("ids") Object obj);

    @Update("update test_user set test_name=#{params.testName} where 1=1 ")
    public int updateTestUserByWhereParam(Where whereParamType);

    @Insert(batch = true)
    public int bacthInsert(List<TestUser> testUsers);
}
