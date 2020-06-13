package com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.dropDownList;

import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowTypes.WindowObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.FocusType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventFocus;
import java.util.Iterator;

//Author: Hunter Bragg

public class WindowDropDownList extends WindowObject {
	
	EArrayList<DropDownListEntry> listContents = new EArrayList();
	DropDownListEntry selectedEntry;
	int entryHeight = 17;
	boolean listOpen = false;
	boolean fixedWidth = false;
	boolean globalAction = false;
	boolean drawTop = true;
	boolean alwaysOpen = false;
	boolean drawHighlight = true;
	int selectedColor = EColors.steel.intVal;
	int borderColor = EColors.black.intVal;
	int backColor = EColors.dgray.intVal;
	
	public WindowDropDownList(IWindowObject parentIn, int x, int y) { init(parentIn, x, y, 75, entryHeight); }
	public WindowDropDownList(IWindowObject parentIn, int x, int y, int entryHeightIn) { this(parentIn, x, y, entryHeightIn, false); }
	public WindowDropDownList(IWindowObject parentIn, int x, int y, int entryHeightIn, boolean useGlobalAction) {
		init(parentIn, x, y, width, entryHeightIn);
		entryHeight = entryHeightIn;
		globalAction = useGlobalAction;
	}
	
	@Override
	public void drawObject(int mX, int mY) {
		drawRect(borderColor);
		//drawRect(backColor, 1);
		
		if (!hasFocus() && listOpen && !alwaysOpen) { closeList(); }
		
		if (selectedEntry != null) {
			//drawStringC(selectedEntry, startX + (width / 2), startY + (entryHeight / 3), selectedEntry.getColor());
		}
		
		if (isEnabled() && (alwaysOpen || listOpen)) {
			
			int offset = 0;
			
			if (drawTop) {
				drawRect(borderColor);
				drawRect(selectedColor, 1);
				drawStringC("...", midX, startY + (entryHeight / 4), EColors.lgray);
			}
			else { offset = -entryHeight; }
			
			for (int i = 0; i < listContents.size(); i++) {
				DropDownListEntry entry = listContents.get(i);
				int yPos = startY + (i * entryHeight) + entryHeight + offset;
				
				drawRect(startX, yPos, endX, yPos + entryHeight, borderColor);
				drawRect(startX + 1, yPos + 1, endX - 1, yPos + entryHeight, backColor);
				
				if (drawHighlight) {
					if (isMouseInside(mX, mY) && mY >= yPos && mY < yPos + entryHeight) {
						drawRect(startX + 2, yPos + 2, endX - 2, yPos + entryHeight - 1, 0x44adadad);
					}
				}
				
				drawString(entry, startX + 5, yPos + (entryHeight / 3), entry.getColor());
			}
			
			int endPos = startY + (listContents.size() * entryHeight) + entryHeight + offset;
			drawRect(startX, endPos, endX, endPos + 1, borderColor);
		}
		
		super.drawObject(mX, mY);
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {

		if (button == 0) {
			if (listOpen) {	selectListOption(); }
			else { openList(); }
		}
		
		super.mousePressed(mX, mY, button);
	}
	
	@Override
	public void onFocusGained(EventFocus eventIn) {
		if (eventIn.getFocusType().equals(FocusType.MousePress)) {
			if (eventIn.getActionCode() == 0) {
				if (listOpen) {	selectListOption(); }
				else { openList(); }
			}
		}
	}
	
	@Override public void onFocusLost(EventFocus eventin) { closeList(); }
	
	protected void openList() {
		listOpen = true;
		int newHeight = entryHeight + (listContents.size() * entryHeight) - 1;
		setDimensions(startX, startY, width, newHeight);
	}
	
	protected void closeList() {
		listOpen = false;
		setDimensions(startX, startY, width, entryHeight);
	}
	
	protected void selectListOption() {
		if (isEnabled()) {
			
			int offset = drawTop ? entryHeight : 0;
			
			if (mY >= startY + offset) {
				int relClickPosY = mY - (startY + offset);
				int selectedPos = relClickPosY / entryHeight;
				selectedEntry = listContents.get(selectedPos);
				selectedEntry.runEntryAction();
				WindowButton.playPressSound();
				closeList();
			}
			else {
				closeList();
			}
		}
	}
	
	public WindowDropDownList addEntry(String title) { return addEntry(title, EColors.lgray, null); }
	public WindowDropDownList addEntry(String title, EColors colorIn, Object objIn) { return addEntry(new DropDownListEntry(title, colorIn.intVal, objIn)); }
	public WindowDropDownList addEntry(String title, int colorIn, Object objIn) { return addEntry(new DropDownListEntry(title, colorIn, objIn)); }
	public WindowDropDownList addEntry(DropDownListEntry entryIn) {
		listContents.add(entryIn.setEntryID(listContents.size()).setParentList(this).setGlobalActionPresent(globalAction));
		if (listContents.size() == 1) { selectedEntry = entryIn; }
		adjustWidth();
		return this;
	}
	
	public synchronized WindowDropDownList removeEntry(DropDownListEntry entryIn) {
		Iterator<DropDownListEntry> it = listContents.iterator();
		while (it.hasNext()) {
			if (it.next().equals(entryIn)) { it.remove(); break; }
		}
		if (listContents.size() == 0) { selectedEntry = null; }
		if (listContents.size() > 0) { selectedEntry = listContents.get(0); }
		if (!fixedWidth) { adjustWidth(); }
		return this;
	}
	
	public synchronized WindowDropDownList removeEntry(int entryIdIn) {
		Iterator<DropDownListEntry> it = listContents.iterator();
		int i = 0;
		while (it.hasNext()) {
			if (i == entryIdIn) { it.remove(); break; }
			i++;
		}
		if (listContents.size() == 0) { selectedEntry = null; }
		if (listContents.size() > 0) { selectedEntry = listContents.get(0); }
		if (!fixedWidth) { adjustWidth(); }
		return this;
	}
	
	private void adjustWidth() {
		if (!fixedWidth) {
			
			String longestString = "";
			for (DropDownListEntry e : listContents) {
				String displayString = e.getText();
				if (displayString.length() > longestString.length()) { longestString = displayString; }
			}
			
			width = mc.fontRendererObj.getStringWidth(longestString) + 10;
			int h = height;
			
			if (alwaysOpen) {
				h = startY + ((listContents.size() - 1) * entryHeight) - 2;
			}
			
			setDimensions(startX, startY, width, h);
		}
	}
	
	public DropDownListEntry getHoveringEntry(int mXIn, int mYIn) {
		if (isMouseInside(mXIn, mYIn)) {
			
			int offset = drawTop ? entryHeight : 0;
			
			if (mY >= startY + offset) {
				int relClickPosY = mY - (startY + offset);
				int selectedPos = relClickPosY / entryHeight;
				if (selectedPos <= listContents.size() - 1) {
					return listContents.get(selectedPos);
				}
			}
			
		}
		return null;
	}
	
	public EArrayList<DropDownListEntry> getEntries() { return listContents; }
	public DropDownListEntry getSelectedEntry() { return selectedEntry; }
	public DropDownListEntry getEntryFromObject(Object objIn) {
		for (DropDownListEntry e : listContents) { if (e.getEntryObject().equals(objIn)) { return e; } }
		return null;
	}
	
	public WindowDropDownList setDrawHighlight(boolean val) { drawHighlight = val; return this; }
	public WindowDropDownList setAlwaysOpen(boolean val) { alwaysOpen = val; if (val) { openList(); } return this; }
	public WindowDropDownList setBorderColor(EColors colorIn) { borderColor = colorIn.intVal; return this; }
	public WindowDropDownList setBackColor(EColors colorIn) { backColor = colorIn.intVal; return this; }
	public WindowDropDownList setSelectedColor(EColors colorIn) { selectedColor = colorIn.intVal; return this; }
	public WindowDropDownList setBorderColor(int colorIn) { borderColor = colorIn; return this; }
	public WindowDropDownList setBackColor(int colorIn) { backColor = colorIn; return this; }
	public WindowDropDownList setSelectedColor(int colorIn) { selectedColor = colorIn; return this; }
	public WindowDropDownList setDrawTop(boolean val) { drawTop = val; return this; }
	public WindowDropDownList setFixedWidth(boolean val) { fixedWidth = val; return this; }
	public WindowDropDownList setFixedWidth(boolean val, int newWidth) { fixedWidth = val; setWidth(newWidth); return this; }
	public WindowDropDownList setWidth(int widthIn) {  setDimensions(startX, startY, widthIn, height); return this; }
	public WindowDropDownList setSelectedEntry(int entry) { selectedEntry = listContents.get(entry); return this; }
	public WindowDropDownList setSelectedEntry(DropDownListEntry entry) { return setSelectedEntry(entry, false); }
	
	public WindowDropDownList setSelectedEntry(DropDownListEntry entry, boolean addIfNotContains) {
		if (entry != null) {
			if (listContents.contains(entry)) { selectedEntry = entry; }
			else if (addIfNotContains) {
				addEntry(entry);
				selectedEntry = entry;
			}
		}
		return this;
	}
	
	public void runGlobalAction() {}
	
}
