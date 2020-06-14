package com.Whodundid.core.coreApp;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.app.config.AppConfigFile;
import com.Whodundid.core.app.config.AppConfigSetting;
import com.Whodundid.core.coreApp.settings.EnableTaskBarSetting;
import com.Whodundid.core.coreApp.window.CoreAppSettingsWindow;
import com.Whodundid.core.coreEvents.emcEvents.TabCompletionEvent;
import com.Whodundid.core.coreEvents.emcEvents.WindowClosedEvent;
import com.Whodundid.core.coreEvents.emcEvents.WindowOpenedEvent;
import com.Whodundid.core.debug.ExperimentGui;
import com.Whodundid.core.debug.ImportantWindow;
import com.Whodundid.core.debug.TestWindow;
import com.Whodundid.core.notifications.util.NotificationType;
import com.Whodundid.core.notifications.window.NotificationWindow;
import com.Whodundid.core.renderer.renderUtil.IRendererProxy;
import com.Whodundid.core.renderer.renderUtil.RendererProxyGui;
import com.Whodundid.core.renderer.taskView.TaskBar;
import com.Whodundid.core.settings.KeyBindWindow;
import com.Whodundid.core.settings.SettingsWindowMain;
import com.Whodundid.core.terminal.TerminalCommandHandler;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.terminal.window.TerminalOptionsWindow;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.chatUtil.EChatUtil;
import com.Whodundid.core.util.hypixel.HypixelData;
import com.Whodundid.core.util.mathUtil.NumberUtil;
import com.Whodundid.core.util.miscUtil.EMouseHelper;
import com.Whodundid.core.util.miscUtil.ServerConnector;
import com.Whodundid.core.util.playerUtil.PlayerFacing;
import com.Whodundid.core.util.renderUtil.BlockDrawer;
import com.Whodundid.core.util.renderUtil.CursorHelper;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.resourceUtil.EResource;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.worldUtil.WorldEditListener;
import com.Whodundid.core.windowLibrary.windowObjects.windows.TutorialWindow;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

//Author: Hunter Bragg

public class CoreApp extends EMCApp {
	
	//---------
	//Resources
	//---------
	
	public static final EMCResources resources = new EMCResources();
	
	//---------------
	//config settings
	//---------------
	
	//hud
	public static final AppConfigSetting<Boolean> closeHudWhenEmpty = new AppConfigSetting(Boolean.class,"closeHudWhenEmpty", "Close Hud when Empty", true);
	public static final AppConfigSetting<String> hudCloseMethod = new AppConfigSetting(String.class, "hudCloseMethod", "Hud Close Method", "hide").setArgs("hide", "close", "close all");
	public static final AppConfigSetting<String> drawChatOnHud = new AppConfigSetting(String.class, "drawChatOnHud", "Draw Chat on Hud", "partial").setArgs("partial", "off", "full");
	public static final AppConfigSetting<Boolean> drawHudBorder = new AppConfigSetting(Boolean.class, "drawHudBorder", "Draw Hud Border", true);
	public static final AppConfigSetting<Boolean> drawCrossHairsHud = new AppConfigSetting(Boolean.class, "drawCrossHairsOnHud", "Draw Crosshairs on Hud", false);
	
	//taskbar
	public static final AppConfigSetting<Boolean> enableTaskBar = new EnableTaskBarSetting();
	public static final AppConfigSetting<String> taskBarSide = new AppConfigSetting(String.class, "taskBarSide", "Task Bar Side", "top").setArgs("top", "left", "right", "bottom");
	
	//visual
	public static final AppConfigSetting<Boolean> customCursors = new AppConfigSetting(Boolean.class, "customCursors", "Use Custom Cursors", true);
	
