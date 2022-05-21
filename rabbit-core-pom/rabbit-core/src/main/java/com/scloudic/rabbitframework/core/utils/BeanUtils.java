package com.scloudic.rabbitframework.core.utils;
import com.scloudic.rabbitframework.core.exceptions.DataParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {
	private static final Logger logger = LoggerFactory.getLogger(BeanUtils.class);

	public static void copyProperties(Object dest, Object orig) {
		try {
			org.apache.commons.beanutils.BeanUtils.copyProperties(dest, orig);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataParseException(e.getMessage(), e);
		}
	}
}
