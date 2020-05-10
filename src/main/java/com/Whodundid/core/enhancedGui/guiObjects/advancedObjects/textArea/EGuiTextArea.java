package com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea;

import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.scrollList.EGuiScrollList;
import com.Whodundid.core.enhancedGui.objectEvents.EventFocus;
import com.Whodundid.core.enhancedGui.objectEvents.EventMouse;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.MouseType;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.renderUtil.CursorHelper;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import java.util.Iterator;
import org.lwjgl.input.Keyboard;

//Author: Hunter Bragg

public class EGuiTextArea<obj> extends EGuiScrollList {
	
	EArrayList<TextAreaLine> textDocument;
	TextAreaLine currentLine, longestLine;
	protected boolean editable = true;
	protected boolean drawLineNumbers = false;
	protected int maxWidth = Integer.MAX_VALUE;
	protected int lineHeight = 10;
	
	public EGuiTextArea(IEnhancedGuiObject parentIn, int x, int y, int widthIn, int heightIn) {
		this(parentIn, x, y, widthIn, heightIn, false);
	}
	public EGuiTextArea(IEnhancedGuiObject parentIn, int x, int y, int widthIn, int heightIn, boolean editableIn) {
		this(parentIn, x, y, widthIn, heightIn, false, false);
	}
	public EGuiTextArea(IEnhancedGuiObject parentIn, int x, int y, int widthIn, int heightIn, boolean editableIn, boolean addLine) {
		super(parentIn, x, y, widthIn, heightIn);
		editable = editableIn;
		textDocument = new EArrayList();
		Keyboard.enableRepeatEvents(true);
		setBackgroundColor(0xff2d2d2d);
		setResetDrawn(true);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		postEvent(new EventMouse(this, mX, mY, button, MouseType.Pressed));
		if (button == 0) {
			EUtil.ifNotNullDo(getWindowParent(), w -> w.bringToFront());
			
			if (isEditable() && textDocument.isEmpty()) {
				TextAreaLine l = addTextLine();
				setSelectedLine(l);
				l.requestFocus();
			}
			else if (textDocument.isNotEmpty()) {
				if (button != 1) {
					TextAreaLine l = getLineMouseIsOver();
					
					if (l != null) {
						setSelectedLine(l);
						l.requestFocus();
					}
					
				}
			}
		}
	}
	
	@Override
	public void mouseScrolled(int change) {
		super.mouseScrolled(change);
	}
	
	//override to prevent cursor updates
	@Override
	public void updateCursorImage() {
		if (!isEditable()) { super.updateCursorImage(); }
	}
	
	@Override
	public void mouseEntered(int mX, int mY) {
		if (isEditable()) {
			CursorHelper.setCursor(EMCResources.cursorIBeam);
		}
	}
	
	@Override
	public void mouseExited(int mX, int mY) {
		CursorHelper.reset();
	}
	
	@Override
	public void onFocusGained(EventFocus eventIn) {
		super.onFocusGained(eventIn);
	}
	
	public TextAreaLine addTextLine() { return addTextLine("", 0xffffff, null, false); }
	public TextAreaLine addTextLine(boolean moveDown) { return addTextLine("", 0xffffff, null, moveDown); }
	public TextAreaLine addTextLine(String textIn) { return addTextLine(textIn, 0xffffff, null, false); }
	public TextAreaLine addTextLine(String textIn, int colorIn) { return addTextLine(textIn, colorIn, null, false); }
	public TextAreaLine addTextLine(String textIn, obj objectIn) { return addTextLine(textIn, 0xffffff, objectIn, false); }
	public TextAreaLine addTextLine(String textIn, int colorIn, obj objectIn) { return addTextLine(textIn, colorIn, objectIn, false); }
	public TextAreaLine addTextLine(String textIn, int colorIn, obj objectIn, boolean moveDown) {
		return addTextLine(new TextAreaLine(this, textIn, colorIn, objectIn, textDocument.size()), 0, moveDown);
	}
	
