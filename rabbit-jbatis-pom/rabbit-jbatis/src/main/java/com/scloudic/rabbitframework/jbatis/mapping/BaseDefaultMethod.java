package com.scloudic.rabbitframework.jbatis.mapping;

import com.scloudic.rabbitframework.jbatis.scripting.sql.*;

public enum BaseDefaultMethod {
    INSERTBYENTITY("insertByEntity", "插入", "%s ", InsertByEntity.class, false),
    BACTHINSERT("batchInsertEntity", "批量插入", "%s ", BatchInsertByEntity.class, false),
    DELETEBYID("deleteById", "根据主键删除", "delete from %s where %s=#{id} ", DeleteById.class, false),
    DELETEBYPARAMS("deleteByParams", "根据where条件删除", "delete from %s${tableSuffix} ", DeleteByParams.class, false),
    UPDATEBYENTITY("updateByEntity", "根据实体修改不为空的字段", "%s ", UpdateByEntity.class, false),
    UPDATEDYNAMICTABLE("updateDynamicTable", "根据实体主键修改动态表记录", "%s ", UpdateDynamicTable.class, false),
    UPDATEBYPARAMS("updateByParams", "根据where条件修改", "%s", UpdateByParams.class, false),
    SELECTBYID("selectById", "根据主键查询", "select * from %s where %s=#{id} ", SelectById.class, false),
    SELECTDYNAMICTABLEBYID("selectDynamicTableById", "根据主键查询动态表记录", "select * from %s${tableSuffix} where %s=#{id} ", SelectDynamicTableById.class, false),
    SELECTONEBYPARAMS("selectOneByParams", "根据where条件查询唯一", "select ${showColumns} from %s${tableSuffix} ", SelectOneByParams.class, false),
    SELECTBYPARAMS("selectByParams", "根据where条件查询", "select ${showColumns} from %s${tableSuffix} ", SelectByParams.class, false),
    SELECTCOUNTBYPARAMS("selectCountByParams", "根据where条件查询总数", "select count(1) from %s${tableSuffix} ", SelectCountByParams.class, false),
    SELECTCOUNT("selectCount", "查询记录总数", "select count(*) from %s ", SelectCount.class, false),
    SELECTDYNAMICTABLECOUNT("selectDynamicTableCount", "获取动态表总数", "select count(*) from %s${tableSuffix} ", SelectDynamicTableCount.class, false),
    SELECTENTITYALL("selectEntityAll", "查询所有记录", "select * from %s ", SelectEntityAll.class, false),
    SELECTDYNAMICTABLEENTITYALL("selectDynamicTableEntityAll", "查询动态表所有记录", "select * from %s${tableSuffix} ", SelectDynamicTableEntityAll.class, false),
    SELECTPAGEBYPARAMS("selectPageByParams", "根据where条件分页查询", "select ${showColumns} from %s${tableSuffix} ", SelectPageByParams.class, false),
    SELECTENTITYPAGE("selectEntityPage", "分页查询", "select * from %s ", SelectEntityPage.class, false),
    SELECTDYNAMICTABLEENTITYPAGE("selectDynamicTableEntityPage", "分页查询动态表数据", "select * from %s${tableSuffix} ", SelectDynamicTableEntityPage.class, false),
    INSERTDYNAMICTABLE("insertDynamicTable", "插入", "%s ", InsertDynamicTable.class, false),
    DELETEDYNAMICTABLEBYID("deleteDynamicTableById", "根据主键删除动态表记录", "delete from %s${tableSuffix} where %s=#{id} ", DeleteById.class, false),
    SELECTSQL("selectSQL", "动态SQL查询", "%s ", CustomerSelect.class, true),
    SELECTPAGESQL("selectPageSQL", "动态SQL分页查询", "%s ", CustomerSelect.class, true),
    SELECTWHEREPAGESQL("selectWherePageSQL", "动态SQL分页条件查询", "%s ", CustomerSelect.class, true),
    SELECTWHERESQL("selectWhereSQL", "动态SQL分页查询", "%s ", CustomerSelect.class, true),
    ;
    private String method;
    private String msg;
    private String sql;
    private Class<?> clazz;
    private boolean dynamicSql;

    BaseDefaultMethod(String method, String msg, String sql, Class<?> clazz, boolean dynamicSql) {
        this.method = method;
        this.msg = msg;
        this.sql = sql;
        this.clazz = clazz;
        this.dynamicSql = dynamicSql;
    }

    public boolean isDynamicSql() {
        return dynamicSql;
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
