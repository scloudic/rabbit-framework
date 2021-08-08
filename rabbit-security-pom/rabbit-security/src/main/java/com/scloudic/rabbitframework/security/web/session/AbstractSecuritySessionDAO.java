package com.scloudic.rabbitframework.security.web.session;

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSecuritySessionDAO extends AbstractSessionDAO {
    private static Logger logger = LoggerFactory.getLogger(AbstractSecuritySessionDAO.class);

    @Override
    public void update(Session session) throws UnknownSessionException {
        PrincipalCollection existingPrincipals = (PrincipalCollection) session
                .getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        if (existingPrincipals == null || CollectionUtils.isEmpty(existingPrincipals)) {
            Serializable sessionId = session.getId();
            logger.error("PrincipalCollection is null,update sessionId is " + sessionId);
            return;
        }
        doUpdate(session);
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = session.getId();
        if (sessionId == null) {
            sessionId = this.generateSessionId(session);
        }
        assignSessionId(session, sessionId);
        PrincipalCollection existingPrincipals = (PrincipalCollection) session
                .getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        if (existingPrincipals == null || CollectionUtils.isEmpty(existingPrincipals)) {
            logger.error("PrincipalCollection is null,doCreate sessionId is " + sessionId);
            return sessionId;
        }
        logger.debug("RedisSessionDAO:doCreate=>" + sessionId);
        doSave(session);
        return sessionId;
    }

    public abstract void doSave(Session session) throws UnknownSessionException;

    public abstract void doUpdate(Session session) throws UnknownSessionException;

    public abstract void doDelete(String userId, String keyPrefix) throws UnknownSessionException;

}
