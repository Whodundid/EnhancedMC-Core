package com.Whodundid.core.coreApp.window;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.coreApp.CoreApp;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.notifications.window.NotificationWindow;
import com.Whodundid.core.settings.util.ReloaderDialogueBox;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton3Stage;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.colorPicker.ColorPickerSimple;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.scrollList.WindowScrollList;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowLabel;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowRect;
import com.Whodundid.core.windowLibrary.windowObjects.utilityObjects.ColorButton;
import com.Whodundid.core.windowLibrary.windowObjects.windows.TutorialWindow;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import net.minecraft.util.MathHelper;

//Author: Hunter Bragg

public class CoreAppSettingsWindow extends WindowParent {
	
	CoreApp app = (CoreApp) RegisteredApps.getApp(AppType.CORE);
	int vPos = 0;
	int hPos = 0;
	public WindowScrollList list;
	private boolean dontReloadWindows = false;
	
	//menus
	public WindowButton tutorial, notifications, reloadConfigs;
	
	//hud
	public WindowButton closeHudEmpty, drawProxyBorder, drawCrossHairs;
	public WindowButton3Stage hudCloseMethod, drawChat;
	
	//taskbar
	public WindowButton enableTaskbar;
	
	//visual
	public WindowButton drawCustomCursors;
	public ColorButton hoverTextColor;
	
	//debug
	public WindowButton showIncompats, showTerminal;
	
	//----------------------------------
	//CoreAppSettingsWindow Constructors
	//----------------------------------
	
	public CoreAppSettingsWindow() {
		super();
		aliases.add("emcsettings");
		windowIcon = EMCResources.emcSettingsIcon;
	}
	
	//----------------------
	//WindowObject Overrides
	//----------------------
	
	@Override
	public void initWindow() {
		setObjectName("EMC Core App Settings");
		defaultDims();
		setMinDims(170, 75);
		setResizeable(true);
		setMaximizable(true);
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		list = new WindowScrollList(this, startX + 2, startY + 2, width - 4, height - 4);
		list.setBackgroundColor(0xff292929);
		
		int mY = addMenus(0);
		int hY = addHud(mY);
		int tY = addTaskbar(hY);
		int vY = addVisual(tY);
		int dY = addDebug(vY);
		
		list.fitItemsInList(6, 7);
		
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
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		boolean rel = dontReloadWindows ? false : true;
		
		if (object instanceof ColorPickerSimple) {
			ColorPickerSimple cp = (ColorPickerSimple) object;
			
			if (cp.getStoredObject() instanceof String && args.length == 1 && args[0] instanceof Integer) {
				if ("hover".equals(cp.getStoredObject())) { CoreApp.hoverTextColor.set((int) args[0]); hoverTextColor.setColor(CoreApp.hoverTextColor.get()); }
			}
		}
		
		//menus
		if (object == tutorial) { EnhancedMC.displayWindow(new TutorialWindow()); }
		if (object == notifications) { EnhancedMC.displayWindow(new NotificationWindow(), this, CenterType.object); }
		if (object == reloadConfigs) { EnhancedMC.displayWindow(new ReloaderDialogueBox(RegisteredApps.getRegisteredAppsList()), CenterType.screen); }
		//hud
		if (object == closeHudEmpty) { closeHudEmpty.toggleTrueFalse(app.closeHudWhenEmpty, app, true, false); }
		if (object == hudCloseMethod) { hudCloseMethod.updateDislay(app.hudCloseMethod, app, true); }
		if (object == drawChat) { drawChat.updateDislay(app.drawChatOnHud, app, true); }
		if (object == drawProxyBorder) { drawProxyBorder.toggleTrueFalse(app.drawHudBorder, app, true, false); }
		if (object == drawCrossHairs) { drawCrossHairs.toggleTrueFalse(app.drawCrossHairsHud, app, true, false); }
		//taskbar
		if (object == enableTaskbar) { enableTaskbar.toggleTrueFalse(app.enableTaskBar, app, true, false); }
		//visual
		if (object == drawCustomCursors) { drawCustomCursors.toggleTrueFalse(app.customCursors, app, true, false); }
		if (object == hoverTextColor) {
			ColorPickerSimple cp = new ColorPickerSimple(this, CoreApp.hoverTextColor.get());
			EnhancedMC.displayWindow(cp);
			cp.getHeader().setTitle("Set Hover Text Color");
			EnhancedMC.getRenderer().setFocusLockObject(cp);
			cp.setStoredObject("hover");
		}
		//debug
		if (object == showTerminal) { showTerminal.toggleTrueFalse(app.enableTerminal, app, true, rel); }
		if (object == showIncompats) { showIncompats.toggleTrueFalse(app.showIncompats, app, true, rel); }
	}
	
