package com.Whodundid.guiCreator.gui.creatorParts.toolbelt.toolbeltParts;

import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.guiCreator.gui.creatorParts.toolbelt.CreatorToolBelt;
import com.Whodundid.guiCreator.util.CreatorResources;
import com.Whodundid.guiCreator.util.CreatorTool;

public class CreatorToolButton extends EGuiButton {
	
	public CreatorToolBelt toolBelt;
	public CreatorCategory category;
	public CreatorTool tool;
	
	public CreatorToolButton(CreatorToolBelt guiIn, CreatorCategory catIn, CreatorTool toolIn) {
		super(guiIn, 0, 0, 0, 0);
		toolBelt = guiIn;
		category = catIn;
		tool = toolIn;
		
		assignResource();
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		super.drawObject(mXIn, mYIn);
		
		//draw selection border if current tool
		if (toolBelt != null && toolBelt.getParentCreator() != null) {
			if (tool == toolBelt.getParentCreator().getCurrentTool()) {
				int color = 0xff00abab;
				drawHRect(startX - 1, startY - 1, endX + 1, endY + 1, 1, color);
			}
		}
	}
	
	public CreatorTool getType() { return tool; }
	public CreatorCategory getCategory() { return category; }
	
	private void assignResource() {
		switch (tool) {
		//creator
		case SELECT: setButtonTexture(CreatorResources.select); setHoverText("Select"); break;
		case MOVE: setButtonTexture(CreatorResources.move); setHoverText("Move"); break;
		case RESIZE: setButtonTexture(CreatorResources.resize); setHoverText("Resize"); break;
		case EYEDROPPER: setButtonTexture(CreatorResources.eyedropper); setHoverText("Eye Dropper"); break;
		//shape
		case LINE: setButtonTexture(CreatorResources.line); setHoverText("Line"); break;
		case SQUARE: setButtonTexture(CreatorResources.square); setHoverText("Square"); break;
		case CIRCLE: setButtonTexture(CreatorResources.circle); setHoverText("Circle"); break;
		//action
		case BUTTON: setButtonTexture(CreatorResources.btn); setHoverText("Button"); break;
		case BUTTON3: setButtonTexture(CreatorResources.btn3); setHoverText("3 Stage Button"); break;
		case CHECKBOX: setButtonTexture(CreatorResources.check); setHoverText("Checkbox"); break;
		case RADIOBUTTON: setButtonTexture(CreatorResources.radio); setHoverText("Radio Button"); break;
		case SLIDER: setButtonTexture(CreatorResources.slider); setHoverText("Slider"); break;
		case TEXTFIELD: setButtonTexture(CreatorResources.textbox); setHoverText("Textbox"); break;
		//basic
		case CONTAINER: setButtonTexture(CreatorResources.container); setHoverText("Container"); break;
		case IMAGEBOX: setButtonTexture(CreatorResources.imgbox); setHoverText("Image Box"); break;
		case LABEL: setButtonTexture(CreatorResources.label); setHoverText("Label"); break;
		//advanced
		case DROPDOWNLIST: setButtonTexture(CreatorResources.dropdown); setHoverText("Drop Down List"); break;
		case HEADER: setButtonTexture(CreatorResources.header); setHoverText("Header"); break;
		case SCROLLLIST: setButtonTexture(CreatorResources.list); setHoverText("Scrollable List"); break;
		case TEXTAREA: setButtonTexture(CreatorResources.tlist); setHoverText("Scrollable Text List"); break;
		//util
		case PLAYERVIEWER: setButtonTexture(CreatorResources.pviewer); setHoverText("Player Viewer"); break;
		case PROGRESSBAR: setButtonTexture(CreatorResources.progress); setHoverText("Progress Bar"); break;
		default: break;
		}
	}
}
