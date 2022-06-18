package com.scloudic.rabbitframework.redisson.aop;

import com.scloudic.rabbitframework.core.annotations.LockValue;
import com.scloudic.rabbitframework.core.annotations.RedisLock;
import com.scloudic.rabbitframework.core.exceptions.BizException;
import com.scloudic.rabbitframework.core.reflect.MetaObject;
import com.scloudic.rabbitframework.core.reflect.MetaObjectUtils;
import com.scloudic.rabbitframework.core.utils.StringUtils;
import com.scloudic.rabbitframework.redisson.RedisCache;
import com.scloudic.rabbitframework.redisson.RedisException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Order(2147483600)
public class RedissonLockInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(RedissonLockInterceptor.class);
    private static final String pointCupExpression = "execution(@com.scloudic.rabbitframework.core.annotations.RedisLock * *(..))";
    private RedisCache redisCache;

    @Pointcut(pointCupExpression)
    public void formAnnotatedMethod() {
    }

    @Around("formAnnotatedMethod()")
    public Object doInterceptor(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        RedisLock redisLock = method.getAnnotation(RedisLock.class);
        Parameter[] parameters = method.getParameters();
        int parameterLength = parameters.length;
        String value = "";
        for (int i = 0; i < parameterLength; i++) {
            Parameter parameter = parameters[i];
            Object arg = args[i];
            LockValue lockValue = parameter.getAnnotation(LockValue.class);
            if (lockValue != null) {
                if (lockValue.isObject()) {
                    MetaObject metaObject = MetaObjectUtils.forObject(arg);
                    value = metaObject.getValue(lockValue.keyName()).toString();
                } else {
                    value = arg.toString();
                }
                break;
            }
        }
        String key = redisLock.key();
        if (StringUtils.isNotBlank(value)) {
            key = key + ":" + value;
        }
        boolean isSuccess = false;
        if (redisLock.leaseTime() != -1) {
            isSuccess = redisCache.tryLock(key, redisLock.waitTime(), redisLock.leaseTime());
        } else {
            isSuccess = redisCache.tryLock(key, redisLock.waitTime());
        }
        if (isSuccess) {
            logger.debug("redissonLock加锁成功");
            try {
                return pjp.proceed();
            } catch (Exception e) {
                throw e;
            } finally {
                logger.debug("redissonLock锁释放");
                if (isSuccess) {
                    redisCache.unLock(key);
                }
            }
        } else {
            logger.warn("redissonLock获了加锁失败");
            throw new RedisException(redisLock.exceptionMsg());
        }
    }

    public void setRedisCache(RedisCache redisCache) {
        this.redisCache = redisCache;
    }
}