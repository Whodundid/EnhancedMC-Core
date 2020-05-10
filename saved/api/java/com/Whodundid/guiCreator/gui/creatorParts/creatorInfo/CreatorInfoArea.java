package com.Whodundid.guiCreator.gui.creatorParts.creatorInfo;

import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.types.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.guiCreator.gui.CreatorGui;

import net.minecraft.util.EnumChatFormatting;

public class CreatorInfoArea extends EnhancedGuiObject {
	
	CreatorGui parentCreator;
	EGuiLabel toolDisplay, modeDisplay, mousePosDisplay;
	
	public CreatorInfoArea(CreatorGui guiIn) {
		EDimension ddim = guiIn.getDesignSpace().getDimensions();
		EDimension cdim = guiIn.getDimensions();
		
		init(guiIn, ddim.startX, ddim.endY + 2, ddim.width, cdim.endY - 3 - ddim.endY - 2);
		
		parentCreator = guiIn;
	}
	
	@Override
	public void initObjects() {
		toolDisplay = new EGuiLabel(this, startX + 4, midY - 3, "Current Tool: ");
		modeDisplay = new EGuiLabel(this, startX + 151, midY - 3, "Mode: ");
		
		toolDisplay.setDisplayStringColor(EColors.seafoam);
		modeDisplay.setDisplayStringColor(EColors.lred);
		
		addObject(toolDisplay, modeDisplay);
		
		update();
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(startX, startY, endX, endY, 0xff000000); //background
		drawRect(startX + 1, startY + 1, endX - 1, endY - 1, 0xff343434); //inner background
		
		drawRect(startX + 1, startY + 1, startX + 146, endY - 1, 0xff686868); //tool background
		
		super.drawObject(mXIn, mYIn);
		
		drawRect(startX + 146, startY, startX + 147, endY, 0xff000000); //separator line (tool - mousePos)
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		
	}
	
	public void update() {
		toolDisplay.setDisplayString("Current Tool: " + EnumChatFormatting.YELLOW + parentCreator.getCurrentTool().name);
		modeDisplay.setDisplayString("Mode: " + EnumChatFormatting.YELLOW + parentCreator.getCurrentMode().name);
	}
}
