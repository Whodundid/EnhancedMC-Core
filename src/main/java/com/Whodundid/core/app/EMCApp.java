package com.Whodundid.core.app;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.config.AppConfigManager;
import com.Whodundid.core.app.config.AppConfigSetting;
import com.Whodundid.core.coreEvents.emcEvents.ChatLineCreatedEvent;
import com.Whodundid.core.coreEvents.emcEvents.EMCAppCalloutEvent;
import com.Whodundid.core.coreEvents.emcEvents.GameWindowResizedEvent;
import com.Whodundid.core.coreEvents.emcEvents.RendererRCMOpenEvent;
import com.Whodundid.core.coreEvents.emcEvents.TabCompletionEvent;
import com.Whodundid.core.coreEvents.emcEvents.WindowClosedEvent;
import com.Whodundid.core.coreEvents.emcEvents.WindowOpenedEvent;
import com.Whodundid.core.notifications.util.NotificationType;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.resourceUtil.EResource;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowParent;
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
	protected IWindowParent mainWindow;
	protected EArrayList<IWindowParent> windows = new EArrayList();
	protected StorageBoxHolder<String, String> dependencies = new StorageBoxHolder().noDuplicates();
	protected StorageBoxHolder<String, String> softDependencies = new StorageBoxHolder().noDuplicates();
	protected String appName = "no name";
	protected EArrayList<String> nameAliases = new EArrayList();
	protected EArrayList<AppConfigSetting> appSettings = new EArrayList();
	protected EArrayList<NotificationType> notifications = new EArrayList();
	protected AppConfigManager configManager;
	protected boolean enabled = false;
	protected String version = "no version";
	protected String author = "no author";
	protected String artist = "no artist";
	protected String additionalInfo = null;
	protected StorageBox<String, String> donation = null;
	protected EArrayList<String> contributors = new EArrayList();
	protected String versionDate = "no date";
	protected boolean isDisableable = true;
	protected boolean canBeEnabled = true;
	protected boolean incompatible = false; //flag stating that this app is incompatible with at least one other loaded emc apps
	protected boolean shouldLoad = true; //flag for whether or not this app should be loaded into the core
	protected boolean blockedOnHypixel = false;
	protected AppResources resources;
	protected EArrayList<EResource> logo = new EArrayList();
	protected long logoInterval = 1000l;
	protected boolean enforceMajorVersion = true;
	protected boolean enforceMinorVersion = false;
	protected boolean enforceBuildVersion = false;
	
	public EMCApp(AppType appIn) { this(AppType.getAppName(appIn)); }
	public EMCApp(String appNameIn) {
		appName = appNameIn;
		configManager = new AppConfigManager(this);
		build();
	}
	
	public abstract void build();
	
	public void rebuild() {
		appSettings.clear();
		windows.clear();
		dependencies.clear();
		nameAliases.clear();
		softDependencies.clear();
		notifications.clear();
		
		build();
	}
	
	@Override
	public int compareTo(EMCApp app) {
		return appName.compareTo(app.appName);
	}
	
	public AppType getAppType() { return AppType.getTypeFromString(appName); }
	public boolean isEnabled() { return enabled; }
	public boolean hasConfig() { return configManager.getNumberOfConfigFiles() > 0; }
	public boolean isDisableable() { return isDisableable; }
	public boolean canBeEnabled() { return canBeEnabled; }
	public boolean isIncompatible() { return incompatible; }
	public EArrayList<IWindowParent> getWindows() { return windows; }
	public EArrayList<String> getNameAliases() { return nameAliases; }
	public EArrayList<AppConfigSetting> getSettings() { return appSettings; }
	public StorageBoxHolder<String, String> getDependencies() { return dependencies; }
	public StorageBoxHolder<String, String> getSoftDependencies() { return softDependencies; }
	public IWindowParent getMainWindow() throws Exception { return mainWindow != null ? mainWindow.getClass().newInstance() : null; }
	public AppConfigManager getConfig() { return configManager; }
	public EMCApp setEnabled(boolean valueIn) { enabled = valueIn; return this; }
	public EMCApp setCanBeEnabled(boolean val) { canBeEnabled = val; return this; }
	public EMCApp setIncompatible(boolean valueIn) { incompatible = valueIn; return this; }
	public String getVersion() { return version; }
	public String getName() { return appName; }
	public String getAuthor() { return author; }
	public String getArtist() { return artist; }
	public String getVersionDate() { return versionDate; }
	public String getAdditionalInfo() { return additionalInfo; }
	public boolean getBlockedOnHypixel() { return blockedOnHypixel; }
	public StorageBox<String, String> getDonation() { return donation; }
	public EArrayList<String> getContributors() { return contributors; }
	public EMCApp setResources(AppResources resourcesIn) { resources = resourcesIn; return this; }
	public AppResources getResources() { return resources; }
	public EArrayList<EResource> getLogo() { return logo; }
	public EArrayList<NotificationType> getNotifications() { return notifications; }
	public long getLogoInterval() { return logoInterval; }
	public boolean enforcesMajorVersion() { return enforceMajorVersion; }
	public boolean enforcesMinorVersion() { return enforceMinorVersion; }
	public boolean enforcesBuildVersion() { return enforceBuildVersion; }
	
	public EMCApp setAliases(String alias, String... additional) {
		if (alias != null) {
			nameAliases.clear();
			nameAliases.add(alias);
			EUtil.filterNullDo(s -> nameAliases.add(s), additional);
		}
		return this;
	}
	
	public EMCApp registerNotifcation(NotificationType note, NotificationType... additional) {
		if (note != null) {
			notifications.add(note);
			EnhancedMC.getNotificationHandler().registerNotificationType(note);
			EUtil.filterNullDo(t -> EnhancedMC.getNotificationHandler().registerNotificationType(t), additional);
		}
		return this;
	}
	
	public EMCApp registerSetting(AppConfigSetting setting, AppConfigSetting... additional) {
		if (setting != null) {
			appSettings.add(setting);
			setting.setApp(this);
			EUtil.filterNullDo(s -> { appSettings.add(s); s.setApp(this); }, additional); 
		}
		return this;
	}
	
	public EMCApp addDependency(AppType typeIn, String versionIn) { return addDependency(AppType.getAppName(typeIn), versionIn); }
	public EMCApp addDependency(String nameIn, String versionIn) {
		if (nameIn != null && versionIn != null) {
			dependencies.add(nameIn, versionIn);
		}
		return this;
	}
	
	protected EMCApp setMainWindow(IWindowParent windowIn) {
		IWindowParent oldWindow = mainWindow;
		if (oldWindow != null) {
			if (windows.contains(oldWindow)) {
				Iterator<IWindowParent> it = windows.iterator();
				while (it.hasNext()) {
					if (oldWindow.equals(it.next())) { it.remove(); break; } 
				}
				oldWindow = null;
			}
		}
		mainWindow = windowIn;
		windows.add(mainWindow);
		return this;
	}
	
	protected EMCApp addWindow(IWindowParent... windowIn) {
		for (IWindowParent g : windowIn) { if (!windows.contains(g)) { windows.add(g); } }
		return this;
	}
	
	public void onPostInit(FMLPostInitializationEvent event) {}
	public void onDevModeDisabled() {}
	
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
	public void gameWindowResized(GameWindowResizedEvent e) {}
	
}
