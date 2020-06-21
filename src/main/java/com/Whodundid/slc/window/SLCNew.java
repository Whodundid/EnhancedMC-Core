package com.Whodundid.slc.window;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.playerUtil.DummyPlayer;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowLabel;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowRect;
import com.Whodundid.core.windowLibrary.windowObjects.utilityObjects.PlayerViewer;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.slc.SLCApp;
import com.Whodundid.slc.util.LayerTypes;
import com.Whodundid.slc.util.SLCResources;
import net.minecraft.util.ResourceLocation;

public class SLCNew extends WindowParent {
	
	public SLCApp mod = (SLCApp) RegisteredApps.getApp(AppType.SLC);
	ResourceLocation playerSkin;
	ResourceLocation playerCape;
	PlayerViewer viewer;
	DummyPlayer player;
	WindowButton play, stop;
	WindowButton globalSetting;
	WindowButton optionsBtn, gStateBtn, gUpBtn, gDownBtn, gFlipBtn;
	WindowButton prof1, prof2, prof3, prof4;
	WindowButton resetProfileBtn, loadProfileBtn;
	private SLCNew instance;
	
	@Override
	public void initWindow() {
		instance = this;
		setObjectName("SLC Advanced Options");
		setDimensions(400, 254);
		playerSkin = mc.thePlayer.getLocationSkin();
		playerCape = mc.thePlayer.getLocationCape();
		windowIcon = SLCResources.icon;
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		//player = new DummyPlayer(playerSkin, playerCape, mc.thePlayer.getSkinType().equals("slim"), null, "dummy", true);
		viewer = new PlayerViewer(this, startX + 10, startY + 10, 140, height - 20, mc.thePlayer);
		
		play = new WindowButton(this, viewer.endX + 10, endY - 30, 60, 20, "Play");
		stop = new WindowButton(this, play.endX + 10, endY - 30, 60, 20, "Stop");
		globalSetting = new WindowButton(this, stop.endX + 10, endY - 30, 90, 20, mod.currentMode.getModeType());
		
		EArrayList<LayerTypes> types = EArrayList.of(LayerTypes.H, LayerTypes.J, LayerTypes.LA, LayerTypes.RA, LayerTypes.LL, LayerTypes.RL, LayerTypes.CA);
		
		int lastY = startY;
		for (int i = 0; i < types.size(); i++) {
			GuiLayerContainer c = new GuiLayerContainer(types.get(i), lastY);
			lastY = c.getY();
		}
		
		addObject(viewer, play, stop, globalSetting);
	}
	
	private void buildParts() {
		
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
	}
	
	private class GuiLayerContainer {
		protected LayerTypes type;
		public WindowButton valUp, valDown, flip, state, running;
		public WindowLabel displayLabel, speedLabel, blinkDurLabel, offsetLabel;
		private int endY;
		
		public GuiLayerContainer(LayerTypes typeIn, int yPos) {
			type = typeIn;
			build(yPos);
		}
		
		private void build(int yPos) {
			WindowRect blackBack = new WindowRect(instance, viewer.endX + 10, yPos, endX, yPos + 31, EColors.black);
			WindowRect greyInner = new WindowRect(instance, viewer.endX + 11, yPos + 1, endX - 1, yPos + 30, EColors.steel);
			
			String name = type.getMCType().getPartName();
			name = EUtil.capitalFirst(name.replace("sleeve", " Arm").replace("pants_leg", " Leg").replace("_", ""));
			
			displayLabel = new WindowLabel(instance, greyInner.startX + 6, greyInner.midY - 3, name, EColors.cyan);
			
			addObject(blackBack, greyInner);
			addObject(displayLabel);
			
			endY = yPos + 30;
		}
		
		public int getY() { return endY; }
		public LayerTypes getType() { return type; }
	}
	
	@Override public boolean showInLists() { return false; }
	
}
