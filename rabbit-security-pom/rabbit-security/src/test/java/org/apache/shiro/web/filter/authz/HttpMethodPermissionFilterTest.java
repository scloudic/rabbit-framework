package org.apache.shiro.web.filter.authz;

import org.junit.Test;

public class HttpMethodPermissionFilterTest {
	@Test
	public void testHttpMethod() {
		HttpMethodPermissionFilter filter = new HttpMethodPermissionFilter();
		String[] str = filter.buildPermissions(new String[] { "123" }, "/login");
		for (String string : str) {
			System.out.println(string);
		}
	}
}
