package com.Whodundid.enhancedChat.chatWindow.windowObjects;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.enhancedGui.guiObjects.utilityObjects.EGuiRightClickMenu;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.enhancedChat.chatWindow.ChatWindow;
import com.Whodundid.enhancedChat.guis.EnhancedChatGuiMain;

public class MainRCM extends EGuiRightClickMenu {

	ChatWindow window;
	
	public MainRCM(ChatWindow windowIn) {
		window = windowIn;
		
		setTitle("Window");
		
		addOption("Chat Settings", EMCResources.guiSettingsButton);
		addOption(window.isPinned() ? "Unpin from HUD" : "Pin to HUD", EMCResources.guiPinButton);
		
		setActionReciever(this);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		String s = (String) getSelectedObject();
		switch (s) {
		case "Chat Settings": EnhancedMC.displayWindow(new EnhancedChatGuiMain()); break;
		case "Pin to HUD":
		case "Unpin from HUD": window.setPinned(!window.isPinned()); break;
		case "Clear Window History": window.clearChatMessages(); break;
		case "Clear Tab History": break;
		}
	}
}
