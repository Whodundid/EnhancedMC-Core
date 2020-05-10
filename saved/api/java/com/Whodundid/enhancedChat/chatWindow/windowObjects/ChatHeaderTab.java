package com.Whodundid.enhancedChat.chatWindow.windowObjects;

import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.ObjectModifyType;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedTopParent;
import com.Whodundid.core.util.mathUtil.NumberUtil;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.enhancedChat.EnhancedChatApp;
import com.Whodundid.enhancedChat.chatWindow.ChatWindow;

//Jan 11, 2019
//Jan 24, 2019
//Last edited: Mar 22, 2019
//First Added: Jan 11, 2019
//Author: Hunter Bragg

public class ChatHeaderTab extends EGuiButton {

	ChatWindow parentWindow;
	ChatWindowHeader parentHeader;
	protected String filter = "";
	protected boolean addClose = true;
	protected boolean pressed = false;
	protected boolean windowCreated = false;
	protected boolean addedToOther = false;
	protected String recipientName = "";
	protected int drawColor = 0xffb2b2b2;
	public static int defaultWidth = 55;
	protected StorageBox<Integer, Integer> mousePos = new StorageBox();
	protected StorageBox<Integer, Integer> startPos = new StorageBox();
	EGuiButton closeButton = null;
	
	protected ChatHeaderTab(ChatWindowHeader parentHeaderIn) {
		super(parentHeaderIn);
		parentHeader = parentHeaderIn;
		parentWindow = parentHeader.getParentChatWindow();
	}
	
