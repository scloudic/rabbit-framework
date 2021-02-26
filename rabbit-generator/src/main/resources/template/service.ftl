<#if packageName??>
package ${packageName};
import ${parentPackage}.${entitySuffix}.${entity.objectName};
</#if>
import com.rabbitframework.jbatis.mapping.BaseMapper;
import com.rabbitframework.jbatis.annontations.Mapper;
import com.rabbitframework.jbatis.mapping.param.WhereParamType;
import com.rabbitframework.jbatis.service.IService

public interface ${entity.objectName}${fileSuffix} extends IService<${entity.objectName}> {

}

