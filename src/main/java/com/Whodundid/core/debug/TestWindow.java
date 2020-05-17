package com.Whodundid.core.debug;

import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.utilityObjects.EGuiPlayerViewer;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;

//Author: Hunter Bragg

public class TestWindow extends WindowParent {

	EGuiPlayerViewer viewer;
	EGuiButton ok;
	
	public TestWindow() {
		super();
		getAliases().add("test");
		windowIcon = EMCResources.experimentIcon;
	}
	
	@Override
	public void initGui() {
		setDimensions(150, 150);
		setObjectName("Test Window");
		setResizeable(true);
		setMinDims(50, 50);
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		viewer = new EGuiPlayerViewer(this, startX + 5, startY + 5, width - 10, height - 10, mc.thePlayer);
		viewer.setLockHead(true);
		
		ok = new EGuiButton(this, endX - 55, endY - 25, 50, 20, "Test");
		
		addObject(viewer);
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
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		
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
