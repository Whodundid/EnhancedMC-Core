package com.Whodundid.guiCreator.handles;

import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.guiCreator.gui.CreatorGui;
import com.Whodundid.guiCreator.handles.toolHandles.*;

public class CreatorButtonHandler {
	
	CreatorGui parentCreator;
	
	//tools
	CreatorHandler creator;
	ShapeHandler shape;
	ActionHandler action;
	BasicHandler basic;
	AdvancedHandler advanced;
	
	//-------------------------------
	
	public CreatorButtonHandler(CreatorGui guiIn) {
		parentCreator = guiIn;
		
		//tools init
		creator = new CreatorHandler(guiIn);
		shape = new ShapeHandler(guiIn);
		action = new ActionHandler(guiIn);
		basic = new BasicHandler(guiIn);
		advanced = new AdvancedHandler(guiIn);
	}
	
	public void toolPress(int x, int y, int button) {
		//set clickPos
		if (parentCreator.getClickPos() != null) { parentCreator.getClickPos().setValues(x, y); }
		
		switch (parentCreator.getCurrentTool().getCategory()) {
		case CREATOR: creator.handlePress(x, y, button); break;
		case SHAPE: shape.handlePress(x, y, button); break;
		case ACTION: action.handlePress(x, y, button); break;
		case BASIC: basic.handlePress(x, y, button); break;
		case ADVANCED: advanced.handlePress(x, y, button); break;
		default: break;
		}
		
		//only do if creatingObject not null
		if (parentCreator.getCreatingObject() != null) {
			//add object to the designSpace & select it
			parentCreator.getCreatingObject().setClickable(false);
			
			//get object that mouse is hovering over
			IEnhancedGuiObject hoverOver = null;
			EArrayList<IEnhancedGuiObject> listObjects = parentCreator.getDesignSpace().getListObjects();
			if (listObjects.isNotEmpty()) {
				for (int i = listObjects.size() - 1; i >= 0; i--) {
					IEnhancedGuiObject o = listObjects.get(i);
					if (o != null) {
						if (o.isMouseInside(x, y)) {
							hoverOver = o;
							break;
						}
					}
				}
			}
			
			if (hoverOver == null) {
				//don't use the relative list coordinates
				parentCreator.getDesignSpace().addObjectToList(false, parentCreator.getCreatingObject());
			}
			else {
				hoverOver.addObject(parentCreator.getCreatingObject());
			}
			
			parentCreator.setSelectedObject(parentCreator.getCreatingObject());
			
			//set the tool to select
			//parentCreator.setCurrentTool(CreatorTool.SELECT);
		}
	}
	
	public void toolRelease(int x, int y, int button) {
		switch (parentCreator.getCurrentTool().getCategory()) {
		case CREATOR: creator.handleRelease(x, y, button); break;
		case SHAPE: shape.handleRelease(x, y, button); break;
		case ACTION: action.handleRelease(x, y, button); break;
		case BASIC: basic.handleRelease(x, y, button); break;
		case ADVANCED: advanced.handleRelease(x, y, button); break;
		default: break;
		}
		
		//clear clickPos
		if (parentCreator.getClickPos() != null) { parentCreator.getClickPos().clear(); }
		
		//reset the creating object
		parentCreator.setCreatingObject(null);
	}
}
