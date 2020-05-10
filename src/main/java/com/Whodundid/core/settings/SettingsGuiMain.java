package com.Whodundid.core.settings;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.app.gui.IncompatibleWindowList;
import com.Whodundid.core.coreApp.CoreApp;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.debug.ImportantGui;
import com.Whodundid.core.enhancedGui.StaticEGuiObject;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiTextField;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.scrollList.EGuiScrollList;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiRect;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.settings.guiParts.ReloaderDialogueBox;
import com.Whodundid.core.settings.guiParts.SettingsMenuContainer;
import com.Whodundid.core.settings.guiParts.SettingsRCM;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;

//Author: Hunter Bragg

public class SettingsGuiMain extends WindowParent {
	
	EGuiButton keyBindGui, reloadConfigs, problem;
	EGuiButton terminalBtn;
	EGuiButton hiddenButton1, hiddenButton2;
	EGuiScrollList scrollList;
	EGuiTextField searchField;
	SettingsRCM rcm;
	int leftPress = 0, rightPress = 0;
	
	public SettingsGuiMain() {
		super();
		aliases.add("settings", "appsettings", "applist");
	}
	
	@Override
	public void initGui() {
		setObjectName("EMC Settings");
		setDimensions(defaultWidth, defaultHeight);
		windowIcon = EMCResources.settingsIcon;
	}
	
