package com.Whodundid.guiCreator.handles.toolHandles;

import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiEllipse;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLine;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiRect;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.guiCreator.gui.CreatorGui;
import com.Whodundid.guiCreator.handles.TypeHandler;
import com.Whodundid.guiCreator.util.CreatorTool;

public class ShapeHandler extends TypeHandler {

	public ShapeHandler(CreatorGui guiIn) {
		super(guiIn);
	}

	@Override
	public void handlePress(int x, int y, int button) {
		//get the color
		int color = parentCreator.getColorPicker().getMainColor();
		
		switch (parentCreator.getCurrentTool()) {
		case LINE: linePress(x, y, button, color); break;
		case SQUARE: squarePress(x, y, button, color); break;
		case CIRCLE: circlePress(x, y, button, color); break;
		default: break;
		}
	}

	@Override
	public void handleRelease(int x, int y, int button) {
		switch (parentCreator.getCurrentTool()) {
		case LINE: lineRelease(x, y, button); break;
		case SQUARE: squareRelease(x, y, button); break;
		case CIRCLE: circleRelease(x, y, button); break;
		default: break;
		}
	}

	//--------------------
	//ShapeHandler Methods
	//--------------------
	
	//-----
	//Press
	//-----
	
	private void linePress(int x, int y, int button, int color) {
		EGuiLine o = new EGuiLine(parentCreator.getDesignSpace(), x, y, x + 1, y + 1, color);
		
		parentCreator.setCreatingObject(o);
		parentCreator.setResizingObject(o);
		parentCreator.setResizingDir(ScreenLocation.botRight);
	}
	
	private void squarePress(int x, int y, int button, int color) {
		EGuiRect o = new EGuiRect(parentCreator.getDesignSpace(), x, y, x + 1, y + 1, color);
		
		parentCreator.setCreatingObject(o);
		parentCreator.setResizingObject(o);
		parentCreator.setResizingDir(ScreenLocation.botRight);
	}

	private void circlePress(int x, int y, int button, int color) {
		EGuiEllipse o = new EGuiEllipse(parentCreator.getDesignSpace(), x, y, x + 1, y + 1, color);
		
		parentCreator.setCreatingObject(o);
		parentCreator.setResizingObject(o);
		parentCreator.setResizingDir(ScreenLocation.botRight);
	}
	
	//-------
	//Release
	//-------
	
	private void lineRelease(int x, int y, int button) {
		parentCreator.setResizingObject(null);
		parentCreator.setResizingDir(ScreenLocation.out);
		parentCreator.setCurrentTool(CreatorTool.MOVE);
	}
	
	private void squareRelease(int x, int y, int button) {
		parentCreator.setResizingObject(null);
		parentCreator.setResizingDir(ScreenLocation.out);
		parentCreator.setCurrentTool(CreatorTool.MOVE);
	}

	private void circleRelease(int x, int y, int button) {
		parentCreator.setResizingObject(null);
		parentCreator.setResizingDir(ScreenLocation.out);
		parentCreator.setCurrentTool(CreatorTool.MOVE);
	}
	
}
