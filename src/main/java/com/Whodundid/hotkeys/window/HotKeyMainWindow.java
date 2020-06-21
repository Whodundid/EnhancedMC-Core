package com.Whodundid.hotkeys.window;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.header.WindowHeader;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.hotkeys.util.HKResources;

//Last edited: Dec 21, 2018
//First Added: Dec 21, 2018
//Author: Hunter Bragg

public class HotKeyMainWindow extends WindowParent {
	
	WindowButton creator, keyList, keyVisual, settings;
	
	public HotKeyMainWindow() {
		super();
		aliases.add("hotkeymain", "keymain", "hkmain", "hotkey");
		windowIcon = HKResources.icon;
	}
	
	@Override
	public void initWindow() {
		setObjectName("Hotkey Main Menu");
		defaultDims();
	}
	
	@Override
	public void initObjects() {
		setHeader(new WindowHeader(this));
		
		creator = new WindowButton(this, midX - 75, midY + 35, 150, 20, "Hotkey Creator");
		keyList = new WindowButton(this, midX - 75, creator.endY + 3, 150, 20, "Hotkey List");
		//keyVisual = new EGuiButton(this, midX - 75, keyList.endY + 3, 150, 20, "Registered Hotkey Visual");
		settings = new WindowButton(this, midX - 75, keyList.endY + 3, 150, 20, "Settings");
		
		creator.setStringColor(EColors.lime);
		keyList.setStringColor(EColors.seafoam);
		settings.setStringColor(EColors.yellow);
		
		addObject(creator, keyList, keyVisual, settings);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		
		drawRect(startX + 6, startY + 6, endX - 6, endY - 6, 0xff000000); //black border
		drawRect(startX + 7, startY + 7, endX - 7, endY - 7, 0xff292929); //inner back
		
		drawRect(creator.startX - 7, creator.startY - 7, creator.endX + 7, settings.endY + 7, 0xff000000);
		drawRect(creator.startX - 6, creator.startY - 6, creator.endX + 6, settings.endY + 6, 0xff202020);
		
		drawStringCS("Select an option below to", midX, midY, EColors.lorange);
		drawStringCS("access a specific hotkey menu.", midX, midY + 10, EColors.lorange);
		
		drawRect(midX - 48, midY - 108, midX + 48, midY - 12, EColors.black);
		drawTexture(midX - 45, midY - 105, 90, 90, HKResources.logo);
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object.equals(creator)) { EnhancedMC.displayWindow(new HotKeyCreatorWindow(), this); }
		if (object.equals(keyList)) { EnhancedMC.displayWindow(new HotKeyListWindow(), this); }
		//if (object.equals(keyVisual)) { EnhancedMC.displayWindow(new NotYetDialogueBox(this)); }
		if (object.equals(settings)) { EnhancedMC.displayWindow(new HotKeySettingsWindow(), this); }
	}
	
}
