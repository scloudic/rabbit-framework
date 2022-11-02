<#if parentPackage??>
package ${packageName};
</#if>
<#list entity.importPackage as importPackage>
import ${importPackage};
</#list>
import com.scloudic.rabbitframework.jbatis.annontations.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Table
@Getter
@Setter
public class ${entity.objectName}${fileSuffix} implements Serializable {
	private static final long serialVersionUID = 1L;
<#list entity.idProperties as idProperties>
    /**
    *
    * ${idProperties.remarks}
    *
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
    *
    * ${columnProperties.remarks}
    *
    */
    @Column
    private ${columnProperties.javaType.shortName} ${columnProperties.javaProperty};

</#list>
<#list entity.columnProperties as mColumnProperties>
    <#if mColumnProperties.javaProperty?contains("delStatus")>
    public void ${mColumnProperties.setterMethodName}(${mColumnProperties.javaType.shortName} ${mColumnProperties.javaProperty}) {
        this.${mColumnProperties.javaProperty} = ${mColumnProperties.javaProperty};
    }
    @Transient
    public ${mColumnProperties.javaType.shortName} ${mColumnProperties.getterMethodName}() {
        return ${mColumnProperties.javaProperty};
    }
    </#if>
</#list>
}
