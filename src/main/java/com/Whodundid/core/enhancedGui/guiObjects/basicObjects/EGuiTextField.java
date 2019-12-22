package com.Whodundid.core.enhancedGui.guiObjects.basicObjects;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiUtil.EObjectGroup;
import com.Whodundid.core.enhancedGui.objectEvents.EventFocus;
import com.Whodundid.core.enhancedGui.types.EnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

//Last edited: Jan 2, 2019
//First Added: Oct 2, 2018
//Author: Hunter Bragg

public class EGuiTextField extends EnhancedActionObject {
	
	public String text = "", textWhenEmpty = "";
	public int maxStringLength = 32;
	public int textWhenEmptyColor = 0xb2b2b2;
	public int enabledColor = 14737632;
	public int disabledColor = 7368816;
	public int mainDrawColor = enabledColor;
	public int backgroundColor = 0xff000000;
	public int borderColor = -6250336;
	protected boolean editable = true;
	protected boolean enableBackgroundDrawing = true;
	protected boolean alwaysDrawCursor = false;
	protected boolean onlyAcceptLetters = false;
	protected boolean onlyAcceptNumbers = false;
	protected boolean allowClipboardPastes = true;
	protected boolean useObjectGroupForCursorDraw = false;
	protected int cursorCounter;
	protected int lineScrollOffset;
	protected int cursorPosition;
	protected int selectionEnd;

	protected EGuiTextField() {}
	public EGuiTextField(IEnhancedGuiObject parentIn, int x, int y, int widthIn, int heightIn) {
		init(parentIn, x, y, widthIn, heightIn);
		Keyboard.enableRepeatEvents(true);
	}
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		if (getEnableBackgroundDrawing()) {
			drawRect(startX - 1, startY - 1, endX + 3, endY + 1, borderColor);
			drawRect(startX, startY, endX + 2, endY, backgroundColor);
		}
		
		int drawColor = isEnabled() ? mainDrawColor : disabledColor;
		//System.out.println(this + " " + drawColor);
		int j = cursorPosition - lineScrollOffset;
		int k = selectionEnd - lineScrollOffset;
		String s = fontRenderer.trimStringToWidth(text.substring(lineScrollOffset), getWidth());
		
		boolean flag = j >= 0 && j <= s.length();
		boolean drawCursorFlag = false;
		boolean cursorCounterFlag = EnhancedMC.updateCounter / 20 % 2 == 0 && flag;
		
		if (alwaysDrawCursor) { drawCursorFlag = cursorCounterFlag; }
		else if (useObjectGroupForCursorDraw) {
			EObjectGroup g = getObjectGroup();
			if (g != null) { drawCursorFlag = g.doAnyHaveFocus() && cursorCounterFlag; }
		}
		else { drawCursorFlag = hasFocus() && cursorCounterFlag && isEnabled(); }
		
		int l = enableBackgroundDrawing ? startX + 4 : startX + 2;
		int i1 = enableBackgroundDrawing ? startY + (height - 6) / 2 : startY + height / 2 - 3;
		int j1 = l;
		
		if (k > s.length()) { k = s.length(); }
		if (s.length() > 0) {
			String s1 = flag ? s.substring(0, j) : s;
			j1 = drawStringWithShadow(s1, l, i1, drawColor);
		}
		
		boolean flag2 = cursorPosition < text.length() || text.length() >= getMaxStringLength();
		int k1 = j1;
		
		if (!flag) { k1 = j > 0 ? l + width : l; } 
		else if (flag2) {
			k1 = j1 - 1;
			j1--;
		}
		
		if (s.length() > 0 && flag && j < s.length()) {
			j1 = drawStringWithShadow(s.substring(j), j1, i1, drawColor);
		}
		
