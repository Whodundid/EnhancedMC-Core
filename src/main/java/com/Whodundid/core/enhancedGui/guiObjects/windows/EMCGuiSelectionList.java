package com.Whodundid.core.enhancedGui.guiObjects.windows;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.coreApp.CoreApp;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.debug.ExperimentGui;
import com.Whodundid.core.debug.TestWindow;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.util.guiUtil.CommonVanillaGuis;
import com.Whodundid.core.util.guiUtil.GuiOpener;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;

//Author: Hunter Bragg

public class EMCGuiSelectionList extends EGuiSelectionList {
	
	public EMCGuiSelectionList() { this(EnhancedMCRenderer.getInstance()); }
	public EMCGuiSelectionList(IEnhancedGuiObject parentIn) {
		ScaledResolution res = new ScaledResolution(mc);
		init(parentIn, (res.getScaledWidth() - 200) / 2, (res.getScaledHeight() - 230) / 2, 200, 228);
		listContents = buildList();
		defaultSelectionObject = null;
		actionReciever = this;
		setZLevel(10000);
		aliases.add("guiselection", "guiselect", "guilist", "glist");
		windowIcon = EMCResources.openGuiIcon;
	}
	
	private StorageBoxHolder<String, Object> buildList() {
		StorageBoxHolder<String, Object> list = new StorageBoxHolder();
		if (RegisteredApps.getAllGuiClasses().size() > 0) {
			boolean flag = ((CoreApp) RegisteredApps.getApp(AppType.CORE)).enableTerminal.get();
			if (EnhancedMC.isDebugMode() || flag) {
				list.add(EnumChatFormatting.GRAY + "EMC Debug Guis", null);
				if (EnhancedMC.isOpMode()) {
					list.add(EnumChatFormatting.LIGHT_PURPLE + "Experiment Gui", new StorageBox<Class, StorageBox<Class[], Object[]>>(ExperimentGui.class, null));
					list.add(EnumChatFormatting.LIGHT_PURPLE + "Test Window", new StorageBox<Class, StorageBox<Class[], Object[]>>(TestWindow.class, null));
				}
				if (flag) {
					list.add(EnumChatFormatting.LIGHT_PURPLE + "Enhanced MC Terminal", new StorageBox<Class, StorageBox<Class[], Object[]>>(ETerminal.class, null));
				}
				list.add("", null);
			}
			
			list.add(EnumChatFormatting.GRAY + "EMC App Guis", null);
			
			for (EMCApp a : RegisteredApps.getAppsList()) {
				boolean isCore = a.getAppType() == AppType.CORE;
				
				for (IWindowParent p : a.getGuis()) {
					if (!p.showInLists()) { continue; }
					if (p.isOpWindow() && !EnhancedMC.isOpMode()) { continue; }
					else if (p.isDebugWindow() && !EnhancedMC.isDebugMode()) { continue; }
					list.add(EnumChatFormatting.GREEN + p.getClass().getSimpleName(), new StorageBox<Class, StorageBox<Class[], Object[]>>(p.getClass(), null));
				}
			}
		}
		
		if (CommonVanillaGuis.getGuis().size() > 0) {
			list.add("", null);
			list.add(EnumChatFormatting.GRAY + "Vanilla Guis", null);
			for (StorageBox<Class, StorageBox<Class[], Object[]>> g : CommonVanillaGuis.getGuis()) {
				list.add(EnumChatFormatting.GREEN + g.getObject().getSimpleName(), g);
			}
		}
		
		return list;
	}
	
	@Override
	protected void selectCurrentOptionAndClose() {
		if (selectionList.getCurrentLine() != null && selectionList.getCurrentLine().getStoredObj() != null) {
			selectedObject = selectionList.getCurrentLine().getStoredObj();
			if (selectedObject instanceof StorageBox) {
				try {
					GuiOpener.openGui((Class)((StorageBox) selectedObject).getObject(), this, CenterType.object);
				} catch (Exception e) { e.printStackTrace(); }
			}
		}
		close();
	}
}
