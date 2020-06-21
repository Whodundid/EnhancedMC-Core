package com.Whodundid.pingDrawer.window;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.app.config.AppConfigSetting;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowTextField;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.colorPicker.ColorPickerSimple;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.scrollList.WindowScrollList;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowLabel;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowRect;
import com.Whodundid.core.windowLibrary.windowObjects.utilityObjects.ColorButton;
import com.Whodundid.core.windowLibrary.windowObjects.utilityObjects.ScreenLocationSelector;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventFocus;
import com.Whodundid.pingDrawer.PingApp;
import com.Whodundid.pingDrawer.util.PingResources;
import net.minecraft.util.MathHelper;

//Author: Hunter Bragg

public class PingAppSettingsWindow extends WindowParent {

	PingApp pingApp = (PingApp) RegisteredApps.getApp(AppType.PING);
	private int vPos = 0;
	private int hPos = 0;
	WindowScrollList list;
	
	//threshold
	WindowLabel zeroLabel, infinityLabel;
	WindowTextField low2;
	ColorButton low;
	
	WindowTextField med1, med2;
	ColorButton med;
	
	WindowTextField high1, high2;
	ColorButton high;
	
	WindowTextField vhigh1;
	ColorButton vhigh;
	
	WindowButton enableTab, reset;
	
	//personal
	WindowButton drawOwn, chatDraw, hudDraw, useThresholds, ownReset;
	ColorButton ownColor;
	ScreenLocationSelector locationSelector;
	
	//----------------------------------
	//PingAppSettingsWindow Constructors
	//----------------------------------
	
	public PingAppSettingsWindow() {
		super();
		aliases.add("pingsettings", "psettings");
		windowIcon = PingResources.icon1;
	}
	
	//----------------------------
	//IEnhancedGuiObject Overrides
	//----------------------------
	
	@Override
	public void initWindow() {
		setObjectName("Ping Drawer Settings");
		defaultDims();
		setMinDims(defaultWidth, 75);
		setResizeable(true);
		setMaximizable(true);
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		list = new WindowScrollList(this, startX + 2, startY + 2, width - 4, height - 4);
		list.setBackgroundColor(0xff292929);
		
		int thresY = addThreshold(0);
		int ownY = addOwn(thresY);
		
		WindowRect divider = new WindowRect(list, 0, thresY + 7, list.getListDimensions().endX, thresY + 8, EColors.black);
		list.addObjectToList(divider);
		list.addToIgnoreList(divider);
		
		list.fitItemsInList(6, 6);
		
		addObject(list);
	}
	
	@Override
	public void preReInit() {
		vPos = list.getVScrollBar().getScrollPos();
		hPos = list.getHScrollBar().getScrollPos();
	}
	
	@Override
	public void postReInit() {
		list.getVScrollBar().setScrollBarPos(vPos);
		list.getHScrollBar().setScrollBarPos(hPos);
	}
	
	@Override
	public PingAppSettingsWindow resize(int xIn, int yIn, ScreenLocation areaIn) {
		int vPos = list.getVScrollBar().getScrollPos();
		int hPos = list.getHScrollBar().getScrollPos();
		
		super.resize(xIn, yIn, areaIn);
		
		list.getVScrollBar().onResizeUpdate(vPos, xIn, yIn, areaIn);
		list.getHScrollBar().onResizeUpdate(hPos, xIn, yIn, areaIn);
		return this;
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object instanceof ColorPickerSimple) {
			ColorPickerSimple picker = (ColorPickerSimple) object;
			Object typeObj = picker.getStoredObject();
			
			if (typeObj instanceof String) {
				String type = (String) typeObj;
				if (args.length == 1 && args[0] instanceof Integer) {
					int color = (int) args[0];
					
					switch (type) {
					case "own": PingApp.ownColor.set(color); ownColor.setColor(color); pingApp.getConfig().saveAllConfigs(); break;
					case "low": PingApp.lowColor.set(color); updateValues(); pingApp.getConfig().saveAllConfigs(); break;
					case "med": PingApp.medColor.set(color); updateValues(); pingApp.getConfig().saveAllConfigs(); break;
					case "high": PingApp.highColor.set(color); updateValues(); pingApp.getConfig().saveAllConfigs(); break;
					case "vhigh": PingApp.vhighColor.set(color); updateValues(); pingApp.getConfig().saveAllConfigs(); break;
					}
				}
			}
			
		}
		
