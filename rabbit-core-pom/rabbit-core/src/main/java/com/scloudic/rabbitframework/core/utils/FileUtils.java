package com.scloudic.rabbitframework.core.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils extends org.apache.commons.io.FileUtils {
	private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

	/**
	 * 图片压缩转换为输出流
	 * @param in in
	 * @param scale scale
	 * @return 输出流
	 */
	public static OutputStream imageThumbnailToOutStream(InputStream in, float scale) {
		OutputStream output = null;
		try {
			output = new ByteArrayOutputStream();
			Thumbnails.of(in).scale(scale).toOutputStream(output);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return output;
	}

	/**
	 * 图片压缩输入到文件
	 * 
	 * @param in in
	 * @param scale scale
	 * @param outFile outFile
	 */
	public static void imageThumbnailToFile(InputStream in, float scale, File outFile) {
		try {
			Thumbnails.of(in).scale(scale).toFile(outFile);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}