package com.rabbitfragmework.dbase.test.intercept;

import com.rabbitframework.dbase.annontations.Intercept;
import com.rabbitframework.dbase.intercept.Interceptor;
import com.rabbitframework.dbase.intercept.Invocation;
import com.rabbitframework.dbase.intercept.Plugin;

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