		if (object == low2) {
			try {
				int val = Integer.parseInt(low2.getText());
				
				val = MathHelper.clamp_int(val, 1, PingApp.medThresh.get() - 1);
				
				PingApp.lowThresh.set(val);
				updateValues();
			}
			catch (Exception e) {
				low2.setText("" + PingApp.lowThresh.get());
				low2.setTextColor(PingApp.lowColor.get());
			}
		}
		
		if (object == med2) {
			try {
				int val = Integer.parseInt(med2.getText());
				
				val = MathHelper.clamp_int(val, PingApp.lowThresh.get() + 1, PingApp.highThresh.get() - 1);
				
				PingApp.medThresh.set(val);
				updateValues();
			}
			catch (Exception e) {
				med2.setText("" + PingApp.medThresh.get());
				med2.setTextColor(PingApp.medColor.get());
			}
		}
		
		if (object == high2) {
			try {
				int val = Integer.parseInt(high2.getText());
				
				val = MathHelper.clamp_int(val, PingApp.medThresh.get() + 1, Integer.MAX_VALUE);
				
				PingApp.highThresh.set(val);
				updateValues();
			}
			catch (Exception e) {
				high2.setText("" + PingApp.highThresh.get());
				high2.setTextColor(PingApp.highColor.get());
			}
		}
		
		if (object == reset) {
			AppConfigSetting.reset(PingApp.lowThresh, PingApp.medThresh, PingApp.highThresh, PingApp.lowColor, PingApp.medColor, PingApp.highColor, PingApp.vhighColor);
			updateValues();
		}
		
		if (object == low) { openColorPicker("Set Low Threshold Color", "low", PingApp.lowColor.get()); }
		if (object == med) { openColorPicker("Set Medium Threshold Color", "med", PingApp.medColor.get()); }
		if (object == high) { openColorPicker("Set High Threshold Color", "high", PingApp.highColor.get()); }
		if (object == vhigh) { openColorPicker("Set Very High Threshold Color", "vhigh", PingApp.vhighColor.get()); }
		if (object == ownColor) { openColorPicker("Set Personal Ping Color", "own", PingApp.ownColor.get()); }
		
		if (object == chatDraw) { chatDraw.toggleTrueFalse(PingApp.drawWithChat, pingApp, true); }
		if (object == hudDraw) { hudDraw.toggleTrueFalse(PingApp.drawWithHud, pingApp, true); }
		if (object == useThresholds) { useThresholds.toggleTrueFalse(PingApp.drawOwnThresholds, pingApp, true); }
		if (object == enableTab) { enableTab.toggleTrueFalse(PingApp.enableTab, pingApp, true); }
		
		if (object == ownReset) {
			PingApp.ownColor.set(PingApp.ownColor.getDefault());
			ownColor.setColor(PingApp.ownColor.get());
		}
		
