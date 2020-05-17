package com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header;

import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiUtil.EObjectGroup;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.ObjectModifyType;
import com.Whodundid.core.enhancedGui.types.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedTopParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.util.miscUtil.EMouseHelper;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import org.lwjgl.input.Mouse;

//Author: Hunter Bragg

public class EGuiHeader extends EnhancedGuiObject {
	
	public static int defaultHeight = 19;
	public static int buttonWidth = 16;
	public EGuiButton fileUpButton, closeButton, maximizeButton, pinButton;
	public boolean fullClose = false;
	public boolean drawDefault = true;
	public boolean drawTitle = true;
	public boolean titleCentered = false;
	public String title = "";
	public int borderColor = 0xff000000;
	public int mainColor = 0xff2D2D2D;
	public int titleColor = 0xb2b2b2;
	public int titleOffset = 0;
	protected boolean headerMoveable = true;
	protected boolean drawBackground = true;
	protected boolean drawHeader = true;
	protected boolean drawParentFocus = true;
	protected boolean alwaysDrawFocused = false;
	protected boolean moving = false;
	protected EArrayList<HeaderTab> tabs = new EArrayList();
	protected IWindowParent window;
	private int buttonPos = 18;
	
	//-----------------------
	//EGuiHeader Constructors
	//-----------------------
	
	protected EGuiHeader() {}
	public EGuiHeader(IEnhancedGuiObject parentIn) { this(parentIn, true, defaultHeight, ""); }
	public EGuiHeader(IEnhancedGuiObject parentIn, boolean drawDefaultIn, int headerHeight) { this(parentIn, drawDefaultIn, headerHeight, ""); }
	public EGuiHeader(IEnhancedGuiObject parentIn, boolean drawDefaultIn, int headerHeight, String titleIn) {
		if (parentIn != null) {
			EDimension dim = parentIn.getDimensions();
			init(parentIn, dim.startX, dim.startY - headerHeight, dim.width, headerHeight);
			window = parentIn.getWindowParent();
		}
		drawDefault = drawDefaultIn;
		
		if (drawDefault) {
			addCloseButton();
			addMaximizeButton();
			addPinButton();
			addFileUpButton();
			
			if (titleIn.isEmpty()) {
				title = getParent().getObjectName();
			}
			else { title = titleIn; }
			
			EObjectGroup group = new EObjectGroup(getParent());
			group.addObject(this, fileUpButton, pinButton, maximizeButton, closeButton);
			setObjectGroup(group);
		}
		else {
			title = titleIn;
		}
	}
	
	//---------------------------
	//EGuiHeader Function Buttons
	//---------------------------
	
	protected EGuiHeader addCloseButton() {
		closeButton = new EGuiButton(this, endX - buttonPos, startY + 2, 16, 16);
		closeButton.setTextures(EMCResources.guiCloseButton, EMCResources.guiCloseButtonSel).setPersistent(true);
		closeButton.setHoverText("Close");
		addObject(closeButton);
		buttonPos += buttonWidth;
		return this;
	}
	
	protected EGuiHeader addMaximizeButton() {
		maximizeButton = new EGuiButton(this, endX - buttonPos, startY + 2, 16, 16);
		
		if (window != null && window.isMaximizable()) {
			maximizeButton.setButtonTexture(window.isMaximized() ? EMCResources.guiMinButton : EMCResources.guiMaxButton);
			maximizeButton.setButtonSelTexture(window.isMaximized() ? EMCResources.guiMinButtonSel : EMCResources.guiMaxButtonSel);
			
			maximizeButton.setHoverText(window.isMaximized() ? "Miniaturize" : "Maximize");
			
			addObject(maximizeButton);
			buttonPos += (buttonWidth + 1);
		}
		return this;
	}
	
