package com.Whodundid.core.windowLibrary.windowObjects.windows;

import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.TrippleBox;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.textArea.TextAreaLine;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.textArea.WindowTextArea;
import com.Whodundid.core.windowLibrary.windowTypes.ActionWindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import java.util.Iterator;

//Author: Hunter Bragg

public class WindowSelectionList extends ActionWindowParent {
	
	protected WindowButton select, cancel;
	protected WindowTextArea list;
	protected EArrayList<TrippleBox<String, Integer, Object>> toAdd = new EArrayList();
	protected IWindowObject actionReciever;
	
	private int vPos;
	private int hPos;
	
	//------------------------------
	//EGuiSelectionList Constructors
	//------------------------------
	
	public WindowSelectionList(IWindowObject parent) {
		super(parent);
	}
	
	//---------------------------
	//EnhancedGuiObject Overrides
	//---------------------------
	
	@Override
	public void initWindow() {
		setObjectName("Make A Selection..");
		setDimensions(200, 230);
		setMinDims(100, 100);
		setResizeable(true);
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		select = new WindowButton(this, endX - 85, endY - 25, 80, 20, "Select");
		cancel = new WindowButton(this, startX + 5, endY - 25, 80, 20, "Cancel");
		
		list = new WindowTextArea(this, startX + 5, startY + 5, width - 10, height - 35, false) {
			@Override
			public void keyPressed(char typedChar, int keyCode) {
				super.keyPressed(typedChar, keyCode);
				if (keyCode == 28) {
					selectCurrentOptionAndClose();
				}
			}
		};
		list.setResetDrawn(false);
		
		Iterator<TrippleBox<String, Integer, Object>> it = toAdd.iterator();
		while (it.hasNext()) {
			TrippleBox<String, Integer, Object> b = it.next();
			add(b.o1, b.o2, b.o3);
			it.remove();
		}
		if (!list.getTextDocument().isEmpty()) { list.setSelectedLine(list.getTextLine(0)); }
		
		addObject(select, cancel, list);
	}
	
	@Override
	public void preReInit() {
		vPos = list.getVScrollBar().getScrollPos();
		hPos = list.getHScrollBar().getScrollPos();
	}
	
	@Override
	public void postReInit() {
		list.getVScrollBar().setScrollBarPos(vPos);
		list.getHScrollBar().setScrollBarPos(hPos);
	}
	
	@Override
	public WindowSelectionList resize(int xIn, int yIn, ScreenLocation areaIn) {
		if (xIn != 0 || yIn != 0) {
			int vPos = list.getVScrollBar().getScrollPos();
			int hPos = list.getHScrollBar().getScrollPos();
			
			super.resize(xIn, yIn, areaIn);
			
			list.getVScrollBar().onResizeUpdate(vPos, xIn, yIn, areaIn);
			list.getHScrollBar().onResizeUpdate(hPos, xIn, yIn, areaIn);
		}
		return this;
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		
		if (select != null && list != null) {
			select.setEnabled(list.getCurrentLine() != null && list.getCurrentLine().getStoredObj() != null);
		}
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == 28) { selectCurrentOptionAndClose(); }
		super.keyPressed(typedChar, keyCode);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == select) { selectCurrentOptionAndClose(); }
		if (object == cancel) { close(); }
	}
	
	//-------------------------
	//EGuiSelectionList Methods
	//-------------------------
	
	public WindowSelectionList writeLine() { return writeLine("", 0); }
	public WindowSelectionList writeLine(String text) { return writeLine(text, EColors.white.intVal); }
	public WindowSelectionList writeLine(String text, EColors color) { return writeLine(text, color.intVal); }
	public WindowSelectionList writeLine(String text, int color) {
		if (list != null) { list.addTextLine(text, color); }
		else { toAdd.add(new TrippleBox(text, color, null)); }
		return this;
	}
	
	public WindowSelectionList addOption(String text) { return addOption(text, EColors.green.intVal, text); }
	public WindowSelectionList addOption(String text, Object arg) { return addOption(text, EColors.green.intVal, arg); }
	public WindowSelectionList addOption(String text, EColors color, Object arg) { return addOption(text, color.intVal, arg); }
	public WindowSelectionList addOption(String text, int color, Object arg) {
		if (list != null) { add(text, color, arg); }
		else { toAdd.add(new TrippleBox(text, color, arg)); }
		return this;
	}
	
	protected void selectCurrentOptionAndClose() {
		if (list.getCurrentLine() != null && list.getCurrentLine().getStoredObj() != null) {
			selectedObject = list.getCurrentLine().getStoredObj();
			performAction(null, null);
			close();
		}
	}
	
	private void add(String text, int color, Object arg) {
		TextAreaLine l = new TextAreaLine(list, text, color, arg) {
			@Override
			public void onDoubleClick() {
				if (list.getCurrentLine() != null && list.getCurrentLine().getStoredObj() != null) {
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
		list.addTextLine(l);
	}
	
}
