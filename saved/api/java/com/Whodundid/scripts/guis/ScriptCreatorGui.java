package com.Whodundid.scripts.guis;

import org.lwjgl.input.Keyboard;

import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiTextField;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.EGuiTextArea;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.storageUtil.EDimension;

//Last edited: Oct 18, 2018
//First Added: Oct 18, 2018
//Author: Hunter Bragg

public class ScriptCreatorGui extends WindowParent {

	EGuiTextArea explorer, writingArea, outputLog;
	EGuiButton compile, save, load, fileMenu, run, help, settings;
	
	public ScriptCreatorGui() { super(); }
	public ScriptCreatorGui(Object oldGuiIn) { super(oldGuiIn); }
	public ScriptCreatorGui(IEnhancedGuiObject parentIn) { super(parentIn); }
	public ScriptCreatorGui(IEnhancedGuiObject parentIn, Object oldGuiIn) { super(parentIn, oldGuiIn); }
	public ScriptCreatorGui(IEnhancedGuiObject parentIn, int posX, int posY) { super(parentIn, posX, posY); }
	public ScriptCreatorGui(IEnhancedGuiObject parentIn, int posX, int posY, Object oldGuiIn) { super(parentIn, posX, posY, oldGuiIn); }
	
	@Override
	public void initGui() {
		centerObjectWithSize(700, 365);
		super.initGui();
		
		Keyboard.enableRepeatEvents(true);
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		explorer = new EGuiTextArea(this, startX + 5, startY + 33, 140, 312);
		writingArea = new EGuiTextArea(this, startX + 149, startY + 33, 545, 212).setDrawLineNumbers(true);
		outputLog = new EGuiTextArea(this, startX + 149, startY + 252, 545, 93).setDrawLineNumbers(true);
		
		EGuiHeader writingHeader = new EGuiHeader(writingArea, false, 13).setDrawTitle(false);
		EDimension dim = writingHeader.getDimensions();
		writingHeader.addObject(new EGuiTextField(writingHeader, dim.startX + 3, dim.startY, 150, 13).setEnableBackgroundDrawing(true).setTextColor(0x55ff55));
		
		writingArea.addObject(writingHeader);
		explorer.addObject(new EGuiHeader(explorer, false, 13).setTitle("Script Explorer").setTitleColor(0xffbb00));
		
		run = new EGuiButton(this, startX + 624, startY + 2, 70, 17, "Run Script");
		save = new EGuiButton(this, startX + 5, startY + 2, 40, 17, "Save");
		load = new EGuiButton(this, startX + 47, startY + 2, 40, 17, "Load");
		
		addObject(explorer, writingArea, outputLog);
		addObject(run, save, load);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		
	}
}