	@Override
	public CoreAppSettingsWindow resize(int xIn, int yIn, ScreenLocation areaIn) {
		if (getMaximizedPosition() != ScreenLocation.center && (xIn != 0 || yIn != 0)) {
			int vPos = list.getVScrollBar().getScrollPos();
			int hPos = list.getHScrollBar().getScrollPos();
			
			super.resize(xIn, yIn, areaIn);
			
			list.getVScrollBar().onResizeUpdate(vPos, xIn, yIn, areaIn);
			list.getHScrollBar().onResizeUpdate(hPos, xIn, yIn, areaIn);
			
			setPreMax(getDimensions());
		}
		return this;
	}
	
	//-----------------------------
	//CoreAppSettingsWindow Methods
	//-----------------------------
	
	public void scrollToBottom() {
		if (list != null) {
			list.getVScrollBar().setScrollBarPos(list.getVScrollBar().getHighVal());
		}
	}
	
	public void scrollToTop() {
		if (list != null) {
			list.getVScrollBar().setScrollBarPos(0);
		}
	}
	
	public CoreAppSettingsWindow setDontReloadWindows(boolean val) { dontReloadWindows = val; return this; }
	
	//--------------------------------------
	//CoreAppSettingsWindow Internal Methods
	//--------------------------------------
	
	private int addMenus(int yPos) {
		EDimension ld = list.getListDimensions();
		
		WindowLabel menuLabel = new WindowLabel(list, ld.midX, yPos + 4, "General Menus", EColors.orange).setDrawCentered(true);
		WindowRect labelBack = new WindowRect(list, 0, yPos, list.getListDimensions().endX, yPos + 15, EColors.dsteel);
		WindowRect divider1 = new WindowRect(list, 0, yPos + 15, list.getDimensions().endX, yPos + 16, EColors.black);
		
		int w = MathHelper.clamp_int(ld.width - (ld.width / 3), 100, 200);
		
		tutorial = new WindowButton(list, ld.midX - (w / 2), menuLabel.endY + 12, w, 20, "EMC Tutorial");
		notifications = new WindowButton(list, ld.midX - (w / 2), tutorial.endY + 3, w, 20, "Notifications");
		reloadConfigs = new WindowButton(list, ld.midX - (w / 2), notifications.endY + 3, w, 20, "Reload All Configs");
		
		tutorial.setStringColor(EColors.lime);
		notifications.setStringColor(EColors.seafoam);
		reloadConfigs.setStringColor(EColors.yellow);
		
		IActionObject.setActionReceiver(this, tutorial, notifications, reloadConfigs);
		
		WindowRect back = new WindowRect(list, 0, yPos + 16, list.getDimensions().endX, reloadConfigs.endY + 9, 0xff202020);
		
		setClickable(false, labelBack, divider1, back);
		
		list.addAndIgnore(labelBack, divider1, menuLabel, back);
		list.addObjectToList(tutorial, notifications, reloadConfigs);
		
		return (reloadConfigs.endY + 9) - list.getDimensions().startY;
	}
	
