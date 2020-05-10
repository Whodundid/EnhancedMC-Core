package com.Whodundid.core.terminal.gui.termParts;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiTextField;
import com.Whodundid.core.terminal.TerminalCommandHandler;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.gui.TerminalRCM;
import com.Whodundid.core.util.renderUtil.CenterType;

//Author: Hunter Bragg

public class TerminalTextField extends EGuiTextField {

	protected TerminalCommandHandler handler;
	protected ETerminal term;
	protected boolean isTabCompleting = false;
	
	public TerminalTextField(ETerminal termIn, int xIn, int yIn, int widthIn, int heightIn) {
		super(termIn, xIn, yIn, widthIn, heightIn);
		term = termIn;
		handler = EnhancedMC.getTerminalHandler();
		setUseObjectGroupForCursorDraws(true);
		setDrawShadowed(false);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
		if (button == 1) { EnhancedMC.displayWindow(new TerminalRCM(this), CenterType.cursorCorner); }
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == 15) { //tab
			performAction("tab");
		}
		
		if (keyCode == 14) { //backspace
			term.resetTab();
			//if (term.getTab1()) { term.setTab1(false); }
			//term.setTextTabBeing("");
		}
		
		if (keyCode == 28) { //enter
			term.historyLine = 0;
			term.preservedInput = "";
			term.resetTab();
		}
		
		//System.out.println(keyCode);
		if (keyCode == 57) { //space
			term.resetTab();
		}
		
		if (keyCode == 203) { //left
			term.resetTab();
		}
		
		if (keyCode == 205) { //right
			term.resetTab();
		}
		
		if (keyCode == 200) { //up
			if (term.historyLine < handler.cmdHistory.size()) {
				if (term.lastUsed == 0) {
					term.historyLine += 1;
					setText(getHistoryLine(term.historyLine));
					term.historyLine += 1;
				} else {
					setText(getHistoryLine(term.historyLine));
					term.historyLine += 1;
				}
			}
			term.lastUsed = 1;
			
			term.resetTab();
		}
		else if (keyCode == 208) { //down
			if (term.historyLine <= handler.cmdHistory.size() && term.historyLine > 1) {
				if (term.historyLine == handler.cmdHistory.size() || term.lastUsed == 1) { term.historyLine -= 2; }
				else { term.historyLine -= 1; }
				setText(getHistoryLine(term.historyLine));
				term.lastUsed = 0;
			} else if (term.historyLine <= 1) { 
				setText(term.preservedInput);
				term.lastUsed = 2;
			}
			
			term.resetTab();
		}
		else {
			super.keyPressed(typedChar, keyCode);
			if (term.historyLine <= 0) { term.preservedInput = getText(); }
		}
		
		//if (text.isEmpty()) { term.resetTab(); }
	}
	
	public ETerminal getTerminal() { return term; }
	
	private String getHistoryLine(int lineNum) {
		if (!handler.cmdHistory.isEmpty()) {
			if (lineNum == 0) { return handler.cmdHistory.get(handler.cmdHistory.size() - 1); }
			else if (lineNum > 0) { return handler.cmdHistory.get(handler.cmdHistory.size() - (lineNum + 1)); }
		}
		return null;
	}
}
