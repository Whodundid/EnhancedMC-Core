package com.Whodundid.core.debug.terminal.gui;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.EGuiTextArea;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.TextAreaLine;
import com.Whodundid.core.enhancedGui.guiUtil.EObjectGroup;
import com.Whodundid.core.enhancedGui.objectEvents.EventFocus;
import com.Whodundid.core.enhancedGui.objectEvents.EventMouse;
import com.Whodundid.core.enhancedGui.objectEvents.ObjectEvent;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.MouseType;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import java.awt.Color;

public class ETerminal extends WindowParent {

	TerminalTextField inputField;
	EGuiTextArea history;
	boolean init = false;
	int historyLine = 0;
	int lastUsed = 2;
	EArrayList<String> cmdHistory = new EArrayList();
	
	@Override
	public void initGui() {
		setObjectName("EMC Terminal");
		setDimensions(startX, startY, 300, 153);
		setMinimumDims(70, 25);
		setResizeable(true);
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		inputField = new TerminalTextField(this, startX + 2, endY - 14, width - 6, 12);
		history = new EGuiTextArea(this, startX + 1, startY + 1, width - 2, height - 3 - inputField.height);
		
		inputField.setBackgroundColor(0xff000000);
		inputField.setBorderColor(0xff222222);
		inputField.setTextColor(0x00ff00);
		inputField.setMaxStringLength(255);
		
		history.setBackgroundColor(0xff000000);
		history.setBorderColor(0xff222222);
		history.setDrawLineNumbers(true);
		history.setResetDrawn(false);
		
		objectGroup = new EObjectGroup(this);
		objectGroup.addObject(header, history, inputField);
		header.setObjectGroup(objectGroup);
		history.setObjectGroup(objectGroup);
		inputField.setObjectGroup(objectGroup);
		
		addObject(inputField, history);
		
		if (!init) { history.addTextLine("> EMC Terminal v1.0 initialized..", 0xffff00); init = true; }
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		int color = Color.HSBtoRGB(System.currentTimeMillis() % 10000L / 10000.0f, 0.8f, 1f);
		if (header != null) { header.setTitleColor(-color + 0xff222222); }
		drawRect(startX, startY, endX, endY, 0xff000000);
		super.drawObject(mXIn, mYIn, ticks);
	}
	
	@Override
	public void onFocusGained(EventFocus e) {
		//if (e.getFocusType() == FocusType.Transfer) { inputField.requestFocus(); }
	}
	
	@Override
	public void onGroupNotification(ObjectEvent e) {
		if (e instanceof EventMouse) {
			if (((EventMouse) e).getMouseType() == MouseType.Pressed) {
				bringToFront();
			}
			if (((EventMouse) e).getMouseType() == MouseType.Released) {
				if (inputField != null) { inputField.requestFocus(); }
			}
		}
	}
	
	@Override
	public ETerminal resize(int xIn, int yIn, ScreenLocation areaIn) {
		try {
			if (xIn != 0 || yIn != 0) {
				int vPos = history.getVScrollBar().getScrollPos();
				//System.out.println(history.getHScrollBar().getScrollPos() + " " + history.getHScrollBar().getVisibleAmount());
				int hPos = history.getHScrollBar().getScrollPos();
				EArrayList<TextAreaLine> lines = new EArrayList(history.getTextDocument());
				history.clear();
				super.resize(xIn, yIn, areaIn);
				lines.forEach(l -> history.addTextLine(l));
				history.getVScrollBar().setScrollBarPos(vPos);
				history.getHScrollBar().setScrollBarPos(hPos);
			}
		} catch (Exception e) { e.printStackTrace(); }
		return this;
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object == inputField) {
			String cmd = inputField.getText();
			if (!cmd.isEmpty()) {
				try {
					if (!(cmd.equals("clear") || cmd.equals("clr") || cmd.equals("cls"))) {
						history.addTextLine("> " + cmd, 0xffffff);
					}
					cmdHistory.add(cmd);
					EnhancedMC.getTerminal().executeCommand(this, cmd);
					inputField.setText("");
					history.getVScrollBar().setScrollBarPos(history.getVScrollBar().getHighVal());
				} catch (Exception e) { e.printStackTrace(); }
			}
		}
	}
	
	public ETerminal writeln(String msgIn) { return writeln(msgIn, 0xffffff); }
	public ETerminal writeln(String msgIn, int colorIn) {
		history.addTextLine(msgIn, colorIn).setObjectGroup(objectGroup);
		return this;
	}
	
	public ETerminal error(String msgIn) {
		history.addTextLine(msgIn, 0xff5555).setObjectGroup(objectGroup);
		return this;
	}
	
	public int getLastUsed() { return lastUsed; }
	public int getHisLine() { return historyLine; }
	public EArrayList<String> getHistory() { return cmdHistory; }
	
	public ETerminal setLastUsed(int in) { lastUsed = in; return this; }
	public ETerminal setHistoryLine(int in) { historyLine = in; return this; }
	public ETerminal clear() { history.clear(); return this; }
	public ETerminal clearHistory() { cmdHistory.clear(); return this; }
}