	public TextAreaLine addTextLine(TextAreaLine lineIn) { return addTextLine(lineIn, 0, false); }
	public TextAreaLine addTextLine(TextAreaLine lineIn, int offset) { return addTextLine(lineIn, offset, false); }
	public TextAreaLine addTextLine(TextAreaLine lineIn, int offset, boolean moveDown) {
		int moveArg = moveDown ? 1 : 0;
		EDimension ld = getListDimensions();
		lineIn.setDimensions(3, 1 + (textDocument.size() * 10) + offset, mc.fontRendererObj.getStringWidth(lineIn.getText()), 10);
		textDocument.add(lineIn);
		addObjectToList(lineIn);
		fitItemsInList(3, 8);
		lineIn.setLineNumber(textDocument.size() - 1);
		return lineIn;
	}
	
	public TextAreaLine insertTextLine() { return insertTextLine("", 0xffffff, -1); }
	public TextAreaLine insertTextLine(int atPos) { return insertTextLine("", 0xffffff, atPos); }
	public TextAreaLine insertTextLine(String textIn) { return insertTextLine(textIn, 0xffffff, -1); }
	public TextAreaLine insertTextLine(String textIn, int atPos) { return insertTextLine(textIn, 0xffffff, atPos); }
	public TextAreaLine insertTextLine(String textIn, int colorIn, int atPos) {
		if (atPos == -1) {
			if (currentLine == null) {
				System.out.println("pos: " + atPos);
				atPos = textDocument.size();
				
			}
		}
		return null;
	}
	
	public TextAreaLine insertTextLine(TextAreaLine lineIn, int atPos) {
		
		return lineIn;
	}
	
	public EGuiTextArea deleteLine() { return deleteLine(getCurrentLine()); }
	public EGuiTextArea deleteLine(int lineNumber) { return deleteLine(getTextLine(lineNumber)); }
	public EGuiTextArea deleteLine(TextAreaLine lineIn) {
		textDocument.remove(lineIn);
		removeObjectFromList(lineIn);
		fitItemsInList(5, 8);
		return this;
	}
	
	public EGuiTextArea setSelectedLine(TextAreaLine lineIn) { return setSelectedLine(lineIn, true); }
	public EGuiTextArea setSelectedLine(TextAreaLine lineIn, boolean makeDrawn) {
		if (lineIn != null && textDocument.contains(lineIn)) {
			currentLine = lineIn;
			if (makeDrawn && currentLine != null) {
				//currentLine.requestFocus();
				//makeLineNumberDrawn(currentLine.getLineNumber());
			}
		}
		return this;
	}
	
	public EGuiTextArea setLineNumberDrawn(int lineNumber) { return setLineNumberDrawn(getTextLine(lineNumber)); }
	public EGuiTextArea setLineNumberDrawn(TextAreaLine lineIn) {
		return this;
	}
	
	public EGuiTextArea setMaxLineWidth(int widthIn) {
		return this;
	}
	
	public EGuiTextArea indentLine() { return indentLine(getCurrentLine()); }
	public EGuiTextArea indentLine(int lineNumber) { return indentLine(getTextLine(lineNumber)); }
	public EGuiTextArea indentLine(TextAreaLine lineIn) {
		return this;
	}
	
	/*
	private void fitDocumentInDims() {
		//get current scroll position -- if there is one
		//EGuiScrollBar vs = getVScrollBar();
		//int prevScroll = vs != null ? vs.getScrollPos() : 0;
		
		//reset list values
		setListHeight(0);
		setListWidth(width - 2);
		
		if (textDocument.size() > 0) {
			growListHeight(1);
			setListHeight(textDocument.size() * 10); //currently hardcoding line height
			growListHeight(1);
			
			int len = getLongestLineLength();
			setListWidth(len > getListWidth() ? len + 10 : getListWidth());
			
			//if (verticalScroll != null) { if (scrollableHeight > (height - 2)) { growListWidth(15); } }
			//else if (heightToBeSet > (height - 2)) { growListWidth(15); }
			
			if (horizontalScroll != null) {	if (scrollableWidth > (width - 2)) { growListHeight(9); } }
			growListHeight(2);
		}
		
		//return to original scroll position
		//if (vs != null) { vs.setScrollBarPos(prevScroll); }
	}
	*/
	
