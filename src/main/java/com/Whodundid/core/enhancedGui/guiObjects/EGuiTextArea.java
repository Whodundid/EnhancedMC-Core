package com.Whodundid.core.enhancedGui.guiObjects;

import com.Whodundid.core.enhancedGui.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.guiObjectUtil.TextAreaLine;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventFocus;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.playerUtil.Direction;
import com.Whodundid.core.util.renderUtil.CursorHelper;
import com.Whodundid.core.util.storageUtil.EArrayList;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

//Last edited: Jan 9, 2019
//First Added: Oct 2, 2018
//Author: Hunter Bragg

public class EGuiTextArea<obj> extends EnhancedGuiObject {
	
	EArrayList<TextAreaLine> textDocument, drawnLines;
	TextAreaLine currentLine, longestLine;
	EGuiScrollBar verticalScroll, horizontalScroll;
	protected boolean editable = true;
	protected int currentDrawnLine = -1;
	protected int currentPosY = 0;
	protected int currentPosX = 0;
	protected int previousPosY = -1;
	protected int previousPosX = -1;
	protected int lineHeight = 12;
	protected boolean drawLineNumbers = false;
	protected int lineNumberSeparatorPos = 0;
	
	public EGuiTextArea(IEnhancedGuiObject parentIn, int x, int y, int widthIn, int heightIn) {
		this(parentIn, x, y, widthIn, heightIn, false);
	}
	public EGuiTextArea(IEnhancedGuiObject parentIn, int x, int y, int widthIn, int heightIn, boolean editableIn) {
		init(parentIn, x, y, widthIn, heightIn);
		editable = editableIn;
		textDocument = new EArrayList();
		drawnLines = new EArrayList();
		Keyboard.enableRepeatEvents(true);
		
		verticalScroll = new EGuiScrollBar(this, getLineCount(), textDocument.size() + getLineCount(), true, Direction.E) {
			@Override
			public void mouseReleased(int mX, int mY, int button) {
				getParent().requestFocus();
				super.mouseReleased(mX, mY, button);
			}
		};
		horizontalScroll = new EGuiScrollBar(this, getDrawWidth(), longestLine != null ? fontRenderer.getStringWidth(longestLine.getText()) : getDrawWidth(), false, Direction.S) {
			@Override
			public void mouseReleased(int mX, int mY, int button) {
				getParent().requestFocus();
				super.mouseReleased(mX, mY, button);
			}
		};
		
		verticalScroll.setVisible(false);
		horizontalScroll.setVisible(false);
		
		addObject(verticalScroll, horizontalScroll);
		
		if (textDocument.isEmpty() && editable) {
			TextAreaLine newLine = addTextLine();
			setSelectedLine(newLine);
		}
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		try {
			updateBeforeNextDraw(mXIn, mYIn);
			
			drawRect(startX, startY, endX, endY, 0xff000000); //black
			drawRect(startX + 1, startY + 1, endX - 1, endY - 1, 0xff2D2D2D); //grey
			
			if (drawLineNumbers && drawnLines.size() > 0) {
				drawRect(startX + lineNumberSeparatorPos, startY + 1, startX + lineNumberSeparatorPos + 1, endY - 1, 0xff000000);
			}
			
			//verticalScroll.setVisible(true);
			verticalScroll.setVisible(textDocument.size() > getLineCount());
			horizontalScroll.setVisible(
					longestLine != null ? fontRenderer.getStringWidth(longestLine.getText()) > width - (drawLineNumbers ? lineNumberSeparatorPos : 0) :
					false);
			
			try {
				if (verticalScroll.getScrollPos() - getLineCount() != currentPosY) {
					currentPosY = verticalScroll.getScrollPos() - getLineCount();
					//System.out.println(currentPosY + " " + getLineCount() + " " + verticalScroll.getScrollPos());
					currentPosY = currentPosY < 0 ? 0 : currentPosY;
					recreateDrawnLines(currentPosY);
				}
				
			} catch (Exception e) { e.printStackTrace(); }
			
			//CursorHelper.setCursor(isMouseInside(mX, mY) ? CursorHelper.iBeamCursor : null);
			
			int scale = res.getScaleFactor();
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
			GL11.glScissor(
					((startX + 1) * scale),
					(Display.getHeight() - startY * scale) - (height - 1) * scale,
					(width - 2) * scale,
					(height - 2) * scale);
			
			for (TextAreaLine l : drawnLines) {
				if (l != null && l.checkDraw()) {
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					l.drawObject(mX, mY, ticks);
				}
			}
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
			
			if (checkDraw()) {
				GlStateManager.pushMatrix();
				GlStateManager.enableBlend();
				synchronized (guiObjects) {
					for (IEnhancedGuiObject o : guiObjects) {
						if (o.checkDraw() && !drawnLines.contains(o)) {
		    				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		    	        	o.drawObject(mXIn, mYIn, ticks);
		    			}
					}
				}
				GlStateManager.popMatrix();
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	@Override
	public void mouseScrolled(int change) {
		if (isCtrlKeyDown()) {
			System.out.println(getDimensions());
			System.out.println("Scroll High: " + verticalScroll.getHighVal() + getLineCount());
			System.out.println("Scroll pos: " + verticalScroll.getScrollPos());
			System.out.println("Current Line: " + currentLine + ", " + currentLine.getDimensions());
			System.out.println("CurrentPosY: " + currentPosY);
			System.out.println("size: " + textDocument.size());
			System.out.println("Current Draw Line: " + getCurrentDrawnLine());
		} else {
			//System.out.println("Scrolled: " + currentPosY);
			if (currentPosY - change >= 0 && textDocument.size() > getLineCount()) {
				//currentPosY -= change;
				//System.out.println("Scrolled2: " + currentPosY);
				setDocumentVerticalPos(currentPosY -= change);
				//verticalScroll.setScrollBarPos(currentPosY + getLineCount());
			}
		}
	}
	
	@Override
	public void mouseEntered(int mX, int mY) {
		if (isEditable()) { CursorHelper.setCursor(CursorHelper.iBeamCursor); }
	}
	
	@Override
	public void mouseExited(int mX, int mY) {
		if (isEditable()) { CursorHelper.setCursor(null); }
	}
	
	@Override
	public void onFocusGained(EventFocus eventIn) {
		if (currentLine != null) { currentLine.requestFocus(); }
		super.onFocusGained(eventIn);
	}
	
	public TextAreaLine insertTextLine(int pos, boolean atEndOfLine, int cursorPos) {
		if (atEndOfLine) {
			if (pos > textDocument.size()) { return addTextLine(); }
			return insertTextLine(pos);
		}
		else if (pos >= 1) {
			TextAreaLine previousLine = getTextLineWithLineNumber(pos - 1);
			if (previousLine != null) {
				TextAreaLine nextLine;
				String lineText = previousLine.getText();
				String textStayingOnLine = lineText.substring(0, cursorPos);
				String textMovingToNextLine = lineText.substring(cursorPos);
				
				if (pos <= textDocument.size()) { nextLine = insertTextLine(pos); }
				else { nextLine = addTextLine(); }
				
				if (nextLine != null) {
					previousLine.setText(textStayingOnLine);
					nextLine.setText(textMovingToNextLine);
					nextLine.setCursorPosition(0);
					return nextLine;
				}
			}
		}
		return null;
	}
	
	private TextAreaLine insertTextLine(int pos) {
		EArrayList<TextAreaLine> linesAfterPos = new EArrayList();
		Iterator<TextAreaLine> it = textDocument.iterator();
		Iterator<IEnhancedGuiObject> qt = guiObjects.iterator();
		int q = pos;
		while (q >= 0 && q <= textDocument.size()) {
			linesAfterPos.add(getTextLineWithLineNumber(q));
			q++;
		}
		while (it.hasNext()) {
			TextAreaLine l = it.next();
			if (linesAfterPos.contains(l)) { it.remove(); }
		}
		while (qt.hasNext()) {
			IEnhancedGuiObject o = qt.next();
			if (linesAfterPos.contains(o)) { qt.remove(); }
		}
		TextAreaLine returnLine = addTextLine().setLineNumber(pos);
		for (TextAreaLine l : linesAfterPos) { addTextLine(l); }
		return returnLine;
	}
	
	public TextAreaLine addTextLine() { return addTextLine("", 0xffffff, null, false); }
	public TextAreaLine addTextLine(boolean moveDown) { return addTextLine("", 0xffffff, null, moveDown); }
	public TextAreaLine addTextLine(String textIn) { return addTextLine(textIn, 0xffffff, null, false); }
	public TextAreaLine addTextLine(String textIn, int colorIn) { return addTextLine(textIn, colorIn, null, false); }
	public TextAreaLine addTextLine(String textIn, obj objectIn) { return addTextLine(textIn, 0xffffff, objectIn, false); }
	public TextAreaLine addTextLine(String textIn, int colorIn, obj objectIn) { return addTextLine(textIn, colorIn, objectIn, false); }
	public TextAreaLine addTextLine(String textIn, int colorIn, obj objectIn, boolean moveDown) {
		TextAreaLine l = new TextAreaLine(this);
		l.setText(textIn);
		l.setTextColor(colorIn);
		l.setStoredObj(objectIn);
		l.setLineNumber(textDocument.size() - currentPosY);
		calculateLongestLine();
		return addTextLine(l, moveDown);
	}
	public TextAreaLine addTextLine(TextAreaLine lineIn) { return addTextLine(lineIn, false); }
	public TextAreaLine addTextLine(TextAreaLine lineIn, boolean moveDown) {
		//int moveArg = (textDocument.size() + 1) * lineHeight > height ? 1 : 0;
		int moveArg = moveDown ? 1 : 0;
		addObject(lineIn);
		textDocument.add(lineIn);
		lineIn.setLineNumber(textDocument.size());
		int val = MathHelper.clamp_int(textDocument.size() - getLineCount(), 0, Integer.MAX_VALUE);
		verticalScroll.setHighVal(val + getLineCount());
		//verticalScroll.setScrollBarPos(currentPosY + 1 + getLineCount());
		setDocumentVerticalPos(currentPosY + moveArg);
		calculateLongestLine();
		return lineIn;
	}
	
	public TextAreaLine deleteLineAndMoveTextUp(int lineNumber) {
		if (lineNumber > 0 && lineNumber <= textDocument.size()) {
			TextAreaLine l = getTextLineWithLineNumber(lineNumber);
			return deleteLineAndMoveTextUp(l);
		}
		return null;
	}
	
	public TextAreaLine deleteLineAndMoveTextUp(TextAreaLine lineIn) {
		if (lineIn != null) {
			String lineText = lineIn.getText();
			int previousLinePos = lineText.length() - lineIn.getCursorPosition();
			TextAreaLine previousLine = getTextLineWithLineNumber(lineIn.getLineNumber() - 1);
			if (previousLine != null) {
				previousLine.setText(previousLine.getText() + lineText);
				removeTextLine(lineIn);
				previousLine.setCursorPosition(MathHelper.clamp_int(previousLine.getText().length() - previousLinePos, 0, previousLine.getText().length()));
				previousLine.requestFocus();
			}
		}
		return null;
	}
	
	public EGuiTextArea removeTextLine(int lineNumber) {
		if (lineNumber >= 0 && lineNumber < textDocument.size()) {
			removeTextLine(textDocument.get(lineNumber));
		}
		return this;
	}
	
	public EGuiTextArea removeTextLine(TextAreaLine lineIn) {
		if (lineIn != null) {
			int q = lineIn.getLineNumber();
			while (q >= 1 && q <= textDocument.size()) {
				TextAreaLine l = getTextLineWithLineNumber(q);
				if (l != null) {
					l.decrementLineNumber();
					q++;
				} else { break; }
			}
			TextAreaLine previousLine = getTextLineWithLineNumber(lineIn.getLineNumber());
			guiObjects.remove(lineIn);
			textDocument.remove(lineIn);
			int val = MathHelper.clamp_int(textDocument.size() - getLineCount(), 0, Integer.MAX_VALUE);
			verticalScroll.setHighVal(val + getLineCount());
			if (previousLine != null) { previousLine.requestFocus(); }
			//System.out.println("go up check: " + lineIn.getDrawnLineNumber() + " " + currentPosY);
			//verticalScroll.setScrollBarPos((currentPosY - 1) + getLineCount());
			if (drawnLines.contains(lineIn)) {
				if (textDocument.size() > getLineCount()) { setDocumentVerticalPos(currentPosY -= 1); }
				else { setDocumentVerticalPos(0); }
			}
			calculateLongestLine();
		}
		return this; 
	}
	
	public EGuiTextArea setDocumentVerticalPos(int posIn) {
		posIn = posIn < 0 ? 0 : posIn;
		currentPosY = posIn;
		verticalScroll.setScrollBarPos(currentPosY + getLineCount());
		recreateDrawnLines(currentPosY);
		return this;
	}
	
	public EGuiTextArea setDocumentHorizontalPos(int posIn) {
		if (posIn >= 0 && posIn <= longestLine.getText().length()) {
			currentPosX = posIn;
			horizontalScroll.setScrollBarPos(currentPosX);
		}
		return this;
	}
	
	protected void recreateDrawnLines(int posIn) {
		try {
			if (currentPosY > textDocument.size()) { currentPosY--; }
			drawnLines.clear();
			for (TextAreaLine l : textDocument) { l.setDrawnLineNumber(-1); l.setVisible(false); }
			calculateLineNumberSeparatorPos();
			int pos = currentPosY;
			int i = 0;
			//System.out.println("Pos: " + pos + " " + currentPosY);
			while (pos < textDocument.size() && pos < (currentPosY + getLineCount())) {
				TextAreaLine l = getTextLineWithLineNumber(pos + 1);
				if (l != null) {
					int xPos = drawLineNumbers ? startX + lineNumberSeparatorPos + 4 : startX + 3;
					int widthVal = drawLineNumbers ? width - lineNumberSeparatorPos - 9 : width - 9;
					l.setDimensions(xPos, (i * lineHeight) + (startY + 1), widthVal, lineHeight);
					l.setDrawnLineNumber(i);
					l.setVisible(true);
					//System.out.println(l.getDimensions());
					drawnLines.add(l);
				}
				pos++;
				i++;
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public void moveCurrentLineUp() {
		TextAreaLine upLine = getTextLineWithLineNumber(currentLine.getLineNumber() - 1);
		if (upLine != null) {
			if (currentLine.getDrawnLineNumber() == 0) { setDocumentVerticalPos(currentPosY - 1); }
			if (currentLine.getDrawnLineNumber() < 0) { setDocumentVerticalPos(upLine.getLineNumber() - getLineCount()); }
			upLine.setCursorPosition(currentLine.getCursorPosition());
			upLine.requestFocus();
		}
	}
	
	public void moveCurrentLineDown() {
		TextAreaLine downLine = getTextLineWithLineNumber(currentLine.getLineNumber() + 1);
		if (downLine != null) {
			if (currentLine.getDrawnLineNumber() == getLineCount() - 1) { setDocumentVerticalPos(currentPosY + 1); }
			if (currentLine.getDrawnLineNumber() < 0) { setDocumentVerticalPos(downLine.getLineNumber() - 1); }
			downLine.setCursorPosition(currentLine.getCursorPosition());
			downLine.requestFocus();
		}
	}
	
	public void makeTextLineDrawn(TextAreaLine lineIn) {
		if (lineIn != null) { makeLineNumberDrawn(lineIn.getLineNumber()); }
	}
	
	public void makeLineNumberDrawn(int lineNumIn) {
		if (lineNumIn >= 1 && lineNumIn <= textDocument.size()) {
			TextAreaLine l = getTextLineWithLineNumber(lineNumIn);
			if (lineNumIn < currentPosY) { setDocumentVerticalPos(lineNumIn - 1); }
			else if (lineNumIn >= currentPosY + getLineCount()) { setDocumentVerticalPos(lineNumIn - getLineCount()); }
		}
	}
	
	public void makeTextLineEndDrawn(TextAreaLine lineIn) {
		if (lineIn != null) { makeLineNumberEndDrawn(lineIn.getLineNumber()); }
	}
	
	public void makeLineNumberEndDrawn(int lineNumIn) {
		if (lineNumIn >= 1 && lineNumIn <= textDocument.size()) {
			TextAreaLine l = getTextLineWithLineNumber(lineNumIn);
			if (l.getDrawnLineNumber() == -1) { makeLineNumberDrawn(lineNumIn); }
			String lineDrawnText = l.getVisibleText();
			System.out.println(lineDrawnText);
		}
	}
	
	public EGuiTextArea clear() {
		verticalScroll.setVisible(false);
		horizontalScroll.setVisible(false);
		Iterator it = guiObjects.iterator();
		while (it.hasNext()) {
			if (it.next() instanceof TextAreaLine) { it.remove(); }
		}
		textDocument.clear();
		drawnLines.clear();
		currentLine = null;
		return this;
	}
	
	/** SIMPLIFY THIS, THERE'S NO NEED FOR 2 */
	public EGuiTextArea setSelectedLine(TextAreaLine lineIn) { return setSelectedLine(lineIn, true); }
	public EGuiTextArea setSelectedLine(TextAreaLine lineIn, boolean makeDrawn) {
		currentLine = lineIn;
		if (makeDrawn && currentLine != null) {
			currentLine.requestFocus();
			//makeLineNumberDrawn(currentLine.getLineNumber());
		}
		return this;
	}
	
	public TextAreaLine getTextLineWithLineNumber(int lineNumber) {
		if (lineNumber >= 1 && lineNumber <= textDocument.size()) {
			for (TextAreaLine l : textDocument) { if (l.getLineNumber() == lineNumber) { return l; } }
		}
		return null;
	}
	
	protected void calculateLineNumberSeparatorPos() {
		int longestNum = 0;
		for (TextAreaLine l : textDocument) {
			int val = fontRenderer.getStringWidth(String.valueOf(l.getLineNumber()));
			if (val > longestNum) { longestNum = val; }
		}
		lineNumberSeparatorPos = longestNum + 6;
	}
	
	public TextAreaLine updateAndGetLongestLine() {
		calculateLongestLine();
		return longestLine;
	}
	
	protected void calculateLongestLine() {
		TextAreaLine q = null;
		for (TextAreaLine l : textDocument) {
			int val = l.getText().length();
			if (q == null) { q = l; }
			else if (val > q.getText().length()) { q = l; }
		}
		longestLine = q;
		//horizontalScroll.setHighVal(valIn)
	}
	
	protected void disableLineNumbers() {
		recreateDrawnLines(currentPosY);
	}
	
	protected void enableLineNumbers() {
		calculateLineNumberSeparatorPos();
		recreateDrawnLines(currentPosY);
	}
	
	public EGuiTextArea setDrawLineNumbers(boolean val) {
		if (val != drawLineNumbers) {
			drawLineNumbers = val;
			if (val) { enableLineNumbers(); }
			else { disableLineNumbers(); }
			return this;
		}
		drawLineNumbers = val;
		return this;
	}
	
	public TextAreaLine getLineWithText(String textIn) {
		for (TextAreaLine l : textDocument) {
			//System.out.println(l.getText());
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
	
	public int getLineCount() { return height / lineHeight; }
	public int getCurrentDrawnLine() { return currentLine != null ? currentLine.getDrawnLineNumber() : -1; }
	public int getDrawWidth() { return width - (drawLineNumbers ? lineNumberSeparatorPos : 0); }
	public int getLineNumberSeparatorPos() { return lineNumberSeparatorPos; }
	public int getCurrentVerticalPos() { return currentPosY; }
	public int getCurrentHorizontalPos() { return currentPosX; }
	public boolean getDrawLineNumbers() { return drawLineNumbers; }
	public boolean isEditable() { return editable; }
	public EGuiTextArea setLineHeight(int heightIn) { lineHeight = heightIn; return this; }
	public EGuiTextArea setEditable(boolean val) { editable = val; return this; }
	public TextAreaLine getCurrentLine() { return currentLine; }
	public TextAreaLine getTextLine(int numIn) { return numIn >= 0 && numIn < textDocument.size() ? textDocument.get(numIn) : null; }
	public EArrayList<TextAreaLine> getTextDocument() { return textDocument; }
}
