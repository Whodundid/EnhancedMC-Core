package com.Whodundid.pingDrawer;

import com.Whodundid.core.app.AppConfigSetting;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.IUseScreenLocation;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.util.miscUtil.NetPlayerComparator;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.google.common.collect.Ordering;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import net.minecraft.client.gui.Gui;
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
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

//Last edited: Dec 10, 2018
//First Added: Oct 19, 2018
//Author: Hunter Bragg

@Mod(modid = PingApp.MODID, version = PingApp.VERSION, name = PingApp.NAME, dependencies = "required-after:enhancedmc")
public final class PingApp extends EMCApp implements IUseScreenLocation {

	public static final String MODID = "pingdisplay";
	public static final String VERSION = "2.0";
	public static final String NAME = "Ping Display";
	
	protected Entity lastJoinedEntity;
	protected int ping = 0;
	protected int xPos = 0, yPos = 0;
	protected boolean hasPing = false;
	public boolean drawWithChatOpen = false;
	private EnhancedTabList tabList = null;
	private GuiPlayerTabOverlay vanillaTabList = null;
	NetHandlerPlayClient nethandlerplayclient;
	Ordering<NetworkPlayerInfo> order;
	List<EntityPlayer> list;
	List<NetworkPlayerInfo> nameList;
	
	AppConfigSetting<ScreenLocation> loc = new AppConfigSetting(ScreenLocation.class, "loc", "Screen Draw Location", ScreenLocation.botLeft);
	
	protected boolean toggleTab = false;
	protected boolean tabPressed = false;
	long tapTimer = -1l;
	
	int lowColor = 0x55ff55;
	int medColor = 0xffff00;
	int highColor = 0xff9900;
	int vHighColor = 0xff5555;
	
	int lowThresh = 100;
	int medThresh = 175;
	int highThresh = 250;
	
	public PingApp() {
		super(AppType.PING);
		version = VERSION;
		shouldLoad = false;
		author = "Whodundid";
		addDependency(AppType.CORE, "1.0");
		configManager.setMainConfig(new PingConfig(this, "pingConfig"));
		setMainGui(new PingGui());
		addGui(new PingSetLocationGui());
		setAliases("ping", "latency");
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
		if (isEnabled()) {
			try {
				if (e != null) {
					if (e.entity != null && e.entity.equals(mc.thePlayer)) {
						if (mc.ingameGUI != null) {
							tabList = new EnhancedTabList(this, mc.ingameGUI);
							vanillaTabList = new GuiPlayerTabOverlay(mc, mc.ingameGUI);
						}
						updateTabState(isEnabled());
						
						hasPing = !mc.isSingleplayer();
						if (hasPing) {
							ping = -1;
						}
					}
					
					lastJoinedEntity = e.entity;
				}
			} catch (Exception q) { q.printStackTrace(); }
		}
	}
	