		if (drawCursorFlag) {
			if (flag2) { drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + fontRenderer.FONT_HEIGHT, -3092272); }
			else { drawStringWithShadow("_", k1, i1, drawColor); }
		}
		
		if (k != j) {
			int l1 = l + fontRenderer.getStringWidth(s.substring(0, k));
			drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + fontRenderer.FONT_HEIGHT);
		}
		super.drawObject(mX, mY, ticks);
	}
	
	@Override 
	public void mousePressed(int mX, int mY, int button) {
		super.mousePressed(mX, mY, button);
		if (hasFocus() && button == 0) {
			int i = mX - startX;
			if (enableBackgroundDrawing) { i -= 4; }
			String s = fontRenderer.trimStringToWidth(text.substring(lineScrollOffset), getWidth());
			setCursorPosition(fontRenderer.trimStringToWidth(s, i).length() + lineScrollOffset);
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (editable) {
			if (GuiScreen.isKeyComboCtrlA(keyCode)) {
				setCursorPositionEnd();
				setSelectionPos(0);
			} else if (GuiScreen.isKeyComboCtrlC(keyCode)) { GuiScreen.setClipboardString(getSelectedText()); }
			else if (GuiScreen.isKeyComboCtrlV(keyCode) && isEnabled() && allowClipboardPastes) { writeText(GuiScreen.getClipboardString()); }
			else if (GuiScreen.isKeyComboCtrlX(keyCode)) {
				GuiScreen.setClipboardString(getSelectedText());
				if (isEnabled()) { writeText(""); }
			} else {
				switch (keyCode) {
				case 14: //backspace
					if (GuiScreen.isCtrlKeyDown()) {
						if (isEnabled()) { deleteWords(-1); }
					} else if (isEnabled()) { deleteFromCursor(-1); }
					break;
				case 28: //enter
					if (getActionReciever() != null) { getActionReciever().actionPerformed(this); }
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
					break;
				case 205: //right
					if (GuiScreen.isShiftKeyDown()) {
						if (GuiScreen.isCtrlKeyDown()) { setSelectionPos(getNthWordFromPos(1, getSelectionEnd())); }
						else { setSelectionPos(getSelectionEnd() + 1); }
					} else if (GuiScreen.isCtrlKeyDown()) { setCursorPosition(getNthWordFromCursor(1)); } 
					else { moveCursorBy(1); }
					break;
				case 207: //end
					if (GuiScreen.isShiftKeyDown()) { setSelectionPos(text.length()); } 
					else { setCursorPositionEnd(); }
					break;
				case 211: //delete
					if (GuiScreen.isCtrlKeyDown()) {
						if (isEnabled()) { deleteWords(1); }
					} else if (isEnabled()) { deleteFromCursor(1); }
					break;
				case 1: //esc
					break;
				default:
					if (isEnabled() && ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
						if (onlyAcceptLetters) { if (Character.isLetter(typedChar)) { writeText(Character.toString(typedChar)); } }
						else if (onlyAcceptNumbers) { if (Character.isDigit(typedChar)) { writeText(Character.toString(typedChar)); } }
						else { writeText(Character.toString(typedChar)); }
					}
					break;
				}
			}
		}
	}
	
	@Override
	public void onFocusGained(EventFocus eventIn) {
		if (!alwaysDrawCursor) { EnhancedMC.updateCounter = 0; }
		if (text.equals(textWhenEmpty)) { text = ""; setTextColor(enabledColor); setCursorPosition(0); }
		super.onFocusGained(eventIn);
	}
	
	@Override
	public void onFocusLost(EventFocus eventIn) {
		if (text.isEmpty()) { text = textWhenEmpty; setTextColor(textWhenEmptyColor); }
	}
	
	public void writeText(String textIn) {
		String s = "";
		String s1 = ChatAllowedCharacters.filterAllowedCharacters(textIn);
		int i = cursorPosition < selectionEnd ? cursorPosition : selectionEnd;
		int j = cursorPosition < selectionEnd ? selectionEnd : cursorPosition;
		int k = maxStringLength - text.length() - (i - j);
		int l = 0;
		if (text.length() > 0) {
			s = s + text.substring(0, i);
		}
		if (k < s1.length()) {
			s = s + s1.substring(0, k);
			l = k;
		} else {
			s = s + s1;
			l = s1.length();
		}
		if (text.length() > 0 && j < text.length()) {
			s = s + text.substring(j);
		}
		text = s;
		moveCursorBy(i - selectionEnd + l);
	}

	/**
	 * Deletes the specified number of words starting at the cursor position.
	 * Negative numbers will delete words left of the cursor.
	 */
	public void deleteWords(int numberOfWords) {
		if (text.length() != 0) {
			if (selectionEnd != cursorPosition) {
				writeText("");
			} else {
				deleteFromCursor(getNthWordFromCursor(numberOfWords) - cursorPosition);
			}
		}
	}

	/**
	 * delete the selected text, otherwsie deletes characters from either side of
	 * the cursor. params: delete num
	 */
	public void deleteFromCursor(int p_146175_1_) {
		if (text.length() != 0) {
			if (selectionEnd != cursorPosition) {
				writeText("");
			} else {
				boolean flag = p_146175_1_ < 0;
				int i = flag ? cursorPosition + p_146175_1_ : cursorPosition;
				int j = flag ? cursorPosition : cursorPosition + p_146175_1_;
				String s = "";
				if (i >= 0) {
					s = text.substring(0, i);
				}
				if (j < text.length()) {
					s = s + text.substring(j);
				}
				text = s;
				if (flag) { moveCursorBy(p_146175_1_); }
			}
		}
	}

	/**
	 * see @getNthNextWordFromPos() params: N, position
	 */
	public int getNthWordFromCursor(int pos) { return getNthWordFromPos(pos, getCursorPosition());}

	/**
	 * gets the position of the nth word. N may be negative, then it looks
	 * backwards. params: N, position
	 */
	public int getNthWordFromPos(int posIn, int cursorPos) {
		return getNthWordFromPos(posIn, cursorPos, true);
	}

	public int getNthWordFromPos(int posIn, int cursorPos, boolean p_146197_3_) {
		int i = cursorPos;
		boolean flag = posIn < 0;
		int j = Math.abs(posIn);
		for (int k = 0; k < j; ++k) {
			if (!flag) {
				int l = text.length();
				i = text.indexOf(32, i);
				if (i == -1) {
					i = l;
				} else {
					while (p_146197_3_ && i < l && text.charAt(i) == 32) {
						++i;
					}
				}
			} else {
				while (p_146197_3_ && i > 0 && text.charAt(i - 1) == 32) {
					--i;
				}
				while (i > 0 && text.charAt(i - 1) != 32) {
					--i;
				}
			}
		}
		return i;
	}

	/**
	 * draws the vertical line cursor in the textbox
	 */
	protected void drawCursorVertical(int p_146188_1_, int p_146188_2_, int p_146188_3_, int p_146188_4_) {
		if (p_146188_1_ < p_146188_3_) {
			int i = p_146188_1_;
			p_146188_1_ = p_146188_3_;
			p_146188_3_ = i;
		}
		if (p_146188_2_ < p_146188_4_) {
			int j = p_146188_2_;
			p_146188_2_ = p_146188_4_;
			p_146188_4_ = j;
		}
		if (p_146188_3_ > startX + width) { p_146188_3_ = startX + width; }
		if (p_146188_1_ > startX + width) { p_146188_1_ = startX + width; }
		
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
		GlStateManager.disableTexture2D();
		GlStateManager.enableColorLogic();
		GlStateManager.colorLogicOp(5387);
		worldrenderer.begin(7, DefaultVertexFormats.POSITION);
		worldrenderer.pos(p_146188_1_, p_146188_4_, 0.0D).endVertex();
		worldrenderer.pos(p_146188_3_, p_146188_4_, 0.0D).endVertex();
		worldrenderer.pos(p_146188_3_, p_146188_2_, 0.0D).endVertex();
		worldrenderer.pos(p_146188_1_, p_146188_2_, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.disableColorLogic();
		GlStateManager.enableTexture2D();
	}
	
	public String getSelectedText() {
		int startPos = cursorPosition < selectionEnd ? cursorPosition : selectionEnd;
		int endPos = cursorPosition < selectionEnd ? selectionEnd : cursorPosition;
		return text.substring(startPos, endPos);
	}
	
	public EGuiTextField setCursorPosition(int posIn) {
		cursorPosition = posIn;
		int i = text.length();
		cursorPosition = MathHelper.clamp_int(cursorPosition, 0, i);
		setSelectionPos(cursorPosition);
		return this;
	}

	public EGuiTextField setMaxStringLength(int lengthIn) {
		maxStringLength = lengthIn;
		if (text.length() > lengthIn) { text = text.substring(0, lengthIn); }
		return this;
	}
	
	/** Sets the position of the selection anchor (i.e. position the selection was started at) */
	public EGuiTextField setSelectionPos(int posIn) {
		int i = text.length();
		if (posIn > i) { posIn = i; }
		if (posIn < 0) { posIn = 0; }
		selectionEnd = posIn;
		
		if (fontRenderer != null) {
			if (lineScrollOffset > i) { lineScrollOffset = i; }
			int j = getWidth();
			String s = fontRenderer.trimStringToWidth(text.substring(lineScrollOffset), j);
			int k = s.length() + lineScrollOffset;
			if (posIn == lineScrollOffset) { lineScrollOffset -= fontRenderer.trimStringToWidth(text, j, true).length(); }
			if (posIn > k) { lineScrollOffset += posIn - k; } 
			else if (posIn <= lineScrollOffset) { lineScrollOffset -= lineScrollOffset - posIn; }
			lineScrollOffset = MathHelper.clamp_int(lineScrollOffset, 0, i);
		}
		return this;
	}

	public EGuiTextField setText(String textIn) {
		if (textIn != null) {
			if (textIn.isEmpty()) {
				text = textWhenEmpty;
				setTextColor(textWhenEmptyColor);
			}
			else {
				if (textIn.length() > maxStringLength) { text = textIn.substring(0, maxStringLength); } 
				else {
					String filtered = textIn.replaceAll("\t", "   ");
					text = filtered;
				}
				setTextColor(enabledColor);
			}
		}
		else { text = ""; }
		setCursorPositionEnd();
		return this;
	}
	
	public EGuiTextField setCursorPositionZero() { setCursorPosition(0); return this; }
	public EGuiTextField setCursorPositionEnd() { setCursorPosition(text.length()); return this; }
	public EGuiTextField setEnableBackgroundDrawing(boolean val) { enableBackgroundDrawing = val; return this; }
	public EGuiTextField setEditable(boolean val) { editable = val; return this; }
	public EGuiTextField setTextColor(int colorIn) { mainDrawColor = colorIn; return this; }
	public EGuiTextField setDisabledTextColour(int colorIn) { disabledColor = colorIn; return this; }
	public EGuiTextField setAlwaysDrawCursor(boolean val) { alwaysDrawCursor = val; return this; }
	public EGuiTextField setUseObjectGroupForCursorDraws(boolean val) { useObjectGroupForCursorDraw = val; return this; }
	public EGuiTextField setOnlyAcceptLetters(boolean val) { onlyAcceptLetters = val; return this; }
	public EGuiTextField setOnlyAcceptNumbers(boolean val) { onlyAcceptNumbers = val; return this; }
	public EGuiTextField setAllowClipboardPastes(boolean val) { allowClipboardPastes = val; return this; }
	public EGuiTextField setTextWhenEmpty(String textIn) { textWhenEmpty = textIn; text = textWhenEmpty; setTextColor(textWhenEmptyColor); return this; }
	public EGuiTextField setTextWhenEmptyColor(int colorIn) { textWhenEmptyColor = colorIn; return this; }
	public EGuiTextField moveCursorBy(int moveAmount) { setCursorPosition(selectionEnd + moveAmount); return this; }
	public EGuiTextField clear() { setText(""); return this; }
	public EGuiTextField setBackgroundColor(int colorIn) { backgroundColor = colorIn; return this; }
	public EGuiTextField setBorderColor(int colorIn) { borderColor = colorIn; return this; }
	
	public void updateCursorCounter() { cursorCounter++; }
	public int getMaxStringLength() { return maxStringLength; }
	public int getCursorPosition() { return cursorPosition; }
	public boolean getEnableBackgroundDrawing() { return enableBackgroundDrawing; }
	public boolean onlyAcceptsletters() { return onlyAcceptLetters; }
	public boolean onlyAcceptsNumbers() { return onlyAcceptNumbers; }
	public boolean allowsClipboardPastes() { return allowClipboardPastes; }
	public int getSelectionEnd() { return selectionEnd; }
	public int getWidth() { return getEnableBackgroundDrawing() ? width - 8 : width; }
	public String getText() { return text; }
	public boolean isEmpty() { return text != null ? text.isEmpty() : true; }
	public boolean isNotEmpty() { return text != null ? !text.isEmpty() : false; }
}
