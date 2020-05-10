package com.Whodundid.clearVisuals;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiSlider;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiTextField;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiContainer;
import com.Whodundid.core.enhancedGui.objectEvents.EventMouse;
import com.Whodundid.core.enhancedGui.objectEvents.ObjectEvent;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.MouseType;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;

public class ClearVisualsGui extends WindowParent {
	
	protected ClearVisualsApp mod = (ClearVisualsApp) RegisteredApps.getApp(AppType.CLEARVISUALS);
	protected EGuiSlider gamaSlider;
	protected EGuiTextField gamaEntry;
	protected EGuiButton drawFire, drawWaterOverlay, drawFog, clearLava, clearWater, resetGama;
	protected EGuiContainer gama, rendering;
	
	public ClearVisualsGui() {
		super();
		aliases.add("clearvisualsgui", "cvgui");
	}
	
	@Override
	public void initGui() {
		setObjectName("ClearVisuals Settings");
		centerObjectWithSize(defaultWidth, defaultHeight);
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		setHeader(new EGuiHeader(this));
		
		rendering = new EGuiContainer(this, startX + 2, startY + 2, width - 4, 134) {
			@Override
			public void drawObject(int mXIn, int mYIn) {
				super.drawObject(mXIn, mYIn);
				drawStringWithShadow("Remove Fire", midX - 32, startY + 29, 0xb2b2b2);
				drawStringWithShadow("Remove Water Overlay", midX - 32, startY + 50, 0xb2b2b2);
				drawStringWithShadow("Remove Sky Overlay", midX - 32, startY + 71, 0xb2b2b2);
				drawStringWithShadow("Clear Lava", midX - 32, startY + 92, 0xb2b2b2);
				drawStringWithShadow("Clear Water", midX - 32, startY + 113, 0xb2b2b2);
			}
		};
		
		gama = new EGuiContainer(this, startX + 2, rendering.getDimensions().endY + 1, width - 4, ((guiInstance.endY - 2) - (rendering.getDimensions().endY + 1))) {
			@Override
			public void drawObject(int mXIn, int mYIn) {
				super.drawObject(mXIn, mYIn);
				drawStringWithShadow("Custom Gama Entry", midX - 32, startY + 52, 0xb2b2b2);
				if (mod.getCurrentMCGama() < 0) {
					drawStringWithShadow("Note: Negative gama values can", startX + 12, endY - 23, 0xffd800);
					drawStringWithShadow("produce rather odd lighting effects.", startX + 12, endY - 14, 0xffd800);
				}
				else if (mod.getCurrentMCGama() <= 0.009) {
					if (mod.getCurrentMCGama() >= 0) { drawStringWithShadow("Minecraft equivalent: Moody", startX + 12, endY - 19, 0xffd800); }
				}
				else if (mod.getCurrentMCGama() > 0.009) {
					if (mod.getCurrentMCGama() <= 1.009 && mod.getCurrentMCGama() >= 1) {
						drawStringWithShadow("Minecraft equivalent: Bright", startX + 12, endY - 19, 0xffd800);
					}
					else {
						String percentValue = gamaSlider.displayValue;
						if (percentValue.startsWith("0")) { percentValue = percentValue.substring(2); }
						else { percentValue = percentValue.replace(".", ""); }
						drawStringWithShadow("Minecraft equivalent: Brightnes +" + percentValue + "%", startX + 12, endY - 19, 0xffd800);
					}
				}
			}
		};
		
		rendering.setTitle("Rendering");
		drawFire = new EGuiButton(rendering, rendering.startX + 10, rendering.startY + 24, 55, 18).setTrueFalseButton(true).updateTrueFalseDisplay(mod.isFireDrawn());
		drawWaterOverlay = new EGuiButton(rendering, rendering.startX + 10, rendering.startY + 45, 55, 18).setTrueFalseButton(true).updateTrueFalseDisplay(mod.isWaterOverlayDrawn());
		drawFog = new EGuiButton(rendering, rendering.startX + 10, rendering.startY + 66, 55, 18).setTrueFalseButton(true).updateTrueFalseDisplay(mod.isFogDrawn());
		clearLava = new EGuiButton(rendering, rendering.startX + 10, rendering.startY + 87, 55, 18).setTrueFalseButton(true).updateTrueFalseDisplay(mod.isClearLava());
		clearWater = new EGuiButton(rendering, rendering.startX + 10, rendering.startY + 109, 55, 18).setTrueFalseButton(true).updateTrueFalseDisplay(mod.isClearWater());
		
		gama.setTitle("Gama Control");
		gamaSlider = new EGuiSlider(gama, gama.startX + 10, gama.startY + 23, gama.width - 20, 18, -10, 20, mod.getCurrentMCGama(), false);
		resetGama = new EGuiButton(gama, gama.startX + 10, gama.startY + 70, gama.width - 20, 18, "Reset");
		gamaEntry = new EGuiTextField(gama, gama.startX + 11, gama.startY + 47, 50, 17) {
			{ setText("" + gamaSlider.displayValue); }
			@Override
			public void keyPressed(char typedChar, int keyCode) {
				super.keyPressed(typedChar, keyCode);
				if (keyCode == 28) {
					try {
						if (!gamaEntry.getText().isEmpty()) {
							mod.setGama(Float.parseFloat(gamaEntry.getText()));
							gamaSlider.setSliderValue(mod.getCurrentMCGama());
						}
					} catch (Exception e) {
						mod.setGama(0.0f);
						gamaSlider.setSliderValue(0.0f);
					}
					mod.getConfig().saveAllConfigs();
				}
			}
		};
		gamaSlider.registerListener(guiInstance);
		
		drawFire.setActionReciever(this);
		drawWaterOverlay.setActionReciever(this);
		drawFog.setActionReciever(this);
		clearLava.setActionReciever(this);
		clearWater.setActionReciever(this);
		gamaSlider.setActionReciever(this);
		resetGama.setActionReciever(this);
		
		rendering.addObject(drawFire, drawWaterOverlay, drawFog, clearLava, clearWater);
		gama.addObject(gamaSlider, resetGama, gamaEntry);
		
		addObject(rendering, gama);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object.equals(drawFire)) { 
			mod.setFireVisibility(!mod.isFireDrawn());
			drawFire.updateTrueFalseDisplay(mod.isFireDrawn());
			mod.getConfig().saveAllConfigs();
		}
		if (object.equals(drawWaterOverlay)) { 
			mod.setUnderWaterOverlayVisibility(!mod.isWaterOverlayDrawn());
			drawWaterOverlay.updateTrueFalseDisplay(mod.isWaterOverlayDrawn());
			mod.getConfig().saveAllConfigs();
		}
		if (object.equals(drawFog)) {
			mod.setFogVisibility(mod.isFogDrawn());
			drawFog.updateTrueFalseDisplay(mod.isFogDrawn());
			mod.getConfig().saveAllConfigs();
		}
		if (object.equals(clearLava)) { 
			mod.setClearLava(!mod.isClearLava());
			clearLava.updateTrueFalseDisplay(mod.isClearLava());
			mod.getConfig().saveAllConfigs();
		}
		if (object.equals(clearWater)) {
			mod.setClearWater(!mod.isClearWater());
			clearWater.updateTrueFalseDisplay(mod.isClearWater());
			mod.getConfig().saveAllConfigs();
		}
		if (object.equals(resetGama)) {
			mod.setGama(mod.vanillaGama);
			gamaSlider.setSliderValue(mod.vanillaGama);
			mod.getConfig().saveAllConfigs();
		}
		if (object.equals(gamaSlider)) {
			mod.setGama(gamaSlider.getSliderValue());
			gamaEntry.setText("" + gamaSlider.displayValue);
		}
	}
	
	@Override
	public void onEvent(ObjectEvent e) {
		if (e instanceof EventMouse) {
			if (((EventMouse) e).getMouseType() == MouseType.Released) {
				mod.getConfig().saveAllConfigs();
			}
		}
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
	}
}
