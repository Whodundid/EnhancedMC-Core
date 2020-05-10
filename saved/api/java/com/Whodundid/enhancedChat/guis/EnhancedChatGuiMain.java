package com.Whodundid.enhancedChat.guis;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiTextField;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.scrollList.EGuiScrollList;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiContainer;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.objectEvents.EventFocus;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.enhancedChat.EnhancedChatApp;
import com.Whodundid.enhancedChat.chatWindow.ChatWindow;
import net.minecraft.util.MathHelper;

//Last edited: Jan 18, 2019
//First Added: Jan 18, 2019
//Author: Hunter Bragg

public class EnhancedChatGuiMain extends WindowParent {
	
	EnhancedChatApp chatMod = (EnhancedChatApp) RegisteredApps.getApp(AppType.ENHANCEDCHAT);
	EGuiScrollList scrollList;
	EGuiButton showChatInfo, showTimeStamps, warnOnLinks, pinWindows, blockMsgTStamps;
	EGuiButton appearanceSettings;
	//EGuiButton chatOrganizerSettings;
	EGuiButton historyReset;
	EGuiLabel chatInfoLabel, timeStampsLabel, warnLinksLabel, pinWindowsLabel, historyLengthLabel, blockMsgTStampsLabel;
	EGuiLabel lengthLabel, currentLength;
	EGuiTextField historyLengthInput;
	EGuiContainer global, history, subSetting;
	
	public EnhancedChatGuiMain() {
		super();
		aliases.add("chatmain");
	}
	
