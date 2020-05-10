package com.Whodundid.guiCreator.gui.creatorParts.designSpace;

import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.scrollList.EGuiScrollList;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiContainer;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.guiCreator.gui.CreatorGui;
import com.Whodundid.guiCreator.util.CreatorMode;

public class CreatorDesignSpace extends EGuiScrollList {

	CreatorGui parentCreator;
	
	public CreatorDesignSpace(CreatorGui guiIn, int xIn, int yIn) {
		init(guiIn, xIn, yIn + 14);
		parentCreator = guiIn;
		
		setBackgroundColor(0xff999999);
	}
	
	@Override
	public void initObjects() {
		//determine and assign dimensions
		EDimension tbdim = parentCreator.getToolBelt().getDimensions();
		EDimension pdim = parentCreator.getObjProperties().getDimensions();
		EDimension cdim = parentCreator.getDimensions();
		
		int w = pdim.startX - 5 - tbdim.endX - 5;
		int h = cdim.height - 5 - cdim.startY - 3;
		
		setDimensions(w, h);
		
		//create list objects
		super.initObjects();
		
		//add header
		EGuiHeader head = new EGuiHeader(this, false, 14, "Designer");
		head.setMoveable(false);
		head.setParentFocusDrawn(false);
		head.setTitleColor(EColors.lime.c());
		addObject(head);
		
		//set list object properties
		setListSize(res.getScaledWidth(), res.getScaledHeight());
		setResetDrawn(true);
		
		//adding default window
		EGuiContainer defWindow = new EGuiContainer(this, 15, 35, 220, 255);
		defWindow.setResizeable(true);
		defWindow.setObjectName("Unnamed Gui");
		defWindow.setClickable(false);
		addObjectToList(defWindow);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		super.drawObject(mXIn, mYIn);
		IEnhancedGuiObject selObj = parentCreator.getSelectedObject();
		
		//update creating object
		//if (creatingObject != null && clickPos != null) {
		//	creatingObject.setDimensions(mXIn - clickPos.getObject(), mYIn - clickPos.getValue());
		//}
		
		//update object being edited
		/*
		if (resizeObj) {
			if (selObj != null && clickPos != null) {
				selObj.setDimensions(mXIn - clickPos.getObject(), mYIn - clickPos.getValue());
			}
		}
		*/
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		super.actionPerformed(object, args);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		if (parentCreator.getCurrentMode() == CreatorMode.EDIT_MODE) {
			try {
				parentCreator.getHandler().toolPress(mXIn, mYIn, button);
			} catch (Exception e) {}
		}
		else {
			if (drawnListObjects.isNotEmpty()) {
				for (int i = drawnListObjects.size() - 1; i >= 0; i--) {
					IEnhancedGuiObject o = drawnListObjects.get(i);
					if (o != null) {
						if (o.isMouseInside(mXIn, mYIn)) { o.mousePressed(mXIn, mYIn, button); break; }
					}
				}
			}
		}
	}
		
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		if (parentCreator.getCurrentMode() == CreatorMode.EDIT_MODE) {
			try { parentCreator.getHandler().toolRelease(mXIn, mYIn, button); } catch (Exception e) {}
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		parentCreator.keyPressed(typedChar, keyCode);
	}
	
	@Override
	public void keyReleased(char typedChar, int keyCode) {
		parentCreator.keyReleased(typedChar, keyCode);
	}
	
	//--------------------------
	//CreatorDesignSpace Methods
	//--------------------------
	
	//--------------------------
	//CreatorDesignSpace Getters
	//--------------------------
	
	//--------------------------
	//CreatorDesignSpace Setters
	//--------------------------
}
