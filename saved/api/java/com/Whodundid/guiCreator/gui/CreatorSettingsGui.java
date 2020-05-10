package com.Whodundid.guiCreator.gui;

import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;

public class CreatorSettingsGui extends WindowParent {

	EGuiButton bEUttoN;
	
	public CreatorSettingsGui() {
		super();
		aliases.add("creatorsettings", "gcreators", "gcreatorsettings", "egcs", "gcs");
	}
	
	@Override
	public void initGui() {
		setDimensions(defaultWidth, defaultHeight);
		setMinDims(170, 75);
		setResizeable(true);
	}
	
	@Override
	public void initObjects() {
		
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
