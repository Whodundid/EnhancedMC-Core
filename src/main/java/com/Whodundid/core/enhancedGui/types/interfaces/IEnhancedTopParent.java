package com.Whodundid.core.enhancedGui.types.interfaces;

import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.ObjectModifyType;
import com.Whodundid.core.util.miscUtil.ScreenLocation;

//Jan 1, 2019
//Last edited: Jan 21, 2019
//First Added: Dec 27, 2018
//Author: Hunter Bragg

/** An interface outlining behavior for Top Level Enhanced Gui Objects. Top level objects handle drawing, object focus, object manipulation, and inputs. */
public interface IEnhancedTopParent extends IEnhancedGuiObject {
	
	//draw order
	
	public IEnhancedTopParent bringObjectToFront(IEnhancedGuiObject objIn);
	public IEnhancedTopParent sendObjectToBack(IEnhancedGuiObject objIn);
	
	//hovering text
	public IEnhancedTopParent setObjectWithHoveringText(IEnhancedGuiObject objIn);
	public IEnhancedGuiObject getObjectWithHoveringText();
	
	//objects
	public IEnhancedGuiObject getHighestZLevelObject();
	
	//focus
	
	public IEnhancedGuiObject getDefaultFocusObject();
	public IEnhancedTopParent setDefaultFocusObject(IEnhancedGuiObject objIn);
	public IEnhancedGuiObject getFocusedObject();
	public IEnhancedTopParent setObjectRequestingFocus(IEnhancedGuiObject objIn);
	public IEnhancedGuiObject getFocusLockObject();
	public IEnhancedTopParent setFocusLockObject(IEnhancedGuiObject objIn);
	public IEnhancedTopParent clearFocusLockObject();
	public boolean doesFocusLockExist();
	public void clearFocusedObject();
	public void updateFocus();
	
	//object modification
	
	public boolean isMoving();
	public boolean isResizing();
	public ObjectModifyType getModifyType();
	public IEnhancedTopParent setResizingDir(ScreenLocation areaIn);
	public IEnhancedTopParent setModifyingObject(IEnhancedGuiObject objIn, ObjectModifyType typeIn);
	public IEnhancedTopParent setModifyMousePos(int mX, int mY);
	public IEnhancedGuiObject getModifyingObject();
	public IEnhancedTopParent clearModifyingObject();
	
	//mouse checks
	
	/** Returns true if the mouse is inside of any object. */
	public boolean isMouseInsideObject(int mX, int mY);
	/** Returns true if the mouse is inside of an EGuiHeader object. */
	public boolean isMouseInsideHeader(int mX, int mY);
	/** Returns the objects with this highest z level under the mouse. */
	public IEnhancedGuiObject getHighestZObjectUnderMouse();
	
	//close
	
	public void closeGui(boolean fullClose);
	public IEnhancedTopParent setCloseAndRecenter(boolean val);
}
