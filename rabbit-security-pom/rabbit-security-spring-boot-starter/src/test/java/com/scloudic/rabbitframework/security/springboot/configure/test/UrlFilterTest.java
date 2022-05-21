package com.scloudic.rabbitframework.security.springboot.configure.test;

import org.apache.shiro.util.PatternMatcher;
import org.apache.shiro.util.RegExPatternMatcher;

public class UrlFilterTest {

    public static void main(String[] args) {
       // String filterUrl = "/(static|css|img|images|lib|res)/*";
        String filterUrl = "/(static|css|img|images|lib|res)/.*(js|jpg)";
        PatternMatcher pathMatcher = new RegExPatternMatcher();
        System.out.println(pathMatcher.matches(filterUrl, "/img/dad/fina.jpg"));
    }
}
