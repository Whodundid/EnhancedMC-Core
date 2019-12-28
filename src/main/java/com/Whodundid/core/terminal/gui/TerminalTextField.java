package com.Whodundid.core.terminal.gui;

import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiTextField;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class TerminalTextField extends EGuiTextField {

	protected ETerminal term;
	protected boolean isTabCompleting = false;
	
	public TerminalTextField(ETerminal termIn, int xIn, int yIn, int widthIn, int heightIn) {
		super(termIn, xIn, yIn, widthIn, heightIn);
		term = termIn;
		setUseObjectGroupForCursorDraws(true);
		setDrawShadowed(false);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		super.drawObject(mXIn, mYIn, ticks);
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == 28) { //enter
			term.historyLine = 0;
			term.preservedInput = "";
		}
		if (keyCode == 200) { //up
			if (term.historyLine < term.cmdHistory.size()) {
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
		}
		else if (keyCode == 208) { //down
			if (term.historyLine <= term.cmdHistory.size() && term.historyLine > 1) {
				if (term.historyLine == term.cmdHistory.size() || term.lastUsed == 1) { term.historyLine -= 2; }
				else { term.historyLine -= 1; }
				setText(getHistoryLine(term.historyLine));
				term.lastUsed = 0;
			} else if (term.historyLine <= 1) { 
				setText(term.preservedInput);
				term.lastUsed = 2;
			}
		}
		else {
			super.keyPressed(typedChar, keyCode);
			if (term.historyLine <= 0) { term.preservedInput = getText(); }
		}
	}
	
	private String getHistoryLine(int lineNum) {
		if (!term.cmdHistory.isEmpty()) {
			if (lineNum == 0) { return term.cmdHistory.get(term.cmdHistory.size() - 1); }
			else if (lineNum > 0) { return term.cmdHistory.get(term.cmdHistory.size() - (lineNum + 1)); }
		}
		return null;
	}
	
	private void drawTabCompletions(EArrayList<String> completions) {
		
	}
}
