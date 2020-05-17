package com.Whodundid.core.renderer.renderUtil;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiRightClickMenu;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EMCGuiSelectionList;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.settings.SettingsGuiMain;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.storageUtil.EArrayList;

//Author: Hunter Bragg

public class RendererRCM extends EGuiRightClickMenu {
	
	@Override
	public void initGui() {
		//if (RegisteredApps.isAppRegEn(AppType.ENHANCEDCHAT)) { addOption("New Chat Window"); }
		if (EnhancedMC.getEMCApp().enableTerminal.get()) { addOption("New Terminal", EMCResources.terminalButton); }
		addOption("New Window", EMCResources.plusButton);
		addOption("Open EMC Settings", EMCResources.guiSettingsButton);
		addOption("Close All Objects", EMCResources.guiCloseButton);
		
		setRunActionOnPress(true);
		setActionReceiver(this);
		
		setTitle("Options...");
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == this) {
			switch ((String) getSelectedObject()) {
			case "New Chat Window": openChatWindow(); break;
			case "New Terminal": openTerminal(); break;
			case "New Window": openGui(); break;
			case "Open EMC Settings": openSettings(); break;
			case "Close All Objects": clearScreen(); break;
			}
		}
	}
	
	private void openSettings() {
		EnhancedMC.displayWindow(new SettingsGuiMain(), CenterType.cursor);
	}
	
	private void openChatWindow() {
		if (RegisteredApps.isAppRegEn(AppType.ENHANCEDCHAT)) {
			RegisteredApps.getApp(AppType.ENHANCEDCHAT).sendArgs("EnhancedChat: add window cursor");
		}
	}
	
	private void openTerminal() {
		EnhancedMC.displayWindow(new ETerminal(), CenterType.cursor);
	}
	
	private void openGui() {
		EnhancedMC.displayWindow(new EMCGuiSelectionList(EnhancedMCRenderer.getInstance()), CenterType.cursor);
	}
	
	private void clearScreen() {
		EnhancedMCRenderer ren = EnhancedMCRenderer.getInstance();
		EArrayList<IEnhancedGuiObject> objs = EArrayList.combineLists(ren.getObjects(), ren.getAddingObjects());
		for (IEnhancedGuiObject o : objs) {
			if (o.isCloseable()) { o.close(); }
		}
	}
	
}
