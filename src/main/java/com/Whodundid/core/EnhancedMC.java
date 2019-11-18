package com.Whodundid.core;

import com.Whodundid.core.coreSubMod.EnhancedMCMod;
import com.Whodundid.core.debug.DebugFunctions;
import com.Whodundid.core.enhancedGui.types.EnhancedGui;
import com.Whodundid.core.enhancedGui.types.InnerEnhancedGui;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.events.EventListener;
import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.renderer.RendererProxyGui;
import com.Whodundid.core.settings.SettingsGuiMain;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.subMod.SubModSettings;
import com.Whodundid.core.subMod.SubModType;
import com.Whodundid.core.util.miscUtil.EFontRenderer;
import com.Whodundid.core.util.miscUtil.EMouseHelper;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.CursorHelper;
import com.Whodundid.core.util.renderUtil.Resources;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.StorageBox;
import java.io.File;
import java.util.Scanner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
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
public final class EnhancedMC {
	
	public static final String MODID = "enhancedmccore";
	public static final String VERSION = "1.0";
	public static final String NAME = "EnhancedMC";
	public static final Minecraft mc = Minecraft.getMinecraft();
	public static final KeyBinding openSettingsGui = new KeyBinding("Settings", Keyboard.KEY_P, "EnhancedMC");
	public static final KeyBinding debugCommand = new KeyBinding("Debug key", Keyboard.KEY_GRAVE, "EnhancedMC");
	public static final Logger EMCLogger = LogManager.getLogger("EnhancedMC");
	public static EFontRenderer fontRenderer;
	private static final EnhancedMCRenderer renderer = EnhancedMCRenderer.getInstance();
	private static EventListener eventListener;
	static boolean isInitialized = false;
	public static int updateCounter = 0;
	public static boolean enableDebugFunctions = false;
	public final EnhancedMCMod modInstance = new EnhancedMCMod();
	public final Resources resources = new Resources();
	public static boolean safeRemoteDesktopMode = false;
	
	@EventHandler
    private void init(FMLInitializationEvent event) {
		//register EventListener
		MinecraftForge.EVENT_BUS.register(eventListener = new EventListener());
		
		//safe rm check
		File checkRM = new File("checkRM");
		if (checkRM.exists()) {
			Scanner reader = null;
			try { reader = new Scanner(checkRM); if (reader.hasNext()) { safeRemoteDesktopMode = Boolean.parseBoolean(reader.next()); } }
			catch (Exception e) { e.printStackTrace(); }
			finally { if (reader != null) { reader.close(); } checkRM.delete(); }
		}
		
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
		if (openSettingsGui.isPressed()) { openSettingsGui(); }
		if (debugCommand.isPressed()) {
			if (!RegisteredSubMods.isModRegistered(SubModType.HOTKEYS)) {
				if (EnhancedMCMod.useDebugKey.get()) { DebugFunctions.runDebugFunction(0); }
			}
			else if (EnhancedMCMod.useDebugKey.get()) {
				DebugFunctions.runDebugFunction(0);
			}
		}
	}
	
	public static boolean isDebugMode() { return enableDebugFunctions; }
	public static void setDebugMode(boolean val) { enableDebugFunctions = val; }
	
	public static EventListener getEventListener() { return eventListener; }
	public static EFontRenderer getFontRenderer() { return fontRenderer; }
	public static EnhancedMCRenderer getRenderer() { return renderer; }
	public static boolean isInitialized() { return isInitialized; }
	public static void log(Level levelIn, String msg) { EMCLogger.log(levelIn, msg); }
	public static void info(String msg) { EMCLogger.log(Level.INFO, msg); }
	public static void error(String msg) { EMCLogger.log(Level.ERROR, msg); }
	public static void error(String msg, Throwable throwableIn) { EMCLogger.log(Level.ERROR, msg, throwableIn); }
	public static void openSettingsGui() { displayEGui(new SettingsGuiMain()); }
	
	public static <T extends InnerEnhancedGui> boolean isEGuiOpen(Class<T> guiIn) {
		return guiIn != null ? renderer.getAllChildren().stream().anyMatch(o -> o.getClass().equals(guiIn)) : false;
	}
	
