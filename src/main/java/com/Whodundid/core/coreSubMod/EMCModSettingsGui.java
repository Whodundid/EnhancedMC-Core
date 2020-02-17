package com.Whodundid.core.coreSubMod;

import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton3Stage;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.scrollList.EGuiScrollList;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubModType;
import com.Whodundid.core.util.renderUtil.EColors;

//Author: Hunter Bragg

public class EMCModSettingsGui extends WindowParent {
	
	EMCMod mod = (EMCMod) RegisteredSubMods.getMod(SubModType.CORE);
	EGuiScrollList list;
	EGuiButton menuOverride, closeHudEmpty, showIncompats, drawProxyBorder, showTerminal;
	EGuiButton3Stage drawChat;
	
	public EMCModSettingsGui() {
		super();
		aliases.add("emcsettings", "enhancedmcs", "emcs");
	}
	
	@Override
	public void initGui() {
		setObjectName("EMC Core Mod Settings");
		setDimensions(defaultWidth, defaultHeight);
		setMinDims(170, 75);
		setResizeable(true);
	}
	
	@Override
	public void initObjects() {
		setHeader(new EGuiHeader(this));
		
		list = new EGuiScrollList(this, startX + 2, startY + 20, width - 4, height - 22);
		list.setBackgroundColor(0xff303030);
		
		int fY = addFunction(8);
		int vY = addVisual(fY);
		int dY = addDebug(vY);
		
		list.fitItemsInList(6, 6);
		
		addObject(list);
	}
	
	private int addFunction(int yPos) {
		//build objects
		
		EGuiLabel functionLabel = new EGuiLabel(list, 6, yPos, "Functionality", EColors.orange.c());
		
		menuOverride = new EGuiButton(list, 6, functionLabel.endY + 8, 60, 20, EMCMod.menuOverride);
		EGuiLabel menuLabel = new EGuiLabel(list, menuOverride.endX + 10, menuOverride.startY + 6, "Override Pause Menu");
		
		closeHudEmpty = new EGuiButton(list, 6, menuOverride.endY + 7, 60, 20, EMCMod.closeHudWhenEmpty);
		EGuiLabel closeEmptyLabel = new EGuiLabel(list, closeHudEmpty.endX + 10, closeHudEmpty.startY + 6, "Close Hud When Empty");
		
		//set values
		
		menuOverride.setActionReciever(this);
		closeHudEmpty.setActionReciever(this);
		
		menuLabel.setHoverText("Replaces Minecraft's pause menu with Enhanced MC's pause menu.");
		closeEmptyLabel.setHoverText("Closes the EMC hud if there are no windows or objects on screen.");
		menuLabel.setDisplayStringColor(0xb2b2b2);
		closeEmptyLabel.setDisplayStringColor(0xb2b2b2);
		
		//add objects
		
		list.addObjectToList(functionLabel);
		list.addObjectToList(menuOverride, menuLabel);
		list.addObjectToList(closeHudEmpty, closeEmptyLabel);
		//list.addObjectToList(keysOnHud, keysOnHudLabel);
		
		return closeHudEmpty.endY - list.getDimensions().startY;
	}
	
	private int addVisual(int yPos) {
		//build objects
		
		EGuiLabel visualLabel = new EGuiLabel(list, 6, yPos + 10, "Visual", EColors.orange.c());
		
		drawChat = new EGuiButton3Stage(list, 6, visualLabel.endY + 8, 60, 20, EMCMod.drawChatOnHud, EColors.yellow.intVal);
		EGuiLabel drawChatLabel = new EGuiLabel(list, drawChat.endX + 10, drawChat.startY + 6, "Draw Chat When Open");
		
		drawProxyBorder = new EGuiButton(list, 6, drawChat.endY + 7, 60, 20, EMCMod.drawHudBorder);
		EGuiLabel drawProxyBorderLabel = new EGuiLabel(list, drawProxyBorder.endX + 10, drawProxyBorder.startY + 6, "Draw Hud Border");
		
		showIncompats = new EGuiButton(list, 6, drawProxyBorder.endY + 7, 60, 20, EMCMod.showIncompats);
		EGuiLabel incompatLabel = new EGuiLabel(list, showIncompats.endX + 10, showIncompats.startY + 6, "Display Incompatible Mods");
		
		//set values
		
		drawChat.setActionReciever(this);
		drawProxyBorder.setActionReciever(this);
		showIncompats.setActionReciever(this);
		
		drawChatLabel.setHoverText("Allows chat messages to be drawn when windows are open.");
		drawProxyBorderLabel.setHoverText("Draws a red border on the screen whenever the EMC hud is open.");
		incompatLabel.setHoverText("Displays incompatible EMC sub mods within the main settings window.");
		drawChatLabel.setDisplayStringColor(0xb2b2b2);
		drawProxyBorderLabel.setDisplayStringColor(0xb2b2b2);
		incompatLabel.setDisplayStringColor(0xb2b2b2);
		
		//add objects
		
		list.addObjectToList(visualLabel);
		list.addObjectToList(drawChat, drawChatLabel);
		list.addObjectToList(drawProxyBorder, drawProxyBorderLabel);
		list.addObjectToList(showIncompats, incompatLabel);
		
		return showIncompats.endY - list.getDimensions().startY;
	}
	
	private int addDebug(int yPos) {
		//build objects
		
		EGuiLabel debugLabel = new EGuiLabel(list, 6, yPos + 10, "Debug", EColors.orange.c());
		
		showTerminal = new EGuiButton(list, 6, debugLabel.endY + 8, 60, 20, EMCMod.enableTerminal);
		EGuiLabel terminalLabel = new EGuiLabel(list, showTerminal.endX + 10, showTerminal.startY + 6, "Enable EMC Terminal");
		
		//set values
		
		showTerminal.setActionReciever(this);
		
		terminalLabel.setHoverText("Terminal used for debug and advanced purposes.");
		terminalLabel.setDisplayStringColor(0xb2b2b2);
		
		//add objects
		
		list.addObjectToList(debugLabel);
		list.addObjectToList(showTerminal, terminalLabel);
		
		return showTerminal.endY - list.getDimensions().startY;
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
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		//function
		if (object == menuOverride) { menuOverride.toggleTrueFalse(mod.menuOverride, mod, false); }
		if (object == closeHudEmpty) { closeHudEmpty.toggleTrueFalse(mod.closeHudWhenEmpty, mod, false); }
		//visual
		if (object == drawChat) { drawChat.updateDislay(mod.drawChatOnHud, mod, false); }
		if (object == drawProxyBorder) { drawProxyBorder.toggleTrueFalse(mod.drawHudBorder, mod, false); }
		if (object == showIncompats) { showIncompats.toggleTrueFalse(mod.showIncompats, mod, false); }
		//debug
		if (object == showTerminal) { showTerminal.toggleTrueFalse(mod.enableTerminal, mod, false); }
		
	}
}
