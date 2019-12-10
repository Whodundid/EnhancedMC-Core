package com.Whodundid.core.coreSubMod;

import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiContainer;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiScrollList;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubModType;
import com.Whodundid.core.util.storageUtil.EDimension;

public class CoreSettingsGui extends WindowParent {
	
	EnhancedMCMod mod = (EnhancedMCMod) RegisteredSubMods.getMod(SubModType.CORE);
	EGuiScrollList list;
	EGuiContainer functionality, visual, debug;
	EGuiButton menuOverride, drawChat, showIncompats, showTerminal, useDebugKey;
	EGuiLabel menuLabel, drawChatLabel, incompatLabel, terminalLabel, debugLabel;
	
	@Override
	public void initGui() {
		setObjectName("EMC Core Mod Settings");
		setDimensions(startX, startY, defaultWidth, defaultHeight);
		setMinimumDims(170, 75);
		setResizeable(true);
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		setHeader(new EGuiHeader(this));
		
		int h = (height - 24) / 3;
		int diff = (height - 24) % 3;
		
		functionality = new EGuiContainer(this, startX + 2, startY + 20, width - 4, h);
		visual = new EGuiContainer(this, startX + 2, functionality.endY + 1, width - 4, h);
		debug = new EGuiContainer(this, startX + 2, visual.endY + 1, width - 4, h + diff);
		
		functionality.setTitle("Functionality");
		visual.setTitle("Visual");
		debug.setTitle("Debug");
		
		EDimension fd = functionality.getDimensions();
		EDimension vd = visual.getDimensions();
		EDimension dd = debug.getDimensions();
		
		EGuiScrollList fl = new EGuiScrollList(functionality, fd.startX, fd.startY + 17, fd.width, fd.height - 17);
		menuOverride = new EGuiButton(fl, 6, 6, 60, 20).setTrueFalseButton(true, EnhancedMCMod.emcMenuOverride);
		menuLabel = new EGuiLabel(fl, menuOverride.endX + 10, menuOverride.startY + 6, "Override Pause Menu");
		fl.addObjectToList(menuOverride, menuLabel);
		fl.growListHeight(10 + menuOverride.height);
		fl.setListWidth(190);
		functionality.addObject(fl);
		
		EGuiScrollList vl = new EGuiScrollList(visual, vd.startX, vd.startY + 17, vd.width, vd.height - 17);
		drawChat = new EGuiButton(vl, 6, 6, 60, 20).setTrueFalseButton(true, EnhancedMCMod.drawChatOnGui);
		showIncompats = new EGuiButton(vl, 6, drawChat.endY + 5, 60, 20).setTrueFalseButton(true, EnhancedMCMod.showIncompats);
		drawChatLabel = new EGuiLabel(vl, drawChat.endX + 10, drawChat.startY + 6, "Draw Chat When Open");
		incompatLabel = new EGuiLabel(vl, showIncompats.endX + 10, showIncompats.startY + 6, "Display Incompatible Mods");
		vl.addObjectToList(drawChat, showIncompats, drawChatLabel, incompatLabel);
		vl.growListHeight(58);
		vl.setListWidth(210);
		visual.addObject(vl);
		
		EGuiScrollList dl = new EGuiScrollList(functionality, dd.startX, dd.startY + 17, dd.width, dd.height - 17);
		showTerminal = new EGuiButton(dl, 6, 6, 60, 20).setTrueFalseButton(true, EnhancedMCMod.enableTerminal);
		useDebugKey = new EGuiButton(dl, 6, showTerminal.endY + 5, 60, 20).setTrueFalseButton(true, EnhancedMCMod.useDebugKey);
		terminalLabel = new EGuiLabel(dl, showTerminal.endX + 10, showTerminal.startY + 6, "Enable EMC Terminal");
		debugLabel = new EGuiLabel(dl, useDebugKey.endX + 10, useDebugKey.startY + 6, "Use Debug Key");
		dl.addObjectToList(showTerminal, useDebugKey, terminalLabel, debugLabel);
		dl.growListHeight(58);
		dl.setListWidth(180);
		debug.addObject(dl);
		
		menuOverride.setActionReciever(this);
		drawChat.setActionReciever(this);
		showIncompats.setActionReciever(this);
		showTerminal.setActionReciever(this);
		useDebugKey.setActionReciever(this);
		
		menuLabel.setDisplayStringColor(0xb2b2b2);
		drawChatLabel.setDisplayStringColor(0xb2b2b2);
		incompatLabel.setDisplayStringColor(0xb2b2b2);
		terminalLabel.setDisplayStringColor(0xb2b2b2);
		debugLabel.setDisplayStringColor(0xb2b2b2);
		
		menuLabel.setHoverText("Replaces Minecraft's pause menu with Enhanced MC's pause menu.");
		drawChatLabel.setHoverText("Allows chat messages to be drawn when windows are open.");
		incompatLabel.setHoverText("Displays incompatible EMC sub mods within the main settings window.");
		terminalLabel.setHoverText("Terminal used for debug and advanced purposes.");
		
		//functionality.addObject(menuOverride, menuLabel);
		//visual.addObject(drawChat, showIncompats, drawChatLabel, incompatLabel);
		//debug.addObject(showTerminal, useDebugKey, terminalLabel, debugLabel);
		
		//list.growListHeight(2 + functionality.height);
		//list.growListHeight(2 + visual.height);
		//list.growListHeight(debug.height);
		
		//int w = list.getListDimensions().width - 2;
		
		addObject(functionality, visual, debug);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		drawDefaultBackground();
		
		drawRect(startX + 1, startY + 19, endX - 1, endY - 1, -0x00cfcfcf); //grey background
		drawRect(startX, startY + 18, endX, startY + 19, 0xff000000); //top line
		
		drawCenteredStringWithShadow("Enhanced MC Settings", midX, startY + 6, 0xffbb00);
		
		super.drawObject(mXIn, mYIn, ticks);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object == menuOverride) { menuOverride.toggleTrueFalse(mod.emcMenuOverride, mod, false); }
		if (object == drawChat) { drawChat.toggleTrueFalse(mod.drawChatOnGui, mod, false); }
		if (object == showIncompats) { showIncompats.toggleTrueFalse(mod.showIncompats, mod, false); }
		if (object == showTerminal) { showTerminal.toggleTrueFalse(mod.enableTerminal, mod, false); }
		if (object == useDebugKey) { useDebugKey.toggleTrueFalse(mod.useDebugKey, mod, false); }
	}
}