	public static <T extends InnerEnhancedGui> InnerEnhancedGui getWindowInstance(Class<T> guiIn) {
		return guiIn != null ? (InnerEnhancedGui) renderer.getAllChildren().stream().filter(o -> o.getClass().equals(guiIn)).findFirst().get() : null;
	}
	
	public static void displayEGui(IWindowParent guiIn) { displayEGui(guiIn, null, false, CenterType.screen); }
	public static void displayEGui(IWindowParent guiIn, CenterType loc) { displayEGui(guiIn, null, false, loc); }
	public static void displayEGui(IWindowParent guiIn, Object oldObject) { displayEGui(guiIn, oldObject, true, CenterType.object); }
	public static void displayEGui(IWindowParent guiIn, Object oldObject, boolean transferHistory) { displayEGui(guiIn, oldObject, transferHistory, CenterType.object); }
	public static void displayEGui(IWindowParent guiIn, Object oldObject, CenterType loc) { displayEGui(guiIn, oldObject, true, loc); }
	public static void displayEGui(IWindowParent guiIn, Object oldObject, boolean transferHistory, CenterType loc) {
		if (guiIn == null) { mc.displayGuiScreen(null); }
		if (mc.currentScreen == null || !(mc.currentScreen instanceof RendererProxyGui)) { mc.displayGuiScreen(new RendererProxyGui()); }
		if (guiIn != null) {
			if (guiIn instanceof EnhancedGui) { mc.displayGuiScreen((EnhancedGui) guiIn); }
			else {
				renderer.addObject(guiIn);
				if (oldObject instanceof GuiScreen) { mc.displayGuiScreen(null); }
				else if (oldObject instanceof IWindowParent) { ((IWindowParent) oldObject).close(); }
			}
			if (transferHistory && oldObject instanceof IWindowParent) {
				IWindowParent old = (IWindowParent) oldObject;
				old.getGuiHistory().add(old);
				guiIn.setGuiHistory(old.getGuiHistory());
			}
			setPos(guiIn, oldObject instanceof IEnhancedGuiObject ? (IEnhancedGuiObject) oldObject : null, loc);
			guiIn.bringToFront();
			guiIn.requestFocus();
		}
	}
	
	private static void setPos(IWindowParent guiIn, IEnhancedGuiObject objectIn, CenterType typeIn) {
		ScaledResolution res = new ScaledResolution(mc);
		EDimension gDim = guiIn.getDimensions();
		int headerHeight = guiIn.hasHeader() ? guiIn.getHeader().height : 0;
		
		int sX = 0;
		int sY = 0;
		
		switch (typeIn) {
		case cursor:
			sX = EMouseHelper.mX - (gDim.width / 2);
			sY = EMouseHelper.mY - (gDim.height - headerHeight) / 2;
			break;
		case object:
			if (objectIn != null) {
				EDimension objDim = objectIn.getDimensions();
				sX = objDim.midX - (gDim.width / 2);
				sY = objDim.midY - (gDim.height / 2);
				break;
			}
		case objectCorner:
			if (objectIn != null) {
				EDimension objDim = objectIn.getDimensions();
				sX = objDim.startX;
				sY = objDim.startY;
				break;
			}
		case screen:
			sX = res.getScaledWidth() - gDim.startX - gDim.width;
			sY = res.getScaledHeight() - gDim.startY - gDim.height;
			break;
		default: break;
		}
		
		sX = sX < 0 ? 1 : sX;
		sY = (sY - headerHeight) < 2 ? 2 + headerHeight : sY;
		sX = sX + gDim.width > res.getScaledWidth() ? -1 + sX - (sX + gDim.width - res.getScaledWidth()) : sX;
		sY = sY + gDim.height > res.getScaledHeight() ? -2 + sY - (sY + gDim.height - res.getScaledHeight()) : sY;
		guiIn.setPosition(sX, sY);
	}
	
	private static SubMod getMod(String modNameIn, EArrayList<SubMod> checkList) {
		for (SubMod m : checkList) { if (m.getName().equals(modNameIn)) { return m; } }
		return null;
	}
	
	public boolean isModRegistered(SubMod modIn) { return RegisteredSubMods.isModRegistered(modIn); }
	public boolean isModRegistered(SubModType typeIn) { return RegisteredSubMods.isModRegistered(typeIn); }
	public boolean isModRegistered(String modName) { return RegisteredSubMods.isModRegEn(modName); }
	
	protected static void createdByHunterBragg() {}
}
