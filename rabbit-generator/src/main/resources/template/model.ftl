<#if packageName??>
package ${packageName};
</#if>
<#list entity.importPackage as importPackage>
import ${importPackage};
</#list>
import com.rabbitframework.dbase.annontations.*;
import java.io.Serializable;

/**
* This class corresponds to the database table ${entity.tableName}
*/
@Table
public class ${entity.objectName}${fileSuffix} implements Serializable {
	private static final long serialVersionUID = 1L;
<#list entity.idProperties as idProperties>
	public static final String ${idProperties.columnName?upper_case} = "${idProperties.columnName}";
</#list>
<#list entity.columnProperties as columnProperties>
	public static final String ${columnProperties.columnName?upper_case} = "${columnProperties.columnName}";
</#list>

<#list entity.idProperties as idProperties>
    /**
    * This field corresponds to the database column ${entity.tableName}.${idProperties.columnName} 
    * <p/>
    * description:${idProperties.remarks} 
    */
    @ID
    private ${idProperties.javaType.shortName} ${idProperties.javaProperty};
    
</#list>
<#list entity.columnProperties as columnProperties>
    /**
    * This field corresponds to the database column ${entity.tableName}.${columnProperties.columnName}
    * <p/>
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

    public ${mColumnProperties.javaType.shortName} ${mColumnProperties.getterMethodName}() {
        return ${mColumnProperties.javaProperty};
    }

</#list>
}