	public ChatHeaderTab(ChatWindowHeader parentHeaderIn, String filterIn) { this(parentHeaderIn, filterIn, defaultWidth); }
	public ChatHeaderTab(ChatWindowHeader parentHeaderIn, String filterIn, int widthIn) {
		super(parentHeaderIn);
		init(parentHeaderIn, 0, 0, widthIn, parentHeaderIn.getDimensions().height - 3);
		parentHeader = parentHeaderIn;
		parentWindow = parentHeader.getParentChatWindow();
		filter = filterIn;
		setDrawTextures(false);
		setDrawString(false);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		if (pressed) { checkForMove(mXIn, mYIn); }
		if (closeButton != null) { closeButton.setVisible(width > 17 && isMouseInside(mXIn, mYIn)); }
		boolean isTypeEnabled = parentWindow.isFilterEnabled(getFilter());
		drawColor = isTypeEnabled ? 0xffffd800 : 0xffb2b2b2;
		
		String drawString = "";
		
		try {
			String actual = filter;
			for (int i = 0; i < actual.length(); i++) {
				if (mc.fontRendererObj.getStringWidth(drawString) <= width - 13) {
					drawString += actual.charAt(i);
					continue;
				}
				else if (drawString.length() > 1) {
					drawString = drawString.substring(0, drawString.length() - 1);
					drawString += "..";
					break;
				}
				else {
					break;
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
		
		int borderColor = isTypeEnabled ? 0xff000000 + drawColor - 0x00404000 : 0xaab2b2b2;
		//System.out.println(type.getChatType() + " " + borderColor);
		if (isMouseInside(mXIn, mYIn)) {
			if (drawString.length() == 0) {
				drawRect(startX, startY, startX + 1, endY, isTypeEnabled ? borderColor + 0x00080800 : 0xaab2b2b2);
				drawRect(startX + 1, startY, endX - 1, startY + 1, isTypeEnabled ? borderColor + 0x00080800 : 0xaab2b2b2);
				drawRect(endX - 1, startY, endX, endY, isTypeEnabled ? borderColor + 0x00080800 : 0xaab2b2b2);
				drawRect(startX + 1, endY - 1, endX - 1, endY, isTypeEnabled ? borderColor + 0x00080800 : 0xaab2b2b2);
			}
			else {
				drawRect(startX, startY, startX + 1, endY, 0xaab2b2b2); //left
				drawRect(startX + 1, startY, endX - 1, startY + 1, 0xaab2b2b2); //top
				drawRect(endX - 1, startY, endX, endY, 0xaab2b2b2); //right
				drawRect(startX + 1, endY - 1, endX - 1, endY, 0xaab2b2b2); //bottom
			}
		} else {
			if (drawString.length() == 0) {
				drawRect(startX, startY, startX + 1, endY, isTypeEnabled ? borderColor : 0xaa909090);
				drawRect(startX + 1, startY, endX - 1, startY + 1, isTypeEnabled ? borderColor : 0xaa909090);
				drawRect(endX - 1, startY, endX, endY, isTypeEnabled ? borderColor - 0x00252500 : 0xaa404040);
				drawRect(startX + 1, endY - 1, endX - 1, endY, isTypeEnabled ? borderColor - 0x00252500 : 0xaa404040);
			}
			else {
				drawRect(startX, startY, startX + 1, endY, 0xaa909090); //left
				drawRect(startX + 1, startY, endX - 1, startY + 1, 0xaa909090); //top
				drawRect(endX - 1, startY, endX, endY, 0xaa404040); //right
				drawRect(startX + 1, endY - 1, endX - 1, endY, 0xaa404040); //bottom
			}
		}
		drawRect(startX + 1, startY + 1, endX - 1, endY - 1, isTypeEnabled ? ((drawString.length() > 0) ? 0xaa757575 : borderColor + 0x00222200) : 0xaa757575); //middle background
		
		drawStringWithShadow(drawString, startX + 4, startY + 4, drawColor);
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		
		if (enabled && checkDraw()) {
			pressedButton = button;
			if (runActionOnPress) { onPress(); }
			else if (button == 0) {
				//playPressSound();
				performAction(null, null);
			}
		}
		
		if (button == 0) {
			IEnhancedTopParent topParent = getTopParent();
			topParent.setModifyMousePos(mX, mY);
			ChatWindow window = parentHeader.getParentChatWindow();
			EArrayList<String> chats = window.getAllFilters();
			if (chats.size() > 1) {
				toggleEnabledValue();
				mousePos.setValues(mX, mY);
				startPos.setValues(startX, startY);
				topParent.setModifyingObject(this, ObjectModifyType.Move);
				pressed = true;
			}
			else if (chats.size() == 1) {
				pressed = true;
				if (getParent().getObjectGroup() != null && getParent().getObjectGroup().getGroupParent() != null) {
					topParent.setModifyingObject(getParent().getObjectGroup().getGroupParent(), ObjectModifyType.Move);
				}
			}
			else {
				if (getParent().getObjectGroup() != null && getParent().getObjectGroup().getGroupParent() != null) {
					topParent.setModifyingObject(getParent().getObjectGroup().getGroupParent(), ObjectModifyType.Move);
				}
			}
		} else {
			parentHeader.mousePressed(mX, mY, button);
		}
    }
	
	@Override
	public void mouseReleased(int mX, int mY, int button) {
		getTopParent().clearModifyingObject();
		if (parentHeader.getAllChatTabs().size() > 1) {
			pressed = false;
			if (!checkMouseDrag(mX, mY) && startPos != null && startPos.getObject() != null && startPos.getValue() != null) {
				setPosition(startPos.getObject(), startPos.getValue());
			}
		}
		super.mouseReleased(mX, mY, button);
	}
	
	@Override
	public void onAdded() {
		if (addClose) {
			addCloseButton();
		}
	}
	
	protected void addCloseButton() {
		closeButton = new EGuiButton(this, width >= 9 ? (endX - 9) : midX + (width - 7), startY + 3, 7, 7, "x") {
			{
				setDrawTextures(false);
				setRunActionOnPress(true);
				setDisplayStringHoverColor(0xff6060);
			}
			@Override
			public void drawObject(int mXIn, int mYIn) {
				super.drawObject(mXIn, mYIn);
			}
			@Override
			public void onPress() {
				playPressSound();
				ChatWindow window = parentHeader.getParentChatWindow();
				EArrayList<String> chats = window.getAllFilters();
				if (chats.size() > 1) {
					if (chats.contains(filter)) { chats.remove(filter); }
					window.setChatFilters(chats);
					if (filter.equals("All") && parentWindow.isFilterEnabled(filter)) {
						parentWindow.setChatTabsToEnabledValue(true, new EArrayList<String>(parentWindow.getAllFilters()));
					}
					else if (parentWindow.getAllFilters().contains("All")) {
						boolean none = true;
						for (String s : chats) { if (parentWindow.isFilterEnabled(s)) { none = false; break; } }
						if (none) { window.updateVisual(); }
					}
				} else { window.close(); }
			}
		};
		addObject(closeButton);
	}
	
	protected void checkForMove(int mX, int mY) {
		//System.out.println("Window created : " + windowCreated + " ; " + addedToOther);
		if (!getTopParent().isMouseInsideHeader(mX, mY)) {
			if (!windowCreated) {
				if (checkMouseDrag(mX, mY)) {
					ChatWindow newWindow = new ChatWindow(filter);
					newWindow.setDimensions(mX - 22, mY + 10, 280, 152);
					if (EnhancedChatApp.makeNewWindowsPinned.get()) { newWindow.setPinned(true); }
					getTopParent().addObject(newWindow);
					
					IEnhancedTopParent topParent = getTopParent();
					topParent.setModifyingObject(newWindow, ObjectModifyType.MoveAlreadyClicked);
					topParent.setModifyMousePos(mX, mY);
					
					ChatWindow oldWindow = parentHeader.getParentChatWindow();
					EArrayList<String> oldChatTypes = oldWindow.getAllFilters();
					if (oldChatTypes.contains(filter)) {
						EArrayList<String> newChatTypes = oldChatTypes;
						newChatTypes.remove(filter);
						oldWindow.setChatFilters(newChatTypes);
						oldWindow.updateVisual();
					}
					windowCreated = true;
				}
			}
		}
		else {
			if (!addedToOther) {
				EArrayList<IEnhancedGuiObject> underMouse = getTopParent().getAllChildrenUnderMouse();
				
				for (IEnhancedGuiObject o : underMouse) {
					if (o instanceof ChatWindowHeader) {
						if (parentHeader != o) {
							ChatWindowHeader h = (ChatWindowHeader) o;
							ChatWindow window = h.getParentChatWindow();
							
							//System.out.println(windowCreated + " " + window);
							
							ChatWindow oldWindow = parentHeader.getParentChatWindow();
							EArrayList<String> oldChatTypes = oldWindow.getAllFilters();
							if (oldChatTypes.contains(filter)) {
								EArrayList<String> newChatTypes = oldChatTypes;
								newChatTypes.remove(filter);
								if (newChatTypes.isNotEmpty()) {
									oldWindow.setChatFilters(newChatTypes);
									oldWindow.updateVisual();
								}
								else {
									oldWindow.close();
								}
							}
							
							EArrayList<String> filters = window.getActiveFilters();
							window.setChatFilters(new EArrayList());
							filters.add(filter);
							window.setChatFilters(filters);
							addedToOther = true;
							break;
						}
					}
				}
			}
		}
	}
	
	protected boolean checkMouseDrag(int mX, int mY) {
		StorageBox<Integer, Integer> testBox = new StorageBox(mX, mY);
		int dist = NumberUtil.getDistance(mousePos, testBox);
		//System.out.println("dist: " + dist);
		return dist > 20;
	}
	
	public String getFilter() { return filter; }
	public String getRecipient() { return recipientName; }
	public ChatHeaderTab setRecipientName(String nameIn) { recipientName = nameIn; return this; }
	
	protected void toggleEnabledValue() {
		parentWindow.setChatTabsToEnabledValue(!parentWindow.isFilterEnabled(filter), filter);
		if (filter.equals("All")) {
			boolean isEnabled = parentWindow.isFilterEnabled(filter);
			EArrayList<String> chats = new EArrayList<String>(parentWindow.getAllFilters());
			if (chats.contains(filter)) { chats.remove(filter); }
			parentWindow.setChatTabsToEnabledValue(!isEnabled, chats);
		}
		else {
			if (parentWindow.getAllFilters().contains("All")) {
				boolean none = true;
				for (String s : parentWindow.getAllFilters()) { if (parentWindow.isFilterEnabled(s)) { none = false; break; } }
				if (none) { parentWindow.setChatTabsToEnabledValue(true, "All"); }
				else { parentWindow.setChatTabsToEnabledValue(false, "All"); }
			}
		}
		parentHeader.getParentChatWindow().updateEnabledChatTypes();
	}
}
