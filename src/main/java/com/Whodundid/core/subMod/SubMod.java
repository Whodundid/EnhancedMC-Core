package com.Whodundid.core.subMod;

import com.Whodundid.core.enhancedGui.EnhancedGui;
import com.Whodundid.core.events.emcEvents.ChatLineCreatedEvent;
import com.Whodundid.core.events.emcEvents.ModCalloutEvent;
import com.Whodundid.core.subMod.config.SubModConfigManager;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import java.math.BigDecimal;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

//Last edited: Dec 27, 2018
//First Added: Oct 15, 2018
//Author: Hunter Bragg

public abstract class SubMod {
	
	protected Minecraft mc = Minecraft.getMinecraft();
	protected EnhancedGui mainGui;
	protected EArrayList<GuiScreen> guis = new EArrayList();
	protected StorageBoxHolder<SubModType, BigDecimal> dependencies = new StorageBoxHolder();
	protected StorageBoxHolder<SubModType, BigDecimal> softDependencies = new StorageBoxHolder();
	protected String modName = "noname";
	protected SubModConfigManager configManager;
	protected boolean enabled = false;
	protected String version = "unspecified";
	protected String author = "unspecified";
	protected String versionDate = "unspecified";
	protected boolean isDisableable = true;
	protected boolean incompatible = false;
	private SubModType mod;
	
	public SubMod(SubModType modIn) {
		mod = modIn;
		modName = SubModType.getModName(modIn);
		configManager = new SubModConfigManager(this);
	}
	
	public abstract SubMod getInstance();
	public SubModType getModType() { return mod; }
	public boolean isEnabled() { return enabled; }
	public boolean hasConfig() { return configManager.getNumberOfConfigFiles() > 0; }
	public boolean isDisableable() { return isDisableable; }
	public boolean isIncompatible() { return incompatible; }
	public EArrayList<GuiScreen> getGuis() { return guis; }
	public StorageBoxHolder<SubModType, BigDecimal> getDependencies() { return dependencies; }
	public StorageBoxHolder<SubModType, BigDecimal> getSoftDependencies() { return softDependencies; }
	public EnhancedGui getMainGui(boolean setPosition, StorageBox<Integer, Integer> pos, EnhancedGui oldGui) { return mainGui; }
	public SubModConfigManager getConfig() { return configManager; }
	public SubMod setEnabled(boolean valueIn) { enabled = valueIn; return this; }
	public SubMod setIncompatible(boolean valueIn) { incompatible = valueIn; return this; }
	public String getVersion() { return version; }
	public String getName() { return modName; }
	public String getAuthor() { return author; }
	public String getVersionDate() { return versionDate; }
	
	protected SubMod setMainGui(EnhancedGui guiIn) {
		EnhancedGui oldGui = mainGui;
		if (oldGui != null) {
			if (mc.currentScreen.equals(oldGui)) { mc.displayGuiScreen(null); }
			if (guis.contains(oldGui)) {
				Iterator<GuiScreen> it = guis.iterator();
				while (it.hasNext()) {
					if (oldGui.equals(it.next())) { it.remove(); break; } 
				}
				oldGui = null;
			}
		}
		mainGui = guiIn;
		guis.add(mainGui);
		return this;
	}
	
	protected SubMod addGui(EnhancedGui... guiIn) {
		for (EnhancedGui gui : guiIn) { if (!guis.contains(gui)) { guis.add(gui); } }
		return this;
	}
	
	public void onPostInit() {}
	
	public Object sendArgs(Object... args) { return null; }
	
	public void eventClientTick(TickEvent.ClientTickEvent e) {}
	public void eventRenderTick(TickEvent.RenderTickEvent e) {}
	public void eventOverlayRenderTick(RenderGameOverlayEvent e) {}
	public void eventTextOverlayRenderTick(RenderGameOverlayEvent.Text e) {}
	public void eventPreOverlayRenderTick(RenderGameOverlayEvent.Pre e) {}
	public void eventPostOverlayRenderTick(RenderGameOverlayEvent.Post e) {}
	public void eventRenderFogTick(EntityViewRenderEvent.FogDensity e) {}
	public void eventBlockOverlayTick(RenderBlockOverlayEvent e) {}
	public void eventRenderPlayer(RenderPlayerEvent e) {}
	public void eventLivingTick(LivingUpdateEvent e) {}
	public void eventMouse(MouseEvent e) {}
	public void eventKey(KeyInputEvent e) {}
	public void eventChat(ClientChatReceivedEvent e) {}
	public void eventChatLineCreated(ChatLineCreatedEvent e) {}
	public void eventWorldLoad(WorldEvent.Load e) {}
	public void eventWorldUnload(WorldEvent.Unload e) {}
	public void eventInitGui(GuiScreenEvent.InitGuiEvent e) {}
	public void eventServerJoin(EntityJoinWorldEvent e) {}
	public void eventModCallout(ModCalloutEvent e) {}
	public void eventCommand(CommandEvent e) {}
}
