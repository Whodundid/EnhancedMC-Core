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
		textDocument.add(lineIn);
		addObjectToList(lineIn);
		fitDocumentInDims();
		return lineIn;
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
	
	public TextAreaLine getTextLineWithLineNumber(int lineNumberIn) {
		if (lineNumberIn >= 1 && lineNumberIn <= textDocument.size()) {
			for (TextAreaLine l : textDocument) { if (l.getLineNumber() == lineNumberIn) { return l; } }
		}
		return null;
	}
	
	public EGuiTextArea setSelectedLine(TextAreaLine lineIn) { return setSelectedLine(lineIn, true); }
	public EGuiTextArea setSelectedLine(TextAreaLine lineIn, boolean makeDrawn) {
		if (lineIn != null && textDocument.contains(lineIn)) {
			currentLine = lineIn;
			if (makeDrawn && currentLine != null) {
				currentLine.requestFocus();
				//makeLineNumberDrawn(currentLine.getLineNumber());
			}
		}
		return this;
	}
	
	private void fitDocumentInDims() {
		setListHeight(0);
		setListWidth(width);
		if (textDocument.size() > 0) {
			growListHeight(1);
			setListHeight(textDocument.size() * 10);
			growListHeight(1);
			
			int len = getLongestLineLength();
			setListWidth(len > 0 ? len : getListWidth());
			
			if (verticalScroll != null) { if (scrollableHeight > (height - 2)) { growListWidth(8); } }
			else if (heightToBeSet > (height - 2)) { growListWidth(8); }
			
			if (horizontalScroll != null) {	if (scrollableWidth > (width - 2)) { growListHeight(5); } }
			else if (widthToBeSet > (width - 2)) { growListHeight(5); }
		}
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
	
	public EGuiTextArea setDrawLineNumbers(boolean valIn) { drawLineNumbers = valIn; return this; }
	public EGuiTextArea setEditable(boolean valIn) { editable = valIn; return this; }
	
	public int getLineCount() { return (height - 2) / 10; }
	public boolean hasLineNumbers() { return drawLineNumbers; }
	public boolean isEditable() { return editable; }
	public TextAreaLine getCurrentLine() { return currentLine; }
	public TextAreaLine getTextLine(int numIn) { return numIn >= 0 && numIn < textDocument.size() ? textDocument.get(numIn) : null; }
	public EArrayList<TextAreaLine> getTextDocument() { return textDocument; }
}
