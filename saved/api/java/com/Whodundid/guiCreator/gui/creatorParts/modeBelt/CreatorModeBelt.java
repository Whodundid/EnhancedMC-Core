package com.Whodundid.guiCreator.gui.creatorParts.modeBelt;

import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.types.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.guiCreator.gui.CreatorGui;
import com.Whodundid.guiCreator.gui.creatorParts.modeBelt.modebeltParts.CreatorModeButton;
import com.Whodundid.guiCreator.util.CreatorMode;

public class CreatorModeBelt extends EnhancedGuiObject {

	CreatorGui parentCreator;
	EArrayList<CreatorModeButton> modes = new EArrayList();
	int modeWidth = 2;
	
	public CreatorModeBelt(CreatorGui guiIn, int xIn, int yIn) { this(guiIn, xIn, yIn, 2); }
	public CreatorModeBelt(CreatorGui guiIn, int xIn, int yIn, int modeWidthIn) {
		init(guiIn, xIn, yIn + 14);
		parentCreator = guiIn;
		modeWidth = modeWidthIn;
	}
	
	//----------------------------
	//IEnhancedGuiObject Overrides
	//----------------------------
	
	@Override
	public void initObjects() {
		CreatorModeButton edit = new CreatorModeButton(this, CreatorMode.EDIT_MODE);
		CreatorModeButton test = new CreatorModeButton(this, CreatorMode.TEST_MODE);
		
		//add each category to the tool list
		modes.add(edit, test);
		
		//position each tool within the list and actually create and apply the dimensions
		buildModes();
		
		EGuiHeader head = new EGuiHeader(this, false, 14, "Modes");
		head.setMoveable(false);
		head.setParentFocusDrawn(false);
		head.setDrawTitleCentered(true);
		head.setTitleColor(EColors.lime.c());
		addObject(head);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(startX, startY, endX, endY, 0xff000000);
		drawRect(startX + 1, startY + 1, endX - 1, endY - 1, 0xff444444);
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object instanceof CreatorModeButton) {
			parentCreator.setCurrentMode(((CreatorModeButton) object).getType());
		}
	}
	
	//-----------------------
	//CreatorToolBelt Methods
	//-----------------------
	
	private void buildModes() {
		int toolSize = 20; //arbitrary tool size
		
		//determine toolbelt dims
		int w = (modeWidth * toolSize) + modeWidth + 3; //add the width back so that there are gaps
		int h = 2; //starting height value
		
		//position each tool
		int curY = startY + 2;
		int rows = (int) (Math.ceil((double) modes.size() / (double) modeWidth));
		
		for (int i = 0; i < modes.size(); i++) {
			CreatorModeButton button = modes.get(i);
			
			int xPos = i % modeWidth; //x offset multiple starting at 0
			int yPos = i / modeWidth; //y offset multiple starting at 0
			
			int bx = startX + 2 + (toolSize * xPos) + xPos;
			int by = curY + (toolSize * yPos) + yPos;
			
			//add to list first then dimension
			addObject(button);
			button.setDimensions(bx, by, toolSize, toolSize);
		}
		
		//update height values
		curY += (rows * toolSize) + rows;
		h += (rows * toolSize) + rows;
		
		//add space to match top
		h += 1;
		
		//assign our dimensions
		setDimensions(w, h);
	}
	
	//-----------------------
	//CreatorToolBelt Getters
	//-----------------------
	
	public EArrayList<CreatorModeButton> getModes() { return modes; }
	public CreatorGui getParentCreator() { return parentCreator; }
}
