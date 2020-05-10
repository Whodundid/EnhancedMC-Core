package com.Whodundid.guiCreator.gui;

import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.guiCreator.gui.creatorParts.colorPicker.CreatorColorPicker;
import com.Whodundid.guiCreator.gui.creatorParts.creatorInfo.CreatorInfoArea;
import com.Whodundid.guiCreator.gui.creatorParts.designSpace.CreatorDesignSpace;
import com.Whodundid.guiCreator.gui.creatorParts.modeBelt.CreatorModeBelt;
import com.Whodundid.guiCreator.gui.creatorParts.objectProperties.CreatorObjectProperties;
import com.Whodundid.guiCreator.gui.creatorParts.toolbelt.CreatorToolBelt;
import com.Whodundid.guiCreator.handles.CreatorButtonHandler;
import com.Whodundid.guiCreator.util.CreatorMode;
import com.Whodundid.guiCreator.util.CreatorTool;
import org.lwjgl.input.Keyboard;

import static com.Whodundid.guiCreator.util.CreatorTool.*;
import static com.Whodundid.guiCreator.util.CreatorMode.*;

public class CreatorGui extends WindowParent {
	
	//vars
	CreatorTool curTool = MOVE;
	CreatorMode curMode = EDIT_MODE;
	StorageBox<Integer, Integer> clickPos = new StorageBox(0, 0);
	ScreenLocation resizingDir = ScreenLocation.out;
	
	//buttons
	EGuiButton create, open, undo, redo, reset;
	
	//object
	IEnhancedGuiObject selectedObject = null;
	IEnhancedGuiObject creatingObject = null;
	IEnhancedGuiObject movingObject = null;
	IEnhancedGuiObject resizingObject = null;
	
	//parts
	CreatorToolBelt toolBelt;
	CreatorModeBelt modeBelt;
	CreatorObjectProperties properties;
	CreatorDesignSpace designer;
	CreatorInfoArea infoArea;
	CreatorColorPicker colorPicker;
	
	//handler
	CreatorButtonHandler handler;
	
	//-----------------------
	//Constructor & Overrides
	//-----------------------
	
	public CreatorGui() {
		super();
		aliases.add("creatorgui", "guicreator", "egc", "ecg");
		handler = new CreatorButtonHandler(this);
	}
	
