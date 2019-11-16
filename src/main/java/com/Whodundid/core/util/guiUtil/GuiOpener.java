package com.Whodundid.core.util.guiUtil;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.types.InnerEnhancedGui;
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

public class GuiOpener {
	
	public static void openGui(Class guiIn) throws Exception { openGui(guiIn, null, null); }
	public static void openGui(Class guiIn, Class[] paramTypes, Object[] paramValues) throws Exception {
		if (guiIn != null) {
			if (!testForVanillaGui(guiIn)) {
				
				System.out.println(guiIn + " ");
				
				Object obj = getObject(guiIn, paramTypes, paramValues);
				
				if (obj != null) {
					if (obj instanceof InnerEnhancedGui) {
						EnhancedMC.displayEGui((InnerEnhancedGui) obj);
					}
					else if (obj instanceof GuiScreen) {
						Minecraft.getMinecraft().displayGuiScreen((GuiScreen) obj);
					}
				}
			}
			else { Minecraft.getMinecraft().displayGuiScreen((GuiScreen) Class.forName(guiIn.getName()).getConstructor().newInstance()); }
		}
	}
	
	private static Object getObject(Class gui, Class[] paramTypes, Object[] paramValues) throws Exception {
		if (paramTypes != null) {
			if (paramValues != null) {
				return Class.forName(gui.getName()).getConstructor(paramTypes).newInstance(paramValues);
			}
			else { throw new IllegalArgumentException("Missing param values!"); }
		}
		else {
			return Class.forName(gui.getName()).getConstructor().newInstance();
		}
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
