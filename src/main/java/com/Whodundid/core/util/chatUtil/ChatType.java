package com.Whodundid.core.util.chatUtil;

import net.minecraft.util.EnumChatFormatting;

//Last edited: 11-18-17
//First Added: 4-14-17
//Author: Hunter Bragg

public enum ChatType {
	ALL("All", 0xFFAA88, EnumChatFormatting.YELLOW),
	GAME("Game", 0xFFAA00, EnumChatFormatting.GOLD),
	GUILD("Guild", 0x00AA00, EnumChatFormatting.DARK_GREEN),
	PARTY("Party", 0x5555FF, EnumChatFormatting.BLUE),
	PRIVATE("Private", 0xFFAA00, EnumChatFormatting.LIGHT_PURPLE),
	LOBBY("Lobby", 0x55FF55, EnumChatFormatting.GREEN),
	PLAYER("Player", 0x76DBE2, EnumChatFormatting.LIGHT_PURPLE),
	NONE("None", 0xFFFFFF, EnumChatFormatting.WHITE);
	
	private final String type;
	private final int color;
	private final EnumChatFormatting mcColor;
	
	private ChatType(String typeIn, int displayColorIn, EnumChatFormatting mcColorIn) {
		type = typeIn;
		color = displayColorIn;
		mcColor = mcColorIn;
	}
	
	public String getChatType() { return type; }
	public int getChatDisplayColor() { return color; }
	public EnumChatFormatting getMCColor() { return mcColor; }
	public boolean isNone() { return type.equals("None");  }
	
	public static String getDisplayName(ChatType typeIn) { return typeIn.getChatType(); }
	public static int getDisplayColor(ChatType typeIn) { return typeIn.getChatDisplayColor(); }
	public static EnumChatFormatting getMCColorCode(ChatType typeIn) { return typeIn.getMCColor(); }
}