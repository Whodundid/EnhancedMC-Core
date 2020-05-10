package com.Whodundid.clearVisuals;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.util.worldUtil.WorldHelper;
import net.minecraft.block.material.Material;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;

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
	public float modGama = 0.0f;
	protected boolean removeFire = true;
	protected boolean removeWaterOverlay = true;
	protected boolean removeFog = true;
	protected boolean clearLava = true;
	protected boolean clearWater = true;
	private static ClearVisualsApp instance;
	
	public ClearVisualsApp() {
		super(AppType.CLEARVISUALS);
		instance = this;
		shouldLoad = false;
		version = VERSION;
		author = "Whodundid";
		versionDate = "Jul 10, 2019";
		addDependency(AppType.CORE, "1.0");
		configManager.setMainConfig(new ClearVisualsConfig(this, "clearVisuals"));
		setMainGui(new ClearVisualsGui());
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
	public void renderFogDensityEvent(EntityViewRenderEvent.FogDensity e) {
		if (isEnabled()) {
			if (e.block.getMaterial() == Material.lava && clearLava) { e.density = 0; e.setCanceled(true); }
			if (e.block.getMaterial() == Material.water && clearWater) { e.density = 0; e.setCanceled(true); }
			if (e.block.getMaterial() == Material.air && removeFog) { e.density = 0; e.setCanceled(true); }
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
				case 9: if (removeWaterOverlay) { e.setCanceled(true); } break; //water overlay
				case 51: if (removeFire) { e.setCanceled(true); } break; //fire
				default: break;
				}
			}
		}
	}
	
	@Override
	public void worldLoadClientEvent(WorldEvent.Load e) {
		configManager.loadMainConfig();
	}
	
	public ClearVisualsApp setFireVisibility(boolean val) { removeFire = val; return this; }
	public ClearVisualsApp setUnderWaterOverlayVisibility(boolean val) { removeWaterOverlay = val; return this; }
	public ClearVisualsApp setFogVisibility(boolean val) { removeFog = !val; return this; }
	public ClearVisualsApp setClearLava(boolean val) { clearLava = val; return this; }
	public ClearVisualsApp setClearWater(boolean val) { clearWater = val; return this; }
	public ClearVisualsApp setGama(float val) { modGama = val; if (isEnabled()) { mc.gameSettings.gammaSetting = val; } return this; }
	public float getCurrentMCGama() { return mc.gameSettings.gammaSetting; }
	public float getModGama() { return modGama; }
	public boolean isFireDrawn() { return removeFire; }
	public boolean isWaterOverlayDrawn() { return removeWaterOverlay; }
	public boolean isFogDrawn() { return removeFog; }
	public boolean isClearLava() { return clearLava; }
	public boolean isClearWater() { return clearWater; }
}
