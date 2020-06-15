package com.Whodundid.core.terminal.window;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.coreApp.CoreApp;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.colorPicker.ColorPickerSimple;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.scrollList.WindowScrollList;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowLabel;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;

public class TerminalOptionsWindow extends WindowParent {

	WindowScrollList settings;
	WindowButton drawLineNumbers, backColor, maxLines;
	
	public TerminalOptionsWindow() {
		aliases.add("termoptions", "toptions");
	}
	
	@Override
	public void initWindow() {
		setDimensions(190, 110);
		setMinDims(75, 75);
		setResizeable(true);
		setObjectName("Terminal Settings");
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		settings = new WindowScrollList(this, startX + 2, startY + 2, width - 4, height - 4);
		settings.setBackgroundColor(0xff303030);
		
		//Visual label
		WindowLabel visual = new WindowLabel(settings, startX + 8, startY + 10, "Visual", EColors.orange);
		
		settings.addObjectToList(false, visual);
		
		//buttons
		//drawLineNumbers = new EGuiButton(settings, startX + 12, visual.endY + 8, 60, 20, CoreApp.termLineNumbers);
		drawLineNumbers = new WindowButton(settings, startX + 12, visual.endY + 8, 60, 20, "false");
		backColor = new WindowButton(settings, startX + 13, drawLineNumbers.endY + 10, 20, 20) {
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
		
		drawLineNumbers.setActionReceiver(this);
		backColor.setActionReceiver(this);
		
		//labels
		WindowLabel numberLabel = new WindowLabel(settings, drawLineNumbers.endX + 10, drawLineNumbers.midY - 4, "Draw line numbers", EColors.lgray);
		WindowLabel background = new WindowLabel(settings, backColor.endX + 10, backColor.midY - 4, "Terminal background color", EColors.lgray);
		
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
	public TerminalOptionsWindow resize(int xIn, int yIn, ScreenLocation areaIn) {
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
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == drawLineNumbers) { lineNumbers(); }
		if (object == backColor) { changeColor(); }
		
		if (object instanceof ColorPickerSimple) {
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
		EnhancedMC.displayWindow(new ColorPickerSimple(this));
	}
	
}
