package com.Whodundid.windowHUD;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppConfigSetting;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.windowHUD.windowObjects.hotbar.HotBarRCM;
import com.Whodundid.windowHUD.windowObjects.hotbar.HotBarRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = WindowHUDApp.MODID, version = WindowHUDApp.VERSION, name = WindowHUDApp.NAME, dependencies = "required-after:enhancedmc")
public class WindowHUDApp extends EMCApp {
	
	public static final String MODID = "windowhud";
	public static final String VERSION = "0.1";
	public static final String NAME = "Window HUD";
	
	public static final AppConfigSetting<Boolean> windowHotbar = new AppConfigSetting(Boolean.class, "windowHotBar", "Enable Window Hotbar", false);
	public static final AppConfigSetting<Boolean> verticalHotbar = new AppConfigSetting(Boolean.class, "verticalHotBar", "Use Vertical Hotbar", false);
	public static final AppConfigSetting<Boolean> windowScoreBoard = new AppConfigSetting(Boolean.class, "windowScoreboard", "Enable Window Scoreboard", false);
	public static final AppConfigSetting<Boolean> windowTabMenu = new AppConfigSetting(Boolean.class, "windowTabMenu", "Enable Window Tab Menu", false);
	public static final AppConfigSetting<Boolean> windowHealth = new AppConfigSetting(Boolean.class, "windowHealth", "Enable Window Health", false);
	
	private static HotBarRenderer hotbar = null;
	
	public WindowHUDApp() {
		super(AppType.WINDOWHUD);
		addDependency(AppType.CORE, "1.0");
		shouldLoad = false;
		setAliases("windowhud", "hudwindows", "hud");
	}
	
	@Override
	public void worldLoadClientEvent(WorldEvent.Load e) {
		if (isEnabled()) {
			EnhancedMCRenderer ren = EnhancedMC.getRenderer();
			if (ren.getObjects().contains(hotbar) || ren.getAddingObjects().contains(hotbar)) {
				EnhancedMC.getRenderer().removeObject(hotbar);
			} else {
				EnhancedMC.getRenderer().addObject(hotbar = new HotBarRenderer());
			}
		}
	}
	
	@Override
	public void overlayPreEvent(RenderGameOverlayEvent.Pre e) {
		if (isEnabled()) {
			if (e.type == ElementType.HOTBAR) { e.setCanceled(true); }
		}
	}
	
	@Override
	public EMCApp setEnabled(boolean val) {
		EnhancedMCRenderer ren = EnhancedMC.getRenderer();
		if (!val) {
			ren.getObjects().removeAllInstancesOf(HotBarRenderer.class);
			ren.getObjects().removeAllInstancesOf(HotBarRCM.class);
			ren.getAddingObjects().removeAllInstancesOf(HotBarRenderer.class);
			ren.getAddingObjects().removeAllInstancesOf(HotBarRCM.class);
			
			hotbar = null;
			
		} else if (val) {
			if (hotbar == null) {
				ren.addObject(hotbar = new HotBarRenderer());
			}
			else {
				ren.getObjects().removeAllInstancesOf(HotBarRenderer.class);
				ren.getObjects().removeAllInstancesOf(HotBarRCM.class);
				ren.getAddingObjects().removeAllInstancesOf(HotBarRenderer.class);
				ren.getAddingObjects().removeAllInstancesOf(HotBarRCM.class);
				ren.addObject(hotbar = new HotBarRenderer());
			}
		}
		super.setEnabled(val);
		return this;
	}
}
