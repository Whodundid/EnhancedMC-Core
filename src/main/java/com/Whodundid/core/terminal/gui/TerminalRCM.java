package com.Whodundid.core.terminal.gui;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjects.utilityObjects.EGuiRightClickMenu;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.terminal.TerminalCommandHandler;
import com.Whodundid.core.terminal.gui.termParts.TerminalTextField;
import com.Whodundid.core.terminal.gui.termParts.TerminalTextLine;
import com.Whodundid.core.util.chatUtil.EChatUtil;
import net.minecraft.client.gui.GuiScreen;

public class TerminalRCM extends EGuiRightClickMenu {

	ETerminal term;
	TerminalTextLine line;
	TerminalTextField inputField;
	
	public TerminalRCM(ETerminal termIn) {
		super();
		term = termIn;
		build();
		
		//addOption("Search text..");
		addOption("Clear");
		addOption("Clear History");
		addOption("Paste");
		//addOption("Options", EMCResources.guiSettingsButton);
	}
	
	public TerminalRCM(TerminalTextLine lineIn) {
		super();
		term = lineIn.getTerminal();
		line = lineIn;
		build();
		
		addOption("Copy");
		//addOption("Clear");
		//addOption("Clear History");
		//addOption("Options", EMCResources.guiSettingsButton);
	}
	
	public TerminalRCM(TerminalTextField fieldIn) {
		super();
		term = fieldIn.getTerminal();
		inputField = fieldIn;
		build();
		
		addOption("Copy");
		addOption("Paste");
		//addOption("Clear");
		//addOption("Clear History");
		//addOption("Options", EMCResources.guiSettingsButton);
	}
	
	private void build() {
		setRunActionOnPress(true);
		setActionReciever(this);
		
		setTitle("Terminal");
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == this) {
			switch ((String) getSelectedObject()) {
			case "Copy": copy(); break;
			case "Paste": paste(); break;
			case "Search text..": search(); break;
			case "Clear": clear(); break;
			case "Clear History": clearHistory(); break;
			case "Options": openOptions(); break;
			}
		}
	}
	
	private void copy() {
		//if it was for a textfield
		if (inputField != null) {
			String text = inputField.getText();
			text = EChatUtil.removeFormattingCodes(text);
			text = text.trim();
			GuiScreen.setClipboardString(text);
		}
		//if it was for a textline
		else if (line != null) {
			String text = line.getText();
			if (text.startsWith("> ")) { text = text.substring(2); }
			text = EChatUtil.removeFormattingCodes(text);
			text = text.trim();
			GuiScreen.setClipboardString(text);
		}
	}
	
	private void paste() {
		if (term != null && term.getInputField() != null && GuiScreen.getClipboardString() != null) {
			term.getInputField().writeText(GuiScreen.getClipboardString());
		}
	}
	
	private void search() {
		
	}
	
	private void clear() {
		term.clear();
		term.clear();
	}
	
	private void clearHistory() {
		TerminalCommandHandler.cmdHistory.clear();
		term.writeln("Terminal history cleared..", 0x55ff55);
	}
	
	private void openOptions() {
		EnhancedMC.displayWindow(new TerminalOptions());
	}
	
	@Override public boolean isDebugWindow() { return true; }
}
