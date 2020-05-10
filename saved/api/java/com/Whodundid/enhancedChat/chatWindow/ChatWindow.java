package com.Whodundid.enhancedChat.chatWindow;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiScrollBar;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiLinkConfirmationDialogueBox;
import com.Whodundid.core.enhancedGui.guiUtil.EObjectGroup;
import com.Whodundid.core.enhancedGui.objectEvents.EventFocus;
import com.Whodundid.core.enhancedGui.objectEvents.EventMouse;
import com.Whodundid.core.enhancedGui.objectEvents.ObjectEvent;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.FocusType;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.MouseType;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.renderer.renderUtil.IRendererProxy;
import com.Whodundid.core.util.chatUtil.ChatBuilder;
import com.Whodundid.core.util.chatUtil.ChatLineWrapper;
import com.Whodundid.core.util.chatUtil.EChatUtil;
import com.Whodundid.core.util.chatUtil.TimedChatLine;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.enhancedChat.EnhancedChatApp;
import com.Whodundid.enhancedChat.chatOrganizer.ChatFilterList;
import com.Whodundid.enhancedChat.chatOrganizer.ChatOrganizer;
import com.Whodundid.enhancedChat.chatUtil.NameFinder;
import com.Whodundid.enhancedChat.chatWindow.windowObjects.ChatWindowHeader;
import com.Whodundid.enhancedChat.chatWindow.windowObjects.MainRCM;
import com.Whodundid.enhancedChat.chatWindow.windowObjects.PlayerRCM;
import com.Whodundid.enhancedChat.chatWindow.windowObjects.WindowChatField;
import com.google.common.collect.Lists;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.ScaledResolution;
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
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import tv.twitch.chat.ChatUserInfo;

//Jan 13, 2019
//Jan 21, 2019
//Last edited: Jan 22, 2019
//First Added: Dec 17, 2018
//Author: Hunter Bragg

public class ChatWindow extends WindowParent {
	
	public EnhancedChatApp mod = (EnhancedChatApp) RegisteredApps.getApp(AppType.ENHANCEDCHAT);
	public ChatOrganizer chatOrganizer;
	public EArrayList<String> sentWindowHistory = new EArrayList();
	public EArrayList<TimedChatLine> windowHistory = new EArrayList();
	public EArrayList<TimedChatLine> totalLines = new EArrayList();
	protected EArrayList<String> selectedFilters = new EArrayList();
	protected EArrayList<String> enabledFilters = new EArrayList();
	protected ChatWindowHeader header;
	protected WindowChatField entryField;
	protected EGuiScrollBar scrollBar;
	protected String recipientName = "", resizeText = "", clickedPlayer = "";
	protected int scrollPos = 0;
	protected boolean loadChatsOnInit = false;
	MainRCM mainRCM = null;
	PlayerRCM playerRCM = null;
	
	public ChatWindow() { this("none"); }
	public ChatWindow(EArrayList<String> filtersIn) { this(filtersIn.toArray(new String[0])); }
	public ChatWindow(String... filtersIn) {
		super();
		if (mod != null) {
			chatOrganizer = mod.getChatOrganizer();
			
			EArrayList<String> filters = new EArrayList(filtersIn);
			if (filters.contains("none") ) { //this is not right, will address later
				EArrayList<String> userFilters = new EArrayList(mod.defaultFilters);
				if (userFilters.isNotEmpty()) { selectedFilters.addAll(userFilters); }
			}
			else { selectedFilters.addA(filtersIn); }
			
			if (selectedFilters.contains("All")) { totalLines.addAll(chatOrganizer.getFilterHistoryList("All")); }
			else { for (String s : selectedFilters) { totalLines.addAll(chatOrganizer.getFilterHistoryList(s)); } }
		}
		
		aliases.add("chatwindow", "cwindow");
	}
	
