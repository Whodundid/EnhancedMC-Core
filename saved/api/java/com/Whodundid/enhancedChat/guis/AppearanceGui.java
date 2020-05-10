package com.Whodundid.enhancedChat.guis;

import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiSlider;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.scrollList.EGuiScrollList;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;

public class AppearanceGui extends WindowParent {
	
	EGuiScrollList list;
	EGuiSlider opactiySlider;
	EGuiButton backgroundColor;
	EGuiButton back;
	
	public AppearanceGui() {
		super();
		aliases.add("chatappearance", "chatapp");
	}
	
	@Override
	public void initGui() {
		setDimensions(defaultWidth, defaultHeight);
		setObjectName("Window Appearance Settings");
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
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
