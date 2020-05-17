package com.Whodundid.core.notifications.window;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiRightClickMenu;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.notifications.util.NotificationObject;
import com.Whodundid.core.util.renderUtil.CenterType;

//Author: Hunter Bragg

public class NotificationRCM extends EGuiRightClickMenu {

	private NotificationObject note = null;
	
	public NotificationRCM() { this(null); }
	public NotificationRCM(NotificationObject noteIn) {
		super();
		note = noteIn;
	}
	
	@Override
	public void initGui() {
		if (note != null) { addOption("Close", EMCResources.guiCloseButton); }
		addOption("Settings", EMCResources.guiSettingsButton);

		setRunActionOnPress(true);
		setActionReceiver(this);
		
		setTitle("Notifications");
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
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