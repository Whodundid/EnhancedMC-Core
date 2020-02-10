package com.Whodundid.core.subMod;

import com.Whodundid.core.coreEvents.emcEvents.ChatLineCreatedEvent;
import com.Whodundid.core.coreEvents.emcEvents.SubModCalloutEvent;
import com.Whodundid.core.coreEvents.emcEvents.TabCompletionEvent;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.subMod.config.SubModConfigManager;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.ModSetting;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

//Author: Hunter Bragg

public abstract class SubMod {
	
	protected Minecraft mc = Minecraft.getMinecraft();
	protected IWindowParent mainGui;
	protected EArrayList<IWindowParent> guis = new EArrayList();
	protected StorageBoxHolder<String, String> dependencies = new StorageBoxHolder().noDuplicates();
	protected StorageBoxHolder<String, String> softDependencies = new StorageBoxHolder().noDuplicates();
	protected String modName = "no name";
	protected EArrayList<String> nameAliases = new EArrayList();
	protected EArrayList<ModSetting> modSettings = new EArrayList();
	protected SubModConfigManager configManager;
	protected boolean enabled = false;
	protected String version = "no version";
	protected String author = "no author";
	protected String versionDate = "no date";
	protected boolean isDisableable = true;
	protected boolean incompatible = false; //a flag stating that this submod is incompatible with at least one other loaded emc submod
	
	public SubMod(SubModType modIn) { this(SubModType.getModName(modIn)); }
	public SubMod(String modNameIn) {
		modName = modNameIn;
		configManager = new SubModConfigManager(this);
	}
	
	public SubModType getModType() { return SubModType.getTypeFromString(modName); }
	public boolean isEnabled() { return enabled; }
	public boolean hasConfig() { return configManager.getNumberOfConfigFiles() > 0; }
	public boolean isDisableable() { return isDisableable; }
	public boolean isIncompatible() { return incompatible; }
	public EArrayList<IWindowParent> getGuis() { return guis; }
	public EArrayList<String> getNameAliases() { return nameAliases; }
	public EArrayList<ModSetting> getSettings() { return modSettings; }
	public StorageBoxHolder<String, String> getDependencies() { return dependencies; }
	public StorageBoxHolder<String, String> getSoftDependencies() { return softDependencies; }
	public IWindowParent getMainGui() throws Exception { return mainGui != null ? mainGui.getClass().newInstance() : null; }
	public SubModConfigManager getConfig() { return configManager; }
	public SubMod setEnabled(boolean valueIn) { enabled = valueIn; return this; }
	public SubMod setIncompatible(boolean valueIn) { incompatible = valueIn; return this; }
	public String getVersion() { return version; }
	public String getName() { return modName; }
	public String getAuthor() { return author; }
	public String getVersionDate() { return versionDate; }
	
	public SubMod setAliases(String alias, String... additional) {
		if (alias != null) {
			nameAliases.clear();
			nameAliases.add(alias);
			for (String s : additional) { nameAliases.add(s); }
		}
		return this;
	}
	
	public SubMod registerSetting(ModSetting setting, ModSetting... additional) {
		if (setting != null) {
			modSettings.add(setting);
			setting.setMod(this);
			for (ModSetting s : additional) { modSettings.add(s); s.setMod(this); }
		}
		return this;
	}
	
	public SubMod addDependency(SubModType typeIn, String versionIn) { return addDependency(SubModType.getModName(typeIn), versionIn); }
	public SubMod addDependency(String nameIn, String versionIn) {
		dependencies.add(nameIn, versionIn);
		return this;
	}
	
	protected SubMod setMainGui(IWindowParent guiIn) {
		IWindowParent oldGui = mainGui;
		if (oldGui != null) {
			if (guis.contains(oldGui)) {
				Iterator<IWindowParent> it = guis.iterator();
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
	
	protected SubMod addGui(IWindowParent... guiIn) {
		for (IWindowParent g : guiIn) { if (!guis.contains(g)) { guis.add(g); } }
		return this;
	}
	
	public void onPostInit() {}
	
	public Object sendArgs(Object... args) { return null; }
	
	//events
	
	//ticks
	public void tickEvent(TickEvent e) {}
	public void clientTickEvent(TickEvent.ClientTickEvent e) {}
	public void renderTickEvent(TickEvent.RenderTickEvent e) {}
	public void livingTickEvent(LivingUpdateEvent e) {}
	
	//overlay renders
	public void overlayEvent(RenderGameOverlayEvent e) {}
	public void overlayTextEvent(RenderGameOverlayEvent.Text e) {}
	public void overlayPreEvent(RenderGameOverlayEvent.Pre e) {}
	public void OverlayPostEvent(RenderGameOverlayEvent.Post e) {}
	
	//visual renders
	public void renderFogDensityEvent(EntityViewRenderEvent.FogDensity e) {}
	public void renderFogEvent(EntityViewRenderEvent.RenderFogEvent e) {}
	public void renderBlockOverlayEvent(RenderBlockOverlayEvent e) {}
	public void renderPlayerPreEvent(RenderPlayerEvent.Pre e) {}
	public void renderPlayerPostEvent(RenderPlayerEvent.Post e) {}
	public void renderLastWorldEvent(RenderWorldLastEvent e) {}
	
	//input
	public void mouseEvent(MouseEvent e) {}
	public void keyEvent(KeyInputEvent e) {}
	public void chatEvent(ClientChatReceivedEvent e) {}
	public void commandEvent(CommandEvent e) {}
	
	//guis
	public void initGuiEvent(GuiScreenEvent.InitGuiEvent e) {}
	
	//world
	public void worldLoadClientEvent(WorldEvent.Load e) {}
	public void worldLoadServerEvent(WorldEvent.Load e) {}
	public void worldUnloadEvent(WorldEvent.Unload e) {}
	public void serverJoinEvent(EntityJoinWorldEvent e) {}
	
	//emc specific
	public void terminalRegisterCommandEvent(ETerminal termIn, boolean runVisually) {}
	public void tabCompletionEvent(TabCompletionEvent e) {}
	public void chatLineCreatedEvent(ChatLineCreatedEvent e) {}
	public void subModCalloutEvent(SubModCalloutEvent e) {}
}