	@Override
	public void initGui() {
		setObjectName("EMC Gui Creation Tool");
		setPinnable(false);
		setDimensions(10, EGuiHeader.defaultHeight + 10, res.getScaledWidth() - 10, res.getScaledHeight() - EGuiHeader.defaultHeight - 10);
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		modeBelt = new CreatorModeBelt(this, startX + 3, startY + 3, 2);
		
		addObject(modeBelt);
		
		toolBelt = new CreatorToolBelt(this, modeBelt.startX, modeBelt.endY + 2, 2);
		properties = new CreatorObjectProperties(this);
		designer = new CreatorDesignSpace(this, modeBelt.endX + 5, startY + 3);
		
		addObject(toolBelt, properties, designer);
		
		infoArea = new CreatorInfoArea(this);
		colorPicker = new CreatorColorPicker(this);
		
		addObject(infoArea, colorPicker);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		
		try {
			if (clickPos != null && clickPos.getObject() != null && clickPos.getValue() != null) {
				if (movingObject != null) {
					movingObject.move(mXIn - clickPos.getObject(), mYIn - clickPos.getValue()); clickPos.setValues(mXIn, mYIn);
				}
				else if (resizingObject != null && resizingDir != ScreenLocation.out) {
					resizingObject.resize(mXIn - clickPos.getObject(), mYIn - clickPos.getValue(), resizingDir); clickPos.setValues(mXIn, mYIn);
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
		
		drawDefaultBackground();
		
		drawRect(startX + 1, startY + 1, toolBelt.endX + 2, endY - 1, 0xff222222); //tools background
		drawRect(properties.startX - 2, startY + 1, endX - 1, endY - 1, 0xff222222); //properties background
		drawRect(toolBelt.endX + 2, startY, toolBelt.endX + 3, endY, 0xff000000); //divider line (tools - designer)
		drawRect(properties.startX - 3, startY, properties.startX - 2, endY, 0xff000000); //divider line (designer - properties)
		
		super.drawObject(mXIn, mYIn);
		
		//draw red box around selected object
		if (selectedObject != null && getCurrentMode() == CreatorMode.EDIT_MODE) {
			EDimension sdim = selectedObject.getDimensions();
			drawHRect(sdim.startX - 0.5, sdim.startY - 0.5, sdim.endX + 0.5, sdim.endY + 0.5, 0.5, EColors.lred);
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == Keyboard.KEY_D && isCtrlKeyDown()) {
			setSelectedObject(null);
		}
		if (keyCode == 211) { //delete
			if (selectedObject != null) {
				getDesignSpace().removeObjectFromList(selectedObject);
				selectedObject = null;
			}
		}
	}
	
	@Override
	public void keyReleased(char typedChar, int keyCode) {
		
	}
	
	//------------------
	//CreatorGui Methods
	//------------------
	
	public void update() {
		infoArea.update();
	}
	
	public void updateMode() {
		EArrayList<IEnhancedGuiObject> objs = getDesignSpace().getListObjects();
		
		if (curMode == CreatorMode.EDIT_MODE) {
			for (IEnhancedGuiObject o : objs) {
				o.setClickable(false);
			}
		}
		else if (curMode == CreatorMode.TEST_MODE) {
			for (IEnhancedGuiObject o : objs) {
				o.setClickable(true);
			}
		}
	}
	
	//------------------
	//CreatorGui Getters
	//------------------
	
	public CreatorToolBelt getToolBelt() { return toolBelt; }
	public CreatorModeBelt getModeBelt() { return modeBelt; }
	public CreatorObjectProperties getObjProperties() { return properties; }
	public CreatorDesignSpace getDesignSpace() { return designer; }
	public CreatorInfoArea getInfoArea() { return infoArea; }
	public CreatorColorPicker getColorPicker() { return colorPicker; }
	
	public IEnhancedGuiObject getSelectedObject() { return selectedObject; }
	public IEnhancedGuiObject getCreatingObject() { return creatingObject; }
	public IEnhancedGuiObject getMovingObject() { return movingObject; }
	public IEnhancedGuiObject getResizingObject() { return resizingObject; }
	public ScreenLocation getResizingDir() { return resizingDir; }
	
	public CreatorButtonHandler getHandler() { return handler; }
	public StorageBox<Integer, Integer> getClickPos() { return clickPos; }
	
	public CreatorTool getCurrentTool() { return curTool; }
	public CreatorMode getCurrentMode() { return curMode; }
	
	//------------------
	//CreatorGui Setters
	//------------------
	
	public CreatorGui setCurrentTool(CreatorTool toolIn) { curTool = toolIn; update(); return this; }
	public CreatorGui setCurrentMode(CreatorMode modeIn) { if (curMode != modeIn) { selectedObject = null; } curMode = modeIn; update(); updateMode(); return this; }
	
	public CreatorGui setSelectedObject(IEnhancedGuiObject objectIn) { selectedObject = objectIn; update(); return this; }
	public CreatorGui setCreatingObject(IEnhancedGuiObject objectIn) { creatingObject = objectIn; update(); return this; }
	public CreatorGui setMovingObject(IEnhancedGuiObject objectIn) { movingObject = objectIn; return this; }
	public CreatorGui setResizingObject(IEnhancedGuiObject objectIn) { resizingObject = objectIn; return this; }
	public CreatorGui setResizingDir(ScreenLocation locIn) { resizingDir = locIn; return this; }
	
	//----------------
	//Button functions
	//----------------
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == create) { create(); }
		if (object == open) { open(); }
		if (object == undo) { undo(); }
		if (object == redo) { redo(); }
		if (object == reset) { reset(); }
		
		update();
	}

	private void create() {
		
	}
	
	private void open() {
		
	}
	
	private void undo() {
		
	}
	
	private void redo() {
		
	}
	
	private void reset() {
		
	}
}
