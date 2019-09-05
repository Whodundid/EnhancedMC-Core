package com.Whodundid.core.enhancedGui.guiObjects;

import com.Whodundid.core.enhancedGui.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.guiObjectUtil.DropDownListEntry;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventFocus;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.FocusType;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.renderUtil.Resources;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;

//Last edited: Jan 2, 2019
//First Added: Sep 30, 2018
//Author: Hunter Bragg

public class EGuiDropDownList extends EnhancedGuiObject {
	
	ArrayList<DropDownListEntry> listContents = new ArrayList();
	DropDownListEntry selectedEntry;
	int entryHeight = 13;
	boolean listOpen = false;
	boolean fixedWidth = false;
	boolean globalAction = false;
	
	public EGuiDropDownList(IEnhancedGuiObject parentIn, int x, int y) {
		init(parentIn, x, y, 75, 14);
	}
	public EGuiDropDownList(IEnhancedGuiObject parentIn, int x, int y, int entryHeightIn) { 
		this(parentIn, x, y, entryHeightIn, false); 
	}
	public EGuiDropDownList(IEnhancedGuiObject parentIn, int x, int y, int entryHeightIn, boolean useGlobalAction) {
		init(parentIn, x, y, width, entryHeightIn);
		entryHeight = entryHeightIn;
		globalAction = useGlobalAction;
	}
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		int color = enabled ? 0xffffff : 0x777777;
		mc.renderEngine.bindTexture(Resources.guiButtonBase);
		drawModalRectWithCustomSizedTexture(startX, startY, 0, 0, width, entryHeight, width, entryHeight);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		if (!hasFocus() && listOpen) { closeList(); }
		if (selectedEntry != null) {
			drawCenteredString(selectedEntry.getDisplayString(), startX + (width / 2), startY + (entryHeight / 4), color);
		}
		if (isEnabled() && checkDraw() && listOpen) {
			mc.renderEngine.bindTexture(Resources.guiButtonSel);
			drawModalRectWithCustomSizedTexture(startX, startY, 0, 0, width, entryHeight, width, entryHeight);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			drawCenteredString("...", startX + (width / 2), startY + (entryHeight / 4), color);
			for (int i = 0; i < listContents.size(); i++) {
				mc.renderEngine.bindTexture(Resources.guiButtonBase);
				drawModalRectWithCustomSizedTexture(startX, startY + (i * entryHeight) + entryHeight, 0, 0, width, entryHeight, width, entryHeight);
				drawCenteredString(listContents.get(i).getDisplayString(), startX + (width / 2), startY + (entryHeight / 4) + (i * entryHeight) + entryHeight, color);
			}
		}
		super.drawObject(mX, mY, ticks);
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		super.mousePressed(mX, mY, button);
		if (hasFocus()) {
			if (listOpen) {	closeList(); }
			else { selectListOption(button); }
		}
	}
	
	@Override
	public void onFocusGained(EventFocus eventIn) {
		if (eventIn.getFocusType().equals(FocusType.MousePress)) {
			if (listOpen) {	selectListOption(eventIn.getActionCode()); }
			else { openList(eventIn.getActionCode()); }
		}
		getTopParent().setFocusLockObject(this);
	}
	
	@Override public void onFocusLost(EventFocus eventin) { closeList(); }
	
	protected void openList(int button) {
		if (checkDraw() && isMouseHover && button == 0) {
			listOpen = true;
			playPressSound();
			int newHeight = entryHeight + (listContents.size() * entryHeight) - 1;
			setDimensions(startX, startY, width, newHeight);
		}
	}
	
	protected void closeList() {
		listOpen = false;
		getTopParent().clearFocusLockObject();
		relinquishFocus();
		setDimensions(startX, startY, width, entryHeight);
		System.out.println("ye");
	}
	
	protected void selectListOption(int button) {
		if (isEnabled() && (mY >= startY + entryHeight)) {
			int relClickPosY = mY - (startY + entryHeight + 1);
			int selectedPos = relClickPosY / entryHeight;
			selectedEntry = listContents.get(selectedPos);
			selectedEntry.runEntryAction();
			playPressSound();
			closeList();
		}
	}
	
	public EGuiDropDownList addListEntry(DropDownListEntry entryIn) {
		listContents.add(entryIn.setEntryID(listContents.size()).setParentList(this).setGlobalActionPresent(globalAction));
		if (listContents.size() == 1) { selectedEntry = entryIn; }
		adjustWidth();
		return this;
	}
	
	public synchronized EGuiDropDownList removeListEntry(DropDownListEntry entryIn) {
		Iterator<DropDownListEntry> it = listContents.iterator();
		while (it.hasNext()) {
			if (it.next().equals(entryIn)) { it.remove(); break; }
		}
		if (listContents.size() == 0) { selectedEntry = null; }
		if (listContents.size() > 0) { selectedEntry = listContents.get(0); }
		if (!fixedWidth) { adjustWidth(); }
		return this;
	}
	
	public synchronized EGuiDropDownList removeListEntry(int entryIdIn) {
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
				String displayString = e.getDisplayString();
				if (displayString.length() > longestString.length()) { longestString = displayString; }
			}
			width = fontRenderer.getStringWidth(longestString) + 6;
			setDimensions(startX, startY, width, height);
		}
	}
	
	public ArrayList<DropDownListEntry> getEntries() { return listContents; }
	public DropDownListEntry getSelectedEntry() { return selectedEntry; }
	public DropDownListEntry getEntryFromObject(Object objIn) {
		for (DropDownListEntry e : listContents) { if (e.getEntryObject().equals(objIn)) { return e; } }
		return null;
	}
	
	public EGuiDropDownList setFixedWidth(boolean val) { fixedWidth = val; return this; }
	public EGuiDropDownList setFixedWidth(boolean val, int newWidth) { fixedWidth = val; setWidth(newWidth); return this; }
	public EGuiDropDownList setWidth(int widthIn) {  setDimensions(startX, startY, widthIn, height); return this; }
	public EGuiDropDownList setSelectedEntry(int entry) { selectedEntry = listContents.get(entry); return this; }
	public EGuiDropDownList setSelectedEntry(DropDownListEntry entry) { return setSelectedEntry(entry, false); }
	public EGuiDropDownList setSelectedEntry(DropDownListEntry entry, boolean addIfNotContains) {
		if (entry != null) {
			if (listContents.contains(entry)) { selectedEntry = entry; }
			else if (addIfNotContains) {
				addListEntry(entry);
				selectedEntry = entry;
			}
		}
		return this;
	}
	
	public void playPressSound() { mc.getSoundHandler().playSound(PositionedSoundRecord.create(Resources.buttonSound, 1.0F)); }
	
	public void runGlobalAction() {}
}
