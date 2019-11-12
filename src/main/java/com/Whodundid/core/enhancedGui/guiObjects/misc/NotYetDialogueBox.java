package com.Whodundid.core.enhancedGui.guiObjects.misc;

import com.Whodundid.core.enhancedGui.guiObjects.EGuiDialogueBox;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;

//Last edited: Feb 18, 2019
//First Added: Feb 18, 2019
//Author: Hunter Bragg

public class NotYetDialogueBox extends EGuiDialogueBox {
	
	public NotYetDialogueBox(IEnhancedGuiObject parentIn) {
		super(parentIn, parentIn.getDimensions().getMidX() - 125, parentIn.getDimensions().getMidY() - 48, 250, 75, DialogueBoxTypes.ok);
		setResizeable(false);
	}
	
	@Override
	public void onAdded() {
		setMessage("This feature is not ready yet.. Be on the lookout for future EnhancedMC releases!").setMessageColor(0xff5555);
		setDisplayString("Error");
	}
}
