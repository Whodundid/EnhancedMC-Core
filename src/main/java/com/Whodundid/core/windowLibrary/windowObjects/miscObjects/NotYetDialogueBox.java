package com.Whodundid.core.windowLibrary.windowObjects.miscObjects;

import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.windowLibrary.windowObjects.windows.WindowDialogueBox;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;

//Author: Hunter Bragg

public class NotYetDialogueBox extends WindowDialogueBox {
	
	public NotYetDialogueBox(IWindowObject parentIn) {
		super(DialogueBoxTypes.ok);
		setResizeable(false);
	}
	
	@Override
	public void onAdded() {
		setMessage("This feature is not ready yet.. Be on the lookout for future EnhancedMC releases!").setMessageColor(0xff5555);
		setTitle("Error");
		setTitleColor(EColors.lgray.intVal);
	}
	
}
