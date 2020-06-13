package com.Whodundid.core.terminal.window.termParts;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.terminal.window.TerminalRCM;
import com.Whodundid.core.util.chatUtil.EChatUtil;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.textArea.TextAreaLine;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.EventType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventFocus;
import net.minecraft.client.gui.GuiScreen;

public class TerminalTextLine extends TextAreaLine {

	ETerminal term;
	
	public TerminalTextLine(ETerminal termIn, String text) { this(termIn, text, EColors.white, null, -1); }
	public TerminalTextLine(ETerminal termIn, String text, int color) { this(termIn, text, color, null, -1); }
	public TerminalTextLine(ETerminal termIn, String text, EColors color) { this(termIn, text, color.c(), null, -1); }
	public TerminalTextLine(ETerminal termIn, String text, EColors color, Object objIn) { this(termIn, text, color.c(), objIn, -1); }
	public TerminalTextLine(ETerminal termIn, String text, int color, Object objIn) { this(termIn, text, color, objIn, -1); }
	public TerminalTextLine(ETerminal termIn, String text, EColors color, Object objIn, int lineNumber) { this(termIn, text, color.c(), objIn, lineNumber); }
	public TerminalTextLine(ETerminal termIn, String text, int color, Object objIn, int lineNumber) {
		super(termIn.getTextArea(), text, color, objIn, lineNumber);
		term = termIn;
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		if (button == 1) {
			EnhancedMC.displayWindow(new TerminalRCM(this), CenterType.cursorCorner);
		}
		else {
			if (checkLinkClick(mXIn, mYIn, button)) { term.writeln("Opening Link..\n", EColors.seafoam); }
			if (term != null && term.getInputField() != null) { term.getInputField().requestFocus(); }
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		//super.keyPressed(typedChar, keyCode);
		if (GuiScreen.isKeyComboCtrlC(keyCode)) {
			String text = getText();
			if (text.startsWith("> ")) { text = text.substring(2); }
			text = EChatUtil.removeFormattingCodes(text);
			text = text.trim();
			GuiScreen.setClipboardString(text);
		}
	}
	
	@Override
	public void onFocusGained(EventFocus eventIn) {
		super.onFocusGained(eventIn);
		if (eventIn.getEventType() == EventType.Mouse) {
			if (eventIn.getActionCode() == 1) {
				EnhancedMC.displayWindow(new TerminalRCM(this), CenterType.cursorCorner);
			}
		}
	}
	
	@Override
	public void onDoubleClick() {
		if (term != null && term.getInputField() != null) {
			String text = getText();
			text = EChatUtil.removeFormattingCodes(text);
			text = text.trim();
			if (text.startsWith("> ")) { text = text.substring(2); }
			term.getInputField().writeText(text);
			term.getInputField().requestFocus();
		}
	}

	public TerminalTextLine setTerminal(ETerminal termIn) { term = termIn; return this; }
	public ETerminal getTerminal() { return term; }
}