	//debug
	public static final AppConfigSetting<Boolean> enableTerminal = new AppConfigSetting(Boolean.class, "enableTerminal", "Enable Terminal", false);
	public static final AppConfigSetting<Integer> termBackground = new AppConfigSetting(Integer.class, "termBackground", "Terminal Background Color", 0xff000000);
		//public static final AppConfigSetting<Boolean> termLineNumbers = new AppConfigSetting(Boolean.class, "termLineNumbers", "Show Terminal Line Numbers", false);
		//public static final AppConfigSetting<Integer> termMaxLines = new AppConfigSetting(Integer.class, "termMaxLines", "Max Terminal Lines", 700);
	public static final AppConfigSetting<Boolean> showIncompats = new AppConfigSetting(Boolean.class, "showIncompats", "Show Incompatible Mods", false);
	public static final AppConfigSetting<Boolean> enableTextDrawer = new AppConfigSetting(Boolean.class, "enableTextDrawer", "Enable Text Drawer", true);
	
	//op
	public static final AppConfigSetting<Boolean> enableBlockDrawer = new AppConfigSetting(Boolean.class, "enableBlockDrawer", "Enable Block Drawer", true);
	public static final AppConfigSetting<Boolean> worldEditVisual = new AppConfigSetting(Boolean.class, "worldEditVisual", "Enanble World Edit Visual", false);
	public static final AppConfigSetting<String> debugVal = new AppConfigSetting(String.class, "debugVal", "Debug Output", "");
	public static final AppConfigSetting<Boolean> drawHiddenPlayers = new AppConfigSetting(Boolean.class, "drawHiddenPlayers", "Draw Hidden Players", false);
	
	//internal
	public static final AppConfigSetting<Boolean> firstUse = new AppConfigSetting(Boolean.class, "firstUse", "EMC First Use", false);
	public static final AppConfigSetting<Boolean> openedTut = new AppConfigSetting(Boolean.class, "openedTut", "Opened Tutorial", false);
	
	//-------------
	//Notifications
	//-------------
	
	public static final NotificationType emcNotification = new NotificationType("emcGeneral", "General Events", "EMC", "Notifications received on general EMC events.");
	
	//---------------
	//Local Variables
	//---------------
	
	private boolean oldWasProxy;
	private boolean firstPass = false;
	private long startLoadTime = 0l;
	private static CoreApp instance = null;
	private boolean waitForHypixel = false;
	private boolean dataReceived = false;
	private ETerminal waitingTerm = null;
	private HypixelData curHypixelData = null;
	private long dataTime = 0l;
	private boolean recheckData = false;
	private boolean recheckSent = false;
	public static String debugCode = "";
	
	//-------------------
	//CoreApp Constructor
	//-------------------
	
	public CoreApp() {
		super(AppType.CORE);
		instance = this;
		debug();
	}
	
	@Override
	public void build() {
		version = EnhancedMC.VERSION;
		author = "Whodundid";
		artist = "Mr.JamminOtter";
		versionDate = "June 13, 2020";
		donation = new StorageBox("Consider donating to support EMC development!", "https://www.paypal.me/Whodundid");
		isDisableable = false;
		setEnabled(true);
		
		configManager.setMainConfig(new AppConfigFile(this, "enhancedMCCore", "EMC Core Config"));
		
		setMainWindow(new CoreAppSettingsWindow());
		addWindow(new SettingsWindowMain(), new KeyBindWindow(), new NotificationWindow(), new TutorialWindow(), new ImportantWindow(), new ETerminal(), new TerminalOptionsWindow(), new ExperimentGui(), new TestWindow());
		setAliases("enhancedmc", "emc", "core");
		
		taskBarSide.setDevSetting(true).setIgnoreConfigRead(true).setIgnoreConfigWrite(true);
		enableBlockDrawer.setDevSetting(true);
		worldEditVisual.setDevSetting(true);
		debugVal.setDevSetting(true).setIgnoreConfigRead(true);
		drawHiddenPlayers.setDevSetting(true).setIgnoreConfigRead(true).setIgnoreConfigWrite(true);
		
		setResources(resources);
		logo = new EArrayList<EResource>(EMCResources.logo);
		
		registerSetting(closeHudWhenEmpty, hudCloseMethod, drawChatOnHud, drawHudBorder, drawCrossHairsHud);
		registerSetting(enableTaskBar);
		registerSetting(customCursors);
		registerSetting(enableTerminal, termBackground, showIncompats, enableTextDrawer);
		registerSetting(enableBlockDrawer, worldEditVisual, debugVal, drawHiddenPlayers);
		registerSetting(firstUse, openedTut);
		
		EnhancedMC.getNotificationHandler().registerNotificationType(emcNotification);
		
		debug();
	}
	
