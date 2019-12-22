package com.Whodundid.core.util;

import com.Whodundid.core.util.storageUtil.EArrayList;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import org.lwjgl.input.Keyboard;

//Last edited: Jan 16, 2019
//First Added: Sep 14, 2018
//Author: Hunter Bragg

public class EUtil {
	
	public static Minecraft mc = Minecraft.getMinecraft();
	private static FontRenderer fr = mc.fontRendererObj;
	
	/** Returns false if any of the provided objects are null. */
	public static boolean nullCheck(Object... objsIn) {
		boolean val = true;
		for (Object o : objsIn) { if (o == null) { val = false; } }
		return val;
	}
	
	/** Breaks a String into a list of smaller strings based on a set maximum line width. */
	public static EArrayList<String> createWordWrapString(String stringIn, int widthMax) {
		EArrayList<String> lines = new EArrayList();
		try {
			if (stringIn != null && !stringIn.isEmpty() && fr.getStringWidth(stringIn) > widthMax) {
				String restOfString = stringIn;
				while (fr.getStringWidth(restOfString) > widthMax) {
					int i = 0;
					int iPos = 0;
					char end = Character.MIN_VALUE;
					String buildString = "";
					while (!(fr.getStringWidth(buildString) >= widthMax) && i < restOfString.length() - 1) {
						buildString += restOfString.charAt(i);
						i++;
					}
					while (i > 0 && end != ' ') {
						iPos = i;
						end = restOfString.charAt(i--);
					}
					if (i <= 0) {
						lines.add(restOfString.substring(0, buildString.length() - 1));
						restOfString = restOfString.substring(buildString.length() - 1, restOfString.length());
					} else {
						lines.add(restOfString.substring(0, iPos));
						restOfString = restOfString.substring(iPos + 1, restOfString.length());
					}
				}
				lines.add(restOfString);
			} else { lines.add(stringIn); }
		} catch (Exception e) { e.printStackTrace(); }
		return lines;
	}
	
	/** Converts a series of keyboard integer keys to their String equivalent(s). */
	public static String keysToString(int[] keysIn) {
		EArrayList<Integer> list = new EArrayList();
		for (int i : keysIn) { list.add(i); }
		return keysToString(list);
	}
	
	/** Converts a series of keyboard integer keys to their String equivalent(s). */
	public static String keysToString(EArrayList<Integer> keysIn) {
		String newText = "";
		for (int i = 0; i < keysIn.size(); i++) {
			int keyCode = keysIn.get(i);
			boolean isCtrl = (Minecraft.isRunningOnMac && (keyCode == 219 || keyCode == 220)) || keyCode == 29 || keyCode == 157;
			boolean isShift = keyCode == 42 || keyCode == 54;
			boolean isAlt = keyCode == 56 || keyCode == 184;
			
			String altString = Util.getOSType() == Util.EnumOS.OSX ? "Options" : "Alt";
			
			if (isCtrl) { newText += (keysIn.size() > 1) ? "Ctrl + " : "Ctrl"; }
			if (isShift) { newText += (keysIn.size() > 1 && i != keysIn.size() - 1) ? "Shift + " : "Shift"; }
			if (isAlt) { newText += (keysIn.size() > 1 && i != keysIn.size() - 1) ? altString + " + " : altString; }
			
			if (!isCtrl && !isShift && !isAlt) {
				String keyName = Keyboard.getKeyName(keyCode);
				keyName = keyName.toLowerCase();
				if (keyName.length() > 0) { keyName = keyName.substring(0, 1).toUpperCase() + keyName.substring(1); }
				if (keyName.equals("Grave")) { keyName = "Tilde"; }
				newText += keyName;
			}
		}
		return newText;
	}
	
	/** Utility function to check if the values in one array match the values from another. */
	public static boolean validateArrayContents(List list1, List list2) {
		if (list1.size() != list2.size()) { return false; } //if the sizes differ, they're not the same.
		for (int i = 0; i < list1.size(); i++) {
			Object a = list1.get(i);
			Object b = list2.get(i);
			if (a != null && b != null) {
				Class c1 = list1.get(i).getClass();
				Class c2 = list2.get(i).getClass();
				if (!c1.equals(c2)) { return false; }
			}
			else if (a == null && b != null) { return false; }
			else if (a != null && b == null) { return false; }
			else { return false; }
		}
		return true;
	}
	
	/** Creates a substring from a given string ending at the first space found from the given starting position. */
	public static String subStringToSpace(String in, int startPos) {
		if (in != null && !in.isEmpty()) {
			int pos = startPos;
			while (pos < in.length() && in.charAt(pos) != ' ') {
				pos++;
			}
			return in.substring(startPos, pos);
		}
		return in;
	}
	
	/** Returns the index for the position in a string where another string is located within it. */
	public static int findStartingIndex(String toSearch, String toFind) {
		if (toSearch != null && !toSearch.isEmpty() && toFind.length() <= toSearch.length()) {
			String cur = "";
			int index = 0;
			int j = 0;
			for (int i = 0; i < toSearch.length() - toFind.length() + 1; i++) {
				if (cur.equals(toFind)) { return index; }
				if (toSearch.charAt(i) == toFind.charAt(j)) {
					cur += toSearch.charAt(i);
					j++;
				}
				else if (toSearch.charAt(i) == toFind.charAt(0)) {
					cur = "" + toSearch.charAt(i);
					index = i + 1;
					j = 1;
				}
				else {
					cur = "";
					index = i + 1;
					j = 0;
				}
			}
		}
		return -1;
	}
	
	public static String getFullEMCSourceFilePath(Class classIn) {
		String path = System.getProperty("user.dir");
		if (path != null && path.length() >= 5) { path = path.substring(0, path.length() - 3); }
		String fullPath = classIn.getProtectionDomain().getCodeSource().getLocation().getPath();
		String p = "src\\main\\java" + fullPath.substring(EUtil.findStartingIndex(fullPath, "/com/Whodundid/"), fullPath.length() - 5) + "java";
		p = p.replace("/", "\\");
		return path + p;
	}
	
	public static String getRelativeEMCSourceFilePath(Class classIn) {
		String fullPath = classIn.getProtectionDomain().getCodeSource().getLocation().getPath();
		String p = fullPath.substring(EUtil.findStartingIndex(fullPath, "com/Whodundid/"), fullPath.length() - 5) + "java";
		p = p.replace("/", "\\");
		return p;
	}
	
	/** Returns the actual width in pixels for the given RescoureLocation. */
	public static int getImageWidth(ResourceLocation locIn) {
		try {
			IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(locIn);
			InputStream stream = resource.getInputStream();
			BufferedImage image = ImageIO.read(stream);
			return image.getWidth();
		} catch (Exception e) { e.printStackTrace(); }
		return -1;
	}
	
	/** Returns the actual height in pixels for the given RescoureLocation. */
	public static int getImageHeight(ResourceLocation locIn) {
		try {
			IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(locIn);
			InputStream stream = resource.getInputStream();
			BufferedImage image = ImageIO.read(stream);
			return image.getHeight();
		} catch (Exception e) { e.printStackTrace(); }
		return -1;
	}
}