	public TextAreaLine getTextLine(int numIn) {
		if (numIn >= 0 && numIn < textDocument.size()) {
			int first = 0;
			int last = textDocument.size();
			int mid = (first + last) / 2;
			while (first <= last && mid < textDocument.size()) {
				TextAreaLine l = textDocument.get(mid);
				if (l != null) {
					if (l.getLineNumber() == numIn) { return l; }
					else if (l.getLineNumber() < numIn) { first = mid + 1; }
					else { last = mid - 1; }
				}
				mid = (first + last) / 2;
			}
		}
		return null;
	}
	
	public TextAreaLine getLineWithText(String textIn) {
		for (TextAreaLine l : textDocument) {
			if (l.getText().equals(textIn)) { return l; }
		}
		return null;
	}
	
	public TextAreaLine getLineWithObject(Object objectIn) {
		for (TextAreaLine l : textDocument) {
			if (l.getStoredObj() == null) {
				if (objectIn == null) { return l; }
			} else if (l.getStoredObj().equals(objectIn)) { return l; }
		}
		return null;
	}
	
	public TextAreaLine getLineWithTextAndObject(String textIn, Object objectIn) {
		for (TextAreaLine l : textDocument) {
			if (l.getStoredObj() == null ) {
				if (l.getText().equals(textIn) && objectIn == null) { return l; }
			} else if (l.getText().equals(textIn) && l.getStoredObj().equals(objectIn)) { return l; }
		}
		return null;
	}
	
	public TextAreaLine getLongestTextLine() {
		TextAreaLine longest = null;
		int longestLen = 0;
		for (TextAreaLine l : textDocument) {
			int len = mc.fontRendererObj.getStringWidth(l.getText());
			if (len > longestLen) { longest = l; longestLen = len; }
		}
		return longest;
	}
	
	public int getLongestLineLength() {
		longestLine = getLongestTextLine();
		return longestLine != null ? mc.fontRendererObj.getStringWidth(longestLine.getText()) : - 1;
	}
	
	public EGuiTextArea clear() {
		setListWidth(width - 2);
		setListHeight(0);
		Iterator it = guiObjects.iterator();
		while (it.hasNext()) {
			if (it.next() instanceof TextAreaLine) { it.remove(); }
		}
		listContents.clear();
		textDocument.clear();
		currentLine = null;
		longestLine = null;
		return this;
	}
	
	public EGuiTextArea setTextDocument(EArrayList<TextAreaLine> docIn) {
		clear();
		for (TextAreaLine l : docIn) { addTextLine(l); }
		return this;
	}
	
	public EGuiTextArea setLineHeight(int in) { lineHeight = in; return this; }
	public EGuiTextArea setDrawLineNumbers(boolean valIn) { drawLineNumbers = valIn; return this; }
	public EGuiTextArea setEditable(boolean valIn) { editable = valIn; return this; }
	
	public TextAreaLine getLineMouseIsOver() {
		int mPosY = mY - startY - 1;
		int scrollOffset = verticalScroll.getScrollPos() - verticalScroll.getVisibleAmount();
		int posY = mPosY + scrollOffset;
		
		TextAreaLine l = getTextLine(posY / lineHeight);
		
		return l;
	}
	
	public int getLineHeight() { return lineHeight; }
	public int getLineCount() { return (height - 2) / 10; }
	public boolean hasLineNumbers() { return drawLineNumbers; }
	public boolean isEditable() { return editable; }
	public TextAreaLine getCurrentLine() { return currentLine; }
	public EArrayList<TextAreaLine> getTextDocument() { return (EArrayList<TextAreaLine>) textDocument; }
	
}
