package com.Whodundid.guiCreator.gui.creatorParts.colorPicker;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.colorPicker.EGuiColorPickerSimple;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.types.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.guiCreator.gui.CreatorGui;

public class CreatorColorPicker extends EnhancedGuiObject {
	
	CreatorGui parentCreator;
	ColorButton mainButton, secondButton;
	EGuiButton swapButton;
	EGuiLabel displayLabel;
	int mainColor = 0xff000000;
	int secondColor = 0xffffffff;
	
	public CreatorColorPicker(CreatorGui guiIn) {
		EDimension pdim = guiIn.getObjProperties().getDimensions();
		EDimension cdim = guiIn.getDimensions();
		
		//position beneath object properties
		int sX = pdim.startX;
		int sY = pdim.endY + 2 + 14;
		int w = pdim.width;
		int h = cdim.endY - pdim.endY - 1 - 2 - 2 - 14;
		
		init(guiIn, sX, sY, w, h);
		
		parentCreator = guiIn;
	}
	
	@Override
	public void initObjects() {
		mainButton = new ColorButton(this, startX + 25, startY + 25, 35, 35, mainColor);
		secondButton = new ColorButton(this, startX + 10, startY + 10, 35, 35, secondColor);
		
		//add second before main so that second is behind
		addObject(secondButton, mainButton);
		
		swapButton = new EGuiButton(this, secondButton.endX + 3, mainButton.startY - 3 - 12, 12, 12);
		
		addObject(swapButton);
		
		displayLabel = new EGuiLabel(this, startX + 10, startY + 70, "Hex value: " + String.format("%x", mainColor));
		displayLabel.setDisplayStringColor(EColors.cyan);
		
		addObject(displayLabel);
		
		EGuiHeader head = new EGuiHeader(this, false, 14, "Colors");
		head.setMoveable(false);
		head.setParentFocusDrawn(false);
		head.setTitleColor(EColors.lime.c());
		addObject(head);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(startX, startY, endX, endY, 0xff000000); //back background
		drawRect(startX + 1, startY + 1, endX - 1, endY - 1, 0xff666666); //inner
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		//display a color picker gui if main color is pressed
		if (object == mainButton) {
			EnhancedMC.displayWindow(new EGuiColorPickerSimple(this));
		}
		if (object == swapButton) {
			swapColors();
		}
		if (object instanceof EGuiColorPickerSimple && args.length > 0) {
			if (args[0] instanceof Integer) {
				this.setMainColor((int) args[0]);
			}
		}
	}
	
	//--------------------------
	//CreatorColorPicker Methods
	//--------------------------	
	
	public void swapColors() {
		int temp = mainColor;
		
		mainColor = secondColor;
		secondColor = temp;
		
		update();
	}
	
	public void update() {
		mainButton.setBackgroundColor(mainColor);
		secondButton.setBackgroundColor(secondColor);
		displayLabel.setDisplayString("Hex value: " + String.format("%x", mainColor));
	}
	
	//--------------------------
	//CreatorColorPicker Getters
	//--------------------------
	
	public CreatorGui getParentCreator() { return parentCreator; }
	public int getMainColor() { return mainColor; }
	public int getSecondColor() { return secondColor; }
	
	//--------------------------
	//CreatorColorPicker Setters
	//--------------------------
	
	public CreatorColorPicker setMainColor(EColors colorIn) { return setMainColor(colorIn.c()); }
	public CreatorColorPicker setSecondColor(EColors colorIn) { return setSecondColor(colorIn.c()); }
	public CreatorColorPicker setMainColor(int colorIn) { mainColor = colorIn; update(); return this; }
	public CreatorColorPicker setSecondColor(int colorIn) { secondColor = colorIn; update(); return this; }
	
	//---------------
	//Private classes
	//---------------
	
	private class ColorButton extends EGuiButton {
		
		public ColorButton(CreatorColorPicker parent, int x, int y, int w, int h, int color) {
			super(parent, x, y, w, h);
			
			setDrawTextures(false);
			setDrawBackground(true);
			setBackgroundColor(color);
		}
		
		@Override
		public void drawObject(int mXIn, int mYIn) {
			super.drawObject(mXIn, mYIn);
			
			//draw border
			drawHRect(startX, startY, endX, endY, 1, EColors.black);
		}
	}
}
