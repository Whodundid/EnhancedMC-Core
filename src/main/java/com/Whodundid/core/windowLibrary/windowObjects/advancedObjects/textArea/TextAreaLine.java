package com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.textArea;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.chatUtil.EChatUtil;
import com.Whodundid.core.util.guiUtil.GuiOpener;
import com.Whodundid.core.util.renderUtil.CursorHelper;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.TrippleBox;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowTextField;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowLabel;
import com.Whodundid.core.windowLibrary.windowObjects.windows.LinkConfirmationWindow;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowParent;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.EventType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.MouseType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventFocus;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventMouse;
import java.io.File;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatAllowedCharacters;

//Author: Hunter Bragg

public class TextAreaLine<obj> extends WindowTextField {
	
	protected WindowTextArea parentTextArea;
	protected WindowLabel numberLabel;
	protected IWindowObject focusRequester;
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
	protected String linkText = "";
	protected boolean webLink;
	protected Object linkObject;
	
	public TextAreaLine(WindowTextArea textAreaIn) { this(textAreaIn, "", 0xffffff, null, -1); }
	public TextAreaLine(WindowTextArea textAreaIn, String textIn) { this(textAreaIn, textIn, 0xffffff, null, -1); }
	public TextAreaLine(WindowTextArea textAreaIn, String textIn, int colorIn) { this(textAreaIn, textIn, colorIn, null, -1); }
	public TextAreaLine(WindowTextArea textAreaIn, String textIn, obj objectIn) { this(textAreaIn, textIn, 0xffffff, objectIn, -1); }
	public TextAreaLine(WindowTextArea textAreaIn, String textIn, int colorIn, obj objectIn) { this(textAreaIn, textIn, colorIn, objectIn, -1); }
	public TextAreaLine(WindowTextArea textAreaIn, String textIn, int colorIn, obj objectIn, int lineNumberIn) {
		init(textAreaIn, 0, 0, 0, 0);
		setMaxStringLength(1500);
		parent = textAreaIn;
		parentTextArea = textAreaIn;
		lineNumber = lineNumberIn;
		setText(textIn);
		textColor = colorIn;
		setStoredObj(objectIn);
		setDrawShadowed(false);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		updateBeforeNextDraw(mXIn, mYIn);
		updateValues();
		boolean current = parentTextArea.getCurrentLine() == this;
		
		if (current && parentTextArea.getDrawLineHighlight()) {
			drawRect(startX - 2, startY + 1, parentTextArea.endX - 1, endY, 0x39909090);
		}
		
		if (parentTextArea.hasLineNumbers()) { drawLineNumber(); }
		drawText();
		
		if (textRecentlyEntered == true) {
			if (System.currentTimeMillis() - startTime >= 600) {
				startTime = 0l;
				textRecentlyEntered = false;
			}
		}
		
		if (checkDraw()) {
			for (IWindowObject o : windowObjects) {
				if (o.checkDraw()) {
    				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    	        	o.drawObject(mXIn, mYIn);
    			}
			}
		}
		
		if (parentTextArea.isEditable()) {
			if (clickStartPos != -1) {
				int i = mX - startX + 2;
				int cursorPos = mc.fontRendererObj.trimStringToWidth(text, i).length();
				setSelectionPos(cursorPos);
			}
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (hasFocus()) {
			parentTextArea.keyPressed(typedChar, keyCode);
			//if (drawnLineNumber < 0) { parentTextArea.makeLineNumberDrawn(lineNumber); }
			if (GuiScreen.isKeyComboCtrlA(keyCode)) { setCursorPositionEnd(); }
			else if (GuiScreen.isKeyComboCtrlC(keyCode)) { GuiScreen.setClipboardString(getSelectedText()); }
			else if (GuiScreen.isKeyComboCtrlV(keyCode) && isEnabled()) { writeText(GuiScreen.getClipboardString()); }
			else if (GuiScreen.isKeyComboCtrlX(keyCode)) {
				GuiScreen.setClipboardString(getSelectedText());
				if (isEnabled() && parentTextArea.isEditable()) {
					writeText("");
				}
			}
			else {
				switch (keyCode) {
				case 28: //enter
					if (parentTextArea.isEditable()) {
						parentTextArea.createNewLineAfter(this);
						//TextAreaLine newLine = parentTextArea.insertTextLine(text.substring(getSelectionEnd()), lineNumber + 1);
						//if (newLine != null) { newLine.requestFocus(); }
					}
					break;
				case 200: //up
					parentTextArea.selectPreviousLine(this, getCursorPosition());
					break;
				case 208: //down
					parentTextArea.selectNextLine(this, getCursorPosition());
					break;
				case 14: //backspace
					if (parentTextArea.isEditable()) {
						if (getText().isEmpty() || cursorPosition == 0) {
							parentTextArea.deleteLineAndAddPrevious(this);
						}
						else if (GuiScreen.isCtrlKeyDown()) {
							if (isEnabled()) { deleteWords(-1); }
						}
						else if (isEnabled()) { deleteFromCursor(-1); }
						startTextTimer();
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
					}
					else if (GuiScreen.isCtrlKeyDown()) { setCursorPosition(getNthWordFromCursor(-1)); }
					else { moveCursorBy(-1); }
					startTextTimer();
					break;
				case 205: //right
					if (GuiScreen.isShiftKeyDown()) {
						if (GuiScreen.isCtrlKeyDown()) { setSelectionPos(getNthWordFromPos(1, getSelectionEnd())); }
						else { setSelectionPos(getSelectionEnd() + 1); }
					}
					else if (GuiScreen.isCtrlKeyDown()) { setCursorPosition(getNthWordFromCursor(1)); }
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
						}
						else if (isEnabled()) { deleteFromCursor(1); startTextTimer(); }
					}
					break;
				default:
					if (parentTextArea.isEditable()) {
						if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
							if (isEnabled()) {
								writeText(Character.toString(typedChar));
								startTextTimer();
							}
						}
					}
				} //switch
				
			}
		}
	}
	
	@Override 
	public void mousePressed(int mX, int mY, int button) {
		postEvent(new EventMouse(this, mX, mY, button, MouseType.Pressed));
		try {
			if (isMouseOver(mX, mY)) { EUtil.ifNotNullDo(getWindowParent(), w -> w.bringToFront()); }
			if (button == 0) {
				if (isResizeable() && !getEdgeAreaMouseIsOn().equals(ScreenLocation.out)) {
					getTopParent().setModifyingObject(this, ObjectModifyType.Resize);
					getTopParent().setResizingDir(getEdgeAreaMouseIsOn());
					getTopParent().setModifyMousePos(mX, mY);
				}
				if (parentTextArea.isEditable() && hasFocus()) {
					int i = mX - startX + 2;
					int cursorPos = mc.fontRendererObj.trimStringToWidth(text, i).length();
					setCursorPosition(cursorPos);
					
					if (clickStartPos == -1) { clickStartPos = cursorPos; }
				}
				
				checkLinkClick(mX, mY, button);
			}
		}
		catch (Exception e) { e.printStackTrace(); }
		
		parentTextArea.mousePressed(mX, mY, button);
	}
	
	@Override
	public void onFocusLost(EventFocus eventIn) {
		clickStartPos = -1;
		super.onFocusLost(eventIn);
	}
	
	/** Prevent cursor updates. */
	@Override public void updateCursorImage() {}
	
	@Override
	public void mouseEntered(int mXIn, int mYIn) {
		super.mouseEntered(mXIn, mYIn);
		if (parentTextArea.isEditable()) { CursorHelper.setCursor(EMCResources.cursorIBeam); }
	}
	
	@Override
	public void mouseExited(int mXIn, int mYIn) {
		super.mouseExited(mXIn, mYIn);
		IWindowObject over = getTopParent().getHighestZObjectUnderMouse();
		boolean inside = over != parentTextArea || !parentTextArea.isChild(over);
		if (parentTextArea.isEditable() && !inside) { CursorHelper.reset(); }
	}
	
	@Override
	public void onFocusGained(EventFocus eventIn) {
		parentTextArea.setSelectedLine(this);
		
		super.onFocusGained(eventIn);
		if (focusRequester != null) {
			focusRequester.requestFocus();
		}
		
		if (eventIn.getEventType() == EventType.Mouse) {
			checkLinkClick(eventIn.getMX(), eventIn.getMY(), eventIn.getActionCode());
		}
		
		else if (parentTextArea.isEditable() && eventIn.getEventType() == EventType.Focus) {
			int i = mX - startX + 2;
			setCursorPosition(mc.fontRendererObj.trimStringToWidth(text, i).length());
		}
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
	
	protected boolean checkLinkClick(int mXIn, int mYIn, int button) {
		if (linkText != null && !linkText.isEmpty()) {
			String uText = EChatUtil.removeFormattingCodes(text);
			
			String test = "";
			int total = 0;
			int startPos = startX;
			int endPos = startX;
			int linkPos = 0;
			
			for (int i = 0; i < uText.length(); i++) {
				char c = uText.charAt(i);
				int cLen = mc.fontRendererObj.getCharWidth(c);
				
				total += cLen;
				
				if (test.equals(linkText)) {
					break;
				}
				
				if (c == linkText.charAt(linkPos)) {
					endPos += cLen;
					linkPos++;
					test += c;
				}
				else {
					startPos = startX + total;
					endPos = startPos;
					linkPos = 0;
					test = "";
				}
			}
			
			if (mXIn >= startPos && mXIn <= endPos) {
				try {
					WindowButton.playPressSound();
					if (webLink) {
						EnhancedMC.displayWindow(new LinkConfirmationWindow((String) linkObject));
						return true;
					}
					else if (linkObject != null) {
						if (linkObject instanceof File) { EUtil.openFile((File) linkObject); return true; }
						if (linkObject instanceof IWindowParent) { EnhancedMC.displayWindow((IWindowParent) linkObject); return true; }
						if (linkObject instanceof GuiScreen) { GuiOpener.openGui(linkObject.getClass()); return true; }
					}
				}
				catch (Exception e) { e.printStackTrace(); }
			}
			
		} //if null link
		
		return false;
	}
	
	protected void drawLineNumber() {
		//drawStringWithShadow("" + lineNumber, parentTextArea.startX + parentTextArea.getLineNumberSeparatorPos() - (lineNumberWidth + 2), startY + 2, lineNumberColor);
	}
	
	protected void drawText() {
		int selStart = cursorPosition;
		int selEnd = selectionEnd;
		boolean hasSel = (selStart != selEnd);
		
		//draw text
		if (drawShadowed) { drawStringS(text, startX, startY + 2, textColor); }
		else { drawString(text, startX, startY + 2, textColor); }
		
		if (lineEquals) {
			if (hasSel) { //draw highlight
				int start = selStart;
				int end = selEnd;
				
				//fix substring positions
				if (selStart > selEnd) {
					start = selEnd;
					end = selStart;
				}
				
				int xStart = startX + mc.fontRendererObj.getStringWidth(text.substring(0, start));
				int xEnd = xStart + mc.fontRendererObj.getStringWidth(text.substring(start, end));
				
				//fix highlight selection
				if (selStart > selEnd) {
					int temp = xStart;
					xStart = xEnd;
					xEnd = temp;
				}
				
				drawCursorVertical(xStart, startY + 1, xEnd - 1, startY + 1 + mc.fontRendererObj.FONT_HEIGHT);
			}
			else if ((textRecentlyEntered || drawCursor) && hasFocus()) { //draw vertical cursor
				int textCursorPosLength = mc.fontRendererObj.getStringWidth(text.substring(0, cursorPosition)); //this is not finished -- does not check for horizontal position
				drawRect(startX + textCursorPosLength - 1, startY + 1, startX + textCursorPosLength, endY, 0xffffffff);
			}
		}
	}
	
	//--------------------
	//TextAreaLine Methods
	//--------------------
	
	public TextAreaLine setLinkText(String textIn) { return setLinkText(textIn, null, false); }
	public TextAreaLine setLinkText(String textIn, Object linkObjectIn) { return setLinkText(textIn, linkObjectIn, false); }
	public TextAreaLine setLinkText(String textIn, boolean isWebLink) { return setLinkText(textIn, null, isWebLink); }
	public TextAreaLine setLinkText(String textIn, Object linkObjectIn, boolean isWebLink) {
		linkText = textIn;
		linkObject = linkObjectIn;
		webLink = isWebLink;
		return this;
	}
	
	public TextAreaLine incrementLineNumber() { setLineNumber(lineNumber + 1); return this; }
	public TextAreaLine decrementLineNumber() { setLineNumber(lineNumber - 1); return this; }
	public TextAreaLine indent() { setText("    " + getText()); return this; }
	
	//--------------------
	//TextAreaLine Getters
	//--------------------
	
	public IWindowObject getFocusRequester() { return focusRequester; }
	public int getDrawnLineNumber() { return drawnLineNumber; }
	public int getLineNumber() { return lineNumber; }
	public obj getStoredObj() { return storedObj; }
	public long getDoubleClickThreshold() { return doubleClickThreshold; }
	public TrippleBox<String, Object, Boolean> getLink() { return new TrippleBox(linkText, linkObject, webLink); }
	
	//--------------------
	//TextAreaLine Setters
	//--------------------
	
	public TextAreaLine setHighlighted(boolean val) {
		cursorPosition = 0;
		selectionEnd = val ? text.length() : 0;
		return this;
	}
	
	public TextAreaLine setStoredObj(obj objectIn) { storedObj = objectIn; return this; }
	public TextAreaLine setLineNumber(int numberIn) { lineNumber = numberIn; lineNumberWidth = mc.fontRendererObj.getStringWidth(String.valueOf(lineNumber)); return this; }
	public TextAreaLine setLineNumberColor(int colorIn) { lineNumberColor = colorIn; return this; }
	public TextAreaLine setDrawnLineNumber(int numberIn) { drawnLineNumber = numberIn; return this; }
	public TextAreaLine setDoubleClickThreshold(long timeIn) { doubleClickThreshold = timeIn; return this; }
	public TextAreaLine setFocusRequester(IWindowObject obj) { focusRequester = obj; return this; }
	
	@Override public String toString() { return "[" + lineNumber + ": " + getText() + "]"; }
	
}