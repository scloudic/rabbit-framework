<#if packageName??>
package ${packageName};
import ${parentPackage}.${entitySuffix}.${entity.objectName};
import ${parentPackage}.${mapperSuffix}.${entity.objectName}${mapperSuffix?cap_first};
import ${parentPackage}.${serviceSuffix}.${entity.objectName}${serviceSuffix?cap_first};
</#if>
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rabbitframework.jbatis.service.IServiceImpl;
@Service
public class ${entity.objectName}${fileSuffix} extends IServiceImpl<${entity.objectName}${mapperSuffix?cap_first},${entity.objectName}> implements ${entity.objectName}${serviceSuffix?cap_first} {
    @Autowired
    private ${entity.objectName}${mapperSuffix?cap_first} ${entity.objectName?uncap_first}${mapperSuffix?cap_first};
    @Override
    public ${entity.objectName}${mapperSuffix?cap_first} getBaseMapper() {
        return ${entity.objectName?uncap_first}${mapperSuffix?cap_first};
    }
}

