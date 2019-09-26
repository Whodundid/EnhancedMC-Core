package com.Whodundid.core.subMod.gui;

import com.Whodundid.core.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiDialogueBox;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiRect;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiTextArea;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.util.storageUtil.StorageBox;
import net.minecraft.util.EnumChatFormatting;

public class SubModInfoDialogueBox extends EGuiDialogueBox {

	protected SubMod mod;
	EGuiRect topLine;
	EGuiButton okButton;
	EGuiLabel modName, modAuthor, version, versionDate, incompatible, dependencies;
	
	public SubModInfoDialogueBox(IEnhancedGuiObject parentIn, SubMod modIn) {
		init(parentIn);
		centerObjectWithSize(165, 202);
		mod = modIn;
		requestFocus();
		getTopParent().setFocusLockObject(this);
		setZLevel(99999);
		setResizeable(true);
		setMinimumWidth(140).setMinimumHeight(160);
	}
	
	public SubModInfoDialogueBox(IEnhancedGuiObject parentIn, int xPos, int yPos, int width, int height, SubMod modIn) {
		init(parentIn, xPos, yPos, width, height);
		mod = modIn;
		requestFocus();
		getTopParent().setFocusLockObject(this);
		setZLevel(999999);
		setResizeable(true);
		setMinimumWidth(140).setMinimumHeight(160);
	}
	
	@Override
	public void initObjects() {
		this.setHeader(new EGuiHeader(this));
		
		topLine = new EGuiRect(this, startX + 1, startY + 16, endX - 1, startY + 17, 0xff000000);
		addObject(topLine);
		
		okButton = new EGuiButton(this, midX - 25, endY - 27, 50, 20, "Ok") {
			{ setRunActionOnPress(true); }
			@Override
			public void performAction() {
				playPressSound();
				parent.close();
			}
		};
		addObject(okButton.setZLevel(1));
		
		modName = new EGuiLabel(this, midX, startY + 5, mod.getName()).enableShadow(true).setDrawCentered(true).setDisplayStringColor(0xffbb00);
		modAuthor = new EGuiLabel(this, startX + 5, startY + 25, "Author: " + mod.getAuthor()).enableShadow(true).setDisplayStringColor(0xb2b2b2);
		version = new EGuiLabel(this, startX + 5, startY + 40, "Version: " + mod.getVersion()).enableShadow(true).setDisplayStringColor(0xb2b2b2);
		versionDate = new EGuiLabel(this, startX + 5, startY + 55, "Version Date: " + mod.getVersionDate()).enableShadow(true).setDisplayStringColor(0xb2b2b2);
		incompatible = new EGuiLabel(this, startX + 5, startY + 70, "Incompatible!").enableShadow(true).setDisplayStringColor(0xff5555);
		dependencies = new EGuiLabel(this, startX + 5, startY + (mod.isIncompatible() ? 85 : 70), "Dependencies:").enableShadow(true).setDisplayStringColor(0xb2b2b2);
		
		EGuiTextArea dependencyList = new EGuiTextArea(this, startX + 10, startY + (mod.isIncompatible() ? 100 : 85), width - 20, endY - startY - 130 + (mod.isIncompatible() ? -3 : 12));
		dependencyList.setDrawLineNumbers(true).setEditable(false);
		addObject(dependencyList);
		
		if (!mod.getDependencies().getObjects().isEmpty()) {
			for (StorageBox<String, String> box : mod.getDependencies()) {
				dependencyList.addTextLine(box.getObject() + EnumChatFormatting.YELLOW + " (" + box.getValue() + ")", 0x55ff55);
			}
		} else { dependencyList.addTextLine("None", 0xb2b2b2); }
		
		
		if (mod.isIncompatible()) { addObject(incompatible); }
		
		addObject(modName, modAuthor, version, versionDate, dependencies);
		
		setDisplayString("Mod Info");
	}
}
