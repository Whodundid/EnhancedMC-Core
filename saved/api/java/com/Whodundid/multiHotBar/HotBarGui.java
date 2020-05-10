package com.Whodundid.multiHotBar;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;

//Last edited: Nov 2, 2018
//First Added: Nov 2, 2018
//Author: Hunter Bragg

public class HotBarGui extends WindowParent {

	EGuiButton layerVal, drawLayered;
	MultiHotbarApp mod = (MultiHotbarApp) RegisteredApps.getApp(AppType.MULTIHOTBAR);
	
	public HotBarGui() { super(); }
	public HotBarGui(Object oldGuiIn) { super(oldGuiIn); }
	public HotBarGui(IEnhancedGuiObject parentIn) { super(parentIn); }
	public HotBarGui(IEnhancedGuiObject parentIn, Object oldGuiIn) { super(parentIn, oldGuiIn); }
	public HotBarGui(IEnhancedGuiObject parentIn, int posX, int posY) { super(parentIn, posX, posY); }
	public HotBarGui(IEnhancedGuiObject parentIn, int posX, int posY, Object oldGuiIn) { super(parentIn, posX, posY, oldGuiIn); }
	
	@Override
	public void initGui() {
		setObjectName("Multi Hotbar Settings");
		defaultPos();
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		layerVal = new EGuiButton(this, midX + 15, midY - 106, 75, 20, "" + mod.numberOfLayers);
		drawLayered = new EGuiButton(this, midX + 15, midY - 81, 75, 20, (mod.layered) ? "Rows" : "One Long Row");
		addObject(layerVal, drawLayered);
	}
	
	@Override
	public void drawObject(int mX, int mY) {
		drawDefaultBackground();
		drawString("Number of hotbars:", midX - 90, midY - 100, 0xffffff);
		drawString("HotBar mode:", midX - 90, midY - 75, 0xffffff);
		super.drawObject(mX, mY);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == layerVal) {
			if (mod.numberOfLayers == 4) { mod.numberOfLayers = 1; }
			else { mod.numberOfLayers += 1; }
			mod.slotSet = false;
			layerVal.setDisplayString("" + mod.numberOfLayers);
		}
		if (object == drawLayered) {
			mod.layered = !mod.layered;
			mod.slotSet = false;
			drawLayered.setDisplayString((mod.layered) ? "Rows" : "One Long Row");
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == 13) { mod.debugMode = !mod.debugMode; }
		super.keyPressed(typedChar, keyCode);
	}
}
