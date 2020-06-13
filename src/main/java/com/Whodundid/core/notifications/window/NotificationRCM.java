package com.Whodundid.core.notifications.window;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.notifications.util.NotificationObject;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.windowLibrary.windowObjects.windows.RightClickMenu;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;

//Author: Hunter Bragg

public class NotificationRCM extends RightClickMenu {

	private NotificationObject note = null;
	
	public NotificationRCM() { this(null); }
	public NotificationRCM(NotificationObject noteIn) {
		super();
		note = noteIn;
	}
	
	@Override
	public void initWindow() {
		if (note != null) { addOption("Close", EMCResources.guiCloseButton); }
		addOption("Settings", EMCResources.guiSettingsButton);

		setRunActionOnPress(true);
		setActionReceiver(this);
		
		setTitle("Notifications");
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == this) {
			switch ((String) getSelectedObject()) {
			case "Close": note.close(); break;
			case "Settings": openSettings(); break;
			}
		}
	}
	
	private void openSettings() {
		EnhancedMC.displayWindow(new NotificationWindow(), CenterType.screen);
		if (note != null) { note.close(); }
	}
	
}