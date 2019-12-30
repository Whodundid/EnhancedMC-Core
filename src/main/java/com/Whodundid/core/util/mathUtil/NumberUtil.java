package com.Whodundid.core.util.mathUtil;

import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.storageUtil.StorageBox;
import java.util.List;

//Last edited: 9-16-18
//First Added: 9-14-18
//Author: Hunter Bragg

public class NumberUtil {
	
	public static int max(int a, int b) { return (a > b) ? a : b; }
	public static int min(int a, int b) { return (a < b) ? a : b; }
	
	public static boolean isInteger(String s) { return s.matches("-?\\d+"); }
	public static boolean isInteger(String s, int radix) {
		if (s.isEmpty()) { return false; }
		for (int i = 0; i < s.length(); i++) {
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
	
	public static NumType getNumType(Class<? extends Number> c) {
		if (c == Byte.class || c == byte.class) { return NumType.b; }
		if (c == Short.class || c == short.class) { return NumType.s; }
		if (c == Integer.class || c == int.class) { return NumType.i; }
		if (c == Long.class || c == long.class) { return NumType.l; }
		if (c == Float.class || c == float.class) { return NumType.f; }
		if (c == Double.class || c == double.class) { return NumType.d; }
		return NumType.n;
	}
	
	public static double sumValues(List<Number> valsIn) {
		if (valsIn != null) {
			double total = 0;
			for (Number n : valsIn) { total += n.doubleValue(); }
			return total;
		}
		return Double.NaN;
	}
	
	public static double squareAndPowValues(List<Number> valsIn, int expIn) {
		if (valsIn != null) {
			double total = 0;
			for (Number n : valsIn) { total += Math.pow(n.doubleValue(), expIn); }
			return total;
		}
		return Double.NaN;
	}
	
	public static double sumOfProducts(List<Number> vals1, List<Number> vals2) {
		if (EUtil.nullCheck(vals1, vals2)) {
			if (vals1.size() == vals2.size()) {
				double total = 0;
				for (int i = 0; i < vals1.size(); i++) {
					total += (vals1.get(i).doubleValue() * vals2.get(i).doubleValue());
				}
				return total;
			}
		}
		return Double.NaN;
	}
	
	public static double sumSquareFirstProdSecond(List<Number> vals1, int expIn, List<Number> vals2) {
		if (EUtil.nullCheck(vals1, vals2)) {
			if (vals1.size() == vals2.size()) {
				double total = 0;
				for (int i = 0; i < vals1.size(); i++) {
					total += (Math.pow(vals1.get(i).doubleValue(), expIn) * vals2.get(i).doubleValue());
				}
				return total;
			}
		}
		return Double.NaN;
	}
}
