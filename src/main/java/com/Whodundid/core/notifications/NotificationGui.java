package com.Whodundid.core.notifications;

import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiTextField;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.scrollList.EGuiScrollList;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;

public class NotificationGui extends WindowParent {
	
	EGuiButton toggleState, back;
	EGuiScrollList notificationList;
	EGuiTextField searchField;
	
	@Override
	public void initGui() {
		
	}
	
	@Override
	public void initObjects() {
		
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		
	}
	
	private class NotificationSetting {
		
		EGuiLabel name;
		EGuiButton toggle;
		
		public NotificationSetting(String typeIn) {
			
		}
	}
}
 