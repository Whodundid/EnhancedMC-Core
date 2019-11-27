package com.Whodundid.core.debug.console.gui;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjectUtil.TextAreaLine;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiTextArea;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiTextField;
import com.Whodundid.core.enhancedGui.types.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class EConsole extends WindowParent {

	EGuiTextField inputField;
	EGuiTextArea history;
	boolean init = false;
	EArrayList<String> cmdHistory = new EArrayList();
	
	@Override
	public void initGui() {
		setObjectName("EMC Terminal");
		centerObjectWithSize(300, 150);
		setMinimumDims(60, 25);
		setResizeable(true);
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		inputField = new EGuiTextField(this, startX + 2, endY - 14, width - 6, 12);
		history = new EGuiTextArea(this, startX + 1, startY + 1, width - 2, height - 3 - inputField.height);
		
		inputField.setBackgroundColor(0xff000000);
		inputField.setBorderColor(0xff222222);
		inputField.setTextColor(0x00ff00);
		//System.out.println(0x00ff00);
		history.setBackgroundColor(0xff000000);
		history.setBorderColor(0xff222222);
		history.setDrawLineNumbers(true);
		history.setResetDrawn(false);
		
		addObject(inputField, history);
		
		if (!init) { history.addTextLine("> EMC Terminal v1.0 initialized..", 0xffffff); init = true; }
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		drawRect(startX, startY, endX, endY, 0xff000000);
		super.drawObject(mXIn, mYIn, ticks);
	}
	
	@Override
	public EConsole resize(int xIn, int yIn, ScreenLocation areaIn) {
		try {
			if (xIn != 0 || yIn != 0) {
				int vPos = history.getVScrollBar().getScrollPos();
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
					EnhancedMC.getConsole().executeCommand(this, cmd);
					inputField.setText("");
					history.getVScrollBar().setScrollBarPos(history.getVScrollBar().getHighVal());
				} catch (Exception e) { e.printStackTrace(); }
			}
		}
	}
	
	public EConsole writeln(String msgIn) { return writeln(msgIn, 0xffffff); }
	public EConsole writeln(String msgIn, int colorIn) {
		history.addTextLine("" + msgIn, colorIn);
		return this;
	}
	
	public EConsole clear() {
		history.clear();
		return this;
	}
	
	public EConsole clearHistory() {
		cmdHistory.clear();
		return this;
	}
}