	@Override
	public void initGui() {
		setResizeable(true);
		setMinDims(101, 26);
		selectedFilters.setAllowDuplicates(false);
		enabledFilters.setAllowDuplicates(false);
		
		setDimensions(startX, startY, mod.defaultWidth, mod.defaultHeight);
		
		/*
		int xPos = 0, yPos = 0;
		int wid = 0, hei = 0;
		if (mod.useDefaultPos.get()) { xPos = 1; yPos = res.getScaledHeight() - mod.defaultHeight - 2; }
		else { xPos = mod.drawPosX; yPos = mod.drawPosY; }
		if (mod.useDefaultSize.get()) { wid = mod.defaultWidth; hei = mod.defaultHeight; }
		else { wid = mod.drawWidth; hei = mod.drawHeight; }
		*/
		
		resizeLines();
		checkForAll();
	}
	
	private void checkForAll() {
		if (selectedFilters.contains("All")) { enabledFilters.add("All"); }
		else { setChatTabsToEnabledValue(true, selectedFilters); }
	}

	@Override
	public void initObjects() {
		setHeader(new ChatWindowHeader(this, selectedFilters));
		header.setMainColor(mod.defaultHeaderOpacity + 0x00727272);
		entryField = new WindowChatField(this, startX + 2, endY - 13, width - 9, 10);
		entryField.setTextColor(EColors.white.c());
		entryField.setText(resizeText);
		
		scrollBar = new EGuiScrollBar(this, getLineCount(), windowHistory.size(), 3, height - entryField.height - 6, ScreenLocation.left);
		int scrollBarPos = windowHistory.size() - scrollPos;
		scrollBarPos = scrollBarPos < getLineCount() ? getLineCount() : scrollBarPos;
		scrollBar.setScrollBarPos(scrollBarPos);
		
		objectGroup = new EObjectGroup(this);
		objectGroup.addObject(header, entryField, scrollBar);
		header.setObjectGroup(objectGroup);
		entryField.setObjectGroup(objectGroup);
		scrollBar.setObjectGroup(objectGroup);
		
		addObject(entryField, scrollBar);
		
		if (loadChatsOnInit) { header.updateChatTabs(); loadChatsOnInit = false; }
		
		if (getTopParent().getModifyingObject() != this) { entryField.requestFocus(); }
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(startX + (mod.showTimeStamps.get() ? 47 : 1), startY + 1, endX - 1, endY - 15, mod.defaultOpacity); //main chat
		if (mod.showTimeStamps.get()) { drawRect(startX + 1, startY + 1, startX + 47, endY - 15, mod.defaultTimeOpacity); } //timestamp area
		
		drawRect(startX, startY, startX + 1, endY - 14, 0xff000000); //right black bar
		drawRect(endX - 1, startY, endX, endY - 14, 0xff000000); //left black bar
		drawRect(startX, endY - 15, endX, endY - 14, 0xff000000); //bottom black bar
		drawRect(startX + 1, startY, endX, startY + 1, 0xff000000); //top black bar
		
		if (isPinned()) {
			if (mc.currentScreen instanceof IRendererProxy) {
				entryField.setVisible(true);
				drawRect(startX + 1, endY - 14, endX - 1, endY - 1, 0x44000000); //text field background
				drawRect(startX, endY - 1, endX, endY, 0xff000000); //text bottom black bar
				drawRect(startX, endY - 14, startX + 1, endY, 0xff000000);
				drawRect(endX - 1, endY - 14, endX, endY, 0xff000000);
			}
			else { entryField.setVisible(false); }
		}
		else {
			drawRect(startX + 1, endY - 14, endX - 1, endY - 1, 0x44000000); //text field background
			drawRect(startX, endY - 1, endX, endY, 0xff000000); //text bottom black bar
			drawRect(startX, endY - 14, startX + 1, endY, 0xff000000);
			drawRect(endX - 1, endY - 14, endX, endY, 0xff000000);
		}
		
		
		
		/*
		if (getObjectGroup() != null && getObjectGroup().doAnyHaveFocus() || getObjectGroup().isMouseOverAny(mXIn, mYIn)) {
			entryField.setVisible(true);
			drawRect(startX + 1, endY - 14, endX - 1, endY - 1, 0x44000000); //text field background
			drawRect(startX, endY - 1, endX, endY, 0xff000000); //text bottom black bar
			drawRect(startX, endY - 14, startX + 1, endY, 0xff000000);
			drawRect(endX - 1, endY - 14, endX, endY, 0xff000000);
		} else { entryField.setVisible(false); }
		*/
		
		scrollPos = (windowHistory.size() - scrollBar.getScrollPos());
		scrollPos = scrollPos < 0 ? 0 : scrollPos;
		scrollBar.setVisible(windowHistory.size() > getLineCount());
		
		if (scrollBar.isVisible()) {
			drawRect(scrollBar.endX, startY, scrollBar.endX + 1, endY - 15, 0xff000000);
		}
		
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		int scale = res.getScaleFactor();
		GL11.glScissor(
				(startX + 1) * scale,
				(Display.getHeight() - (startY - entryField.height - 5) * scale) - height * scale,
				(width - 2) * scale,
				(height - entryField.height - 5) * scale);
		
		drawChat();
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		
		super.drawObject(mXIn, mYIn);
		
		StorageBox<IChatComponent, TimedChatLine> c = getChatComponent(Mouse.getX(), Mouse.getY());
		if (this.equals(getTopParent().getHighestZObjectUnderMouse())) {
			if (c != null && c.getObject() != null && !this.getObjects().contains(playerRCM)) { handleComponentHover(c.getObject(), mXIn, mYIn); }
		}
		
		//DEBUG DRAW TABS
		
		drawString(this.getActiveFilters(), startX + 3, endY - 31, EColors.white);
		
		//drawRect(startX, startY + 1, endX, startY + 2, 0xffff0000);
		//drawRect(startX, startY + height - 15, endX, startY + height - 16, 0xffff0000);
	}
	
