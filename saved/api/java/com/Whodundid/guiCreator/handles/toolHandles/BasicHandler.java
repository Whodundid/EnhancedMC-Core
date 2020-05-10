package com.Whodundid.guiCreator.handles.toolHandles;

import com.Whodundid.guiCreator.gui.CreatorGui;
import com.Whodundid.guiCreator.handles.TypeHandler;

public class BasicHandler extends TypeHandler {

	public BasicHandler(CreatorGui guiIn) {
		super(guiIn);
	}

	@Override
	public void handlePress(int x, int y, int button) {
		
		//get the color
		int color = parentCreator.getColorPicker().getMainColor();
		
		switch (parentCreator.getCurrentTool()) {
		case SCROLLLIST: headerPress(x, y, button, color); break;
		case TEXTAREA: containerPress(x, y, button, color); break;
		case DROPDOWNLIST: imagePress(x, y, button, color); break;
		case PLAYERVIEWER: labelPress(x, y, button, color); break;
		default: break;
		}
	}

	@Override
	public void handleRelease(int x, int y, int button) {
		switch (parentCreator.getCurrentTool()) {
		case BUTTON: headerRelease(x, y, button); break;
		case BUTTON3: containerRelease(x, y, button); break;
		case CHECKBOX: imageRelease(x, y, button); break;
		case RADIOBUTTON: labelRelease(x, y, button); break;
		default: break;
		}
	}
	
	//--------------------
	//BasicHandler Methods
	//--------------------
	
	//-----
	//Press
	//-----
	
	private void headerPress(int x, int y, int button, int color) {
		
	}
	
	private void containerPress(int x, int y, int button, int color) {
		
	}

	private void imagePress(int x, int y, int button, int color) {
		
	}
	
	private void labelPress(int x, int y, int button, int color) {
		
	}
	
	//-------
	//Release
	//-------
	
	private void headerRelease(int x, int y, int button) {
		
	}
	
	private void containerRelease(int x, int y, int button) {
		
	}

	private void imageRelease(int x, int y, int button) {
		
	}
	
	private void labelRelease(int x, int y, int button) {
		
	}
	
}
