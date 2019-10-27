package com.rabbitframework.web.filter.sensitive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tjzq.commons.utils.ResourceUtils;

/**
 * 创建一个FilterSet，枚举了0~65535的所有char是否是某个敏感词开头的状态
 * 
 * 判断是否是敏感词开头，是不是 获取头节点 OK--下一个字 然后逐级遍历，DFA算法
 * 
 */
public class WordFilter {
	private static final FilterSet set = new FilterSet(); // 存储首字
	private static final Map<Integer, WordNode> nodes = new HashMap<Integer, WordNode>(1024, 1); // 存储节点
	private static final Set<Integer> stopwdSet = new HashSet<Integer>(); // 停顿词
	private static final char SIGN = '*'; // 敏感词过滤替换

	static {
		try {
			init();
		} catch (Exception e) {
			// 加载失败
		}
	}

	private static void init() {
		// 获取敏感词
		addSensitiveWord(readWordFromFile("wd.txt"));
		addStopWord(readWordFromFile("stopwd.txt"));
	}

	/**
	 * 增加敏感词
	 * 
	 * @param path
	 * @return
	 */
	private static List<String> readWordFromFile(String path) {
		List<String> words;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(ResourceUtils.getResourceAsStream(path)));
			words = new ArrayList<String>(1200);
			for (String buf = ""; (buf = br.readLine()) != null;) {
				if (buf == null || buf.trim().equals(""))
					continue;
				words.add(buf);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
			}
		}
		return words;
	}

	/**
	 * 增加停顿词
	 * 
	 * @param words
	 */
	private static void addStopWord(final List<String> words) {
		if (!isEmpty(words)) {
			char[] chs;
			for (String curr : words) {
				chs = curr.toCharArray();
				for (char c : chs) {
					stopwdSet.add(charConvert(c));
				}
			}
		}
	}

	/**
	 * 添加DFA节点
	 * 
	 * @param words
	 */
	private static void addSensitiveWord(final List<String> words) {
		if (!isEmpty(words)) {
			char[] chs;
			int fchar;
			int lastIndex;
			WordNode fnode; // 首字母节点
			for (String curr : words) {
				chs = curr.toCharArray();
				fchar = charConvert(chs[0]);
				if (!set.contains(fchar)) {// 没有首字定义
					set.add(fchar);// 首字标志位 可重复add,反正判断了，不重复了
					fnode = new WordNode(fchar, chs.length == 1);
					nodes.put(fchar, fnode);
				} else {
					fnode = nodes.get(fchar);
					if (!fnode.isLast() && chs.length == 1)
						fnode.setLast(true);
				}
				lastIndex = chs.length - 1;
				for (int i = 1; i < chs.length; i++) {
					fnode = fnode.addIfNoExist(charConvert(chs[i]), i == lastIndex);
				}
			}
		}
	}

	/**
	 * 过滤判断 将敏感词转化为成屏蔽词
	 * 
	 * @param src
	 * @return
	 */
	public static final String doFilter(final String src) {
		if (set != null && nodes != null) {
			char[] chs = src.toCharArray();
			int length = chs.length;
			int currc; // 当前检查的字符
			int cpcurrc; // 当前检查字符的备份
			int k;
			WordNode node;
			for (int i = 0; i < length; i++) {
				currc = charConvert(chs[i]);
				if (!set.contains(currc)) {
					continue;
				}
				node = nodes.get(currc);// 日 2
				if (node == null)// 其实不会发生，习惯性写上了
					continue;
				boolean couldMark = false;
				int markNum = -1;
				if (node.isLast()) {// 单字匹配（日）
					couldMark = true;
					markNum = 0;
				}
				// 继续匹配（日你/日你妹），以长的优先
				// 你-3 妹-4 夫-5
				k = i;
				cpcurrc = currc; // 当前字符的拷贝
				for (; ++k < length;) {
					int temp = charConvert(chs[k]);
					if (temp == cpcurrc)
						continue;
					if (stopwdSet != null && stopwdSet.contains(temp))
						continue;
					node = node.querySub(temp);
					if (node == null)// 没有了
						break;
					if (node.isLast()) {
						couldMark = true;
						markNum = k - i;// 3-2
					}
					cpcurrc = temp;
				}
				if (couldMark) {
					for (k = 0; k <= markNum; k++) {
						chs[k + i] = SIGN;
					}
					i = i + markNum;
				}
			}
			return new String(chs);
		}

		return src;
	}

	/**
	 * 是否包含敏感词
	 * 
	 * @param src
	 * @return
	 */
	public static final boolean isContains(final String src) {
		if (set != null && nodes != null) {
			char[] chs = src.toCharArray();
			int length = chs.length;
			int currc; // 当前检查的字符
			int cpcurrc; // 当前检查字符的备份
			int k;
			WordNode node;
			for (int i = 0; i < length; i++) {
				currc = charConvert(chs[i]);
				if (!set.contains(currc)) {
					continue;
				}
				node = nodes.get(currc);// 日 2
				if (node == null)// 其实不会发生，习惯性写上了
					continue;
				boolean couldMark = false;
				if (node.isLast()) {// 单字匹配（日）
					couldMark = true;
				}
				// 继续匹配（日你/日你妹），以长的优先
				// 你-3 妹-4 夫-5
				k = i;
				cpcurrc = currc;
				for (; ++k < length;) {
					int temp = charConvert(chs[k]);
					if (temp == cpcurrc)
						continue;
					if (stopwdSet != null && stopwdSet.contains(temp))
						continue;
					node = node.querySub(temp);
					if (node == null)// 没有了
						break;
					if (node.isLast()) {
						couldMark = true;
					}
					cpcurrc = temp;
				}
				if (couldMark) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 大写转化为小写 全角转化为半角
	 * 
	 * @param src
	 * @return
	 */
	private static int charConvert(char src) {
		int r = BCConvert.qj2bj(src);
		return (r >= 'A' && r <= 'Z') ? r + 32 : r;
	}

	/**
	 * 判断一个集合是否为空
	 * 
	 * @param col
	 * @return
	 */
	public static <T> boolean isEmpty(final Collection<T> col) {
		if (col == null || col.isEmpty()) {
			return true;
		}
		return false;
	}
}
