package com.Whodundid.core.enhancedGui.guiObjects.windows;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.EGuiTextArea;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.TextAreaLine;
import com.Whodundid.core.enhancedGui.types.ActionWindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import net.minecraft.client.gui.ScaledResolution;

//Author: Hunter Bragg

public class EGuiSelectionList extends ActionWindowParent {
	
	protected EGuiButton select, cancelSel;
	protected EGuiTextArea selectionList;
	protected Object defaultSelectionObject = null;
	protected StorageBoxHolder<String, ?> listContents = null;
	protected Object selectedObject = null;
	protected Object storedObject = null;
	protected EArrayList<Object> additionalValues = null;
	protected String headerString = "Make A Selection..";
	protected IEnhancedGuiObject actionReciever;
	protected int headerStringColor = 0xb2b2b2;
	
	protected EGuiSelectionList() { super(EnhancedMC.getRenderer()); }
	public EGuiSelectionList(IEnhancedGuiObject parentIn, StorageBoxHolder<String, ?> objectListIn) { this(parentIn, true, objectListIn, null); }
	public EGuiSelectionList(IEnhancedGuiObject parentIn, StorageBoxHolder<String, ?> objectListIn, Object selObjIn) { this(parentIn, true, objectListIn, selObjIn); }
	public EGuiSelectionList(IEnhancedGuiObject parentIn, int xPos, int yPos, StorageBoxHolder<String, ?> objectListIn) { this(parentIn, xPos, yPos, 200, 230, objectListIn, null); }
	public EGuiSelectionList(IEnhancedGuiObject parentIn, int xPos, int yPos, StorageBoxHolder<String, ?> objectListIn, Object selObjIn) { this(parentIn, xPos, yPos, 200, 230, objectListIn, selObjIn); }
	public EGuiSelectionList(IEnhancedGuiObject parentIn, int xPos, int yPos, int widthIn, int heightIn, StorageBoxHolder<String, ?> objectListIn) { this(parentIn, xPos, yPos, widthIn, heightIn, objectListIn, null); }
	public EGuiSelectionList(IEnhancedGuiObject parentIn, int xPos, int yPos, int widthIn, int heightIn, StorageBoxHolder<String, ?> objectListIn, Object selObjIn) {
		super(parentIn);
		init(parentIn, xPos, yPos, widthIn, heightIn);
		listContents = objectListIn;
		defaultSelectionObject = selObjIn;
		actionReciever = getParent();
	}
	protected EGuiSelectionList(IEnhancedGuiObject parentIn, boolean noPos, StorageBoxHolder<String, ?> objectListIn, Object selObjIn) {
		super(parentIn);
		ScaledResolution res = new ScaledResolution(mc);
		init(parentIn, (res.getScaledWidth() - 200) / 2, (res.getScaledHeight() - 230) / 2, 200, 230);
		listContents = objectListIn;
		defaultSelectionObject = selObjIn;
		actionReciever = getParent();
		requestFocus();
		getTopParent().setFocusLockObject(this);
		setZLevel(10000);
	}
	
	@Override
	public void initGui() {	
		setObjectName("Gui Selection List");
		
		setHeader(new EGuiHeader(this));
		header.setTitle(headerString);
		header.setTitleColor(headerStringColor);
		
		select = new EGuiButton(this, endX - 85, endY - 25, 80, 20, "Select");
		cancelSel = new EGuiButton(this, startX + 5, endY - 25, 80, 20, "Cancel");
		
		selectionList = new EGuiTextArea(this, startX + 5, startY + 5, width - 10, height - 35, false) {
			@Override
			public void keyPressed(char typedChar, int keyCode) {
				super.keyPressed(typedChar, keyCode);
				if (keyCode == 28) {
					selectCurrentOptionAndClose();
				}
			}
		};
		selectionList.setDrawLineNumbers(true);
		selectionList.setResetDrawn(false);
		
		addObject(select, cancelSel, selectionList);
		
		for (StorageBox<String, ?> b : listContents) {
			TextAreaLine l = new TextAreaLine(selectionList, b.getObject(), 0xffffff, b.getValue()) {
				@Override
				public void onDoubleClick() {
					if (selectionList.getCurrentLine() != null && selectionList.getCurrentLine().getStoredObj() != null) {
						selectCurrentOptionAndClose();
					}
				}
				@Override
				public void keyPressed(char typedChar, int keyCode) {
					super.keyPressed(typedChar, keyCode);
					if (keyCode == 28) {
						selectCurrentOptionAndClose();
					}
				}
			};
			selectionList.addTextLine(l);
		}
		if (!selectionList.getTextDocument().isEmpty()) { selectionList.setSelectedLine(selectionList.getTextLine(0)); }
	}
	
	protected void selectCurrentOptionAndClose() {
		if (selectionList.getCurrentLine() != null && selectionList.getCurrentLine().getStoredObj() != null) {
			selectedObject = selectionList.getCurrentLine().getStoredObj();
			performAction(null, null);
			close();
		}
	}
	
	@Override
	public void onAdded() {
		bringToFront();
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		select.setEnabled(selectionList.getCurrentLine() != null && selectionList.getCurrentLine().getStoredObj() != null);
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		bringToFront();
		super.mousePressed(mXIn, mYIn, button);
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == 1) { close(); }
		if (keyCode == 28) { selectCurrentOptionAndClose(); }
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object.equals(select)) { selectCurrentOptionAndClose(); }
		if (object.equals(cancelSel)) { close(); }
	}
	
	public EGuiSelectionList setHeaderString(String msgIn) { headerString = msgIn; if (header != null) { header.setTitle(msgIn); } return this; }
	public EGuiSelectionList setHeaderStringColor(int colorIn) { headerStringColor = colorIn; if (header != null) { header.setTitleColor(colorIn); } return this; }
	public EGuiSelectionList setStoredObject(Object objIn) { storedObject = objIn; return this; }
	public EGuiSelectionList setAdditionalValues(EArrayList<Object> values) { additionalValues = values; return this; }
	
	public String getHeaderString() { return header != null ? header.getTitle() : headerString; }
	public int getHeaderStringColor() { return header != null ? header.getTitleColor() : headerStringColor; }
	public Object getStoredObject() { return storedObject; }
	public EArrayList<Object> getAdditionalValues() { return additionalValues; }
	
	@Override
	public void performAction(Object... args) {
		if (actionReciever != null) {
			IWindowParent p = actionReciever.getWindowParent();
			if (p != null) { p.bringToFront(); }
			actionReciever.actionPerformed(this, args);
		}
	}
	@Override public void onPress() {}
	@Override public boolean runsActionOnPress() { return false; }
	@Override public EGuiSelectionList setRunActionOnPress(boolean val) { return null; }
	@Override public EGuiSelectionList setActionReceiver(IEnhancedGuiObject objIn) { actionReciever = objIn; return this; }
	@Override public IEnhancedGuiObject getActionReceiver() { return actionReciever; }
	@Override public EGuiSelectionList setSelectedObject(Object objIn) { selectedObject = objIn; return this; }
	@Override public Object getSelectedObject() { return selectedObject; }
}
