package com.rabbitframework.security.web.session.mgt;

import java.io.Serializable;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DelegatingSession;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.servlet.ShiroHttpSession;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.apache.shiro.web.session.mgt.WebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tjzq.commons.utils.StringUtils;

public class SimpleWebSessionManager extends SimpleSessionManager implements WebSessionManager {
    private static final Logger logger = LoggerFactory.getLogger(SimpleWebSessionManager.class);
    public static final String DEFAULT_TOKEN = "Authorization";
    private boolean tokenEnabled;
    private Cookie sessionIdCookie;
    private boolean sessionIdCookieEnabled;
    private String tokenName = DEFAULT_TOKEN;

    public SimpleWebSessionManager() {
        Cookie cookie = new SimpleCookie(ShiroHttpSession.DEFAULT_SESSION_ID_NAME);
        cookie.setHttpOnly(true);
        this.sessionIdCookie = cookie;
        this.sessionIdCookieEnabled = true;
        this.tokenEnabled = true;
        setSessionValidationSchedulerEnabled(false);
    }

    /**
     * 创建session
     */
    // @Override
    // protected Session doCreateSession(SessionContext context) {
    // Session session = newSessionInstance(context);
    // if (log.isTraceEnabled()) {
    // log.trace("Creating session for host {}", session.getHost());
    // }
    // // 去掉重复生成sessionId
    //// if (session.getId() == null && session instanceof SimpleSession) {
    //// HttpServletRequest httpRequest = WebUtils.getHttpRequest(context);
    //// String currUrl =
    // WebUtils.getPathWithinApplication(WebUtils.toHttp(httpRequest));
    //// boolean isLoginSubmission = WebUtils.toHttp(httpRequest).getMethod()
    //// .equalsIgnoreCase(AccessControlFilter.POST_METHOD);
    //// if (postLoginUrl != null && postLoginUrl.equals(currUrl) &&
    // isLoginSubmission) {
    //// // ignore
    //// } else {
    //// WebSessionKey webSessionKey = new WebSessionKey(httpRequest,
    // WebUtils.getHttpResponse(context));
    //// Serializable id = super.getSessionId(webSessionKey);
    //// if (id == null && WebUtils.isWeb(webSessionKey)) {
    //// ServletRequest request = WebUtils.getRequest(webSessionKey);
    //// ServletResponse response = WebUtils.getResponse(webSessionKey);
    //// id = getReferencedSessionId(request, response);
    //// if (id != null) {
    //// ((SimpleSession) session).setId(id);
    //// }
    //// }
    //// }
    //// }
    // create(session);
    // return session;
    // }
    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId = getSessionId(sessionKey);
        ServletRequest request = null;
        if (sessionKey instanceof WebSessionKey) {
            request = ((WebSessionKey) sessionKey).getServletRequest();
        }
        if (request != null && null != sessionId) {
            Object sessionObj = request.getAttribute(sessionId.toString());
            if (sessionObj != null) {
                return (Session) sessionObj;
            }
        }
        Session session = super.retrieveSession(sessionKey);
        if (request != null && null != sessionId) {
            request.setAttribute(sessionId.toString(), session);
        }
        return session;
    }

    public Cookie getSessionIdCookie() {
        return sessionIdCookie;
    }

    public void setSessionIdCookie(Cookie sessionIdCookie) {
        this.sessionIdCookie = sessionIdCookie;
    }

    public boolean isSessionIdCookieEnabled() {
        return sessionIdCookieEnabled;
    }

    public void setSessionIdCookieEnabled(boolean sessionIdCookieEnabled) {
        this.sessionIdCookieEnabled = sessionIdCookieEnabled;
    }

    private void storeSessionId(Serializable currentId, HttpServletRequest request, HttpServletResponse response) {
        if (currentId == null) {
            String msg = "sessionId cannot be null when persisting for subsequent requests.";
            throw new IllegalArgumentException(msg);
        }
        Cookie template = getSessionIdCookie();
        Cookie cookie = new SimpleCookie(template);
        String idString = currentId.toString();
        cookie.setValue(idString);
        cookie.saveTo(request, response);
        logger.trace("Set session ID cookie for session with id {}", idString);
    }

    private void removeSessionIdCookie(HttpServletRequest request, HttpServletResponse response) {
        getSessionIdCookie().removeFrom(request, response);
    }

    private String getSessionIdCookieValue(ServletRequest request, ServletResponse response) {
        if (!isSessionIdCookieEnabled()) {
            logger.debug("Session ID cookie is disabled - session id will not be acquired from a request cookie.");
            return null;
        }
        if (!(request instanceof HttpServletRequest)) {
            logger.debug(
                    "Current request is not an HttpServletRequest - cannot get session ID cookie.  Returning null.");
            return null;
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        return getSessionIdCookie().readValue(httpRequest, WebUtils.toHttp(response));
    }

    private Serializable getReferencedSessionId(ServletRequest request, ServletResponse response) {
        String id = null;
        // 是否开启token
        if (isTokenEnabled()) {
            // 优先于从token中取值
            if (StringUtils.isNotBlank(getTokenName())) {
                HttpServletRequest httpServletRequest = (HttpServletRequest) request;
                id = httpServletRequest.getHeader(getTokenName());
                logger.debug("token:" + id);
            }
        }
        if (StringUtils.isBlank(id)) {
            id = getSessionIdCookieValue(request, response);
        }
        if (StringUtils.isNotBlank(id)) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,
                    ShiroHttpServletRequest.COOKIE_SESSION_ID_SOURCE);
        } else {
            id = getUriParamValue(request, ShiroHttpSession.DEFAULT_SESSION_ID_NAME);
            if (StringUtils.isBlank(id)) {
                // not a URI path segment parameter, try the query parameters:
                String name = getSessionIdName();
                id = request.getParameter(name);
                if (StringUtils.isBlank(id)) {
                    // try lowercase:
                    id = request.getParameter(name.toLowerCase());
                }
            }

            if (StringUtils.isNotBlank(id)) {
                request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,
                        ShiroHttpServletRequest.URL_SESSION_ID_SOURCE);
            }
        }
        if (StringUtils.isNotBlank(id)) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            // automatically mark it valid here. If it is invalid, the
            // onUnknownSession method below will be invoked and we'll remove
            // the attribute at that time.
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
        }
        return id;
    }

    private String getUriParamValue(ServletRequest servletRequest, String paramName) {
        if (!(servletRequest instanceof HttpServletRequest)) {
            return null;
        }
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String value = request.getParameter(paramName);
        return value;
    }

    // since 1.2.1
    private String getSessionIdName() {
        String name = this.sessionIdCookie != null ? this.sessionIdCookie.getName() : null;
        if (name == null) {
            name = ShiroHttpSession.DEFAULT_SESSION_ID_NAME;
        }
        return name;
    }

    protected Session createExposedSession(Session session, SessionContext context) {
        if (!WebUtils.isWeb(context)) {
            return super.createExposedSession(session, context);
        }
        ServletRequest request = WebUtils.getRequest(context);
        ServletResponse response = WebUtils.getResponse(context);
        SessionKey key = new WebSessionKey(session.getId(), request, response);
        return new DelegatingSession(this, key);
    }

    protected Session createExposedSession(Session session, SessionKey key) {
        if (!WebUtils.isWeb(key)) {
            return super.createExposedSession(session, key);
        }

        ServletRequest request = WebUtils.getRequest(key);
        ServletResponse response = WebUtils.getResponse(key);
        SessionKey sessionKey = new WebSessionKey(session.getId(), request, response);
        return new DelegatingSession(this, sessionKey);
    }

    /**
     * Stores the Session's ID, usually as a Cookie, to associate with future
     * requests.
     *
     * @param session the session that was just {@link #createSession created}.
     */
    @Override
    protected void onStart(Session session, SessionContext context) {
        super.onStart(session, context);

        if (!WebUtils.isHttp(context)) {
            logger.debug("SessionContext argument is not HTTP compatible or does not have an HTTP request/response "
                    + "pair. No session ID cookie will be set.");
            return;

        }
        HttpServletRequest request = WebUtils.getHttpRequest(context);
        HttpServletResponse response = WebUtils.getHttpResponse(context);

        if (isSessionIdCookieEnabled()) {
            Serializable sessionId = session.getId();
            if (sessionId != null) {
                storeSessionId(sessionId, request, response);
            }
        } else {
            logger.debug("Session ID cookie is disabled.  No cookie has been set for new session with id {}",
                    session.getId());
        }

        request.removeAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE);
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_IS_NEW, Boolean.TRUE);
    }

    @Override
    public Serializable getSessionId(SessionKey key) {
        Serializable id = super.getSessionId(key);
        if (id == null && WebUtils.isWeb(key)) {
            ServletRequest request = WebUtils.getRequest(key);
            ServletResponse response = WebUtils.getResponse(key);
            id = getSessionId(request, response);
        }
        return id;
    }

    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        return getReferencedSessionId(request, response);
    }

    @Override
    protected void onExpiration(Session s, ExpiredSessionException ese, SessionKey key) {
        super.onExpiration(s, ese, key);
        onInvalidation(key);
    }

    @Override
    protected void onInvalidation(Session session, InvalidSessionException ise, SessionKey key) {
        super.onInvalidation(session, ise, key);
        onInvalidation(key);
    }

    private void onInvalidation(SessionKey key) {
        ServletRequest request = WebUtils.getRequest(key);
        if (request != null) {
            request.removeAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID);
        }
        if (WebUtils.isHttp(key)) {
            logger.debug("Referenced session was invalid.  Removing session ID cookie.");
            removeSessionIdCookie(WebUtils.getHttpRequest(key), WebUtils.getHttpResponse(key));
        } else {
            logger.debug("SessionKey argument is not HTTP compatible or does not have an HTTP request/response "
                    + "pair. Session ID cookie will not be removed due to invalidated session.");
        }
    }

    @Override
    protected void onStop(Session session, SessionKey key) {
        super.onStop(session, key);
        if (WebUtils.isHttp(key)) {
            HttpServletRequest request = WebUtils.getHttpRequest(key);
            HttpServletResponse response = WebUtils.getHttpResponse(key);
            logger.debug("Session has been stopped (subject logout or explicit stop).  Removing session ID cookie.");
            removeSessionIdCookie(request, response);
        } else {
            logger.debug("SessionKey argument is not HTTP compatible or does not have an HTTP request/response "
                    + "pair. Session ID cookie will not be removed due to stopped session.");
        }
    }

    /**
     * This is a native session manager implementation, so this method returns
     * {@code false} always.
     *
     * @return {@code false} always
     * @since 1.2
     */
    public boolean isServletContainerSessions() {
        return false;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public boolean isTokenEnabled() {
        return tokenEnabled;
    }

    public void setTokenEnabled(boolean tokenEnabled) {
        this.tokenEnabled = tokenEnabled;
    }
}