	private int addHud(int yPos) {
		EDimension ld = list.getListDimensions();
		
		WindowLabel hudLabel = new WindowLabel(list, ld.midX, yPos + 4, "Hud", EColors.orange).setDrawCentered(true);
		WindowRect labelBack = new WindowRect(list, 0, yPos, list.getListDimensions().endX, yPos + 16, EColors.black);
		WindowRect divider1 = new WindowRect(list, 0, yPos + 1, list.getDimensions().endX, yPos + 15, EColors.dsteel);
		
		closeHudEmpty = new WindowButton(list, ld.midX - 100, hudLabel.endY + 10, 60, 20, CoreApp.closeHudWhenEmpty);
		WindowLabel closeEmptyLabel = new WindowLabel(list, closeHudEmpty.endX + 10, closeHudEmpty.startY + 6, "Close Hud when Empty", EColors.lgray);
		
		hudCloseMethod = new WindowButton3Stage(list, ld.midX - 100, closeHudEmpty.endY + 6, 60, 20, CoreApp.hudCloseMethod, EColors.yellow.intVal);
		WindowLabel hudCloseMethodLabel = new WindowLabel(list, hudCloseMethod.endX + 10, hudCloseMethod.startY + 6, "Hud Close Method", EColors.lgray);
		
		drawChat = new WindowButton3Stage(list, ld.midX - 100, hudCloseMethod.endY + 6, 60, 20, CoreApp.drawChatOnHud, EColors.yellow.intVal);
		WindowLabel drawChatLabel = new WindowLabel(list, drawChat.endX + 10, drawChat.startY + 6, "Draw Chat on Hud", EColors.lgray);
		
		drawProxyBorder = new WindowButton(list, ld.midX - 100, drawChat.endY + 6, 60, 20, CoreApp.drawHudBorder);
		WindowLabel drawProxyBorderLabel = new WindowLabel(list, drawProxyBorder.endX + 10, drawProxyBorder.startY + 6, "Draw Hud Border", EColors.lgray);
		
		drawCrossHairs = new WindowButton(list, ld.midX - 100, drawProxyBorder.endY + 6, 60, 20, CoreApp.drawCrossHairsHud);
		WindowLabel drawCrossHairsLabel = new WindowLabel(list, drawCrossHairs.endX + 10, drawCrossHairs.startY + 6, "Draw Crosshairs on Hud", EColors.lgray);
		
		//set values
		
		IActionObject.setActionReceiver(this, closeHudEmpty, hudCloseMethod, drawChat, drawProxyBorder, drawCrossHairs);
		
		setHoverText("Closes the hud if there are no windows or objects on screen.", closeHudEmpty, closeEmptyLabel);
		setHoverText("Determines how all window objects are handled when the hud is closed.", hudCloseMethod, hudCloseMethodLabel);
		setHoverText("Determines how chat messages are drawn when the hud is open.", drawChat, drawChatLabel);
		setHoverText("Draws a red border on the screen whenever the EMC hud is open.", drawProxyBorder, drawProxyBorderLabel);
		setHoverText("Determines if Minecraft crosshairs will draw when the hud is open.", drawCrossHairs, drawCrossHairsLabel);
		
		//add objects
		
		list.addAndIgnore(labelBack, divider1, hudLabel);
		list.addObjectToList(closeHudEmpty, closeEmptyLabel);
		list.addObjectToList(hudCloseMethod, hudCloseMethodLabel);
		list.addObjectToList(drawChat, drawChatLabel);
		list.addObjectToList(drawProxyBorder, drawProxyBorderLabel);
		list.addObjectToList(drawCrossHairs, drawCrossHairsLabel);
		
		return (drawCrossHairs.endY + 7) - list.getDimensions().startY;
	}
	
	private int addTaskbar(int yPos) {
		EDimension ld = list.getListDimensions();
		
		WindowLabel taskbarLabel = new WindowLabel(list, ld.midX, yPos + 4, "Taskbar", EColors.orange).setDrawCentered(true);
		WindowRect labelBack = new WindowRect(list, 0, yPos, list.getListDimensions().endX, yPos + 16, EColors.black);
		WindowRect divider1 = new WindowRect(list, 0, yPos + 1, list.getDimensions().endX, yPos + 15, EColors.dsteel);
		
		enableTaskbar = new WindowButton(list, ld.midX - 100, taskbarLabel.endY + 10, 60, 20, CoreApp.enableTaskBar);
		WindowLabel enableTaskbarLabel = new WindowLabel(this, enableTaskbar.endX + 10, enableTaskbar.startY + 6, "Enable Taskbar", EColors.lgray);
		
		WindowRect back = new WindowRect(list, 0, yPos + 16, list.getDimensions().endX, enableTaskbar.endY + 7, 0xff202020);
		back.setClickable(false);
		
		//set values
		
		enableTaskbar.setActionReceiver(this);
		setHoverText("Enables a taskbar which displays currently running windows.", enableTaskbar, enableTaskbarLabel);
		
		//add objects
		
		list.addAndIgnore(labelBack, divider1, back, taskbarLabel);
		list.addObjectToList(enableTaskbar, enableTaskbarLabel);
		
		return (enableTaskbar.endY + 7) - list.getDimensions().startY;
	}
	