	@Override
	public void clientTickEvent(TickEvent.ClientTickEvent e) {
		//System.out.println(hasPing);
		if (isEnabled() && hasPing) {
			if (mc.theWorld != null && mc.thePlayer != null) {
				list = mc.theWorld.playerEntities;
				nethandlerplayclient = mc.thePlayer.sendQueue;
				order = Ordering.from(new NetPlayerComparator());
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
	
	@Override
	public void overlayPreEvent(RenderGameOverlayEvent.Pre e) {
		if (isEnabled()) {
			if (toggleTab && mc.theWorld != null) {
				if (e.type == ElementType.PLAYER_LIST && e.isCancelable()) {
					e.setCanceled(true);
				}
			}
		}
	}
	
	@Override
	public void OverlayPostEvent(RenderGameOverlayEvent.Post e) {
		//!(EnhancedMC.isEGuiOpen(PingSetLocationGui.class))
		if (isEnabled()) {
			if (toggleTab && mc.theWorld != null) {
				drawTab();
			}
			if (hasPing) {
				drawPing();
			}
		}
	}
	
	@Override public void setLocation(ScreenLocation locIn) { loc.set(locIn); }
	@Override public void setLocation(int xIn, int yIn) { loc.set(ScreenLocation.custom); xPos = xIn; yPos = yIn; }
	@Override public StorageBox<Integer, Integer> getLocation() { return new StorageBox<Integer, Integer>(xPos, yPos); }
	@Override public ScreenLocation getScreenLocation() { return loc.get(); }
	@Override public IWindowParent getScreenLocationGui() { return new PingSetLocationGui(); }
	
	//a helper method which replaces the existing tabGui in GuiIngame
	private void replaceTabOverlay(GuiPlayerTabOverlay replaceIn) {
		GuiIngame gig = mc.ingameGUI;
		if (replaceIn != null && gig != null && gig.getClass().getSuperclass() != null) {
			try {
				Class parent = gig.getClass().getSuperclass();
				boolean isObfuscated = parent.getSimpleName().equals("avo");
				
				Field f = parent.getDeclaredField(isObfuscated ? "v" : "overlayPlayerList");
				f.setAccessible(true);
				
				Field mods = Field.class.getDeclaredField("modifiers");
				mods.setAccessible(true);
				mods.setInt(f, f.getModifiers() & ~Modifier.FINAL);
				
				f.set(gig, replaceIn);
				f.setAccessible(false);
			} catch (Exception e) { e.printStackTrace(); }
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
		if (!drawWithChatOpen && mc.ingameGUI.getChatGUI().getChatOpen()) { return; }
		ScaledResolution res = new ScaledResolution(mc);
		if (mc.theWorld != null && !mc.isSingleplayer()) {
			String msg = ping > 0 ? "PING: " + ping + " ms" : "Calculating..";
			int l = mc.fontRendererObj.getStringWidth(msg);
			int drawPosX = 0, drawPosY = 0;
			switch (loc.get()) {
			case botLeft: drawPosX = 0; drawPosY = res.getScaledHeight(); break;
			case botRight: drawPosX = res.getScaledWidth() - l - 1; drawPosY = res.getScaledHeight(); break;
			case topLeft: drawPosX = 0; drawPosY = 11; break;
			case topRight: drawPosX = res.getScaledWidth() - l - 1; drawPosY = 11; break;
			case center: drawPosX = res.getScaledWidth() / 2 - (l / 2); drawPosY = res.getScaledHeight() / 2 - 11; break;
			case custom: drawPosX = xPos; drawPosY = yPos; break;
			default: drawPosX = res.getScaledWidth() / 2 - (l / 2); drawPosY = res.getScaledHeight() / 2 - 11; break;
			}
			Gui.drawRect(drawPosX, drawPosY, drawPosX + l + 1, drawPosY - 10, Integer.MIN_VALUE);
			mc.fontRendererObj.drawString(msg, drawPosX + 1, drawPosY - 9, 0x00ff00);
		}
	}
	
	public int getThreshold(String type) {
		switch (type) {
		case "l": return lowThresh;
		case "m": return medThresh;
		case "h": return highThresh;
		default: return 0;
		}
	}
	
	public int getColor(String type) {
		switch (type) {
		case "l": return lowColor;
		case "m": return medColor;
		case "h": return highColor;
		case "v": return vHighColor;
		default: return 0xffffff;
		}
	}
	
	public int getPingColor(int latencyIn) {
		if (latencyIn < lowThresh) { return lowColor; }
		else if (latencyIn < medThresh) { return medColor; }
		else if (latencyIn < highThresh) { return highColor; }
		else { return vHighColor; }
	}
	
	public PingApp setDrawWithChatOpen(boolean val) { drawWithChatOpen = val; return this; }
	public Entity getLastJoinedEntity() { return lastJoinedEntity; }
	public int getClientServerPing() { return ping; }
	public boolean doesClientHavePing() { return hasPing; }
}
