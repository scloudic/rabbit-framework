package com.scloudic.rabbitframework.core.utils;

import com.scloudic.rabbitframework.core.exceptions.DataParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

public class StringUtils extends org.apache.commons.lang.StringUtils {
    private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);
    public static final String EMPTY_STRING = "";
    public static final String CONFIG_LOCATION_DELIMITERS = ",; \t\n";
    public static final String SEPARATOR = "_";
    public static final String PREFERRED_ENCODING = "UTF-8";

    public static boolean hasLength(String str) {
        return (str != null && str.length() > 0);
    }


    public static boolean hasText(String str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static int stringToInt(String value) {
        Integer result = null;
        if (isEmpty(value)) {
            return result;
        }
        try {
            result = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
            throw new DataParseException(e.getMessage(), e);
        }
        return result;
    }

    public static int stringToInt(String value, int defaultValue) {
        int result = defaultValue;
        if (isEmpty(value)) {
            return result;
        }
        try {
            result = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    public static Integer integerValueOf(String value, Integer defaultValue) {
        Integer result = defaultValue;
        if (isEmpty(value)) {
            return result;
        }
        return Integer.valueOf(value);
    }

    public static long stringToLong(String value) {
        Long result = null;
        if (isEmpty(value)) {
            return result;
        }
        try {
            result = Long.parseLong(value);
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
            throw new DataParseException(e.getMessage(), e);
        }
        return result;
    }

    public static long stringToLong(String value, long defaultValue) {
        Long result = defaultValue;
        if (isEmpty(value)) {
            return result;
        }
        try {
            result = Long.parseLong(value);
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    public static boolean stringToBoolean(String value, boolean defaultValue) {
        boolean result = defaultValue;
        if (isEmpty(value)) {
            return result;
        }
        return Boolean.parseBoolean(value);
    }

    public static String trim(String str) {
        return str != null ? str.trim() : null;
    }

    public static String trimToNull(String str) {
        String ts = trim(str);
        return isEmpty(ts) ? null : ts;
    }


    public static String trimToEmpty(String str) {
        return str != null ? str.trim() : "";
    }


    public static Long longToZero(Long value) {
        Long returnValue = value;
        if (returnValue == null) {
            return 0L;
        }
        return returnValue;
    }


    public static Integer integerToZero(Integer value) {
        Integer returnValue = value;
        if (returnValue == null) {
            return 0;
        }
        return returnValue;
    }


    public static String arrayToString(String[] array, String separator) {
        String str = "";

        if (array != null && array.length > 0) {
            for (int i = 0; i < array.length; i++) {
                if (i < array.length - 1)
                    str += array[i] + separator;
                else
                    str += array[i];
            }
        }

        return str;
    }

    public static String integerToString(int a) {

        String Leverid = "";
        for (int i = 0; i < a; i++) {
            Leverid = Leverid + String.valueOf(i + 1) + ",";
        }

        return Leverid;

    }

    public static boolean compareString(String strA, String strB, String separator) {
        boolean flag = false;

        if (strA == null || strB == null)
            return flag;

        String[] strArrayA = strA == null ? new String[]{} : strA.split(separator);
        strB = separator + strB + separator;

        for (int i = 0; i < strArrayA.length; i++) {
            if (strB.indexOf(separator + strArrayA[i] + separator) > -1)
                flag = true;
        }

        return flag;
    }

    public static List<String> tokenizeToArray(String str) {
        return tokenizeToArray(str, CONFIG_LOCATION_DELIMITERS, true, false);
    }

    public static String[] tokenizeToStringArray(String str) {
        List<String> token = tokenizeToArray(str);
        return toStringArray(token);
    }

    public static List<String> tokenizeToArray(String str, String delimiters, boolean trimTokens,
                                               boolean ignoreEmptyTokens) {
        if (str == null) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(str, delimiters);
        List<String> tokens = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    /**
     * 字符串解析转换字符串数组
     *
     * @param str               str
     * @param delimiters        分割符
     * @param trimTokens        是否去除空
     * @param ignoreEmptyTokens 是否忽略空
     * @return array
     */
    public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens,
                                                 boolean ignoreEmptyTokens) {
        if (str == null) {
            return null;
        }
        List<String> tokens = tokenizeToArray(str, delimiters, trimTokens, ignoreEmptyTokens);
        return toStringArray(tokens);
    }

    public static String[] toStringArray(Collection<String> collection) {
        if (collection == null) {
            return null;
        }
        return collection.toArray(new String[collection.size()]);
    }

    /**
     * Returns the input argument, but ensures the first character is
     * capitalized (if possible).
     *
     * @param in the string to uppercase the first character.
     * @return the input argument, but with the first character capitalized (if
     * possible).
     * @since 1.2
     */
    public static String uppercaseFirstChar(String in) {
        if (in == null || in.length() == 0) {
            return in;
        }
        int length = in.length();
        StringBuilder sb = new StringBuilder(length);

        sb.append(Character.toUpperCase(in.charAt(0)));
        if (length > 1) {
            String remaining = in.substring(1);
            sb.append(remaining);
        }
        return sb.toString();
    }

    public static String lowercaseFirstChar(String in) {
        if (in == null || in.length() == 0) {
            return in;
        }
        int length = in.length();
        StringBuilder sb = new StringBuilder(length);

        sb.append(Character.toLowerCase(in.charAt(0)));
        if (length > 1) {
            String remaining = in.substring(1);
            sb.append(remaining);
        }
        return sb.toString();
    }

    /**
     * Returns the specified array as a comma-delimited (',') string.
     *
     * @param array the array whose contents will be converted to a string.
     * @return the array's contents as a comma-delimited (',') string.
     * @since 1.0
     */
    public static String toString(Object[] array) {
        return toDelimitedString(array, ",");
    }

    /**
     * Returns the array's contents as a string, with each element delimited by
     * the specified {@code delimiter} argument. Useful for {@code toString()}
     * implementations and log messages.
     *
     * @param array     the array whose contents will be converted to a string
     * @param delimiter the delimiter to use between each element
     * @return a single string, delimited by the specified {@code delimiter}.
     * @since 1.0
     */
    public static String toDelimitedString(Object[] array, String delimiter) {
        if (array == null || array.length == 0) {
            return EMPTY_STRING;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                sb.append(delimiter);
            }
            sb.append(array[i]);
        }
        return sb.toString();
    }

    /**
     * 分隔符的字符串转换为骆驼拼写法的字符串
     * <p>
     * 如:hello_world 转换为helloWorld
     *
     * @param inputString inputString
     * @param firstCharacterUppercase 首字母是否大写
     * @return string
     */
    public static String toCamelCase(String inputString, boolean firstCharacterUppercase) {
        StringBuilder sb = new StringBuilder();
        boolean nextUpperCase = false;
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);
            switch (c) {
                case '_':
                case '-':
                case '@':
                case '$':
                case '#':
                case ' ':
                case '/':
                case '&':
                    if (sb.length() > 0) {
                        nextUpperCase = true;
                    }
                    break;
                default:
                    if (nextUpperCase) {
                        sb.append(Character.toUpperCase(c));
                        nextUpperCase = false;
                    } else {
                        sb.append(Character.toLowerCase(c));
                    }
                    break;
            }
        }

        if (firstCharacterUppercase) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }

        return sb.toString();
    }

    public static String toUnderScoreCase(String property) {
        return toSeparatorName(property, SEPARATOR);
    }

    public static String toSeparatorName(String property, String separator) {
        StringBuilder result = new StringBuilder();
        if (property != null && property.length() > 0) {
            result.append(property.substring(0, 1).toLowerCase());
            for (int i = 1; i < property.length(); i++) {
                char ch = property.charAt(i);
                if (Character.isUpperCase(ch)) {
                    result.append(separator);
                    result.append(Character.toLowerCase(ch));
                } else {
                    result.append(ch);
                }
            }
        }
        return result.toString();
    }
}
