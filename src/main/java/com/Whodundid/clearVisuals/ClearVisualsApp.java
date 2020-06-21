package com.Whodundid.clearVisuals;

import com.Whodundid.clearVisuals.util.CVResources;
import com.Whodundid.clearVisuals.window.ClearVisualsWindow;
import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.config.AppConfigFile;
import com.Whodundid.core.app.config.AppConfigSetting;
import com.Whodundid.core.util.hypixel.HypixelData;
import com.Whodundid.core.util.resourceUtil.EResource;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.worldUtil.WorldHelper;
import net.minecraft.block.material.Material;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

//Dec 29, 2018
//Last edited: Jul 1, 2019
//First Added: Mar 22, 2018
//Author: Hunter Bragg

@Mod(modid = ClearVisualsApp.MODID, version = ClearVisualsApp.VERSION, name = ClearVisualsApp.NAME, dependencies = "required-after:enhancedmc")
public final class ClearVisualsApp extends EMCApp {
	
	public static final String MODID = "clearvisuals";
	public static final String VERSION = "1.1";
	public static final String NAME = "ClearVisuals";
	public float vanillaGama = 0.0f;
	
	//---------
	//Resources
	//---------
	
	public static final CVResources resources = new CVResources();
	
	//---------------
	//config settings
	//---------------
	
	public static final AppConfigSetting<Float> gama = new AppConfigSetting(Float.class, "gama", "Gama", 0.0f);
	public static final AppConfigSetting<Boolean> removeFire = new AppConfigSetting(Boolean.class, "removeFire", "Remove Fire", true);
	public static final AppConfigSetting<Boolean> removeWaterOverlay = new AppConfigSetting(Boolean.class, "removeWater", "Remove Water Overlay", true);
	public static final AppConfigSetting<Boolean> removeFog = new AppConfigSetting(Boolean.class, "removeFog", "Remove Fog", true);
	public static final AppConfigSetting<Boolean> clearLava = new AppConfigSetting(Boolean.class, "clearLava", "Clear Lava", true);
	public static final AppConfigSetting<Boolean> clearWater = new AppConfigSetting(Boolean.class, "clearWater", "Clear Water", true);
	
	protected boolean preClearLava = clearLava.get();
	protected boolean preClearWater = clearWater.get();
	protected boolean allowClearFluids = true;
	private static ClearVisualsApp instance;
	
	public ClearVisualsApp() {
		super(AppType.CLEARVISUALS);
		instance = this;
	}
	
	@Override
	public void build() {
		version = VERSION;
		author = "Whodundid";
		artist = "Mr.JamminOtter";
		versionDate = "June 11, 2020";
		donation = new StorageBox("Consider donating to support EMC development!", "https://www.paypal.me/Whodundid");
		
		addDependency(AppType.CORE, "1.0");
		configManager.setMainConfig(new AppConfigFile(this, "clearVisuals", "EMC Clear Visuals Config"));
		
		registerSetting(gama, removeFire, removeWaterOverlay, removeFog, clearLava, clearWater);
		
		setResources(resources);
		logo = new EArrayList<EResource>(CVResources.logo);
		
		setMainWindow(new ClearVisualsWindow());
		setAliases("clearvisuals", "cv");
	}
	
	public static ClearVisualsApp instance() { return instance; }
	
	@Override
	public ClearVisualsApp setEnabled(boolean valIn) {
		super.setEnabled(valIn);
		if (valIn) { configManager.loadMainConfig(); }
		else { mc.gameSettings.gammaSetting = 0.0f; }
		return this;
	}
	
	@Override
	public void clientTickEvent(ClientTickEvent e) {
		if (EnhancedMC.isHypixel()) {
			HypixelData data = EnhancedMC.getHypixelData();
			
			if (data != null) {
				allowClearFluids = !(!(data.isLobby() || data.isLimbo()) && data.isPvp());
			}
			else { allowClearFluids = true; }
		}
		else {
			allowClearFluids = true;
		}
	}
	
	@Override
	public void renderFogDensityEvent(EntityViewRenderEvent.FogDensity e) {
		if (isEnabled()) {
			if (allowClearFluids) {
				if (e.block.getMaterial() == Material.lava && clearLava.get()) { e.density = 0; e.setCanceled(true); }
				if (e.block.getMaterial() == Material.water && clearWater.get()) { e.density = 0; e.setCanceled(true); }
			}
			if (e.block.getMaterial() == Material.air && removeFog.get()) { e.density = 0; e.setCanceled(true); }
		}
	}
	
	@Override
	public void renderFogEvent(EntityViewRenderEvent.RenderFogEvent e) {
		//if (removeFog && e.block.getMaterial() == Material.air) { System.out.println(e.isCancelable()); }
	}
	
	@Override
	public void renderBlockOverlayEvent(RenderBlockOverlayEvent e) {
		if (isEnabled()) {
			if (e.blockForOverlay != null) {
				switch (WorldHelper.getBlockID(e.blockForOverlay.getBlock())) {
				case 9: if (removeWaterOverlay.get()) { e.setCanceled(true); } break; //water overlay
				case 51: if (removeFire.get()) { e.setCanceled(true); } break; //fire
				default: break;
				}
			}
		}
	}
	
	@Override
	public void worldLoadClientEvent(WorldEvent.Load e) {
		if (isEnabled()) {
			configManager.loadMainConfig();
		}
	}
	
	public float getCurrentMCGama() { return mc.gameSettings.gammaSetting; }
	public boolean allowClearFluids() { return allowClearFluids; }
	
	public ClearVisualsApp setGama(float val) { gama.set(val); if (isEnabled()) { mc.gameSettings.gammaSetting = val; } return this; }
	
}
