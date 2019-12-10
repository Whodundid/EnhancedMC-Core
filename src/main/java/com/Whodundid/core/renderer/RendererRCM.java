package com.Whodundid.core.renderer;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiRightClickMenu;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EMCGuiSelectionList;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.settings.SettingsGuiMain;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubModType;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.Resources;

public class RendererRCM extends EGuiRightClickMenu {
	
	public RendererRCM(IEnhancedGuiObject parentIn, int x, int y) {
		super(parentIn, x, y);
		
		if (RegisteredSubMods.isModRegEn(SubModType.ENHANCEDCHAT)) { addOption("New Chat Window"); }
		addOption("New Window");
		addOption("Open EMC Settings", Resources.guiSettingsButton);
		addOption("Close All Objects");
		
		setRunActionOnPress(true);
		setActionReciever(this);
		
		setTitle("Enhanced MC");
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object == this) {
			switch ((String) getSelectedObject()) {
			case "New Chat Window": openChatWindow(); break;
			case "New Window": openGui(); break;
			case "Open EMC Settings": openSettings(); break;
			case "Close All Objects": clearScreen(); break;
			}
		}
	}
	
	private void openSettings() {
		EnhancedMC.displayEGui(new SettingsGuiMain(), CenterType.cursor);
	}
	
	private void openChatWindow() {
		if (RegisteredSubMods.isModRegEn(SubModType.ENHANCEDCHAT)) {
			RegisteredSubMods.getMod(SubModType.ENHANCEDCHAT).sendArgs("EnhancedChat: add window cursor");
		}
	}
	
	private void openGui() {
		EnhancedMC.displayEGui(new EMCGuiSelectionList(EnhancedMCRenderer.getInstance()), CenterType.cursor);
	}
	
	private void clearScreen() {
		EnhancedMCRenderer.getInstance().getImmediateChildren().clear();
	}
}
