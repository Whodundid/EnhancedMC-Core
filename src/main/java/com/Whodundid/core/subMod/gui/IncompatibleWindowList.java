package com.Whodundid.core.subMod.gui;

import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.EGuiTextArea;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiRect;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiDialogueBox;
import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import net.minecraft.util.EnumChatFormatting;

public class IncompatibleWindowList extends EGuiDialogueBox {
	
	EGuiButton okButton;
	EGuiLabel problem;
	EGuiRect topLine;
	EGuiTextArea incompatibleList;
	int longest = 0;
	
	public IncompatibleWindowList() {
		init(EnhancedMCRenderer.getInstance());
		centerObjectWithSize(272, 200);
		mInit();
	}
	
	public IncompatibleWindowList(int xPos, int yPos, int width, int height) {
		init(EnhancedMCRenderer.getInstance(), xPos, yPos, width, height);
		mInit();
	}
	
	private void mInit() {
		requestFocus();
		getTopParent().setFocusLockObject(this);
		setZLevel(99999);
		setResizeable(true);
		setMinWidth(160).setMinHeight(101);
		setPinnable(false);
	}
	
	@Override
	public void initObjects() {
		setHeader(new EGuiHeader(this));
		
		okButton = new EGuiButton(this, midX - 25, endY - 27, 50, 20, "Ok") {
			@Override
			public void onPress() {
				playPressSound();
				parent.close();
			}
		};
		okButton.setRunActionOnPress(true);
		addObject(okButton.setZLevel(1));
		
		problem = new EGuiLabel(this, midX, startY + 6, "The following EMC SubMods are incompatible.");
		problem.enableWordWrap(true, width - 15).setLineGapHeight(3).enableShadow(true).setDrawCentered(true).setDisplayStringColor(0xffbb00);
		
		topLine = new EGuiRect(this, startX + 1, problem.startY + problem.getTextHeight() + 2, endX - 1, problem.startY + problem.getTextHeight() + 3, 0xff000000);
		addObject(topLine);
		
		incompatibleList = new EGuiTextArea(this, startX + 7, topLine.endY + 6, width - 14, okButton.startY - topLine.endY - 12).setDrawLineNumbers(true).setEditable(false);
		addObject(incompatibleList);
		
		EArrayList<SubMod> mods = RegisteredSubMods.getIncompatibleModsList();
		if (mods.isNotEmpty()) {
			for (SubMod s : mods) {
				StorageBoxHolder<SubMod, String> incompatMods = RegisteredSubMods.getModImcompatibility(s);
				
				incompatibleList.addTextLine(EnumChatFormatting.GRAY + s.getName() + " :");
				
				for (StorageBox<SubMod, String> box : incompatMods) {
					SubMod m = box.getObject();
					incompatibleList.addTextLine("   - " + EnumChatFormatting.RED + "requires " + EnumChatFormatting.YELLOW + 
												 m.getName() + EnumChatFormatting.RED + " version '" + box.getValue() + "'", 0xb2b2b2);
				}
				
				if (mods.indexOf(s) < mods.size() - 1) { incompatibleList.addTextLine(); }
			}
		} else { incompatibleList.addTextLine("None", 0xb2b2b2); }
		
		addObject(problem);
		
		setTitleColor(0xc2c2c2);
		setTitle("SubMod Incompatibility");
	}
}
