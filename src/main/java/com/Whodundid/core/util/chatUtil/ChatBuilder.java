package com.Whodundid.core.util.chatUtil;

import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.event.HoverEvent.Action;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;

//Author: Semx11

public class ChatBuilder {

	private IChatComponent parent;
	private String text;
	private ChatStyle style;

	public ChatBuilder(String text) { this(text, null, Inheritance.SHALLOW); }
	public ChatBuilder(String textIn, IChatComponent parentIn, Inheritance inheritance) {
		parent = parentIn;
		text = textIn;

		switch (inheritance) {
		case DEEP: style = parentIn != null ? parentIn.getChatStyle() : new ChatStyle(); break;
		default:
			case SHALLOW: style = new ChatStyle(); break;
			case NONE:
				style = new ChatStyle().setColor(null).setBold(false).setItalic(false).setStrikethrough(false)
									   .setUnderlined(false).setObfuscated(false).setChatClickEvent(null).setChatClickEvent(null)
									   .setInsertion(null);
				break;
		}
	}

	public static ChatBuilder of(String text) { return new ChatBuilder(text); }
	
	public ChatBuilder setColor(EnumChatFormatting color) { style.setColor(color); return this; }
	public ChatBuilder setBold(boolean bold) { style.setBold(bold); return this; }
	public ChatBuilder setItalic(boolean italic) { style.setItalic(italic); return this; }
	public ChatBuilder setStrikethrough(boolean strikethrough) { style.setStrikethrough(strikethrough); return this; }
	public ChatBuilder setUnderlined(boolean underlined) { style.setUnderlined(underlined); return this; }
	public ChatBuilder setObfuscated(boolean obfuscated) { style.setObfuscated(obfuscated); return this; }
	public ChatBuilder setClickEvent(ClickEvent.Action action, String value) { style.setChatClickEvent(new ClickEvent(action, value)); return this; }
	public ChatBuilder setHoverEvent(String value) { return setHoverEvent(new ChatComponentText(value)); }
	public ChatBuilder setHoverEvent(IChatComponent value) { return setHoverEvent(Action.SHOW_TEXT, value); }
	public ChatBuilder setHoverEvent(HoverEvent.Action action, IChatComponent value) { style.setChatHoverEvent(new HoverEvent(action, value)); return this; }
	public ChatBuilder setInsertion(String insertion) { style.setInsertion(insertion); return this; }
	public ChatBuilder append(String text) { return append(text, Inheritance.SHALLOW); }
	public ChatBuilder append(String text, Inheritance inheritance) { return new ChatBuilder(text, build(), inheritance); }

	public IChatComponent build() {
		IChatComponent thisComponent = new ChatComponentText(text).setChatStyle(style);
		return parent != null ? parent.appendSibling(thisComponent) : thisComponent;
	}

	public enum Inheritance { DEEP, SHALLOW, NONE; }
}