		if (object == drawOwn) {
			drawOwn.toggleTrueFalse(PingApp.drawOwn, pingApp, true);
			boolean val = PingApp.drawOwn.get();
			chatDraw.setEnabled(val);
			hudDraw.setEnabled(val);
		}
	}
	
	//-----------------------------------
	//CoreAppSettingsGui Internal Methods
	//-----------------------------------
	
	private int addThreshold(int yPos) {
		WindowLabel threshLabel = new WindowLabel(list, list.getListDimensions().midX, yPos + 4, "Tab List Ping Thresholds (ms)", EColors.orange);
		threshLabel.setDrawCentered(true);
		
		WindowRect labelDivider = new WindowRect(list, 0, threshLabel.endY + 2, list.getListDimensions().endX, threshLabel.endY + 3, EColors.black);
		WindowRect labelBack = new WindowRect(list, 0, yPos, list.getListDimensions().endX, labelDivider.startY, EColors.dsteel);
		
		int x = list.getListDimensions().midX - (list.isVScrollDrawn() ? 4 : 1);
		int w = 100;
		
		enableTab = new WindowButton(list, x - w, threshLabel.endY + 8, 60, 20, PingApp.enableTab);
		WindowLabel enableTabLabel = new WindowLabel(list, enableTab.endX + 10, enableTab.startY + 6, "Enable Enhanced Tab", EColors.lgray);
		
		WindowRect blackBack = new WindowRect(list, x - w, enableTab.endY + 6, x + w, enableTab.endY + 106, EColors.black);
		WindowRect greyInner = new WindowRect(list, x - w + 1, blackBack.startY + 1, x + w - 1, blackBack.endY - 1, EColors.steel);
		
		low = new ColorButton(list, blackBack.endX - 32, blackBack.startY + 7, 18, 18);
		low2 = new WindowTextField(list, low.startX - 74, blackBack.startY + 8, 60, 16) {
			@Override
			public void onFocusLost(EventFocus e) {
				super.onFocusLost(e);
				performAction();
			}
		};
		WindowLabel lowRange = new WindowLabel(list, low2.startX - 11, low2.startY + 6, "-", EColors.lgray);
		zeroLabel = new WindowLabel(list, lowRange.startX - 14, low2.startY + 5, "0");
		
		med = new ColorButton(list, blackBack.endX - 32, low.endY + 5, 18, 18);
		med2 = new WindowTextField(list, med.startX - 74, low2.endY + 7, 60, 16) {
			@Override
			public void onFocusLost(EventFocus e) {
				super.onFocusLost(e);
				performAction();
			}
		};
		med1 = new WindowTextField(list, med2.startX - 79, low2.endY + 7, 60, 16);
		WindowLabel medRange = new WindowLabel(list, med2.startX - 11, med2.startY + 5, "-", EColors.lgray);
		
		high = new ColorButton(list, blackBack.endX - 32, med.endY + 5, 18, 18);
		high2 = new WindowTextField(list, high.startX - 74, med2.endY + 7, 60, 16) {
			@Override
			public void onFocusLost(EventFocus e) {
				super.onFocusLost(e);
				performAction();
			}
		};
		high1 = new WindowTextField(list, high2.startX - 79, med2.endY + 7, 60, 16);
		WindowLabel highRange = new WindowLabel(list, high2.startX - 11, high2.startY + 5, "-", EColors.lgray);
		
		vhigh = new ColorButton(list, blackBack.endX - 32, high.endY + 5, 18, 18);
		vhigh1 = new WindowTextField(list, x - 85, high2.endY + 7, 60, 16);
		infinityLabel = new WindowLabel(list, vhigh1.endX + 20, vhigh1.startY + 5, "Infinity");
		WindowLabel vhighRange = new WindowLabel(list, vhigh1.endX + 8, vhigh1.startY + 5, "-", EColors.lgray);
		
		reset = new WindowButton(list, blackBack.startX, blackBack.endY + 6, 60, 20, "Reset");
		WindowLabel resetLabel = new WindowLabel(list, reset.endX + 10, reset.startY + 6, "Reset Threshold Values", EColors.lgray);
		
		//assign values
		updateValues();
		
		setEnabled(false, med1, high1, vhigh1);
		IActionObject.setActionReceiver(this, enableTab, low2, low, med2, med, high2, high, vhigh, reset);
		
		setClickable(false, zeroLabel, lowRange, medRange, highRange, infinityLabel, vhighRange);
		setClickable(false, blackBack, greyInner);
		
		setHoverText("Click to change", low, med, high, vhigh);
		
		reset.setStringColor(EColors.yellow);
		
		setHoverText("Draws each person's in numbers and can be toggled with 'shift + tab'.", enableTab, enableTabLabel);
		setHoverText("Resets all ping thresholds back to their default vaules along with their color.", reset, resetLabel);
		
		list.addAndIgnore(labelBack, labelDivider, threshLabel);
		list.addAndIgnore(blackBack, greyInner);
		list.addObjectToList(enableTab, enableTabLabel);
		list.addObjectToList(low2, low, zeroLabel, lowRange);
		list.addObjectToList(med1, med2, med, medRange);
		list.addObjectToList(high1, high2, high, highRange);
		list.addObjectToList(vhigh1, vhigh, infinityLabel, vhighRange);
		list.addObjectToList(reset, resetLabel);
		
		return reset.endY - list.getDimensions().startY;
	}
	
	private int addOwn(int yPos) {
		WindowLabel ownLabel = new WindowLabel(list, list.getListDimensions().midX, yPos + 11, "Personal Ping", EColors.orange);
		ownLabel.setDrawCentered(true);
		
		WindowRect labelDivider = new WindowRect(list, 0, ownLabel.endY + 2, list.getListDimensions().endX, ownLabel.endY + 3, EColors.black);
		WindowRect labelBack = new WindowRect(list, 0, yPos + 7, list.getListDimensions().endX, labelDivider.startY, EColors.dsteel);
		
		int w1 = 120;
		int x = list.getListDimensions().midX - (list.isVScrollDrawn() ? 4 : 1);
		int w = 100;
		int xPos = x - w;
		
		locationSelector = new ScreenLocationSelector(list, pingApp, list.getListDimensions().midX - (w1 / 2), ownLabel.endY + 39, w1);
		locationSelector.setDisplayName("ping");
		
		WindowRect divider = new WindowRect(list, 0, locationSelector.endY + 23, list.getListDimensions().endX, locationSelector.endY + 24, EColors.black);
		
		//buttons
		drawOwn = new WindowButton(list, xPos, locationSelector.endY + 30, 60, 20, PingApp.drawOwn);
		WindowLabel drawOwnLabel = new WindowLabel(list, drawOwn.endX + 10, drawOwn.startY + 6, "Draw personal ping", EColors.lgray);
		
		chatDraw = new WindowButton(list, drawOwn.startX + 20, drawOwn.endY + 6, 60, 20, PingApp.drawWithChat);
		WindowLabel chatDrawLabel = new WindowLabel(list, chatDraw.endX + 10, chatDraw.startY + 6, "Draw with chat open", EColors.lgray);
		
		hudDraw = new WindowButton(list, drawOwn.startX + 20, chatDraw.endY + 6, 60, 20, PingApp.drawWithHud);
		WindowLabel hudDrawLabel = new WindowLabel(list, hudDraw.endX + 10, hudDraw.startY + 6, "Draw with hud open", EColors.lgray);
		
		useThresholds = new WindowButton(list, drawOwn.startX + 20, hudDraw.endY + 6, 60, 20, PingApp.drawOwnThresholds);
		WindowLabel useThresholdsLabel = new WindowLabel(list, useThresholds.endX + 10, useThresholds.startY + 6, "Draw thresholds", EColors.lgray);
		
		//lines
		WindowRect lineDown = new WindowRect(list, drawOwn.startX + 9, drawOwn.endY, drawOwn.startX + 10, useThresholds.midY, EColors.black);
		WindowRect lineChat = new WindowRect(list, lineDown.endX, chatDraw.midY - 1, chatDraw.startX, chatDraw.midY, EColors.black);
		WindowRect lineHud = new WindowRect(list, lineDown.endX, hudDraw.midY - 1, hudDraw.startX, hudDraw.midY, EColors.black);
		WindowRect lineThres = new WindowRect(list, lineDown.endX, useThresholds.midY - 1, useThresholds.startX, useThresholds.midY, EColors.black);
		
		//color
		ownColor = new ColorButton(list, xPos, useThresholds.endY + 6, 20, 20);
		WindowLabel ownColorLabel = new WindowLabel(list, ownColor.endX + 10, ownColor.startY + 6, "Personal ping draw color", EColors.lgray);
		
		ownReset = new WindowButton(list, xPos, ownColor.endY + 6, 100, 20, "Reset Color");
		
		WindowRect back = new WindowRect(list, 0, labelDivider.endY, list.getListDimensions().endX, divider.startY, 0xff222222);
		
		//assign values
		ownReset.setStringColor(EColors.yellow);
		
		ownColor.setHoverText("Click to change");
		ownColor.setColor(PingApp.ownColor.get());
		
		IActionObject.setActionReceiver(this, ownColor, ownReset, drawOwn, chatDraw, hudDraw, useThresholds);
		
		setHoverText("Enables drawing your personal ping at the selected location on screen.", drawOwn, drawOwnLabel);
		setHoverText("Sets the color for which your pesonal ping will be drawn as on the hud.", ownColor, ownColorLabel);
		setHoverText("Draws your individual ping while the chat gui is open.", chatDraw, chatDrawLabel);
		setHoverText("Draws your individual ping while the EMC Hud is open.", hudDraw, hudDrawLabel);
		setHoverText("Draws your individual ping with the same colors for ping thresholds in tab.", useThresholds, useThresholdsLabel);
		
		back.setClickable(false);
		
		boolean val = PingApp.drawOwn.get();
		setEnabled(val, chatDraw, hudDraw, useThresholds);
		
		list.addAndIgnore(back, divider, labelBack, labelDivider, ownLabel);
		list.addObjectToList(locationSelector);
		list.addObjectToList(ownColor, ownReset, ownColorLabel);
		list.addObjectToList(drawOwn, drawOwnLabel);
		list.addObjectToList(chatDraw, chatDrawLabel);
		list.addObjectToList(hudDraw, hudDrawLabel);
		list.addObjectToList(useThresholds, useThresholdsLabel);
		list.addAndIgnore(lineDown, lineChat, lineHud, lineThres);
		
		return ownReset.endY - list.getDimensions().startY;
	}
	
	private void updateValues() {
		zeroLabel.setColor(EColors.gray);
		low2.setText("" + pingApp.lowThresh.get()).setTextColor(PingApp.lowColor.get());
		
		med1.setText("" + (pingApp.lowThresh.get() + 1)).setTextColor(PingApp.medColor.get());
		med2.setText("" + pingApp.medThresh.get()).setTextColor(PingApp.medColor.get());
		
		high1.setText("" + (pingApp.medThresh.get() + 1)).setTextColor(PingApp.highColor.get());
		high2.setText("" + pingApp.highThresh.get()).setTextColor(PingApp.highColor.get());
		
		vhigh1.setText("" + (pingApp.highThresh.get() + 1)).setTextColor(PingApp.highColor.get());
		infinityLabel.setColor(PingApp.vhighColor.get());
		
		low.setColor(PingApp.lowColor.get());
		med.setColor(PingApp.medColor.get());
		high.setColor(PingApp.highColor.get());
		vhigh.setColor(PingApp.vhighColor.get());
	}
	
	private void openColorPicker(String title, String type, int colorIn) {
		ColorPickerSimple cp = new ColorPickerSimple(this, colorIn);
		EnhancedMC.displayWindow(cp);
		cp.getHeader().setTitle(title);
		EnhancedMC.getRenderer().setFocusLockObject(cp);
		cp.setStoredObject(type);
	}
	
}
