package com.rabbitframework.security.util;

import org.apache.shiro.util.AntPathMatcher;
import org.junit.Test;

public class AntPathMatherTest {
    private AntPathMatcher antPathMather = new AntPathMatcher();

    @Test
    public void testPathMather() {
        String source = "/login";
        String pattern = "/login/test";
        System.out.println(antPathMather.matchStart(pattern, source));
    }
}
