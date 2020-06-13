package com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.header;

import com.Whodundid.core.util.mathUtil.NumberUtil;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.ITopParent;

//Author: Hunter Bragg

public class HeaderTab extends WindowButton {

	protected WindowHeader header;
	protected boolean pressed = false;
	protected boolean windowCreated = false;
	protected int drawColor = 0xffb2b2b2;
	public static int defaultWidth = 55;
	protected StorageBox<Integer, Integer> mousePos = new StorageBox();
	protected StorageBox<Integer, Integer> startPos = new StorageBox();
	WindowButton closeButton = null;
	
	protected HeaderTab(WindowHeader headerIn) {
		super(headerIn);
		header = headerIn;
	}
	
	public HeaderTab(WindowHeader parentHeaderIn, String titleIn) { this(parentHeaderIn, titleIn, defaultWidth); }
	public HeaderTab(WindowHeader parentHeaderIn, String titleIn, int widthIn) {
		super(parentHeaderIn);
		init(parentHeaderIn, 0, 0, widthIn, parentHeaderIn.getDimensions().height - 3);
		header = parentHeaderIn;
		setString(titleIn);
		setDrawTextures(false);
		setDrawString(false);
	}
	
	@Override
	public void onAdded() {
		closeButton = new WindowButton(this, width >= 9 ? (endX - 9) : midX + (width - 7), startY + 3, 7, 7, "x") {
			@Override
			public void drawObject(int mXIn, int mYIn) {
				super.drawObject(mXIn, mYIn);
			}
			@Override
			public void onPress() {
				playPressSound();
			}
		};
		
		closeButton.setDrawTextures(false);
		closeButton.setRunActionOnPress(true);
		closeButton.setStringHoverColor(0xff6060);
		
		addObject(null, closeButton);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		if (pressed) { checkForMove(mXIn, mYIn); }
		if (closeButton != null) { closeButton.setVisible(width > 17 && isMouseInside(mXIn, mYIn)); }
		
		String drawString = "";
		
		try {
			String actual = getString();
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
				else { break; }
			}
		}
		catch (Exception e) { e.printStackTrace(); }
		
		int borderColor = 0xff000000 + 0xffffd800 - 0x00404000;
		//System.out.println(type.getChatType() + " " + borderColor);
		if (isMouseInside(mXIn, mYIn)) {
			//if (drawString.length() == 0) {
			//	drawRect(startX, startY, startX + 1, endY, isTypeEnabled ? borderColor + 0x00080800 : 0xaab2b2b2);
			//	drawRect(startX + 1, startY, endX - 1, startY + 1, isTypeEnabled ? borderColor + 0x00080800 : 0xaab2b2b2);
			//	drawRect(endX - 1, startY, endX, endY, isTypeEnabled ? borderColor + 0x00080800 : 0xaab2b2b2);
			//	drawRect(startX + 1, endY - 1, endX - 1, endY, isTypeEnabled ? borderColor + 0x00080800 : 0xaab2b2b2);
			//}
			//else {
				drawRect(startX, startY, startX + 1, endY, 0xaab2b2b2); //left
				drawRect(startX + 1, startY, endX - 1, startY + 1, 0xaab2b2b2); //top
				drawRect(endX - 1, startY, endX, endY, 0xaab2b2b2); //right
				drawRect(startX + 1, endY - 1, endX - 1, endY, 0xaab2b2b2); //bottom
			//}
		}
		else {
			//if (drawString.length() == 0) {
			//	drawRect(startX, startY, startX + 1, endY, isTypeEnabled ? borderColor : 0xaa909090);
			//	drawRect(startX + 1, startY, endX - 1, startY + 1, isTypeEnabled ? borderColor : 0xaa909090);
			//	drawRect(endX - 1, startY, endX, endY, isTypeEnabled ? borderColor - 0x00252500 : 0xaa404040);
			//	drawRect(startX + 1, endY - 1, endX - 1, endY, isTypeEnabled ? borderColor - 0x00252500 : 0xaa404040);
			//}
			//else {
				drawRect(startX, startY, startX + 1, endY, 0xaa909090); //left
				drawRect(startX + 1, startY, endX - 1, startY + 1, 0xaa909090); //top
				drawRect(endX - 1, startY, endX, endY, 0xaa404040); //right
				drawRect(startX + 1, endY - 1, endX - 1, endY, 0xaa404040); //bottom
			//}
		}
		drawRect(startX + 1, startY + 1, endX - 1, endY - 1, drawString.length() > 0 ? 0xaa757575 : borderColor + 0x00222200); //middle background
		
		drawStringWithShadow(drawString, startX + 4, startY + 4, drawColor);
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		super.mousePressed(mX, mY, button);
		/*
		if (button == 0) {
			IEnhancedTopParent topParent = getTopParent();
			topParent.setModifyMousePos(mX, mY);
			InGameChatWindow window = parentHeader.getParentChatWindow();
			EArrayList<String> chats = window.getAllFilters();
			if (chats.size() > 1) {
				toggleEnabledValue();
				mousePos.setValues(mX, mY);
				startPos.setValues(startX, startY);
				topParent.setModifyingObject(this, ObjectModifyType.Move);
				pressed = true;
			} else {
				if (getParent().getObjectGroup() != null && getParent().getObjectGroup().getGroupParent() != null) {
					topParent.setModifyingObject(getParent().getObjectGroup().getGroupParent(), ObjectModifyType.Move);
				}
			}
		} else {
			parentHeader.mousePressed(mX, mY, button);
		}
		*/
    }
	
	@Override
	public void mouseReleased(int mX, int mY, int button) {
		/*
		getTopParent().clearModifyingObject();
		if (parentHeader.getAllChatTabs().size() > 1) {
			pressed = false;
			if (!checkMouseDrag(mX, mY) && startPos != null && startPos.getObject() != null && startPos.getValue() != null) {
				setPosition(startPos.getObject(), startPos.getValue());
			}
		}
		*/
		super.mouseReleased(mX, mY, button);
	}
	
	protected void checkForMove(int mX, int mY) {
		/*
		if (!windowCreated) {
			//if (!getTopParent().isMouseInsideHeader(mX, mY)) {
				if (checkMouseDrag(mX, mY)) {
					InGameChatWindow newWindow = new InGameChatWindow(getTopParent(), filter);
					newWindow.setDimensions(mX - 22, mY + 10, 280, 152);
					if (EnhancedChatMod.makeNewWindowsPinned.get()) { newWindow.setPinned(true); }
					getTopParent().addObject(newWindow);
					
					IEnhancedTopParent topParent = getTopParent();
					topParent.setModifyingObject(newWindow, ObjectModifyType.MoveAlreadyClicked);
					topParent.setModifyMousePos(mX, mY);
					
					InGameChatWindow oldWindow = parentHeader.getParentChatWindow();
					EArrayList<String> oldChatTypes = oldWindow.getAllFilters();
					if (oldChatTypes.contains(filter)) {
						EArrayList<String> newChatTypes = oldChatTypes;
						newChatTypes.remove(filter);
						oldWindow.setChatFilters(newChatTypes);
						oldWindow.updateVisual();
					}
					windowCreated = true;
				}
			//}
		}
		*/
	}
	
	protected boolean checkMouseDrag(int mX, int mY) {
		ITopParent p = getTopParent();
		if (p != null) {
			//System.out.println("is mouse in header: " + p.isMouseInsideHeader(mX, mY));
		}
		StorageBox<Integer, Integer> testBox = new StorageBox(mX, mY);
		int dist = NumberUtil.getDistance(mousePos, testBox);
		//System.out.println("dist: " + dist);
		return dist > 20;
	}
	
}
