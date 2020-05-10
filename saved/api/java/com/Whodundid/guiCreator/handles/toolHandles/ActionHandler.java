package com.Whodundid.guiCreator.handles.toolHandles;

import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton3Stage;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiCheckBox;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiTextField;
import com.Whodundid.guiCreator.gui.CreatorGui;
import com.Whodundid.guiCreator.gui.creatorParts.designSpace.CreatorDesignSpace;
import com.Whodundid.guiCreator.handles.TypeHandler;
import com.Whodundid.guiCreator.util.CreatorTool;

public class ActionHandler extends TypeHandler {

	public ActionHandler(CreatorGui guiIn) {
		super(guiIn);
	}

	@Override
	public void handlePress(int x, int y, int button) {
		
		//get the color
		int color = parentCreator.getColorPicker().getMainColor();
		
		switch (parentCreator.getCurrentTool()) {
		case BUTTON: buttonPress(x, y, button, color); break;
		case BUTTON3: button3Press(x, y, button, color); break;
		case CHECKBOX: checkboxPress(x, y, button, color); break;
		case RADIOBUTTON: radioPress(x, y, button, color); break;
		case SLIDER: sliderPress(x, y, button, color); break;
		case TEXTFIELD: textfieldPress(x, y, button, color); break;
		default: break;
		}
	}

	@Override
	public void handleRelease(int x, int y, int button) {
		switch (parentCreator.getCurrentTool()) {
		case BUTTON: buttonRelease(x, y, button); break;
		case BUTTON3: button3Release(x, y, button); break;
		case CHECKBOX: checkboxRelease(x, y, button); break;
		case RADIOBUTTON: radioRelease(x, y, button); break;
		case SLIDER: sliderRelease(x, y, button); break;
		case TEXTFIELD: textfieldRelease(x, y, button); break;
		default: break;
		}
	}
	
	//---------------------
	//ActionHandler Methods
	//---------------------
	
	//-----
	//Press
	//-----
	
	private void buttonPress(int x, int y, int button, int color) {
		CreatorDesignSpace s = parentCreator.getDesignSpace();
		EGuiButton o = new EGuiButton(parentCreator.getDesignSpace(), x - 10, y - 7, 60, 20);
		o.setDrawStretched(false);
		o.setMinDims(5, 5);
		o.setMaxDims(180, 20);
		
		parentCreator.setCreatingObject(o);
	}
	
	private void button3Press(int x, int y, int button, int color) {
		EGuiButton3Stage o = new EGuiButton3Stage(parentCreator.getDesignSpace(), x - 10, y - 7, 60, 20);
		o.setDrawStretched(false);
		o.setMinDims(5, 5);
		o.setMaxDims(180, 20);
		
		parentCreator.setCreatingObject(o);
	}

	private void checkboxPress(int x, int y, int button, int color) {
		EGuiCheckBox o = new EGuiCheckBox(parentCreator.getDesignSpace(), x - 5, y - 5, 20, 20);
		o.setMinDims(5, 5);
		o.setMaxDims(180, 20);
		
		parentCreator.setCreatingObject(o);
	}
	
	private void radioPress(int x, int y, int button, int color) {
		/*
		EGuiButton3Stage o = new EGuiButton3Stage(parentCreator.getDesignSpace(), x - 10, y - 7, 60, 20);
		o.setMinDims(5, 5);
		o.setMaxDims(180, 20);
		
		parentCreator.setCreatingObject(o);
		*/
	}
	
	private void sliderPress(int x, int y, int button, int color) {
		/*
		EGuiButton3Stage o = new EGuiButton3Stage(parentCreator.getDesignSpace(), x - 10, y - 7, 60, 20);
		o.setMinDims(5, 5);
		o.setMaxDims(180, 20);
		
		parentCreator.setCreatingObject(o);
		*/
	}
	
	private void textfieldPress(int x, int y, int button, int color) {
		EGuiTextField o = new EGuiTextField(parentCreator.getDesignSpace(), x - 10, y - 7, 60, 20);
		o.setMinDims(5, 5);
		o.setMaxDims(180, 20);
		
		parentCreator.setCreatingObject(o);
	}
	
	//-------
	//Release
	//-------
	
	private void buttonRelease(int x, int y, int button) {
		parentCreator.setCurrentTool(CreatorTool.MOVE);
	}
	
	private void button3Release(int x, int y, int button) {
		parentCreator.setCurrentTool(CreatorTool.MOVE);
	}

	private void checkboxRelease(int x, int y, int button) {
		parentCreator.setCurrentTool(CreatorTool.MOVE);
	}
	
	private void radioRelease(int x, int y, int button) {
		parentCreator.setCurrentTool(CreatorTool.MOVE);
	}
	
	private void sliderRelease(int x, int y, int button) {
		parentCreator.setCurrentTool(CreatorTool.MOVE);
	}
	
	private void textfieldRelease(int x, int y, int button) {
		parentCreator.setCurrentTool(CreatorTool.MOVE);
	}

}