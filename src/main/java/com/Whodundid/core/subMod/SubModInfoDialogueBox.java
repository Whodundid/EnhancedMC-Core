package com.Whodundid.core.subMod;

import com.Whodundid.core.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiDialogueBox;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiRect;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiTextArea;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedGuiObject;

public class SubModInfoDialogueBox extends EGuiDialogueBox {

	protected SubMod mod;
	
	public SubModInfoDialogueBox(IEnhancedGuiObject parentIn, int xPos, int yPos, int width, int height, SubMod modIn) {
		init(parentIn, xPos, yPos, width, height);
		mod = modIn;
		addObject(header = new EGuiHeader(this));
		requestFocus();
		getTopParent().setFocusLockObject(this);
		setDisplayString("Mod Info");
		setZLevel(2);
		
		EGuiRect topLine;
		
		topLine = new EGuiRect(this, startX + 1, startY + 16, endX - 1, startY + 17, 0xff000000);
		addObject(topLine);
		
		EGuiButton okButton;
		
		okButton = new EGuiButton(this, midX - 25, endY - 27, 50, 20, "Ok") {
			{ setRunActionOnPress(true); }
			@Override
			public void performAction() {
				playPressSound();
				parent.close();
			}
		};
		addObject(okButton.setZLevel(1));
		
		EGuiLabel modName, modAuthor, version, versionDate, incompatible, dependencies;
		
		modName = new EGuiLabel(this, midX, startY + 5, mod.getName()).enableShadow(true).setDrawCentered(true).setDisplayStringColor(0xffbb00);
		modAuthor = new EGuiLabel(this, startX + 5, startY + 25, "Author: " + mod.getAuthor()).enableShadow(true).setDisplayStringColor(0xb2b2b2);
		version = new EGuiLabel(this, startX + 5, startY + 40, "Version: " + mod.getVersion()).enableShadow(true).setDisplayStringColor(0xb2b2b2);
		versionDate = new EGuiLabel(this, startX + 5, startY + 55, "Version Date: " + mod.getVersionDate()).enableShadow(true).setDisplayStringColor(0xb2b2b2);
		incompatible = new EGuiLabel(this, startX + 5, startY + 70, "Incompatible with core!").enableShadow(true).setDisplayStringColor(0xff5555);
		dependencies = new EGuiLabel(this, startX + 5, startY + (mod.isIncompatible() ? 85 : 70), "Dependencies:").enableShadow(true).setDisplayStringColor(0xb2b2b2);
		
		EGuiTextArea dependencyList = new EGuiTextArea(this, startX + 10, startY + (mod.isIncompatible() ? 100 : 85), width - 20, (mod.isIncompatible() ? 70 : 84)).setDrawLineNumbers(true).setEditable(false);
		addObject(dependencyList);
		
		if (mod.getDependencies().getObjects().isNotEmpty()) {
			for (String t : mod.getDependencies().getObjects()) {
				dependencyList.addTextLine(t, 0x55ff55);
			}
		} else { dependencyList.addTextLine("None", 0xb2b2b2); }
		
		
		if (mod.isIncompatible()) { addObject(incompatible); }
		
		addObject(modName, modAuthor, version, versionDate, dependencies);
	}
}
