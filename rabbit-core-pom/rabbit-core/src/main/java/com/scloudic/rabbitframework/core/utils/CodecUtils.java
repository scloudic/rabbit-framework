package com.scloudic.rabbitframework.core.utils;

import com.scloudic.rabbitframework.core.exceptions.CodecException;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * @author justin.liang
 */
public class CodecUtils {
	/**
	 * Shiro's default preferred character encoding, equal to <b>
	 * <code>UTF-8</code></b>.
	 */
	public static final String PREFERRED_ENCODING = "UTF-8";

	public static byte[] toBytes(char[] chars) {
		return toBytes(new String(chars), PREFERRED_ENCODING);
	}

	/**
	 * Converts the specified character array into a byte array using the
	 * specified character encoding.
	 * <p>
	 * This is a convenience method equivalent to calling the
	 * {@link #toBytes(String, String)} method with a a wrapping String and the
	 * specified encoding, i.e.
	 * <p>
	 * <code>toBytes( new String(chars), encoding );</code>
	 *
	 * @param chars
	 *            the character array to be converted to a byte array
	 * @param encoding
	 *            the character encoding to use to when converting to bytes.
	 * @return the bytes of the specified character array under the specified
	 *         encoding.
	 * @throws CodecException
	 *             if the JVM does not support the specified encoding.
	 */
	public static byte[] toBytes(char[] chars, String encoding) throws CodecException {
		return toBytes(new String(chars), encoding);
	}

	public static byte[] toBytes(String source) {
		return toBytes(source, PREFERRED_ENCODING);
	}

	/**
	 * Converts the specified source to a byte array via the specified encoding,
	 * throwing a {@link CodecException CodecException} if the encoding fails.
	 *
	 * @param source
	 *            the source string to convert to a byte array.
	 * @param encoding
	 *            the encoding to use to use.
	 * @return the byte array of the specified source with the given encoding.
	 * @throws CodecException
	 *             if the JVM does not support the specified encoding.
	 */
	public static byte[] toBytes(String source, String encoding) throws CodecException {
		try {
			return source.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			String msg = "Unable to convert source [" + source + "] to byte array using " + "encoding '" + encoding
					+ "'";
			throw new CodecException(msg, e);
		}
	}

	public static String toString(byte[] bytes) {
		return toString(bytes, PREFERRED_ENCODING);
	}

	/**
	 * Converts the specified byte array to a String using the specified
	 * character encoding. This implementation does the same thing as
	 * <code>new {@link String#String(byte[], String) String(byte[], encoding)}</code>
	 * , but will wrap any {@link UnsupportedEncodingException} with a nicer
	 * runtime {@link CodecException}, allowing you to decide whether or not you
	 * want to catch the exception or let it propagate.
	 *
	 * @param bytes
	 *            the byte array to convert to a String
	 * @param encoding
	 *            the character encoding used to encode the String.
	 * @return the specified byte array as an encoded String
	 * @throws CodecException
	 *             if the JVM does not support the specified encoding.
	 */
	public static String toString(byte[] bytes, String encoding) throws CodecException {
		try {
			return new String(bytes, encoding);
		} catch (UnsupportedEncodingException e) {
			String msg = "Unable to convert byte array to String with encoding '" + encoding + "'.";
			throw new CodecException(msg, e);
		}
	}

	public static char[] toChars(byte[] bytes) {
		return toChars(bytes, PREFERRED_ENCODING);
	}

	/**
	 * Converts the specified byte array to a character array using the
	 * specified character encoding.
	 * <p>
	 * Effectively calls
	 * <code>{@link #toString(byte[], String) toString(bytes,encoding)}.{@link String#toCharArray() toCharArray()};</code>
	 *
	 * @param bytes
	 *            the byte array to convert to a String
	 * @param encoding
	 *            the character encoding used to encode the bytes.
	 * @return the specified byte array as an encoded char array
	 * @throws CodecException
	 *             if the JVM does not support the specified encoding.
	 */
	public static char[] toChars(byte[] bytes, String encoding) throws CodecException {
		return toString(bytes, encoding).toCharArray();
	}

