package com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiTextField;
import com.Whodundid.core.enhancedGui.objectEvents.EventFocus;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.ObjectModifyType;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatAllowedCharacters;

//Author: Hunter Bragg

public class TextAreaLine<obj> extends EGuiTextField {
	
	protected EGuiTextArea parentTextArea;
	protected EGuiLabel numberLabel;
	public int lineNumberColor = 0xb2b2b2;
	protected int lineNumber = 0;
	protected int drawnLineNumber = 0;
	protected int lineNumberWidth = 0;
	protected int maxVisibleLength = 3;
	protected boolean textRecentlyEntered = false;
	protected boolean deleting = false;
	protected boolean creating = false;
	protected boolean highlighted = false;
	protected boolean lineEquals = false, drawCursor = false;
	protected long startTime = 0l;
	protected long doubleClickTimer = 0l;
	protected long doubleClickThreshold = 500l;
	protected boolean clicked = false;
	protected obj storedObj;
	
	public TextAreaLine(EGuiTextArea textAreaIn) { this(textAreaIn, "", 0xffffff, null, -1); }
	public TextAreaLine(EGuiTextArea textAreaIn, String textIn) { this(textAreaIn, textIn, 0xffffff, null, -1); }
	public TextAreaLine(EGuiTextArea textAreaIn, String textIn, int colorIn) { this(textAreaIn, textIn, colorIn, null, -1); }
	public TextAreaLine(EGuiTextArea textAreaIn, String textIn, obj objectIn) { this(textAreaIn, textIn, 0xffffff, objectIn, -1); }
	public TextAreaLine(EGuiTextArea textAreaIn, String textIn, int colorIn, obj objectIn) { this(textAreaIn, textIn, colorIn, objectIn, -1); }
	public TextAreaLine(EGuiTextArea textAreaIn, String textIn, int colorIn, obj objectIn, int lineNumberIn) {
		init(textAreaIn, 0, 0, 0, 0);
		setMaxStringLength(1500);
		parent = textAreaIn;
		parentTextArea = textAreaIn;
		lineNumber = lineNumberIn;
		setText(textIn);
		mainDrawColor = colorIn;
		setStoredObj(objectIn);
		setDrawShadowed(false);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		updateBeforeNextDraw(mXIn, mYIn);
		updateValues();
		
		if (highlighted || lineEquals) {
			//drawRect(startX - (parentTextArea.hasLineNumbers() ? 3 : 2), startY, parentTextArea.endX - 1, endY, 0x20909090);
			drawRect(startX - 2, startY + 2, parentTextArea.endX - 1, endY, 0x20909090);
		}
		
		if (parentTextArea.hasLineNumbers()) { drawLineNumber(); }
		drawText();
		
		if (checkDraw()) {
			for (IEnhancedGuiObject o : guiObjects) {
				if (o.checkDraw()) {
    				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    	        	o.drawObject(mXIn, mYIn, ticks);
    			}
			}
		}
	}
	
