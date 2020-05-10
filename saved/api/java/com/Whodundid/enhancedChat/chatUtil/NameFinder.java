package com.Whodundid.enhancedChat.chatUtil;

import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.chatUtil.EChatUtil;
import com.Whodundid.core.util.chatUtil.TimedChatLine;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.enhancedChat.chatOrganizer.ChatOrganizer;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

//10-20-18
//Last edited: Jul 8, 2019
//First Added: 3-22-18
//Author: Hunter Bragg

public class NameFinder {
	
	public static String getNameFromChat(StorageBox<IChatComponent, TimedChatLine> fullComponent) {
		if (fullComponent != null) {
			IChatComponent comp = fullComponent.getObject();
			TimedChatLine chatline = fullComponent.getValue();
			IChatComponent lineComp = chatline.getChatComponent();
			String lfor = lineComp.getFormattedText();
			String lunf = EChatUtil.removeFormattingCodes(lfor);
			
			if (comp != null && lfor != null && lunf != null && !lfor.isEmpty() && !lunf.isEmpty()) {
				ChatStyle s = comp.getChatStyle();
				if (s != null) {
					HoverEvent h = s.getChatHoverEvent();
					if (!lunf.contains(":")) {
						if (lfor.contains("found a ") && lfor.contains("" + EnumChatFormatting.AQUA) && lfor.contains("Mystery Box") && lfor.contains("!")) {
							if (lunf.charAt(0) == '[') {
								return EUtil.subStringToSpace(lunf.substring(14, lunf.length()), 0);
							}
							return EUtil.subStringToSpace(lunf, 0);
						}
						if (lunf.contains(" found an ") && lunf.contains(" !")) {
							return EUtil.subStringToSpace(NameFinder.isolateNameFromChat(lunf), 0);
						}
						if (lfor.contains("" + EnumChatFormatting.YELLOW) && (lunf.contains(" joined.") || lunf.contains(" left."))) {
							int spaces = 0;
							
							if (lunf.startsWith("Guild") || lunf.startsWith("Party")) { spaces = 8; }
							else if (lunf.startsWith("Friend")) { spaces = 9; }
							
							return EUtil.subStringToSpace(lunf, spaces);
						}
						if (lunf.contains(">>>")) {
							return EUtil.subStringToSpace(isolateNameFromChat(lunf.substring(5)), 0);
						}
						if (lfor.contains(EnumChatFormatting.AQUA + "[") || lfor.contains(EnumChatFormatting.GREEN + "[")) {
							return EUtil.subStringToSpace(NameFinder.isolateNameFromChat(lunf), 0);
						}
						if (lunf.charAt(0) == '<') {
							return NameFinder.isolateNameFromChat(lunf);
						}
						if (lfor.contains(" has joined (")) {
							return EUtil.subStringToSpace(isolateNameFromChat(lunf), 0);
						}
					}
					else {
						if (h != null && h.getValue() != null) {
							String unf = EChatUtil.removeFormattingCodes(h.getValue().getUnformattedText());
							if (unf.contains("Hypixel Level: ")) {
								return NameFinder.isolateNameFromChat(unf);
							}
							else if (unf.contains(EChatUtil.getFormattingChar() + "eClick here to view ") && unf.contains(EChatUtil.getFormattingChar() + "e's profile")) {
								unf = unf.replace(EChatUtil.getFormattingChar() + "eClick here to view ", "");
								unf = unf.replace(EChatUtil.getFormattingChar() + "e's profile", "");
								if (unf.length() > 2 && unf.charAt(0) == EChatUtil.getFormattingChar()) { unf = unf.substring(2, unf.length()); }
								return unf;
							}
						}
						else {
							if (lfor.contains(EnumChatFormatting.AQUA + "[") || lfor.contains(EnumChatFormatting.GREEN + "[")) {
								return removeColon(EUtil.subStringToSpace(isolateNameFromChat(lunf), 0));
							}
							
							String line = EUtil.subStringToSpace(lunf, 0);
							return line.substring(0, line.length() - 1);
						}
					}
				}
			}
		}
		return null;
	}
	
	public static String isolateNameFromChat(String chatIn) {
		if (!ChatOrganizer.checkListForMessage(ChatOrganizer.game.getFilterList(), chatIn) && !ChatOrganizer.checkListForMessage(ChatOrganizer.trash.getFilterList(), chatIn)) {
			String name = "";
			if (chatIn.length() > 2) {
				
				if (chatIn.contains("\n")) {
					String chatBefore = "";
					for (int i = 1; i < chatIn.length(); i++) {
						if (chatIn.charAt(i) == '\n') {
							chatBefore = chatIn.substring(0, i);
							break;
						}
					}
					if (chatBefore.charAt(0) == '[') {
						String findRest = "[";
						String chatAfter = "";
						for (int i = 1; i < chatBefore.length(); i++) {
							if (chatBefore.charAt(i) == ']') {
								findRest += ']';
								chatAfter = chatBefore.substring(i + 1, chatBefore.length());
								break;
							}
							findRest += chatBefore.charAt(i);
						}
						chatAfter = chatAfter.trim();
						for (int q = 0; q < chatAfter.length(); q++) {
							if (chatAfter.charAt(q) != '[') {
								name += chatAfter.charAt(q);
							}
						}
						return name;
					}
					name = chatBefore;
					return name;
				}
				
				if (chatIn.charAt(0) == '[') { return findAfter(chatIn, '[', ']'); } //remote hypixel rank
				else if (chatIn.charAt(0) == '<') { return getInside(chatIn, '<', '>'); } //singleplayer and vanilla servers
				name = chatIn;
				return name;
			}
		}
		return "";
	}
	
	private static String findAfter(String in, char test1, char test2) {
		if (in.charAt(0) == test1) {
			String s = "";
			String findRest = "" + test1;
			String chatAfter = "";
			for (int i = 1; i < in.length(); i++) {
				if (in.charAt(i) == test2) {
					findRest += test2;
					chatAfter = in.substring(i + 1, in.length());
					break;
				}
				findRest += in.charAt(i);
			}
			chatAfter = chatAfter.trim();
			for (int q = 0; q < chatAfter.length(); q++) {
				if (chatAfter.charAt(q) != '[') {
					s += chatAfter.charAt(q);
				}
			}
			return s;
		}
		return "";
	}
	
	private static String getInside(String in, char test1, char test2) {
		if (in.charAt(0) == test1) {
			String s = "";
			String chatAfter = "";
			for (int i = 0; i < in.length(); i++) {
				if (in.charAt(i) == test1) {
					chatAfter = in.substring(i + 1, in.length());
				}
			}
			for (int i = 0; i < chatAfter.length(); i++) {
				if (chatAfter.charAt(i) == test2) {
					s = chatAfter.substring(0, i);
					break;
				}
			}
			return s;
		}
		return "";
	}
	
	private static String removeColon(String in) {
		if (in.length() > 1 && in.charAt(in.length() - 1) == ':') {
			in = in.substring(0, in.length() - 1);
		}
		return in;
	}
}
