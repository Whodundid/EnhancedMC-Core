package com.Whodundid.enhancedChat.chatWindow.windowObjects;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiSelectionList;
import com.Whodundid.core.enhancedGui.objectEvents.EventMouse;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.MouseType;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.ObjectModifyType;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedTopParent;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.enhancedChat.chatOrganizer.ChatFilterList;
import com.Whodundid.enhancedChat.chatOrganizer.ChatOrganizer;
import com.Whodundid.enhancedChat.chatWindow.ChatWindow;

public class ChatHeaderNewTabButton extends ChatHeaderTab {

	ChatWindow parentWindow;
	ChatWindowHeader parentHeader;
	
	public ChatHeaderNewTabButton(ChatWindowHeader parentHeaderIn) {
		super(parentHeaderIn);
		init(parentHeaderIn, 0, 0, 1, 1);
		parentHeader = parentHeaderIn;
		parentWindow = parentHeaderIn.getParentChatWindow();
		setDrawTextures(false);
		setDisplayString("+");
		addClose = false;
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		if (isMouseInside(mXIn, mYIn)) {
			drawRect(startX, startY, startX + 1, endY, 0xaab2b2b2); //left
			drawRect(startX + 1, startY, endX - 1, startY + 1, 0xaab2b2b2); //top
			drawRect(endX - 1, startY, endX, endY, 0xaab2b2b2); //right
			drawRect(startX + 1, endY - 1, endX - 1, endY, 0xaab2b2b2); //bottom
		} else {
			drawRect(startX, startY, startX + 1, endY, 0xaa909090); //left
			drawRect(startX + 1, startY, endX - 1, startY + 1, 0xaa909090); //top
			drawRect(endX - 1, startY, endX, endY, 0xaa404040); //right
			drawRect(startX + 1, endY - 1, endX - 1, endY, 0xaa404040); //bottom
		}
		drawRect(startX + 1, startY + 1, endX - 1, endY - 1, 0xaa757575); //middle background
		
		drawStringWithShadow(getDisplayString(), midX - mc.fontRendererObj.getStringWidth(getDisplayString()) / 2, startY + 4, drawColor);
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		if (button == 0) {
			//playPressSound();
			EArrayList<String> activeChatFilters = parentHeader.getAllChatFilters();
			EArrayList<ChatFilterList> selectableFilters = new EArrayList();
			if (!activeChatFilters.contains("All")) { selectableFilters.add(new ChatFilterList("All")); }
			for (ChatFilterList l : ChatOrganizer.filters) { if (!activeChatFilters.contains(l.getFilterName())) { selectableFilters.add(l); } }
			openSelectionGui(selectableFilters);
		}
		else {
			parentHeader.mousePressed(mX, mY, button);
		}
	}
	
	@Override
	public void mouseReleased(int mX, int mY, int button) {
		if (eventHandler != null) { eventHandler.processEvent(new EventMouse(this, mX, mY, button, MouseType.Released)); }
		IEnhancedTopParent par = getTopParent();
		if (par.getModifyType() == ObjectModifyType.Resize) { par.clearModifyingObject(); }
		if (par.getDefaultFocusObject() != null) { par.getDefaultFocusObject().requestFocus(); }
	}
	
	private void openSelectionGui(EArrayList<ChatFilterList> filtersIn) {
		try {
			EArrayList<String> displayValues = new EArrayList();
			for (ChatFilterList l : filtersIn) {
				displayValues.add(l.getMCColor() + l.getFilterName());
			}
			
			EGuiSelectionList tabSelectionList = new EGuiSelectionList(getTopParent(), StorageBoxHolder.createBox(displayValues, filtersIn));
			tabSelectionList.setHeaderString("Available Chat Filters..");
			tabSelectionList.setActionReciever(parentWindow);
			
			EnhancedMC.displayWindow(tabSelectionList);
		}
		catch (Exception e) { e.printStackTrace(); }
	}
}
