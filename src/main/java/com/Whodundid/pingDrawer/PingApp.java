package com.Whodundid.pingDrawer;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.IUseScreenLocation;
import com.Whodundid.core.app.config.AppConfigFile;
import com.Whodundid.core.app.config.AppConfigSetting;
import com.Whodundid.core.coreApp.CoreApp;
import com.Whodundid.core.coreEvents.emcEvents.GameWindowResizedEvent;
import com.Whodundid.core.renderer.renderUtil.IRendererProxy;
import com.Whodundid.core.renderer.taskView.TaskBar;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.miscUtil.NetPlayerComparator;
import com.Whodundid.core.util.renderUtil.GLObject;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.resourceUtil.EResource;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowParent;
import com.Whodundid.pingDrawer.settings.EnableTabSetting;
import com.Whodundid.pingDrawer.util.EnhancedTabList;
import com.Whodundid.pingDrawer.util.PingResources;
import com.Whodundid.pingDrawer.window.PingAppSettingsWindow;
import com.Whodundid.pingDrawer.window.PingSetLocationWindow;
import com.google.common.collect.Ordering;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

//Last edited: Dec 10, 2018
//First Added: Oct 19, 2018
//Author: Hunter Bragg

@Mod(modid = PingApp.MODID, version = PingApp.VERSION, name = PingApp.NAME, dependencies = "required-after:enhancedmc")
public final class PingApp extends EMCApp implements IUseScreenLocation {

	public static final String MODID = "pingdisplay";
	public static final String VERSION = "2.0";
	public static final String NAME = "Ping Display";
	
	//---------
	//Resources
	//---------
	
	public static final PingResources resources = new PingResources();
	
	//---------------
	//config settings
	//---------------
	
	public static final EnableTabSetting enableTab = new EnableTabSetting();
	public static final AppConfigSetting<ScreenLocation> loc = new AppConfigSetting(ScreenLocation.class, "loc", "Screen Draw Location", ScreenLocation.botLeft);
	public static final AppConfigSetting<Boolean> drawOwn = new AppConfigSetting(Boolean.class, "drawOwn", "Draw Personal Ping", true);
	public static final AppConfigSetting<Boolean> drawWithChat = new AppConfigSetting(Boolean.class, "drawWithChat", "Draw With Chat Open", true);
	public static final AppConfigSetting<Boolean> drawWithHud = new AppConfigSetting(Boolean.class, "drawWithHud", "Draw With Hud Open", true);
	public static final AppConfigSetting<Boolean> drawOwnThresholds = new AppConfigSetting(Boolean.class, "drawOwnThresholds", "Draw Personal Ping with Thresholds", true);
	public static final AppConfigSetting<Integer> ownColor = new AppConfigSetting(Integer.class, "ownColor", "Personal Ping Color", 0xff00ff00);
	public static final AppConfigSetting<Integer> lowThresh = new AppConfigSetting(Integer.class, "lowThresh", "Low Ping Threshhold", 100);
	public static final AppConfigSetting<Integer> medThresh = new AppConfigSetting(Integer.class, "lowThresh", "Medium Ping Threshhold", 175);
	public static final AppConfigSetting<Integer> highThresh = new AppConfigSetting(Integer.class, "lowThresh", "High Ping Threshhold", 250);
	public static final AppConfigSetting<Integer> lowColor = new AppConfigSetting(Integer.class, "lowColor", "Low Color", 0xff55ff55);
	public static final AppConfigSetting<Integer> medColor = new AppConfigSetting(Integer.class, "medColor", "Medium Color", 0xffffff00);
	public static final AppConfigSetting<Integer> highColor = new AppConfigSetting(Integer.class, "highColor", "High Color", 0xffff9900);
	public static final AppConfigSetting<Integer> vhighColor = new AppConfigSetting(Integer.class, "vhighColor", "Very High Color", 0xffff5555);
	
