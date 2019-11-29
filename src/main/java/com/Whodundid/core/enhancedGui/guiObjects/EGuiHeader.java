package com.Whodundid.core.enhancedGui.guiObjects;

import com.Whodundid.core.enhancedGui.guiObjectUtil.EObjectGroup;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.ObjectModifyType;
import com.Whodundid.core.enhancedGui.types.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedTopParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.util.renderUtil.Resources;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EDimension;
import org.lwjgl.input.Mouse;

//Dec 31, 2018
//Last edited: Jan 12, 2019
//First Added: Dec 17, 2018
//Author: Hunter Bragg

public class EGuiHeader extends EnhancedGuiObject {
	
	public EGuiButton fileUpButton, closeButton, pinButton;
	public boolean fullClose = false;
	public boolean drawDefault = true;
	public boolean drawTitle = true;
	public String title = "";
	public int borderColor = 0xff000000;
	public int mainColor = 0xff2D2D2D;
	public int titleColor = 0xb2b2b2;
	protected boolean drawBackground = true;
	protected boolean drawHeader = true;
	protected boolean drawParentFocus = true;
	protected boolean moving = false;
	
	protected EGuiHeader() {}
	public EGuiHeader(IEnhancedGuiObject parentIn) { this(parentIn, true, 19, ""); }
	public EGuiHeader(IEnhancedGuiObject parentIn, boolean drawDefaultIn, int headerHeight) { this(parentIn, drawDefaultIn, headerHeight, ""); }
	public EGuiHeader(IEnhancedGuiObject parentIn, boolean drawDefaultIn, int headerHeight, String titleIn) {
		if (parentIn != null) {
			EDimension dim = parentIn.getDimensions();
			init(parentIn, dim.startX, dim.startY - headerHeight, dim.width, headerHeight);
		}
		drawDefault = drawDefaultIn;
		
		if (drawDefault) {
			addFileUpButton();
			addPinButton();
			//addMoveButton();
			addCloseButton();
			
			if (titleIn.isEmpty()) {
				title = getParent().getObjectName();
			}
			
			EObjectGroup group = new EObjectGroup(getParent());
			group.addObject(this, fileUpButton, pinButton, closeButton);
			setObjectGroup(group);
		}
	}
	
	protected EGuiHeader addFileUpButton() {
		fileUpButton = new EGuiButton(this, endX - 52, startY + 2, 16, 16, "");
		fileUpButton.setTextures(Resources.guiFileUpButton, Resources.guiFileUpButtonSel).setVisible(false);
		addObject(fileUpButton);
		return this;
	}
	
	protected EGuiHeader addPinButton() {
		pinButton = new EGuiButton(this, endX - 35, startY + 2, 16, 16, "");
		IWindowParent p = getWindowParent();
		if (p != null) {
			pinButton.setButtonTexture(p.isPinned() ? Resources.guiPinButtonOpen : Resources.guiPinButton);
			pinButton.setButtonSelTexture(p.isPinned() ? Resources.guiPinButtonOpenSel : Resources.guiPinButtonSel);
			pinButton.setDrawBackground(true).setBackgroundColor(0xffbb0000).setPersistent(true);
			addObject(pinButton);
		}
		return this;
	}
	
	protected EGuiHeader addCloseButton() {
		closeButton = new EGuiButton(this, endX - 18, startY + 2, 16, 16, "");
		closeButton.setTextures(Resources.guiCloseButton, Resources.guiCloseButtonSel).setPersistent(true);
		addObject(closeButton);
		return this;
	}
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		IEnhancedTopParent top = getTopParent();
		if (!moving && top.getModifyingObject() == parent && top.getModifyType() == ObjectModifyType.Move && !Mouse.isButtonDown(0)) {
			top.clearModifyingObject();
		}
		
