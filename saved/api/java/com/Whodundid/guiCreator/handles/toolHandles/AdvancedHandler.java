package com.Whodundid.guiCreator.handles.toolHandles;

import com.Whodundid.guiCreator.gui.CreatorGui;
import com.Whodundid.guiCreator.handles.TypeHandler;

public class AdvancedHandler extends TypeHandler {

	public AdvancedHandler(CreatorGui guiIn) {
		super(guiIn);
	}

	@Override
	public void handlePress(int x, int y, int button) {
		
		//get the color
		int color = parentCreator.getColorPicker().getMainColor();
		
		switch (parentCreator.getCurrentTool()) {
		case SCROLLLIST: listPress(x, y, button, color); break;
		case TEXTAREA: tlistPress(x, y, button, color); break;
		case DROPDOWNLIST: dropdownPress(x, y, button, color); break;
		case PLAYERVIEWER: playerviewerPress(x, y, button, color); break;
		case PROGRESSBAR: progressPress(x, y, button, color); break;
		default: break;
		}
	}

	@Override
	public void handleRelease(int x, int y, int button) {
		switch (parentCreator.getCurrentTool()) {
		case BUTTON: listRelease(x, y, button); break;
		case BUTTON3: tlistRelease(x, y, button); break;
		case CHECKBOX: dropdownRelease(x, y, button); break;
		case RADIOBUTTON: playerviewerRelease(x, y, button); break;
		case SLIDER: progressRelease(x, y, button); break;
		default: break;
		}
	}
	
	//-----------------------
	//AdvancedHandler Methods
	//-----------------------
	
	//-----
	//Press
	//-----
	
	private void listPress(int x, int y, int button, int color) {
		
	}
	
	private void tlistPress(int x, int y, int button, int color) {
		
	}

	private void dropdownPress(int x, int y, int button, int color) {
		
	}
	
	private void playerviewerPress(int x, int y, int button, int color) {
		
	}
	
	private void progressPress(int x, int y, int button, int color) {
		
	}
	
	//-------
	//Release
	//-------
	
	private void listRelease(int x, int y, int button) {
		
	}
	
	private void tlistRelease(int x, int y, int button) {
		
	}

	private void dropdownRelease(int x, int y, int button) {
		
	}
	
	private void playerviewerRelease(int x, int y, int button) {
		
	}
	
	private void progressRelease(int x, int y, int button) {
		
	}
	
}
