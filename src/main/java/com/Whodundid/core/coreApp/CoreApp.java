package com.Whodundid.core.coreApp;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppConfigSetting;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.config.AppConfigFile;
import com.Whodundid.core.coreEvents.emcEvents.TabCompletionEvent;
import com.Whodundid.core.coreEvents.emcEvents.WindowClosedEvent;
import com.Whodundid.core.coreEvents.emcEvents.WindowOpenedEvent;
import com.Whodundid.core.debug.ExperimentGui;
import com.Whodundid.core.debug.ImportantGui;
import com.Whodundid.core.debug.TestWindow;
import com.Whodundid.core.notifications.util.NotificationType;
import com.Whodundid.core.notifications.window.NotificationWindow;
import com.Whodundid.core.renderer.BlockDrawer;
import com.Whodundid.core.renderer.renderUtil.IRendererProxy;
import com.Whodundid.core.renderer.renderUtil.RendererProxyGui;
import com.Whodundid.core.renderer.taskView.TaskBar;
import com.Whodundid.core.settings.KeyBindGui;
import com.Whodundid.core.settings.SettingsGuiMain;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.gui.TerminalOptions;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.chatUtil.EChatUtil;
import com.Whodundid.core.util.miscUtil.EMouseHelper;
import com.Whodundid.core.util.playerUtil.PlayerFacing;
import com.Whodundid.core.util.renderUtil.CursorHelper;
import com.Whodundid.core.util.resourceUtil.EResource;
import com.Whodundid.core.util.serverUtil.ServerConnector;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.worldUtil.WorldEditListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
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
	
	public static final AppConfigSetting<Boolean> closeHudWhenEmpty = new AppConfigSetting(Boolean.class,"closeHudWhenEmpty", "Close Hud when Empty", true);
	public static final AppConfigSetting<Boolean> drawHudBorder = new AppConfigSetting(Boolean.class, "drawHudBorder", "Draw Hud Border", true);
	public static final AppConfigSetting<Boolean> showIncompats = new AppConfigSetting(Boolean.class, "showIncompats", "Show Incompatible Mods", false);
	public static final AppConfigSetting<String> drawChatOnHud = new AppConfigSetting(String.class, "drawChatOnHud", "Draw Chat on Hud", "partial").setArgs("partial", "off", "full");
	public static final AppConfigSetting<Boolean> customCursors = new AppConfigSetting(Boolean.class, "customCursors", "Use Custom Cursors", true);
	
	public static final AppConfigSetting<Boolean> enableTerminal = new AppConfigSetting(Boolean.class, "enableTerminal", "Enable Terminal", false);
	//public static final AppConfigSetting<Boolean> termLineNumbers = new AppConfigSetting(Boolean.class, "termLineNumbers", "Show Terminal Line Numbers", false);
	public static final AppConfigSetting<Integer> termBackground = new AppConfigSetting(Integer.class, "termBackground", "Terminal Background Color", 0xff000000);
	//public static final AppConfigSetting<Integer> termMaxLines = new AppConfigSetting(Integer.class, "termMaxLines", "Max Terminal Lines", 700);
	
	public static final AppConfigSetting<Boolean> enableTaskBar = new AppConfigSetting(Boolean.class, "enableTaskBar", "Enable Task Bar", false);
	public static final AppConfigSetting<String> taskBarSide = new AppConfigSetting(String.class, "taskBarSide", "Task Bar Side", "top").setArgs("top", "left", "right", "bottom");
	
	public static final AppConfigSetting<Boolean> enableBlockDrawer = new AppConfigSetting(Boolean.class, "enableBlockDrawer", "Enable Block Drawer", true);
	public static final AppConfigSetting<Boolean> worldEditVisual = new AppConfigSetting(Boolean.class, "worldEditVisual", "Enanble World Edit Visual", false);
	
	public static final AppConfigSetting<Boolean> firstUse = new AppConfigSetting(Boolean.class, "firstUse", "EMC First Use", false);
	public static final AppConfigSetting<Boolean> openedTut = new AppConfigSetting(Boolean.class, "openedTut", "Opened Tutorial", false);
	
	public static final NotificationType emcNotification = new NotificationType("emcGeneral", "General Events", "EMC", "Notifications received on general EMC events.");
	
	private boolean oldWasProxy;
	private boolean firstPass = false;
	private long startLoadTime = 0l;
	private static CoreApp instance = null;
	
	public CoreApp() {
		super(AppType.CORE);
		instance = this;
		version = EnhancedMC.VERSION;
		author = "Whodundid";
		artist = "Mr.JamminOtter";
		versionDate = "May 17, 2020";
		isDisableable = false;
		setEnabled(true);
		
		configManager.setMainConfig(new AppConfigFile(this, "enhancedMCCore", "EMC Core Config"));
		
		setMainGui(new CoreAppSettingsGui());
		addGui(new SettingsGuiMain(), new KeyBindGui(), new NotificationWindow(), new ImportantGui(), new ETerminal(), new TerminalOptions(), new ExperimentGui(), new TestWindow());
		setAliases("enhancedmc", "emc", "core");
		
		taskBarSide.setOpSetting(true);
		enableBlockDrawer.setOpSetting(true);
		worldEditVisual.setOpSetting(true);
		
		setResources(new EMCResources());
		logo = new EArrayList<EResource>(EMCResources.logo);
		
		registerSetting(closeHudWhenEmpty, drawHudBorder, showIncompats, drawChatOnHud, customCursors);
		registerSetting(enableTerminal, termBackground);
		registerSetting(enableTaskBar, taskBarSide);
		registerSetting(enableBlockDrawer, worldEditVisual);
		registerSetting(firstUse, openedTut);
		
		EnhancedMC.getNotificationHandler().registerNotificationType(emcNotification);
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
	}
	
	@Override
	public void initGuiEvent(GuiScreenEvent.InitGuiEvent e) {
		CursorHelper.reset();
		
		//remove the unpinned objects when a proxy isn't being loaded
		if (!(e.gui instanceof IRendererProxy)) {
			EnhancedMC.getRenderer().removeUnpinnedObjects();
		}
	}
	
	@Override
	public void worldUnloadEvent(WorldEvent.Unload e) {
		CursorHelper.reset();
		BlockDrawer.clearBlocks();
		EnhancedMC.getRenderer().removeUnpinnedObjects();
		
		//update the proxy unload check
		proxyCheckUnload();
	}
	
	@Override
	public void overlayPreEvent(RenderGameOverlayEvent.Pre e) {
		if (e.type == ElementType.CHAT && mc.currentScreen instanceof IRendererProxy && EnhancedMC.getEMCApp().drawChatOnHud.get().equals("Off")) { e.setCanceled(true); }
	}
	
	@Override public void windowOpenedEvent(WindowOpenedEvent e) { TaskBar.windowOpened(e.getWindow()); }
	@Override public void windowClosedEvent(WindowClosedEvent e) { TaskBar.windowClosed(e.getWindow()); }
	
	@Override public void keyEvent(KeyInputEvent e) { EnhancedMC.checkKeyBinds(); }
	@Override public void mouseEvent(MouseEvent e) { EMouseHelper.mouseClicked(e.button); }
	@Override public void renderTickEvent(TickEvent.RenderTickEvent e) { PlayerFacing.checkEyePosition(e); }
	@Override public void overlayTextEvent(RenderGameOverlayEvent.Text e) { EnhancedMC.getRenderer().onTextRender(e); }
	@Override public void overlayPostEvent(RenderGameOverlayEvent.Post e) { }
	@Override public void renderLastWorldEvent(RenderWorldLastEvent e) { BlockDrawer.draw(e); }
	@Override public void chatEvent(ClientChatReceivedEvent e) { EChatUtil.readChat(e.message); WorldEditListener.checkForPositions(); }
	@Override public void tabCompletionEvent(TabCompletionEvent e) { EChatUtil.onTabComplete(e.getCompletion()); }
	@Override public void worldLoadClientEvent(WorldEvent.Load e) { CursorHelper.reset(); }
	@Override public void worldLoadServerEvent(WorldEvent.Load e) { CursorHelper.reset(); }
	@Override public void serverJoinEvent(EntityJoinWorldEvent e) {}
	
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
}
