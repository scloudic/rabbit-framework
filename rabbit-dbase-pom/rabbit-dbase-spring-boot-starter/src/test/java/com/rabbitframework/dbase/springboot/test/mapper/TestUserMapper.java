package com.rabbitframework.dbase.springboot.test.mapper;
import com.rabbitframework.dbase.annontations.*;
import com.rabbitframework.dbase.mapping.BaseMapper;
import com.rabbitframework.dbase.mapping.RowBounds;
import com.rabbitframework.dbase.mapping.param.WhereParamType;
import com.rabbitframework.dbase.springboot.test.model.TestUser;
import java.util.List;
import java.util.Map;

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
    public int updateTestUserByWhereParam(WhereParamType whereParamType);

    @Insert(batch = true)
    public int bacthInsert(List<TestUser> testUsers);
}