	protected Entity lastJoinedEntity;
	protected int ping = 0;
	protected int xPos = 0, yPos = 0;
	protected boolean hasPing = false;
	private EnhancedTabList tabList = null;
	private GuiPlayerTabOverlay vanillaTabList = null;
	NetHandlerPlayClient nethandlerplayclient;
	NetPlayerComparator comparator = new NetPlayerComparator();
	Ordering<NetworkPlayerInfo> order;
	List<EntityPlayer> list;
	List<NetworkPlayerInfo> nameList;
	private static PingApp instance;
	
	protected boolean toggleTab = false;
	protected boolean tabPressed = false;
	long tapTimer = -1l;
	
	public PingApp() {
		super(AppType.PING);
		instance = this;
	}
	
	@Override
	public void build() {
		version = VERSION;
		versionDate = "June 15, 2020";
		author = "Whodundid";
		artist = "Mr.JamminOtter";
		donation = new StorageBox("Consider donating to support EMC development!", "https://www.paypal.me/Whodundid");
		addDependency(AppType.CORE, "1.0");
		
		configManager.setMainConfig(new AppConfigFile(this, "pingConfig", "EMC Ping Display Config"));
		setResources(resources);
		
		logo = new EArrayList<EResource>(PingResources.logo);
		
		registerSetting(enableTab, loc, drawWithChat, drawWithHud, ownColor, drawOwnThresholds, lowThresh, medThresh, highThresh, lowColor, medColor, highColor, vhighColor);
		
		setMainWindow(new PingAppSettingsWindow());
		addWindow(new PingSetLocationWindow());
		setAliases("ping", "latency");
	}
	
	public static PingApp getInstance() { return instance; }
	
	@Override
	public void terminalRegisterCommandEvent(ETerminal conIn, boolean runVisually) {
		
	}
	
	@Override
	public PingApp setEnabled(boolean val) {
		super.setEnabled(val);
		updateTabState(val);
		toggleTab = false;
		return this;
	}
	
	@Override
	public void serverJoinEvent(EntityJoinWorldEvent e) {
		try {
			if (e != null) {
				if (e.entity != null && e.entity.equals(mc.thePlayer)) {
					if (enableTab.get()) {
						if (mc.ingameGUI != null) {
							tabList = new EnhancedTabList(this, mc.ingameGUI);
							vanillaTabList = new GuiPlayerTabOverlay(mc, mc.ingameGUI);
						}
						updateTabState(isEnabled());
					}
					
					hasPing = !mc.isSingleplayer();
					if (hasPing) {
						ping = -1;
					}
				}
				
				lastJoinedEntity = e.entity;
			}
		}
		catch (Exception q) { q.printStackTrace(); }
	}
	
	@Override
	public void clientTickEvent(TickEvent.ClientTickEvent e) {
		if (hasPing) {
			if (mc.theWorld != null && mc.thePlayer != null) {
				list = mc.theWorld.playerEntities;
				nethandlerplayclient = mc.thePlayer.sendQueue;
				order = Ordering.from(comparator);
				nameList = order.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
				if (!nameList.isEmpty()) {
					for (NetworkPlayerInfo i : nameList) {
						String name = i.getGameProfile().getName();
						if (name.equals(mc.thePlayer.getName())) { ping = i.getResponseTime(); }
					}
				}
			}
			
			if (tapTimer > 0) {
				if (System.currentTimeMillis() - tapTimer >= 300) {
					tapTimer = -1l;
				}
			}
			
		}
	}
	
