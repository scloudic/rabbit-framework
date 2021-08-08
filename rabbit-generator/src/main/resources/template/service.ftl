<#if packageName??>
package ${packageName};
import ${parentPackage}.${entitySuffix}.${entity.objectName};
</#if>
import com.scloudic.rabbitframework.jbatis.service.IService;

public interface ${entity.objectName}${fileSuffix} extends IService<${entity.objectName}> {

}

