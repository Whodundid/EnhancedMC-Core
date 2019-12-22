package com.Whodundid.core.settings;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreSubMod.EnhancedMCMod;
import com.Whodundid.core.debug.ExperimentGui;
import com.Whodundid.core.debug.ImportantGui;
import com.Whodundid.core.enhancedGui.StaticEGuiObject;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiRect;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiScrollList;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiTextField;
import com.Whodundid.core.enhancedGui.guiUtil.EObjectGroup;
import com.Whodundid.core.enhancedGui.objectEvents.EventKeyboard;
import com.Whodundid.core.enhancedGui.objectEvents.ObjectEvent;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.KeyboardType;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.settings.guiParts.ReloaderDialogueBox;
import com.Whodundid.core.settings.guiParts.SettingsMenuContainer;
import com.Whodundid.core.settings.guiParts.SettingsRCM;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.subMod.SubModType;
import com.Whodundid.core.subMod.gui.IncompatibleWindowList;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.Resources;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;

//Dec 28, 2018

//Last edited: Jun 28, 2019
//Edit note: replaced page system with scrollable list to make space for more options
//First Added: Sep 14, 2018
//Author: Hunter Bragg

public class SettingsGuiMain extends WindowParent {
	
	EGuiButton keyBindGui, reloadConfigs, problem;
	EGuiButton experimentGui, disableDebugMode, consoleBtn;
	EGuiButton hiddenButton1, hiddenButton2;
	EGuiScrollList scrollList;
	EGuiTextField searchField;
	SettingsRCM rcm;
	int leftPress = 0, rightPress = 0;
	
	public SettingsGuiMain() {
		super();
		aliases.add("settings", "modsettings", "modlist");
	}
	
