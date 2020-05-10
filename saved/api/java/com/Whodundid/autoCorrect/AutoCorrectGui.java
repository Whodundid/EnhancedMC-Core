package com.Whodundid.autoCorrect;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiTextField;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.EGuiTextArea;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.TextAreaLine;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;

//Last edited: Oct 22, 2018
//First Added: Oct 14, 2018
//Author: Hunter Bragg

public class AutoCorrectGui extends WindowParent {

	AutoCorrectApp man = (AutoCorrectApp) RegisteredApps.getApp(AppType.AUTOCORRECT);
	EGuiTextField commandName;
	EGuiTextArea commandList, aliasList;
	EGuiButton newCommand, deleteCommand, reloadCommands, saveCommands, edit;
	AutoCorrectCommand selectedCommand = null;
	
	public AutoCorrectGui() { super(); }
	public AutoCorrectGui(Object oldGuiIn) { super(oldGuiIn); }
	public AutoCorrectGui(IEnhancedGuiObject parentIn) { super(parentIn); }
	public AutoCorrectGui(IEnhancedGuiObject parentIn, Object oldGuiIn) { super(parentIn, oldGuiIn); }
	public AutoCorrectGui(IEnhancedGuiObject parentIn, int posX, int posY) { super(parentIn, posX, posY); }
	public AutoCorrectGui(IEnhancedGuiObject parentIn, int posX, int posY, Object oldGuiIn) { super(parentIn, posX, posY, oldGuiIn); }
	
	@Override
	public void initGui() {
		super.initGui();
		centerObjectWithSize(400, 256);
		setObjectName("AutoCorrect Menu");
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		try {
			newCommand = new EGuiButton(this, midX + 10, midY + 98, 90, 22, "Create New");
			deleteCommand = new EGuiButton(this, midX - 190, midY + 98, 70, 22, "Delete");
			reloadCommands = new EGuiButton(this, midX - 200, midY + 133, 110, 22, "Reload All Commands");
			saveCommands = new EGuiButton(this, midX - 80, midY + 133, 110, 22, "Save All Commands");
			
			commandName = new EGuiTextField(this, midX + 10, midY - 105, 180, 20);
			
			commandList = new EGuiTextArea(this, midX - 190, midY - 106, 180, 196, false).setDrawLineNumbers(true);
			aliasList = new EGuiTextArea(this, midX + 10, midY - 62, 180, 152, false).setDrawLineNumbers(true);
			
			addObject(newCommand, deleteCommand, reloadCommands, saveCommands);
			addObject(commandName, commandList, aliasList);
			
			for (AutoCorrectCommand c : man.getCommandList()) { commandList.addTextLine(c.getBaseCommand(), 0x00ff00, c); }
			
			if (commandList.getTextDocument().size() > 0) {
				TextAreaLine l = commandList.getTextLine(1);
				if (l != null) {
					buildLists((AutoCorrectCommand) commandList.getTextLine(1).getStoredObj());
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	@Override
	public void drawObject(int mX, int mY) {
		drawDefaultBackground();
		//mc.renderEngine.bindTexture(Resources.guiBase);
		//drawTexturedModalRect(midX - gWidth, midY - gHeight / 2, 0, 0, gWidth, gHeight);
		//drawTexturedModalRect(midX, midY - gHeight / 2, 56, 0, gWidth, gHeight);
		//drawTexturedModalRect(midX - 1, midY - gHeight / 2, 0, 0, 2, gHeight);
		drawCenteredString("Registered AutoCorrect Commands", midX - 100, midY - 120, 0xFFFFFF);
		drawString("Command Name:", midX + 10, midY - 120, 0xFFFFFF);
		drawString("Aliases", midX + 10, midY - 77, 0xFFFFFF);
		
		if (commandList.getCurrentLine() != null && commandList.getCurrentLine().getStoredObj() != null) {
			if (!commandList.getCurrentLine().getStoredObj().equals(selectedCommand)) {
				selectedCommand = (AutoCorrectCommand) commandList.getCurrentLine().getStoredObj();
				buildLists(selectedCommand);
			}
		}
		
		super.drawObject(mX, mY);
	}
	
	private void buildLists(AutoCorrectCommand cmdIn) {
		if (cmdIn != null && commandName != null && aliasList != null) {
			commandName.setText(cmdIn.getBaseCommand());
			aliasList.clear();
			for (String s : cmdIn.getAliases()) { aliasList.addTextLine(s).setLineNumberColor(0xffaa00); }
			//aliasList.setDocumentVerticalPos(0);
		}
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		
		if (object.equals(deleteCommand)) {
			if (commandList.getCurrentLine() != null) {
				AutoCorrectCommand cmd = (AutoCorrectCommand)commandList.getCurrentLine().getStoredObj();
				if (cmd != null) {
					man.unloadCommandFromRegistry(cmd);
					man.removeCommand(cmd);
					commandList.clear();
					aliasList.clear();
					for (AutoCorrectCommand c : man.getCommandList()) { commandList.addTextLine(c.getBaseCommand(), 0x00ff00, c); }
					//commandList.setDocumentVerticalPos(0);
					commandName.setText("");
					if (commandList.getTextDocument().size() > 0) {
						//commandList.getTextLine(0); //this would break it..
						//buildLists((AutoCorrectCommand)commandList.getTextLine(0).getStoredObj());
					}
					man.saver.saveCommands();
				}
			}
		}
		if (object.equals(reloadCommands)) {
			man.unloadAllCommandsFromRegistry();
			man.commands.clear();
			commandList.clear();
			aliasList.clear();
			man.loader.loadCommands();
			man.registerCommands();
			for (AutoCorrectCommand c : man.getCommandList()) { commandList.addTextLine(c.getBaseCommand(), 0x00ff00, c); }
			//commandList.setDocumentVerticalPos(0);
			commandName.setText("");
			//if (commandList.getTextDocument().size() > 0) { buildLists((AutoCorrectCommand)commandList.getTextLineWithLineNumber(0).getStoredObj()); }
		}
		if (object.equals(saveCommands)) { man.saver.saveCommands(); }
		
	}
}
