package com.Whodundid.core.util.chatUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;

//Last edited: 10-20-18
//First Added: 3-22-18
//Author: Hunter Bragg

public class EChatUtil {
	
	static Minecraft mc = Minecraft.getMinecraft();
	private static boolean chatOpen = false;
	private static IChatComponent lastChat = null;
	private static String lastTypedMessage = "";
	
	public static String removeFormattingCodes(String chatIn) {
		String s = "";
		for (int i = 0; i < chatIn.length(); i++) {
			char c = chatIn.charAt(i);
			if (c != '\u00A7') { // this symbol: §
				s += c;
			} else {
				i += 1;
			}
		}
		return s;
	}
	
	public static void readChat(IChatComponent chatMsg) { lastChat = chatMsg; }
	
	public void checkIfChatWindowOpen() { chatOpen = mc.ingameGUI.getChatGUI().getChatOpen(); }
	public static boolean isChatOpen() { return chatOpen; }
	public static String getLMsgFor() { return lastChat != null ? lastChat.getFormattedText() : ""; }
	public static String getLMsgUnf() { return lastChat != null ? removeFormattingCodes(lastChat.getUnformattedText()) : ""; }
	public static void sendLongerChatMessage(String messageIn) { new LongerChatMessage(messageIn).sendMessage(); }
	public static void setLastTypedMessage(String msgIn) { lastTypedMessage = msgIn; }
	public static String getLastTypedMessage() { return lastTypedMessage; }
	
	public static boolean checkMsgUnfContains(String testIn) { return lastChat != null ? getLMsgUnf().contains(testIn) : false; }
	public static boolean checkMsgForContains(String testIn) { return lastChat != null ? getLMsgFor().contains(testIn) : false; }
}