	protected EGuiHeader addPinButton() {
		pinButton = new EGuiButton(this, endX - buttonPos, startY + 2, 16, 16) {
			@Override
			public void drawObject(int mXIn, int mYIn) {
				if (window != null && window.isPinnable()) {
					setBackgroundColor(window.isPinned() ? 0xffbb0000 : EColors.dgray.c());
				}
				super.drawObject(mXIn, mYIn);
			}
		};
		if (window != null && window.isPinnable()) {
			pinButton.setTextures(EMCResources.guiPinButtonOpen, EMCResources.guiPinButtonOpenSel);
			pinButton.setDrawBackground(true).setBackgroundColor(0xffbb0000).setPersistent(true);
			pinButton.setHoverText("Pin to Hud");
			addObject(pinButton);
			buttonPos += (buttonWidth + 1);
		}
		return this;
	}
	
	protected EGuiHeader addFileUpButton() {
		fileUpButton = new EGuiButton(this, endX - buttonPos, startY + 2, 16, 16);
		fileUpButton.setTextures(EMCResources.guiFileUpButton, EMCResources.guiFileUpButtonSel).setVisible(false);
		fileUpButton.setHoverText("Go Back");
		addObject(fileUpButton);
		buttonPos += (buttonWidth + 1);
		return this;
	}
	
	//---------------------------------------
	//EGuiHeader IEnhancedGuiObject Overrides
	//---------------------------------------
	
