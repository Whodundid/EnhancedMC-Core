package com.Whodundid.core.util;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.util.guiUtil.GuiOpener;
import com.Whodundid.core.util.storageUtil.EArrayList;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.Util;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;

//Author: Hunter Bragg

public class EUtil {
	
	public static Minecraft mc = Minecraft.getMinecraft();
	private static FontRenderer fr = mc.fontRendererObj;
	private static GuiScreen screenToLoad = null;
	private static Class screenClass = null;
	private static boolean closeWorld = false;
	
	//--------------
	//Static updater
	//--------------
	
	public static void update() {
		if (closeWorld) {
			mc.theWorld.sendQuittingDisconnectingPacket();
			mc.loadWorld(null);
			closeWorld = false;
		}
		if (screenClass != null) {
			if (screenClass != null) {
				screenToLoad = GuiOpener.createGuiScreenInstance(screenClass);
				System.out.println("the screen: " + screenToLoad);
				screenClass = null;
			}
		}
		if (screenToLoad != null) {
			mc.displayGuiScreen(screenToLoad);
			screenToLoad = null;
		}
	}
	
	//-----------------------
	//EMC Source Path Helpers
	//-----------------------
	
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
	
	//------------
	//Server World
	//------------
	
	public static void closeWorld() { closeWorld = true; }
	public static void closeWorld(GuiScreen in) { closeWorld = true; screenToLoad = in; }
	public static void closeWorld(Class in) { closeWorld = true; screenClass = in; }
	
	//---------
	//GuiScreen
	//---------
	
	public static void setScreenToLoad(GuiScreen in) { screenToLoad = in; }
	public static void setScreenToLoad(Class in) { screenClass = in; }
	
	//----
	//File
	//----
	
	public static void openFile(File file) {
		String s = file.getAbsolutePath();
		if (Util.getOSType() == Util.EnumOS.OSX) {
			try {
				EnhancedMC.info(s);
				Runtime.getRuntime().exec(new String[] {"/usr/bin/open", s});
				return;
			}
			catch (IOException e) {
				EnhancedMC.error("Couldn\'t open file", e);
			}
		}
		else if (Util.getOSType() == Util.EnumOS.WINDOWS) {
			String s1 = String.format("cmd.exe /C start \"Open file\" \"%s\"", new Object[] {s});

			try {
				Runtime.getRuntime().exec(s1);
				return;
			}
			catch (IOException e) {
				EnhancedMC.error("Couldn\'t open file", e);
			}
		}

		boolean stillCantOpen = false;

		try {
			Class<?> oclass = Class.forName("java.awt.Desktop");
			Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
			oclass.getMethod("browse", new Class[] { URI.class }).invoke(object, new Object[] { file.toURI() });
		}
		catch (Throwable e) {
			EnhancedMC.error("Couldn\'t open link", e);
			stillCantOpen = true;
		}

		if (stillCantOpen) {
			EnhancedMC.info("Opening via system class!");
			Sys.openURL("file://" + s);
		}
	}
	
	//-------
	//Objects
	//-------
	
	/** Returns false if any of the provided objects are null. */
	public static boolean nullCheck(Object obj, Object... objsIn) {
		boolean val = true;
		for (Object o : add(obj, objsIn)) { if (o == null) { val = false; } }
		return val;
	}
	
	/** Returns true if any values within either list match. */
	public static <e> boolean findMatch(List<e> objsIn, List<e> list) {
		if (list != null && objsIn.size() > 0) {
			for (e element : list) {
				if (element == null) {
					for (e check : objsIn) {
						if (check == null) { return true; }
					}
				}
				else {
					for (e check : objsIn) {
						if (check.equals(element)) { return true; }
					}
				}
			}
		}
		return false;
	}
	
	/** A statement that returns the specified ifTrue value if any member within the given list matches the given predicate.
	 *  If no member of the list matches the predicate then the ifFalse value is returned instead. */
	public static <A, R> R forEachTest(List<A> list, Predicate<? super A> predicate, R ifTrue, R ifFalse) {
		Objects.requireNonNull(list);
		Objects.requireNonNull(predicate);
		
		for (A a : list) {
			if (predicate.test(a)) { return ifTrue; }
		}
		
		return ifFalse;
	}
	
