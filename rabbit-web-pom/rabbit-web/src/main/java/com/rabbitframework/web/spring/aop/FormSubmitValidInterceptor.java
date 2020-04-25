package com.rabbitframework.web.spring.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.ws.rs.BeanParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.QueryParam;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitframework.commons.utils.StatusCode;
import com.rabbitframework.web.DataJsonResponse;
import com.rabbitframework.web.annotations.FormValid;
import com.rabbitframework.web.utils.FieldError;
import com.rabbitframework.web.utils.ResponseUtils;
import com.rabbitframework.web.utils.ValidationUtils;
import com.tjzq.commons.utils.JsonUtils;
import com.tjzq.commons.utils.StringUtils;

@Aspect
public class FormSubmitValidInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(FormSubmitValidInterceptor.class);
	private static final String pointCupExpression = "execution(@com.rabbitframework.web.annotations.FormValid * *(..))";

	@Pointcut(pointCupExpression)
	public void formAnnotatedMethod() {
	}

	@Around("formAnnotatedMethod()")
	public Object doInterceptor(ProceedingJoinPoint pjp) throws Throwable {
		Object[] args = pjp.getArgs();
		if (args.length == 0) {// 参数为空则不拦截
			return pjp.proceed();
		}
		MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
		Method method = methodSignature.getMethod();
		FormValid formValid = method.getAnnotation(FormValid.class);
		String[] fieldFilter = formValid.fieldFilter();
		Parameter[] parameters = method.getParameters();
		List<Object> beanParams = new ArrayList<Object>();
		int parameterLength = parameters.length;
		Map<String, NotBlank> paramMap = new HashMap<String, NotBlank>();
		for (int i = 0; i < parameterLength; i++) {
			Parameter parameter = parameters[i];
			Annotation[] annotations = parameter.getAnnotations();
			if (annotations.length == 0) {
				continue;
			}
			String beanName = getAnnotationName(parameter, annotations, paramMap, args[i]);
			if (StringUtils.isNotBlank(beanName)) {
				beanParams.add(args[i]);
			}
		}
		List<FieldError> fieldErrors = new ArrayList<FieldError>();
		if (beanParams.size() > 0) {
			for (Object beanParam : beanParams) {
				fieldErrors.addAll(ValidationUtils.validateEntity(beanParam, fieldFilter));
			}
		}
		for (Map.Entry<String, NotBlank> entry : paramMap.entrySet()) {
			String fieldName = entry.getKey();
			NotBlank notBlank = entry.getValue();
			FieldError fieldError = ValidationUtils.getFieldError(fieldName, notBlank.message());
			fieldErrors.add(fieldError);
		}
		if (fieldErrors.size() > 0) {
			DataJsonResponse result = new DataJsonResponse();
			result.setStatus(StatusCode.SC_VALID_ERROR);
			result.setMessage(JsonUtils.toJson(fieldErrors));
			// result.setMessage(ServletContextHelper.getMessage("fail"));
			// result.setData(fieldErrors);
			String resultJson = result.toJson();
			logger.debug("resultJson:" + resultJson);
			return ResponseUtils.ok(resultJson);
		}
		return pjp.proceed();
	}

	private String getAnnotationName(Parameter parameter, Annotation[] annotations, Map<String, NotBlank> map,
			Object value) {
		String resultValue = "";
		if (value != null) {
			resultValue = value.toString();
		}
		int annotationsLength = annotations.length;
		String name = null;
		NotBlank notBlank = parameter.getAnnotation(NotBlank.class);
		for (int i = 0; i < annotationsLength; i++) {
			Annotation annotation = annotations[i];
			if (annotation instanceof BeanParam) {
				name = parameter.getType().getSimpleName().toLowerCase(Locale.ENGLISH);
				break;
			}
			if (annotation instanceof FormParam) {
				FormParam formParam = (FormParam) annotation;
				if (notBlank != null && StringUtils.isBlank(resultValue)) {
					map.put(formParam.value(), notBlank);
				}
				break;
			}
			if (annotation instanceof QueryParam) {
				QueryParam queryParam = (QueryParam) annotation;
				if (notBlank != null && StringUtils.isBlank(resultValue)) {
					map.put(queryParam.value(), notBlank);
				}
				break;
			}
			if (annotation instanceof FormDataParam) {
				FormDataParam formDataParam = (FormDataParam) annotation;
				if (notBlank != null && StringUtils.isBlank(resultValue)) {
					map.put(formDataParam.value(), notBlank);
				}
			}
		}
		return name;
	}
}