	@Override public void updateCursorImage() {}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (hasFocus()) {
			parentTextArea.keyPressed(typedChar, keyCode);
			//if (drawnLineNumber < 0) { parentTextArea.makeLineNumberDrawn(lineNumber); }
			if (GuiScreen.isKeyComboCtrlA(keyCode)) { setCursorPositionEnd(); }
			else if (GuiScreen.isKeyComboCtrlC(keyCode)) { GuiScreen.setClipboardString(getSelectedText()); }
			else if (GuiScreen.isKeyComboCtrlV(keyCode) && isEnabled()) { writeText(GuiScreen.getClipboardString()); checkLine(); }
			else if (GuiScreen.isKeyComboCtrlX(keyCode)) {
				GuiScreen.setClipboardString(getSelectedText());
				//if (isEnabled() && parentTextArea.isEditable()) {
				//	writeText("");
				//	checkLine();
				//}
			} else {
				switch (keyCode) {
				case 28: //enter
					if (parentTextArea.isEditable()) {
						if (!creating) {
							creating = true;
							//TextAreaLine newLine = parentTextArea.insertTextLine(lineNumber + 1, getSelectionEnd() == text.length(), cursorPosition);
							//if (newLine != null) { newLine.requestFocus(); }
						}
					}
					break;
				case 200: //up
					//parentTextArea.moveCurrentLineUp();
					break;
				case 208: //down
					//parentTextArea.moveCurrentLineDown();
					break;
				case 14: //backspace
					if (parentTextArea.isEditable()) {
						if (getText().isEmpty()) {
							if (!deleting) {
								deleting = true;
								//if (parentTextArea.getTextLineWithLineNumber(lineNumber - 1) != null) { parentTextArea.removeTextLine(this); }
								//else { deleting = false; }
							}
						} else if (cursorPosition == 0) {
							//if (!deleting) {
							//	deleting = true;
							//	parentTextArea.deleteLineAndMoveTextUp(this);
							//}
						}
						if (GuiScreen.isCtrlKeyDown()) {
							if (isEnabled()) { deleteWords(-1); }
						} else if (isEnabled()) { deleteFromCursor(-1); }
						startTextTimer();
						checkLine();
					}
					break;
				case 199: //home
					if (GuiScreen.isShiftKeyDown()) { setSelectionPos(0); }
					else { setCursorPositionZero(); }
					break;
				case 203: //left
					if (GuiScreen.isShiftKeyDown()) {
						if (GuiScreen.isCtrlKeyDown()) { setSelectionPos(getNthWordFromPos(-1, getSelectionEnd())); }
						else { setSelectionPos(getSelectionEnd() - 1); }
					} else if (GuiScreen.isCtrlKeyDown()) { setCursorPosition(getNthWordFromCursor(-1)); }
					else { moveCursorBy(-1); }
					startTextTimer();
					break;
				case 205: //right
					if (GuiScreen.isShiftKeyDown()) {
						if (GuiScreen.isCtrlKeyDown()) { setSelectionPos(getNthWordFromPos(1, getSelectionEnd())); }
						else { setSelectionPos(getSelectionEnd() + 1); }
					} else if (GuiScreen.isCtrlKeyDown()) { setCursorPosition(getNthWordFromCursor(1)); }
					else { moveCursorBy(1); }
					startTextTimer();
					break;
				case 207: //end
					if (GuiScreen.isShiftKeyDown()) { setSelectionPos(text.length()); }
					else { setCursorPositionEnd(); }
					break;
				case 211: //delete
					if (parentTextArea.isEditable()) {
						if (GuiScreen.isCtrlKeyDown()) {
							if (isEnabled()) { deleteWords(1); startTextTimer(); }
						} else if (isEnabled()) { deleteFromCursor(1); startTextTimer(); }
						checkLine();
					}
					break;
				default:
					if (parentTextArea.isEditable()) {
						if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
							if (isEnabled()) {
								writeText(Character.toString(typedChar));
								startTextTimer();
								checkLine();
							}
						}
					}
				}
			}
		}
	}
	
	@Override 
	public void mousePressed(int mX, int mY, int button) {
		try {
			if (isMouseOver(mX, mY)) { requestFocus(); }
			if (button == 0) {
				if (clicked && System.currentTimeMillis() - doubleClickTimer < doubleClickThreshold) { onDoubleClick(); clicked = false; doubleClickTimer = 0l; }
				if (!clicked) { clicked = true; doubleClickTimer = System.currentTimeMillis(); }
				if (isResizeable() && !getEdgeAreaMouseIsOn().equals(ScreenLocation.out)) {
					getTopParent().setModifyingObject(this, ObjectModifyType.Resize);
					getTopParent().setResizingDir(getEdgeAreaMouseIsOn());
					getTopParent().setModifyMousePos(mX, mY);
				}
				if (hasFocus()) {
					int i = mX - startX + 2;
					String s = fontRenderer.trimStringToWidth(text.substring(lineScrollOffset), getWidth());
					setCursorPosition(fontRenderer.trimStringToWidth(s, i).length() + lineScrollOffset);
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
		parentTextArea.mousePressed(mX, mY, button);
	}
	
	@Override
	public void mouseScrolled(int change) {
		//parentTextArea.mouseScrolled(change);
	}
	
	@Override
	public void onFocusLost(EventFocus eventIn) {
		super.onFocusLost(eventIn);
		creating = false;
	}
	
	@Override
	public void onFocusGained(EventFocus eventIn) {
		parentTextArea.setSelectedLine(this);
		super.onFocusGained(eventIn);
	}
	
	protected void checkLine() {
		//TextAreaLine l = parentTextArea.updateAndGetLongestLine();
		//if (l.equals(this)) {
		//	if (fontRenderer.getStringWidth(text.substring(parentTextArea.getCurrentHorizontalPos())) > width - maxVisibleLength) {
		//		parentTextArea.makeLineNumberEndDrawn(lineNumber);
		//	}
		//}
	}
	
	protected void updateValues() {
		if (clicked && System.currentTimeMillis() - doubleClickTimer >= doubleClickThreshold) { clicked = false; doubleClickTimer = 0l; }
		if (parentTextArea != null && parentTextArea.getCurrentLine() != null) {
			lineEquals = parentTextArea.getCurrentLine().equals(this);
			drawCursor = parentTextArea.isEditable() && lineEquals && EnhancedMC.updateCounter / 20 % 2 == 0;
		}
	}
	
	private void startTextTimer() {
		startTime = System.currentTimeMillis();
		textRecentlyEntered = true;
	}
	
	protected void drawLineNumber() {
		//drawStringWithShadow("" + lineNumber, parentTextArea.startX + parentTextArea.getLineNumberSeparatorPos() - (lineNumberWidth + 2), startY + 2, lineNumberColor);
	}
	
	protected void drawText() {
		int j = cursorPosition - lineScrollOffset;
		int k = selectionEnd - lineScrollOffset;
		String s = fontRenderer.trimStringToWidth(text.substring(lineScrollOffset), getWidth());
		
		int textLength = fontRenderer.getStringWidth(text);
		if (textLength > width) {
			
			
		} else {
			if (lineEquals && (textRecentlyEntered || drawCursor)) {
				int textCursorPosLength = fontRenderer.getStringWidth(text.substring(0, cursorPosition)); //this is not finished -- does not check for horizontal position
				drawRect(startX + textCursorPosLength - 1, startY + 1, startX + textCursorPosLength, endY, 0xffb2b2b2);
			}
			//drawStringWithShadow(text, startX, startY + 2, enabledColor);
		}
		if (drawShadowed) { drawStringWithShadow(text, startX, startY + 2, mainDrawColor); }
		else { drawString(text, startX, startY + 2, mainDrawColor); }
	}
	
	public TextAreaLine incrementLineNumber() { setLineNumber(lineNumber + 1); return this; }
	public TextAreaLine decrementLineNumber() { setLineNumber(lineNumber - 1); return this; }
	public TextAreaLine setHighlighted(boolean val) { highlighted = val; return this; }
	public TextAreaLine setStoredObj(obj objectIn) { storedObj = objectIn; return this; }
	public TextAreaLine setLineNumber(int numberIn) { lineNumber = numberIn; lineNumberWidth = fontRenderer.getStringWidth(String.valueOf(lineNumber)); return this; }
	public TextAreaLine setLineNumberColor(int colorIn) { lineNumberColor = colorIn; return this; }
	public TextAreaLine setDrawnLineNumber(int numberIn) { drawnLineNumber = numberIn; return this; }
	public TextAreaLine indent() { setText("    " + getText()); return this; }
	public TextAreaLine setDoubleClickThreshold(long timeIn) { doubleClickThreshold = timeIn; return this; }
	
	public int getDrawnLineNumber() { return drawnLineNumber; }
	public int getLineNumber() { return lineNumber; }
	public obj getStoredObj() { return storedObj; }
	public long getDoubleClickThreshold() { return doubleClickThreshold; }
	public void onDoubleClick() {}
	
	@Override public String toString() { return "[" + lineNumber + ": " + getText() + "]"; }
}