	@Override
	public void initGui() {
		setObjectName("EMC Settings");
		setDimensions(defaultWidth, defaultHeight);
		super.initGui();
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
		experimentGui = new EGuiButton(this, 1, res.getScaledHeight() - 21, 85, 20, "ExperimentGui");
		disableDebugMode = new EGuiButton(this, experimentGui.endX + 1, res.getScaledHeight() - 21, 85, 20, "Disable Debug");
		consoleBtn = new EGuiButton(this, reloadConfigs.endX + 3, endY - 25, keyBindGui.startX - reloadConfigs.endX - 6, 20) {
			@Override
			public void mousePressed(int mXIn, int mYIn, int button) {
				if (button == 1) {
					guiInstance.addObject(new SettingsRCM(mm, new ETerminal(), "EMC Terminal"));
				}
				super.mousePressed(mXIn, mYIn, button);
			}
		};
		problem = new EGuiButton(this, endX - 17, startY + 2, 15, 15).setTextures(Resources.guiProblemOpen, Resources.guiProblemOpenSel);
		
		problem.setVisible(RegisteredSubMods.getIncompatibleModsList().isNotEmpty());
		problem.setDrawBackground(true).setBackgroundColor(0xffbb0000);
		
		reloadConfigs.setActionReciever(this);
		keyBindGui.setActionReciever(this);
		experimentGui.setActionReciever(this);
		disableDebugMode.setActionReciever(this);
		problem.setActionReciever(this);
		
		experimentGui.setVisible(EnhancedMC.isDebugMode());
		experimentGui.setMoveable(true);
		disableDebugMode.setVisible(EnhancedMC.isDebugMode());
		disableDebugMode.setMoveable(true);
		StaticEGuiObject.setPersistent(true, keyBindGui, reloadConfigs);
		
		hiddenButton1 = new EGuiButton(this, startX + 1, startY + 1, 10, 10) {
			@Override public void performAction() {
				if (getPressedButton() == 0) {
					if (!EnhancedMC.isDebugMode()) {
						leftPress++;
						if (leftPress == 3 && rightPress == 1) {
							experimentGui.setVisible(true);
							disableDebugMode.setVisible(true);
							EnhancedMC.setDebugMode(true);
							playPressSound();
						}
					}
				}
			}
			@Override public void drawObject(int mX, int mY, float ticks) {
				super.drawObject(mX, mY, ticks);
			}
		};
		
		hiddenButton2 = new EGuiButton(this, endX - (RegisteredSubMods.getIncompatibleModsList().isEmpty() ? 11 : 28), startY + 1, 10, 10) {
			@Override public void performAction() {
				if (getPressedButton() == 0) {
					if (!EnhancedMC.isDebugMode()) {
						if (leftPress == 2) { rightPress++; }
					}
				}
			}
			@Override public void drawObject(int mX, int mY, float ticks) {
				super.drawObject(mX, mY, ticks);
			}
		};
		
		hiddenButton1.setTextures(Resources.emptyPixel, Resources.emptyPixel).setRunActionOnPress(true);
		hiddenButton2.setTextures(Resources.emptyPixel, Resources.emptyPixel).setRunActionOnPress(true);
		
		scrollList = new EGuiScrollList(this, startX + 5, startY + 23, endX - startX - 10, endY - startY - 75).setBackgroundColor(0xff252525);
		
		searchField = new EGuiTextField(this, scrollList.startX + 1, scrollList.endY + 6, scrollList.width - 4, 15) {
			@Override
			public void keyPressed(char typedChar, int keyCode) {
				super.keyPressed(typedChar, keyCode);
				assembleList();
			}
		};
		searchField.setTextWhenEmpty("Search mods...").setEnableBackgroundDrawing(true);
		
		//add all objects first
		addObject(scrollList, problem, searchField, keyBindGui, reloadConfigs);
		addObject(hiddenButton1, hiddenButton2);
		addObject(experimentGui, disableDebugMode);
		
		EnhancedMCMod em = (EnhancedMCMod) RegisteredSubMods.getMod(SubModType.CORE);
		if (em.enableTerminal.get()) { addObject(consoleBtn); }
		
		objectGroup = new EObjectGroup(this);
		objectGroup.addObjects(getAllChildren());
		for (IEnhancedGuiObject o : getAllChildren()) { o.setObjectGroup(objectGroup); }
		
		//then build the list
		if (RegisteredSubMods.getRegisteredModsList().isEmpty()) { //THIS SHOULD BE IMPOSSIBLE!
			scrollList.addObjectToList(new EGuiLabel(scrollList, (width - 10) / 2, 80, "No Enhanced MC Sub Mods Detected", 0xff5555).enableWordWrap(true, 125).setDrawCentered(true));
		}
		else { assembleList(); }
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		updateBeforeNextDraw(mXIn, mYIn);
		drawDefaultBackground();
		
		if (RegisteredSubMods.getIncompatibleModsList().isNotEmpty()) {
			problem.setDrawBackground(EnhancedMC.updateCounter / 30 % 2 == 0);
		}
		
		drawRect(startX + 1, startY + 19, endX - 1, endY - 1, -0x00cfcfcf); //grey background
		drawRect(startX, startY + 18, endX, startY + 19, 0xff000000); //top line
		
		drawCenteredStringWithShadow("Enhanced MC Sub Mods", midX, startY + 6, 0xffbb00);
		super.drawObject(mXIn, mYIn, ticks);
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
		if (searchField.getText().toLowerCase().equals("whodundid")) {
			searchField.clear();
			EnhancedMC.displayEGui(new ImportantGui(), CenterType.screen);
			assembleList();
			return;
		}
		
		//gather and filter all present EMC submods
		EArrayList<SubMod> filteredMods = new EArrayList();
		EArrayList<SubMod> incompats = new EArrayList();
		if (searchField != null && !searchField.getText().equals("Search mods...")) {
			for (SubMod m : RegisteredSubMods.getModsList()) {
				if (SubModType.getModName(m.getModType()).toLowerCase().contains(searchField.getText().toLowerCase())) {
					if (m.isIncompatible()) { incompats.add(m); }
					else { filteredMods.add(m); }
				}
			}
		}
		else {
			for (SubMod m : RegisteredSubMods.getModsList()) {
				if (m.isIncompatible()) { incompats.add(m); }
				else { filteredMods.add(m); }
			}
		}
		
		//prepare to add filtered mods to list
		EDimension l = scrollList.getListDimensions();
		EDimension ld = scrollList.getDimensions();
		EGuiRect separator = new EGuiRect(this, ld.getMidX() + 16, ld.startY, ld.getMidX() + 17, ld.endY, 0xff000000);
		int size = filteredMods.size() + (EnhancedMCMod.showIncompats.get() ? incompats.size() : 0);
		
		//actually add mods to list
		if (size > 0) {
			int count = 0;
			int lineStart = 0;
			int cHeight = 0;
			
			for (int i = 0; i < filteredMods.size(); i++) {
				SubMod m = filteredMods.get(i);
				
				SettingsMenuContainer container = new SettingsMenuContainer(scrollList, m, count, this);
				
				cHeight = container.getHeight() + 2;
				scrollList.addObjectToList(container);
				scrollList.growListHeight(cHeight);
				count++;
			}
			
			int lineEnd = 3 + count * cHeight;
			
			//add length specific lines if there are multiple categories
			if (incompats.isNotEmpty()) {
				EGuiRect workingSeparator = new EGuiRect(this, l.getMidX() + 18, lineStart, l.getMidX() + 19, lineEnd, 0xff000000);
				scrollList.addObjectToList(workingSeparator);
			} else {
				addObject(separator); //add the general separator instead
			}
			
			if (EnhancedMCMod.showIncompats.get() && incompats.isNotEmpty()) {
				int space = 2;
				//add box
				scrollList.growListHeight(cHeight);
				scrollList.addObjectToList(new EGuiRect(scrollList, l.startX, l.startY + (count * cHeight) + 1 + space, l.endX, l.startY + (count * cHeight) + 2 + space, 0xff000000));
				scrollList.addObjectToList(new EGuiRect(scrollList, l.startX, l.startY + (count * cHeight) + 2 + space, l.endX, l.startY + (count * cHeight) + 18 + space, 0xff3b3b3b));
				scrollList.addObjectToList(new EGuiLabel(scrollList, l.midX, l.startY + (count * cHeight) + 6 + space, "Incompatible Mods").setDrawCentered(true).setDisplayStringColor(0xff4444));
				scrollList.addObjectToList(new EGuiRect(scrollList, l.startX, l.startY + (count * cHeight) + 18 + space, l.endX, l.startY + (count * cHeight) + 19 + space, 0xff000000));
				
				int lineStartIncompat = -1 + ((count + 1) * cHeight);
				
				count += 1;
				
				for (int i = 0; i < incompats.size(); i++) {
					SubMod m = incompats.get(i);
					
					SettingsMenuContainer container = new SettingsMenuContainer(scrollList, m, count, space - 4, this);
					
					cHeight = container.getHeight() + 2;
					scrollList.addObjectToList(container);
					scrollList.growListHeight(cHeight);
					count++;
				}
				
				int lineEndIncompat = ((count + 1) * cHeight) + 13;
				EGuiRect incompatSeparator = new EGuiRect(this, l.getMidX() + 18, lineStartIncompat, l.getMidX() + 19, lineEndIncompat, 0xff000000);
				scrollList.addObjectToList(incompatSeparator);
			}
			else {
				scrollList.growListHeight(2); //add spacing to bottom so it matches the top
			}
		}
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object.runActionOnPress()) { object.performAction(); }
		else {
			if (object == keyBindGui) { EnhancedMC.displayEGui(new KeyBindGui(), this); }
			if (object == reloadConfigs) {
				EnhancedMC.displayEGui(new ReloaderDialogueBox(RegisteredSubMods.getRegisteredModsList()), CenterType.screen);
				assembleList();
			}
			if (object == experimentGui) { EnhancedMC.displayEGui(new ExperimentGui(), this); }
			if (object == disableDebugMode) {
				EnhancedMC.setDebugMode(false);
				experimentGui.setVisible(false);
				disableDebugMode.setVisible(false);
				leftPress = 0;
				rightPress = 0;
			}
			if (object == consoleBtn) { EnhancedMC.displayEGui(new ETerminal(), this, true, false, false, CenterType.objectIndent); }
			if (object == problem) { EnhancedMC.displayEGui(new IncompatibleWindowList()); }
		}
	}
	
	@Override
	public void onGroupNotification(ObjectEvent e) {
		if (e instanceof EventKeyboard) {
			EventKeyboard kbe = (EventKeyboard) e;
			if (kbe.getKeyboardType() == KeyboardType.Pressed) {
				if (searchField != null && kbe.getEventKey() != 1) {
					searchField.requestFocus();
					searchField.writeText("" + kbe.getEventChar());
				}
			}
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (searchField != null && keyCode != 1) { searchField.requestFocus(); searchField.writeText("" + typedChar); }
	}
	
	public void openRCM(int mXIn, int mYIn) { addObject(rcm = new SettingsRCM(this)); }
	public void openRCM(int mXIn, int mYIn, SubMod modIn) { addObject(rcm = new SettingsRCM(this, modIn)); }
	
	public void updateList() {
		if (scrollList != null) {
			int curPos = scrollList.getVScrollBar().getScrollPos();
			scrollList.getVScrollBar().reset();
			assembleList();
			scrollList.getVScrollBar().setScrollBarPos(curPos);
		}
	}
}
