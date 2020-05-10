package com.Whodundid.hotkeys.hotkKeyGuis;

import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.util.renderUtil.EColors;

//Last edited: Dec 21, 2018
//First Added: Dec 21, 2018
//Author: Hunter Bragg

public class RegisteredKeyVisualGui extends WindowParent {
	
	@Override
	public void initGui() {
		setDimensions(200, 256);
		setObjectName("RegisteredKeyVisualGui");
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		
		drawStringCS("Not ready :)", midX, midY, EColors.rainbow());
		super.drawObject(mXIn, mYIn);
	}
}
