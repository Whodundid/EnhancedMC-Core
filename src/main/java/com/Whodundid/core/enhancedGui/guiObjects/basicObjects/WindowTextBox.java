package com.Whodundid.core.enhancedGui.guiObjects.basicObjects;

import com.Whodundid.core.enhancedGui.types.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;

public class WindowTextBox extends EnhancedGuiObject {
	
	StorageBoxHolder<String, Integer> lines = new StorageBoxHolder();
	private boolean centered = false;
	private boolean shadowed = false;
	private boolean drawBackground = true;
	private int maxWidth = -1;
	private int maxHeight = -1;
	private int border = 5;
	private int lineGap = 0;
	private int xPos, yPos;
	
	//--------------------------
	//WindowTextBox Constructors
	//--------------------------
	
	public WindowTextBox(IEnhancedGuiObject parent, int x, int y) { this(parent, x, y, null); }
	public WindowTextBox(IEnhancedGuiObject parent, int x, int y, EArrayList<String> linesIn) {
		init(parent);
		xPos = x;
		yPos = y;
		EUtil.ifNotNullDo(linesIn, arr -> arr.forEach(l -> addLine(l, 0xffffff)));
		redimension();
	}
	
	//---------------------------
	//EnhancedGuiObject Overrides
	//---------------------------
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		
		//draw background
		if (drawBackground) {
			drawRect(startX, startY, startX + width, startY + height, EColors.black);
			drawRect(startX + 1, startY + 1, startX + width - 1, startY + height - 1, 0xff202020);
			drawRect(startX + 2, startY + 2, startX + width - 2, startY + height - 2, 0xff282828);
			drawRect(startX + 3, startY + 3, startX + width - 3, startY + height - 2, 0xff303030);
		}
		
		//draw contents scissored
		
		int scissorX = maxWidth > 0 ? 0 : startX;
		int scissorY = maxHeight > 0 ? 0 : startY;
		int scissorW = maxWidth > 0 ? res.getScaledWidth() : startX + maxWidth + border;
		int scissorH = maxHeight > 0 ? res.getScaledHeight() : startY + maxHeight + border;
		
		scissor(scissorX, scissorY, scissorW, scissorH);
		
		int yPos = startY + border + 5;
		for (StorageBox<String, Integer> line : lines) {
			drawLine(line.getObject(), line.getValue(), yPos);
			
			yPos += mc.fontRendererObj.FONT_HEIGHT + lineGap;
		}
		
		endScissor();
		
