package com.Whodundid.core.util.guiUtil;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.windowLibrary.windowTypes.EnhancedGui;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowParent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCustomizeSkin;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenOptionsSounds;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.gui.GuiSnooper;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.gui.ScreenChatOptions;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;

//Author: Hunter Bragg

/** A helper class used to display WindowParents and GuiScreens. */
public class GuiOpener {
	
	public static Object openGui(Class guiIn) throws Exception { return openGui(guiIn, null, null, null, false, CenterType.screen); }
	public static Object openGui(Class guiIn, IWindowParent old) throws Exception { return openGui(guiIn, null, null, old, false, CenterType.screen); }
	public static Object openGui(Class guiIn, IWindowParent old, boolean passOld) throws Exception { return openGui(guiIn, null, null, old, passOld, CenterType.screen); }
	public static Object openGui(Class guiIn, CenterType typeIn) throws Exception { return openGui(guiIn, null, null, null, false, typeIn); }
	public static Object openGui(Class guiIn, IWindowParent old, CenterType typeIn) throws Exception { return openGui(guiIn, null, null, old, false, typeIn); }
	public static Object openGui(Class guiIn, IWindowParent old, boolean passOld, CenterType typeIn) throws Exception { return openGui(guiIn, null, null, old, passOld, typeIn); }
	public static Object openGui(Class guiIn, Class[] paramTypes, Object[] paramValues) throws Exception { return openGui(guiIn, null, null, null, false, CenterType.screen); }
	public static Object openGui(Class guiIn, Class[] paramTypes, Object[] paramValues, IWindowParent old) throws Exception { return openGui(guiIn, null, null, old, false, CenterType.screen); }
	public static Object openGui(Class guiIn, Class[] paramTypes, Object[] paramValues, IWindowParent old, boolean passOld) throws Exception { return openGui(guiIn, null, null, old, passOld, CenterType.screen); }
	public static Object openGui(Class guiIn, Class[] paramTypes, Object[] paramValues, CenterType typeIn) throws Exception { return openGui(guiIn, null, null, null, false, CenterType.screen); }
	public static Object openGui(Class guiIn, Class[] paramTypes, Object[] paramValues, IWindowParent old, CenterType typeIn) throws Exception { return openGui(guiIn, null, null, null, false, CenterType.screen); }
	public static Object openGui(Class guiIn, Class[] paramTypes, Object[] paramValues, IWindowParent old, boolean passOld, CenterType typeIn) throws Exception {
		if (guiIn != null) {
			if (!testForVanillaGui(guiIn)) {
				
				Object obj = getObject(guiIn, paramTypes, paramValues);
				
				if (obj != null) {
					if (obj instanceof WindowParent) {
						return EnhancedMC.displayWindow((WindowParent) obj, old, true, true, passOld, typeIn);				
					}
					else if (obj instanceof EnhancedGui) {
						return EnhancedMC.displayWindow((EnhancedGui) obj, old, true, false, passOld, typeIn);
					}
					else if (obj instanceof GuiScreen) {
						Minecraft.getMinecraft().displayGuiScreen((GuiScreen) obj);
						return Minecraft.getMinecraft().currentScreen;
					}
				}
			}
		}
		return null;
	}
	
	public static GuiScreen createGuiScreenInstance(Class gui) {
		try {
			if (gui != null) {
				EArrayList<Class> types = null;
				EArrayList values = null;
				Minecraft mc = Minecraft.getMinecraft();
				
				if (gui.isAssignableFrom(GuiMultiplayer.class)) { types = new EArrayList(GuiScreen.class); values = new EArrayList(mc.currentScreen); }
				if (gui.isAssignableFrom(GuiCustomizeSkin.class)) { types = new EArrayList(GuiScreen.class); values = new EArrayList(mc.currentScreen); }
				if (gui.isAssignableFrom(GuiScreenResourcePacks.class)) { types = new EArrayList(GuiScreen.class); values = new EArrayList(mc.currentScreen); }
				
				if (gui.isAssignableFrom(GuiOptions.class)) { types = new EArrayList(GuiScreen.class, GameSettings.class); values = new EArrayList().add(mc.currentScreen, mc.gameSettings); }
				if (gui.isAssignableFrom(GuiVideoSettings.class)) { types = new EArrayList(GuiScreen.class, GameSettings.class); values = new EArrayList().add(mc.currentScreen, mc.gameSettings); }
				if (gui.isAssignableFrom(ScreenChatOptions.class)) { types = new EArrayList(GuiScreen.class, GameSettings.class); values = new EArrayList().add(mc.currentScreen, mc.gameSettings); }
				if (gui.isAssignableFrom(GuiScreenOptionsSounds.class)) { types = new EArrayList(GuiScreen.class, GameSettings.class); values = new EArrayList().add(mc.currentScreen, mc.gameSettings); }
				if (gui.isAssignableFrom(GuiSnooper.class)) { types = new EArrayList(GuiScreen.class, GameSettings.class); values = new EArrayList().add(mc.currentScreen, mc.gameSettings); }
				
				if (gui.isAssignableFrom(GuiLanguage.class)) { types = new EArrayList(GuiScreen.class, GameSettings.class, LanguageManager.class); values = new EArrayList().add(mc.currentScreen, mc.gameSettings, mc.getLanguageManager()); }
				
				if (types != null && values != null) {
					Class[] typesC = new Class[types.size()];
					for (int i = 0; i < types.size(); i++) { typesC[i] = types.get(i); }
					return (GuiScreen) Class.forName(gui.getName()).getConstructor(typesC).newInstance(values.toArray());
				}
				
				Object o = Class.forName(gui.getName()).getConstructor().newInstance();
				if (o instanceof GuiScreen) { return (GuiScreen) o; }
			}
		}
		catch (Exception e) { e.printStackTrace(); }
		
		return null;
	}
	
	private static Object getObject(Class gui, Class[] paramTypes, Object[] paramValues) throws Exception {
		if (paramTypes != null) {
			if (paramValues != null) {
				return Class.forName(gui.getName()).getConstructor(paramTypes).newInstance(paramValues);
			}
			throw new IllegalArgumentException("Missing param values!");
		}
		return Class.forName(gui.getName()).getConstructor().newInstance();
	}
	
	private static boolean testForVanillaGui(Class gui) throws Exception {
		Minecraft mc = Minecraft.getMinecraft();
		GuiScreen screen = createGuiScreenInstance(gui);
		
		if (screen != null) {
			mc.displayGuiScreen(screen);
			return true;
		}
		
		return false;
	}
	
}
