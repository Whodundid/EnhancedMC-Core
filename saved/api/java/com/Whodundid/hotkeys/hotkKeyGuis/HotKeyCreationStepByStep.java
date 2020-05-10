package com.Whodundid.hotkeys.hotkKeyGuis;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.debug.IDebugCommand;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiTextField;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.EGuiTextArea;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.hotkeys.HotKeyApp;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyActionType;
import com.Whodundid.hotkeys.hotkKeyGuis.util.KeyEntryTextField;
import net.minecraft.client.settings.KeyBinding;

//Last edited: Feb 14, 2019
//First Added: Feb 14, 2019
//Author: Hunter Bragg

public class HotKeyCreationStepByStep extends WindowParent {
	
	HotKeyApp man = (HotKeyApp) RegisteredApps.getApp(AppType.HOTKEYS);
	HotKey creationKey;
	KeyEntryTextField keyEntry;
	EGuiTextArea selectionList;
	EGuiTextField nameEntry, commandEntry, argEntry;
	EGuiButton cancelButton, nextButton, createButton, backButton;
	String keyName = "";
	boolean enabledVal = true;
	KeyActionType selectedHotKeyType;
	Class selectedGui;
	//EScript selectedScript;
	IDebugCommand selectedDebug;
	AppType selectedMod;
	KeyBinding selectedKeyBind;
	int currentScreen = 0;
	
	@Override
	public void initGui() {
		super.initGui();
		defaultPos();
		loadScreen(currentScreen);
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		
		drawStringCS("Not ready :)", midX, midY, EColors.rainbow());
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == nextButton) {
			
		}
	}
	
	private void loadScreen(int screenNumIn) {
		switch (screenNumIn) {
		case 0:
		case 1:
		case 2:
		case 3:
		default: break;
		}
	}
	
	private void loadSelectionList() {
		
	}
	
	private void hideAllObjects() {
		
	}
	
	private void createKey() {
		
	}
}
