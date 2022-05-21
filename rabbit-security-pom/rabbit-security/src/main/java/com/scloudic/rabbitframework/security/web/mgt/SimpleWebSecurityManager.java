package com.scloudic.rabbitframework.security.web.mgt;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * 基于WEB应用的安全管理器,继承{@link DefaultWebSecurityManager}
 *
 * @author justin
 * @since 3.3.1
 */
public class SimpleWebSecurityManager extends DefaultWebSecurityManager {
    private static final Logger logger = LoggerFactory.getLogger(SimpleWebSecurityManager.class);

    public SimpleWebSecurityManager() {
        super();
    }

    public SimpleWebSecurityManager(Realm singleRealm) {
        this();
    }

    public SimpleWebSecurityManager(Collection<Realm> realms) {
        this();
    }

    @Override
    protected SubjectContext resolveSession(SubjectContext context) {
        if (context.resolveSession() != null) {
            logger.debug("Context already contains a session.  Returning.");
            return context;
        }
        try {
            //Context couldn't resolve it directly, let's see if we can since we have direct access to
            //the session manager:
            Session session = resolveContextSession(context);
            if (session != null) {
                context.setSession(session);
            }
        } catch (InvalidSessionException e) {
            logger.debug("Resolved SubjectContext context session is invalid.  Ignoring and creating an anonymous " +
                    "(session-less) Subject instance.", e.getMessage());
        }
        return context;
    }
}