	@Override
	public void initGui() {
		setObjectName("Enhanced Chat Settings");
		defaultDims();
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		//global
		global = new EGuiContainer(this, startX + 2, startY + 2, width - 4, 103);
		global.setTitle("General Chat");
		EDimension gDim = global.getDimensions();
		
		scrollList = new EGuiScrollList(global, gDim.startX, gDim.startY + 17, gDim.width, gDim.height - 17);
		
		showChatInfo = new EGuiButton(scrollList, 5, 5, 55, 16);
		chatInfoLabel = new EGuiLabel(scrollList, showChatInfo.endX + 7, showChatInfo.midY - 4, "Show additional chat info").setDisplayStringColor(0xb2b2b2);
		
		showTimeStamps = new EGuiButton(scrollList, 5, showChatInfo.endY + 4, 55, 16);
		timeStampsLabel = new EGuiLabel(scrollList, showTimeStamps.endX + 7, showTimeStamps.midY - 4, "Show message time stamps").setDisplayStringColor(0xb2b2b2);
		
		warnOnLinks = new EGuiButton(scrollList, 5, showTimeStamps.endY + 4, 55, 16);
		warnLinksLabel = new EGuiLabel(scrollList, warnOnLinks.endX + 7, warnOnLinks.midY - 4, "Warn opening internet links").setDisplayStringColor(0xb2b2b2);
		
		pinWindows = new EGuiButton(scrollList, 5, warnOnLinks.endY + 4, 55, 16);
		pinWindowsLabel = new EGuiLabel(scrollList, pinWindows.endX + 7, pinWindows.midY - 4, "Make new windows pinned").setDisplayStringColor(0xb2b2b2);
		
		blockMsgTStamps = new EGuiButton(scrollList, 5, pinWindows.endY + 4, 55, 16);
		blockMsgTStampsLabel = new EGuiLabel(scrollList, blockMsgTStamps.endX + 7, blockMsgTStamps.midY - 4, "Space TStamps by message").setDisplayStringColor(0xb2b2b2);
		
		scrollList.growListHeight(104);
		
		showChatInfo.setTrueFalseButton(true).updateTrueFalseDisplay(chatMod.showMoreChatInfo.get()).setActionReciever(guiInstance);
		showTimeStamps.setTrueFalseButton(true).updateTrueFalseDisplay(chatMod.showTimeStamps.get()).setActionReciever(guiInstance);
		warnOnLinks.setTrueFalseButton(true).updateTrueFalseDisplay(chatMod.warnOnLinks.get()).setActionReciever(guiInstance);
		pinWindows.setTrueFalseButton(true).updateTrueFalseDisplay(chatMod.makeNewWindowsPinned.get()).setActionReciever(guiInstance);
		
		scrollList.addObjectToList(showChatInfo, chatInfoLabel, showTimeStamps, timeStampsLabel, warnOnLinks, warnLinksLabel, pinWindows, pinWindowsLabel, blockMsgTStamps, blockMsgTStampsLabel);
		global.addObject(scrollList);
		
		//history
		history = new EGuiContainer(this, startX + 2, global.endY + 1, width - 4, 66);
		history.setTitle("Chat History");
		EDimension hDim = history.getDimensions();
		
		historyLengthLabel = new EGuiLabel(history, hDim.startX + 10, hDim.startY + 25, "Chat History Length").setDisplayStringColor(0xb2b2b2);
		historyLengthInput = new EGuiTextField(history, hDim.startX + 11, hDim.startY + 42, 55, 16) {
			@Override
			public void onFocusGained(EventFocus e) {
				if (!alwaysDrawCursor) { EnhancedMC.updateCounter = 0; }
				text = "";
				setTextColor(enabledColor);
				setCursorPosition(0);
			}
			@Override
			public void keyPressed(char typedChar, int keyCode) {
				if (keyCode == 28) {
					if (!getText().isEmpty()) {
						double parsedVal = 0;
						try {
							parsedVal = Double.parseDouble(getText());
							int val = (int) parsedVal;
							val = MathHelper.clamp_int(val, 1, Integer.MAX_VALUE);
							chatMod.chatHistoryLength = val;
						} catch (Exception e) {
							chatMod.chatHistoryLength = chatMod.defaultHistoryLength;
							e.printStackTrace();
						}
						currentLength.setDisplayString("" + chatMod.chatHistoryLength);
						return;
					}
				}
				super.keyPressed(typedChar, keyCode);
			}
		};
		historyLengthInput.setTextWhenEmpty("length");
		
		lengthLabel = new EGuiLabel(history, historyLengthInput.endX + 10, historyLengthInput.midY - 4, "Current:").setDisplayStringColor(0xffbb00);
		currentLength = new EGuiLabel(history, historyLengthInput.endX + 60, historyLengthInput.midY - 4, "" + chatMod.chatHistoryLength).setDisplayStringColor(0x00ffdc);
		
		historyReset = new EGuiButton(history, hDim.endX - 50, historyLengthInput.startY - 1, 40, 18, "Reset");
		historyReset.setActionReciever(guiInstance);
		
		history.addObject(historyLengthLabel, historyLengthInput, lengthLabel, currentLength, historyReset);
		
		//sub setting
		subSetting = new EGuiContainer(this, startX + 2, history.endY + 1, width - 4, (guiInstance.endY - 2) - (history.endY + 1));
		subSetting.setTitle("Specific Menus");
		EDimension sDim = subSetting.getDimensions();
		
		//chatOrganizerSettings = new EGuiButton(subSetting, sDim.startX + 10, sDim.startY + 25, sDim.width - 20, 20, "Chat Organizer");
		//appearanceSettings = new EGuiButton(subSetting, sDim.startX + 10, chatOrganizerSettings.endY + 5, sDim.width - 20, 20, "Window Appearance");
		appearanceSettings = new EGuiButton(subSetting, sDim.startX + 10, sDim.startY + 25, sDim.width - 20, 20, "Window Appearance");
		
		//chatOrganizerSettings.setActionReciever(guiInstance);
		appearanceSettings.setActionReciever(guiInstance);
		
		subSetting.addObject(appearanceSettings);
		//subSetting.addObject(chatOrganizerSettings);
		
		addObject(global, history, subSetting);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == showChatInfo) {
			showChatInfo.toggleTrueFalse(chatMod.showMoreChatInfo, chatMod, false);
		}
		if (object == showTimeStamps) {
			showTimeStamps.toggleTrueFalse(chatMod.showTimeStamps, chatMod, false);
			for (ChatWindow w : chatMod.getRenderBridge().getAllActiveChatWindows()) { w.updateVisual(); }
		}
		if (object == warnOnLinks) {
			warnOnLinks.toggleTrueFalse(chatMod.warnOnLinks, chatMod, false);
		}
		if (object == pinWindows) {
			pinWindows.toggleTrueFalse(chatMod.makeNewWindowsPinned, chatMod, false);
		}
		if (object == historyReset) {
			chatMod.chatHistoryLength = chatMod.defaultHistoryLength;
			currentLength.setDisplayString("" + chatMod.chatHistoryLength);
			chatMod.getConfig().saveMainConfig();
		}
		//if (object == chatOrganizerSettings) {
		//	EnhancedMC.displayEGui(new ChatOrganizerGui(), this);
		//}
		if (object == appearanceSettings) {
			EnhancedMC.displayWindow(new AppearanceGui(), this);
		}
	}
}
