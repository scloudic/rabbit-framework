package com.rabbitframework.web.spring.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.BeanParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.QueryParam;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tjzq.commons.utils.JsonUtils;
import com.tjzq.commons.utils.StringUtils;

/**
 * log日志拦截
 *
 * @author: justin
 * @date: 2017-06-13 12:13
 */
@Aspect
public class LogInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

	public Object doInterceptor(ProceedingJoinPoint pjp) throws Throwable {
		try {
			Object[] args = pjp.getArgs();
			if (args == null || args.length == 0) {
				return pjp.proceed();
			}
			Signature signature = pjp.getSignature();
			MethodSignature methodSignature = (MethodSignature) signature;
			Method method = methodSignature.getMethod();
			String methodName = method.getDeclaringClass() + "." + method.getName();
			Map<String, Object> paramsValue = new HashMap<String, Object>();
			Parameter[] parameters = method.getParameters();
			int parameterLength = parameters.length;
			for (int i = 0; i < parameterLength; i++) {
				Parameter parameter = parameters[i];
				Annotation[] annotations = parameter.getAnnotations();
				if (annotations.length == 0) {
					continue;
				}
				String name = getAnnotationName(parameter, annotations);
				if (StringUtils.isBlank(name)) {
					continue;
				}
				Object value = args[i];
				paramsValue.put(name, value);
			}
			LogInfo logInfo = new LogInfo();
			logInfo.setMethodName(methodName);
			logInfo.setValue(paramsValue);
			String jsonValue = JsonUtils.nullConvertToJsonStr(logInfo);
			logger.debug("logInfo=>" + jsonValue);
		} catch (Exception e) {
			logger.warn("get logInfo fail");
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
				QueryParam formParam = (QueryParam) annotation;
				name = formParam.value();
				break;
			}
		}
		return name;
	}
}
