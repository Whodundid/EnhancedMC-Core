package com.Whodundid.core.settings;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreSubMod.EnhancedMCMod;
import com.Whodundid.core.debug.ExperimentGui;
import com.Whodundid.core.debug.ImportantGui;
import com.Whodundid.core.enhancedGui.EnhancedGui;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiRect;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiScrollList;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiTextField;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.subMod.SubModSettings;
import com.Whodundid.core.subMod.SubModType;
import com.Whodundid.core.subMod.gui.IncompatibleWindowList;
import com.Whodundid.core.subMod.gui.SubModMenuContainer;
import com.Whodundid.core.util.miscUtil.ChatBuilder;
import com.Whodundid.core.util.miscUtil.EUtil;
import com.Whodundid.core.util.renderUtil.Resources;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import net.minecraft.util.EnumChatFormatting;

//Dec 28, 2018

//Last edited: Jun 28, 2019
//Edit note: replaced page system with scrollable list to make space for more options
//First Added: Sep 14, 2018
//Author: Hunter Bragg

public class SettingsGuiMain extends EnhancedGui {
	
	EGuiButton keyBindGui, reloadConfigs, problem;
	EGuiButton experimentGui, disableDebugMode;
	EGuiButton hiddenButton1, hiddenButton2;
	EGuiScrollList scrollList;
	EGuiTextField searchField;
	protected int pageToBeLoaded = -1;
	int leftPress = 0, rightPress = 0;
	int found = 0;
	
	public SettingsGuiMain() { super(); }
	public SettingsGuiMain(int posX, int posY) { super(posX, posY); }
	public SettingsGuiMain(int posX, int posY, int page) { super(posX, posY); pageToBeLoaded = page; }
	
