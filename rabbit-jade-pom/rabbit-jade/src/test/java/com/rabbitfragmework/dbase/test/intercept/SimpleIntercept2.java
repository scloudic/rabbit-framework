package com.rabbitfragmework.dbase.test.intercept;

import com.rabbitframework.jade.annontations.Intercept;
import com.rabbitframework.jade.intercept.Interceptor;
import com.rabbitframework.jade.intercept.Invocation;
import com.rabbitframework.jade.intercept.Plugin;

@Intercept(args = { String.class }, method = "print", interfaceType = SimplePrintInteface.class)
public class SimpleIntercept2 implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		System.out.println("SimpleIntercept2");
		return invocation.process();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}
}
