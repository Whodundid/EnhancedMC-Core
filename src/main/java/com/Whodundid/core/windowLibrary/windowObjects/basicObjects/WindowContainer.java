package com.Whodundid.core.windowLibrary.windowObjects.basicObjects;

import com.Whodundid.core.windowLibrary.windowTypes.WindowObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;

//Author: Hunter Bragg

public class WindowContainer extends WindowObject {
	
	public String title = "";
	//public int displayStringColor = 0x47ff96;
	public int titleColor = 0xffffff;
	public int titleBorderColor = 0xff000000;
	public int titleBackgroundColor = 0xff222222;
	public int borderColor = 0xff000000;
	public int backgroundColor = 0xff4c4c4c;
	protected boolean drawTitle = true;
	protected boolean drawBackground = true;
	protected boolean drawBorder = true;
	protected boolean useCustomWidth = false;
	protected boolean drawTitleFullWidth = true;
	protected boolean center = false;
	protected int titleWidth = 0;
	protected int titleAreaWidth = 0;
	protected int titleAreaHeight = 0;
	
	public WindowContainer(IWindowObject parentIn, int xIn, int yIn, int widthIn, int heightIn) {
		this(parentIn, xIn, yIn, widthIn, heightIn, true);
	}
	
	public WindowContainer(IWindowObject parentIn, int xIn, int yIn, int widthIn, int heightIn, boolean drawTitleIn) {
		init(parentIn, xIn, yIn, widthIn, heightIn);
		drawTitle = drawTitleIn;
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		if (drawBorder) { drawRect(startX, startY, endX, endY, borderColor); } //border
		if (drawBackground) { drawRect(startX + 1, startY + 1, endX - 1, endY - 1, backgroundColor); } //inner
		if (drawTitle) {
			titleAreaHeight = height >= 18 ? 18 : height;
			int drawWidth = titleWidth + 6;
			if (useCustomWidth) { drawWidth = titleAreaWidth; }
			if (drawTitleFullWidth) { drawWidth = width - 1; }
			drawRect(startX + 1, startY + 1, startX + drawWidth + 1, startY + titleAreaHeight, titleBorderColor);
			drawRect(startX + 1, startY + 1, startX + drawWidth, startY + titleAreaHeight - 1, titleBackgroundColor);
			if (center) { drawStringCS(title, midX, startY + 5, titleColor); }
			else { drawStringS(title, startX + 4, startY + 5, titleColor); }
			
		}
		
		scissor(startX + 1, startY + titleAreaHeight + 1, endX - 1, endY - 1);
		super.drawObject(mXIn, mYIn);
		endScissor();
	}
	
	public int getTitle() { return titleWidth; }
	public int getTitleAreaWidth() { return titleAreaWidth; }
	public int getTitleAreaHeight() { return titleAreaHeight; }
	
	public WindowContainer setTitle(String stringIn) { title = stringIn; titleWidth = mc.fontRendererObj.getStringWidth(stringIn); return this; }
	public WindowContainer setTitleColor(int colorIn) { titleColor = colorIn; return this; }
	public WindowContainer setBorderColor(int colorIn) { borderColor = colorIn; return this; }
	public WindowContainer setBackgroundColor(int colorIn) { backgroundColor = colorIn; return this; }
	public WindowContainer setTitleBorderColor(int colorIn) { titleBorderColor = colorIn; return this; }
	public WindowContainer setTitleBackgroundColor(int colorIn) { titleBackgroundColor = colorIn; return this; }
	public WindowContainer setDrawTitle(boolean val) { drawTitle = val; return this; }
	public WindowContainer setDrawBackground(boolean val) { drawBackground = val; return this; }
	public WindowContainer setDrawBorder(boolean val) { drawBorder = val; return this; }
	public WindowContainer setTitleWidth(int widthIn) { if (widthIn > 0) { useCustomWidth = true; titleAreaWidth = widthIn; } return this; }
	public WindowContainer setTitleFullWidth(boolean val) { drawTitleFullWidth = val; return this; }
	public WindowContainer setTitleCentered(boolean val) { center = val; return this; }
	
}
