package com.Whodundid.core;

import com.Whodundid.core.coreSubMod.EnhancedMCMod;
import com.Whodundid.core.debug.DebugFunctions;
import com.Whodundid.core.events.EventListener;
import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.settings.SettingsGuiMain;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.subMod.SubModSettings;
import com.Whodundid.core.subMod.SubModType;
import com.Whodundid.core.util.ModdedInGameGui;
import com.Whodundid.core.util.miscUtil.EFontRenderer;
import com.Whodundid.core.util.renderUtil.CursorHelper;
import com.Whodundid.core.util.renderUtil.Resources;
import com.Whodundid.core.util.storageUtil.StorageBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import java.math.BigDecimal;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

//Jan 10, 2019
//Last edited: Feb 18, 2019
//Edit note: added 'isInitialized' boolean flag which indicates if all of EnhancedMC's dependancies have fully loaded.
//First Added: Nov 16, 2017
//Author: Hunter Bragg

@Mod(modid = EnhancedMC.MODID, version = EnhancedMC.VERSION, name = EnhancedMC.NAME)
public class EnhancedMC {
	
	public static final String MODID = "enhancedmccore";
	public static final String VERSION = "1.0";
	public static final String NAME = "EnhancedMC Core";
	public static final Minecraft mc = Minecraft.getMinecraft();
	public static final KeyBinding openSettingsGui = new KeyBinding("Settings", Keyboard.KEY_P, "EnhancedMC");
	public static final KeyBinding debugCommand = new KeyBinding("Debug key", Keyboard.KEY_GRAVE, "EnhancedMC");
	public static final Logger EMCLogger = LogManager.getLogger("EnhancedMC");
	public static EFontRenderer fontRenderer;
	private static final EnhancedMCRenderer renderer = new EnhancedMCRenderer();
	public static ModdedInGameGui enhancedMCGui;
	private EventListener eventListener;
	static boolean isInitialized = false;
	public static int updateCounter = 0;
	public static boolean enableDebugFunctions = false;
	public final EnhancedMCMod modInstance = new EnhancedMCMod();
	
	@EventHandler
    private void init(FMLInitializationEvent event) {
		//register EventListener
		MinecraftForge.EVENT_BUS.register(eventListener = new EventListener());
		
    	//register keybinds
    	ClientRegistry.registerKeyBinding(openSettingsGui);
    	ClientRegistry.registerKeyBinding(debugCommand);
    	
    	//initialize client resources
    	new Resources();
    	CursorHelper.init();
    	fontRenderer = new EFontRenderer(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.renderEngine, false);
		if (mc.gameSettings.language != null) {
            fontRenderer.setUnicodeFlag(mc.isUnicode());
            fontRenderer.setBidiFlag(mc.getLanguageManager().isCurrentLanguageBidirectional());
        }
		((IReloadableResourceManager) mc.getResourceManager()).registerReloadListener(fontRenderer);
		
		//create modded hud instance
		enhancedMCGui = new ModdedInGameGui(mc);
		mc.ingameGUI = enhancedMCGui;
    	
    	//register commands
    	ClientCommandHandler h = ClientCommandHandler.instance;
	}
	
	@EventHandler
	private void postInit(FMLPostInitializationEvent e) {
		if (mc.getSession() != null && mc.getSession().getUsername() != null && mc.getSession().getUsername().equals("Jenarie")) {
			Display.setTitle("Alt Acc");
		}
		
		//register core subMod instance
    	RegisteredSubMods.registerSubMod(modInstance);
		
		BigDecimal coreVersion = new BigDecimal(VERSION);
		//find and register EMC sub mods into EMC core
		for (ModContainer c : Loader.instance().getModList()) {
			Object m = c.getMod();
			if (m instanceof SubMod) {
				//try to register the sub mod
				SubMod instance = null;
				boolean incompatible = false;
				try {
					//check for incompatibility
					StorageBox<SubModType, BigDecimal> box = ((SubMod) m).getDependencies().getBoxWithObj(SubModType.CORE);
					if (box != null) {
						BigDecimal reqCoreVersion = box.getValue();
						if (reqCoreVersion.compareTo(coreVersion) != 0) {
							incompatible = true;
						}
					}
					
					instance = ((SubMod) m).getInstance();
				} catch (Exception q) {
					log(Level.INFO, "Error trying to read submod: " + m + "!");
					q.printStackTrace();
				}
				if (instance != null) {
					instance.setIncompatible(incompatible);
					RegisteredSubMods.registerSubMod(instance);
					c.getMetadata().parent = MODID; //assign the subMod as a child of EMC to fore
				}
			}
		}
		
		//attempt to load each of the found subMod's config files (if they exist)
		for (SubMod m : RegisteredSubMods.getRegisteredModsList()) {
			try {
				if (m.hasConfig()) { m.getConfig().loadAllConfigs(); m.getConfig().saveAllConfigs(); }
			} catch (NullPointerException q) {
				log(Level.INFO, "Error tying to load subMod: " + m + " config!");
				q.printStackTrace();
			}
		}
		
		//send postInit update to each loaded subMod
		for (SubMod m : RegisteredSubMods.getRegisteredModsList()) {
			try {
				m.onPostInit();
			} catch (Exception q) {
				log(Level.INFO, "Error trying to run postInit on submod: " + m + "!");
				q.printStackTrace();
			}
		}
		
		try {
			//attempt to load the subMod settings file
			SubModSettings.loadConfig();
		} catch (Exception q) {
			log(Level.INFO, "Error trying to load global config!");
		}
		
		isInitialized = true;
	}
	
	public static void checkKeyBinds() {
		if (openSettingsGui.isPressed()) { mc.displayGuiScreen(new SettingsGuiMain()); }
		if (debugCommand.isPressed() && !RegisteredSubMods.isModRegistered(SubModType.HOTKEYS)) { DebugFunctions.runDebugFunction(0); }
	}
	
	public static boolean isDebugMode() { return enableDebugFunctions; }
	public static void setDebugMode(boolean val) { enableDebugFunctions = val; }
	
	public EventListener getEventListener() { return eventListener; }
	public static EFontRenderer getFontRenderer() { return fontRenderer; }
	public static ModdedInGameGui getInGameGui() { return enhancedMCGui; }
	public static EnhancedMCRenderer getRenderer() { return renderer; }
	public static boolean isInitialized() { return isInitialized; }
	public static void log(Level levelIn, String msg) { EMCLogger.log(levelIn, msg); }
	protected static void createdByHunterBragg() {}
}