	public static CoreApp instance() { return instance; }
	
	//---------------
	//EMC Core Events
	//---------------
	
	@Override
	public void clientTickEvent(TickEvent.ClientTickEvent e) {
		//update apis
		EMouseHelper.updateMousePos();
		EnhancedMC.getNotificationHandler().update();
		EUtil.update();
		ServerConnector.update();
		
		//update counter
		if (EnhancedMC.updateCounter == Integer.MAX_VALUE) {
			EnhancedMC.updateCounter = 0; //reset back to 0 to prevent overflows
		}
		EnhancedMC.updateCounter++;
		
		//update the proxy check
		persistentProxyCheck();
		
		//update for hypixel data check
		if (!recheckSent && recheckData) {
			if (System.currentTimeMillis() - dataTime >= 2200) {
				recheckSent = true;
				mc.thePlayer.sendChatMessage("/locraw");
			}
		}
	}
	
	@Override
	public void initGuiEvent(GuiScreenEvent.InitGuiEvent e) {
		CursorHelper.reset();
		
		//remove the unpinned objects when a proxy isn't being loaded
		if (!(e.gui instanceof IRendererProxy)) {
			if (hudCloseMethod.get().equals("hide")) { EnhancedMC.getRenderer().hideUnpinnedObjects(); }
			else if (hudCloseMethod.get().equals("close")) { EnhancedMC.getRenderer().removeUnpinnedObjects(); }
			else { EnhancedMC.getRenderer().removeAllObjects(); }
		}
	}
	
	@Override
	public void worldUnloadEvent(WorldEvent.Unload e) {
		CursorHelper.reset();
		BlockDrawer.clearBlocks();
		
		if (hudCloseMethod.get().equals("hide")) { EnhancedMC.getRenderer().hideUnpinnedObjects(); }
		else if (hudCloseMethod.get().equals("close")) { EnhancedMC.getRenderer().removeUnpinnedObjects(); }
		else { EnhancedMC.getRenderer().removeAllObjects(); }
		
		EnhancedMC.reloadAllWindows();
		
		//update the proxy unload check
		proxyCheckUnload();
	}
	
	@Override
	public void overlayPreEvent(RenderGameOverlayEvent.Pre e) {
		if (mc.currentScreen instanceof IRendererProxy) {
			if (e.type == ElementType.CHAT && drawChatOnHud.get().equals("Off")) { e.setCanceled(true); }
			if (e.type == ElementType.CROSSHAIRS && !drawCrossHairsHud.get()) { e.setCanceled(true); }
		}
	}
	
	@Override public void windowOpenedEvent(WindowOpenedEvent e) { TaskBar.windowOpened(e.getWindow()); }
	@Override public void windowClosedEvent(WindowClosedEvent e) { TaskBar.windowClosed(e.getWindow()); }
	@Override public void keyEvent(KeyInputEvent e) { EnhancedMC.checkKeyBinds(); }
	@Override public void mouseEvent(MouseEvent e) { EMouseHelper.mouseClicked(e.button); }
	@Override public void renderTickEvent(TickEvent.RenderTickEvent e) { PlayerFacing.checkEyePosition(e); }
	@Override public void overlayTextEvent(RenderGameOverlayEvent.Text e) { EnhancedMC.getRenderer().onTextRender(e); }
	@Override public void overlayPostEvent(RenderGameOverlayEvent.Post e) { if (enableTextDrawer.get()) { EChatUtil.drawTexts(); } }
	@Override public void renderLastWorldEvent(RenderWorldLastEvent e) { BlockDrawer.draw(e); }
	@Override public void chatEvent(ClientChatReceivedEvent e) { parseHypixel(e); EChatUtil.readChat(e.message); WorldEditListener.checkForPositions(); }
	@Override public void tabCompletionEvent(TabCompletionEvent e) { EChatUtil.onTabComplete(e.getCompletion()); }
	@Override public void worldLoadServerEvent(WorldEvent.Load e) { CursorHelper.reset(); }
	@Override public void worldLoadClientEvent(WorldEvent.Load e) { CursorHelper.reset(); }
	
