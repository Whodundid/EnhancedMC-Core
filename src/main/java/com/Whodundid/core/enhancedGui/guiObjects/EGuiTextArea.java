package com.Whodundid.core.enhancedGui.guiObjects;

import com.Whodundid.core.enhancedGui.guiObjectUtil.TextAreaLine;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventFocus;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import java.util.Iterator;
import org.lwjgl.input.Keyboard;

//Last edited: Jan 9, 2019
//First Added: Oct 2, 2018
//Author: Hunter Bragg

public class EGuiTextArea<obj> extends EGuiScrollList {
	
	EArrayList<TextAreaLine> textDocument;
	TextAreaLine currentLine, longestLine;
	protected boolean editable = true;
	protected boolean drawLineNumbers = false;
	protected int maxWidth = Integer.MAX_VALUE;
	
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
	public void drawObject(int mXIn, int mYIn, float ticks) {
		super.drawObject(mXIn, mYIn, ticks);
	}
	
	@Override
	public void mouseScrolled(int change) {
		super.mouseScrolled(change);
	}
	
	@Override
	public void mouseEntered(int mX, int mY) {
		
	}
	
	@Override
	public void mouseExited(int mX, int mY) {
		
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
		return addTextLine(new TextAreaLine(this, textIn, colorIn, objectIn, textDocument.size()), moveDown);
	}
	
	public TextAreaLine addTextLine(TextAreaLine lineIn) { return addTextLine(lineIn, false); }
	public TextAreaLine addTextLine(TextAreaLine lineIn, boolean moveDown) {
		int moveArg = moveDown ? 1 : 0;
		EDimension ld = this.getListDimensions();
		lineIn.setDimensions(3, 1 + (textDocument.size() * 10), ld.width, 10);
		//if (textDocument.size() % 2 == 1) {
		//	EGuiRect back = new EGuiRect(this, 0, 2 + (textDocument.size() * 10), ld.width, 12 + (textDocument.size() * 10), 0x1a000000);
		//	addObjectToList(back.setClickable(false));
		//}
		textDocument.add(lineIn);
		addObjectToList(lineIn);
		fitDocumentInDims();
		return lineIn;
	}
	
	public EGuiTextArea insertTextLine() { return insertTextLine("", 0xffffff, -1); }
	public EGuiTextArea insertTextLine(int atPos) { return insertTextLine("", 0xffffff, atPos); }
	public EGuiTextArea insertTextLine(String textIn) { return insertTextLine(textIn, 0xffffff, -1); }
	public EGuiTextArea insertTextLine(String textIn, int atPos) { return insertTextLine(textIn, 0xffffff, atPos); }
	public EGuiTextArea insertTextLine(String textIn, int colorIn, int atPos) {
		return this;
	}
	
	public EGuiTextArea deleteLine() { return deleteLine(getCurrentLine()); }
	public EGuiTextArea deleteLine(int lineNumber) { return deleteLine(getTextLine(lineNumber)); }
	public EGuiTextArea deleteLine(TextAreaLine lineIn) {
		textDocument.remove(lineIn);
		removeObjectFromList(lineIn);
		fitDocumentInDims();
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
	
	private void fitDocumentInDims() {
		//get current scroll position -- if there is one
		//EGuiScrollBar vs = getVScrollBar();
		//int prevScroll = vs != null ? vs.getScrollPos() : 0;
		
		//reset list values
		setListHeight(0);
		setListWidth(width);
		
		if (textDocument.size() > 0) {
			growListHeight(1);
			setListHeight(textDocument.size() * 10); //currently hardcoding line height
			growListHeight(1);
			
			int len = getLongestLineLength();
			setListWidth(len > 0 ? len : getListWidth());
			
			if (verticalScroll != null) { if (scrollableHeight > (height - 2)) { growListWidth(11); } }
			else if (heightToBeSet > (height - 2)) { growListWidth(8); }
			
			if (horizontalScroll != null) {	if (scrollableWidth > (width - 2)) { growListHeight(10); } }
			growListHeight(4);
		}
		
		//return to original scroll position
		//if (vs != null) { vs.setScrollBarPos(prevScroll); }
	}
	
	public TextAreaLine getTextLine(int numIn) {
		if (numIn >= 1 && numIn < textDocument.size()) {
			int first = 0;
			int last = textDocument.size();
			int mid = (first + last) / 2;
			while (first <= last) {
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
			int len = fontRenderer.getStringWidth(l.getText());
			if (len > longestLen) { longest = l; longestLen = len; }
		}
		return longest;
	}
	
	public int getLongestLineLength() {
		if (longestLine == null) { longestLine = getLongestTextLine(); }
		return longestLine != null ? fontRenderer.getStringWidth(longestLine.getText()) : - 1;
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
	
	public EGuiTextArea setDrawLineNumbers(boolean valIn) { drawLineNumbers = valIn; return this; }
	public EGuiTextArea setEditable(boolean valIn) { editable = valIn; return this; }
	
	public int getLineCount() { return (height - 2) / 10; }
	public boolean hasLineNumbers() { return drawLineNumbers; }
	public boolean isEditable() { return editable; }
	public TextAreaLine getCurrentLine() { return currentLine; }
	public EArrayList<TextAreaLine> getTextDocument() { return textDocument; }
}
