package com.Whodundid.enhancedChat.guis;

import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.EGuiTextArea;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiContainer;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.enhancedChat.chatOrganizer.ChatFilterList;
import com.Whodundid.enhancedChat.chatOrganizer.ChatOrganizer;
import net.minecraft.util.EnumChatFormatting;

public class ChatOrganizerGui extends WindowParent {

	EGuiLabel titleLabel;
	EGuiTextArea filterListDisplay;
	EArrayList<ChatFilterList> filters = new EArrayList();
	EGuiButton previous, next;
	EGuiButton addFilter, removeFilter;
	EGuiButton reset, help, back;
	ChatFilterList currentList;
	EGuiContainer buttonContainer;
	int filterButtonPos = 0;
	
	public ChatOrganizerGui() {
		super();
		aliases.add("chatorganizer");
	}
	
	@Override
	public void initGui() {
		setObjectName("Chat Organizer Settings");
		defaultPos();
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		titleLabel = new EGuiLabel(this, midX, startY + 6, (currentList != null ? currentList.getFilterName() : "") + "Chat Filters", 0xffbb00).setDrawCentered(true).enableShadow(true);
		
		filterListDisplay = new EGuiTextArea(this, startX + 5, startY + 21, width - 10, height - 97, true).setDrawLineNumbers(true);
		
		previous = new EGuiButton(this, startX + 5, filterListDisplay.endY + 2, 11, 19).setTextures(EMCResources.guiButtonLeft, EMCResources.guiButtonLeftSel);
		next = new EGuiButton(this, endX - 16, filterListDisplay.endY + 2, 11, 19).setTextures(EMCResources.guiButtonRight, EMCResources.guiButtonRightSel);
		buttonContainer = new EGuiContainer(this, previous.endX + 2, filterListDisplay.endY + 2, next.startX - previous.endX - 4, 19).setDrawTitle(false);
		
		addFilter = new EGuiButton(this, startX + 5, previous.endY + 6, width / 2 - 10, 20, "Add Filter");
		removeFilter = new EGuiButton(this, endX - (width / 2 - 5), previous.endY + 6, width / 2 - 10, 20, "Remove Filter");
		
		reset = new EGuiButton(this, startX + 5, endY - 23, 80, 20, "Reset Filters");
		help = new EGuiButton(this, midX - 10, endY - 23, 20, 20).setButtonTexture(EMCResources.guiInfo).setTextures(EMCResources.guiInfo, EMCResources.guiInfoSel);
		back = new EGuiButton(this, endX - 85, endY - 23, 80, 20, "Back to Main");
		
		addObject(titleLabel, filterListDisplay);
		addObject(previous, buttonContainer, next);
		addObject(addFilter, removeFilter);
		addObject(reset, help, back);
		
		buildFilterList();
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		
		drawRect(startX + 1, startY + 19, endX - 1, endY - 1, 0xff383838); //grey background
		drawRect(startX, startY + 18, endX, startY + 19, 0xff000000); //top line
		drawRect(startX, endY - 53, endX, endY - 52, 0xff000000); //mid line
		drawRect(startX + 1, endY - 52, endX - 1, endY - 26, 0xff505050); //lighter mid grey background
		drawRect(startX, endY - 26, endX, endY - 25, 0xff000000); //bottom line
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == back) { fileUpAndClose(); }
	}
	
	private void setCurrentList(ChatFilterList listIn) {
		
	}
	
	private void buildFilterList() {
		if (currentList == null && ChatOrganizer.filters.size() > 0) { currentList = ChatOrganizer.filters.get(0); }
		if (currentList != null) {
			//filterListDisplay.clear();
			titleLabel.setDisplayString(currentList.getMCColor() + currentList.getFilterName() + " " + EnumChatFormatting.GOLD + titleLabel.getDisplayString());
			System.out.println(currentList.getFilterList());
			for (String s : currentList.getFilterList()) {
				filterListDisplay.addTextLine(s);
			}
		}
	}
	
	private void buildFilterButtons() {
		
	}
}
