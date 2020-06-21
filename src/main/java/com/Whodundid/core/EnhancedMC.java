package com.Whodundid.core;

import com.Whodundid.clearVisuals.ClearVisualsApp;
import com.Whodundid.core.app.AppLoader;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.coreApp.CoreApp;
import com.Whodundid.core.coreEvents.EventListener;
import com.Whodundid.core.coreEvents.emcEvents.RendererRCMOpenEvent;
import com.Whodundid.core.coreEvents.emcEvents.WindowOpenedEvent;
import com.Whodundid.core.notifications.NotificationHandler;
import com.Whodundid.core.notifications.util.NotificationObject;
import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.renderer.renderUtil.IRendererProxy;
import com.Whodundid.core.renderer.renderUtil.RendererProxyGui;
import com.Whodundid.core.renderer.renderUtil.RendererRCM;
import com.Whodundid.core.renderer.taskView.TaskBar;
import com.Whodundid.core.settings.SettingsWindowMain;
import com.Whodundid.core.terminal.TerminalCommandHandler;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.hypixel.HypixelData;
import com.Whodundid.core.util.miscUtil.EMouseHelper;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.CursorHelper;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.windowLibrary.WindowObjectS;
import com.Whodundid.core.windowLibrary.windowTypes.EnhancedGui;
import com.Whodundid.core.windowLibrary.windowTypes.OverlayWindow;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowParent;
import com.Whodundid.hotkeys.HotKeyApp;
import com.Whodundid.pingDrawer.PingApp;
import com.Whodundid.playerInfo.PlayerInfoApp;
import com.Whodundid.slc.SLCApp;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

//Project Started: April 14, 2017
//Author: Hunter Bragg

//@Mod(modid = EnhancedMC.MODID, version = EnhancedMC.VERSION, name = EnhancedMC.NAME)
public class EnhancedMC extends DummyModContainer {
	
	public static final String MODID = "enhancedmc";
	public static final String VERSION = "1.0.1";
	public static final String NAME = "EnhancedMC";
	public static final Minecraft mc = Minecraft.getMinecraft();
	public static final KeyBinding openSettingsGui = new KeyBinding("Settings", Keyboard.KEY_P, "EnhancedMC");
	public static final KeyBinding openHud = new KeyBinding("Open Hud", 0, "EnhancedMC");
	public static final Logger EMCLogger = LogManager.getLogger("EnhancedMC");
	private static final EnhancedMCRenderer renderer = EnhancedMCRenderer.getInstance();
	private static final TerminalCommandHandler terminal = TerminalCommandHandler.getInstance();
	private static final NotificationHandler notifications = NotificationHandler.getInstance();
	private static final RegisteredApps appsList = RegisteredApps.getInstance();
	private static EventListener eventListener = EventListener.getInstance();
	private static boolean isInitialized = false;
	public static int updateCounter = 0;
	public static boolean enableDebugFunctions = false;
	public static final CoreApp appInstance = new CoreApp();
	public static boolean enableDevFunctions = false;
	private static boolean isDev = false;
	private static boolean deobf = false;
	
	public EnhancedMC() {
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = MODID;
		meta.version = VERSION;
		meta.name = NAME;
		meta.credits = "EMC backend containing library functions, window-based environment kit, hud interaction, and primary event distribution."
					 + "\nASM Modifications: Minor fixes for MC and implementation of additional event hooks."
					 + "\nArt assets created by Mr.JamminOtter";
		meta.description = "The core mod of EnhancedMC";
		meta.authorList = Arrays.asList("Whodundid", "Mr.JamminOtter");
		meta.url = "https://github.com/Whodundid/EnhancedMC-Core";
	}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		
		try {
			deobf = Launch.classLoader.getClassBytes("net.minecraft.world.World") != null;
		}
		catch (Exception e) { e.printStackTrace(); }
		
