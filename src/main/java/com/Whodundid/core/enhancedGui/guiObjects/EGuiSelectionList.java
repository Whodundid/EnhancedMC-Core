package com.Whodundid.core.enhancedGui.guiObjects;

import com.Whodundid.core.enhancedGui.guiObjectUtil.TextAreaLine;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import net.minecraft.client.gui.ScaledResolution;

//Last edited: Jan 24, 2019
//First Added: Jan 23, 2019
//Author: Hunter Bragg

public class EGuiSelectionList extends InnerEnhancedGui implements IEnhancedActionObject {
	
	EGuiButton select, cancelSel;
	EGuiTextArea selectionList;
	Object defaultSelectionObject = null;
	StorageBoxHolder<String, ?> listContents = null;
	Object selectedObject = null;
	Object storedObject = null;
	EArrayList<Object> additionalValues = null;
	String headerString = "Make A Selection..";
	IEnhancedGuiObject actionReciever;
	int headerStringColor = 0xb2b2b2;
	private EGuiSelectionList instance = null;
	
	public EGuiSelectionList(IEnhancedGuiObject parentIn, StorageBoxHolder<String, ?> objectListIn) { this(parentIn, true, objectListIn, null); }
	public EGuiSelectionList(IEnhancedGuiObject parentIn, StorageBoxHolder<String, ?> objectListIn, Object selObjIn) { this(parentIn, true, objectListIn, selObjIn); }
	public EGuiSelectionList(IEnhancedGuiObject parentIn, int xPos, int yPos, StorageBoxHolder<String, ?> objectListIn) { this(parentIn, xPos, yPos, 200, 230, objectListIn, null); }
	public EGuiSelectionList(IEnhancedGuiObject parentIn, int xPos, int yPos, StorageBoxHolder<String, ?> objectListIn, Object selObjIn) { this(parentIn, xPos, yPos, 200, 230, objectListIn, selObjIn); }
	public EGuiSelectionList(IEnhancedGuiObject parentIn, int xPos, int yPos, int widthIn, int heightIn, StorageBoxHolder<String, ?> objectListIn) { this(parentIn, xPos, yPos, widthIn, heightIn, objectListIn, null); }
	public EGuiSelectionList(IEnhancedGuiObject parentIn, int xPos, int yPos, int widthIn, int heightIn, StorageBoxHolder<String, ?> objectListIn, Object selObjIn) {
		init(parentIn, xPos, yPos, widthIn, heightIn);
		listContents = objectListIn;
		defaultSelectionObject = selObjIn;
		actionReciever = getParent();
		instance = this;
	}
	protected EGuiSelectionList(IEnhancedGuiObject parentIn, boolean noPos, StorageBoxHolder<String, ?> objectListIn, Object selObjIn) {
		ScaledResolution res = new ScaledResolution(mc);
		init(parentIn, (res.getScaledWidth() - 200) / 2, (res.getScaledHeight() - 230) / 2, 200, 230);
		listContents = objectListIn;
		defaultSelectionObject = selObjIn;
		actionReciever = getParent();
		instance = this;
	}
	
	@Override
	public void initGui() {
		requestFocus();
		getTopParent().setFocusLockObject(this);
		setZLevel(10000);
		
		setHeader(new EGuiHeader(this));
		header.setDisplayString(headerString);
		header.setDisplayStringColor(headerStringColor);
		
		select = new EGuiButton(this, startX + 10, endY - 28, 80, 20, "Select");
		cancelSel = new EGuiButton(this, endX - 90, endY - 28, 80, 20, "Cancel");
		
		selectionList = new EGuiTextArea(this, startX + 10, startY + 10, width - 20, height - 45, false).setDrawLineNumbers(true);
		
		addObject(select, cancelSel, selectionList);
		
		for (StorageBox<String, ?> b : listContents) {
			TextAreaLine l = new TextAreaLine(selectionList, b.getObject(), 0xffffff, b.getValue()) {
				@Override
				public void onDoubleClick() {
					if (selectionList.getCurrentLine() != null && getStoredObj() != null) {
						selectOptionAndClose(getStoredObj());
					}
				}
			};
			selectionList.addTextLine(l);
		}
		if (!selectionList.getTextDocument().isEmpty()) { selectionList.setSelectedLine(selectionList.getTextLineWithLineNumber(1)); }
	}
	
	private void selectOptionAndClose(Object o) {
		selectedObject = o;
		actionReciever.actionPerformed(this);
		close();
	}
	
	public EGuiSelectionList setHeaderString(String msgIn) { headerString = msgIn; if (header != null) { header.setDisplayString(msgIn); } return this; }
	public EGuiSelectionList setHeaderStringColor(int colorIn) { headerStringColor = colorIn; if (header != null) { header.setDisplayStringColor(colorIn); } return this; }
	public EGuiSelectionList setStoredObject(Object objIn) { storedObject = objIn; return this; }
	public EGuiSelectionList setAdditionalValues(EArrayList<Object> values) { additionalValues = values; return this; }
	public String getHeaderString() { return header != null ? header.getDisplayString() : headerString; }
	public int getHeaderStringColor() { return header != null ? header.getStringColor() : headerStringColor; }
	public Object getStoredObject() { return storedObject; }
	public EArrayList<Object> getAdditionalValues() { return additionalValues; }
	
	@Override
	public void onObjectAddedToParent() {
		bringToFront();
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		drawDefaultBackground();
		select.setEnabled(selectionList.getCurrentLine() != null);
		super.drawObject(mXIn, mYIn, ticks);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		bringToFront();
		super.mousePressed(mXIn, mYIn, button);
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == 1) {
			close();
		}
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object.equals(select)) {
			if (selectionList.getCurrentLine() != null && selectionList.getCurrentLine().getStoredObj() != null) {
				selectOptionAndClose(selectionList.getCurrentLine().getStoredObj());
			}
		}
		if (object.equals(cancelSel)) { close(); }
	}
	
	@Override public boolean runActionOnPress() { return false; }
	@Override public EGuiSelectionList setRunActionOnPress(boolean val) { return null; }
	@Override public void performAction() {}
	@Override public EGuiSelectionList setActionReciever(IEnhancedGuiObject objIn) { actionReciever = objIn; return this; }
	@Override public IEnhancedGuiObject getActionReciever() { return actionReciever; }
	@Override public EGuiSelectionList setSelectedObject(Object objIn) { selectedObject = objIn; return this; }
	@Override public Object getSelectedObject() { return selectedObject; }
	
}
