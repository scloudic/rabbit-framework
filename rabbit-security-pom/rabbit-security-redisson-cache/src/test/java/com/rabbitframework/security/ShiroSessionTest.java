package com.rabbitframework.security;

import com.rabbitframework.security.cache.redisson.RedisSessionDAO;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * shiroSession用户缓存测试
 *
 * @author justin.liang
 */
public class ShiroSessionTest {
    private static final Logger logger = LoggerFactory.getLogger(ShiroSessionTest.class);

    private RedisSessionDAO redisSessionDAO;


    public void testGetSessionData() {
        String key = "security_redis_cache:session:04e3422c-33fb-4801-b3d8-6ab15b04a813";
        Session session = redisSessionDAO.readSession(key);
        SimpleSession value = (SimpleSession) session;
        Collection<Object> collection = value.getAttributeKeys();
//
//        org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY
//        Iterator<Object> it = collection.iterator();
//        while (it.hasNext()) {
//            logger.info(it.next().toString());
//        }
        Object authencated = value.getAttribute("org.apache.shiro.subject.support.DefaultSubjectContext_AUTHENTICATED_SESSION_KEY");
        SimplePrincipalCollection principals = (SimplePrincipalCollection) value.getAttribute("org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY");
        SecurityUser securityUser = (SecurityUser) principals.getPrimaryPrincipal();
        logger.info("principals:" + securityUser);
        logger.info("authencated:" + authencated);
    }

}
