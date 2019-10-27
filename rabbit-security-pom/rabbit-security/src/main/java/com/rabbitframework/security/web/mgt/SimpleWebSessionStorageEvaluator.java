package com.rabbitframework.security.web.mgt;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.session.mgt.NativeSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.WebSubject;
import org.apache.shiro.web.util.WebUtils;

public class SimpleWebSessionStorageEvaluator extends
		DefaultSessionStorageEvaluator {

	private SessionManager sessionManager;

	void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	/**
	 * Returns {@code true} if session storage is generally available (as
	 * determined by the super class's global configuration property
	 * {@link #isSessionStorageEnabled()} and no request-specific override has
	 * turned off session storage, {@code false} otherwise.
	 * <p/>
	 * This means session storage is disabled if the
	 * {@link #isSessionStorageEnabled()} property is {@code false} or if a
	 * request attribute is discovered that turns off session storage for the
	 * current request.
	 *
	 * @param subject
	 *            the {@code Subject} for which session state persistence may be
	 *            enabled
	 * @return {@code true} if session storage is generally available (as
	 *         determined by the super class's global configuration property
	 *         {@link #isSessionStorageEnabled()} and no request-specific
	 *         override has turned off session storage, {@code false} otherwise.
	 */
	@SuppressWarnings({ "SimplifiableIfStatement" })
	@Override
	public boolean isSessionStorageEnabled(Subject subject) {
		if (subject.getSession(false) != null) {
			// use what already exists
			return true;
		}

		if (!isSessionStorageEnabled()) {
			// honor global setting:
			return false;
		}

		// SHIRO-350: non-web subject instances can't be saved to web-only
		// session managers:
		// since 1.2.1:
		if (!(subject instanceof WebSubject)
				&& (this.sessionManager != null && !(this.sessionManager instanceof NativeSessionManager))) {
			return false;
		}

		return WebUtils._isSessionCreationEnabled(subject);
	}

}
