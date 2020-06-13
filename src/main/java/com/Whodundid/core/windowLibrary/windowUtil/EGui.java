package com.Whodundid.core.windowLibrary.windowUtil;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.util.chatUtil.EChatUtil;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.renderUtil.GLObject;
import com.google.common.collect.Lists;
import java.net.URI;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.stream.GuiTwitchUserMode;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityList;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.ClientCommandHandler;
import org.lwjgl.input.Keyboard;
import tv.twitch.chat.ChatUserInfo;

//Author: Hunter Bragg

public class EGui extends GLObject {

	public int startXPos, startYPos, startWidth, startHeight;
	public int startX, startY, endX, endY;
	public int width, height;
	public int midX, midY;
	public int mX, mY;
	public int minWidth = 0;
	public int minHeight = 0;
	public int maxWidth = Integer.MAX_VALUE;
	public int maxHeight = Integer.MAX_VALUE;
	
	//---------------
	//Drawing Helpers
	//---------------
	
	public void drawRect() { drawRect(EColors.white.intVal); }
	public void drawRect(EColors color) { drawRect(color.c()); }
	public void drawRect(int color) { drawRect(startX, startY, endX, endY, color); }
	
	public void drawHRect() { drawRect(EColors.white.intVal); }
	public void drawHRect(EColors color) { drawHRect(color.c()); }
	public void drawHRect(int color) { drawHRect(startX, startY, endX, endY, 1, color); }
	
	public void drawRect(EColors color, int offset) { drawRect(color.c(), offset); }
	public void drawRect(int color, int offset) { drawRect(startX + offset, startY + offset, endX - offset, endY - offset, color); }
	
	public void drawHRect(EColors color, int offset) { drawHRect(color.c(), offset); }
	public void drawHRect(int color, int offset) { drawHRect(startX, + offset, startY + offset, endX - offset, endY - offset, color); }
	
	//---------------
	//keyboard checks
	//---------------
	
