package com.Whodundid.core.windowLibrary.windowObjects.utilityObjects;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.IUseScreenLocation;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowTypes.ActionObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;

//Author: Hunter Bragg

public class ScreenLocationSelector extends ActionObject {
	
	protected IUseScreenLocation obj;
	protected WindowButton bLeft, bRight, tLeft, tRight, center, custom, chatDraw;
	protected int heightRatio = 0, widthRatio = 0;
	public String drawName = "";
	
	public ScreenLocationSelector(IWindowObject parentIn, IUseScreenLocation objIn, int posX, int posY, int size) {
		init(parentIn, posX, posY, size, size - 10);
		obj = objIn;
		
		heightRatio = (int) (height * 0.75);
		widthRatio = (int) (width * 0.5);
	}
	
	@Override
	public void initObjects() {
		bLeft = (WindowButton) new WindowButton(this, startX + 4, startY + heightRatio - 19, 23, 15, "BL").setSelectedObject(ScreenLocation.botLeft);
		bRight = (WindowButton) new WindowButton(this, endX - 27, startY + heightRatio - 19, 23, 15, "BR").setSelectedObject(ScreenLocation.botRight);
		tLeft = (WindowButton) new WindowButton(this, startX + 4, startY + 4, 23, 15, "TL").setSelectedObject(ScreenLocation.topLeft);
		tRight = (WindowButton) new WindowButton(this, endX - 27, startY + 4, 23, 15, "TR").setSelectedObject(ScreenLocation.topRight);
		center = (WindowButton) new WindowButton(this, startX + width / 2 - 11, startY + (heightRatio / 2) - 7, 23, 15, "C").setSelectedObject(ScreenLocation.center);
		custom = (WindowButton) new WindowButton(this, startX + width / 2 - (95 / 2), endY, 95, 16, "Custom location").setSelectedObject(ScreenLocation.custom);
		
		addObject(null, bLeft, bRight, tLeft, tRight, center, custom);
	}
	
	@Override
	public void drawObject(int mX, int mY) {
		drawRect(startX, startY, endX, startY + heightRatio, EColors.black); //border
		drawRect(startX + widthRatio - (widthRatio / 16), startY + heightRatio, startX + widthRatio + (widthRatio / 16), endY - (heightRatio / 8), EColors.black); //pole
		drawRect(startX + widthRatio - (widthRatio / 2), endY - (heightRatio / 8), startX + widthRatio + (widthRatio / 2), endY - (heightRatio / 8) + 3, EColors.black); //base
		drawRect(startX + 3, startY + 3, endX - 3, startY + heightRatio - 3, 0xffC9FFFF); //screen
		
		drawStringS("Select a location to draw " + drawName + ".", midX - mc.fontRendererObj.getStringWidth("Select a location to draw " + drawName + ".") / 2, startY - heightRatio / 5 - 12, 0xb2b2b2);
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
		
		drawStringS("Current Location: ", midX - mc.fontRendererObj.getStringWidth("Current Location: " + msg) / 2, startY - heightRatio / 5, 0xffd800);
		drawStringS(msg, midX - mc.fontRendererObj.getStringWidth("Current Location: " + msg) / 2 + mc.fontRendererObj.getStringWidth("Current Location: "), startY - heightRatio / 5, 0x00ff00);
		
		super.drawObject(mX, mY);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == bLeft) { obj.setLocation(ScreenLocation.botLeft); }
		if (object == bRight) { obj.setLocation(ScreenLocation.botRight); }
		if (object == tLeft) { obj.setLocation(ScreenLocation.topLeft); }
		if (object == tRight) { obj.setLocation(ScreenLocation.topRight); }
		if (object == center) { obj.setLocation(ScreenLocation.center); }
		if (object == custom) { EnhancedMC.displayWindow(obj.getScreenLocationGui(), this); }
		performAction();
	}
	
	public ScreenLocationSelector setDisplayName(String nameIn) { drawName = nameIn; return this; }
	
}
