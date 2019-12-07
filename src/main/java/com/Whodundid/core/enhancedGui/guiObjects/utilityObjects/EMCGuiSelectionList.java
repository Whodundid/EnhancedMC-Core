package com.Whodundid.core.enhancedGui.guiObjects.utilityObjects;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreSubMod.EnhancedMCMod;
import com.Whodundid.core.debug.ExperimentGui;
import com.Whodundid.core.debug.terminal.gui.ETerminal;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiSelectionList;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubModType;
import com.Whodundid.core.util.guiUtil.CommonVanillaGuis;
import com.Whodundid.core.util.guiUtil.GuiOpener;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;

public class EMCGuiSelectionList extends EGuiSelectionList {
	
	public EMCGuiSelectionList() { this(EnhancedMCRenderer.getInstance()); }
	public EMCGuiSelectionList(IEnhancedGuiObject parentIn) {
		ScaledResolution res = new ScaledResolution(mc);
		init(parentIn, (res.getScaledWidth() - 200) / 2, (res.getScaledHeight() - 230) / 2, 200, 228);
		listContents = buildList();
		defaultSelectionObject = null;
		actionReciever = this;
		setZLevel(10000);
	}
	
	private StorageBoxHolder<String, Object> buildList() {
		StorageBoxHolder<String, Object> list = new StorageBoxHolder();
		if (RegisteredSubMods.getAllGuiClasses().size() > 0) {
			boolean flag = ((EnhancedMCMod) RegisteredSubMods.getMod(SubModType.CORE)).enableTerminal.get();
			if (EnhancedMC.isDebugMode() || flag) {
				list.add(EnumChatFormatting.GRAY + "EMC Debug Guis", null);
				if (EnhancedMC.isDebugMode()) {
					list.add(EnumChatFormatting.LIGHT_PURPLE + "Experiment Gui", new StorageBox<Class, StorageBox<Class[], Object[]>>(ExperimentGui.class, null));
				}
				if (flag) {
					list.add(EnumChatFormatting.LIGHT_PURPLE + "Enhanced MC Terminal", new StorageBox<Class, StorageBox<Class[], Object[]>>(ETerminal.class, null));
				}
				list.add("", null);
			}
			
			list.add(EnumChatFormatting.GRAY + "EMC SubMod Guis", null);
			
			for (Class c : RegisteredSubMods.getAllGuiClasses()) {
				list.add(EnumChatFormatting.GREEN + c.getSimpleName(), new StorageBox<Class, StorageBox<Class[], Object[]>>(c, null));
			}
			list.add("", null);
		}
		list.add("", null);
		
		if (CommonVanillaGuis.getGuis().size() > 0) {
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
