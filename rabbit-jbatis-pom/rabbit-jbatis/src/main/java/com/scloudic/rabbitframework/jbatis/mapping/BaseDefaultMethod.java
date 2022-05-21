package com.scloudic.rabbitframework.jbatis.mapping;

import com.scloudic.rabbitframework.jbatis.scripting.sql.*;

public enum BaseDefaultMethod {
    INSERTBYENTITY("insertByEntity", "插入", "%s ", InsertByEntity.class),
    BACTHINSERT("batchInsertEntity", "批量插入", "%s ", BatchInsertByEntity.class),
    DELETEBYID("deleteById", "根据主键删除", "delete from %s where %s=#{id} ", DeleteById.class),
    DELETEBYPARAMS("deleteByParams", "根据where条件删除", "delete from %s${tableSuffix} where 1=1 ", DeleteByParams.class),
    UPDATEBYENTITY("updateByEntity", "根据实体修改不为空的字段", "%s ", UpdateByEntity.class),
    UPDATEDYNAMICTABLE("updateDynamicTable", "根据实体主键修改动态表记录", "%s ", UpdateDynamicTable.class),
    UPDATEBYPARAMS("updateByParams", "根据where条件修改", "%s", UpdateByParams.class),
    SELECTBYID("selectById", "根据主键查询", "select * from %s where %s=#{id} ", SelectById.class),
    SELECTDYNAMICTABLEBYID("selectDynamicTableById", "根据主键查询动态表记录", "select * from %s${tableSuffix} where %s=#{id} ", SelectDynamicTableById.class),
    SELECTONEBYPARAMS("selectOneByParams", "根据where条件查询唯一", "select ${showColumns} from %s${tableSuffix} where 1=1 ", SelectOneByParams.class),
    SELECTBYPARAMS("selectByParams", "根据where条件查询", "select ${showColumns} from %s${tableSuffix} where 1=1 ", SelectByParams.class),
    SELECTCOUNTBYPARAMS("selectCountByParams", "根据where条件查询总数", "select count(1) from %s${tableSuffix} where 1=1 ", SelectCountByParams.class),
    SELECTCOUNT("selectCount", "查询记录总数", "select count(*) from %s ", SelectCount.class),
    SELECTDYNAMICTABLECOUNT("selectDynamicTableCount", "获取动态表总数", "select count(*) from %s${tableSuffix} ", SelectDynamicTableCount.class),
    SELECTENTITYALL("selectEntityAll", "查询所有记录", "select * from %s ", SelectEntityAll.class),
    SELECTDYNAMICTABLEENTITYALL("selectDynamicTableEntityAll", "查询动态表所有记录", "select * from %s${tableSuffix} ", SelectDynamicTableEntityAll.class),
    SELECTPAGEBYPARAMS("selectPageByParams", "根据where条件分页查询", "select ${showColumns} from %s${tableSuffix} where 1=1 ", SelectPageByParams.class),
    SELECTENTITYPAGE("selectEntityPage", "分页查询", "select * from %s ", SelectEntityPage.class),
    SELECTDYNAMICTABLEENTITYPAGE("selectDynamicTableEntityPage", "分页查询动态表数据", "select * from %s${tableSuffix} ", SelectDynamicTableEntityPage.class),
    INSERTDYNAMICTABLE("insertDynamicTable", "插入", "%s ", InsertDynamicTable.class),
    DELETEDYNAMICTABLEBYID("deleteDynamicTableById", "根据主键删除动态表记录", "delete from %s${tableSuffix} where %s=#{id} ", DeleteById.class),
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
