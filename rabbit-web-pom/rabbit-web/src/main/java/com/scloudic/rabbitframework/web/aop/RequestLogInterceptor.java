package com.scloudic.rabbitframework.web.aop;

import com.scloudic.rabbitframework.core.utils.JsonUtils;
import com.scloudic.rabbitframework.core.utils.StringUtils;
import com.scloudic.rabbitframework.web.utils.WebUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Enumeration;
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

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void formAnnotatedMethod() {

    }

    @Around("formAnnotatedMethod()")
    public Object doInterceptor(ProceedingJoinPoint pjp) throws Throwable {
        try {
            Object[] args = pjp.getArgs();
            Signature signature = pjp.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();
            //   Path clazzPath = method.getDeclaringClass().getAnnotation(Path.class);
            String methodName = method.getDeclaringClass() + "." + method.getName();
            Map<String, Object> paramsValue = new HashMap<String, Object>();
            Parameter[] parameters = method.getParameters();
            int parameterLength = 0;
            if (parameters != null) {
                parameterLength = parameters.length;
            }
            for (int i = 0; i < parameterLength; i++) {
                Object value = args[i];
                if (value instanceof HttpServletResponse) {
                    continue;
                }
                if (value instanceof HttpServletRequest) {
                    continue;
                }
                Parameter parameter = parameters[i];
                Annotation[] annotations = parameter.getAnnotations();
                String name = getAnnotationName(parameter, annotations);
                if (StringUtils.isBlank(name)) {
                    name = parameter.getName();
                }
                paramsValue.put(name, value);
            }
            LogInfo logInfo = new LogInfo();
            logInfo.setMethodName(methodName);
            logInfo.setValue(paramsValue);
            String jsonValue = JsonUtils.toJson(logInfo);
            HttpServletRequest request = WebUtils.getRequest();
            Enumeration<String> enumeration = request.getHeaderNames();
            Map<String, String> headerMap = new HashMap<>();
            while (enumeration.hasMoreElements()) {
                String name = enumeration.nextElement();
                headerMap.put(name, request.getHeader(name));
            }
            StringBuilder sb = new StringBuilder();
            sb.append("[REQUEST]=>{");
            sb.append("header:" + JsonUtils.toJson(headerMap));
            sb.append(",requestPath:" + request.getRequestURI() + ",value:" + jsonValue + "}");
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
            if (annotation instanceof RequestBody) {
                name = parameter.getType().getSimpleName().toLowerCase(Locale.ENGLISH);
                break;
            }
            if (annotation instanceof RequestParam) {
                RequestParam formParam = (RequestParam) annotation;
                name = formParam.value();
                break;
            }
            if (annotation instanceof PathVariable) {
                PathVariable pathParam = (PathVariable) annotation;
                name = pathParam.value();
                break;
            }

        }
        return name;
    }
}
