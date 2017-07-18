package com.android.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common {

	private static String IntegerPattern = "\\d+";
	/**
	 * 字符串筛选数字
	 * @param regex
	 * @return
	 */
	public static int filterInteger(String regex) {
		Pattern r = Pattern.compile(IntegerPattern);
		Matcher m = r.matcher(regex);
		if (m.find()) {
			return Integer.parseInt(m.group(0));
		} else {
			return -1;
		}
	}
	/**
	 * 数组转换为字符串
	 * @param a
	 * @return
	 */
	public static String ArrayToString(String[] a ){
		return String.join(",", a);
	}

}