	/** Returns the first member in a list that matches the given predicate. If no object matches. null is returned. */
	public static <E> E getFirst(List<E> list, Predicate<? super E> predicate) {
		Objects.requireNonNull(predicate);
		
		if (list != null) {
			for (E e : list) {
				if (predicate.test(e)) { return e; }
			}
		}
		
		return null;
	}
	
	/** Returns the last member in a list that matches the given predicate. If no object matches, null is returned. */
	public static <E> E getLast(List<E> list, Predicate<? super E> predicate) {
		Objects.requireNonNull(predicate);
		
		if (list != null) {
			for (int i = list.size(); i >= 0; i--) {
				E e = list.get(i);
				if (predicate.test(e)) { return e; }
			}
		}
		
		return null;
	}
	
	//--------------
	//String Helpers
	//--------------
	
	/** Breaks a String into a list of smaller strings based on a set maximum line width. */
	public static EArrayList<String> createWordWrapString(String stringIn, int widthMax) {
		EArrayList<String> lines = new EArrayList();
		try {
			FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
			if (stringIn != null && !stringIn.isEmpty() && widthMax > 5 && fr.getStringWidth(stringIn) > widthMax) {
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
					}
					else {
						lines.add(restOfString.substring(0, iPos));
						restOfString = restOfString.substring(iPos + 1, restOfString.length());
					}
					
				}
				lines.add(restOfString);
			}
			else { lines.add(stringIn); }
		}
		catch (Exception e) { e.printStackTrace(); }
		return lines;
	}
	
	/** Captializes the first letter in the given string. */
	public static String capitalFirst(String in) {
		if (in != null) {
			String val = "";
			if (in.length() > 0) {
				val += Character.toUpperCase(in.charAt(0));
				val += in.substring(1, in.length());
				return val;
			}
		}
		return in;
	}
	
	/** Returns a string made from the given char repeated num times. */
	public static String repeatString(String in, int num) {
		return new String(new char[num]).replace("\0", in);
	}
	
	public static int countSpaces(String in) { return countChar(in, ' '); }
	public static int countChar(String in, char test) {
		if (in != null) {
			int num = 0;
			for (int i = 0; i < in.length(); i++) {
				if (in.charAt(i) == test) { num++; }
			}
			return num;
		}
		return -1;
	}
	
	public static String combineAll(String[] in) { return combineAll(in, ""); }
	public static String combineAll(String[] in, String spacer) {
		String r = "";
		if (in != null) {
			if (in.length > 1) {
				for (String s : in) { r += s + spacer; }
			}
			else if (in.length == 1) { r += in[0]; }
		}
		return r;
	}
	
	/** Returns a string made from the combination of each string in a list concatenated together. */
	public static String combineAll(List<String> in) { return combineAll(in, ""); }
	public static String combineAll(List<String> in, String spacer) {
		String r = "";
		if (in != null) {
			if (in.size() > 1) {
				for (String s : in) { r += s + spacer; }
			}
			else if (in.size() == 1) { r += in.get(0); }
		}
		return r.trim();
	}
	
	public static String subStringToString(String in, int startPos, String toFind) { return subStringToString(in, startPos, toFind, false); }
	public static String subStringToString(String in, int startPos, String toFind, boolean startFromEnd) {
		if (in != null) {
			if (startPos <= in.length()) {
				String from = startFromEnd ? in.substring(startPos, in.length()) : in.substring(startPos);
				int index = startFromEnd ? findStartingIndex(from, toFind, true) : findStartingIndex(from, toFind);
				return (index >= 0) ? (startFromEnd ? from.substring(index + 1, in.length()) : from.substring(0, index)) : from;
			}
		}
		return in;
	}
	
	/** Creates a substring from a given string ending at the first space found from the given starting position. */
	public static String subStringToSpace(String in, int startPos) { return subStringToSpace(in, startPos, false); }
	public static String subStringToSpace(String in, int startPos, boolean startFromEnd) {
		if (in != null && !in.isEmpty()) {
			int pos = startFromEnd ? in.length() - 1 : startPos;
			
			if (startFromEnd) {
				while ((pos > startPos || pos >= 0) && in.charAt(pos) != ' ') {
					pos--;
				}
				return in.substring(pos + 1, in.length());
			}
			else {
				while (pos < in.length() && in.charAt(pos) != ' ') {
					pos++;
				}
				return in.substring(startPos, pos);
			}
		}
		return in;
	}
	
	/** Returns the index for the position in a string where another string is located within it. */
	public static int findStartingIndex(String toSearch, String toFind) { return findStartingIndex(toSearch, toFind, false); }
	
	/** Returns the index for the position in a string where another string is located within it. Can specifiy whether the search starts from the front or back. */
	public static int findStartingIndex(String toSearch, String toFind, boolean fromBack) {
		if (toSearch != null && !toSearch.isEmpty() && toFind != null && toFind.length() <= toSearch.length()) {
			if (!fromBack) {
				String cur = "";
				int index = 0;
				int j = 0;
				
				for (int i = 0; i <= toSearch.length() - 1; i++) {
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
			else {
				try {
					String cur = "";
					int index = toSearch.length() - 1;
					int j = toFind.length() - 1;
					
					for (int i = toSearch.length() - 1; i >= 0; i--) {
						if (cur.equals(toFind)) { return index; }
						if (j < 0) {
							cur = "";
							index = i - 1;
							j = toFind.length() - 1;
						}
						else if (toSearch.charAt(i) == toFind.charAt(j)) {
							cur += toSearch.charAt(i);
							j--;
						}
						else {
							cur = "";
							index = i - 1;
							j = toFind.length() - 1;
						}
					}
				}
				catch (Exception e) { e.printStackTrace(); }
			}
		}
		return -1;
	}
	
	public static int findIndexAfter(String toSearch, String toFind) {
		if (toSearch != null && toFind != null && !toSearch.isEmpty() && !toFind.isEmpty()) {
			int index = findStartingIndex(toSearch, toFind);
			return index + toFind.length();
		}
		return -1;
	}
	
	//--------------------------------
	//GL Keybinding String Conversions
	//--------------------------------
	
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
	
	//-------------
	//Array Helpers
	//-------------
	
	public static <E> E[] asArray(E... vals) {
		return asList(vals).toArray(vals);
	}
	
	public static <E> EArrayList<E> asList(E... vals) {
		return EArrayList.of(vals);
	}
	
	public static <E> E[] add(E obj, E[] array) {
		EArrayList<E> list = new EArrayList(obj).addA(array);
		return list.toArray(array);
	}
	
	public static <E> E[] addEnd(E obj, E[] array) {
		EArrayList<E> list = new EArrayList().addA(array);
		list.add(obj);
		return list.toArray(array);
	}
	
	public static <E> E[] addAt(E obj, int pos, E[] array) {
		EArrayList<E> list = new EArrayList().addA(array);
		if (pos >= 0 && pos < array.length + 1) {
			list.add(pos, obj);
		}
		return list.toArray(array);
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
	
	//--------------
	//Lambda Helpers
	//--------------
	
	public static <E> Stream<E> toStream(E... vals) {
		EArrayList<E> list = new EArrayList<E>().addA(vals);
		return list.stream();
	}
	
	public static <E> Stream<E> filterNull(E... vals) {
		return toStream(vals).filter(o -> o != null);
	}
	
	public static <E> Stream<E> filterNull(Predicate<? super E> filter, E... vals) {
		Objects.requireNonNull(filter);
		return filterNull(vals).filter(filter);
	}
	
	public static <E> Stream<E> filterNull(List<E> list) {
		//Objects.requireNonNull(list);
		return list.stream().filter(o -> o != null);
	}
	
	public static <E> Stream<E> filterNull(Predicate<? super E> filter, List<E> list) {
		//Objects.requireNonNull(filter);
		return filterNull(list).filter(filter);
	}
	
	public static <E> void filterNullDo(Consumer<? super E> action, E... vals) {
		//Objects.requireNonNull(action);
		filterNull(vals).forEach(action);
	}
	
	public static <E> void filterNullDo(Predicate<? super E> filter, Consumer<? super E> action, E... vals) {
		//Objects.requireNonNull(filter);
		//Objects.requireNonNull(action);
		filterNull(vals).filter(filter).forEach(action);
	}
	
	public static <E> void filterNullDo(Consumer<? super E> action, List<E> list) {
		//Objects.requireNonNull(action);
		filterNull(list).forEach(action);
	}
	
	public static <E> void filterNullDo(Predicate<? super E> filter, Consumer<? super E> action, List<E> list) {
		//Objects.requireNonNull(filter);
		//Objects.requireNonNull(action);
		filterNull(list).filter(filter).forEach(action);
	}
	
	public static <E> Stream<E> filter(Predicate<? super E> filter, E... vals) {
		Objects.requireNonNull(filter);
		return toStream(vals).filter(filter);
	}
	
	public static <E> Stream<E> filter(Predicate<? super E> filter, List<E> list) {
		//Objects.requireNonNull(filter);
		//Objects.requireNonNull(list);
		return list.stream().filter(filter);
	}
	
	public static <E> void filterDo(Predicate<? super E> filter, Consumer<? super E> action, E... vals) {
		//Objects.requireNonNull(filter);
		//Objects.requireNonNull(action);
		filter(filter, vals).forEach(action);
	}
	
	public static <E> void filterDo(Predicate<? super E> filter, Consumer<? super E> action, List<E> list) {
		//Objects.requireNonNull(action);
		filter(filter, list).forEach(action);
	}
	
	public static <E> void doForEach(Consumer<? super E> action, E... vals) {
		forEach(vals, action);
	}
	
	public static <E> void ifForEach(boolean check, List<E> list, Consumer<? super E> action) {
		if (check) {
			if (list != null) { list.forEach(action); }
		}
	}
	
	public static <E> void forEach(E[] arr, Consumer<? super E> action) {
		//Objects.requireNonNull(action);
		for (E e : arr) {
			action.accept(e);
		}
	}
	
	public static <E, R> R forEachReturn(E[] arr, Consumer<? super E> action, R returnVal) {
		forEach(arr, action);
		return returnVal;
	}
	
	public static <E, R> R ifDoAndReturn(boolean check, Runnable ifTrue, Runnable ifFalse, R returnVal) {
		if (check && ifTrue != null) { ifTrue.run(); }
		else if (ifFalse != null) { ifFalse.run(); }
		return returnVal;
	}
	
	public static <E, R> R nullDoReturn(E object, Consumer<? super E> action, R returnVal) {
		//Objects.requireNonNull(action);
		if (object != null) { action.accept(object); }
		return returnVal;
	}
	
	public static <E, A, R> R nullDoReturn(E object1, A object2, BiConsumer<? super E, ? super A> action, R returnVal) {
		//Objects.requireNonNull(action);
		if (object1 != null && object2 != null) { action.accept(object1, object2); }
		return returnVal;
	}
	
	/** A statement that performs the following action on the given object if the object is not null. */
	public static <E> boolean ifNotNullDo(E object, Consumer<? super E> action) {
		//Objects.requireNonNull(action);
		if (object != null) { action.accept(object); return true; }
		return false;
	}
	
	public static <E, A> boolean ifNotNullDo(E object1, A object2, BiConsumer<? super E, ? super A> action) {
		//Objects.requireNonNull(action);
		if (object1 != null && object2 != null) { action.accept(object1, object2); return true; }
		return false;
	}
	
	/** A statement that returns the result of a given function if the given object is not null. */
	public static <E, R> R ifNotNullReturn(E object, Function<? super E, R> function, R defaultVal) {
		//Objects.requireNonNull(function);
		return object != null ? function.apply(object) : defaultVal;
	}
	
	public static <E, A, R> R ifNotNullReturn(E object1, A object2, BiFunction<? super E, ? super A, R> function, R defaultVal) {
		//Objects.requireNonNull(function);
		return (object1 != null && object2 != null) ? function.apply(object1, object2) : defaultVal;
	}
	
}
