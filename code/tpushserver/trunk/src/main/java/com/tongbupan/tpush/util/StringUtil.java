package com.tongbupan.tpush.util;

public class StringUtil {

	public static boolean isEmpty(String s) {
		if (s == null || "".equals(s)) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(Object s) {
		if (s == null || "".equals(s.toString())) {
			return true;
		}
		return false;
	}

	public static String decodeTrim(String str) {
		if (str == null) {
			return "";
		}
		return str.trim();
	}
}
