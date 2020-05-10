package com.Whodundid.enhancedChat.chatWindow.windowObjects2;

import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.scrollList.EGuiScrollList;
import com.Whodundid.core.util.chatUtil.TimedChatLine;
import com.Whodundid.enhancedChat.chatWindow.ChatWindow;

public class ChatDisplay extends EGuiScrollList {

	ChatWindow window;
	
	public ChatDisplay(ChatWindow windowIn) {
		super(windowIn, windowIn.startX, windowIn.startY, windowIn.endX, windowIn.endY);
		
		window = windowIn;
	}
	
	public void addChatMessage(TimedChatLine lineIn) {
		
	}
}
