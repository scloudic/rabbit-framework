package com.scloudic.rabbitframework.security.spring;

import com.scloudic.rabbitframework.core.utils.JsonUtils;
import com.scloudic.rabbitframework.core.utils.StringUtils;
import com.scloudic.rabbitframework.security.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SessionCacheManager {
    private static Logger logger = LoggerFactory.getLogger(SessionCacheManager.class);
    public static final String DEF_SESSION_KEY = SessionCacheManager.class.getName() + "_DEF_SESSION_KEY";
    private SessionDAO sessionDAO;

    public Session getSession(Serializable sessionId) {
        return sessionDAO.readSession(sessionId);
    }


    public void delSession() {
        String authorization = SecurityUtils.getSessionId();
        sessionDAO.delete(getSession(authorization));
    }

    public void delSession(Serializable sessionId) {
        sessionDAO.delete(getSession(sessionId));
    }

    public Session getSession() {
        String authorization = SecurityUtils.getSessionId();
        return sessionDAO.readSession(authorization);
    }


    public SessionDAO getSessionDAO() {
        return sessionDAO;
    }

    public void setSessionDAO(SessionDAO sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    public void addValue(String key, String value) {
        String authorization = SecurityUtils.getSessionId();
        addValue(authorization, key, value);
    }

    public void addValue(Serializable sessionId, String key, String value) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
            logger.warn("不能添加会话值数据为空!");
        }
        Session session = getSession(sessionId);
        Object userDef = session.getAttribute(DEF_SESSION_KEY);
        Map<String, String> param = new HashMap<String, String>();
        param.put(key, value);
        if (userDef != null) {
            param.putAll(JsonUtils.getObject(userDef.toString(), Map.class));
        }
        session.setAttribute(DEF_SESSION_KEY, JsonUtils.toJson(param));
        sessionDAO.update(session);
    }

    public <T> T getObject(Class<T> classzz) {
        String authorization = SecurityUtils.getSessionId();
        return getObject(authorization, classzz);
    }

    public <T> T getObject(Serializable sessionId, Class<T> classzz) {
        Session session = getSession(sessionId);
        Object userDef = session.getAttribute(DEF_SESSION_KEY);
        if (userDef == null) {
            return null;
        }
        return JsonUtils.getObject(userDef.toString(), classzz);
    }
}