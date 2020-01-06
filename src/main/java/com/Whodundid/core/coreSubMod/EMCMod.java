package com.Whodundid.core.coreSubMod;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreEvents.emcEvents.TabCompletionEvent;
import com.Whodundid.core.renderer.IRendererProxy;
import com.Whodundid.core.renderer.RendererProxyGui;
import com.Whodundid.core.settings.KeyBindGui;
import com.Whodundid.core.settings.SettingsGuiMain;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.subMod.SubModType;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.chatUtil.EChatUtil;
import com.Whodundid.core.util.miscUtil.EMouseHelper;
import com.Whodundid.core.util.playerUtil.PlayerFacing;
import com.Whodundid.core.util.renderUtil.BlockDrawer;
import com.Whodundid.core.util.renderUtil.CursorHelper;
import com.Whodundid.core.util.serverUtil.ServerConnector;
import com.Whodundid.core.util.storageUtil.ModSetting;
import com.Whodundid.core.util.worldUtil.WorldEditListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
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

public class EMCMod extends SubMod {
	
	public static final ModSetting<Boolean> emcMenuOverride = new ModSetting(true);
	public static final ModSetting<Boolean> useDebugKey = new ModSetting(false);
	public static final ModSetting<Boolean> showIncompats = new ModSetting(false);
	public static final ModSetting<Boolean> enableTerminal = new ModSetting(false);
	public static final ModSetting<String> drawChatOnGui = new ModSetting("Partial").setArgs("Partial", "Off", "Full");
	
	private boolean oldWasProxy;
	private boolean firstPass = false;
	private long startLoadTime = 0l;
	
	public EMCMod() {
		super(SubModType.CORE);
		version = EnhancedMC.VERSION;
		author = "Whodundid";
		configManager.setMainConfig(new EMCConfig(this, "enhancedMCCore"));
		setEnabled(true);
		setMainGui(new EMCModSettingsGui());
		addGui(new SettingsGuiMain(), new KeyBindGui());
		setAliases("enhancedmc", "emc", "core");
		isDisableable = false;
	}
	
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
		
		//hijack the vanilla pause menu
		if (e.gui instanceof GuiIngameMenu) {
			if (EMCMod.emcMenuOverride.get()) { mc.displayGuiScreen(new EMCPauseMenu()); }
		}
		
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
		if (e.type == ElementType.CHAT && mc.currentScreen instanceof IRendererProxy && EnhancedMC.getEMCMod().drawChatOnGui.get().equals("Off")) { e.setCanceled(true); }
	}
	
	@Override public void keyEvent(KeyInputEvent e) { EnhancedMC.checkKeyBinds(); }
	@Override public void mouseEvent(MouseEvent e) { EMouseHelper.mouseClicked(e.button); }
	@Override public void renderTickEvent(TickEvent.RenderTickEvent e) { PlayerFacing.checkEyePosition(e); }
	@Override public void OverlayPostEvent(RenderGameOverlayEvent.Post e) { EnhancedMC.getRenderer().onRenderTick(e); }
	@Override public void renderLastWorldEvent(RenderWorldLastEvent e) { BlockDrawer.draw(e); }
	@Override public void chatEvent(ClientChatReceivedEvent e) { EChatUtil.readChat(e.message); WorldEditListener.checkForPositions(); }
	@Override public void tabCompletionEvent(TabCompletionEvent e) { EChatUtil.onTabComplete(e.getCompletion()); }
	@Override public void worldLoadClientEvent(WorldEvent.Load e) { CursorHelper.reset(); }
	@Override public void worldLoadServerEvent(WorldEvent.Load e) { CursorHelper.reset(); }
	@Override public void serverJoinEvent(EntityJoinWorldEvent e) { CursorHelper.reset(); }
	
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
			return drawChatOnGui.get().equals("Full");
		}
		return s instanceof GuiChat;
	}
}
