<#if packageName??>
package ${packageName};
</#if>
<#if (modelPackage??) && (modelPackage != "")>
import ${modelPackage}.${entity.objectName};
</#if>
import com.rabbitframework.jbatis.mapping.BaseMapper;
import com.rabbitframework.jbatis.annontations.Mapper;
/**
* database table ${entity.tableName} mapper interface
**/
@Mapper
public interface ${entity.objectName}${fileSuffix} <#if (modelPackage??) && (modelPackage != "")> extends BaseMapper<${entity.objectName}></#if> {

}
