package com.Whodundid.core.coreApp;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton3Stage;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.scrollList.EGuiScrollList;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.guiObjects.windows.TutorialWindow;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.notifications.window.NotificationWindow;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.EColors;

//Author: Hunter Bragg

public class CoreAppSettingsGui extends WindowParent {
	
	CoreApp app = (CoreApp) RegisteredApps.getApp(AppType.CORE);
	public EGuiScrollList settingsList;
	
	public EGuiButton tutorial, notifications;
	public EGuiButton closeHudEmpty;
	public EGuiButton drawProxyBorder, enableTaskbar, drawCustomCursors;
	public EGuiButton3Stage drawChat;
	public EGuiButton showIncompats, showTerminal;
	
	public CoreAppSettingsGui() {
		super();
		aliases.add("emcsettings");
		windowIcon = EMCResources.emcSettingsIcon;
	}
	
	@Override
	public void initGui() {
		setObjectName("EMC Core App Settings");
		setDimensions(defaultWidth, defaultHeight);
		setMinDims(170, 75);
		setResizeable(true);
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		settingsList = new EGuiScrollList(this, startX + 2, startY + 20, width - 4, height - 22);
		settingsList.setBackgroundColor(0xff292929);
		
		int mY = addMenus(8);
		int fY = addFunction(mY);
		int vY = addVisual(fY);
		int dY = addDebug(vY);
		
		settingsList.fitItemsInList(6, 6);
		
		addObject(settingsList);
	}
	
	private int addMenus(int yPos) {
		EGuiLabel menusLabel = new EGuiLabel(settingsList, 6, yPos, "Menus", EColors.orange);
		
		tutorial = new EGuiButton(settingsList, 6, menusLabel.endY + 8, 120, 20, "EMC Tutorial");
		notifications = new EGuiButton(settingsList, 6, tutorial.endY + 4, 120, 20, "Notifications");
		
		tutorial.setDisplayStringColor(EColors.lime);
		notifications.setDisplayStringColor(EColors.seafoam);
		
		tutorial.setActionReceiver(this);
		notifications.setActionReceiver(this);
		
		settingsList.addObjectToList(menusLabel);
		settingsList.addObjectToList(tutorial, notifications);
		
		return notifications.endY - settingsList.getDimensions().startY;
	}
	
	private int addFunction(int yPos) {
		//build objects
		
		EGuiLabel functionLabel = new EGuiLabel(settingsList, 6, yPos + 10, "Functionality", EColors.orange);
		
		closeHudEmpty = new EGuiButton(settingsList, 6, functionLabel.endY + 8, 60, 20, CoreApp.closeHudWhenEmpty);
		EGuiLabel closeEmptyLabel = new EGuiLabel(settingsList, closeHudEmpty.endX + 10, closeHudEmpty.startY + 6, "Close Hud When Empty");
		
		//set values
		
		closeHudEmpty.setActionReceiver(this);
		
		closeEmptyLabel.setHoverText("Closes the hud if there are no windows or objects on screen.");
		closeEmptyLabel.setDisplayStringColor(0xb2b2b2);
		
		//add objects
		
		settingsList.addObjectToList(functionLabel);
		settingsList.addObjectToList(closeHudEmpty, closeEmptyLabel);
		
		return closeHudEmpty.endY - settingsList.getDimensions().startY;
	}
	
	private int addVisual(int yPos) {
		//build objects
		
		EGuiLabel visualLabel = new EGuiLabel(settingsList, 6, yPos + 10, "Visual", EColors.orange.c());
		
		drawChat = new EGuiButton3Stage(settingsList, 6, visualLabel.endY + 8, 60, 20, CoreApp.drawChatOnHud, EColors.yellow.intVal);
		EGuiLabel drawChatLabel = new EGuiLabel(settingsList, drawChat.endX + 10, drawChat.startY + 6, "Draw Chat On Hud");
		
		drawProxyBorder = new EGuiButton(settingsList, 6, drawChat.endY + 7, 60, 20, CoreApp.drawHudBorder);
		EGuiLabel drawProxyBorderLabel = new EGuiLabel(settingsList, drawProxyBorder.endX + 10, drawProxyBorder.startY + 6, "Draw Hud Border");
		
		enableTaskbar = new EGuiButton(settingsList, 6, drawProxyBorder.endY + 7, 60, 20, CoreApp.enableTaskBar);
		EGuiLabel enableTaskbarLabel = new EGuiLabel(this, enableTaskbar.endX + 10, enableTaskbar.startY + 6, "Enable Taskbar");
		
		drawCustomCursors = new EGuiButton(settingsList, 6, enableTaskbar.endY + 7, 60, 20, CoreApp.customCursors);
		EGuiLabel drawCustomCursorsLabel = new EGuiLabel(this, drawCustomCursors.endX + 10, drawCustomCursors.startY + 6, "Draw Custom Cursors");
		
		//set values
		
		drawChat.setActionReceiver(this);
		drawProxyBorder.setActionReceiver(this);
		enableTaskbar.setActionReceiver(this);
		drawCustomCursors.setActionReceiver(this);
		
		drawChatLabel.setHoverText("Determines how chat messages are drawn when the hud is open.");
		drawProxyBorderLabel.setHoverText("Draws a red border on the screen whenever the EMC hud is open.");
		enableTaskbarLabel.setHoverText("Enables a taskbar which displays currently running windows.");
		drawCustomCursorsLabel.setHoverText("Allows the cursor to change its appearance based off of what it is hovering over.");
		
		drawChatLabel.setDisplayStringColor(0xb2b2b2);
		drawProxyBorderLabel.setDisplayStringColor(0xb2b2b2);
		enableTaskbarLabel.setDisplayStringColor(0xb2b2b2);
		drawCustomCursorsLabel.setDisplayStringColor(0xb2b2b2);
		
		//add objects
		
		settingsList.addObjectToList(visualLabel);
		settingsList.addObjectToList(drawChat, drawChatLabel);
		settingsList.addObjectToList(drawProxyBorder, drawProxyBorderLabel);
		settingsList.addObjectToList(enableTaskbar, enableTaskbarLabel);
		settingsList.addObjectToList(drawCustomCursors, drawCustomCursorsLabel);
		
		return drawCustomCursors.endY - settingsList.getDimensions().startY;
	}
	
