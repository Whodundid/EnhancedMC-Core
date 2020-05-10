package com.Whodundid.hotkeys.hotkKeyGuis;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiObjects.miscObjects.NotYetDialogueBox;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;

//Last edited: Dec 21, 2018
//First Added: Dec 21, 2018
//Author: Hunter Bragg

public class HotKeyGuiMain extends WindowParent {
	
	EGuiButton creator, keyList, keyVisual, settings;
	
	public HotKeyGuiMain() { super(); }
	public HotKeyGuiMain(Object oldGuiIn) { super(oldGuiIn); }
	public HotKeyGuiMain(IEnhancedGuiObject parentIn) { super(parentIn); }
	public HotKeyGuiMain(IEnhancedGuiObject parentIn, Object oldGuiIn) { super(parentIn, oldGuiIn); }
	public HotKeyGuiMain(IEnhancedGuiObject parentIn, int posX, int posY) { super(parentIn, posX, posY); }
	public HotKeyGuiMain(IEnhancedGuiObject parentIn, int posX, int posY, Object oldGuiIn) { super(parentIn, posX, posY, oldGuiIn); }
	
	@Override
	public void initGui() {
		setObjectName("Hotkey Menu");
		centerObjectWithSize(defaultWidth, defaultHeight);
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		setHeader(new EGuiHeader(this));
		
		creator = new EGuiButton(this, midX - 75, midY + 20, 150, 20, "Hotkey Creator");
		keyList = new EGuiButton(this, midX - 75, creator.endY + 3, 150, 20, "Hotkey List");
		keyVisual = new EGuiButton(this, midX - 75, keyList.endY + 3, 150, 20, "Registered Hotkey Visual");
		settings = new EGuiButton(this, midX - 75, keyVisual.endY + 3, 150, 20, "Settings");
		
		addObject(creator, keyList, keyVisual, settings);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		
		drawRect(startX + 6, startY + 6, endX - 6, endY - 6, 0xff000000);
		drawRect(startX + 7, startY + 7, endX - 7, endY - 7, 0xff303030);
		
		//drawRect(startX + 16, midY + 29, endX - 16, endY - 6, 0xff000000);
		//drawRect(startX + 17, midY + 30, endX - 17, endY - 7, 0xff202020);
		
		drawCenteredStringWithShadow("This is the main HotKey Gui.", midX, startY + 35, 0xffd800);
		
		drawCenteredStringWithShadow("Hotkeys can be created", midX, startY + 57, 0xffd800);
		drawCenteredStringWithShadow("and modified here.", midX, startY + 69, 0xffd800);
		
		drawCenteredStringWithShadow("Select an option below to", midX, startY + 91, 0xffd800);
		drawCenteredStringWithShadow("access a specific hotkey menu.", midX, startY + 103, 0xffd800);
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object.equals(creator)) { EnhancedMC.displayWindow(new HotKeyCreatorGui(), this); }
		if (object.equals(keyList)) { EnhancedMC.displayWindow(new HotKeyListGui(), this); }
		if (object.equals(keyVisual)) { addObject(new NotYetDialogueBox(this)); }
		if (object.equals(settings)) { EnhancedMC.displayWindow(new HotKeySettingsGui(), this); }
	}
}
