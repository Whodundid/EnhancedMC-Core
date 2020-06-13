package com.Whodundid.core.util.chatUtil;

import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.renderUtil.GLObject;
import com.Whodundid.core.util.storageUtil.EArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MovingObjectPosition;

//Author: Hunter Bragg

public class EChatUtil {
	
	static Minecraft mc = Minecraft.getMinecraft();
	private static boolean chatOpen = false;
	private static IChatComponent lastChat = null;
	private static String lastTypedMessage = "";
	private static ITabCompleteListener tabListener = null;
	public static volatile ScreenTextObject threadTextObject = null;
	private static EArrayList<ScreenTextObject> textsToAdd = new EArrayList();
	private static Deque<ScreenTextObject> textsToDraw = new ArrayDeque();
	private static boolean adding = false;
	
	public static void drawTexts() {
		updateTexts();
		
		if (threadTextObject != null) {
			ScreenTextObject t = threadTextObject;
			if (t != null) {
				if (t.getCenter() && t.getShadow()) { GLObject.drawStringCS(t.getText(), t.getX(), t.getY(), t.getColor()); }
				else if (t.getCenter()) { GLObject.drawStringC(t.getText(), t.getX(), t.getY(), t.getColor()); }
				else if (t.getShadow()) { GLObject.drawStringS(t.getText(), t.getX(), t.getY(), t.getColor()); }
				else { GLObject.drawString(t.getText(), t.getX(), t.getY(), t.getColor()); }
			}
		}
		
		if (!adding) {
			while (!textsToDraw.isEmpty()) {
				ScreenTextObject t = textsToDraw.pop();
				
				if (t != null) {
					if (t.getCenter() && t.getShadow()) { GLObject.drawStringCS(t.getText(), t.getX(), t.getY(), t.getColor()); }
					else if (t.getCenter()) { GLObject.drawStringC(t.getText(), t.getX(), t.getY(), t.getColor()); }
					else if (t.getShadow()) { GLObject.drawStringS(t.getText(), t.getX(), t.getY(), t.getColor()); }
					else { GLObject.drawString(t.getText(), t.getX(), t.getY(), t.getColor()); }
				}
				
			}
		}
		
	}
	
	private static void updateTexts() {
		synchronized (textsToAdd) {
			if (!adding) {
				adding = true;
				if (textsToAdd != null && textsToDraw != null && textsToAdd.isNotEmpty()) {
					textsToDraw.addAll(textsToAdd);
					textsToAdd.clear();
				}
				adding = false;
			}
		}
	}
	
	public static ScreenTextObject drawString(String textIn, int xIn, int yIn) { return drawString(textIn, xIn, yIn, EColors.white.intVal, false, false); }
	public static ScreenTextObject drawString(String textIn, int xIn, int yIn, EColors colorIn) { return drawString(textIn, xIn, yIn, colorIn.intVal, false, false); }
	public static ScreenTextObject drawString(String textIn, int xIn, int yIn, int colorIn) { return drawString(textIn, xIn, yIn, colorIn, false, false); }
	public static ScreenTextObject drawString(String textIn, int xIn, int yIn, EColors colorIn, boolean centerIn, boolean shadowIn) { return drawString(textIn, xIn, yIn, colorIn.intVal, centerIn, shadowIn); }
	public static ScreenTextObject drawString(String textIn, int xIn, int yIn, int colorIn, boolean centerIn, boolean shadowIn) {
		return drawString(new ScreenTextObject(textIn, xIn, yIn, colorIn, centerIn, shadowIn));
	}
	
	public static ScreenTextObject drawString(ScreenTextObject objIn) {
		synchronized (textsToAdd) {
			textsToAdd.add(objIn);
		}
		return objIn;
	}
	
	/** Attempts to display a message to the player in the chat. (does not actually send a chat message) */
	public static void show(String msgIn) {
		if (mc.thePlayer != null && msgIn != null) {
			mc.thePlayer.addChatMessage(ChatBuilder.of(msgIn).build());
		}
	}
	
	/** Removes all lingering minecraft FontRenderer chat formatting codes from EnumChatFormatting. */
	public static String removeFormattingCodes(String chatIn) {
		String s = "";
		for (int i = 0; i < chatIn.length(); i++) {
			char c = chatIn.charAt(i);
			if (c != getFormattingChar()) { // this symbol: §
				s += c;
			}
			else {
				i += 1; //remove the following code
			}
		}
		return s;
	}
	
	/** Returns this symbol: § */
	public static char getFormattingChar() { return '\u00A7'; }
	
	public static void registerTabListener(ITabCompleteListener objectIn, String checkWord, String upToCursor) {
		if (EUtil.nullCheck(objectIn, checkWord, upToCursor)) {
			tabListener = objectIn;
			beginTabRequest(checkWord, upToCursor);
		}
	}
	
	public static void onTabComplete(String[] result) {
		if (tabListener != null) {
			tabListener.onTabCompletion(result);
			tabListener = null;
		}
	}
	
	private static void beginTabRequest(String in1, String in2) {
		if (in1.length() >= 1) {
			net.minecraftforge.client.ClientCommandHandler.instance.autoComplete(in1, in2);
			BlockPos blockpos = null;

			if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
				blockpos = mc.objectMouseOver.getBlockPos();
			}

			mc.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete(in1, blockpos));
		}
	}
	
	public static void readChat(IChatComponent chatMsg) { lastChat = chatMsg; }
	
	public static boolean isChatOpen() { return mc.ingameGUI.getChatGUI().getChatOpen(); }
	public static String getLMsgFor() { return lastChat != null ? lastChat.getFormattedText() : ""; }
	public static String getLMsgUnf() { return lastChat != null ? removeFormattingCodes(lastChat.getUnformattedText()) : ""; }
	public static void sendLongerChatMessage(String messageIn) { new LongerChatMessage(messageIn).sendMessage(); }
	public static void setLastTypedMessage(String msgIn) { lastTypedMessage = msgIn; }
	public static String getLastTypedMessage() { return lastTypedMessage; }
	
	public static boolean checkMsgUnfContains(String testIn) { return lastChat != null ? getLMsgUnf().contains(testIn) : false; }
	public static boolean checkMsgForContains(String testIn) { return lastChat != null ? getLMsgFor().contains(testIn) : false; }
	
}
