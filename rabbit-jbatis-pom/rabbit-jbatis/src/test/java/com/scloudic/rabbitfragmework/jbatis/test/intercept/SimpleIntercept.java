package com.scloudic.rabbitfragmework.jbatis.test.intercept;

import com.scloudic.rabbitframework.jbatis.annontations.Intercept;
import com.scloudic.rabbitframework.jbatis.intercept.Interceptor;
import com.scloudic.rabbitframework.jbatis.intercept.Invocation;
import com.scloudic.rabbitframework.jbatis.intercept.Plugin;

@Intercept(args = { String.class }, method = "print", interfaceType = SimplePrintInteface.class)
public class SimpleIntercept implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		System.out.println("SimpleIntercept");
		return invocation.process();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}
}