		super.drawObject(mXIn, mYIn);
	}
	
	//---------------------
	//WindowTextBox Methods
	//---------------------
	
	public WindowTextBox addLine(String lineIn) { return addLine(lineIn, 0xffffff); }
	public WindowTextBox addLine(String lineIn, int color) {
		EUtil.ifNotNullDo(parseLine(lineIn, color), l -> lines.addAll(l));
		redimension();
		return this;
	}
	
	//---------------------
	//WindowTextBox Getters
	//---------------------
	
	public int getMaxWidth() { return maxWidth; }
	public int getMaxHeight() { return maxHeight; }
	
	//---------------------
	//WindowTextBox Setters
	//---------------------
	
	public WindowTextBox setMaxWidth(int widthIn) { maxWidth = widthIn; redimension(); return this; }
	public WindowTextBox setMaxHeight(int heightIn) { maxHeight = heightIn; redimension(); return this; }
	public WindowTextBox setBorderSize(int sizeIn) { border = sizeIn; redimension(); return this; }
	public WindowTextBox setLineGap(int gapIn) { lineGap = gapIn; redimension(); return this; }
	public WindowTextBox setDrawBackground(boolean val) { drawBackground = val; return this; }
	
	//------------------------------
	//WindowTextBox Internal Methods
	//------------------------------
	
	private EArrayList<StorageBox<String, Integer>> parseLine(String lineIn, int colorIn) {
		if (lineIn != null) {
			
			EArrayList<String> newLineCheck = new EArrayList();
			
			//check for new line characters
			int pos = 0;
			for (int i = 0; i < lineIn.length(); i++) {
				if (lineIn.charAt(i) == '\n') {
					
					newLineCheck.add(lineIn.substring(pos, i));
					pos = i;
				}
			}
			
			EArrayList<String> widthAdjusted = new EArrayList();
			
			for (String s : newLineCheck) {
				widthAdjusted.addAll(EUtil.createWordWrapString(s, maxWidth));
			}
			
			EArrayList<StorageBox<String, Integer>> createdLines = new EArrayList();
			
			for (String s : widthAdjusted) {
				createdLines.add(new StorageBox<String, Integer>(s, colorIn));
			}
			
			return createdLines;
		}
		
		return null;
	}
	
	private void redimension() {
		if (lines.isNotEmpty()) {
			int longest = 0;
			for (StorageBox<String, Integer> box : lines) {
				String s = box.getObject();
				int len = mc.fontRendererObj.getStringWidth(s);
				if (len > longest) { longest = len; }
			}
			
			int w = longest + border;
			int h = lines.size() * mc.fontRendererObj.FONT_HEIGHT + (lines.size() * lineGap);
			int x = xPos - (w / 2);
			int y = yPos - (h / 2);
			
			setDimensions(x, y, w, h);
		}
		else {
			setDimensions(xPos - border, yPos - border, xPos + border, yPos + border);
		}
	}
	
	private void drawLine(String s, int color, int i) {
		if (s != null) {
			if (centered) {
				if (shadowed) { drawStringCS(s, xPos, startY + (i * 9) + (i > 0 ? i * lineGap : 0), color); }
				else { drawStringC(s, xPos, startY + (i * 9) + (i > 0 ? i * lineGap : 0), color); }
			}
			else {
				if (shadowed) { drawStringS(s, startX, startY + (i * 9) + (i > 0 ? i * lineGap : 0), color); }
				else { drawString(s, startX, startY + (i * 9) + (i > 0 ? i * lineGap : 0), color); }
			}
		}
	}
	
	//----------------------------
	//WindowTextBox Static Methods
	//----------------------------
	
	public static void drawBox(int x, int y, String... linesIn) {
		if (linesIn != null) {
			StorageBox<String, Integer>[] lines = new StorageBox[linesIn.length];
			for (int i = 0; i < linesIn.length; i++) { lines[i] = new StorageBox(linesIn[i], 0xffffff); }
			drawBox(x, y, 8, 3, true, true, lines);
		}
	}
	
	public static void drawBox(int x, int y, StorageBoxHolder<String, Integer> linesIn) {
		if (linesIn != null) { drawBox(x, y, 8, 3, true, true, linesIn.getBoxesAsArray()); }
	}
	
	public static void drawBox(int x, int y, StorageBox<String, Integer>[] linesIn) {
		drawBox(x, y, 8, 3, true, true, linesIn);
	}
	
	public static void drawBox(int xIn, int yIn, int borderIn, int lineGapIn, boolean centered, boolean shadowed, StorageBox<String, Integer>[] linesIn) {
		if (linesIn != null) {
			
			int longest = 0;
			for (StorageBox<String, Integer> box : linesIn) {
				String s = box.getObject();
				if (s != null) {
					int len = mc.fontRendererObj.getStringWidth(s);
					if (len > longest) { longest = len; }
				}
			}
			
			int w = longest + (borderIn * 2);
			int h = linesIn.length * mc.fontRendererObj.FONT_HEIGHT + (linesIn.length * lineGapIn) + (borderIn * 2);
			int x = xIn - (w / 2);
			int y = yIn - (h / 2);
			
			//draw background
			drawRect(x, y, x + w, y + h, EColors.black);
			drawRect(x + 1, y + 1, x + w - 1, y + h - 1, 0xff202020);
			drawRect(x + 2, y + 2, x + w - 2, y + h - 2, 0xff282828);
			drawRect(x + 3, y + 3, x + w - 3, y + h - 2, 0xff303030);
			
			int ty = y + borderIn + (lineGapIn / 2) + (lineGapIn % 2);
			
			int i = 0;
			for (StorageBox<String, Integer> box : linesIn) {
				if (box != null) {
					String s = box.getObject();
					int c = 0xffffff;
					
					//generics fuckery
					Object cVal = box.getValue();
					if (cVal instanceof EColors) { c = ((EColors) cVal).intVal; }
					else if (cVal instanceof Number) { c = ((Integer) cVal).intValue(); }
					
					//draw
					if (centered) {
						if (shadowed) { drawStringCS(s, xIn, ty + (i * 9) + (i > 0 ? i * lineGapIn : 0), c); }
						else { drawStringC(s, xIn, ty + (i * 9) + (i > 0 ? i * lineGapIn : 0), c); }
					}
					else {
						if (shadowed) { drawStringS(s, x + borderIn, ty + (i * 9) + (i > 0 ? i * lineGapIn : 0), c); }
						else { drawString(s, x + borderIn, ty + (i * 9) + (i > 0 ? i * lineGapIn : 0), c); }
					}
				}
				i++;
			}
			
		}
	}
	
}