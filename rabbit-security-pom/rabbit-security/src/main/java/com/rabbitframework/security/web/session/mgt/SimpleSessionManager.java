package com.rabbitframework.security.web.session.mgt;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.CacheManagerAware;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.AbstractValidatingSessionManager;
import org.apache.shiro.session.mgt.DefaultSessionContext;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.SimpleSessionFactory;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DelegatingSubject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.util.SavedRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleSessionManager extends AbstractValidatingSessionManager implements CacheManagerAware {
	private static final Logger log = LoggerFactory.getLogger(SimpleSessionManager.class);

	private SessionFactory sessionFactory;
	private CacheManager cacheManager;
	private boolean deleteInvalidSessions;

	protected SessionDAO sessionDAO;

	public SimpleSessionManager() {
		this.deleteInvalidSessions = true;
		this.sessionFactory = new SimpleSessionFactory();
		this.sessionDAO = new MemorySessionDAO();
	}

	public void setSessionDAO(SessionDAO sessionDAO) {
		this.sessionDAO = sessionDAO;
		applyCacheManagerToSessionDAO();
	}

	public SessionDAO getSessionDAO() {
		return this.sessionDAO;
	}

	/**
	 * Returns the {@code SessionFactory} used to generate new {@link Session}
	 * instances. The default instance is a {@link SimpleSessionFactory}.
	 *
	 * @return the {@code SessionFactory} used to generate new {@link Session}
	 *         instances.
	 * @since 1.0
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override
	public Object getAttribute(SessionKey sessionKey, Object attributeKey) throws InvalidSessionException {
		Session session = lookupRequiredSession(sessionKey);
		return session.getAttribute(attributeKey);
	}

	private Session lookupSession(SessionKey key) throws SessionException {
		if (key == null) {
			throw new NullPointerException("SessionKey argument cannot be null.");
		}
		return doGetSession(key);
	}

	private Session lookupRequiredSession(SessionKey key) throws SessionException {
		Session session = lookupSession(key);
		if (session == null) {
			String msg = "Unable to locate required Session instance based on SessionKey [" + key + "].";
			throw new UnknownSessionException(msg);
		}
		return session;
	}

	@Override
	public void setAttribute(SessionKey sessionKey, Object attributeKey, Object value) throws InvalidSessionException {
		try {
			super.setAttribute(sessionKey, attributeKey, value);
		} catch (UnknownSessionException e) {
			Subject subject = ThreadContext.getSubject();
			if ((value instanceof PrincipalCollection) && sessionDAO != null && subject != null
					&& (subject instanceof DelegatingSubject)) {
				try {
					SessionContext sessionContext = createSessionContext(((DelegatingSubject) subject).getHost());
					Session session = newSessionInstance(sessionContext);
					session.setAttribute(attributeKey, value);
					session.setTimeout(getGlobalSessionTimeout());
					((SimpleSession) session).setId(sessionKey.getSessionId());
					sessionDAO.create(session);
				} catch (Exception ex) {
					throw e;
				}
			} else {
				if (!(value instanceof SavedRequest)) {
					throw e;
				}
			}
		}
	}

	protected SessionContext createSessionContext(String host) {
		SessionContext sessionContext = new DefaultSessionContext();
		if (StringUtils.hasText(host)) {
			sessionContext.setHost(host);
		}
		return sessionContext;
	}

	/**
	 * Sets the {@code SessionFactory} used to generate new {@link Session}
	 * instances. The default instance is a {@link SimpleSessionFactory}.
	 *
	 * @param sessionFactory
	 *            the {@code SessionFactory} used to generate new
	 *            {@link Session} instances.
	 * @since 1.0
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Returns {@code true} if sessions should be automatically deleted after
	 * they are discovered to be invalid, {@code false} if invalid sessions will
	 * be manually deleted by some process external to Shiro's control. The
	 * default is {@code true} to ensure no orphans exist in the underlying data
	 * store.
	 * <h4>Usage</h4> It is ok to set this to {@code false} <b>
	 * <em>ONLY</em></b> if you have some other process that you manage yourself
	 * that periodically deletes invalid sessions from the backing data store
	 * over time, such as via a Quartz or Cron job. If you do not do this, the
	 * invalid sessions will become 'orphans' and fill up the data store over
	 * time.
	 * <p/>
	 * This property is provided because some systems need the ability to
	 * perform querying/reporting against sessions in the data store, even after
	 * they have stopped or expired. Setting this attribute to {@code false}
	 * will allow such querying, but with the caveat that the application
	 * developer/configurer deletes the sessions themselves by some other means
	 * (cron, quartz, etc).
	 *
	 * @return {@code true} if sessions should be automatically deleted after
	 *         they are discovered to be invalid, {@code false} if invalid
	 *         sessions will be manually deleted by some process external to
	 *         Shiro's control.
	 * @since 1.0
	 */
	public boolean isDeleteInvalidSessions() {
		return deleteInvalidSessions;
	}

	/**
	 * Sets whether or not sessions should be automatically deleted after they
	 * are discovered to be invalid. Default value is {@code true} to ensure no
	 * orphans will exist in the underlying data store.
	 * <h4>WARNING</h4> Only set this value to {@code false} if you are manually
	 * going to delete sessions yourself by some process (quartz, cron, etc)
	 * external to Shiro's control. See the {@link #isDeleteInvalidSessions()
	 * isDeleteInvalidSessions()} JavaDoc for more.
	 *
	 * @param deleteInvalidSessions
	 *            whether or not sessions should be automatically deleted after
	 *            they are discovered to be invalid.
	 * @since 1.0
	 */
	public void setDeleteInvalidSessions(boolean deleteInvalidSessions) {
		this.deleteInvalidSessions = deleteInvalidSessions;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
		applyCacheManagerToSessionDAO();
	}

	/**
	 * Sets the internal {@code CacheManager} on the {@code SessionDAO} if it
	 * implements the {@link org.apache.shiro.cache.CacheManagerAware
	 * CacheManagerAware} interface.
	 * <p/>
	 * This method is called after setting a cacheManager via the
	 * {@link #setCacheManager(org.apache.shiro.cache.CacheManager)
	 * setCacheManager} method <em>em</em> when setting a {@code SessionDAO} via
	 * the {@link #setSessionDAO} method to allow it to be propagated in either
	 * case.
	 *
	 * @since 1.0
	 */
	private void applyCacheManagerToSessionDAO() {
		if (this.cacheManager != null && this.sessionDAO != null && this.sessionDAO instanceof CacheManagerAware) {
			((CacheManagerAware) this.sessionDAO).setCacheManager(this.cacheManager);
		}
	}

	protected Session doCreateSession(SessionContext context) {
		Session s = newSessionInstance(context);
		if (log.isTraceEnabled()) {
			log.trace("Creating session for host {}", s.getHost());
		}
		create(s);
		return s;
	}

	protected Session newSessionInstance(SessionContext context) {
		return getSessionFactory().createSession(context);
	}

	/**
	 * Persists the given session instance to an underlying EIS (Enterprise
	 * Information System). This implementation delegates and calls <code>this.
	 * {@link SessionDAO sessionDAO}.
	 * {@link SessionDAO#create(org.apache.shiro.session.Session) create}
	 * (session);<code>
	 *
	 * @param session
	 *            the Session instance to persist to the underlying EIS.
	 */
	protected void create(Session session) {
		if (log.isDebugEnabled()) {
			log.debug("Creating new EIS record for new session instance [" + session + "]");
		}
		sessionDAO.create(session);
	}

	@Override
	protected void onStop(Session session) {
		if (session instanceof SimpleSession) {
			SimpleSession ss = (SimpleSession) session;
			Date stopTs = ss.getStopTimestamp();
			ss.setLastAccessTime(stopTs);
		}
		onChange(session);
	}

	@Override
	protected void afterStopped(Session session) {
		if (isDeleteInvalidSessions()) {
			delete(session);
		}
	}

	protected void onExpiration(Session session) {
		if (session instanceof SimpleSession) {
			((SimpleSession) session).setExpired(true);
		}
		onChange(session);
	}

	@Override
	protected void afterExpired(Session session) {
		if (isDeleteInvalidSessions()) {
			delete(session);
		}
	}

	protected void onChange(Session session) {
		sessionDAO.update(session);
	}

	protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
		Serializable sessionId = getSessionId(sessionKey);
		if (sessionId == null) {
			log.debug("Unable to resolve session ID from SessionKey [{}].  Returning null to indicate a "
					+ "session could not be found.", sessionKey);
			return null;
		}
		Session s = retrieveSessionFromDataSource(sessionId);
		if (s == null) {
			// session ID was provided, meaning one is expected to be found, but
			// we couldn't find one:
			String msg = "Could not find session with ID [" + sessionId + "]";
			throw new UnknownSessionException(msg);
		}
		return s;
	}

	protected Serializable getSessionId(SessionKey sessionKey) {
		return sessionKey.getSessionId();
	}

	protected Session retrieveSessionFromDataSource(Serializable sessionId) throws UnknownSessionException {
		return sessionDAO.readSession(sessionId);
	}

	protected void delete(Session session) {
		sessionDAO.delete(session);
	}

	protected Collection<Session> getActiveSessions() {
		Collection<Session> active = sessionDAO.getActiveSessions();
		return active != null ? active : Collections.<Session> emptySet();
	}

}
