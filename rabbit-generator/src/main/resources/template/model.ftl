<#if parentPackage??>
package ${packageName};
</#if>
<#list entity.importPackage as importPackage>
import ${importPackage};
</#list>
import com.scloudic.rabbitframework.jbatis.annontations.*;
import java.io.Serializable;

/**
* This class corresponds to the database table ${entity.tableName}
*/
@Table
public class ${entity.objectName}${fileSuffix} implements Serializable {
	private static final long serialVersionUID = 1L;
    public static final String FIELDS = " <#list entity.idProperties as idProperties><#if idProperties_index!=0>,</#if>${idProperties.columnName}</#list><#list entity.columnProperties as columnProperties><#if columnProperties.columnName?contains("active_status")==false && columnProperties.columnName?contains("del_status")==false>,${columnProperties.columnName}</#if></#list> ";

<#list entity.idProperties as idProperties>
    /**
    * This field corresponds to the database column ${entity.tableName}.${idProperties.columnName}
    * <p>
    * description:${idProperties.remarks}
    */
    <#if entity.dialect?contains("oracle") && idProperties.autoincrement==true>
    @ID(keyType = GenerationType.SEQUENCE)
    <#elseif idProperties.autoincrement==true>
    @ID
    <#else>
    @ID(keyType = GenerationType.MANUAL)
    </#if>
    private ${idProperties.javaType.shortName} ${idProperties.javaProperty};

</#list>
<#list entity.columnProperties as columnProperties>
    /**
    * This field corresponds to the database column ${entity.tableName}.${columnProperties.columnName}
    * <p>
    * description:${columnProperties.remarks}
    */
    @Column
    private ${columnProperties.javaType.shortName} ${columnProperties.javaProperty};

</#list>
<#list entity.idProperties as mIdProperties>
    public void ${mIdProperties.setterMethodName}(${mIdProperties.javaType.shortName} ${mIdProperties.javaProperty}) {
        this.${mIdProperties.javaProperty} = ${mIdProperties.javaProperty};
    }

    public ${mIdProperties.javaType.shortName} ${mIdProperties.getterMethodName}() {
        return ${mIdProperties.javaProperty};
    }

</#list>
<#list entity.columnProperties as mColumnProperties>
    public void ${mColumnProperties.setterMethodName}(${mColumnProperties.javaType.shortName} ${mColumnProperties.javaProperty}) {
        this.${mColumnProperties.javaProperty} = ${mColumnProperties.javaProperty};
    }

    <#if mColumnProperties.javaProperty?contains("delStatus")>
    @Transient
    </#if>
    public ${mColumnProperties.javaType.shortName} ${mColumnProperties.getterMethodName}() {
        return ${mColumnProperties.javaProperty};
    }

</#list>
}
