package com.Whodundid.core.notifications.baseObjects;

import com.Whodundid.core.coreSubMod.EMCResources;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiRightClickMenu;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;

public class NotificationRCM extends EGuiRightClickMenu {
	
	@Override
	public void initGui() {
		addOption("Open Notification Settings", EMCResources.guiSettingsButton);
		
		setRunActionOnPress(true);
		setActionReciever(this);
		
		setTitle("Notifications");
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == this) {
			switch ((String) getSelectedObject()) {
			case "Open Notification Settings": openSettings(); break;
			}
		}
	}
	
	private void openSettings() {
		
	}
}
