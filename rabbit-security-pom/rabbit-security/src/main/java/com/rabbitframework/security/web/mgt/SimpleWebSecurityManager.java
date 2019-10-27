package com.rabbitframework.security.web.mgt;

import java.io.Serializable;
import java.util.Collection;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.mgt.SubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionContext;
import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.apache.shiro.web.session.mgt.WebSessionManager;
import org.apache.shiro.web.subject.WebSubject;
import org.apache.shiro.web.subject.WebSubjectContext;
import org.apache.shiro.web.subject.support.DefaultWebSubjectContext;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitframework.security.mgt.SubjectDAOImpl;

public class SimpleWebSecurityManager extends DefaultSecurityManager implements WebSecurityManager {
	private static final Logger log = LoggerFactory.getLogger(SimpleWebSecurityManager.class);
	private boolean sessionStorageEnabled = true;

	public SimpleWebSecurityManager() {
		super();
		super.setSubjectDAO(new SubjectDAOImpl());
		((DefaultSubjectDAO) this.subjectDAO).setSessionStorageEvaluator(new SimpleWebSessionStorageEvaluator());
		setSubjectFactory(new DefaultWebSubjectFactory());
		setRememberMeManager(new CookieRememberMeManager());
		setSessionManager(new ServletContainerSessionManager());
	}

	public SimpleWebSecurityManager(Realm singleRealm) {
		this();
		setRealm(singleRealm);
	}

	public SimpleWebSecurityManager(Collection<Realm> realms) {
		this();
		setRealms(realms);
	}

	@Override
	protected SubjectContext createSubjectContext() {
		return new DefaultWebSubjectContext();
	}

	@Override
	public void setSubjectDAO(SubjectDAO subjectDAO) {
		super.setSubjectDAO(subjectDAO);
		applySessionManagerToSessionStorageEvaluatorIfPossible();
	}

	@Override
	protected void afterSessionManagerSet() {
		super.afterSessionManagerSet();
		applySessionManagerToSessionStorageEvaluatorIfPossible();
	}

	private void applySessionManagerToSessionStorageEvaluatorIfPossible() {
		SubjectDAO subjectDAO = getSubjectDAO();
		if (subjectDAO instanceof DefaultSubjectDAO) {
			SessionStorageEvaluator evaluator = ((DefaultSubjectDAO) subjectDAO).getSessionStorageEvaluator();
			if (evaluator instanceof SimpleWebSessionStorageEvaluator) {
				((SimpleWebSessionStorageEvaluator) evaluator).setSessionManager(getSessionManager());
			}
		}
	}

	@Override
	protected SubjectContext copy(SubjectContext subjectContext) {
		if (subjectContext instanceof WebSubjectContext) {
			return new DefaultWebSubjectContext((WebSubjectContext) subjectContext);
		}
		return super.copy(subjectContext);
	}

	@Override
	public void setSessionManager(SessionManager sessionManager) {
		if (sessionManager != null && !(sessionManager instanceof WebSessionManager)) {
			if (log.isWarnEnabled()) {
				String msg = "The " + getClass().getName() + " implementation expects SessionManager instances "
						+ "that implement the " + WebSessionManager.class.getName() + " interface.  The "
						+ "configured instance is of type [" + sessionManager.getClass().getName() + "] which does not "
						+ "implement this interface..  This may cause unexpected behavior.";
				log.warn(msg);
			}
		}
		setInternalSessionManager(sessionManager);
	}

	private void setInternalSessionManager(SessionManager sessionManager) {
		super.setSessionManager(sessionManager);
	}

	public boolean isHttpSessionMode() {
		SessionManager sessionManager = getSessionManager();
		return sessionManager instanceof WebSessionManager
				&& ((WebSessionManager) sessionManager).isServletContainerSessions();
	}

	@Override
	protected SessionContext createSessionContext(SubjectContext subjectContext) {
		SessionContext sessionContext = super.createSessionContext(subjectContext);
		if (subjectContext instanceof WebSubjectContext) {
			WebSubjectContext wsc = (WebSubjectContext) subjectContext;
			ServletRequest request = wsc.resolveServletRequest();
			ServletResponse response = wsc.resolveServletResponse();
			DefaultWebSessionContext webSessionContext = new DefaultWebSessionContext(sessionContext);
			if (request != null) {
				webSessionContext.setServletRequest(request);
			}
			if (response != null) {
				webSessionContext.setServletResponse(response);
			}

			sessionContext = webSessionContext;
		}
		return sessionContext;
	}

	@Override
	protected SessionKey getSessionKey(SubjectContext context) {
		if (WebUtils.isWeb(context)) {
			Serializable sessionId = context.getSessionId();
			ServletRequest request = WebUtils.getRequest(context);
			ServletResponse response = WebUtils.getResponse(context);
			return new WebSessionKey(sessionId, request, response);
		} else {
			return super.getSessionKey(context);

		}
	}

	@Override
	protected void beforeLogout(Subject subject) {
		super.beforeLogout(subject);
		removeRequestIdentity(subject);
	}

	protected void removeRequestIdentity(Subject subject) {
		if (subject instanceof WebSubject) {
			WebSubject webSubject = (WebSubject) subject;
			ServletRequest request = webSubject.getServletRequest();
			if (request != null) {
				request.setAttribute(ShiroHttpServletRequest.IDENTITY_REMOVED_KEY, Boolean.TRUE);
			}
		}
	}

	public void setSessionStorageEnabled(boolean sessionStorageEnabled) {
		this.sessionStorageEnabled = sessionStorageEnabled;
		DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = (DefaultSessionStorageEvaluator) ((DefaultSubjectDAO) this.subjectDAO)
				.getSessionStorageEvaluator();
		defaultSessionStorageEvaluator.setSessionStorageEnabled(sessionStorageEnabled);
	}
}