	@Override
	public void initObjects() {
		setHeader(new EGuiHeader(this));
		
		SettingsGuiMain mm = this;
		
		reloadConfigs = new EGuiButton(this, startX + 5, endY - 25, 92, 20, "Reload Configs");
		keyBindGui = new EGuiButton(this, endX - 97, endY - 25, 92, 20, "MC KeyBinds") {
			@Override
			public void mousePressed(int mXIn, int mYIn, int button) {
				if (button == 1) {
					guiInstance.addObject(new SettingsRCM(mm, new KeyBindGui(), "Keybinds"));
				}
				super.mousePressed(mXIn, mYIn, button);
			}
		};
		
		terminalBtn = new EGuiButton(this, reloadConfigs.endX + 3, endY - 25, keyBindGui.startX - reloadConfigs.endX - 6, 20) {
			@Override
			public void mousePressed(int mXIn, int mYIn, int button) {
				if (button == 1) {
					guiInstance.addObject(new SettingsRCM(mm, new ETerminal(), "EMC Terminal"));
				}
				super.mousePressed(mXIn, mYIn, button);
			}
		};
		terminalBtn.setTextures(EMCResources.terminalButton, EMCResources.terminalButtonSel);
		
		problem = new EGuiButton(this, endX - 17, startY + 2, 15, 15).setTextures(EMCResources.guiProblemOpen, EMCResources.guiProblemOpenSel);
		
		problem.setVisible(RegisteredApps.getIncompatibleAppList().isNotEmpty());
		problem.setDrawBackground(true).setBackgroundColor(0xffbb0000);
		
		reloadConfigs.setActionReciever(this);
		keyBindGui.setActionReciever(this);
		problem.setActionReciever(this);
		
		StaticEGuiObject.setPersistent(true, keyBindGui, reloadConfigs);
		
		hiddenButton1 = new EGuiButton(this, startX + 1, startY + 1, 10, 10) {
			@Override public void onPress() {
				if (getPressedButton() == 0) {
					if (!EnhancedMC.isDebugMode()) {
						leftPress++;
						if (leftPress == 3 && rightPress == 1) {
							EnhancedMC.setDebugMode(true);
							playPressSound();
						}
					}
				}
			}
			@Override public void drawObject(int mX, int mY) {
				super.drawObject(mX, mY);
			}
		};
		
		hiddenButton2 = new EGuiButton(this, endX - (RegisteredApps.getIncompatibleAppList().isEmpty() ? 11 : 28), startY + 1, 10, 10) {
			@Override public void onPress() {
				if (getPressedButton() == 0) {
					if (!EnhancedMC.isDebugMode()) {
						if (leftPress == 2) { rightPress++; }
					}
				}
			}
			@Override public void drawObject(int mX, int mY) {
				super.drawObject(mX, mY);
			}
		};
		
		hiddenButton1.setDrawTextures(false).setRunActionOnPress(true);
		hiddenButton2.setDrawTextures(false).setRunActionOnPress(true);
		
		scrollList = new EGuiScrollList(this, startX + 5, startY + 23, endX - startX - 10, endY - startY - 75).setBackgroundColor(0xff252525);
		
		searchField = new EGuiTextField(this, scrollList.startX + 1, scrollList.endY + 6, scrollList.width - 4, 15) {
			@Override
			public void keyPressed(char typedChar, int keyCode) {
				super.keyPressed(typedChar, keyCode);
				assembleList();
			}
		};
		searchField.setTextWhenEmpty("Search apps...").setEnableBackgroundDrawing(true);
		
		//add all objects first
		addObject(scrollList, problem, searchField, keyBindGui, reloadConfigs);
		addObject(hiddenButton1, hiddenButton2);
		
		CoreApp em = (CoreApp) RegisteredApps.getApp(AppType.CORE);
		if (em.enableTerminal.get()) { addObject(terminalBtn); }
		
		//then build the list
		if (RegisteredApps.getRegisteredAppList().isEmpty()) { //THIS SHOULD BE IMPOSSIBLE!
			scrollList.addObjectToList(new EGuiLabel(scrollList, (width - 10) / 2, 80, "No Enhanced MC Apps Detected!", 0xff5555).enableWordWrap(true, 125).setDrawCentered(true));
		}
		else { assembleList(); }
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		updateBeforeNextDraw(mXIn, mYIn);
		drawDefaultBackground();
		
		if (RegisteredApps.getIncompatibleAppList().isNotEmpty()) {
			problem.setDrawBackground(EnhancedMC.updateCounter / 30 % 2 == 0);
		}
		
		drawRect(startX + 1, startY + 19, endX - 1, endY - 1, -0x00cfcfcf); //grey background
		drawRect(startX, startY + 18, endX, startY + 19, 0xff000000); //top line
		
		drawCenteredStringWithShadow("Enhanced MC Apps", midX, startY + 6, 0xffbb00);
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public SettingsGuiMain resize(int xIn, int yIn, ScreenLocation areaIn) {
		int curPos = scrollList.getVScrollBar().getScrollPos();
		scrollList.getVScrollBar().reset();
		super.resize(xIn, yIn, areaIn);
		scrollList.getVScrollBar().setScrollBarPos(curPos);
		return this;
	}
	
	private void assembleList() {
		//reset the list first
		scrollList.clearList();
		
		scrollList.setListHeight(0); //reset height to 0
		
		//not important
		if (searchField.getText().toLowerCase().equals("whosyourdaddy")) {
			searchField.clear();
			EnhancedMC.displayWindow(new ImportantGui(), CenterType.screen);
			//assembleList();
			return;
		}
		
		//gather and filter all present EMC submods
		EArrayList<EMCApp> filteredMods = new EArrayList();
		EArrayList<EMCApp> incompats = new EArrayList();
		if (searchField != null && !searchField.getText().equals("Search apps...")) {
			for (EMCApp m : RegisteredApps.getAppsList()) {
				if (m != null) {
					if (m.getName().toLowerCase().contains(searchField.getText().toLowerCase())) {
						if (m.isIncompatible()) { incompats.add(m); }
						else { filteredMods.add(m); }
					}
				}
			}
		}
		else {
			for (EMCApp m : RegisteredApps.getAppsList()) {
				if (m.isIncompatible()) { incompats.add(m); }
				else { filteredMods.add(m); }
			}
		}
		
		//prepare to add filtered mods to list
		EDimension l = scrollList.getListDimensions();
		EDimension ld = scrollList.getDimensions();
		EGuiRect separator = new EGuiRect(this, ld.getMidX() + 16, ld.startY, ld.getMidX() + 17, ld.endY, 0xff000000);
		int size = filteredMods.size() + (CoreApp.showIncompats.get() ? incompats.size() : 0);
		
		//actually add mods to list
		if (size > 0) {
			int count = 0;
			int lineStart = 0;
			int cHeight = 0;
			
			for (int i = 0; i < filteredMods.size(); i++) {
				EMCApp m = filteredMods.get(i);
				
				SettingsMenuContainer container = new SettingsMenuContainer(scrollList, m, count, size > 8, this);
				
				cHeight = container.getHeight() + 2;
				scrollList.addObjectToList(container);
				scrollList.growListHeight(cHeight);
				count++;
			}
			
			int lineEnd = 3 + count * cHeight;
			
			if (CoreApp.showIncompats.get() && incompats.isNotEmpty()) {
				//add length specific lines if there are multiple categories
				EGuiRect workingSeparator = new EGuiRect(this, l.getMidX() + 17, lineStart, l.getMidX() + 18, lineEnd, 0xff000000);
				scrollList.addObjectToList(workingSeparator);
				
				int space = 2;
				//add box
				scrollList.growListHeight(cHeight);
				scrollList.addObjectToList(new EGuiRect(scrollList, l.startX, l.startY + (count * cHeight) + 1 + space, l.endX, l.startY + (count * cHeight) + 2 + space, 0xff000000));
				scrollList.addObjectToList(new EGuiRect(scrollList, l.startX, l.startY + (count * cHeight) + 2 + space, l.endX, l.startY + (count * cHeight) + 18 + space, 0xff3b3b3b));
				scrollList.addObjectToList(new EGuiLabel(scrollList, l.midX, l.startY + (count * cHeight) + 6 + space, "Incompatible Apps").setDrawCentered(true).setDisplayStringColor(0xff4444));
				scrollList.addObjectToList(new EGuiRect(scrollList, l.startX, l.startY + (count * cHeight) + 18 + space, l.endX, l.startY + (count * cHeight) + 19 + space, 0xff000000));
				
				int lineStartIncompat = -1 + ((count + 1) * cHeight);
				
				count += 1;
				
				for (int i = 0; i < incompats.size(); i++) {
					EMCApp m = incompats.get(i);
					
					SettingsMenuContainer container = new SettingsMenuContainer(scrollList, m, count, space - 4, size > 8,  this);
					
					cHeight = container.getHeight() + 2;
					scrollList.addObjectToList(container);
					scrollList.growListHeight(cHeight);
					count++;
				}
				
				int lineEndIncompat = ((count + 1) * cHeight) + 13;
				EGuiRect incompatSeparator = new EGuiRect(this, l.getMidX() + 17, lineStartIncompat, l.getMidX() + 18, lineEndIncompat, 0xff000000);
				scrollList.addObjectToList(incompatSeparator);
			}
			else {
				scrollList.growListHeight(2); //add spacing to bottom so it matches the top
				addObject(separator); //add the general separator instead
			}
		}
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object.runActionOnPress()) { object.performAction(); }
		else {
			if (object == keyBindGui) { EnhancedMC.displayWindow(new KeyBindGui(), this); }
			if (object == reloadConfigs) {
				EnhancedMC.displayWindow(new ReloaderDialogueBox(RegisteredApps.getRegisteredAppList()), CenterType.screen);
				//assembleList();
			}
			if (object == terminalBtn) { EnhancedMC.displayWindow(new ETerminal(), this, true, false, false, CenterType.objectIndent); }
			if (object == problem) { EnhancedMC.displayWindow(new IncompatibleWindowList()); }
		}
	}
	
	public void openRCM(int mXIn, int mYIn) { addObject(rcm = new SettingsRCM(this)); }
	public void openRCM(int mXIn, int mYIn, EMCApp modIn) { addObject(rcm = new SettingsRCM(this, modIn)); }
	
	@Override
	public void sendArgs(Object... args) {
		if (args.length == 2) {
			if (args[0] instanceof String && args[1] instanceof EMCApp) {
				String msg = (String) args[0];
				if (msg.equals("Reload")) {
					updateList();
				}
			}
		}
	}
	
	public void updateList() {
		if (scrollList != null) {
			int curPos = scrollList.getVScrollBar().getScrollPos();
			scrollList.getVScrollBar().reset();
			assembleList();
			scrollList.getVScrollBar().setScrollBarPos(curPos);
		}
	}
}