	private int addDebug(int yPos) {
		//build objects
		
		EGuiLabel debugLabel = new EGuiLabel(settingsList, 6, yPos + 10, "Debug", EColors.orange.c());
		
		showTerminal = new EGuiButton(settingsList, 6, debugLabel.endY + 8, 60, 20, CoreApp.enableTerminal);
		EGuiLabel terminalLabel = new EGuiLabel(settingsList, showTerminal.endX + 10, showTerminal.startY + 6, "Enable EMC Terminal");
		
		showIncompats = new EGuiButton(settingsList, 6, showTerminal.endY + 7, 60, 20, CoreApp.showIncompats);
		EGuiLabel incompatLabel = new EGuiLabel(settingsList, showIncompats.endX + 10, showIncompats.startY + 6, "Display Incompatible Apps");
		
		//set values
		
		showTerminal.setActionReceiver(this);
		showIncompats.setActionReceiver(this);
		
		terminalLabel.setHoverText("Terminal used for debug and advanced purposes.");
		incompatLabel.setHoverText("Displays incompatible EMC Apps within the main settings window.");
		
		terminalLabel.setDisplayStringColor(0xb2b2b2);
		incompatLabel.setDisplayStringColor(0xb2b2b2);
		
		//add objects
		
		settingsList.addObjectToList(debugLabel);
		settingsList.addObjectToList(showTerminal, terminalLabel);
		settingsList.addObjectToList(showIncompats, incompatLabel);
		
		return showIncompats.endY - settingsList.getDimensions().startY;
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		
		drawRect(startX + 1, startY + 19, endX - 1, endY - 1, -0x00cfcfcf); //grey background
		drawRect(startX, startY + 18, endX, startY + 19, 0xff000000); //top line
		
		drawStringCS("Enhanced MC Settings", midX, startY + 6, 0xffbb00);
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		//menus
		if (object == tutorial) { EnhancedMC.displayWindow(new TutorialWindow(), this, CenterType.object); }
		if (object == notifications) { EnhancedMC.displayWindow(new NotificationWindow(), this, CenterType.object); }
		//function
		if (object == closeHudEmpty) { closeHudEmpty.toggleTrueFalse(app.closeHudWhenEmpty, app, false); }
		//visual
		if (object == drawChat) { drawChat.updateDislay(app.drawChatOnHud, app, false); }
		if (object == drawProxyBorder) { drawProxyBorder.toggleTrueFalse(app.drawHudBorder, app, false); }
		if (object == enableTaskbar) { enableTaskbar.toggleTrueFalse(app.enableTaskBar, app, false); }
		if (object == drawCustomCursors) { drawCustomCursors.toggleTrueFalse(app.customCursors, app, false); }
		//debug
		if (object == showTerminal) { showTerminal.toggleTrueFalse(app.enableTerminal, app, false); }
		if (object == showIncompats) { showIncompats.toggleTrueFalse(app.showIncompats, app, false); }
	}
	
	@Override
	public void sendArgs(Object... args) {
		if (args.length == 1) {
			if (args[0] instanceof String) {
				String msg = (String) args[0];
				if (msg.equals("Reload")) {
					int pos = settingsList.getVScrollBar().getScrollPos();
					reInitObjects();
					settingsList.getVScrollBar().setScrollBarPos(pos);
				}
			}
		}
	}
	
	public void scrollToBottom() {
		if (settingsList != null) {
			settingsList.getVScrollBar().setScrollBarPos(settingsList.getVScrollBar().getHighVal());
		}
	}
	
	public void scrollToTop() {
		if (settingsList != null) {
			settingsList.getVScrollBar().setScrollBarPos(0);
		}
	}
	
}
