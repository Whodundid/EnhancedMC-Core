package com.Whodundid.core.enhancedGui.guiObjects;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.types.EnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.InnerEnhancedGui;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.subMod.IUseScreenLocation;
import com.Whodundid.core.util.renderUtil.ScreenLocation;

//Last edited: Jan 2, 2019
//First Added: Dec 14, 2018
//Author: Hunter Bragg

public class EGuiScreenLocationSelector extends EnhancedActionObject {
	
	protected IUseScreenLocation obj;
	protected EGuiButton bLeft, bRight, tLeft, tRight, center, custom, chatDraw;
	protected int heightRatio = 0, widthRatio = 0;
	public String drawName = "";
	
	public EGuiScreenLocationSelector(IEnhancedGuiObject parentIn, IUseScreenLocation objIn, int posX, int posY, int size) {
		init(parentIn, posX, posY, size, size - 10);
		obj = objIn;
		
		heightRatio = (int) (height * 0.75);
		widthRatio = (int) (width * 0.5);
	}
	
	@Override
	public void initObjects() {
		bLeft = (EGuiButton) new EGuiButton(this, startX + 4, startY + heightRatio - 19, 23, 15, "BL").setSelectedObject(ScreenLocation.botLeft);
		bRight = (EGuiButton) new EGuiButton(this, endX - 27, startY + heightRatio - 19, 23, 15, "BR").setSelectedObject(ScreenLocation.botRight);
		tLeft = (EGuiButton) new EGuiButton(this, startX + 4, startY + 4, 23, 15, "TL").setSelectedObject(ScreenLocation.topLeft);
		tRight = (EGuiButton) new EGuiButton(this, endX - 27, startY + 4, 23, 15, "TR").setSelectedObject(ScreenLocation.topRight);
		center = (EGuiButton) new EGuiButton(this, startX + width / 2 - 11, startY + (heightRatio / 2) - 7, 23, 15, "C").setSelectedObject(ScreenLocation.center);
		custom = (EGuiButton) new EGuiButton(this, startX + width / 2 - (95 / 2), endY, 95, 16, "Custom location").setSelectedObject(ScreenLocation.custom);
		
		addObject(bLeft, bRight, tLeft, tRight, center, custom);
	}
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		drawRect(startX, startY, endX, startY + heightRatio, -0x00ffffff);
		drawRect(startX + widthRatio - (widthRatio / 16), startY + heightRatio, startX + widthRatio + (widthRatio / 16), endY - (heightRatio / 8), -0x00ffffff);
		drawRect(startX + widthRatio - (widthRatio / 2), endY - (heightRatio / 8), startX + widthRatio + (widthRatio / 2), endY - (heightRatio / 8) + 3, -0x00ffffff);
		drawRect(startX + 3, startY + 3, endX - 3, startY + heightRatio - 3, 0xffC9FFFF);
		
		drawStringWithShadow("Select a location to draw " + drawName + ".", midX - fontRenderer.getStringWidth("Select a location to draw " + drawName + ".") / 2, startY - heightRatio / 5 - 12, 0xb2b2b2);
		String msg = "";
		switch (obj.getScreenLocation()) {
		case botLeft: msg = "Bottom Left"; break;
		case botRight: msg = "Bottom Right"; break;
		case topLeft: msg = "Top Left"; break;
		case topRight: msg = "Top Right"; break;
		case center: msg = "Center"; break;
		case custom: msg = "Custom (" + obj.getLocation().getObject() + ", " + obj.getLocation().getValue() + ")"; break;
		default: msg = "Center"; break;
		}
		drawStringWithShadow("Current Location: ", midX - fontRenderer.getStringWidth("Current Location: " + msg) / 2, startY - heightRatio / 5, 0xffd800);
		drawStringWithShadow(msg, midX - fontRenderer.getStringWidth("Current Location: " + msg) / 2 + fontRenderer.getStringWidth("Current Location: "), startY - heightRatio / 5, 0x00ff00);
		
		super.drawObject(mX, mY, ticks);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object.equals(bLeft)) { obj.setLocation(ScreenLocation.botLeft); }
		if (object.equals(bRight)) { obj.setLocation(ScreenLocation.botRight); }
		if (object.equals(tLeft)) { obj.setLocation(ScreenLocation.topLeft); }
		if (object.equals(tRight)) { obj.setLocation(ScreenLocation.topRight); }
		if (object.equals(center)) { obj.setLocation(ScreenLocation.center); }
		if (object.equals(custom)) {
			InnerEnhancedGui newGui = obj.getScreenLocationGui();
			//Stack<InnerEnhancedGui> history = getTopParent().getGuiHistory();
			//if (mc.currentScreen instanceof EnhancedGui) { history.push((EnhancedGui) mc.currentScreen); }
			//if (history != null) { newGui.sendGuiHistory(history); }
			//mc.displayGuiScreen(newGui);
			EnhancedMC.displayEGui(newGui, this);
		}
		if (actionReciever != null) { actionReciever.actionPerformed(this); }
	}
	
	public EGuiScreenLocationSelector setDisplayName(String nameIn) { drawName = nameIn; return this; }

	//-------------------------------
	//IEnhancedActionObject overrides
	//-------------------------------
			
	//actions
	@Override public boolean runActionOnPress() { return runActionOnPress; }
	@Override public EGuiScreenLocationSelector setRunActionOnPress(boolean value) { runActionOnPress = value; return this; }
	@Override public void performAction() {}
			
	//objects
	@Override public EGuiScreenLocationSelector setSelectedObject(Object objIn) { storedObject = objIn; return this; }
	@Override public Object getSelectedObject() { return storedObject; }
}