	private int addVisual(int yPos) {
		EDimension ld = list.getListDimensions();
		
		WindowLabel visualLabel = new WindowLabel(list, ld.midX, yPos + 4, "Visual", EColors.orange).setDrawCentered(true);
		WindowRect labelBack = new WindowRect(list, 0, yPos, list.getListDimensions().endX, yPos + 16, EColors.black);
		WindowRect divider1 = new WindowRect(list, 0, yPos + 1, list.getDimensions().endX, yPos + 15, EColors.dsteel);
		
		drawCustomCursors = new WindowButton(list, ld.midX - 100, visualLabel.endY + 10, 60, 20, CoreApp.customCursors);
		WindowLabel drawCustomCursorsLabel = new WindowLabel(this, drawCustomCursors.endX + 10, drawCustomCursors.startY + 6, "Draw Custom Cursors", EColors.lgray);
		
		hoverTextColor = new ColorButton(list, ld.midX - 100, drawCustomCursors.endY + 6, 20, 20, CoreApp.hoverTextColor.get());
		WindowLabel hoverTextLabel = new WindowLabel(list, hoverTextColor.endX + 10, hoverTextColor.startY + 6, "Text Hover Color", EColors.lgray);
		
		IActionObject.setActionReceiver(this, drawCustomCursors, hoverTextColor);
		
		setHoverText("Allows the cursor to change its appearance based off of what it is hovering over.", drawCustomCursors, drawCustomCursorsLabel);
		setHoverText("The color that [this] hovering text will be drawn with.", hoverTextColor, hoverTextLabel);
		
		list.addAndIgnore(labelBack, divider1, visualLabel);
		list.addObjectToList(drawCustomCursors, drawCustomCursorsLabel);
		list.addObjectToList(hoverTextColor, hoverTextLabel);
		
		return (hoverTextColor.endY + 7) - list.getDimensions().startY;
	}
	
	private int addDebug(int yPos) {
		EDimension ld = list.getListDimensions();
		
		WindowLabel debugLabel = new WindowLabel(list, ld.midX, yPos + 4, "Debug", EColors.orange).setDrawCentered(true);
		WindowRect labelBack = new WindowRect(list, 0, yPos, list.getListDimensions().endX, yPos + 16, EColors.black);
		WindowRect divider1 = new WindowRect(list, 0, yPos + 1, list.getDimensions().endX, yPos + 15, EColors.dsteel);
		
		showTerminal = new WindowButton(list, ld.midX - 100, debugLabel.endY + 10, 60, 20, CoreApp.enableTerminal);
		WindowLabel terminalLabel = new WindowLabel(list, showTerminal.endX + 10, showTerminal.startY + 6, "Enable EMC Terminal");
		
		showIncompats = new WindowButton(list, ld.midX - 100, showTerminal.endY + 6, 60, 20, CoreApp.showIncompats);
		WindowLabel incompatLabel = new WindowLabel(list, showIncompats.endX + 10, showIncompats.startY + 6, "Display Incompatible Apps");
		
		WindowRect back = new WindowRect(list, 0, yPos + 16, list.getDimensions().endX, ld.endY + 10, 0xff202020);
		back.setClickable(false);
		
		//set values
		
		IActionObject.setActionReceiver(this, showTerminal, showIncompats);
		
		setHoverText("Terminal used for debug and advanced purposes.", showTerminal, terminalLabel);
		setHoverText("Displays incompatible EMC Apps within the main settings window.", showIncompats, incompatLabel);
		
		WindowLabel.setColor(0xb2b2b2, terminalLabel, incompatLabel);
		
		//add objects
		
		list.addAndIgnore(labelBack, divider1, back, debugLabel);
		list.addObjectToList(showTerminal, terminalLabel);
		list.addObjectToList(showIncompats, incompatLabel);
		
		return (showIncompats.endY + 7) - list.getDimensions().startY;
	}
	
}
