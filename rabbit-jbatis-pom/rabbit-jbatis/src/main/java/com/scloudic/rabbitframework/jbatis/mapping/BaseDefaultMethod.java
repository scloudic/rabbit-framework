package com.scloudic.rabbitframework.jbatis.mapping;

import com.scloudic.rabbitframework.jbatis.scripting.sql.*;

public enum BaseDefaultMethod {
    INSERTBYENTITY("insertByEntity", "插入", "%s ", InsertByEntity.class),
    BACTHINSERT("batchInsertEntity", "批量插入", "%s ", BatchInsertByEntity.class),
    DELETEBYID("deleteById", "根据主键删除", "delete from %s where %s=#{id} ", DeleteById.class),
    DELETEBYPARAMS("deleteByParams", "根据where条件删除", "delete from %s where 1=1 ", DeleteByParams.class),
    UPDATEBYENTITY("updateByEntity", "根据实体修改不为空的字段", "%s ", UpdateByEntity.class),
    UPDATEBYPARAMS("updateByParams", "根据where条件修改", "%s ", UpdateByParams.class),
    SELECTBYID("selectById", "根据主键查询", "select * from %s where %s=#{id} ", SelectById.class),
    SELECTONEBYPARAMS("selectOneByParams", "根据where条件查询唯一", "select ${showColumns} from %s where 1=1 ", SelectOneByParams.class),
    SELECTBYPARAMS("selectByParams", "根据where条件查询", "select ${showColumns} from %s where 1=1 ", SelectByParams.class),
    SELECTCOUNTBYPARAMS("selectCountByParams", "根据where条件查询总数", "select count(1) from %s where 1=1 ", SelectCountByParams.class),
    SELECTCOUNT("selectCount", "查询记录总数", "select count(*) from %s ", SelectCount.class),
    SELECTENTITYALL("selectEntityAll", "查询所有记录", "select * from %s ", SelectEntityAll.class),
    SELECTPAGEBYPARAMS("selectPageByParams", "根据where条件分页查询", "select ${showColumns} from %s where 1=1 ", SelectPageByParams.class),
    SELECTENTITYPAGE("selectEntityPage", "分页查询", "select * from %s ", SelectEntityPage.class),
    ;
    private String method;
    private String msg;
    private String sql;
    private Class<?> clazz;

    BaseDefaultMethod(String method, String msg, String sql, Class<?> clazz) {
        this.method = method;
        this.msg = msg;
        this.sql = sql;
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getMethod() {
        return method;
    }

    public String getMsg() {
        return msg;
    }

    public String getSql() {
        return sql;
    }
}
