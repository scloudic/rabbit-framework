package com.rabbitframework.security.web.session.mgt;

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractRabbitSessionDAO extends AbstractSessionDAO {
	private static Logger logger = LoggerFactory.getLogger(AbstractRabbitSessionDAO.class);

	@Override
	public void update(Session session) throws UnknownSessionException {
		PrincipalCollection existingPrincipals = (PrincipalCollection) session
				.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
		if (existingPrincipals == null || CollectionUtils.isEmpty(existingPrincipals)) {
			logger.error("PrincipalCollection is null");
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
		this.assignSessionId(session, sessionId);
		logger.info("RedisSessionDAO:doCreate=>" + sessionId);
		PrincipalCollection existingPrincipals = (PrincipalCollection) session
				.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
		if (existingPrincipals == null || CollectionUtils.isEmpty(existingPrincipals)) {
			logger.error("PrincipalCollection is null");
			return sessionId;
		}
		doSave(session);
		return sessionId;
	}

	public abstract void doSave(Session session) throws UnknownSessionException;

	public abstract void doUpdate(Session session) throws UnknownSessionException;

}
