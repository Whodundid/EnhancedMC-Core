package com.Whodundid.core.subMod.gui;

import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.EGuiTextArea;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiRect;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiDialogueBox;
import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.util.storageUtil.StorageBox;
import net.minecraft.util.EnumChatFormatting;

//Author: Hunter Bragg

public class SubModInfoDialogueBox extends EGuiDialogueBox {

	protected SubMod mod;
	EGuiRect topLine;
	EGuiButton okButton;
	EGuiLabel modName, modAuthor, version, versionDate, incompatible, dependencies;
	
	public SubModInfoDialogueBox(SubMod modIn) {
		init(EnhancedMCRenderer.getInstance());
		centerObjectWithSize(165, 202);
		mInit(modIn);
	}
	public SubModInfoDialogueBox(int xPos, int yPos, int width, int height, SubMod modIn) {
		init(EnhancedMCRenderer.getInstance(), xPos, yPos, width, height);
		mInit(modIn);
	}
	
	private void mInit(SubMod modIn) {
		mod = modIn;
		requestFocus();
		getTopParent().setFocusLockObject(this);
		bringToFront();
		setResizeable(true);
		setMinWidth(140).setMinHeight(160);
		setObjectName("Mod Info");
		setPinnable(false);
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		topLine = new EGuiRect(this, startX + 1, startY + 16, endX - 1, startY + 17, 0xff000000);
		addObject(topLine);
		
		okButton = new EGuiButton(this, midX - 25, endY - 27, 50, 20, "Ok") {
			{ setRunActionOnPress(true); }
			@Override
			public void onPress() {
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
		
		setTitle("Mod Info");
	}
}
