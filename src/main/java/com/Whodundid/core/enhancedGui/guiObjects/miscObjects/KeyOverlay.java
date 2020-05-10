package com.Whodundid.core.enhancedGui.guiObjects.miscObjects;

import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;

//Author: Hunter Bragg

public class KeyOverlay extends WindowParent {
	
	EGuiHeader movementHeader = null;
	boolean lmb = false;
	boolean rmb = false;
	boolean mmb = false;
	boolean w = false;
	boolean a = false;
	boolean s = false;
	boolean d = false;
	boolean space = false;
	boolean sneak = false;
	
	public KeyOverlay(IEnhancedGuiObject parentIn, int xIn, int yIn) {
		init(parentIn, xIn, yIn, 100, 100);
		setResizeable(true);
	}
	
	@Override
	public void initObjects() {
		movementHeader = new EGuiHeader(this) {
			{
				drawHeader = false;
			}
			@Override
			public void drawObject(int mXIn, int mYIn) {
				drawRect(startX, startY, startX + 1, startY + height, 0x66000000); //left
				drawRect(startX + 1, startY, endX - 1, startY + 1, 0x66000000); //top
				drawRect(endX - 1, startY, endX, startY + height, 0x66000000); //right
				drawRect(startX + 1, startY + 1, endX - 1, startY + height, 0x22000000); //mid
				super.drawObject(mXIn, mYIn);
			}
			@Override
			public void onAdded() {
				movementHeader.setDrawButtons(false);
				movementHeader.setDrawTitle(false);
			}
		};
		movementHeader.setResizeable(true);
		addObject(movementHeader);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		//background
		drawRect(startX, startY, startX + 1, startY + height - 1, 0x66000000); //left
		drawRect(startX + 1, startY, endX - 1, startY + 1, 0x66000000); //top
		drawRect(endX - 1, startY, endX, startY + height - 1, 0x66000000); //right
		drawRect(startX, endY - 1, endX, endY, 0x66000000); //bottom
		drawRect(startX + 1, startY + 1, endX - 1, startY + height - 1, 0x22000000); //mid
		
		//buttons
		drawRect(startX + 3, startY + (height / 3) * 2, startX + 2 + (width / 3 - 2), startY + (height / 3), a ? 0x66777777 : 0x66000000); //a
		drawRect(startX + 3 + (width / 3 - 2) * 2, startY + (height / 3) * 2, startX + 3 + (width / 3 - 2) + 1, startY + (height / 3), 0x66000000); //s
		drawRect(startX + 4 + (width / 3 - 2) * 3, startY + (height / 3) * 2, startX + 4 + (width / 3 - 2) * 2 + 1, startY + (height / 3), 0x66000000); //d
		
		//button strings
		drawString("A", startX + 3 + (width / 3 - 2) / 3 + 1, startY + (height / 3) + ((height / 3) / 2) - 3, 0xffffffff);
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
	}
	
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		super.mouseReleased(mXIn, mYIn, button);
	}
	
	@Override
	public void keyPressed(char typedCharIn, int keyCodeIn) {
		if (keyCodeIn == 30) { a = true; }
	}
	
	@Override
	public void keyReleased(char typedCharIn, int keyCodeIn) {
		if (keyCodeIn == 30) { a = false; }
	}
	
	private void checkButton(int buttonIn) {
		
	}
	
	private void checkKey(int keyCodeIn) {
		
	}
}
