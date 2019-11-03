package com.rabbitfragmework.dbase.test.intercept;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitframework.jade.intercept.InterceptorChain;

import junit.framework.TestCase;

public class InterceptTest extends TestCase {
	private static final Logger logger = LoggerFactory.getLogger(InterceptTest.class);

	public void testIntercept() {
		InterceptorChain interceptorChain = new InterceptorChain();
		SimpleIntercept simpleIntercept = new SimpleIntercept();
		SimpleIntercept2 simpleIntercept2 = new SimpleIntercept2();
		interceptorChain.addInterceptor(simpleIntercept);
		interceptorChain.addInterceptor(simpleIntercept2);
		SimplePrintInteface simplePrint = new SimplePrint();
		SimplePrintInteface print = (SimplePrintInteface) interceptorChain
				.pluginAll(simplePrint);
		print.print("testIntercept");
	}
}