		if (drawHeader) {
			boolean anyFocus = false;
			if (drawParentFocus) {
				IWindowParent p = getWindowParent();
				if (p != null) {
					if (p.hasFocus()) { anyFocus = true; }
					else {
						for (IEnhancedGuiObject o : p.getAllChildren()) {
							if (o.hasFocus()) { anyFocus = true; break; }
						}
					}
				}
			}
			
			if (drawBackground) {
				drawRect(startX, startY, startX + 1, startY + height, borderColor); //left
				drawRect(startX + 1, startY, endX - 1, startY + 1, borderColor); //top
				drawRect(endX - 1, startY, endX, startY + height, borderColor); //right
				drawRect(startX + 1, startY + 1, endX - 1, startY + height, anyFocus ? mainColor - 0x1f1f1f : mainColor); //mid
			}
			if (drawTitle) {
				drawString(title, startX + 4, startY + height / 2 - 3, titleColor);
			}
		}
		super.drawObject(mX, mY, ticks);
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		super.mousePressed(mX, mY, button);
		if (hasFocus()) {
			if (getObjectGroup() != null && getObjectGroup().getGroupParent() != null) {
				IEnhancedGuiObject groupParent = getObjectGroup().getGroupParent();
				if (groupParent.isResizeable()) {
					if (groupParent.getEdgeAreaMouseIsOn() != ScreenLocation.out) { groupParent.mousePressed(mX, mY, button); }
					else { headerClick(button); }
					return;
				}
			}
			headerClick(button);
		}
	}
	
	@Override
	public EGuiHeader setEnabled(boolean val) {
		drawHeader = val;
		if (drawDefault) {
			//moveButton.setVisible(val).setPersistent(val);
			closeButton.setVisible(val).setPersistent(val);
			if (getWindowParent() != null) {
				if (fileUpButton != null) { fileUpButton.setVisible(val && getWindowParent().getGuiHistory() != null && !getWindowParent().getGuiHistory().isEmpty()); }
			}
		}
		for (IEnhancedGuiObject o : getImmediateChildren()) { o.setVisible(val); }
		return this;
	}
	
	protected void headerClick(int button) {
		getParent().bringToFront();
		IEnhancedTopParent topParent = getTopParent();
		if (button == 0) {
			topParent.setModifyingObject(parent, ObjectModifyType.Move);
			topParent.setModifyMousePos(mX, mY);
		}
		else { topParent.clearModifyingObject(); }
	}
	
	public EGuiHeader updateFileUpButtonVisibility() {
		if (getParent() instanceof WindowParent) {
			WindowParent gui = (WindowParent) getParent();
			if (gui != null && gui.getGuiHistory() != null && !gui.getGuiHistory().isEmpty() && fileUpButton != null) {
				fileUpButton.setVisible(true);
			}
		}
		else if (getWindowParent() != null && getWindowParent().getGuiHistory() != null && !getWindowParent().getGuiHistory().isEmpty() && fileUpButton != null) { fileUpButton.setVisible(true); }
		return this;
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object == closeButton) { handleClose(); }
		if (object == pinButton) { handlePin(); }
		if (object == fileUpButton) { handleFileUp(); }
	}
	
	protected void handleClose() {
		parent.close();
	}
	
	protected void handlePin() {
		IWindowParent p = getWindowParent();
		if (p != null) {
			if (pinButton.getPressedButton() == 0) { p.setPinned(!p.isPinned()); }
			pinButton.setButtonTexture(p.isPinned() ? Resources.guiPinButtonOpen : Resources.guiPinButton);
			pinButton.setButtonSelTexture(p.isPinned() ? Resources.guiPinButtonOpenSel : Resources.guiPinButtonSel);
		}
	}
	
	protected void handleFileUp() {
		if (getParent() instanceof WindowParent) {
			((WindowParent) getParent()).fileUpAndClose();
		}
		else if (getTopParent() != null) { getTopParent().closeGui(false); }
	}
	
	public EGuiHeader setDrawButtons(boolean val) {
		if (fileUpButton != null) { fileUpButton.setVisible(val); }
		if (pinButton != null) { pinButton.setVisible(val); }
		if (closeButton != null) { closeButton.setVisible(val); }
		return this;
	}
	
	public EGuiHeader setTitleColor(int colorIn) { titleColor = colorIn; return this; }
	public EGuiHeader setBorderColor(int colorIn) { borderColor = colorIn; return this; }
	public EGuiHeader setMainColor(int colorIn) { mainColor = colorIn; return this; }
	public EGuiHeader setTitle(String stringIn) { title = stringIn; return this; }
	public EGuiHeader setDrawTitle(boolean val) { drawTitle = val; return this; }
	public EGuiHeader setDrawBackground(boolean val) { drawBackground = val; return this; }
	public EGuiHeader setDrawHeader(boolean val) { drawHeader = val; return this; }
	public EGuiHeader setParentFocusDrawn(boolean val) { drawParentFocus = val; return this; }
	
	public int getTitleColor() { return titleColor; }
	public String getTitle() { return title; }
	public boolean isParentFocusDrawn() { return drawParentFocus; }
}
