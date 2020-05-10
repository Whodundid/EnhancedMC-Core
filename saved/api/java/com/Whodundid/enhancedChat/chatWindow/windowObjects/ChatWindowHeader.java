package com.Whodundid.enhancedChat.chatWindow.windowObjects;

import com.Whodundid.core.enhancedGui.StaticEGuiObject;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiUtil.EObjectGroup;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.ObjectModifyType;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedTopParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.enhancedChat.chatWindow.ChatWindow;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;

//Last edited: Mar 22, 2019
//First Added: Jan 11, 2019
//Author: Hunter Bragg

public class ChatWindowHeader extends EGuiHeader {
	
	protected ChatWindow parentWindow;
	protected EArrayList<String> currentFilters = new EArrayList();
	protected EArrayList<ChatHeaderTab> currentChatTabs = new EArrayList();
	protected EArrayList<ChatHeaderTab> newTabs = new EArrayList();
	
	public ChatWindowHeader(ChatWindow parentIn) {
		super(parentIn, false, 19, "");
		parentWindow = parentIn;
		setDrawTitle(false);
	}
	
	public ChatWindowHeader(ChatWindow parentIn, String... filtersIn) { this(parentIn, new EArrayList<String>(filtersIn)); }
	public ChatWindowHeader(ChatWindow parentIn, EArrayList<String> typesIn) {
		super(parentIn, false, 19, "");
		parentWindow = parentIn;
		setDrawTitle(false);
		
		addPinButton();
		addCloseButton();
		
		EObjectGroup group = new EObjectGroup(getParent());
		group.addObject(this, pinButton, closeButton);
		setObjectGroup(group);
		
		createChatTabs(typesIn);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		mX = mXIn; mY = mYIn;
		if (!mouseEntered && isMouseInside(mXIn, mYIn)) { mouseEntered = true; mouseEntered(mX, mY); }
		if (mouseEntered && !isMouseInside(mXIn, mYIn)) { mouseEntered = false; mouseExited(mX, mY); }
		if (!objsToBeRemoved.isEmpty()) { StaticEGuiObject.removeObjects(this, objsToBeRemoved); }
		if (!objsToBeAdded.isEmpty()) { StaticEGuiObject.addObjects(this, objsToBeAdded); }
		if (!newTabs.isEmpty()) { updateOnNextDraw(); }
		
		IEnhancedTopParent top = getTopParent();
		if (top.getModifyingObject() == parent && top.getModifyType() == ObjectModifyType.Move && !Mouse.isButtonDown(0)) {
			top.clearModifyingObject();
		}
		
		//updateCursorImage();
		if (checkDraw()) {
			boolean anyFocus = false;
			if (drawParentFocus) {
				IWindowParent p = getWindowParent();
				if (p != null) {
					if (p.hasFocus()) { anyFocus = true; }
					else {
						for (IEnhancedGuiObject o : p.getAllChildren()) {
							if (o.hasFocus()) { anyFocus = true; break; }
						}
					}
				}
			}
			
			if (drawHeader) {
				drawRect(startX, startY, startX + 1, startY + height, borderColor); //left
				drawRect(startX + 1, startY, endX - 1, startY + 1, borderColor); //top
				drawRect(endX - 1, startY, endX, startY + height, borderColor); //right
				drawRect(startX + 1, startY + 1, endX - 1, startY + height, anyFocus ? mainColor - 0x1f1f1f : mainColor); //mid
				if (drawTitle) {
					drawString(title, startX + 4, startY + height / 2 - 3, titleColor);
				}
			}
			for (ChatHeaderTab b : currentChatTabs) {
				b.drawObject(mXIn, mYIn);
			}
			for (IEnhancedGuiObject o : guiObjects) {
				if (o.checkDraw() && !currentChatTabs.contains(o)) {
    				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    	        	o.drawObject(mX, mY);
    			}
	        }
		}
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		parentWindow.handleRCMs(mX, mY, button);
		if (button == 1) { parentWindow.openMainRCM(mX, mY); }
		super.mousePressed(mX, mY, button);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == pinButton) { handlePin(); }
		if (object == closeButton) { handleClose(); }
	}
	
	public EArrayList<String> getAllChatFilters() {
		EArrayList<String> chats = new EArrayList();
		for (ChatHeaderTab b : currentChatTabs) { chats.add(b.getFilter()); }
		return chats;
	}
	
	/** Returns a copy of the current chat tabs in this header. */
	public EArrayList<ChatHeaderTab> getAllChatTabs() { return new EArrayList<ChatHeaderTab>(currentChatTabs); }
	/** Returns a copy of the current chat types in this header. */
	public EArrayList<String> getCurrentChatTypes() { return new EArrayList<String>(currentFilters); }
	
	/** Retrieves a specific chat tab of the given ChatType. If the type is not in the list, null is returned instead. */
	public ChatHeaderTab getChatTab(String filterNameIn) {
		for (ChatHeaderTab b : currentChatTabs) { if (b.getFilter().equals(filterNameIn)) { return b; } }
		return null;
	}
	
	/** Signals the header to rebuild the tab list off of the window's chat types. */
	public void updateChatTabs() {
		EArrayList<ChatHeaderTab> tabs = generateNewTabsList(parentWindow.getAllFilters());
		newTabs.addAll(tabs);
	}
	
	/** Clears the existing tabs, adds the newly created tabs from updateChatTabs and visually updates them. */
	protected void updateOnNextDraw() {
		clearChatTabs();
		for (ChatHeaderTab t : newTabs) {
			currentChatTabs.add(t);
			currentFilters.add(t.getFilter());
			addObject(t);
		}
		newTabs.clear();
		parentWindow.updateEnabledChatTypes();
	}
	
	/** Creates new chat tabs from specified list */
	private void createChatTabs(EArrayList<String> filtersIn) {
		if (filtersIn != null) {
			EArrayList<ChatHeaderTab> tabs = generateNewTabsList(parentWindow.getAllFilters());
			for (ChatHeaderTab t : tabs) {
				currentChatTabs.add(t);
				currentFilters.add(t.getFilter());
				addObject(t);
			}
		}
	}
	
	/** Internal helper method that returns a list of ChatHeaderTabs based on a given list of ChatTypes. */
	private EArrayList<ChatHeaderTab> generateNewTabsList(EArrayList<String> filtersIn) {
		EArrayList<ChatHeaderTab> tabs = new EArrayList();
		ChatHeaderNewTabButton newTabObject = new ChatHeaderNewTabButton(this);
		if (filtersIn != null && filtersIn.isNotEmpty()) {
			int bX = 2;
			int bW = MathHelper.clamp_int((((pinButton.startX - 2) - (startX + 2)) - filtersIn.size() * 2 - 16) / filtersIn.size(), 4, 85);
			for (String s : filtersIn) {
				ChatHeaderTab b = new ChatHeaderTab(this, s, bW);
				b.setPosition(startX + bX,  startY + 2); b.startXPos = startX + bX;
				tabs.add(b);
				bX += (bW + 1);
			}
			newTabObject.setDimensions(startX + ((bW + 1) * tabs.size()) + 2, startY + 2, 14, height - 3);
			tabs.add(newTabObject);
		} else {
			newTabObject.setDimensions(startX + 2, startY + 2, 14, height - 3);
			tabs.add(newTabObject);
		}
		return tabs;
	}
	
	/** Used to change the display order of tabs in this header. */
	public void modifyTabOrder(EArrayList<String> orderIn) {
		EArrayList<String> order = new EArrayList();
		if (!EUtil.validateArrayContents(currentFilters, orderIn)) {
			EArrayList<ChatHeaderTab> tabs = generateNewTabsList(orderIn);
			newTabs.addAll(tabs);
		}
	}
	
	protected void clearChatTabs() {
		currentChatTabs.clear();
		currentFilters.clear();
		for (IEnhancedGuiObject o : guiObjects) {
			if (o instanceof ChatHeaderTab) { removeObject(o); }
		}
	}
	
	public ChatWindow getParentChatWindow() { return parentWindow; }
}
