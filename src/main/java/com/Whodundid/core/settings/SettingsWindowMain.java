package com.Whodundid.core.settings;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.app.window.AppProblemsWindowList;
import com.Whodundid.core.coreApp.CoreApp;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.debug.ImportantWindow;
import com.Whodundid.core.settings.util.SettingsMenuContainer;
import com.Whodundid.core.settings.util.SettingsRCM;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowTextField;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.header.WindowHeader;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.scrollList.WindowScrollList;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowLabel;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowRect;
import com.Whodundid.core.windowLibrary.windowObjects.windows.TextureDisplayer;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import java.io.File;

//Author: Hunter Bragg

public class SettingsWindowMain extends WindowParent {
	
	WindowButton keyBindButton, screenshotsButton, problemButton;
	WindowButton terminalButton;
	WindowScrollList scrollList;
	WindowTextField searchField;
	SettingsRCM rcm;
	
	int vPos;
	
	public SettingsWindowMain() {
		super();
		aliases.add("settings", "appsettings", "applist");
	}
	
	@Override
	public void initWindow() {
		setObjectName("EMC Settings");
		setDimensions(defaultWidth, defaultHeight);
		windowIcon = EMCResources.settingsIcon;
	}
	
	@Override
	public void initObjects() {
		setHeader(new WindowHeader(this));
		
		SettingsWindowMain mm = this;
		
		boolean term = CoreApp.enableTerminal.get();
		int w = term ? 92 : 103;
		int sX = term ? endX - 97 : endX - 108;
		
		screenshotsButton = new WindowButton(this, startX + 5, endY - 25, w, 20, "Screenshots");
		keyBindButton = new WindowButton(this, sX, endY - 25, w, 20, "MC KeyBinds") {
			@Override
			public void mousePressed(int mXIn, int mYIn, int button) {
				if (button == 1) {
					windowInstance.addObject(new SettingsRCM(mm, new KeyBindWindow(), "Keybinds"));
				}
				super.mousePressed(mXIn, mYIn, button);
			}
		};
		
		terminalButton = new WindowButton(this, screenshotsButton.endX + 3, endY - 25, keyBindButton.startX - screenshotsButton.endX - 6, 20) {
			@Override
			public void mousePressed(int mXIn, int mYIn, int button) {
				if (button == 1) {
					windowInstance.addObject(new SettingsRCM(mm, new ETerminal(), "EMC Terminal"));
				}
				super.mousePressed(mXIn, mYIn, button);
			}
		};
		terminalButton.setTextures(EMCResources.terminalButton, EMCResources.terminalButtonSel);
		
		problemButton = new WindowButton(this, endX - 17, startY + 2, 15, 15).setTextures(EMCResources.guiProblemOpen, EMCResources.guiProblemOpenSel);
		problemButton.setVisible(RegisteredApps.getIncompatibleAppsList().isNotEmpty() || RegisteredApps.getBrokenAppsList().isNotEmpty());
		problemButton.setDrawBackground(true).setBackgroundColor(0xffbb0000);
		
		IActionObject.setActionReceiver(this, screenshotsButton, keyBindButton, problemButton);
		
		scrollList = new WindowScrollList(this, startX + 5, startY + 23, endX - startX - 10, endY - startY - 75).setBackgroundColor(0xff252525);
		
		searchField = new WindowTextField(this, scrollList.startX + 1, scrollList.endY + 6, scrollList.width - 4, 15) {
			@Override
			public void keyPressed(char typedChar, int keyCode) {
				super.keyPressed(typedChar, keyCode);
				assembleList();
			}
		};
		searchField.setTextWhenEmpty("Search apps...").setEnableBackgroundDrawing(true);
		
		//add all objects first
		addObject(scrollList, problemButton, searchField, keyBindButton, screenshotsButton);
		
		CoreApp em = (CoreApp) RegisteredApps.getApp(AppType.CORE);
		if (em.enableTerminal.get()) { addObject(terminalButton); }
		
		//then build the list
		if (RegisteredApps.getRegisteredAppsList().isEmpty()) { //THIS SHOULD BE IMPOSSIBLE!
			scrollList.addObjectToList(new WindowLabel(scrollList, (width - 10) / 2, 80, "No Enhanced MC Apps Detected!", 0xff5555).enableWordWrap(true, 125).setDrawCentered(true));
		}
		else { assembleList(); }
	}
	
