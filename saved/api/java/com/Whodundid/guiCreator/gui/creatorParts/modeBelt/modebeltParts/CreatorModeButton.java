package com.Whodundid.guiCreator.gui.creatorParts.modeBelt.modebeltParts;

import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.guiCreator.gui.creatorParts.modeBelt.CreatorModeBelt;
import com.Whodundid.guiCreator.util.CreatorMode;
import com.Whodundid.guiCreator.util.CreatorResources;

public class CreatorModeButton extends EGuiButton {
	
	public CreatorModeBelt modeBelt;
	public CreatorMode mode;
	
	public CreatorModeButton(CreatorModeBelt guiIn, CreatorMode toolIn) {
		super(guiIn, 0, 0, 0, 0);
		modeBelt = guiIn;
		mode = toolIn;
		
		assignResource();
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		super.drawObject(mXIn, mYIn);
		
		//draw selection border if current tool
		if (modeBelt != null && modeBelt.getParentCreator() != null) {
			if (mode == modeBelt.getParentCreator().getCurrentMode()) {
				int color = 0xffff2222;
				drawHRect(startX - 1, startY - 1, endX + 1, endY + 1, 1, color);
			}
		}
	}
	
	public CreatorMode getType() { return mode; }
	
	private void assignResource() {
		switch (mode) {
		case EDIT_MODE: setButtonTexture(CreatorResources.stop); setHoverText("Edit Mode"); break;
		case TEST_MODE: setButtonTexture(CreatorResources.play); setHoverText("Test Mode"); break;
		default: break;
		}
	}
}
