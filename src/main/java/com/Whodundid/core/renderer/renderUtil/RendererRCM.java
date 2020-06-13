package com.Whodundid.core.renderer.renderUtil;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.renderer.taskView.TaskBar;
import com.Whodundid.core.settings.SettingsWindowMain;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.windowLibrary.windowObjects.windows.EMCGuiSelectionList;
import com.Whodundid.core.windowLibrary.windowObjects.windows.RightClickMenu;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;

//Author: Hunter Bragg

public class RendererRCM extends RightClickMenu {
	
	@Override
	public void initWindow() {
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
	public void actionPerformed(IActionObject object, Object... args) {
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
		EnhancedMC.displayWindow(new SettingsWindowMain(), CenterType.cursor);
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
		EnhancedMC.displayWindow(new EMCGuiSelectionList(), CenterType.cursor);
	}
	
	private void clearScreen() {
		EnhancedMCRenderer ren = EnhancedMCRenderer.getInstance();
		EArrayList<IWindowObject> objs = EArrayList.combineLists(ren.getObjects(), ren.getAddingObjects());
		TaskBar bar = ren.getTaskBar();
		
		for (IWindowObject o : objs) {
			if (bar != null) {
				if (o == bar || o.isChild(bar)) { continue; }
			}
			
			if (o.isCloseable()) { o.close(); }
		}
		//EnhancedMC.displayWindow(null);
	}
	
}
