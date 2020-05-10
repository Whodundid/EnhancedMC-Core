package com.Whodundid.slc.gui;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.guiObjects.utilityObjects.EGuiPlayerViewer;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.slc.SLCApp;
import com.Whodundid.slc.util.LayerTypes;

import net.minecraft.util.ResourceLocation;

public class SLCNew extends WindowParent {
	
	public SLCApp mod = (SLCApp) RegisteredApps.getApp(AppType.SLC);
	ResourceLocation playerSkin;
	ResourceLocation playerCape;
	EGuiPlayerViewer viewer;
	EGuiButton play, stop;
	EGuiButton optionsBtn, gStateBtn, gUpBtn, gDownBtn, gFlipBtn;
	EGuiButton prof1, prof2, prof3, prof4;
	EGuiButton resetProfileBtn, loadProfileBtn;
	
	@Override
	public void initGui() {
		setObjectName("SLC Advanced Options");
		setDimensions(400, 254);
		//this.setResizeable(true);
		playerSkin = mc.thePlayer.getLocationSkin();
		playerCape = mc.thePlayer.getLocationCape();
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		viewer = new EGuiPlayerViewer(this, startX, startY, 140, height - 24, mc.thePlayer);
		
		addObject(viewer);
	}
	
	private void buildParts() {
		
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
	}
	
	private class GuiLayerContainer {
		protected LayerTypes type;
		public EGuiButton valUp, valDown, flip, state, running;
		public EGuiLabel displayLabel, speedLabel, blinkDurLabel, offsetLabel;
		
		public GuiLayerContainer(LayerTypes typeIn) {
			type = typeIn;
		}
		
		public LayerTypes getType() { return type; }
	}
}
