package com.Whodundid.clearVisuals.window;

import com.Whodundid.clearVisuals.ClearVisualsApp;
import com.Whodundid.clearVisuals.util.CVResources;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowSlider;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowTextField;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.header.WindowHeader;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowContainer;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import net.minecraft.util.MathHelper;

public class ClearVisualsWindow extends WindowParent {
	
	protected ClearVisualsApp mod = (ClearVisualsApp) RegisteredApps.getApp(AppType.CLEARVISUALS);
	protected WindowSlider gamaSlider;
	protected WindowTextField gamaEntry;
	protected WindowButton removeFire, removeWaterOverlay, removeFog, clearLava, clearWater, resetGama;
	protected WindowContainer gama, rendering;
	
	public ClearVisualsWindow() {
		super();
		aliases.add("clearvisualsgui", "cvgui");
	}
	
	@Override
	public void initWindow() {
		setObjectName("Clear Visuals Settings");
		defaultDims();
		windowIcon = CVResources.icon;
		
		setResizeable(true);
		setMinDims(defaultWidth, 100);
		setMaximizable(true);
	}
	
	@Override
	public void initObjects() {
		setHeader(new WindowHeader(this));
		
		int renH = MathHelper.clamp_int(height / 2, 134, 10000);
		int gamH = endY - (startY + 2 + renH) - 2;
		
		rendering = new WindowContainer(this, startX + 2, startY + 2, width - 4, renH) {
			@Override
			public void drawObject(int mXIn, int mYIn) {
				super.drawObject(mXIn, mYIn);
				drawStringS("Remove Fire", removeFire.endX + 10, removeFire.startY + 6, EColors.lgray);
				drawStringS("Remove Water Overlay", removeWaterOverlay.endX + 10, removeWaterOverlay.startY + 6, EColors.lgray);
				drawStringS("Remove Sky Overlay", removeFog.endX + 10, removeFog.startY + 6, EColors.lgray);
				drawStringS("Clear Lava", clearLava.endX + 10, clearLava.startY + 6, EColors.lgray);
				drawStringS("Clear Water", clearWater.endX + 10, clearWater.startY + 6, EColors.lgray);
			}
		};
		
		gama = new WindowContainer(this, startX + 2, rendering.endY + 1, width - 4, ((endY - 2) - (rendering.getDimensions().endY + 1))) {
			@Override
			public void drawObject(int mXIn, int mYIn) {
				super.drawObject(mXIn, mYIn);
				drawStringS("Custom Gama Entry", gamaEntry.endX + 11, startY + 52, EColors.lgray);
				if (mod.getCurrentMCGama() < 0) {
					drawStringCS("Note: Negative gama values can", gama.midX, resetGama.endY + 5, EColors.lorange);
					drawStringCS("produce rather odd lighting effects.", gama.midX, resetGama.endY + 15, EColors.lorange);
				}
				else if (mod.getCurrentMCGama() <= 0.009) {
					if (mod.getCurrentMCGama() >= 0) { drawStringCS("Minecraft equivalent: Moody", gama.midX, resetGama.endY + 10, EColors.lime); }
				}
				else if (mod.getCurrentMCGama() > 0.009) {
					if (mod.getCurrentMCGama() <= 1.009 && mod.getCurrentMCGama() >= 1) {
						drawStringCS("Minecraft equivalent: Bright", gama.midX, resetGama.endY + 10, EColors.lime);
					}
					else {
						String percentValue = gamaSlider.displayValue;
						if (percentValue.startsWith("0")) { percentValue = percentValue.substring(2); }
						else { percentValue = percentValue.replace(".", ""); }
						drawStringCS("Minecraft equivalent: Brightnes +" + percentValue + "%", gama.midX, resetGama.endY + 10, EColors.lime);
					}
				}
			}
		};
		
		rendering.setTitle("Rendering");
		rendering.setTitleColor(EColors.orange.intVal);
		rendering.setTitleBackgroundColor(0xff191919);
		rendering.setTitleCentered(true);
		rendering.setBackgroundColor(EColors.pdgray.intVal);
		
		removeFire = new WindowButton(rendering, rendering.midX - 100, rendering.startY + 24, 60, 18, ClearVisualsApp.removeFire);
		removeWaterOverlay = new WindowButton(rendering, rendering.midX - 100, rendering.startY + 45, 60, 18, ClearVisualsApp.removeWaterOverlay);
		removeFog = new WindowButton(rendering, rendering.midX - 100, rendering.startY + 66, 60, 18, ClearVisualsApp.removeFog);
		clearLava = new WindowButton(rendering, rendering.midX - 100, rendering.startY + 87, 60, 18, ClearVisualsApp.clearLava);
		clearWater = new WindowButton(rendering, rendering.midX - 100, rendering.startY + 109, 60, 18, ClearVisualsApp.clearWater);
		
		if (!mod.allowClearFluids()) {
			clearLava.setEnabled(false);
			clearWater.setEnabled(false);
			
			clearLava.setString("Disabled").setStringColor(EColors.lred);
			clearWater.setString("Disabled").setStringColor(EColors.lred);
			
			clearLava.setHoverText("Setting cannot be enabled when in a Hypixel PvP gamemode!");
			clearWater.setHoverText("Setting cannot be enabled when in a Hypixel PvP gamemode!");
		}
		
		gama.setTitle("Gama Control");
		gama.setTitleColor(EColors.orange.intVal);
		gama.setTitleBackgroundColor(0xff191919);
		gama.setTitleCentered(true);
		gama.setBackgroundColor(0xff222222);
		
		gamaSlider = new WindowSlider(gama, gama.startX + 10, gama.startY + 23, gama.width - 20, 18, -10, 20, mod.getCurrentMCGama(), false) {
			@Override
			public void mouseReleased(int mXIn, int mYIn, int button) {
				super.mouseReleased(mXIn, mYIn, button);
				mod.getConfig().saveAllConfigs();
			}
		};
		
		gamaSlider.setDisplayValueColor(EColors.green.intVal);
		
		gamaEntry = new WindowTextField(gama, gama.midX - 96, gama.startY + 47, 55, 17);
		resetGama = new WindowButton(gama, gamaEntry.startX - 1, gama.startY + 70, gama.width - 20, 18, "Reset");
		
		resetGama.setStringColor(EColors.yellow);
		
		gamaEntry.setText("" + gamaSlider.displayValue);
		gamaEntry.setTextColor(EColors.green.intVal);
		
		IActionObject.setActionReceiver(this, removeFire, removeWaterOverlay, removeFog, clearLava, clearWater, gamaSlider, gamaEntry, resetGama);
		
		rendering.addObject(removeFire, removeWaterOverlay, removeFog, clearLava, clearWater);
		gama.addObject(gamaSlider, resetGama, gamaEntry);
		
		addObject(rendering, gama);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == removeFire) { removeFire.toggleTrueFalse(ClearVisualsApp.removeFire, mod, true); }
		if (object == removeWaterOverlay) { removeFire.toggleTrueFalse(ClearVisualsApp.removeWaterOverlay, mod, true); }
		if (object == removeFog) { removeFire.toggleTrueFalse(ClearVisualsApp.removeFog, mod, true); }
		if (object == clearLava) { removeFire.toggleTrueFalse(ClearVisualsApp.clearLava, mod, true); }
		if (object == clearWater) { removeFire.toggleTrueFalse(ClearVisualsApp.clearWater, mod, true); }
		
		if (object == gamaEntry) {
			try {
				if (!gamaEntry.getText().isEmpty()) {
					mod.setGama(Float.parseFloat(gamaEntry.getText()));
					gamaSlider.setSliderValue(mod.getCurrentMCGama());
				}
			}
			catch (Exception e) {
				mod.setGama(0.0f);
				gamaSlider.setSliderValue(0.0f);
			}
			mod.getConfig().saveAllConfigs();
		}
		
		if (object == resetGama) {
			mod.setGama(mod.vanillaGama);
			gamaSlider.setSliderValue(mod.vanillaGama);
			mod.getConfig().saveAllConfigs();
		}
		
		if (object == gamaSlider) {
			mod.setGama(gamaSlider.getSliderValue());
			gamaEntry.setText("" + gamaSlider.displayValue);
			gamaEntry.setTextColor(EColors.green.intVal);
		}
	}
	
	@Override
	public void sendArgs(Object... args) {
		if (args.length > 0) {
			if (args[0] instanceof String) {
				String val = (String) args[0];
				if (val.equals("Reload")) { reInitObjects(); }
			}
		}
	}
	
}
