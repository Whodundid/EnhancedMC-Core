package com.Whodundid.core.util.guiUtil;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.types.EnhancedGui;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.storageUtil.EArrayList;
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

/** A helper class used to display WindowParents and GuiScreens. */
public class GuiOpener {
	
	public static void openGui(Class guiIn) throws Exception { openGui(guiIn, null, null, null, CenterType.screen); }
	public static void openGui(Class guiIn, IWindowParent old) throws Exception { openGui(guiIn, null, null, old, CenterType.screen); }
	public static void openGui(Class guiIn, CenterType typeIn) throws Exception { openGui(guiIn, null, null, null, typeIn); }
	public static void openGui(Class guiIn, IWindowParent old, CenterType typeIn) throws Exception { openGui(guiIn, null, null, old, typeIn); }
	public static void openGui(Class guiIn, Class[] paramTypes, Object[] paramValues) throws Exception { openGui(guiIn, null, null, null, CenterType.screen); }
	public static void openGui(Class guiIn, Class[] paramTypes, Object[] paramValues, IWindowParent old) throws Exception { openGui(guiIn, null, null, old, CenterType.screen); }
	public static void openGui(Class guiIn, Class[] paramTypes, Object[] paramValues, CenterType typeIn) throws Exception { openGui(guiIn, null, null, null, CenterType.screen); }
		public static void openGui(Class guiIn, Class[] paramTypes, Object[] paramValues, IWindowParent old, CenterType typeIn) throws Exception {
		if (guiIn != null) {
			if (!testForVanillaGui(guiIn)) {
				
				Object obj = getObject(guiIn, paramTypes, paramValues);
				
				if (obj != null) {
					if (obj instanceof WindowParent) {
						EnhancedMC.displayEGui((WindowParent) obj, old, typeIn);
					}
					else if (obj instanceof EnhancedGui) {
						EnhancedMC.displayEGui((EnhancedGui) obj, old, typeIn);
					}
					else if (obj instanceof GuiScreen) {
						Minecraft.getMinecraft().displayGuiScreen((GuiScreen) obj);
					}
				}
			}
		}
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
		EArrayList<Class> types = null;
		EArrayList values = null;
		Minecraft mc = Minecraft.getMinecraft();
		
		if (gui.isAssignableFrom(GuiMultiplayer.class)) { types = new EArrayList(GuiScreen.class); values = new EArrayList().add(null); }
		if (gui.isAssignableFrom(GuiCustomizeSkin.class)) { types = new EArrayList(GuiScreen.class); values = new EArrayList().add(null); }
		if (gui.isAssignableFrom(GuiScreenResourcePacks.class)) { types = new EArrayList(GuiScreen.class); values = new EArrayList().add(null); }
		
		if (gui.isAssignableFrom(GuiOptions.class)) { types = new EArrayList(GuiScreen.class, GameSettings.class); values = new EArrayList().add(null, mc.gameSettings); }
		if (gui.isAssignableFrom(GuiVideoSettings.class)) { types = new EArrayList(GuiScreen.class, GameSettings.class); values = new EArrayList().add(null, mc.gameSettings); }
		if (gui.isAssignableFrom(ScreenChatOptions.class)) { types = new EArrayList(GuiScreen.class, GameSettings.class); values = new EArrayList().add(null, mc.gameSettings); }
		if (gui.isAssignableFrom(GuiScreenOptionsSounds.class)) { types = new EArrayList(GuiScreen.class, GameSettings.class); values = new EArrayList().add(null, mc.gameSettings); }
		if (gui.isAssignableFrom(GuiSnooper.class)) { types = new EArrayList(GuiScreen.class, GameSettings.class); values = new EArrayList().add(null, mc.gameSettings); }
		
		if (gui.isAssignableFrom(GuiLanguage.class)) { types = new EArrayList(GuiScreen.class, GameSettings.class, LanguageManager.class); values = new EArrayList().add(null, mc.gameSettings, mc.getLanguageManager()); }
		
		if (types != null && values != null) {
			Class[] typesC = new Class[types.size()];
			for (int i = 0; i < types.size(); i++) { typesC[i] = types.get(i); }
			
			mc.displayGuiScreen((GuiScreen) Class.forName(gui.getName()).getConstructor(typesC).newInstance(values.toArray()));
			return true;
		}
		return false;
	}
}