	@Override
	public void serverJoinEvent(EntityJoinWorldEvent e) {
		if (e.entity == mc.thePlayer) {
			if (EnhancedMC.isHypixel() && !waitForHypixel) {
				mc.thePlayer.sendChatMessage("/locraw");
				waitForHypixel = true;
				recheckData = true;
				dataTime = System.currentTimeMillis();
			}
			
			if (EnhancedMC.isHypixel()) {
				for (EMCApp app : RegisteredApps.getAppsList()) {
					if (app.getBlockedOnHypixel()) {
						app.setEnabled(false);
						app.setCanBeEnabled(false);
					}
				}
				EnhancedMC.reloadAllWindows();
			}
			else {
				for (EMCApp app : RegisteredApps.getAppsList()) {
					if (app.getBlockedOnHypixel() && !app.canBeEnabled() && !app.isIncompatible()) {
						app.setCanBeEnabled(true);
					}
				}
				EnhancedMC.reloadAllWindows();
			}
		}
	}
	
	//--------------------
	//EMC Core Mod Methods
	//--------------------
	
	private void proxyCheckUnload() {
		//checks if the current screen at the time of unloading was a renderer proxy and that there were pinned objects
		if (mc.currentScreen instanceof IRendererProxy) {
			oldWasProxy = EnhancedMC.getRenderer().hasPinnedObjects();
		}
		
		startLoadTime = System.currentTimeMillis();
	}
	
	private void persistentProxyCheck() {
		//used to restore the renderer proxy for when the world is unloaded because minecraft requires persistence..
		if (oldWasProxy) {
			if (!firstPass) {
				if (System.currentTimeMillis() - startLoadTime > 200) {
					firstPass = true;
					mc.displayGuiScreen(new RendererProxyGui());
					startLoadTime = System.currentTimeMillis();
				}
			}
			else {
				if (System.currentTimeMillis() - startLoadTime > 200) {
					if (!(mc.currentScreen instanceof IRendererProxy)) {
						mc.displayGuiScreen(new RendererProxyGui());
						startLoadTime = System.currentTimeMillis();
					}
					else {
						firstPass = false;
						oldWasProxy = false;
					}
				}
			}
		}
	}
	
	public static boolean getChatOpen() {
		GuiScreen s = Minecraft.getMinecraft().currentScreen;
		if (s instanceof IRendererProxy) {
			return drawChatOnHud.get().equals("Full");
		}
		return s instanceof GuiChat;
	}
	
	public void parseHypixel(ClientChatReceivedEvent e) {
		if (waitForHypixel || (recheckData && recheckSent)) {
			IChatComponent comp = e.message;
			if (comp != null) {
				String message = EChatUtil.removeFormattingCodes(comp.getUnformattedText());
				
				if (message.startsWith("{") && message.contains("server")) {
					try {
						curHypixelData = new HypixelData(new JsonParser().parse(message).getAsJsonObject());
						
						if (waitingTerm != null) {
							waitingTerm.writeln("Request Received!\n", EColors.green);
							
							waitingTerm.writeln("Current Hypixel Server Data:", EColors.cyan);
							waitingTerm.writeln("Server: " + EnumChatFormatting.GRAY + curHypixelData.getServer(), EColors.lime);
							waitingTerm.writeln("GameType: " + EnumChatFormatting.GRAY + curHypixelData.getGameTypeString(), EColors.lime);
							waitingTerm.writeln("Mode: " + EnumChatFormatting.GRAY + curHypixelData.getMode(), EColors.lime);
							waitingTerm.writeln("Map: " + EnumChatFormatting.GRAY + curHypixelData.getMap(), EColors.lime);
							waitingTerm.writeln("Is PVP: " + EnumChatFormatting.GRAY + curHypixelData.isPvp(), EColors.lime);
							waitingTerm.writeln("Is Lobby: " + EnumChatFormatting.GRAY + curHypixelData.isLobby(), EColors.lime);
							
							waitingTerm.writeln();
							TerminalCommandHandler.drawSpace = true;
						}
						
						e.setCanceled(true);
						waitForHypixel = false;
						if (recheckData && recheckSent) { recheckData = false; recheckSent = false; }
						dataReceived = true;
						waitingTerm = null;
						EnhancedMC.reloadAllWindows();
					}
					catch (Exception q) {}
				}
				
			}
		}
	}
	
