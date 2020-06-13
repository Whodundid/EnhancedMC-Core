package com.Whodundid.core.debug;

import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.utilityObjects.PlayerViewer;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;

//Author: Hunter Bragg

public class TestWindow extends WindowParent {

	PlayerViewer viewer;
	WindowButton ok;
	
	public TestWindow() {
		super();
		getAliases().add("test");
		windowIcon = EMCResources.experimentIcon;
	}
	
	@Override
	public void initWindow() {
		setDimensions(150, 150);
		setObjectName("Test Window");
		setResizeable(true);
		setMinDims(50, 50);
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		viewer = new PlayerViewer(this, startX + 5, startY + 5, width - 10, height - 10, mc.thePlayer);
		//viewer.setLockHead(true);
		
		ok = new WindowButton(this, endX - 55, endY - 25, 50, 20, "Test");
		
		addObject(null, viewer);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void onFirstDraw() {
		super.onFirstDraw();
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
	}
	
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		super.mouseReleased(mXIn, mYIn, button);
	}
	
	@Override public boolean isOpWindow() { return true; }
	@Override public boolean showInLists() { return false; }
	
}