		return true;
	}
	
	@Subscribe
	public void init(FMLInitializationEvent event) {
		info("Initializing EMC");
		
		//register EventListener
		MinecraftForge.EVENT_BUS.register(eventListener);
		
		//register keybinds
		ClientRegistry.registerKeyBinding(openSettingsGui);
		ClientRegistry.registerKeyBinding(openHud);
		
		//initialize client resources
		CursorHelper.init();
		
		//register commands
		ClientCommandHandler h = ClientCommandHandler.instance;
	}
	
	@Subscribe
	public void postInit(FMLPostInitializationEvent e) {
		
		String id = mc.getSession() != null ? mc.getSession().getProfile().getId().toString() : "";
		
		isDev = id.equals("be8ba059-2644-4f4c-a5e7-88a38e555b1e") || id.equals("e8f9070f-74f0-4229-8134-5857c794e44d");
		if (enableDevFunctions = isDev) { info("Dev detected -- Running EMC in Dev mode!"); }
		
		//load EMC apps
		AppLoader.loadApps();
		
		//register all commands within the terminal
		terminal.initCommands();
		
		//load currently active notifications
		notifications.loadConfig();
		
		isInitialized = true;
	}
	
	/** Used for rebuilding bundled apps when they are reloaded. */
	public static EArrayList<EMCApp> getBundledApps() {
		EArrayList<EMCApp> apps = new EArrayList();
		
		//create bundled apps
		ClearVisualsApp cv = new ClearVisualsApp();
		HotKeyApp hk = new HotKeyApp();
		PingApp pd = new PingApp();
		PlayerInfoApp pi = new PlayerInfoApp();
		SLCApp slc = new SLCApp();
		
		return apps.addAll(cv, hk, pd, pi, slc);
	}
	
	public static void checkKeyBinds() {
		if (openSettingsGui.isPressed()) { openSettingsGui(); }
		if (openHud.isPressed()) {
			if (mc.currentScreen instanceof IRendererProxy) { mc.displayGuiScreen(null); }
			else { mc.displayGuiScreen(new RendererProxyGui(true)); }
		}
	}
	
	public static <T extends WindowParent> boolean isEGuiOpen(Class<T> windowIn) {
		return windowIn != null ? renderer.getAllChildren().stream().anyMatch(o -> o.getClass() == windowIn) : false;
	}
	
	public static EArrayList<WindowParent> getAllActiveWindows() {
		EArrayList<WindowParent> windows = new EArrayList();
		try {
			renderer.getAllChildren().stream().filter(o -> WindowParent.class.isInstance(o)).filter(o -> !o.isBeingRemoved()).forEach(w -> windows.add((WindowParent) w));
		}
		catch (Exception e) { e.printStackTrace(); }
		return windows;
	}
	
	public static <T extends WindowParent> WindowParent getWindowInstance(Class<T> windowIn) {
		return windowIn != null ? (WindowParent) renderer.getAllChildren().stream().filter(o -> o.getClass() == windowIn).findFirst().get() : null;
	}
	
	public static <T extends WindowParent> EArrayList<T> getAllWindowInstances(Class<T> windowIn) {
		EArrayList<T> windows = new EArrayList();
		try {
			renderer.getAllChildren().stream().filter(o -> o.getClass() == windowIn).filter(o -> !o.isBeingRemoved()).forEach(w -> windows.add((T) w));
		}
		catch (Exception e) { e.printStackTrace(); }
		return windows;
	}
	
	public static void reloadAllWindows() { getAllActiveWindows().forEach(w -> w.sendArgs("Reload")); }
	public static void reloadAllWindows(Object... args) { getAllActiveWindows().forEach(w -> w.sendArgs("Reload", args)); }
	
	public static IWindowParent displayWindow(IWindowParent windowIn) { return displayWindow(windowIn, null, true, false, false, CenterType.screen); }
	public static IWindowParent displayWindow(IWindowParent windowIn, CenterType loc) { return displayWindow(windowIn, null, true, false, false, loc); }
	public static IWindowParent displayWindow(IWindowParent windowIn, boolean transferFocus) { return displayWindow(windowIn, null, transferFocus, false, false, CenterType.screen); }
	public static IWindowParent displayWindow(IWindowParent windowIn, boolean transferFocus, CenterType loc) { return displayWindow(windowIn, null, transferFocus, false, false, loc); }
	public static IWindowParent displayWindow(IWindowParent windowIn, Object oldObject) { return displayWindow(windowIn, oldObject, true, true, true, CenterType.object); }
	public static IWindowParent displayWindow(IWindowParent windowIn, Object oldObject, CenterType loc) { return displayWindow(windowIn, oldObject, true, true, true, loc); }
	public static IWindowParent displayWindow(IWindowParent windowIn, Object oldObject, boolean transferFocus) { return displayWindow(windowIn, oldObject, transferFocus, true, true, CenterType.object); }
	public static IWindowParent displayWindow(IWindowParent windowIn, Object oldObject, boolean transferFocus, CenterType loc) { return displayWindow(windowIn, oldObject, transferFocus, true, true, loc); }
	public static IWindowParent displayWindow(IWindowParent windowIn, Object oldObject, boolean transferFocus, boolean closeOld) { return displayWindow(windowIn, oldObject, transferFocus, closeOld, true, CenterType.object); }
	public static IWindowParent displayWindow(IWindowParent windowIn, Object oldObject, boolean transferFocus, boolean closeOld, boolean transferHistory) { return displayWindow(windowIn, oldObject, transferFocus, closeOld, transferHistory, CenterType.object); }
	public static IWindowParent displayWindow(IWindowParent windowIn, Object oldObject, boolean transferFocus, boolean closeOld, boolean transferHistory, CenterType loc) {
		if (windowIn == null) { mc.displayGuiScreen(null); }
		if (mc.currentScreen == null || !(mc.currentScreen instanceof RendererProxyGui)) { mc.displayGuiScreen(new RendererProxyGui()); }
		if (windowIn != null) {
			if (windowIn instanceof EnhancedGui) { mc.displayGuiScreen((EnhancedGui) windowIn); }
			else {
				if (windowIn instanceof WindowParent) { MinecraftForge.EVENT_BUS.post(new WindowOpenedEvent((WindowParent) windowIn)); }
				if (windowIn instanceof RendererRCM) {
					if (MinecraftForge.EVENT_BUS.post(new RendererRCMOpenEvent((RendererRCM) windowIn))) { return windowIn; }
				}
				if (oldObject instanceof GuiScreen) { mc.displayGuiScreen(null); }
				else if (oldObject instanceof IWindowParent && closeOld) { ((IWindowParent) oldObject).close(); }
				windowIn.setObjectID(WindowObjectS.getPID());
				renderer.addObject(null, windowIn);
			}
			if (oldObject instanceof IWindowParent && !(windowIn instanceof EnhancedGui)) {
				IWindowParent old = (IWindowParent) oldObject;
				
				if (old.isMaximized() && windowIn.isMaximizable()) {
					windowIn.setPreMax(old.getPreMax());
					windowIn.setMaximized(old.getMaximizedPosition());
					windowIn.maximize();
				}
				
				if (transferHistory) {
					old.getWindowHistory().add(old);
					windowIn.setWindowHistory(old.getWindowHistory());
					windowIn.setPinned(old.isPinned());
				}
			}
			
			setPos(windowIn, oldObject instanceof IWindowObject ? (IWindowObject) oldObject : null, loc);
			windowIn.bringToFront();
			if (transferFocus) { windowIn.requestFocus(); }
		}
		return windowIn;
	}
	
	private static void setPos(IWindowParent windowIn, IWindowObject objectIn, CenterType typeIn) {
		ScaledResolution res = new ScaledResolution(mc);
		EDimension gDim = windowIn.getDimensions();
		int headerHeight = windowIn.hasHeader() ? windowIn.getHeader().height : 0;
		
		int sX = 0;
		int sY = 0;
		
		switch (typeIn) {
		case screen:
			sX = (res.getScaledWidth() / 2) - (gDim.width / 2);
			sY = (res.getScaledHeight() / 2) - (gDim.height / 2);
			break;
		case botLeftScreen:
			sX = 1;
			sY = res.getScaledHeight() - 2 - gDim.height;
			break;
		case topLeftScreen:
			sX = 1;
			sY = 2;
			break;
		case botRightScreen:
			sX = res.getScaledWidth() - 1 - gDim.width;
			sY = res.getScaledHeight() - 2 - gDim.height;
			break;
		case topRightScreen:
			sX = res.getScaledWidth() - 1 - gDim.width;
			sY = 2;
			break;
		case cursor:
			sX = EMouseHelper.mX - (gDim.width / 2);
			sY = EMouseHelper.mY - (gDim.height - headerHeight) / 2 + (gDim.height / 7);
			break;
		case cursorCorner:
			sX = EMouseHelper.mX;
			sY = EMouseHelper.mY;
			break;
		case object:
			if (objectIn != null) {
				EDimension objDim = objectIn.getDimensions();
				sX = objDim.midX - (gDim.width / 2);
				sY = objDim.midY - (gDim.height / 2);
			}
			break;
		case objectCorner:
			if (objectIn != null) {
				EDimension objDim = objectIn.getDimensions();
				sX = objDim.startX;
				sY = objDim.startY;
			}
			break;
		case objectIndent:
			if (objectIn != null) {
				EDimension objDim = objectIn.getDimensions();
				sX = objDim.startX + 25;
				sY = objDim.startY + 25;
			}
			break;
		case existingObjectIndent:
			EArrayList<WindowParent> windows = new EArrayList();
			renderer.getAllChildren().stream().filter(o -> windowIn.getClass().isInstance(o)).filter(o -> !o.isBeingRemoved()).forEach(w -> windows.add((WindowParent) w));
			
			if (windows.isNotEmpty()) {
				if (windows.get(0) != null) {
					EDimension objDim = windows.get(0).getDimensions();
					sX = objDim.startX + 25;
					sY = objDim.startY + 25;
				}
			}
			
			break;
		default: break;
		}
		
		TaskBar bar = EnhancedMC.getRenderer().getTaskBar();
		int tb = (bar != null) ? bar.height : 0;
		
		if (!(windowIn instanceof OverlayWindow)) {
			sX = sX < 0 ? 4 : sX;
			sY = (sY - headerHeight) < 2 ? tb + 4 + headerHeight : sY;
			sX = sX + gDim.width > res.getScaledWidth() ? -4 + sX - (sX + gDim.width - res.getScaledWidth()) : sX;
			sY = sY + gDim.height > res.getScaledHeight() ? -4 + sY - (sY + gDim.height - res.getScaledHeight()) : sY;
		}
		
		windowIn.setPosition(sX, sY);
	}
	
	public static void openSettingsGui() {
		mc.displayGuiScreen(new RendererProxyGui());
		if (!isEGuiOpen(SettingsWindowMain.class)) { displayWindow(new SettingsWindowMain()); }
		else {
			WindowParent s = getWindowInstance(SettingsWindowMain.class);
			if (s != null) { s.requestFocus(); }
		}
	}
	
	public static void postNotification(String messageIn) { postNotification(messageIn, null); }
	public static void postNotification(String messageIn, WindowParent windowIn) { notifications.post(messageIn, windowIn); }
	public static void postNotification(NotificationObject obj) { notifications.post(obj); }
	
	public static boolean isInitialized() { return isInitialized; }
	public static boolean isDebugMode() { return enableDebugFunctions; }
	public static boolean isDevMode() { return enableDevFunctions; }
	public static boolean isUserDev() { return isDev; }
	public static boolean isObfus() { return !deobf; }
	public static void setDebugMode(boolean val) { enableDebugFunctions = val; }
	
	public static void setDevMode(boolean val) {
		boolean old = enableDevFunctions;
		enableDevFunctions = val;
		if (old && !val) { RegisteredApps.getAppsList().stream().filter(a -> !a.isIncompatible()).forEach(a -> a.onDevModeDisabled()); }
	}
	
	//hypixel specific
	public static boolean isHypixel() { return !mc.isSingleplayer() && mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.equals("mc.hypixel.net"); }
	public static void requestHypixelServerInfo(ETerminal termIn) { if (isHypixel()) { appInstance.requestHypixelData(termIn); } }
	public static HypixelData getHypixelData() { return appInstance.getHypixelData(); }
	
	public static CoreApp getEMCApp() { return appInstance; }
	public static EventListener getEventListener() { return eventListener; }
	public static EnhancedMCRenderer getRenderer() { return renderer; }
	public static TerminalCommandHandler getTerminalHandler() { return terminal; }
	public static NotificationHandler getNotificationHandler() { return notifications; }
	public static RegisteredApps getApps() { return appsList; }
	
	public static void log(Level levelIn, String msg) { EMCLogger.log(levelIn, msg); }
	public static void info(String msg) { EMCLogger.log(Level.INFO, msg); }
	public static void error(String msg) { EMCLogger.log(Level.ERROR, msg); }
	public static void error(String msg, Throwable throwableIn) { EMCLogger.log(Level.ERROR, msg, throwableIn); }
	
	protected static void createdByHunterBragg() {}
	
}
