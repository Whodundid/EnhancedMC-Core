package com.Whodundid.scripts.guis;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;

//Last edited: Dec 16, 2018
//First Added: Dec 16, 2018
//Author: Hunter Bragg

public class ScriptMainGui extends WindowParent {
	
	EGuiButton creator, scriptList, taskManager;
	
	public ScriptMainGui() { super(); }
	public ScriptMainGui(Object oldGuiIn) { super(oldGuiIn); }
	public ScriptMainGui(IEnhancedGuiObject parentIn) { super(parentIn); }
	public ScriptMainGui(IEnhancedGuiObject parentIn, Object oldGuiIn) { super(parentIn, oldGuiIn); }
	public ScriptMainGui(IEnhancedGuiObject parentIn, int posX, int posY) { super(parentIn, posX, posY); }
	public ScriptMainGui(IEnhancedGuiObject parentIn, int posX, int posY, Object oldGuiIn) { super(parentIn, posX, posY, oldGuiIn); }
	
	@Override
	public void initGui() {
		defaultPos();
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		creator = new EGuiButton(this, midX - 75, midY - 100, 150, 20, "Script Creator");
		scriptList = new EGuiButton(this, midX - 75, midY - 75, 150, 20, "Script List");
		taskManager = new EGuiButton(this, midX - 75, midY - 50, 150, 20, "Script Task Manager");
		
		addObject(creator, scriptList, taskManager);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object.equals(creator)) { EnhancedMC.displayWindow(new ScriptCreatorGui(), this); }
		if (object.equals(scriptList)) { }
		if (object.equals(taskManager)) { EnhancedMC.displayWindow(new ScriptTaskManagerGui(), this); }
	}
}
