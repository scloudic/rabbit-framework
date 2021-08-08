package com.scloudic.rabbitframework.security.web.filter.mgt;

import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;

public class SecurityFilterChainManager extends DefaultFilterChainManager {
	@Override
	protected void addDefaultFilters(boolean init) {
		for (SecurityFilter defaultFilter : SecurityFilter.values()) {
			addFilter(defaultFilter.name(), defaultFilter.newInstance(), init, false);
		}
	}
}
