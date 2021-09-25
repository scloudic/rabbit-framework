package com.scloudic.rabbitframework.web.spring.aop;

import com.scloudic.rabbitframework.core.utils.JsonUtils;
import com.scloudic.rabbitframework.core.utils.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * log日志拦截
 *
 * @author: justin
 */
@Aspect
@Order(1)
public class RequestLogInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(RequestLogInterceptor.class);

    @Pointcut("@annotation(javax.ws.rs.Path)")
    public void formAnnotatedMethod() {
    }

    @Around("formAnnotatedMethod()")
    public Object doInterceptor(ProceedingJoinPoint pjp) throws Throwable {
        try {
            Object[] args = pjp.getArgs();
            Signature signature = pjp.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();
            Path path = method.getAnnotation(Path.class);
            //   Path clazzPath = method.getDeclaringClass().getAnnotation(Path.class);
            String methodName = method.getDeclaringClass() + "." + method.getName();
            Map<String, Object> paramsValue = new HashMap<String, Object>();
            Parameter[] parameters = method.getParameters();
            int parameterLength = 0;
            if (parameters != null) {
                parameterLength = parameters.length;
            }
            for (int i = 0; i < parameterLength; i++) {
                Parameter parameter = parameters[i];
                Annotation[] annotations = parameter.getAnnotations();
                Context context = parameter.getAnnotation(Context.class);
                if (context != null) {
                    continue;
                }
                String name = getAnnotationName(parameter, annotations);
                if (StringUtils.isBlank(name)) {
                    name = parameter.getName();
                }
                Object value = args[i];
                paramsValue.put(name, value);
            }
            LogInfo logInfo = new LogInfo();
            logInfo.setMethodName(methodName);
            logInfo.setValue(paramsValue);
            String jsonValue = JsonUtils.toJson(logInfo);
            StringBuilder sb = new StringBuilder();
            sb.append("[REQUEST]=>{");
            String slash = "";
            if (StringUtils.isBlank(path.value()) || path.value().charAt(0) != '/') {
                slash = "/";
            }
            sb.append("requestPath:" + slash + path.value() + ",value:" + jsonValue + "}");
            String result = sb.toString();
            logger.info(result);
        } catch (Exception e) {
            logger.warn("get logInfo fail:" + e.getMessage());
        }
        return pjp.proceed();
    }

    private String getAnnotationName(Parameter parameter, Annotation[] annotations) {
        int annotationsLength = annotations.length;
        String name = null;
        for (int i = 0; i < annotationsLength; i++) {
            Annotation annotation = annotations[i];
            if (annotation instanceof BeanParam) {
                name = parameter.getType().getSimpleName().toLowerCase(Locale.ENGLISH);
                break;
            }
            if (annotation instanceof FormParam) {
                FormParam formParam = (FormParam) annotation;
                name = formParam.value();
                break;
            }
            if (annotation instanceof QueryParam) {
                QueryParam queryParam = (QueryParam) annotation;
                name = queryParam.value();
                break;
            }
            if (annotation instanceof PathParam) {
                PathParam pathParam = (PathParam) annotation;
                name = pathParam.value();
                break;
            }
            if (annotation instanceof FormDataParam) {
                FormDataParam formDataParam = (FormDataParam) annotation;
                name = formDataParam.value();
                break;
            }
        }
        return name;
    }
}
