package com.Whodundid.core.subMod;

import com.Whodundid.core.coreEvents.emcEvents.ChatLineCreatedEvent;
import com.Whodundid.core.coreEvents.emcEvents.ModCalloutEvent;
import com.Whodundid.core.coreEvents.emcEvents.TabCompletionEvent;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.subMod.config.SubModConfigManager;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
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
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

//First Added: Oct 15, 2018
//Author: Hunter Bragg

public abstract class SubMod {
	
	protected Minecraft mc = Minecraft.getMinecraft();
	protected IWindowParent mainGui;
	protected EArrayList<IWindowParent> guis = new EArrayList();
	protected StorageBoxHolder<String, String> dependencies = new StorageBoxHolder().noDuplicates();
	protected StorageBoxHolder<String, String> softDependencies = new StorageBoxHolder().noDuplicates();
	protected String modName = "noname";
	protected EArrayList<String> nameAliases = new EArrayList();
	protected SubModConfigManager configManager;
	protected boolean enabled = false;
	protected String version = "unspecified";
	protected String author = "unspecified";
	protected String versionDate = "unspecified";
	protected boolean isDisableable = true;
	protected boolean incompatible = false; //a flag stating that this submod is incompatible with at least one other loaded emc submod
	
	public SubMod(SubModType modIn) { this(SubModType.getModName(modIn)); }
	public SubMod(String modNameIn) {
		modName = modNameIn;
		configManager = new SubModConfigManager(this);
	}
	
	//public abstract SubMod getInstance();
	public SubModType getModType() { return SubModType.getTypeFromString(modName); }
	public boolean isEnabled() { return enabled; }
	public boolean hasConfig() { return configManager.getNumberOfConfigFiles() > 0; }
	public boolean isDisableable() { return isDisableable; }
	public boolean isIncompatible() { return incompatible; }
	public EArrayList<IWindowParent> getGuis() { return guis; }
	public EArrayList<String> getNameAliases() { return nameAliases; }
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
		nameAliases.clear();
		nameAliases.add(alias);
		for (String s : additional) { nameAliases.add(s); }
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
	
	public void eventTick(TickEvent e) {}
	public void eventClientTick(TickEvent.ClientTickEvent e) {}
	public void eventRenderTick(TickEvent.RenderTickEvent e) {}
	public void eventOverlayRenderTick(RenderGameOverlayEvent e) {}
	public void eventTextOverlayRenderTick(RenderGameOverlayEvent.Text e) {}
	public void eventPreOverlayRenderTick(RenderGameOverlayEvent.Pre e) {}
	public void eventPostOverlayRenderTick(RenderGameOverlayEvent.Post e) {}
	public void eventRenderFogTick(EntityViewRenderEvent.FogDensity e) {}
	public void eventBlockOverlayTick(RenderBlockOverlayEvent e) {}
	public void eventRenderPlayer(RenderPlayerEvent e) {}
	public void eventLastWorldRender(RenderWorldLastEvent e) {}
	public void eventLivingTick(LivingUpdateEvent e) {}
	public void eventMouse(MouseEvent e) {}
	public void eventKey(KeyInputEvent e) {}
	public void eventChat(ClientChatReceivedEvent e) {}
	public void eventChatLineCreated(ChatLineCreatedEvent e) {}
	public void eventWorldLoadClient(WorldEvent.Load e) {}
	public void eventWorldLoadServer(Load e) {}
	public void eventWorldUnload(WorldEvent.Unload e) {}
	public void eventInitGui(GuiScreenEvent.InitGuiEvent e) {}
	public void eventServerJoin(EntityJoinWorldEvent e) {}
	public void eventModCallout(ModCalloutEvent e) {}
	public void eventCommand(CommandEvent e) {}
	public void eventResize(ScaledResolution newRes) {}
	public void eventTerminalRegister(ETerminal termIn, boolean runVisually) {}
	public void eventTabCompletion(TabCompletionEvent e) {}
}