	@Override
	public void drawObject(int mX, int mY) {
		IEnhancedTopParent top = getTopParent();
		if (!moving && top.getModifyingObject() == parent && top.getModifyType() == ObjectModifyType.Move && !Mouse.isButtonDown(0)) {
			top.clearModifyingObject();
		}
		
		if (drawHeader) {
			boolean anyFocus = alwaysDrawFocused;
			if (drawParentFocus) {
				IEnhancedGuiObject p = drawDefault ? getWindowParent() : getParent();
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
			
			scissor(startX + 1, startY + 1, endX - 1, endY - 1);
			{
				if (drawTitle) {
					double tx = startX + 4 + titleOffset;
					if (titleCentered) {
						double tw = mc.fontRendererObj.getStringWidth(title);
						tx = startX + (width / 2 - tw / 2) + titleOffset + 1;
					}
					drawString(title, tx, startY + height / 2 - 3, titleColor);
				}
				super.drawObject(mX, mY);
			}
			endScissor();
		}
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		super.mousePressed(mX, mY, button);
		if (hasFocus()) {
			if (getObjectGroup() != null && getObjectGroup().getGroupParent() != null) {
				IEnhancedGuiObject groupParent = getObjectGroup().getGroupParent();
				if (groupParent.isResizeable()) {
					if (groupParent.getEdgeAreaMouseIsOn() != ScreenLocation.out) {
						getTopParent().setFocusedObject(getWindowParent());
						groupParent.mousePressed(mX, mY, button);
					}
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
		for (IEnhancedGuiObject o : getObjects()) { o.setVisible(val); }
		return this;
	}
	
	protected void headerClick(int button) {
		IEnhancedTopParent topParent = getTopParent();
		if (button == 0) {
			getParent().bringToFront();
			if (headerMoveable) {
				if (window.isMaximized()) {
					handleMaximize();
					EDimension dims = window.getDimensions();
					window.setPosition(EMouseHelper.mX - dims.width / 2, EMouseHelper.mY + height / 2);
				}
				topParent.setModifyingObject(parent, ObjectModifyType.Move);
				topParent.setModifyMousePos(mX, mY);
			}
		}
		else { topParent.clearModifyingObject(); }
	}
	
	public EGuiHeader updateButtonVisibility() {
		if (getParent() instanceof WindowParent) {
			WindowParent gui = (WindowParent) getParent();
			if (gui != null) {
				int buttonPos = 35;
				
				//System.out.println("maximizable: " + gui.isMaximizable());
				if (maximizeButton != null) {
					if (!gui.isMaximizable()) {
						maximizeButton.setVisible(false);
					}
					else {
						//System.out.println("max adding");
						maximizeButton.setVisible(true);
						maximizeButton.setDimensions(endX - buttonPos, startY + 2, buttonWidth, buttonWidth);
						buttonPos += (buttonWidth + 1);
					}
				}
				
				//System.out.println(buttonPos);
				
				//System.out.println("pinnable: " + gui.isPinnable());
				if (pinButton != null) {
					if (!gui.isPinnable()) {
						pinButton.setVisible(false);
					}
					else {
						//System.out.println("pin adding");
						pinButton.setVisible(true);
						pinButton.setDimensions(endX - buttonPos, startY + 2, buttonWidth, buttonWidth);
						buttonPos += (buttonWidth + 1);
					}
				}
				
				//System.out.println(buttonPos);
				
				//System.out.println("history: " + (gui.getGuiHistory() != null && !gui.getGuiHistory().isEmpty()));
				if (fileUpButton != null) {
					if (gui.getGuiHistory() != null && gui.getGuiHistory().isEmpty()) {
						fileUpButton.setVisible(false);
					}
					else {
						//System.out.println("file adding");
						fileUpButton.setVisible(true);
						fileUpButton.setDimensions(endX - buttonPos, startY + 2, buttonWidth, buttonWidth);
						buttonPos += (buttonWidth + 1);
					}
				}
				
				//System.out.println(buttonPos);
			}
		}
		else if (getWindowParent() != null && getWindowParent().getGuiHistory() != null && !getWindowParent().getGuiHistory().isEmpty() && fileUpButton != null) { fileUpButton.setVisible(true); }
		return this;
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == closeButton) { handleClose(); }
		if (object == pinButton) { handlePin(); }
		if (object == fileUpButton) { handleFileUp(); }
		if (object == maximizeButton) { handleMaximize(); }
	}
	
	protected void handleClose() {
		parent.close();
	}
	
	protected void handlePin() {
		IWindowParent p = getWindowParent();
		if (p != null) {
			if (pinButton.getPressedButton() == 0) { p.setPinned(!p.isPinned()); }
			//pinButton.setButtonTexture(p.isPinned() ? EMCResources.guiPinButtonOpen : EMCResources.guiPinButton);
			//pinButton.setButtonSelTexture(p.isPinned() ? EMCResources.guiPinButtonOpenSel : EMCResources.guiPinButtonSel);
		}
	}
	
	protected void handleMaximize() {
		IWindowParent p = getWindowParent();
		if (p != null) {
			if (p.isMaximizable()) {
				if (p.isMaximized()) {
					p.setMaximized(false);
					p.miniturize();
				}
				else {
					p.setPreMax(p.getDimensions());
					p.setMaximized(true);
					p.maximize();
				}
				
				maximizeButton.setButtonTexture(p.isMaximized() ? EMCResources.guiMinButton : EMCResources.guiMaxButton);
				maximizeButton.setButtonSelTexture(p.isMaximized() ? EMCResources.guiMinButtonSel : EMCResources.guiMaxButtonSel);
			}
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
	
	public EGuiHeader setAlwaysDrawFocused(boolean val) { alwaysDrawFocused = val; return this; }
	public EGuiHeader setMoveable(boolean val) { headerMoveable = val; return this; }
	public EGuiHeader setTitleColor(int colorIn) { titleColor = colorIn; return this; }
	public EGuiHeader setBorderColor(int colorIn) { borderColor = colorIn; return this; }
	public EGuiHeader setMainColor(int colorIn) { mainColor = colorIn; return this; }
	public EGuiHeader setTitle(String stringIn) { title = stringIn; return this; }
	public EGuiHeader setTitleOffset(int offsetIn) { titleOffset = offsetIn; return this; }
	public EGuiHeader setDrawTitleCentered(boolean val) { titleCentered = val; return this; }
	public EGuiHeader setDrawTitle(boolean val) { drawTitle = val; return this; }
	public EGuiHeader setDrawBackground(boolean val) { drawBackground = val; return this; }
	public EGuiHeader setDrawHeader(boolean val) { drawHeader = val; return this; }
	public EGuiHeader setParentFocusDrawn(boolean val) { drawParentFocus = val; return this; }
	
	public int getTabCount() { return tabs.size(); }
	public EArrayList<HeaderTab> getTabs() { return tabs; }
	public int getTitleColor() { return titleColor; }
	public String getTitle() { return title; }
	public boolean isParentFocusDrawn() { return drawParentFocus; }
	public boolean isHeaderMoveable() { return headerMoveable; }
	
}