	public static boolean isCtrlKeyDown() { return Minecraft.isRunningOnMac ? Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220) : Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157); }
	public static boolean isShiftKeyDown() { return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54); }
	public static boolean isAltKeyDown() { return Keyboard.isKeyDown(56) || Keyboard.isKeyDown(184); }
	public static boolean isKeyComboCtrlX(int keyCode) { return keyCode == 45 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown(); }
	public static boolean isKeyComboCtrlV(int keyCode) { return keyCode == 47 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown(); }
	public static boolean isKeyComboCtrlC(int keyCode) { return keyCode == 46 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown(); }
	public static boolean isKeyComboCtrlA(int keyCode) { return keyCode == 30 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown(); }
	
	//----------------
	//Useful Functions
	//----------------
	
	public void sendChatMessage(String msg) { sendChatMessage(msg, true); }
	public void sendChatMessage(String msg, boolean addToChat) {
		if (addToChat) { mc.ingameGUI.getChatGUI().addToSentMessages(msg); }
		if (msg.startsWith("/") && ClientCommandHandler.instance.executeCommand(mc.thePlayer, msg) != 0) { return; }
		EChatUtil.sendLongerChatMessage(msg);
	}
	
	protected void openWebLink(String linkIn) {
		if (linkIn != null && !linkIn.isEmpty()) {
			try {
				URI uri = new URI(linkIn);
				Class<?> oclass = Class.forName("java.awt.Desktop");
				Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
				oclass.getMethod("browse", new Class[] { URI.class }).invoke(object, new Object[] {uri});
			}
			catch (Throwable throwable) { EnhancedMC.error("Couldn\'t open link", throwable); }
		}
	}	
	
	//---------------
	//Component Stuff
	//---------------
	
	protected void handleComponentHover(IChatComponent componentIn, int mX, int mY)  {
		if (componentIn == null) { return; }
		
		HoverEvent hoverevent = componentIn.getChatStyle().getChatHoverEvent();
		if (hoverevent != null && hoverevent.getAction() != null) {
			
			switch (hoverevent.getAction()) {
			case SHOW_ITEM: componentItemHover(componentIn, hoverevent, mX, mY); break;
			case SHOW_ENTITY: componentEntityHover(componentIn, hoverevent, mX, mY); break;
			case SHOW_TEXT: componentTextHover(componentIn, hoverevent, mX, mY); break;
			case SHOW_ACHIEVEMENT: componentAchievementHover(componentIn, hoverevent, mX, mY); break;
			default: EnhancedMC.error("Don\'t know how to handle " + hoverevent); break;
			}
			
			GlStateManager.disableLighting();
		}
	}
	
	protected boolean handleComponentClick(IChatComponent componentIn) {
		if (componentIn == null) { return false; }
		
		ClickEvent clickevent = componentIn.getChatStyle().getChatClickEvent();
		if (clickevent != null && clickevent.getAction() != null) {
			
			switch (clickevent.getAction()) {
			case OPEN_URL: componentURLClick(componentIn, clickevent); break;
			case OPEN_FILE: componentFileClick(componentIn, clickevent); break;
			case SUGGEST_COMMAND: componentSuggestCommandClick(componentIn, clickevent); break;
			case RUN_COMMAND: componentRunCommandClick(componentIn, clickevent); break;
			case TWITCH_USER_INFO: componentTwitchClick(componentIn, clickevent); break;
			default: EnhancedMC.error("Don\'t know how to handle " + clickevent); break;
			}
			
			return true;
		}
		return false;
	}
	
	protected void componentItemHover(IChatComponent componentIn, HoverEvent hoverevent, int mX, int mY) {
		ItemStack itemstack = null;
		
		try {
			NBTBase nbtbase = JsonToNBT.getTagFromJson(hoverevent.getValue().getUnformattedText());
			if (nbtbase instanceof NBTTagCompound) { itemstack = ItemStack.loadItemStackFromNBT((NBTTagCompound)nbtbase); }
		} catch (NBTException var11) { var11.printStackTrace(); }
		
		if (itemstack != null) { renderToolTip(itemstack, mX, mY); }
		else { drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Item!", mX, mY); }
	}
	
	protected void componentEntityHover(IChatComponent componentIn, HoverEvent hoverevent, int mX, int mY) {
		if (this.mc.gameSettings.advancedItemTooltips) {
			try {
				NBTBase nbtbase1 = JsonToNBT.getTagFromJson(hoverevent.getValue().getUnformattedText());
				
				if (nbtbase1 instanceof NBTTagCompound) {
					List<String> list1 = Lists.<String>newArrayList();
					NBTTagCompound nbttagcompound = (NBTTagCompound)nbtbase1;
					list1.add(nbttagcompound.getString("name"));
					
					if (nbttagcompound.hasKey("type", 8)) {
						String s = nbttagcompound.getString("type");
						list1.add("Type: " + s + " (" + EntityList.getIDFromString(s) + ")");
					}
					
					list1.add(nbttagcompound.getString("id"));
					drawHoveringText(list1, mX, mY);
				}
				else { drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Entity!", mX, mY); }
			}
			catch (NBTException var10) { drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Entity!", mX, mY); }
		}
	}
	
	protected void componentTextHover(IChatComponent componentIn, HoverEvent hoverevent, int mX, int mY) {
		String text = hoverevent.getValue().getFormattedText();
		drawHoveringText(NEWLINE_SPLITTER.splitToList(text), mX, mY);
	}
	
	protected void componentAchievementHover(IChatComponent componentIn, HoverEvent hoverevent, int mX, int mY) {
		StatBase statbase = StatList.getOneShotStat(hoverevent.getValue().getUnformattedText());
		
		if (statbase != null) {
			IChatComponent ichatcomponent = statbase.getStatName();
			IChatComponent ichatcomponent1 = new ChatComponentTranslation("stats.tooltip.type." + (statbase.isAchievement() ? "achievement" : "statistic"), new Object[0]);
			ichatcomponent1.getChatStyle().setItalic(Boolean.valueOf(true));
			String s1 = statbase instanceof Achievement ? ((Achievement)statbase).getDescription() : null;
			List<String> list = Lists.newArrayList(new String[] {ichatcomponent.getFormattedText(), ichatcomponent1.getFormattedText()});
			
			if (s1 != null) { list.addAll(mc.fontRendererObj.listFormattedStringToWidth(s1, 150)); }
			
			drawHoveringText(list, mX, mY);
		}
		else { drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid statistic/achievement!", mX, mY); }
	}
	
	protected void componentURLClick(IChatComponent componentIn, ClickEvent event) {}
	
	protected void componentFileClick(IChatComponent componentIn, ClickEvent event) {
		openWebLink(event.getValue());
	}
	
	protected void componentSuggestCommandClick(IChatComponent componentIn, ClickEvent event) {
		System.out.println("suggesting command: " + event.getValue());
	}
	
	protected void componentRunCommandClick(IChatComponent componentIn, ClickEvent event) {
		System.out.println("running command: " + event.getValue());
		sendChatMessage(event.getValue(), false);
	}
	
	protected void componentTwitchClick(IChatComponent componentIn, ClickEvent event) {
		ChatUserInfo chatuserinfo = this.mc.getTwitchStream().func_152926_a(event.getValue());
		if (chatuserinfo != null) {
			mc.displayGuiScreen(new GuiTwitchUserMode(mc.getTwitchStream(), chatuserinfo));
		}
		else { EnhancedMC.error("Tried to handle twitch user but couldn\'t find them!"); }
	}
}
