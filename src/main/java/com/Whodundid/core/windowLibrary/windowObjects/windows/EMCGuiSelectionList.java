package com.Whodundid.core.windowLibrary.windowObjects.windows;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.coreApp.CoreApp;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.debug.ExperimentGui;
import com.Whodundid.core.debug.TestWindow;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.guiUtil.CommonVanillaGuis;
import com.Whodundid.core.util.guiUtil.GuiOpener;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowParent;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;

//Author: Hunter Bragg

public class EMCGuiSelectionList extends WindowSelectionList {
	
	public EMCGuiSelectionList() {
		super(EnhancedMC.getRenderer());
		setActionReceiver(this);
		aliases.add("guiselection", "guiselect", "guilist", "glist");
		windowIcon = EMCResources.openGuiIcon;
	}
	
	@Override
	public void initObjects() {
		super.initObjects();
		buildList();
	}
	
	private void buildList() {
		if (RegisteredApps.getAllGuiClasses().size() > 0) {
			boolean flag = ((CoreApp) RegisteredApps.getApp(AppType.CORE)).enableTerminal.get();
			
			if (EnhancedMC.isDebugMode() || flag) {
				writeLine("EMC Debug Guis", EColors.lgray);
				if (EnhancedMC.isDevMode()) {
					addOption("Experiment Gui", EColors.pink, new StorageBox<Class, StorageBox<Class[], Object[]>>(ExperimentGui.class, null));
					addOption("Test Window", EColors.pink, new StorageBox<Class, StorageBox<Class[], Object[]>>(TestWindow.class, null));
				}
				if (flag) {
					addOption("Enhanced MC Terminal", EColors.pink, new StorageBox<Class, StorageBox<Class[], Object[]>>(ETerminal.class, null));
				}
				writeLine();
			}
			
			writeLine("EMC App Guis", EColors.lgray);
			
			for (EMCApp a : RegisteredApps.getAppsList()) {
				boolean isCore = a.getAppType() == AppType.CORE;
				
				for (IWindowParent p : a.getWindows()) {
					if (!p.showInLists()) { continue; }
					if (p.isOpWindow() && !EnhancedMC.isDevMode()) { continue; }
					else if (p.isDebugWindow() && !EnhancedMC.isDebugMode()) { continue; }
					addOption(p.getClass().getSimpleName(), EColors.green, new StorageBox<Class, StorageBox<Class[], Object[]>>(p.getClass(), null));
				}
			}
		}
		
		if (CommonVanillaGuis.getGuis().size() > 0) {
			writeLine();
			writeLine("Vanilla Guis", EColors.lgray);
			for (StorageBox<Class, StorageBox<Class[], Object[]>> g : CommonVanillaGuis.getGuis()) {
				if (g.getObject() != GuiMainMenu.class && g.getObject() != GuiMultiplayer.class) {
					addOption(g.getObject().getSimpleName(), EColors.green, g);
				}
			}
		}
	}
	
	@Override
	protected void selectCurrentOptionAndClose() {
		if (list.getCurrentLine() != null && list.getCurrentLine().getStoredObj() != null) {
			selectedObject = list.getCurrentLine().getStoredObj();
			
			if (selectedObject instanceof StorageBox) {
				try {
					GuiOpener.openGui((Class) ((StorageBox) selectedObject).getObject(), this, CenterType.object);
				}
				catch (Exception e) { e.printStackTrace(); }
			}
		}
		close();
	}
	
}
