<#if parentPackage??>
package ${packageName};
import ${parentPackage}.${entitySuffix}.${entity.objectName};
</#if>
import com.rabbitframework.jbatis.mapping.BaseMapper;
import com.rabbitframework.jbatis.annontations.Mapper;
/**
* database table ${entity.tableName} mapper interface
**/
@Mapper
public interface ${entity.objectName}${fileSuffix} extends BaseMapper<${entity.objectName}> {

}
