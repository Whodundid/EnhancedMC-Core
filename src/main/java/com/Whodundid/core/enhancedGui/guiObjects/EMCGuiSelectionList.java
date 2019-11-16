package com.Whodundid.core.enhancedGui.guiObjects;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.debug.IDebugCommand;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubModType;
import com.Whodundid.core.util.guiUtil.CommonVanillaGuis;
import com.Whodundid.core.util.guiUtil.GuiOpener;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyActionType;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;

public class EMCGuiSelectionList extends EGuiSelectionList {
	
	public EMCGuiSelectionList(IEnhancedGuiObject parentIn) {
		ScaledResolution res = new ScaledResolution(mc);
		init(parentIn, (res.getScaledWidth() - 200) / 2, (res.getScaledHeight() - 230) / 2, 200, 230);
		listContents = buildList();
		defaultSelectionObject = null;
		actionReciever = this;
	}
	
	private StorageBoxHolder<String, Object> buildList() {
		StorageBoxHolder<String, Object> list = new StorageBoxHolder();
		
		if (RegisteredSubMods.getAllGuiClasses().size() > 0) {
			list.add(EnumChatFormatting.GRAY + "EMC SubMod Guis", null);
			for (Class c : RegisteredSubMods.getAllGuiClasses()) {
				list.add(EnumChatFormatting.GREEN + c.getSimpleName(), new StorageBox<Class, StorageBox<Class[], Object[]>>(c, null));
			}
			list.add("", null);
		}
		if (CommonVanillaGuis.getGuis().size() > 0) {
			list.add(EnumChatFormatting.GRAY + "Vanilla Guis", null);
			for (StorageBox<Class, StorageBox<Class[], Object[]>> g : CommonVanillaGuis.getGuis()) {
				list.add(EnumChatFormatting.GREEN + g.getObject().getSimpleName(), g);
			}
		}
		
		return list;
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		super.drawObject(mXIn, mYIn, ticks);
		//System.out.println("selected obj: " + getSelectedObject());
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object.equals(select)) {
			if (selectionList.getCurrentLine() != null && selectionList.getCurrentLine().getStoredObj() != null) {
				selectedObject = selectionList.getCurrentLine().getStoredObj();
				if (selectedObject instanceof StorageBox) {
					try {
						GuiOpener.openGui((Class)((StorageBox) selectedObject).getObject());
					} catch (Exception e) { e.printStackTrace(); }
				}
			}
			close();
		}
		if (object.equals(cancelSel)) { close(); }
	}
}
