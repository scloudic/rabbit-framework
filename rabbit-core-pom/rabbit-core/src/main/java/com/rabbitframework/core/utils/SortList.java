package com.rabbitframework.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * list的bean排序类，支持desc和asc排序
 * 
 * @author justin.liang
 *
 * @param <E>
 */
public class SortList<E> {
	private static final Logger logger = LoggerFactory.getLogger(SortList.class);

	public void sort(List<E> list, final String method, final String sort) {
		Collections.sort(list, new Comparator() {
			public int compare(Object a, Object b) {
				int ret = 0;
				try {
					Method m1 = ((E) a).getClass().getMethod(method, null);
					Method m2 = ((E) b).getClass().getMethod(method, null);
					if (sort != null && "desc".equals(sort))// 倒序
						ret = m2.invoke(((E) b), null).toString().compareTo(m1.invoke(((E) a), null).toString());
					else
						// 正序
						ret = m1.invoke(((E) a), null).toString().compareTo(m2.invoke(((E) b), null).toString());
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
				return ret;
			}
		});
	}
}