	@Override
	public void keyEvent(KeyInputEvent e) {
		if (enableTab.get()) {
			if (Keyboard.isKeyDown(Keyboard.KEY_TAB) && !tabPressed) {
				if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
					toggleTab = !toggleTab;
				}
				else {
					if (tapTimer < 0) { tapTimer = System.currentTimeMillis(); }
					else if (System.currentTimeMillis() - tapTimer >= 50) {
						toggleTab = !toggleTab;
						tapTimer = -1l;
					}
				}
			}
			tabPressed = Keyboard.isKeyDown(Keyboard.KEY_TAB);
		}
	}
	
	@Override
	public void overlayPreEvent(RenderGameOverlayEvent.Pre e) {
		if (toggleTab && mc.theWorld != null) {
			if (e.type == ElementType.PLAYER_LIST && e.isCancelable()) {
				e.setCanceled(true);
			}
		}
	}
	
	@Override
	public void overlayPostEvent(RenderGameOverlayEvent.Post e) {
		if (enableTab.get() && toggleTab && mc.theWorld != null) {
			drawTab();
		}
		
		if (hasPing) {
			drawPing();
		}
	}
	
	@Override
	public void gameWindowResized(GameWindowResizedEvent e) {
		EDimension old = EnhancedMC.getRenderer().getDimensions();
		int oldW = old.width;
		int oldH = old.height;
		
		if (loc.get() == ScreenLocation.custom) {
			int newX = (xPos * e.getWidth()) / oldW;
			int newY = (yPos * e.getHeight()) / oldH;
			
			xPos = newX;
			yPos = newY;
		}
	}
	
	@Override public void setLocation(ScreenLocation locIn) { loc.set(locIn); }
	@Override public void setLocation(int xIn, int yIn) { loc.set(ScreenLocation.custom); xPos = xIn; yPos = yIn; }
	@Override public StorageBox<Integer, Integer> getLocation() { return new StorageBox<Integer, Integer>(xPos, yPos); }
	@Override public ScreenLocation getScreenLocation() { return loc.get(); }
	@Override public IWindowParent getScreenLocationGui() { return new PingSetLocationWindow(); }
	
	public static void removeEnhancedTab() { instance.updateTabState(false); instance.toggleTab = false; }
	public static void addEnhancedTab() { instance.updateTabState(true); }
	
	//a helper method which replaces the existing tabGui in GuiIngame
	private void replaceTabOverlay(GuiPlayerTabOverlay replaceIn) {
		GuiIngame gig = mc.ingameGUI;
		if (replaceIn != null && gig != null && gig.getClass().getSuperclass() != null) {
			try {
				Class parent = gig.getClass().getSuperclass();
				
				Field f = parent.getDeclaredField(EnhancedMC.isObfus() ? "field_175196_v" : "overlayPlayerList");
				f.setAccessible(true);
				
				Field mods = Field.class.getDeclaredField("modifiers");
				mods.setAccessible(true);
				mods.setInt(f, f.getModifiers() & ~Modifier.FINAL);
				
				f.set(gig, replaceIn);
				f.setAccessible(false);
			}
			catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	private void updateTabState(boolean val) {
		if (val) {
			if (!(mc.ingameGUI != null && mc.ingameGUI.getTabList() instanceof EnhancedTabList)) {
				replaceTabOverlay(tabList);
			}
		}
		else {
			if (mc.ingameGUI != null && mc.ingameGUI.getTabList() instanceof EnhancedTabList) {
				replaceTabOverlay(vanillaTabList);
			}
		}
	}
	
	private void drawTab() {
		ScaledResolution res = new ScaledResolution(mc);
		Scoreboard scoreboard = this.mc.theWorld.getScoreboard();
		ScoreObjective scoreobjective1 = scoreboard.getObjectiveInDisplaySlot(0);
		
		if (mc.isIntegratedServerRunning() && mc.thePlayer.sendQueue.getPlayerInfoMap().size() <= 1 && scoreobjective1 == null) {
			tabList.updatePlayerList(false);
		}
		else {
			tabList.updatePlayerList(true);
			tabList.renderPlayerlist(res.getScaledWidth(), scoreboard, scoreobjective1);
		}
	}
	
	private void drawPing() {
		if (!drawOwn.get()) { return; }
		if (!drawWithChat.get() && mc.ingameGUI.getChatGUI().getChatOpen()) { return; }
		if (!drawWithHud.get() && mc.currentScreen instanceof IRendererProxy) { return; }
		
		ScaledResolution res = new ScaledResolution(mc);
		if (mc.theWorld != null) {
			
			String msg = "Calculating..";
			String pingS = "";
			
			if (hasPing) {
				msg = "PING: ";
				pingS = ping + " ms";
			}
			
			int msgLen = mc.fontRendererObj.getStringWidth(msg);
			int pingLen = mc.fontRendererObj.getStringWidth(pingS);
			int l = msgLen + pingLen;
			
			boolean hud = Minecraft.getMinecraft().currentScreen instanceof IRendererProxy;
			TaskBar bar = EnhancedMC.getRenderer().getTaskBar();
			boolean border = CoreApp.drawHudBorder.get();
			
			int drawPosX = 0;
			int drawPosY = 0;
			
			switch (loc.get()) {
			case botLeft:
				drawPosX = 0;
				drawPosY = res.getScaledHeight();
				break;
			case botRight:
				drawPosX = res.getScaledWidth() - l - 1;
				drawPosY = res.getScaledHeight();
				break;
			case topLeft:
				drawPosX = 0;
				drawPosY = 11;
				break;
			case topRight:
				drawPosX = res.getScaledWidth() - l - 1;
				drawPosY = 11;
				break;
			case center:
				drawPosX = res.getScaledWidth() / 2 - (l / 2);
				drawPosY = res.getScaledHeight() / 2 + 26;
				break;
			case custom:
				drawPosX = xPos;
				drawPosY = yPos;
				break;
			default:
				drawPosX = res.getScaledWidth() / 2 - (l / 2);
				drawPosY = res.getScaledHeight() / 2 - 11;
			}
			
			if (hud) {
				int borderOffset = (border ? 1 : 0);
				int barOffset = (bar != null ? bar.height - 1 : 0);
				
				switch (loc.get()) {
				case botLeft:
					drawPosX += borderOffset;
					drawPosY -= borderOffset;
					break;
				case botRight:
					drawPosX -= borderOffset;
					drawPosY -= borderOffset;
					break;
				case topLeft:
					drawPosX += borderOffset;
					drawPosY += (barOffset + borderOffset);
					break;
				case topRight:
					drawPosX -= borderOffset;
					drawPosY += (barOffset + borderOffset);
					break;
				default: break;
				}
			}
			
			GLObject.drawRect(drawPosX, drawPosY, drawPosX + l + 1, drawPosY - 10, Integer.MIN_VALUE);
			
			if (drawOwnThresholds.get()) {
				int eX = GLObject.drawString(msg, drawPosX + 1, drawPosY - 9, ownColor.get());
				GLObject.drawString(pingS, eX, drawPosY - 9, getPingColor(ping));
			}
			else {
				GLObject.drawString(msg + pingS, drawPosX + 1, drawPosY - 9, ownColor.get());
			}
		}
	}
	
	public int getThreshold(String type) {
		switch (type) {
		case "l": return lowThresh.get();
		case "m": return medThresh.get();
		case "h": return highThresh.get();
		default: return 0;
		}
	}
	
	public int getColor(String type) {
		switch (type) {
		case "l": return lowColor.get();
		case "m": return medColor.get();
		case "h": return highColor.get();
		case "v": return vhighColor.get();
		default: return 0xffffff;
		}
	}
	
	public int getPingColor(int latencyIn) {
		if (latencyIn <= lowThresh.get()) { return lowColor.get(); }
		else if (latencyIn <= medThresh.get()) { return medColor.get(); }
		else if (latencyIn <= highThresh.get()) { return highColor.get(); }
		else { return vhighColor.get(); }
	}
	
	public Entity getLastJoinedEntity() { return lastJoinedEntity; }
	public int getClientServerPing() { return ping; }
	public boolean doesClientHavePing() { return hasPing; }
	
}
