package com.Whodundid.windowHUD.windowObjects.hotbar;

import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.enhancedGui.guiObjects.utilityObjects.EGuiRightClickMenu;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;

public class HotBarRCM extends EGuiRightClickMenu {

	HotBarRenderer hotBar;
	
	public HotBarRCM(HotBarRenderer parentIn) {
		hotBar = parentIn;
		
		addOption(HotBarRenderer.getOrientation() ? "Draw Horizontal" : "Draw Vertical", EMCResources.guiMoveButton);
		addOption("Draw Item Text", EMCResources.guiInfo);
		addOption("Reset Position", EMCResources.guiCloseButton);
		
		setRunActionOnPress(true);
		setActionReciever(this);
		
		setTitle("Hotbar");
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == this) {
			switch ((String) getSelectedObject()) {
			case "Draw Horizontal":
			case "Draw Vertical": switchOrientation(); break;
			case "Draw Item Text": drawItemText(); break;
			case "Reset Position": resetPos(); break;
			}
		}
	}
	
	private void switchOrientation() {
		hotBar.flipOrientation();
	}
	
	private void drawItemText() {
		
	}
	
	private void resetPos() {
		hotBar.resetPosition();
	}
}
