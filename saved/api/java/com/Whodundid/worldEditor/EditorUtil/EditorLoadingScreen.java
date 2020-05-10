package com.Whodundid.worldEditor.EditorUtil;

import com.Whodundid.core.enhancedGui.types.EnhancedGui;
import com.Whodundid.core.enhancedGui.types.WindowParent;

//Last edited: Sep 28, 2018
//First Added: Sep 28, 2018
//Author: Hunter Bragg

public class EditorLoadingScreen extends WindowParent {
	
	@Override public void initGui() {
		super.initGui();
		enableHeader(false);
		//finishInit();
	}
	
	@Override
	public void drawObject(int mX, int mY) {
		drawDefaultBackground();
		drawCenteredString("Loading...", midX, midY, 0x00ff00);
	}
}
