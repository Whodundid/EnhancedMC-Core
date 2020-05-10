package com.Whodundid.core.terminal.gui;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.coreApp.CoreApp;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.colorPicker.EGuiColorPickerSimple;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.scrollList.EGuiScrollList;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.renderUtil.ScreenLocation;

public class TerminalOptions extends WindowParent {

	EGuiScrollList settings;
	EGuiButton drawLineNumbers, backColor, maxLines;
	
	public TerminalOptions() {
		aliases.add("termoptions", "toptions");
	}
	
	@Override
	public void initGui() {
		setDimensions(190, 110);
		setMinDims(75, 75);
		setResizeable(true);
		setObjectName("Terminal Settings");
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		settings = new EGuiScrollList(this, startX + 2, startY + 2, width - 4, height - 4);
		settings.setBackgroundColor(0xff303030);
		
		//Visual label
		EGuiLabel visual = new EGuiLabel(settings, startX + 8, startY + 10, "Visual", EColors.orange);
		
		settings.addObjectToList(false, visual);
		
		//buttons
		//drawLineNumbers = new EGuiButton(settings, startX + 12, visual.endY + 8, 60, 20, CoreApp.termLineNumbers);
		drawLineNumbers = new EGuiButton(settings, startX + 12, visual.endY + 8, 60, 20, "false");
		backColor = new EGuiButton(settings, startX + 13, drawLineNumbers.endY + 10, 20, 20) {
			@Override
			public void drawObject(int mXIn, int mYIn) {
				super.drawObject(mXIn, mYIn);
				drawHRect(backColor.startX - 1, backColor.startY - 1, backColor.endX + 1, backColor.endY + 1, 1, EColors.black);
				drawHRect(backColor.startX, backColor.startY, backColor.endX, backColor.endY, 1, EColors.lgray);
				drawHRect(backColor.startX + 1, backColor.startY + 1, backColor.endX - 1, backColor.endY - 1, 1, EColors.black);
			}
		};
		
		backColor.setDrawBackground(true);
		backColor.setBackgroundColor(CoreApp.termBackground.get());
		backColor.setTextures(null, null);
		
		drawLineNumbers.setActionReciever(this);
		backColor.setActionReciever(this);
		
		//labels
		EGuiLabel numberLabel = new EGuiLabel(settings, drawLineNumbers.endX + 10, drawLineNumbers.midY - 4, "Draw line numbers", EColors.lgray);
		EGuiLabel background = new EGuiLabel(settings, backColor.endX + 10, backColor.midY - 4, "Terminal background color", EColors.lgray);
		
		numberLabel.setHoverText("Displays the current line number in termainls");
		background.setHoverText("Sets the background color in terminals");
		
		//add to list
		settings.addObjectToList(false, drawLineNumbers, backColor);
		settings.addObjectToList(false, numberLabel, background);
		
		settings.fitItemsInList();
		
		addObject(settings);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		//System.out.println(this.getDimensions());
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void sendArgs(Object... args) {
		if (args.length == 2) {
			if (args[0] instanceof String && args[1] instanceof EMCApp) {
				String msg = (String) args[0];
				EMCApp mod = (EMCApp) args[1];
				
				if (msg.equals("Reload") && mod instanceof CoreApp) {
					int vPos = settings.getVScrollBar().getScrollPos();
					int hPos = settings.getHScrollBar().getScrollPos();
					reInitObjects();
					settings.getVScrollBar().onResizeUpdate(vPos, 0, 0, ScreenLocation.out);
					settings.getHScrollBar().onResizeUpdate(hPos, 0, 0, ScreenLocation.out);
				}
			}
		}
	}
	
	@Override
	public TerminalOptions resize(int xIn, int yIn, ScreenLocation areaIn) {
		try {
			if (xIn != 0 || yIn != 0) {
				int vPos = settings.getVScrollBar().getScrollPos();
				int hPos = settings.getHScrollBar().getScrollPos();
				super.resize(xIn, yIn, areaIn);
				settings.getVScrollBar().onResizeUpdate(vPos, xIn, yIn, areaIn);
				settings.getHScrollBar().onResizeUpdate(hPos, xIn, yIn, areaIn);
			}
		} catch (Exception e) { e.printStackTrace(); }
		return this;
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == drawLineNumbers) { lineNumbers(); }
		if (object == backColor) { changeColor(); }
		
		if (object instanceof EGuiColorPickerSimple) {
			if (args.length > 0) {
				try {
					int val = (int) args[0];
					CoreApp.termBackground.set(val);
					backColor.setBackgroundColor(val);
					CoreApp.instance().getConfig().saveMainConfig();
				}
				catch (Exception e) { e.printStackTrace(); }
			}
		}
	}
	
	@Override public boolean isDebugWindow() { return true; }
	
	private void lineNumbers() {
		//CoreApp.termLineNumbers.set(!CoreApp.termLineNumbers.get());
		//drawLineNumbers.toggleTrueFalse(CoreApp.termLineNumbers, CoreApp.instance(), false);
	}
	
	private void changeColor() {
		EnhancedMC.displayWindow(new EGuiColorPickerSimple(this));
	}
	
}
