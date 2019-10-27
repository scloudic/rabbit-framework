package com.rabbitframework.security.web.filter;

import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;

public class SecurityFilterChainManager extends DefaultFilterChainManager {
	@Override
	protected void addDefaultFilters(boolean init) {
		for (SecurityFilter defaultFilter : SecurityFilter.values()) {
			addFilter(defaultFilter.name(), defaultFilter.newInstance(), init, false);
		}
	}
}
