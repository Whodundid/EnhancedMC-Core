package com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.textArea;

import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.renderUtil.CursorHelper;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.scrollList.WindowScrollList;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.MouseType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventMouse;
import java.util.Iterator;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

//Author: Hunter Bragg

public class WindowTextArea extends WindowScrollList {
	
	EArrayList<TextAreaLine> textDocument;
	TextAreaLine currentLine, longestLine;
	protected boolean editable = true;
	protected boolean drawLineNumbers = false;
	protected boolean drawLineHighlight = true;
	protected int maxWidth = Integer.MAX_VALUE;
	protected int lineHeight = 10;
	
	public WindowTextArea(IWindowObject parentIn, int x, int y, int widthIn, int heightIn) {
		this(parentIn, x, y, widthIn, heightIn, false);
	}
	
	public WindowTextArea(IWindowObject parentIn, int x, int y, int widthIn, int heightIn, boolean editableIn) {
		this(parentIn, x, y, widthIn, heightIn, false, false);
	}
	
	public WindowTextArea(IWindowObject parentIn, int x, int y, int widthIn, int heightIn, boolean editableIn, boolean addLine) {
		super(parentIn, x, y, widthIn, heightIn);
		editable = editableIn;
		textDocument = new EArrayList();
		Keyboard.enableRepeatEvents(true);
		setBackgroundColor(0xff2d2d2d);
		setResetDrawn(true);
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
					else { setSelectedLine(null); }
				}
			}
		}
	}
	
	//override to prevent cursor updates
	@Override
	public void updateCursorImage() {
		if (!isEditable()) { super.updateCursorImage(); }
	}
	
	@Override
	public void mouseEntered(int mX, int mY) {
		super.mouseEntered(mX, mY);
		if (isEditable()) {
			CursorHelper.setCursor(EMCResources.cursorIBeam);
		}
	}
	
	@Override
	public void mouseExited(int mX, int mY) {
		super.mouseExited(mX, mY);
		if (getTopParent() != null) {
			IWindowObject o = getTopParent().getHighestZObjectUnderMouse();
			if (o != null && !o.isChild(this)) {
				CursorHelper.reset();
			}
		}
		else { CursorHelper.reset(); }
	}
	
	public TextAreaLine addTextLine() { return addTextLine("", 0xffffff, null, false); }
	public TextAreaLine addTextLine(boolean moveDown) { return addTextLine("", 0xffffff, null, moveDown); }
	public TextAreaLine addTextLine(String textIn) { return addTextLine(textIn, 0xffffff, null, false); }
	public TextAreaLine addTextLine(String textIn, int colorIn) { return addTextLine(textIn, colorIn, null, false); }
	public TextAreaLine addTextLine(String textIn, Object objectIn) { return addTextLine(textIn, 0xffffff, objectIn, false); }
	public TextAreaLine addTextLine(String textIn, int colorIn, Object objectIn) { return addTextLine(textIn, colorIn, objectIn, false); }
	public TextAreaLine addTextLine(String textIn, int colorIn, Object objectIn, boolean moveDown) {
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
		fitItemsInList(3, 7);
		updateVisuals();
		lineIn.setLineNumber(textDocument.size());
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
	
	public WindowTextArea deleteLine() { return deleteLine(getCurrentLine()); }
	public WindowTextArea deleteLine(int lineNumber) { return deleteLine(getTextLine(lineNumber)); }
	public WindowTextArea deleteLine(TextAreaLine lineIn) {
		textDocument.remove(lineIn);
		removeObjectFromList(lineIn);
		fitItemsInList(5, 8);
		return this;
	}
	
	public WindowTextArea setSelectedLine(TextAreaLine lineIn) { return setSelectedLine(lineIn, true); }
	public WindowTextArea setSelectedLine(TextAreaLine lineIn, boolean makeDrawn) {
		if (lineIn == null) { currentLine = null; return this; }
		if (textDocument.contains(lineIn)) {
			currentLine = lineIn;
			if (!currentLine.hasFocus()) { currentLine.requestFocus(); }
			if (makeDrawn && currentLine != null) {
				//setLineNumberDrawn(currentLine);
			}
		}
		return this;
	}
	
	public WindowTextArea setLineNumberDrawn(int lineNumber) { return setLineNumberDrawn(getTextLine(lineNumber)); }
	public WindowTextArea setLineNumberDrawn(TextAreaLine lineIn) {
		if (lineIn != null) {
			int lineYPos = lineIn.endY + lineIn.height;
			int difference = lineYPos - startY;
			
			getVScrollBar().setScrollBarPos(difference);
		}
		return this;
	}
	
	/** Used when pressing enter. */
	public TextAreaLine createNewLineAfter(TextAreaLine theLine) {
		if (theLine != null) {
			String text = theLine.getText().substring(theLine.getCursorPosition());
			TextAreaLine newLine = new TextAreaLine(this, text, theLine.textColor, theLine.getStoredObj(), theLine.getLineNumber() + 1);
			
			EArrayList<TextAreaLine> linesAfter = new EArrayList();
			try {
				for (int i = theLine.getLineNumber() + 1; i < textDocument.size() + 1; i++) {
					TextAreaLine l = getTextLine(i);
					linesAfter.add(l);
				}
			}
			catch (Exception e) { e.printStackTrace(); }
			
			for (TextAreaLine l : linesAfter) { deleteLine(l); }
			theLine.setText(theLine.getText().substring(0, theLine.getCursorPosition()));
			addTextLine(newLine);
			for (TextAreaLine l : linesAfter) { addTextLine(l); }
			
			setSelectedLine(newLine);
			newLine.setCursorPosition(0);
		}
		return null;
	}
	
	public TextAreaLine deleteLineAndAddPrevious(TextAreaLine theLine) {
		if (theLine != null) {
			String text = theLine.getText();
			TextAreaLine prev = getTextLine(theLine.getLineNumber() - 1);
			
			EArrayList<TextAreaLine> linesAfter = new EArrayList();
			try {
				for (int i = theLine.getLineNumber() + 1; i < textDocument.size() + 1; i++) {
					linesAfter.add(getTextLine(i));
				}
			}
			catch (Exception e) { e.printStackTrace(); }
			
			deleteLine(theLine);
			prev.setText(prev.getText() + text);
			for (TextAreaLine l : linesAfter) { deleteLine(l); }
			for (TextAreaLine l : linesAfter) { addTextLine(l); }
			
			setSelectedLine(prev);
			int pos = MathHelper.clamp_int(prev.getText().length() - text.length(), 0, prev.getText().length());
			prev.setCursorPosition(pos);
		}
		return null;
	}
	
	//unfinished
	public WindowTextArea setMaxLineWidth(int widthIn) {
		return this;
	}
	
	//unfinished
	public WindowTextArea indentLine() { return indentLine(getCurrentLine()); }
	public WindowTextArea indentLine(int lineNumber) { return indentLine(getTextLine(lineNumber)); }
	public WindowTextArea indentLine(TextAreaLine lineIn) {
		return this;
	}
	
	public TextAreaLine selectPreviousLine(int numIn, int pos) { return selectPreviousLine(getTextLine(numIn), pos); }
	public TextAreaLine selectPreviousLine(TextAreaLine lineIn, int pos) {
		if (lineIn != null) {
			TextAreaLine line = getTextLine(lineIn.getLineNumber() - 1);
			if (line != null) {
				setSelectedLine(line);
				line.setCursorPosition(pos);
			}
			return line;
		}
		return null;
	}
	
	public TextAreaLine selectNextLine(int numIn, int pos) { return selectNextLine(getTextLine(numIn), pos); }
	public TextAreaLine selectNextLine(TextAreaLine lineIn, int pos) {
		if (lineIn != null) {
			TextAreaLine line = getTextLine(lineIn.getLineNumber() + 1);
			if (line != null) {
				setSelectedLine(line);
				line.setCursorPosition(pos);
			}
			return line;
		}
		return null;
	}
	
	public TextAreaLine getTextLine(int numIn) {
		if (numIn > 0 && numIn <= textDocument.size()) {
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
			}
			else if (l.getStoredObj().equals(objectIn)) { return l; }
		}
		return null;
	}
	
	public TextAreaLine getLineWithTextAndObject(String textIn, Object objectIn) {
		for (TextAreaLine l : textDocument) {
			if (l.getStoredObj() == null ) {
				if (l.getText().equals(textIn) && objectIn == null) { return l; }
			}
			else if (l.getText().equals(textIn) && l.getStoredObj().equals(objectIn)) { return l; }
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
	
	public WindowTextArea clear() {
		setListWidth(width - 2);
		setListHeight(0);
		Iterator it = windowObjects.iterator();
		while (it.hasNext()) {
			if (it.next() instanceof TextAreaLine) { it.remove(); }
		}
		listContents.clear();
		textDocument.clear();
		currentLine = null;
		longestLine = null;
		return this;
	}
	
	public WindowTextArea setTextDocument(EArrayList<TextAreaLine> docIn) {
		clear();
		for (TextAreaLine l : docIn) { addTextLine(l); }
		return this;
	}
	
	public WindowTextArea setLineHeight(int in) { lineHeight = in; return this; }
	public WindowTextArea setDrawLineNumbers(boolean valIn) { drawLineNumbers = valIn; return this; }
	public WindowTextArea setDrawLineHighlight(boolean valIn) { drawLineHighlight = valIn; return this; }
	public WindowTextArea setEditable(boolean valIn) { editable = valIn; return this; }
	
	public TextAreaLine getLineMouseIsOver() {
		int mPosY = mY - startY - 1;
		int scrollOffset = verticalScroll.getScrollPos() - verticalScroll.getVisibleAmount();
		int posY = mPosY + scrollOffset;
		
		int num = (posY / lineHeight) + 1;
		TextAreaLine l = getTextLine(num);
		
		return l;
	}
	
	public int getLineHeight() { return lineHeight; }
	public int getLineCount() { return (height - 2) / 10; }
	public boolean hasLineNumbers() { return drawLineNumbers; }
	public boolean getDrawLineHighlight() { return drawLineHighlight; }
	public boolean isEditable() { return editable; }
	public TextAreaLine getCurrentLine() { return currentLine; }
	public EArrayList<TextAreaLine> getTextDocument() { return textDocument; }
	
}
