package com.rabbitframework.security.web.session;

import com.rabbitframework.security.web.servlet.SecurityWebCookie;
import com.rabbitframework.core.utils.StringUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionContext;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DelegatingSubject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.servlet.ShiroHttpSession;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.apache.shiro.web.util.SavedRequest;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * web会话管理,默认开启cookie、关闭uri后缀显示、开启token机制
 *
 * @author justin
 * @since 3.3.1
 */
public class SecurityWebSessionManager extends DefaultWebSessionManager {
    public static final String DEFAULT_TOKEN = "Authorization";
    private boolean tokenEnabled;
    private String tokenName = DEFAULT_TOKEN;

    public SecurityWebSessionManager() {
        Cookie cookie = new SecurityWebCookie(ShiroHttpSession.DEFAULT_SESSION_ID_NAME);
        cookie.setHttpOnly(true);
        setSessionIdCookie(cookie);
        setSessionIdCookieEnabled(true);
        setSessionIdUrlRewritingEnabled(false);
        tokenEnabled = true;
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String id = "";
        if (isTokenEnabled()) {
            // 优先于从token中取值
            if (StringUtils.isNotBlank(getTokenName())) {
                HttpServletRequest httpServletRequest = (HttpServletRequest) request;
                id = httpServletRequest.getHeader(getTokenName());
            }
        }

        if (StringUtils.isNotBlank(id)) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,
                    ShiroHttpServletRequest.URL_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            request.setAttribute(ShiroHttpServletRequest.SESSION_ID_URL_REWRITING_ENABLED, isSessionIdUrlRewritingEnabled());
            return id;
        }
        return super.getSessionId(request, response);
    }

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
        return super.retrieveSession(sessionKey);
    }

    @Override
    public void setAttribute(SessionKey sessionKey,
                             Object attributeKey, Object value) throws InvalidSessionException {
        try {
            super.setAttribute(sessionKey, attributeKey, value);
        } catch (UnknownSessionException e) {
            Subject subject = ThreadContext.getSubject();
            if ((value instanceof PrincipalCollection) && getSessionDAO() != null
                    && subject != null && (subject instanceof DelegatingSubject)) {
                try {
                    SessionContext sessionContext = createSessionContext(((DelegatingSubject) subject).getHost());
                    Session session = newSessionInstance(sessionContext);
                    session.setAttribute(attributeKey, value);
                    session.setTimeout(getGlobalSessionTimeout());
                    ((SimpleSession) session).setId(sessionKey.getSessionId());
                    getSessionDAO().create(session);
                } catch (Exception ex) {
                    throw ex;
                }
            } else {
                if (!(value instanceof SavedRequest)) {
                    throw e;
                }
            }
        }
    }

    private SessionContext createSessionContext(String host) {
        SessionContext sessionContext = new DefaultSessionContext();
        if (StringUtils.hasText(host)) {
            sessionContext.setHost(host);
        }
        return sessionContext;
    }

    public boolean isTokenEnabled() {
        return tokenEnabled;
    }

    public void setTokenEnabled(boolean tokenEnabled) {
        this.tokenEnabled = tokenEnabled;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }
}
