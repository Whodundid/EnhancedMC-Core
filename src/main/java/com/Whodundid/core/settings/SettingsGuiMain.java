package com.Whodundid.core.settings;

import com.Whodundid.core.EnhancedMC;
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
import com.Whodundid.core.subMod.SubModErrorDialogueBox;
import com.Whodundid.core.subMod.SubModErrorType;
import com.Whodundid.core.subMod.SubModInfoDialogueBox;
import com.Whodundid.core.subMod.SubModSettings;
import com.Whodundid.core.subMod.SubModType;
import com.Whodundid.core.util.miscUtil.ChatBuilder;
import com.Whodundid.core.util.miscUtil.EUtil;
import com.Whodundid.core.util.renderUtil.Resources;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.StorageBox;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;

//Dec 28, 2018

//Last edited: Jun 28, 2019
//Edit note: replaced page system with scrollable list to make space for more options
//First Added: Sep 14, 2018
//Author: Hunter Bragg

public class SettingsGuiMain extends EnhancedGui {
	
	EGuiButton keyBindGui, reloadConfigs, experimentGui, disableDebugMode, hiddenButton1, hiddenButton2;
	SubModErrorDialogueBox errorBox;
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
		//centerGuiWithDimensions(220, 256);
		setGuiName("EMC Settings");
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		reloadConfigs = new EGuiButton(this, startX + 5, endY - 25, 90, 20, "Reload Configs");
		keyBindGui = new EGuiButton(this, endX - 95, endY - 25, 90, 20, "MC KeyBinds");
		experimentGui = new EGuiButton(this, 1, res.getScaledHeight() - 21, 85, 20, "ExperimentGui");
		disableDebugMode = new EGuiButton(this, experimentGui.endX + 1, res.getScaledHeight() - 21, 85, 20, "Disable Debug");
		
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
		
		hiddenButton2 = new EGuiButton(this, endX - 11, startY + 1, 10, 10) {
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
		
		addObject(hiddenButton1, hiddenButton2);
		
		addObject(scrollList, experimentGui, disableDebugMode, searchField, keyBindGui, reloadConfigs);
		
		addObject(separator);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		drawDefaultBackground();
		
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
		if (searchField != null && !searchField.getText().equals("Search mods...")) {
			for (SubMod m : RegisteredSubMods.getRegisteredModsList()) {
				if (SubModType.getModName(m.getModType()).toLowerCase().contains(searchField.getText().toLowerCase())) { filteredMods.add(m); }
			}
		}
		else { filteredMods.addAll(RegisteredSubMods.getRegisteredModsList()); }
		
		found = filteredMods.size();
		
		if (filteredMods.size() > 0) {
			EDimension l = scrollList.getListDimensions();
			
			for (int i = 0; i < filteredMods.size(); i++) {
				SubMod m = filteredMods.get(i);
				
				int dist = 22;
				
				EGuiButton modSettings = new EGuiButton(scrollList, l.startX + 5, l.startY + 2 + (i * dist), 110, 20, SubModType.getModName(m.getModType())) {
					{ setRunActionOnPress(true); }
					@Override public void performAction() {
						if (getPressedButton() == 0) {
							playPressSound();
							if (m != null) {
								StorageBox potato = new StorageBox(guiInstance.startX, guiInstance.startY);
								EnhancedGui gui = m.getMainGui(true, potato, guiInstance);
								if (gui != null) { mc.displayGuiScreen(gui); }
								else {
									errorBox = new SubModErrorDialogueBox(guiInstance, guiInstance.midX - 125, guiInstance.midY - 48, 250, 75, SubModErrorType.NOGUI, m);
									guiInstance.addObject(errorBox);
								}
							}
						}
					}
				};
				EGuiButton enabled = new EGuiButton(scrollList, l.endX - 79, l.startY + 2 + (i * dist), 50, 20, m.isDisableable() ? (m.isEnabled() ? "Enabled" : "Disabled") : "Enabled") {
					{
						setRunActionOnPress(true);
						setEnabled(m.isDisableable());
						setDisplayStringColor(m.isEnabled() ? 0x55ff55 : 0xff5555);
					}
					@Override public void performAction() {
						if (getPressedButton() == 0) {
							playPressSound();
							if (m != null) {
								if (!m.isEnabled()) {
									EArrayList<SubModType> allDependencies = RegisteredSubMods.getAllModDependencies(m);
									EArrayList<SubMod> disabledDependancies = new EArrayList();
									allDependencies.forEach((t) -> { SubMod m = RegisteredSubMods.getMod(t); if (!m.isEnabled()) { disabledDependancies.add(m); } });
									if (!disabledDependancies.isEmpty()) {
										errorBox = new SubModErrorDialogueBox(guiInstance, guiInstance.midX - 125, guiInstance.midY - 48, 250, 75, SubModErrorType.ENABLE, m);
										errorBox.createErrorMessage(disabledDependancies);
										guiInstance.addObject(errorBox);
										return;
									}
								} else {
									EArrayList<SubModType> allDependents = RegisteredSubMods.getAllDependantsOfMod(m);
									EArrayList<SubMod> enabledDependants = new EArrayList();
									allDependents.forEach(t -> { SubMod m = RegisteredSubMods.getMod(t); if (m.isEnabled()) { enabledDependants.add(m); } }); 
									if (!enabledDependants.isEmpty()) {
										errorBox = new SubModErrorDialogueBox(guiInstance, guiInstance.midX - 125, guiInstance.midY - 48, 250, 75, SubModErrorType.DISABLE, m);
										errorBox.createErrorMessage(enabledDependants);
										guiInstance.addObject(errorBox);
										return;
									}
								}
							}
							SubModSettings.updateModState(m, !m.isEnabled());
							setDisplayString(m.isEnabled() ? "Enabled" : "Disabled");
							setDisplayStringColor(m.isEnabled() ? 0x55ff55 : 0xff4444);
						}
					}
				};
				EGuiButton info = new EGuiButton(scrollList, l.endX - 25, l.startY + 2 + (i * dist), 20, 20) {
					{
						setRunActionOnPress(true);
						setButtonTexture(Resources.guiInfo);
						setButtonSelTexture(Resources.guiInfoSel);
					}
					@Override public void performAction() {
						if (getPressedButton() == 0) {
							playPressSound();
							SubModInfoDialogueBox infoBox = new SubModInfoDialogueBox(guiInstance, guiInstance.midX - 75, guiInstance.midY - 101, 150, 202, m);
							guiInstance.addObject(infoBox);
						}
					}
				};
				
				if (i >= 8) {
					scrollList.setListHeight(scrollList.getListHeight() + 2 + modSettings.height);
				}
				
				scrollList.addObjectToList(modSettings, enabled, info);
			}
			
			//if (filteredMods.size() > 8) { scrollList.setListHeight(scrollList.getListHeight() + 1); }
			scrollList.renderScrollBarThumb(filteredMods.size() > 8);
		}
		else {
			scrollList.addObjectToList(new EGuiLabel(scrollList, (width - 10) / 2, 80, "No matches found", 0xff5555).enableWordWrap(true, 125).setDrawCentered(true));
		}
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
		}
	}
}
