package com.scloudic.rabbitframework.security.mgt;

import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SubjectDAO;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
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
}
