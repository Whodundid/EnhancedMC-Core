package com.Whodundid.core.util.mathUtil;

import com.Whodundid.core.util.storageUtil.StorageBox;

//Last edited: 9-16-18
//First Added: 9-14-18
//Author: Hunter Bragg

public class NumberUtil {
	
	public static int returnLarger(int a, int b) { return (a > b) ? a : b; }
	public static int returnSmaller(int a, int b) { return (a < b) ? a : b; }
	public static boolean isInteger(String s) { return s.matches("-?\\d+"); }
	
	private static boolean isInteger(String s, int radix) {
		if (s.isEmpty()) { return false; }
		for (int i = 0; i < s.length(); i++) {
			System.out.println(s.charAt(i));
			if (i == 0 && s.charAt(i) == '-') {
				if (s.length() == 1) { return false; }
				continue;
			}
			if (Character.digit(s.charAt(i), radix) < 0) { return false; }
		}
		return true;
	}
	
	public static int getDistance(StorageBox<Integer, Integer> point1, StorageBox<Integer, Integer> point2) {
		if (point1 != null && point2 != null) {
			if (point1.getObject() != null && point1.getValue() != null && point2.getObject() != null && point2.getValue() != null) {
				int x1 = point1.getObject();
				int y1 = point1.getValue();
				int x2 = point2.getObject();
				int y2 = point2.getValue();
				
				return (int) Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
			}
		}
		return 0;
	}
}
