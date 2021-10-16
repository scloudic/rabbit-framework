package com.scloudic.rabbitframework.security.springboot.configure.test;

import com.scloudic.rabbitframework.security.web.filter.authc.FormAuthcFilter;
import org.springframework.stereotype.Component;

@Component("testAuthcFilter")
public class TestAuthcFilter extends FormAuthcFilter {
}