	@Override
	public void onGroupNotification(ObjectEvent e) {
		if (e instanceof EventMouse) {
			if (((EventMouse) e).getMouseType() == MouseType.Pressed) {
				bringToFront();
			}
			if (((EventMouse) e).getMouseType() == MouseType.Released) {
				if (entryField != null) { entryField.requestFocus(); }
			}
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (entryField != null) { entryField.requestFocus(); }
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		super.mousePressed(mX, mY, button);
		handleRCMs(mX, mY, button);
		if (hasFocus()) { clickOnChat(mX, mY, button); }
	}
	
	@Override
	public void mouseReleased(int mX, int mY, int button) {
		entryField.requestFocus();
		super.mouseReleased(mX, mY, button);
	}
	
	public void handleRCMs(int mX, int mY, int button) {
		if (playerRCM != null) {
			if (getObjects().contains(playerRCM) && !playerRCM.isMouseInside(mX, mY)) {
				removeObject(playerRCM);
				playerRCM = null;
			}
		}
		if (mainRCM != null) {
			if (getObjects().contains(mainRCM) && !mainRCM.isMouseInside(mX, mY)) {
				removeObject(mainRCM);
				mainRCM = null;
			}
		}
	}
	
	@Override
	public void mouseScrolled(int change) {
		scroll(change);
		super.mouseScrolled(change);
	}
	
	@Override
	protected void componentSuggestCommandClick(IChatComponent componentIn, ClickEvent event) {
		entryField.setText(event.getValue());
	}
	
	@Override public void onFocusGained(EventFocus eventIn) {
		if (eventIn.getFocusType().equals(FocusType.MousePress)) { mousePressed(eventIn.getMX(), eventIn.getMY(), eventIn.getActionCode()); }
		else { entryField.requestFocus(); }
	}
	
	@Override
	public ChatWindow resize(int xIn, int yIn, ScreenLocation areaIn) {
		if (xIn != 0 || yIn != 0) {
			super.resize(xIn, yIn, areaIn);
			resizeLines();
		}
		return this;
	}
	
	@Override
	public ChatWindow setDimensions(int startXIn, int startYIn, int widthIn, int heightIn) {
		super.setDimensions(startXIn, startYIn, widthIn, heightIn);
		//THIS CAN CAUSE MAJOR LAG IN THIS IMPLEMENTATION
		//resizeLines();
		return this;
	}
	
	@Override
	public ChatWindow resetPosition() {
		setDimensions(startXPos, startYPos, startWidth, startHeight);
		reInitObjects();
		resizeLines();
		return this;
	}
	
	@Override
	public ChatWindow setHeader(EGuiHeader headerIn) {
		if (header != null) { removeObject(header); }
		if (headerIn instanceof ChatWindowHeader) {
			header = (ChatWindowHeader) headerIn;
			if (header != null) { header.updateButtonVisibility(); }
			addObject(headerIn);
		}
		return this;
	}
	
	public ChatWindow updateVisual() {
		checkForAll();
		resizeLines();
		return this;
	}
	
	protected void resizeLines() {
		windowHistory.clear();
		int i = MathHelper.floor_float(getChatWidth() / mc.gameSettings.chatScale);
		for (int q = totalLines.size() - 1; q >= 0; q--) {
			TimedChatLine l = totalLines.get(q);
			if (l != null && l.getChatComponent() != null) {
				String line = l.getChatComponent().getFormattedText();
				if (line.length() > mod.msgLengthLimit) {
					TimedChatLine shortenedLine = new TimedChatLine(l.getUpdatedCounter(), ChatBuilder.of(line.substring(0, mod.msgLengthLimit) + ", ...").build(), l.getChatLineID()).setTimeStamp(l.getTimeStamp());
					List<IChatComponent> list = ChatLineWrapper.makeList(shortenedLine.getChatComponent(), i, false, false);
					for (IChatComponent c : list) {
						IChatComponent cd = ChatBuilder.of(c.getFormattedText())
								.setHoverEvent(HoverEvent.Action.SHOW_TEXT, ChatBuilder.of(EnumChatFormatting.YELLOW +
										"This text line has been intentionally\n" + EnumChatFormatting.YELLOW + "shortened due to its extreme length!")
								.build()).build();
						TimedChatLine newLine = new TimedChatLine(l.getUpdatedCounter(), cd, l.getChatLineID()).setTimeStamp(l.getTimeStamp());
						windowHistory.add(0, newLine);
					}
				}
				else {
					List<IChatComponent> list = ChatLineWrapper.makeList(l.getChatComponent(), i, false, false);
					for (IChatComponent c : list) {
						TimedChatLine newLine = new TimedChatLine(l.getUpdatedCounter(), c, l.getChatLineID()).setTimeStamp(l.getTimeStamp());
						windowHistory.add(0, newLine);
					}
				}
			}
		}
	}
	
	protected void drawChat() {
		if (windowHistory.size() > 0) {
			String oldID = "";
			if (windowHistory.size() < getLineCount()) {
				int q = 0;
				for (int i = windowHistory.size() - 1; i >= 0; i--) {
					TimedChatLine line = windowHistory.get(i + scrollPos);
					if (line.getTimeStamp() != oldID) {
						oldID = line.getTimeStamp();
						if (mod.showTimeStamps.get()) { drawString(line.getTimeStamp(), startX + 4, startY + 3 + q * 9, 0x888888); }
					}
					drawStringWithShadow(line.getChatComponent().getFormattedText(), startX + (mod.showTimeStamps.get() ? 50 : 3), startY + 3 + q * 9, 0xffffff);
					q++;
				}
			} else {
				boolean inBlock = false;
				for (int i = 0, pos = scrollPos; i + scrollPos < windowHistory.size() && i < getLineCount() + 1; i++, pos += 1) {
					TimedChatLine line = windowHistory.get(pos);
					int xPos = startX + (windowHistory.size() == getLineCount() ? 4 : 6);
					
					if (line.getTimeStamp() != oldID) {
						oldID = line.getTimeStamp();
						if (rangeCheck(pos + 1) && getLine(pos + 1) != null && getTStamp(pos + 1) == oldID) { inBlock = true; }
						else { drawString(line.getTimeStamp(), xPos, endY - 24 - i * 9, 0x888888); }
					}
					else {
						if (inBlock) {
							if (rangeCheck(pos + 1)) {
								if (getLine(pos + 1) != null && getTStamp(pos + 1) != oldID) {
									drawString(line.getTimeStamp(), xPos, endY - 24 - i * 9, 0x888888); inBlock = false;
								}
							} else {
								drawString(line.getTimeStamp(), xPos, endY - 24 - i * 9, 0x888888); inBlock = false;
							}
						}
					}
					
					if (oldID.isEmpty()) { oldID = line.getTimeStamp(); }
					
					drawStringWithShadow(line.getChatComponent().getFormattedText(), startX + (mod.showTimeStamps.get() ? 50 : 6), endY - 24 - i * 9, 0xffffff);
				}
			}
		}
	}
	
	protected void clickOnChat(int mXIn, int mYIn, int button) {
		try {
			StorageBox<IChatComponent, TimedChatLine> fullComponent = getChatComponent(Mouse.getX(), Mouse.getY());
			if (fullComponent != null) {
				IChatComponent comp = fullComponent.getObject();
				
				if (comp != null) {
					if (button == 0) {
						if (handleComponentClick(comp)) {
							entryField.requestFocus();
							return;
						}
			        }
					else if (button == 1) {
						clickedPlayer = NameFinder.getNameFromChat(fullComponent);
						openPlayerRCM(mXIn, mYIn);
					}
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public void scroll(int change) {
		if (!isShiftKeyDown()) { change *= 7; }
		scrollPos += change;
		int i = windowHistory.size();
		if (scrollPos <= 0) { scrollPos = 0; }
		int barChange = windowHistory.size() - scrollPos;
		barChange = barChange < getLineCount() ? getLineCount() : barChange;
		scrollBar.setScrollBarPos(barChange);
	}
	
	public void clearChatMessages() {
		windowHistory.clear();
		totalLines.clear();
	}
	
	public void addChatLine(TimedChatLine lineIn) {
		int i = MathHelper.floor_float(getChatWidth() / mc.gameSettings.chatScale);
		List<IChatComponent> list = ChatLineWrapper.makeList(lineIn.getChatComponent(), i, false, false);
		boolean flag = scrollPos > 0;
		for (IChatComponent ichatcomponent : list) {
			if (flag && scrollPos > 0) { scroll(0); }
			TimedChatLine l = new TimedChatLine(EnhancedMC.updateCounter, ichatcomponent, lineIn.getChatLineID()).setTimeStamp(lineIn.getTimeStamp());
			windowHistory.add(0, l);
			scrollBar.setHighVal(windowHistory.size());
			if (scrollPos == 0) { scrollBar.setScrollBarPos(windowHistory.size()); }
		}
		totalLines.add(0, lineIn);
	}
	
	/** Sets the scroll position to 0, the oldest chat message. */
	public void resetScroll() { scrollPos = 0; scrollBar.setScrollBarPos(getLineCount()); }
	
	public void deleteChatLine(int lineIdIn) {
		Iterator<TimedChatLine> iterator = windowHistory.iterator();
		while (iterator.hasNext()) {
			TimedChatLine chatline = iterator.next();
			if (chatline.getChatLineID() == lineIdIn) {
				iterator.remove();
			}
		}
		iterator = totalLines.iterator();
		while (iterator.hasNext()) {
			TimedChatLine chatline1 = iterator.next();
			if (chatline1.getChatLineID() == lineIdIn) {
				iterator.remove();
				break;
			}
		}
	}
	
	protected StorageBox<IChatComponent, TimedChatLine> getChatComponent(int mXIn, int mYIn) {
		ScaledResolution res = new ScaledResolution(mc);
		int i = res.getScaleFactor();
		float f = mc.gameSettings.chatScale;
		int j = mXIn / i - startX - (mod.showTimeStamps.get() ? 50 : 3); //timestamp bar width
		int k = (Display.getHeight() - mYIn) / i - startY;
		k = windowHistory.size() > getLineCount() ? (height - k - entryField.height - 7) : k - 7;
		
		k = (k > height - entryField.height - 7) ? -1 : k;
		j = MathHelper.floor_float(j / f);
		k = MathHelper.floor_float(k / f);
		
		if (j >= 0 && k >= 0) {
			int l = Math.min(getLineCount(), windowHistory.size());
			if (j <= endX - 1 && k < mc.fontRendererObj.FONT_HEIGHT * l + l) {
				int i1 = k / mc.fontRendererObj.FONT_HEIGHT + scrollPos;
				i1 = windowHistory.size() <= getLineCount() ? windowHistory.size() - 1 - i1 : i1;
				if (i1 >= 0 && i1 < windowHistory.size()) {
					TimedChatLine chatLine = windowHistory.get(i1);
					int j1 = 0;
					for (IChatComponent ichatcomponent : chatLine.getChatComponent()) {
						if (ichatcomponent instanceof ChatComponentText) {
							j1 += mc.fontRendererObj.getStringWidth(GuiUtilRenderComponents.func_178909_a(((ChatComponentText) ichatcomponent).getChatComponentText_TextValue(), false));
							if (j1 > j) {
								//System.out.println(ichatcomponent);
								return new StorageBox(ichatcomponent, chatLine);
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	public ChatWindow setChatFilters(String... typesIn) { return setChatFilters(new EArrayList<String>(typesIn)); }
	public ChatWindow setChatFilters(EArrayList<String> typesIn) {
		selectedFilters.clear();
		for (String s : typesIn) {
			if (s.equals("All")) { selectedFilters.add(s); }
			else if (ChatOrganizer.getFilterList(s) != null) { selectedFilters.add(s); } //check if the filter even exists before adding it
		}
		if (isInit()) { header.updateChatTabs(); }
		else { loadChatsOnInit = true; }
		return this;
	}
	
	/** Returns the chatWindowHeader instance in this chatWindow. */
	@Override public ChatWindowHeader getHeader() { return header; }
	
	/** Returns true if the specified chat is currently enabled in this chat window. */
	public boolean isFilterEnabled(String filterIn) { return enabledFilters.contains(filterIn); }
	
	/** Gets a list of the currently enabled chats in this window. */
	public EArrayList<String> getActiveFilters() { return enabledFilters; }
	
	/** Gets a list of the currently enabled chats in this window. */
	public EArrayList<String> getAllDisabledChatTypes() {
		return new EArrayList<String>(selectedFilters.stream().filter(s -> enabledFilters.notContains(s)).collect(Collectors.toList()));
	}
	
	public ChatWindow setChatTabsToEnabledValue(boolean val, String... filtersIn) { return setChatTabsToEnabledValue(val, new EArrayList<String>(filtersIn)); }
	public ChatWindow setChatTabsToEnabledValue(boolean val, EArrayList<String> typesIn) {
		if (val) { enabledFilters.addAll(typesIn); }
		else {
			for (String s : typesIn) { if (enabledFilters.contains(s)) { enabledFilters.remove(s); } }
		}
		return this;
	}
	
	public void updateEnabledChatTypes() {
		if (isInit() && mod != null && chatOrganizer != null) {
			totalLines.clear();
			windowHistory.clear();
			EArrayList<String> enabledChats = getActiveFilters();
			if (enabledChats.contains("All")) { totalLines.addAll(chatOrganizer.getFilterHistoryList("All")); }
			else {
				for (String s : enabledChats) {
					totalLines.addAll(chatOrganizer.getFilterHistoryList(s));
				}
				sortTotalLinesByTime();
			}
			resizeLines();
			scrollBar.setHighVal(windowHistory.size());
			scrollPos = scrollBar.getHighVal();
			scrollBar.setScrollBarPos(scrollPos);
		}
	}
	
	/** Iterates through all chat lines in this chatWindow and sorts each by their timestamp with the most recent being at the bottom. */
	protected void sortTotalLinesByTime() {
		Collections.sort(totalLines);
		Collections.reverse(totalLines);
	}
	
	public void openPlayerRCM(int mXIn, int mYIn) {
		if (clickedPlayer != null && !clickedPlayer.isEmpty()) {
			if (!getObjects().contains(playerRCM)) {
				addObject(playerRCM = new PlayerRCM(this, clickedPlayer));
			}
		}
	}
	
	public void openMainRCM(int mXIn, int mYIn) {
		if (!getObjects().contains(mainRCM)) {
			addObject(mainRCM = new MainRCM(this));
		}
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object != null && object.getSelectedObject() instanceof ChatFilterList) {
			EArrayList<String> chats = new EArrayList(getAllFilters());
			chats.add(((ChatFilterList) object.getSelectedObject()).getFilterName());
			setChatFilters(chats);
		}
	}
	
	@Override
	protected boolean handleComponentClick(IChatComponent componentIn) {
        if (componentIn == null) { return false; }
		ClickEvent clickevent = componentIn.getChatStyle().getChatClickEvent();
		if (clickevent != null && clickevent.getAction() != null) {
			if (clickevent.getAction() == ClickEvent.Action.OPEN_URL) {
			    if (!mc.gameSettings.chatLinks) { return false; }
			    try {
			        URI uri = new URI(clickevent.getValue());
			        String s = uri.getScheme();
			        
			        if (s == null) { throw new URISyntaxException(clickevent.getValue(), "Missing protocol"); }
			        
			        if (!PROTOCOLS.contains(s.toLowerCase())) {
			            throw new URISyntaxException(clickevent.getValue(), "Unsupported protocol: " + s.toLowerCase());
			        }
			        
			        if (mod.warnOnLinks.get()) { EnhancedMC.displayWindow(new EGuiLinkConfirmationDialogueBox(this, clickevent.getValue())); }
			        else { openWebLink(clickevent.getValue()); }
			    }
			    catch (URISyntaxException urisyntaxexception) {
			        EnhancedMC.error("Can\'t open url for " + clickevent, urisyntaxexception);
			    }
			}
			else if (clickevent.getAction() == ClickEvent.Action.OPEN_FILE) {
			    openWebLink(clickevent.getValue());
			}
			else if (clickevent.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
				System.out.println("suggesting command: " + clickevent.getValue());
			    //setText(clickevent.getValue(), true);
			}
			else if (clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
				System.out.println("running command: " + clickevent.getValue());
			    sendChatMessage(clickevent.getValue(), false);
			}
			else if (clickevent.getAction() == ClickEvent.Action.TWITCH_USER_INFO) {
			    ChatUserInfo chatuserinfo = this.mc.getTwitchStream().func_152926_a(clickevent.getValue());
			    if (chatuserinfo != null) {
			        mc.displayGuiScreen(new GuiTwitchUserMode(mc.getTwitchStream(), chatuserinfo));
			    }
			    else { EnhancedMC.error("Tried to handle twitch user but couldn\'t find them!"); }
			}
			else { EnhancedMC.error("Don\'t know how to handle " + clickevent); }

			return true;
		}
		return false;
    }
	
	@Override
	protected void handleComponentHover(IChatComponent componentIn, int mX, int mY)  {
		if (componentIn != null) {
			if (componentIn.getChatStyle().getChatHoverEvent() != null) {
	            HoverEvent hoverevent = componentIn.getChatStyle().getChatHoverEvent();
	            
	            if (hoverevent.getAction() == HoverEvent.Action.SHOW_ITEM) {
	                ItemStack itemstack = null;
	                
	                try {
	                    NBTBase nbtbase = JsonToNBT.getTagFromJson(hoverevent.getValue().getUnformattedText());
	                    if (nbtbase instanceof NBTTagCompound) { itemstack = ItemStack.loadItemStackFromNBT((NBTTagCompound)nbtbase); }
	                } catch (NBTException var11) { var11.printStackTrace(); }

	                if (itemstack != null) { renderToolTip(itemstack, mX, mY); }
	                else {
	                    drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Item!", mX, mY);
	                }
	            }
	            else if (hoverevent.getAction() == HoverEvent.Action.SHOW_ENTITY) {
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
	                        else {
	                            drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Entity!", mX, mY);
	                        }
	                    }
	                    catch (NBTException var10) {
	                        drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Entity!", mX, mY);
	                    }
	                }
	            }
	            else if (hoverevent.getAction() == HoverEvent.Action.SHOW_TEXT) {
	            	String text = hoverevent.getValue().getFormattedText();
	            	ClickEvent clickEvent = componentIn.getChatStyle().getChatClickEvent();
	            	if (hoverevent.getValue().getUnformattedText().contains("Hypixel Level: ")) {
						String playerName = NameFinder.isolateNameFromChat(EChatUtil.removeFormattingCodes(hoverevent.getValue().getUnformattedText()));
						if (playerName != null) { text += "\n\nRight click for more options.."; }
					}
	            	if (mod.showMoreChatInfo.get()) {
	            		ClickEvent clickevent = componentIn.getChatStyle().getChatClickEvent();
		            	if (clickevent != null && clickevent.getAction() != null) {
		            		if (clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) { text += "\nRuns command: " + clickevent.getValue(); }
		            		if (clickEvent.getAction() == ClickEvent.Action.OPEN_URL) {
		            			text = text.length() > 60 ? text.substring(0, 57) + "..." : text;
		            		}
		            	}
	            	}
	            	drawHoveringText(NEWLINE_SPLITTER.splitToList(text), mX, mY);
	            }
	            else if (hoverevent.getAction() == HoverEvent.Action.SHOW_ACHIEVEMENT) {
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

	            GlStateManager.disableLighting();
	        }
			else if (mod.showMoreChatInfo.get()) {
	        	ClickEvent clickEvent = componentIn.getChatStyle().getChatClickEvent();
	        	if (clickEvent != null && clickEvent.getAction() != null) {
	        		if (clickEvent.getAction() == ClickEvent.Action.OPEN_URL) {
	        			String text = clickEvent.getValue();
	        			text = text.length() > 60 ? text.substring(0, 57) + "..." : text;
	        			drawHoveringText(NEWLINE_SPLITTER.splitToList("§eOpens webpage: §r\n" + text), mX, mY);
	        		}
	        		else if (clickEvent.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
	        			String text = clickEvent.getValue();
	        			text = text.length() > 60 ? text.substring(0, 57) + "..." : text;
	        			drawHoveringText(NEWLINE_SPLITTER.splitToList("§eSuggests command: §r\n" + text), mX, mY);
	        		}
	        		else if (clickEvent.getAction() == ClickEvent.Action.RUN_COMMAND) {
	        			String text = clickEvent.getValue();
	        			text = text.length() > 60 ? text.substring(0, 57) + "..." : text;
	        			drawHoveringText(NEWLINE_SPLITTER.splitToList("§eRuns command: §r\n" + text), mX, mY);
	        		}
	        	}
	        }
		}
    }
	
	//public 
	
	private boolean rangeCheck(int pos) { return windowHistory != null && pos >= 0 && pos < windowHistory.size(); }
	private TimedChatLine getLine(int pos) { return rangeCheck(pos) ? windowHistory.get(pos) : null; }
	private String getTStamp(int pos) { return rangeCheck(pos) ? windowHistory.get(pos).getTimeStamp() : null; }
	
	public ChatWindow scrollToBottom() { if (scrollBar != null) { scrollBar.setScrollBarPos(windowHistory.size()); } return this; }
	public ChatWindow scrollToTop() { if (scrollBar != null) { scrollBar.setScrollBarPos(0); } return this; }
	
	public ChatWindow updateInitText(String textIn) { resizeText = textIn; return this; }
	public EArrayList<String> getAllFilters() { return new EArrayList(selectedFilters); }
	public int getChatWidth() { return width - (mod.showTimeStamps.get() ? 52 : 10); }
	public int getChatHeight() { return height - 15; }
	public int getLineCount() { return getChatHeight() / 9; }
	public WindowChatField getEntryField() { return entryField; }
	public String getRecipientName() { return recipientName; }
	public ChatWindow setRecipientName(String nameIn) { recipientName = nameIn; return this; }
	public EGuiScrollBar getScrollBar() { return scrollBar; }
	public EArrayList<TimedChatLine> getWindowHistory() { return windowHistory; }
}
