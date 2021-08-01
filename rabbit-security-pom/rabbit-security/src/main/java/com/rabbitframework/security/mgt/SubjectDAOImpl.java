package com.rabbitframework.security.mgt;

import java.lang.reflect.Field;

import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SubjectDAO;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.subject.support.DelegatingSubject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;

/**
 * {@link SubjectDAO} 实现类,覆盖
 * {@link DefaultSubjectDAO#mergePrincipals(Subject subject)}方法 为解决应用请求时多次重复性修改
 * {@link Session}
 *
 * @author juyang.liang
 */
public class SubjectDAOImpl extends DefaultSubjectDAO {
    public SubjectDAOImpl() {
        DefaultWebSessionStorageEvaluator webEvalutator = new DefaultWebSessionStorageEvaluator();
        setSessionStorageEvaluator(webEvalutator);
    }

    @Override
    protected void mergePrincipals(Subject subject) {
        PrincipalCollection currentPrincipals = null;
        if (subject.isRunAs() && subject instanceof DelegatingSubject) {
            try {
                Field field = DelegatingSubject.class
                        .getDeclaredField("principals");
                field.setAccessible(true);
                currentPrincipals = (PrincipalCollection) field.get(subject);
            } catch (Exception e) {
                throw new IllegalStateException(
                        "Unable to access DelegatingSubject principals property.",
                        e);
            }
        }
        if (currentPrincipals == null || currentPrincipals.isEmpty()) {
            currentPrincipals = subject.getPrincipals();
        }
        Session session = subject.getSession(false);
        if (session == null) {
            if (!CollectionUtils.isEmpty(currentPrincipals)) {
                session = subject.getSession();
                session.setAttribute(
                        DefaultSubjectContext.PRINCIPALS_SESSION_KEY,
                        currentPrincipals);
            }
        } else {
            PrincipalCollection existingPrincipals = (PrincipalCollection) session
                    .getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (CollectionUtils.isEmpty(currentPrincipals)) {
                if (!CollectionUtils.isEmpty(existingPrincipals)) {
                    session.removeAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                }
            } else {
                if (!existingPrincipals.isEmpty()) {
                    String existingPrincipalsStr = existingPrincipals
                            .toString();
                    String currentPrincipalsStr = currentPrincipals.toString();
                    if (!currentPrincipalsStr.equals(existingPrincipalsStr)) {
                        session.setAttribute(
                                DefaultSubjectContext.PRINCIPALS_SESSION_KEY,
                                currentPrincipals);
                    }
                } else if (!currentPrincipals.equals(existingPrincipals)) {
                    session.setAttribute(
                            DefaultSubjectContext.PRINCIPALS_SESSION_KEY,
                            currentPrincipals);
                }
            }
        }
    }
}
