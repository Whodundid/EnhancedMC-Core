package com.Whodundid.guiCreator.handles.toolHandles;

import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.guiCreator.gui.CreatorGui;
import com.Whodundid.guiCreator.handles.TypeHandler;

public class CreatorHandler extends TypeHandler {

	private IEnhancedGuiObject obj = null;
	private ScreenLocation dir = ScreenLocation.out;
	
	public CreatorHandler(CreatorGui guiIn) {
		super(guiIn);
	}

	@Override
	public void handlePress(int x, int y, int button) {
		//get the object under the mouse
		EArrayList<IEnhancedGuiObject> listObjects = parentCreator.getDesignSpace().getListObjects();
		
		if (listObjects.isNotEmpty()) {
			for (int i = listObjects.size() - 1; i >= 0; i--) {
				IEnhancedGuiObject o = listObjects.get(i);
				
				if (o != null) {
					if (o.isMouseInside(x, y)) {
						
						//check if the mouse is inside a child object
						EArrayList<IEnhancedGuiObject> children = o.getAllChildren();
						for (int j = children.size() - 1; j >= 0; j--) {
							IEnhancedGuiObject c = children.get(j);
							
							if (c != null) {
								if (c.isMouseInside(x, y)) {
									obj = c;
									dir = c.getEdgeAreaMouseIsOn();
									break;
								}
							}
						}
						
						//if obj is still null at this point, then the mouse is not over a child
						if (obj == null) {
							obj = o;
							dir = o.getEdgeAreaMouseIsOn();
						}
						break;
					}
				}
			}
		}
		
		switch (parentCreator.getCurrentTool()) {
		case SELECT: selectPress(x, y, button); break;
		case MOVE: movePress(x, y, button); break;
		case RESIZE: resizePress(x, y, button); break;
		case EYEDROPPER: eyedropperPress(x, y, button); break;
		default: break;
		}
	}

	@Override
	public void handleRelease(int x, int y, int button) {
		switch (parentCreator.getCurrentTool()) {
		case SELECT: selectRelease(x, y, button); break;
		case MOVE: moveRelease(x, y, button); break;
		case RESIZE: resizeRelease(x, y, button); break;
		case EYEDROPPER: eyedropperRelease(x, y, button); break;
		default: break;
		}
		
		obj = null;
	}

	//----------------------
	//CreatorHandler methods
	//----------------------
	
	//-----
	//Press
	//-----
	
	private void selectPress(int x, int y, int button) {
		parentCreator.setSelectedObject(obj);
	}
	
	private void movePress(int x, int y, int button) {
		parentCreator.setSelectedObject(obj);
		parentCreator.setMovingObject(obj);
	}
	
	private void resizePress(int x, int y, int button) {
		parentCreator.setSelectedObject(obj);
		parentCreator.setResizingObject(obj);
		parentCreator.setResizingDir(dir);
	}
	
	private void eyedropperPress(int x, int y, int button) {
		
	}
	
	//-------
	//Release
	//-------
	
	private void selectRelease(int x, int y, int button) {
		//parentCreator.setSelectedObject(null);
	}
	
	private void moveRelease(int x, int y, int button) {
		IEnhancedGuiObject o = parentCreator.getMovingObject();
		o.setInitialPosition(o.getDimensions().startX, o.getDimensions().startY);
		
		parentCreator.setMovingObject(null);
	}
	
	private void resizeRelease(int x, int y, int button) {
		parentCreator.setResizingObject(null);
		parentCreator.setResizingDir(ScreenLocation.out);
	}
	
	private void eyedropperRelease(int x, int y, int button) {
		
	}
	
}
