<#if parentPackage??>
package ${packageName};
import ${parentPackage}.${entitySuffix}.${entity.objectName};
</#if>
import com.scloudic.rabbitframework.jbatis.mapping.BaseMapper;
import com.scloudic.rabbitframework.jbatis.annontations.Mapper;
/**
* database table ${entity.tableName} mapper interface
**/
@Mapper
public interface ${entity.objectName}${fileSuffix} extends BaseMapper<${entity.objectName}> {

}
