package com.Whodundid.playerInfo.gui;

import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiTextField;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.EGuiTextArea;
import com.Whodundid.core.enhancedGui.guiObjects.utilityObjects.EGuiPlayerViewer;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.util.playerUtil.DummyPlayer;

public class PlayerInfoGui extends WindowParent {
	
	boolean searching = false;
	
	EGuiButton search, clear;
	
	EGuiTextField nameEntry;
	EGuiTextArea nameHistory;
	EGuiPlayerViewer playerViewer;
	
	DummyPlayer playerEntity;
	
	@Override
	public void initGui() {
		defaultDims();
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		//nameEntry = new EGuiTextField(this, startX + )
		
		addObject(nameEntry, search);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		
	}

}