	public static byte[] toBytes(Object o) {
		if (o == null) {
			String msg = "Argument for byte conversion cannot be null.";
			throw new IllegalArgumentException(msg);
		}
		if (o instanceof byte[]) {
			return (byte[]) o;
		} else if (o instanceof char[]) {
			return toBytes((char[]) o);
		} else if (o instanceof String) {
			return toBytes((String) o);
		} else if (o instanceof File) {
			return toBytes((File) o);
		} else if (o instanceof InputStream) {
			return toBytes((InputStream) o);
		} else {
			throw new CodecException("conversion error");
		}
	}

	/**
	 * Converts the specified Object into a String.
	 * <p>
	 * If the argument is a {@code byte[]} or {@code char[]} it will be
	 * converted to a String using the {@link #PREFERRED_ENCODING}. If a String,
	 * it will be returned as is.
	 * <p>
	 * If the argument is anything other than these three types, it is passed to
	 * the {@link #objectToString(Object) objectToString} method.
	 *
	 * @param o
	 *            the Object to convert into a byte array
	 * @return a byte array representation of the Object argument.
	 */
	public static String toString(Object o) {
		if (o == null) {
			String msg = "Argument for String conversion cannot be null.";
			throw new IllegalArgumentException(msg);
		}
		if (o instanceof byte[]) {
			return toString((byte[]) o);
		} else if (o instanceof char[]) {
			return new String((char[]) o);
		} else if (o instanceof String) {
			return (String) o;
		} else {
			return objectToString(o);
		}
	}

	public static byte[] toBytes(File file) {
		if (file == null) {
			throw new IllegalArgumentException("File argument cannot be null.");
		}
		try {
			return toBytes(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			String msg = "Unable to acquire InputStream for file [" + file + "]";
			throw new CodecException(msg, e);
		}
	}

	/**
	 * Converts the specified {@link InputStream InputStream} into a byte array.
	 *
	 * @param in
	 *            the InputStream to convert to a byte array
	 * @return the bytes of the input stream
	 * @throws IllegalArgumentException
	 *             if the {@code InputStream} argument is {@code null}.
	 * @throws CodecException
	 *             if there is any problem reading from the {@link InputStream}.
	 * @since 1.0
	 */
	public static byte[] toBytes(InputStream in) {
		if (in == null) {
			throw new IllegalArgumentException("InputStream argument cannot be null.");
		}
		final int BUFFER_SIZE = 512;
		ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead;
		try {
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
			return out.toByteArray();
		} catch (IOException ioe) {
			throw new CodecException(ioe);
		} finally {
			try {
				in.close();
			} catch (IOException ignored) {
			}
			try {
				out.close();
			} catch (IOException ignored) {
			}
		}
	}

	public static byte[] bytes(UUID uuid) {
		if (uuid == null) {
			return null;
		}

		long msb = uuid.getMostSignificantBits();
		long lsb = uuid.getLeastSignificantBits();
		byte[] buffer = new byte[16];

		for (int i = 0; i < 8; i++) {
			buffer[i] = (byte) (msb >>> (8 * (7 - i)));
		}
		for (int i = 8; i < 16; i++) {
			buffer[i] = (byte) (lsb >>> (8 * (7 - i)));
		}
		return buffer;
	}

	public static ByteBuffer byteBuffer(UUID uuid) {
		if (uuid == null) {
			return null;
		}
		return ByteBuffer.wrap(bytes(uuid));
	}

	public static ByteBuffer byteBuffer(String s) {
		return ByteBuffer.wrap(toBytes(s));
	}

	public static String toString(ByteBuffer buffer) {
		byte[] bytes = new byte[buffer.remaining()];
		buffer.get(bytes);
		return toString(bytes);
	}

	/**
	 * Default implementation merely returns
	 * <code>objectArgument.toString()</code>. Subclasses can override this
	 * method for different mechanisms of converting an object to a String.
	 *
	 * @param o
	 *            the Object to convert to a byte array.
	 * @return a String representation of the Object argument.
	 */
	public static String objectToString(Object o) {
		return o.toString();
	}
}
