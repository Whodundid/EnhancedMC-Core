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
import com.Whodundid.core.util.storageUtil.EArrayList;
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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

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
	public final Resources resources = new Resources();
	
	@EventHandler
    private void init(FMLInitializationEvent event) {
		//register EventListener
		MinecraftForge.EVENT_BUS.register(eventListener = new EventListener());
		
    	//register keybinds
    	ClientRegistry.registerKeyBinding(openSettingsGui);
    	ClientRegistry.registerKeyBinding(debugCommand);
    	
    	//initialize client resources
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
		
		EArrayList<SubMod> foundMods = new EArrayList();
		EArrayList<SubMod> coreCheck = new EArrayList();
		EArrayList<SubMod> depCheck = new EArrayList();
		foundMods.add(modInstance);
		
		//find any EMC SubMods in forge loaded mods
		for (ModContainer c : Loader.instance().getModList()) {
			Object m = c.getMod();
			if (m instanceof SubMod) {
				c.getMetadata().parent = MODID; //assign the subMod as a child of EMC to fore
				foundMods.add((SubMod) m);
			}
		}
		
		//process found mods
		for (SubMod m : foundMods) {
			try {
				//check for incompatibility
				boolean incompatible = false;
				
				int numDeps = m.getDependencies().size();
				boolean incompatDep = false;
				int foundDepMods = 0;
				int matchingVers = 0;
				
				for (StorageBox<String, String> box : m.getDependencies()) {
					String modName = box.getObject();
					String modVer = box.getValue();
					
					SubMod dep = getMod(modName, foundMods);
					if (dep != null) {
						foundDepMods += 1;
						if (modVer.equals(dep.getVersion())) { matchingVers += 1; }
					}
				}
				
				if (foundDepMods != numDeps || matchingVers != numDeps) { incompatible = true; }
				
				//set incompatibility state
				m.setIncompatible(incompatible);
				
				//register the subMod into core
				coreCheck.add(m);
			}
			catch (Exception q) {
				log(Level.INFO, "Error trying to read submod: " + m + "!");
				q.printStackTrace();
			}
		}
		
		//check processed mods for subMod incompatibilities
		for (SubMod m : coreCheck) {
			//check if the processed mod was found incompatible with the core
			for (StorageBox<String, String> box : m.getDependencies()) {
				SubMod dep = getMod(box.getObject(), coreCheck);
				if (dep != null) {
					//set the mod incompat if one of it's dependencies was incompat too
					if (dep.isIncompatible()) { m.setIncompatible(true); }
				}
			}
			
			depCheck.add(m);
		}
		
		//finally register each processed mod
		for (SubMod m : depCheck) {
			RegisteredSubMods.registerSubMod(m);
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
		if (debugCommand.isPressed()) {
			if (!RegisteredSubMods.isModRegistered(SubModType.HOTKEYS)) {
				if (EnhancedMCMod.useDebugKey.get()) { DebugFunctions.runDebugFunction(0); }
			}
			else if (!RegisteredSubMods.getMod(SubModType.HOTKEYS).isEnabled()) {
				if (EnhancedMCMod.useDebugKey.get()) { DebugFunctions.runDebugFunction(0); }
			}
		}
	}
	
	public static boolean isDebugMode() { return enableDebugFunctions; }
	public static void setDebugMode(boolean val) { enableDebugFunctions = val; }
	
	public EventListener getEventListener() { return eventListener; }
	public static EFontRenderer getFontRenderer() { return fontRenderer; }
	public static ModdedInGameGui getInGameGui() { return enhancedMCGui; }
	public static EnhancedMCRenderer getRenderer() { return renderer; }
	public static boolean isInitialized() { return isInitialized; }
	public static void log(Level levelIn, String msg) { EMCLogger.log(levelIn, msg); }
	public static void info(String msg) { EMCLogger.log(Level.INFO, msg); }
	public static void error(String msg) { EMCLogger.log(Level.ERROR, msg); }
	public static void error(String msg, Throwable throwableIn) { EMCLogger.log(Level.ERROR, msg, throwableIn); }
	
	private static SubMod getMod(String modNameIn, EArrayList<SubMod> checkList) {
		for (SubMod m : checkList) {
			if (m.getName().equals(modNameIn)) { return m; }
		}
		return null;
	}
	
	protected static void createdByHunterBragg() {}
}
