package com.Whodundid.enhancedChat.chatWindow.windowObjects;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.enhancedGui.guiObjects.utilityObjects.EGuiRightClickMenu;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.enhancedChat.chatWindow.ChatWindow;

public class PlayerRCM extends EGuiRightClickMenu {

	ChatWindow window;
	String playerName;
	
	public PlayerRCM(ChatWindow windowIn, String playerNameIn) {
		window = windowIn;
		playerName = playerNameIn;
		
		setTitle(playerName);
		
		addOption("Direct Message");
		addOption("Invite to Party");
		if (RegisteredApps.isAppRegEn(AppType.PLAYERINFO)) {
			addOption("View Name History");
			addOption("View Skin");
		}
		if (RegisteredApps.isAppRegEn(AppType.MINIMAP)) { addOption("Track Location"); }
		
		setActionReciever(this);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		switch ((String) getSelectedObject()) {
		case "Direct Message":
			window.getEntryField().setText("/msg " + playerName + " ");
			break;
		case "Invite to Party":
			mc.thePlayer.sendChatMessage("/p " + playerName);
			window.getScrollBar().setScrollBarPos(window.getWindowHistory().size());
			break;
		case "View Name History":
			if (RegisteredApps.isAppRegEn(AppType.PLAYERINFO)) {
				RegisteredApps.getApp(AppType.PLAYERINFO).sendArgs("PlayerInfo: fetch names", playerName);
			}
			window.getScrollBar().setScrollBarPos(window.getWindowHistory().size());
			break;
		case "View Skin":
			if (RegisteredApps.isAppRegEn(AppType.PLAYERINFO)) {
				RegisteredApps.getApp(AppType.PLAYERINFO).sendArgs("PlayerInfo: fetch skin", playerName);
			}
			break;
		case "Track Location":
			if (RegisteredApps.isAppRegEn(AppType.MINIMAP)) {
				RegisteredApps.getApp(AppType.MINIMAP).sendArgs("MiniMap: track", playerName);
			}
			break;
		}
	}
}
