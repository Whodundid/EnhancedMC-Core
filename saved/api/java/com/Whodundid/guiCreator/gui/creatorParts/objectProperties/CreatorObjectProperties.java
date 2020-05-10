package com.Whodundid.guiCreator.gui.creatorParts.objectProperties;

import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.scrollList.EGuiScrollList;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.guiCreator.gui.CreatorGui;

public class CreatorObjectProperties extends EGuiScrollList {
	
	CreatorGui parentCreator;
	IEnhancedGuiObject curObject = null;

	public CreatorObjectProperties(CreatorGui guiIn) {
		EDimension cdim = guiIn.getDimensions();
		EDimension tbdim = guiIn.getToolBelt().getDimensions();
		
		int sX = cdim.endX - (cdim.width / 7) - 3;
		int sY = cdim.startY + 17;
		int w = cdim.endX - sX - 3;
		int h = cdim.height - 2 - cdim.startY - cdim.height / 4;
		
		init(guiIn, sX, sY, w, h);
		parentCreator = guiIn;
	}
	
	@Override
	public void initObjects() {
		super.initObjects();
		
		EGuiHeader head = new EGuiHeader(this, false, 14, "Properties");
		head.setMoveable(false);
		head.setParentFocusDrawn(false);
		head.setTitleColor(EColors.lime.c());
		addObject(head);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		super.actionPerformed(object, args);
	}
	
	//-------------------------------
	//CreatorObjectProperties Methods
	//-------------------------------
	
	
	
	//-------------------------------
	//CreatorObjectProperties Getters
	//-------------------------------
	
	public IEnhancedGuiObject getCurrentObject() { return curObject; }
	
	//-------------------------------
	//CreatorObjectProperties Setters
	//-------------------------------	
	
	public CreatorObjectProperties setCurrentObject(IEnhancedGuiObject object) { curObject = object; return this; }
}