	@Override
	public void initGui() {
		setGuiName("EMC Settings");
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		reloadConfigs = new EGuiButton(this, startX + 5, endY - 25, 90, 20, "Reload Configs");
		keyBindGui = new EGuiButton(this, endX - 95, endY - 25, 90, 20, "MC KeyBinds");
		experimentGui = new EGuiButton(this, 1, res.getScaledHeight() - 21, 85, 20, "ExperimentGui");
		disableDebugMode = new EGuiButton(this, experimentGui.endX + 1, res.getScaledHeight() - 21, 85, 20, "Disable Debug");
		problem = new EGuiButton(this, endX - 17, startY + 2, 15, 15).setTextures(Resources.guiProblemOpen, Resources.guiProblemOpenSel);
		
		problem.setVisible(RegisteredSubMods.getIncompatibleModsList().isNotEmpty());
		problem.setDrawBackground(true).setBackgroundColor(0xffbb0000);
		
		experimentGui.setVisible(EnhancedMC.isDebugMode());
		experimentGui.setPositionLocked(true);
		disableDebugMode.setVisible(EnhancedMC.isDebugMode());
		disableDebugMode.setPositionLocked(true);
		EUtil.setObjectPersistence(true, keyBindGui, reloadConfigs);
		
		hiddenButton1 = new EGuiButton(this, startX + 1, startY + 1, 10, 10) {
			{
				setRunActionOnPress(true);
				setButtonTexture(Resources.emptyPixel);
				setButtonSelTexture(Resources.emptyPixel);
			}
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
			{
				setRunActionOnPress(true);
				setButtonTexture(Resources.emptyPixel);
				setButtonSelTexture(Resources.emptyPixel);
			}
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
		
		scrollList = new EGuiScrollList(this, startX + 5, startY + 23, 210, 180).setBackgroundColor(0xff252525);
		
		searchField = new EGuiTextField(this, scrollList.startX + 1, scrollList.endY + 6, scrollList.width - 4, 15) {
			@Override
			public void keyPressed(char typedChar, int keyCode) {
				super.keyPressed(typedChar, keyCode);
				assembleList();
			}
		};
		searchField.setTextWhenEmpty("Search mods...").setEnableBackgroundDrawing(true);
		
		if (RegisteredSubMods.getRegisteredModsList().isEmpty()) { //THIS SHOULD BE IMPOSSIBLE!
			scrollList.addObjectToList(new EGuiLabel(scrollList, (width - 10) / 2, 80, "No Enhanced MC Sub Mods Detected", 0xff5555).enableWordWrap(true, 125).setDrawCentered(true));
		}
		else { assembleList(); }
		
		EDimension l = scrollList.getDimensions();
		EGuiRect separator = new EGuiRect(this, l.getMidX() + 16, l.startY, l.getMidX() + 17, l.endY, 0xff000000);
		
		addObject(scrollList, problem, searchField, keyBindGui, reloadConfigs);
		addObject(hiddenButton1, hiddenButton2);
		addObject(experimentGui, disableDebugMode);
		
		addObject(separator);
		
		searchField.bringToFront();
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		drawDefaultBackground();
		
		if (RegisteredSubMods.getIncompatibleModsList().isNotEmpty()) {
			problem.setDrawBackground(EnhancedMC.updateCounter / 30 % 2 == 0);
		}
		
		drawRect(startX + 1, startY + 19, endX - 1, endY - 1, -0x00cfcfcf); //grey background
		drawRect(startX, startY + 18, endX, startY + 19, 0xff000000); //top line
		
		drawCenteredStringWithShadow("Enhanced MC Sub Mods", midX, startY + 6, 0xffbb00);
		
		super.drawObject(mXIn, mYIn, ticks);
	}
	
	private void assembleList() {
		//reset the list first
		scrollList.clearList();
		scrollList.setListHeight(scrollList.height - 2);
		
		//not important
		if (searchField.getText().toLowerCase().equals("whodundid")) { mc.displayGuiScreen(new ImportantGui()); return; }
		
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
		
		EDimension l = scrollList.getListDimensions();
		int size = filteredMods.size() + (EnhancedMCMod.showIncompats.get() ? incompats.size() : 0);
		
		if (size > 0) {
			int count = 0;
			for (int i = 0; i < filteredMods.size(); i++) {
				SubMod m = filteredMods.get(i);
				
				SubModMenuContainer container = new SubModMenuContainer(scrollList, m, count);
				scrollList.addObjectToList(container);
				count++;
			}
			
			if (EnhancedMCMod.showIncompats.get()) {
				for (int i = 0; i < incompats.size(); i++) {
					SubMod m = incompats.get(i);
					
					SubModMenuContainer container = new SubModMenuContainer(scrollList, m, count);
					scrollList.addObjectToList(container);
					count++;
				}
			}
		}
		
		if (size >= 8) { scrollList.setListHeight(scrollList.getListHeight() + ((22) * (size - 8))); }
		scrollList.renderScrollBarThumb(size > 8);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object.runActionOnPress()) { object.performAction(); }
		else {
			if (object.equals(keyBindGui)) {
				mc.displayGuiScreen(new KeyBindGui(startX, startY, this));
			}
			if (object.equals(reloadConfigs)) {
				for (SubMod m : RegisteredSubMods.getRegisteredModsList()) {
					if (m.hasConfig()) {
						if (mc.thePlayer != null) { mc.thePlayer.addChatComponentMessage(ChatBuilder.of(EnumChatFormatting.YELLOW +  "Reloading " + m.getName() + " config..").build()); }
						m.getConfig().loadAllConfigs();
						m.getConfig().saveAllConfigs();
					}
				}
				
				if (mc.thePlayer != null) { mc.thePlayer.addChatComponentMessage(ChatBuilder.of(EnumChatFormatting.YELLOW + "Reloading global config..").build()); }
				SubModSettings.loadConfig();
				
				assembleList();
			}
			if (object.equals(experimentGui)) {
				mc.displayGuiScreen(new ExperimentGui(guiInstance));
			}
			if (object == disableDebugMode) {
				EnhancedMC.setDebugMode(false);
				experimentGui.setVisible(false);
				disableDebugMode.setVisible(false);
				leftPress = 0;
				rightPress = 0;
			}
			if (object == problem) {
				addObject(new IncompatibleWindowList(this));
			}
		}
	}
	
	public void updateList() {
		if (scrollList != null) {
			int curPos = scrollList.getScrollBar().getScrollPos();
			assembleList();
			scrollList.getScrollBar().setScrollBarPos(curPos);
		}
	}
}
