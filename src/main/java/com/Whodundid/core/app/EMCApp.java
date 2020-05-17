package com.Whodundid.core.app;

import com.Whodundid.core.app.config.AppConfigManager;
import com.Whodundid.core.coreEvents.emcEvents.ChatLineCreatedEvent;
import com.Whodundid.core.coreEvents.emcEvents.EMCAppCalloutEvent;
import com.Whodundid.core.coreEvents.emcEvents.RendererRCMOpenEvent;
import com.Whodundid.core.coreEvents.emcEvents.TabCompletionEvent;
import com.Whodundid.core.coreEvents.emcEvents.WindowClosedEvent;
import com.Whodundid.core.coreEvents.emcEvents.WindowOpenedEvent;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.util.resourceUtil.EResource;
import com.Whodundid.core.util.storageUtil.EArrayList;
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
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

//Author: Hunter Bragg

public abstract class EMCApp implements Comparable<EMCApp> {
	
	protected Minecraft mc = Minecraft.getMinecraft();
	protected IWindowParent mainGui;
	protected EArrayList<IWindowParent> guis = new EArrayList();
	protected StorageBoxHolder<String, String> dependencies = new StorageBoxHolder().noDuplicates();
	protected StorageBoxHolder<String, String> softDependencies = new StorageBoxHolder().noDuplicates();
	protected String appName = "no name";
	protected EArrayList<String> nameAliases = new EArrayList();
	protected EArrayList<AppConfigSetting> modSettings = new EArrayList();
	protected AppConfigManager configManager;
	protected boolean enabled = false;
	protected String version = "no version";
	protected String author = "no author";
	protected String artist = "no artist";
	protected EArrayList<String> contributors = new EArrayList();
	protected String versionDate = "no date";
	protected boolean isDisableable = true;
	protected boolean incompatible = false; //a flag stating that this submod is incompatible with at least one other loaded emc submod
	protected boolean shouldLoad = true; //flag for whether or not this app should be loaded into the core
	protected AppResources resources;
	protected EArrayList<EResource> logo = new EArrayList();
	protected long logoInterval = 1000l;
	
	public EMCApp(AppType appIn) { this(AppType.getAppName(appIn)); }
	public EMCApp(String appNameIn) {
		appName = appNameIn;
		configManager = new AppConfigManager(this);
	}
	
	@Override
	public int compareTo(EMCApp app) {
		return appName.compareTo(app.appName);
	}
	
	public AppType getAppType() { return AppType.getTypeFromString(appName); }
	public boolean isEnabled() { return enabled; }
	public boolean hasConfig() { return configManager.getNumberOfConfigFiles() > 0; }
	public boolean isDisableable() { return isDisableable; }
	public boolean isIncompatible() { return incompatible; }
	public EArrayList<IWindowParent> getGuis() { return guis; }
	public EArrayList<String> getNameAliases() { return nameAliases; }
	public EArrayList<AppConfigSetting> getSettings() { return modSettings; }
	public StorageBoxHolder<String, String> getDependencies() { return dependencies; }
	public StorageBoxHolder<String, String> getSoftDependencies() { return softDependencies; }
	public IWindowParent getMainGui() throws Exception { return mainGui != null ? mainGui.getClass().newInstance() : null; }
	public AppConfigManager getConfig() { return configManager; }
	public EMCApp setEnabled(boolean valueIn) { enabled = valueIn; return this; }
	public EMCApp setIncompatible(boolean valueIn) { incompatible = valueIn; return this; }
	public String getVersion() { return version; }
	public String getName() { return appName; }
	public String getAuthor() { return author; }
	public String getArtist() { return artist; }
	public String getVersionDate() { return versionDate; }
	public EArrayList<String> getContributors() { return contributors; }
	public EMCApp setResources(AppResources resourcesIn) { resources = resourcesIn; return this; }
	public AppResources getResources() { return resources; }
	public EArrayList<EResource> getLogo() { return logo; }
	public long getLogoInterval() { return logoInterval; }
	
	public EMCApp setAliases(String alias, String... additional) {
		if (alias != null) {
			nameAliases.clear();
			nameAliases.add(alias);
			for (String s : additional) { nameAliases.add(s); }
		}
		return this;
	}
	
	public EMCApp registerSetting(AppConfigSetting setting, AppConfigSetting... additional) {
		if (setting != null) {
			modSettings.add(setting);
			setting.setMod(this);
			for (AppConfigSetting s : additional) { modSettings.add(s); s.setMod(this); }
		}
		return this;
	}
	
	public EMCApp addDependency(AppType typeIn, String versionIn) { return addDependency(AppType.getAppName(typeIn), versionIn); }
	public EMCApp addDependency(String nameIn, String versionIn) {
		dependencies.add(nameIn, versionIn);
		return this;
	}
	
	protected EMCApp setMainGui(IWindowParent guiIn) {
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
	
	protected EMCApp addGui(IWindowParent... guiIn) {
		for (IWindowParent g : guiIn) { if (!guis.contains(g)) { guis.add(g); } }
		return this;
	}
	
	public void onPostInit(FMLPostInitializationEvent event) {}
	
	public void registerResources() {
		if (resources != null) {
			resources.registerResources();
		}
	}
	
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
	public void overlayPostEvent(RenderGameOverlayEvent.Post e) {}
	
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
	public void rendererRCMOpenEvent(RendererRCMOpenEvent e) {}
	public void tabCompletionEvent(TabCompletionEvent e) {}
	public void chatLineCreatedEvent(ChatLineCreatedEvent e) {}
	public void subModCalloutEvent(EMCAppCalloutEvent e) {}
	public void windowOpenedEvent(WindowOpenedEvent e) {}
	public void windowClosedEvent(WindowClosedEvent e) {}
}
