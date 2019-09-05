package com.Whodundid.core.util.guiUtil;

import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiCustomizeSkin;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMainMenu;
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

/** A holder class containing a repository of useful GuiScreens in vanilla minecraft. */
public class CommonVanillaGuis {
	
	private static StorageBoxHolder<Class, StorageBox<Class[], Object[]>> vanillaGuis;
	static CommonVanillaGuis instance;
	
	private CommonVanillaGuis() {
		vanillaGuis = new StorageBoxHolder();
		vanillaGuis.add(GuiChat.class, null);
		vanillaGuis.add(GuiIngameMenu.class, null);
		vanillaGuis.add(GuiMainMenu.class, null);
		
		vanillaGuis.add(GuiMultiplayer.class, new StorageBox<Class[], Object[]>());
		vanillaGuis.add(GuiCustomizeSkin.class, new StorageBox<Class[], Object[]>());
		vanillaGuis.add(GuiScreenResourcePacks.class, new StorageBox<Class[], Object[]>());
		
		StorageBox box = new StorageBox<Class[], Object[]>();
		box.setObject(new Class[] { GuiScreen.class, GameSettings.class });
		box.setValue(new Object[] { null, Minecraft.getMinecraft().gameSettings });
		
		vanillaGuis.add(GuiOptions.class, box);
		vanillaGuis.add(GuiVideoSettings.class, box);
		vanillaGuis.add(ScreenChatOptions.class, box);
		vanillaGuis.add(GuiScreenOptionsSounds.class, box);
		vanillaGuis.add(GuiSnooper.class, box);
		
		box.setObject(new Class[] { GuiScreen.class, GameSettings.class, LanguageManager.class });
		box.setValue(new Object[] { null, Minecraft.getMinecraft().gameSettings, Minecraft.getMinecraft().getLanguageManager() });
		vanillaGuis.add(GuiLanguage.class, box);
	}
	
	public static CommonVanillaGuis createRepo() {
		instance = new CommonVanillaGuis();
		return instance;
	}
	
	public static StorageBoxHolder<Class, StorageBox<Class[], Object[]>> getGuis() {
		if (instance == null || vanillaGuis == null) {
			createRepo();
		}
		return vanillaGuis;
	}
}
