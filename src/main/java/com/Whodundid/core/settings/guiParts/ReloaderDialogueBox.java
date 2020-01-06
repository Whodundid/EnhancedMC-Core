package com.Whodundid.core.settings.guiParts;

import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.EGuiTextArea;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiDialogueBox;
import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.subMod.SubModSettings;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import net.minecraft.util.EnumChatFormatting;

//Author: Hunter Bragg

public class ReloaderDialogueBox extends EGuiDialogueBox {

	EArrayList<SubMod> reloadedMods = new EArrayList();
	StorageBoxHolder<SubMod, Reason> failedMods = new StorageBoxHolder();
	EGuiTextArea display;
	
	public ReloaderDialogueBox(EArrayList<SubMod> modsIn) {
		init(EnhancedMCRenderer.getInstance(), -1, -1, 200, 242);
		
		requestFocus();
		getTopParent().setFocusLockObject(this);
		
		setResizeable(true);
		setMinDims(150, 49);
		
		SubModSettings.loadConfig();
		
		for (SubMod m : modsIn) {
			boolean load = m.getConfig().loadAllConfigs();
			boolean save = m.getConfig().saveAllConfigs();
			if (load && save) { reloadedMods.add(m); }
			else if (load && !save) { failedMods.add(m, Reason.Saving); }
			else if (save && !load) { failedMods.add(m, Reason.Loading); }
			else { failedMods.add(m, Reason.In_General); }
		}
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		getHeader().setTitle("Reloading SubMods").setTitleColor(0xb2b2b2);
		
		display = new EGuiTextArea(this, startX + 5, startY + 5, width - 10, height - 35);
		display.setResetDrawn(false);

		display.addTextLine(EnumChatFormatting.YELLOW + "Reloading global config..");
		display.addTextLine();
		
		if (reloadedMods.isNotEmpty()) {
			display.addTextLine("Successfully reloaded mods:", 0x55ff55);
			for (SubMod m : reloadedMods) {
				display.addTextLine(EnumChatFormatting.GRAY + "    " + m.getName());
			}
			if (failedMods.isNotEmpty()) { display.addTextLine(); }
		}
		
		if (failedMods.isNotEmpty()) {
			display.addTextLine("Failed to fully reload mods:", 0xff5555);
			for (StorageBox<SubMod, Reason> box : failedMods) {
				display.addTextLine(EnumChatFormatting.GRAY + "    " + box.getObject().getName() + " : " + EnumChatFormatting.DARK_PURPLE + EnumChatFormatting.ITALIC + box.getValue().msg);
			}
		}
		
		okButton = new EGuiButton(this, midX - 25, endY - 25, 50, 20, "Ok") {
			@Override
			public void onPress() {
				playPressSound();
				parent.close();
			}
		};
		okButton.setRunActionOnPress(true);
		addObject(okButton.setZLevel(1));
		
		addObject(display);
	}
		
	private enum Reason {
		Saving("Saving Config"),
		Loading("Loading Config"),
		In_General("Unknown Error!");
		
		public String msg = "";
		
		private Reason(String reason) {
			msg = reason;
		}
	}
}

