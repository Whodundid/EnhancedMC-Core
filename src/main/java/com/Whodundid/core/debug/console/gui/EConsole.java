package com.Whodundid.core.debug.console.gui;

import com.Whodundid.core.enhancedGui.guiObjects.EGuiTextArea;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiTextField;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;

public class EConsole extends WindowParent {

	EGuiTextField inputField;
	EGuiTextArea history;
	
	@Override
	public void initGui() {
		setObjectName("EMC Settings");
		centerObjectWithSize(300, 150);
		setMaximumDims(60, 25);
		setResizeable(true);
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		inputField = new EGuiTextField(this, startX + 1, endY - 13, width - 2, 12);
		history = new EGuiTextArea(this, startX + 1, startY + 1, width - 2, height - 2 - inputField.height);
		
		history.setDrawLineNumbers(true);
		
		addObject(inputField, history);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		
	}
	
	public EConsole write(String msgIn) { return write(msgIn, 0xffffff); }
	public EConsole write(String msgIn, int colorIn) {
		return this;
	}
	
	public EConsole writeln(String msgIn) { return writeln(msgIn, 0xffffff); }
	public EConsole writeln(String msgIn, int colorIn) {
		return this;
	}
}
