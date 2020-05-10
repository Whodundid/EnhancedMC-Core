package com.Whodundid.autoCorrect;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiTextField;
import com.Whodundid.core.enhancedGui.types.WindowParent;

//Last edited: Dec 2, 2018
//First Added: Dec 2, 2018
//Author: Hunter Bragg

public class AutoCorrectCreatorGui extends WindowParent {

	AutoCorrectApp man = (AutoCorrectApp) RegisteredApps.getApp(AppType.AUTOCORRECT);
	EGuiTextField commandName;
	EGuiButton create, modify, cancel;
	
	public AutoCorrectCreatorGui() {
		super();
		aliases.add("autocorrect");
	}
	
	@Override
	public void initGui() {
		super.initGui();
		defaultPos();
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		commandName = new EGuiTextField(this, midX + 10, midY - 105, 180, 20);
		create = new EGuiButton(this, midX + 10, midY + 98, 70, 22, "Create");
		cancel = new EGuiButton(this, midX - 190, midY + 98, 70, 22, "Delete");
		
		addObject(commandName, create, cancel);
	}
	
	@Override
	public void drawObject(int mX, int mY) {
		drawDefaultBackground();
		super.drawObject(mX, mY);
	}
	
	public void createCommand() {
		
	}
	
}