	@Override
	public void preReInit() {
		vPos = scrollList.getVScrollBar().getScrollPos();
	}
	
	@Override
	public void postReInit() {
		scrollList.getVScrollBar().setScrollBarPos(vPos);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		updateBeforeNextDraw(mXIn, mYIn);
		drawDefaultBackground();
		
		if (problemButton.checkDraw()) {
			problemButton.setDrawBackground(EnhancedMC.updateCounter / 30 % 2 == 0);
		}
		
		drawRect(startX + 1, startY + 19, endX - 1, endY - 1, -0x00cfcfcf); //grey background
		drawRect(startX, startY + 18, endX, startY + 19, 0xff000000); //top line
		
		drawCenteredStringWithShadow("Enhanced MC Apps", midX, startY + 6, 0xffbb00);
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public SettingsWindowMain resize(int xIn, int yIn, ScreenLocation areaIn) {
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
			EnhancedMC.displayWindow(new ImportantWindow(), CenterType.screen);
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
		WindowRect separator = new WindowRect(this, ld.getMidX() + 16, ld.startY, ld.getMidX() + 17, ld.endY, 0xff000000);
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
				WindowRect workingSeparator = new WindowRect(this, l.getMidX() + 17, lineStart, l.getMidX() + 18, lineEnd, 0xff000000);
				scrollList.addObjectToList(workingSeparator);
				
				int space = 2;
				//add box
				scrollList.growListHeight(cHeight);
				scrollList.addObjectToList(new WindowRect(scrollList, l.startX, l.startY + (count * cHeight) + 1 + space, l.endX, l.startY + (count * cHeight) + 2 + space, 0xff000000));
				scrollList.addObjectToList(new WindowRect(scrollList, l.startX, l.startY + (count * cHeight) + 2 + space, l.endX, l.startY + (count * cHeight) + 18 + space, 0xff3b3b3b));
				scrollList.addObjectToList(new WindowLabel(scrollList, l.midX, l.startY + (count * cHeight) + 6 + space, "Incompatible Apps").setDrawCentered(true).setColor(0xff4444));
				scrollList.addObjectToList(new WindowRect(scrollList, l.startX, l.startY + (count * cHeight) + 18 + space, l.endX, l.startY + (count * cHeight) + 19 + space, 0xff000000));
				
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
				WindowRect incompatSeparator = new WindowRect(this, l.getMidX() + 17, lineStartIncompat, l.getMidX() + 18, lineEndIncompat, 0xff000000);
				scrollList.addObjectToList(incompatSeparator);
			}
			else {
				scrollList.growListHeight(2); //add spacing to bottom so it matches the top
				addObject(separator); //add the general separator instead
			}
		}
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object.runsActionOnPress()) { object.performAction(); }
		else {
			if (object == keyBindButton) { EnhancedMC.displayWindow(new KeyBindWindow(), this); }
			if (object == screenshotsButton) {
				File screenshots = new File(System.getProperty("user.dir") + "\\screenshots");
				if (screenshots.exists()) {
					File[] images = screenshots.listFiles();
					if (images != null) {
						File last = images[images.length - 1];
						EnhancedMC.displayWindow(new TextureDisplayer(last));
					}
					else { System.out.println("images is null!"); }
				}
				//EnhancedMC.displayWindow(new ReloaderDialogueBox(RegisteredApps.getRegisteredAppList()), CenterType.screen);
			}
			if (object == terminalButton) { EnhancedMC.displayWindow(new ETerminal(), this, true, false, false, CenterType.screen); }
			if (object == problemButton) { EnhancedMC.displayWindow(new AppProblemsWindowList()); }
		}
	}
	
	public void openRCM(int mXIn, int mYIn) { addObject(rcm = new SettingsRCM(this)); }
	public void openRCM(int mXIn, int mYIn, EMCApp modIn) { addObject(rcm = new SettingsRCM(this, modIn)); }
	
}