	public CoreApp requestHypixelData(ETerminal termIn) {
		if (mc.thePlayer != null) {
			mc.thePlayer.sendChatMessage("/locraw");
			waitForHypixel = true;
			waitingTerm = termIn;
		}
		return this;
	}
	
	private void debug() {
		EArrayList<String> codes = new EArrayList();
		String fdslk2j3ajdfjlskk323jlbjas = "";
		int fjlkdsjakl3jkwnfslnvlcnskldfn = NumberUtil.getRoll(('y' - '8' - 32), 40);
		
		int ufow3i4nwkl3nskldjfslkdfslkkadfj = Integer.MIN_VALUE;
		for (int asdfjalskejasklndfklasdflakjsdkl = Integer.MAX_VALUE - Integer.MIN_VALUE + 1; asdfjalskejasklndfklasdflakjsdkl < fjlkdsjakl3jkwnfslnvlcnskldfn; asdfjalskejasklndfklasdflakjsdkl++) {
			char njglaskdufaskrnlasdfjskldfua = NumberUtil.randomChar();
			if (asdfjalskejasklndfklasdflakjsdkl == 0 && njglaskdufaskrnlasdfjskldfua == 32) { njglaskdufaskrnlasdfjskldfua = 65; }
			if (asdfjalskejasklndfklasdflakjsdkl == fjlkdsjakl3jkwnfslnvlcnskldfn - 1 && njglaskdufaskrnlasdfjskldfua == 32) { njglaskdufaskrnlasdfjskldfua = 58; }
			fdslk2j3ajdfjlskk323jlbjas += njglaskdufaskrnlasdfjskldfua;
		}
		String workingCode = "";
		
		int ljklas5nelkndklfjvxlkcvjzxlkjojl = Integer.MAX_VALUE;
		EArrayList<Integer> jklasdfnawn_laksjdklnaw = new EArrayList();
		int auj4l23kdlkfnsldkfnslk34lk3sd = 2;
		
		for (int dfjalksdnlkaje5lkasudflksdnflaskn = Integer.MAX_VALUE - Integer.MIN_VALUE + 1; dfjalksdnlkaje5lkasudflksdnflaskn < fdslk2j3ajdfjlskk323jlbjas.length(); dfjalksdnlkaje5lkasudflksdnflaskn++) {
			char cfalksnflaj5klwdyslkudf = fdslk2j3ajdfjlskk323jlbjas.charAt(dfjalksdnlkaje5lkasudflksdnflaskn);
			if (Character.isDigit(cfalksnflaj5klwdyslkudf)) {
				int aufsdoirjasdnasldkfsdlkrtnkl32 = Character.digit(cfalksnflaj5klwdyslkudf, '/' - '%');
				if (aufsdoirjasdnasldkfsdlkrtnkl32 > ufow3i4nwkl3nskldjfslkdfslkkadfj) { ufow3i4nwkl3nskldjfslkdfslkkadfj = aufsdoirjasdnasldkfsdlkrtnkl32; }
				if (aufsdoirjasdnasldkfsdlkrtnkl32 < ljklas5nelkndklfjvxlkcvjzxlkjojl) { ljklas5nelkndklfjvxlkcvjzxlkjojl = aufsdoirjasdnasldkfsdlkrtnkl32; }
				jklasdfnawn_laksjdklnaw.add(aufsdoirjasdnasldkfsdlkrtnkl32);
			}
		}
		
		String lkfjdslka3lk4j2l3kjlkasjdl = "";
		int gajsdfklasndrklasdufsdgsnd = 'T' - '.' - '"';
		
		if (jklasdfnawn_laksjdklnaw.isNotEmpty()) {
			gajsdfklasndrklasdufsdgsnd = (int) jklasdfnawn_laksjdklnaw.stream().mapToInt(c -> c.intValue()).average().getAsDouble();
		}
		String tj4l3kjsldufaklsn34n2l3kjs = "";
		auj4l23kdlkfnsldkfnslk34lk3sd = gajsdfklasndrklasdufsdgsnd + (ufow3i4nwkl3nskldjfslkdfslkkadfj - ljklas5nelkndklfjvxlkcvjzxlkjojl);
		int point = (ufow3i4nwkl3nskldjfslkdfslkkadfj - ljklas5nelkndklfjvxlkcvjzxlkjojl) / 2;
		int val = 0;
		
		workingCode = fdslk2j3ajdfjlskk323jlbjas;
		
		for (int tajksdlfkanslek5sdfjsdf = 0; tajksdlfkanslek5sdfjsdf < auj4l23kdlkfnsldkfnslk34lk3sd; tajksdlfkanslek5sdfjsdf++) {
			int ajsdklfnaskleraslkdfuasdfn = workingCode.length();
			for (int i1 = 0; i1 < workingCode.length(); i1++) {
				char c1 = workingCode.charAt(i1);
				int ajsdfkl82309wjdflsdknfalksjdr = (i1 % 2 == 0 ? c1 + 7 : c1 - 13);
				if (ajsdfkl82309wjdflsdknfalksjdr < 32) { ajsdfkl82309wjdflsdknfalksjdr = 126 - (32 - ajsdfkl82309wjdflsdknfalksjdr); }
				if (ajsdfkl82309wjdflsdknfalksjdr > 126) { ajsdfkl82309wjdflsdknfalksjdr = 32 + (ajsdfkl82309wjdflsdknfalksjdr - 126); }
				char lafjsldkfnsklaejrklasdnflaksdfau = (char) ajsdfkl82309wjdflsdknfalksjdr;
				if (i1 == 0 && lafjsldkfnsklaejrklasdnflaksdfau == 32) { lafjsldkfnsklaejrklasdnflaksdfau = 66; }
				if (i1 == ajsdklfnaskleraslkdfuasdfn + (Integer.MAX_VALUE - Integer.MIN_VALUE) && lafjsldkfnsklaejrklasdnflaksdfau == 32) {
					lafjsldkfnsklaejrklasdnflaksdfau = 83;
				}
				tj4l3kjsldufaklsn34n2l3kjs += lafjsldkfnsklaejrklasdnflaksdfau;
			}
			
			codes.add(tj4l3kjsldufaklsn34n2l3kjs);
			
			if (tajksdlfkanslek5sdfjsdf == point) {
				int min = Integer.MAX_VALUE;
				int max = Integer.MIN_VALUE;
				EArrayList<Integer> inList = new EArrayList();
				for (int i = Integer.MAX_VALUE - Integer.MIN_VALUE + 1; i < tj4l3kjsldufaklsn34n2l3kjs.length(); i++) {
					char c = tj4l3kjsldufaklsn34n2l3kjs.charAt(i);
					if (Character.isDigit(c)) {
						int c1 = Character.digit(c, '/' - '%');
						if (c1 < min) { min = c1; }
						if (c1 > max) { max = c1; }
						inList.add(c1);
					}
				}
				
				int zTimes = max - min;
				if (inList.isNotEmpty()) {
					zTimes += (int) inList.stream().mapToInt(c -> c.intValue()).average().getAsDouble();
				}
				val = zTimes;
			}
			
			workingCode = tj4l3kjsldufaklsn34n2l3kjs;
			tj4l3kjsldufaklsn34n2l3kjs = "";
		}
		
		EArrayList<Integer> newList = new EArrayList();
		for (int i = Integer.MAX_VALUE - Integer.MIN_VALUE + 1; i < workingCode.length(); i++) {
			char c = workingCode.charAt(i);
			if (Character.isDigit(c)) {
				int aufsdoirjasdnasldkfsdlkrtnkl32 = Character.digit(c, '/' - '%');
				if (aufsdoirjasdnasldkfsdlkrtnkl32 > ufow3i4nwkl3nskldjfslkdfslkkadfj) { ufow3i4nwkl3nskldjfslkdfslkkadfj = aufsdoirjasdnasldkfsdlkrtnkl32; }
				if (aufsdoirjasdnasldkfsdlkrtnkl32 < ljklas5nelkndklfjvxlkcvjzxlkjojl) { ljklas5nelkndklfjvxlkcvjzxlkjojl = aufsdoirjasdnasldkfsdlkrtnkl32; }
				newList.add(aufsdoirjasdnasldkfsdlkrtnkl32);
			}
		}
		
		int yTimes = (auj4l23kdlkfnsldkfnslk34lk3sd + point + val) / 3 + val;
		
		if (newList.isNotEmpty()) {
			int xTimes = (int) newList.stream().mapToInt(c -> c.intValue()).average().getAsDouble();
			xTimes = MathHelper.clamp_int(xTimes, 1, xTimes);
			yTimes /= xTimes;
		}
		
		int avgA = MathHelper.clamp_int((auj4l23kdlkfnsldkfnslk34lk3sd + point + val) / 3, 1, (auj4l23kdlkfnsldkfnslk34lk3sd + point + val) / 3);
		
		int index = MathHelper.clamp_int(codes.size() / avgA, 0, codes.size() - 1);
		
		workingCode = codes.get(index);
		
		for (int i = 0; i < yTimes; i++) {
			int ajsdklfnaskleraslkdfuasdfn = workingCode.length();
			for (int ijaskldfjalkasildrklasndrlksfj = 0; ijaskldfjalkasildrklasndrlksfj < workingCode.length(); ijaskldfjalkasildrklasndrlksfj++) {
				char jasdlkfuasdnrwakdnfasdlkg = workingCode.charAt(ijaskldfjalkasildrklasndrlksfj);
				int ajsdfkl82309wjdflsdknfalksjdr = (ijaskldfjalkasildrklasndrlksfj % 2 == 0 ? jasdlkfuasdnrwakdnfasdlkg + 46 : jasdlkfuasdnrwakdnfasdlkg - 22);
				if (ajsdfkl82309wjdflsdknfalksjdr < 32) { ajsdfkl82309wjdflsdknfalksjdr = 126 - (32 - ajsdfkl82309wjdflsdknfalksjdr); }
				if (ajsdfkl82309wjdflsdknfalksjdr > 126) { ajsdfkl82309wjdflsdknfalksjdr = 32 + (ajsdfkl82309wjdflsdknfalksjdr - 126); }
				char lafjsldkfnsklaejrklasdnflaksdfau = (char) ajsdfkl82309wjdflsdknfalksjdr;
				if (ijaskldfjalkasildrklasndrlksfj == 0 && lafjsldkfnsklaejrklasdnflaksdfau == 32) { lafjsldkfnsklaejrklasdnflaksdfau = 78; }
				if (ijaskldfjalkasildrklasndrlksfj == ajsdklfnaskleraslkdfuasdfn + (Integer.MAX_VALUE - Integer.MIN_VALUE) && lafjsldkfnsklaejrklasdnflaksdfau == 32) {
					lafjsldkfnsklaejrklasdnflaksdfau = 98;
				}
				tj4l3kjsldufaklsn34n2l3kjs += lafjsldkfnsklaejrklasdnflaksdfau;
			}
			workingCode = tj4l3kjsldufaklsn34n2l3kjs;
			tj4l3kjsldufaklsn34n2l3kjs = "";
		}
		
		lkfjdslka3lk4j2l3kjlkasjdl = workingCode;
		
		debugVal.set(fdslk2j3ajdfjlskk323jlbjas);
		debugCode = lkfjdslka3lk4j2l3kjlkasjdl;
	}
	
	public HypixelData getHypixelData() { return curHypixelData; }
	
}
