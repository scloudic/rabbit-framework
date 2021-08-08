package com.scloudic.rabbitframework.web.utils;

import java.util.ArrayList;
import java.util.List;

import com.scloudic.rabbitframework.core.utils.StringUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtils {

	/**
	 * 获取汉字串拼音首字母，英文字符不变
	 *
	 * @param chinese
	 *            汉字串
	 * @return 汉语拼音首字母
	 */
	public static String cn2FirstSpell(String chinese) {
		if (StringUtils.isBlank(chinese)) {
			return "";
		}
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] _t = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
					if (_t != null) {
						pybf.append(_t[0].charAt(0));
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString().replaceAll("\\W", "").trim();
	}

	/**
	 * 获取汉字串拼音首字母，英文字符不变
	 *
	 * @param chinese
	 *            汉字串
	 * @return 汉语拼音首字母
	 */
	public static List<StringBuffer> cn2MultiFirstSpell(String chinese) {
		List<StringBuffer> list = new ArrayList<StringBuffer>();
		if (StringUtils.isBlank(chinese)) {
			return list;
		}
		list.add(new StringBuffer());
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] hanyu = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
					if (hanyu == null) {
						continue;
					}
					for (int j = 0; j < hanyu.length; j++) {
						if (hanyu[j] != null) {
							hanyu[j] = hanyu[j].substring(0, 1);
						}
					}
					hanyu = removeRepeat(hanyu);
					int length = list.size();
					for (int j = 1; j < hanyu.length; j++) {
						for (int k = 0; k < length; k++) {
							list.add(new StringBuffer(list.get(k)));
						}
					}
					int n = 0;
					for (StringBuffer sb : list) {
						String string = hanyu[n++ % hanyu.length];
						sb.append(string);
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				for (StringBuffer sb : list) {
					sb.append(arr[i]);
				}
			}
		}
		// return pybf.toString().replaceAll("\\W", "").trim();
		return list;
	}

	/**
	 * 获取汉字串拼音，英文字符不变
	 *
	 * @param chinese
	 *            汉字串
	 * @return 汉语拼音
	 */
	public static String cn2Spell(String chinese) {
		if (StringUtils.isBlank(chinese)) {
			return "";
		}
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] strArr = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
					if (strArr != null) {
						pybf.append(strArr[0]);
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString();
	}

	/**
	 * 多音字的处置
	 *
	 * @param chinese
	 * @return
	 */
	public static List<StringBuffer> cn2MultiSpell(String chinese) {
		List<StringBuffer> list = new ArrayList<StringBuffer>();
		if (StringUtils.isBlank(chinese)) {
			return list;
		}
		list.add(new StringBuffer());
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] hanyu = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
					if (hanyu == null) {
						continue;
					}
					hanyu = removeRepeat(hanyu);
					int length = list.size();
					for (int j = 1; j < hanyu.length; j++) {
						for (int k = 0; k < length; k++) {
							list.add(new StringBuffer(list.get(k)));
						}
					}
					int n = 0;
					for (StringBuffer sb : list) {
						String string = hanyu[n++ % hanyu.length];
						sb.append(string);
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				for (StringBuffer sb : list) {
					sb.append(arr[i]);
				}
			}
		}
		return list;
	}

	/**
	 * 删除数组里面的重复数据
	 *
	 * @param arr
	 */
	private static String[] removeRepeat(String[] arr) {
		int length = arr.length;
		for (int i = 0; i < arr.length; i++) {
			if (i >= length) {
				break;
			}
			if (StringUtils.isBlank(arr[i])) {
				System.arraycopy(arr, i + 1, arr, i, arr.length - i - 1);
				arr[arr.length - 1] = "";
				length--;
				i--;
				continue;
			}
			for (int j = i + 1; j < arr.length; j++) {
				if (j >= length) {
					break;
				}
				if (arr[i].equals(arr[j])) {
					System.arraycopy(arr, j + 1, arr, j, arr.length - j - 1);
					arr[arr.length - 1] = "";
					length--;
					j--;
				}
			}
		}

		String[] newarr = null;
		if (length < arr.length) {
			newarr = new String[length];
			System.arraycopy(arr, 0, newarr, 0, length);
			arr = newarr;
		}
		return newarr != null ? newarr : arr;
	}
}
