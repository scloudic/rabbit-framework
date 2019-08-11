package org.apache.shiro.authz.permission;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.util.CollectionUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WildcardPermissionTest {
	private Logger logger = LoggerFactory
			.getLogger(WildcardPermissionTest.class);
	protected static final String WILDCARD_TOKEN = "*";
	protected static final String PART_DIVIDER_TOKEN = ":";
	protected static final String SUBPART_DIVIDER_TOKEN = ",";
	protected static final boolean DEFAULT_CASE_SENSITIVE = false;

	@Test
	public void testParts() {
		// 结果需要精准匹配
		WildcardPermission permission = new WildcardPermission(
				"/index/test,/test:/index");
		List<Permission> perminfo = new ArrayList<>();
		perminfo.add(new WildcardPermission("/index"));
		// perminfo.add(new WildcardPermission("/index"));
		for (Permission permission2 : perminfo) {
			logger.info("result:" + permission2.implies(permission));
		}
		logger.info(permission.getParts().toString());
	}

	public static void main(String[] args) {
		String wildcardString = "/index/test,/test:/index";
		List<String> parts = CollectionUtils.asList(wildcardString
				.split(PART_DIVIDER_TOKEN));
		for (int i = 0; i < parts.size(); i++) {
			System.out.println(parts.get(i));
			Set<String> subparts = CollectionUtils.asSet(parts.get(i).split(
					SUBPART_DIVIDER_TOKEN));
			if (subparts.isEmpty()) {
				System.out.println("isEmpty");
			} else {
				Iterator<String> it = subparts.iterator();
				while (it.hasNext()) {
					System.out.println("value:" + it.next());
				}
			}
		}
		String sss = "sss{asd}";
		String regx = "^\\{[a-z]*\\}$";
		String regx2 = "\\w*\\{[a-z]*\\}\\w*";

		System.out.println(sss.matches(regx2));
	}
}
