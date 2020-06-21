package com.Whodundid.core.windowLibrary.windowTypes.interfaces;

import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.FocusType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.ObjectModifyType;

//Author: Hunter Bragg

/** An interface outlining behavior for Top Level WindowObjects. Top level objects handle drawing, object focus, object manipulation, and inputs. */
public interface ITopParent extends IWindowObject {
	
	//-------
	//drawing
	//-------
	
	public void drawDebugInfo();
	
	//----------
	//draw order
	//----------
	
	public ITopParent bringObjectToFront(IWindowObject objIn);
	public ITopParent sendObjectToBack(IWindowObject objIn);
	
	//-------------
	//hovering text
	//-------------
	
	public ITopParent setHoveringObject(IWindowObject objIn);
	public IWindowObject getHoveringObject();
	
	//------------
	//double click
	//------------
	
	public ITopParent setLastClickedObject(IWindowObject objectIn);
	public IWindowObject getLastClickedObject();
	public ITopParent setLastClickTime(long timeIn);
	public long getLastClickTime();
	
	//-------
	//objects
	//-------
	
	public IWindowObject getHighestZLevelObject();
	public ITopParent hideUnpinnedObjects();
	public ITopParent hideAllExcept(IWindowObject objIn);
	public ITopParent revealHiddenObjects();
	public ITopParent removeUnpinnedObjects();
	public ITopParent removeAllObjects();
	public boolean hasPinnedObjects();
	
	//-----
	//focus
	//-----
	
	public IWindowObject getFocusedObject();
	public ITopParent setFocusedObject(IWindowObject objIn);
	public ITopParent setObjectRequestingFocus(IWindowObject objIn, FocusType typeIn);
	public IWindowObject getFocusLockObject();
	public ITopParent setFocusLockObject(IWindowObject objIn);
	public ITopParent clearFocusLockObject();
	public boolean doesFocusLockExist();
	public void clearFocusedObject();
	public void updateFocus();
	
	//-------------------
	//object modification
	//-------------------
	
	public ObjectModifyType getModifyType();
	public ITopParent setResizingDir(ScreenLocation areaIn);
	public ITopParent setModifyingObject(IWindowObject objIn, ObjectModifyType typeIn);
	public ITopParent setMaximizingWindow(IWindowParent objIn, ScreenLocation side, boolean centerAroundHeader);
	public ITopParent setModifyMousePos(int mX, int mY);
	public IWindowObject getModifyingObject();
	public IWindowParent getMaximizingWindow();
	public ScreenLocation getMaximizingArea();
	public boolean getMaximizingHeaderCenter();
	public ITopParent clearModifyingObject();
	
	//------------
	//mouse checks
	//------------
	
	/** Returns true if the mouse is on the edge of an object. */
	public boolean isMouseOnObjEdge(int mX, int mY);
	/** Returns the edge type that the mouse is currently hovering over, if any. */
	public ScreenLocation getEdgeAreaMouseIsOn();
	/** Returns true if the mouse is inside of any object. */
	public boolean isMouseInsideObject(int mX, int mY);
	/** Returns true if the mouse is inside of an EGuiHeader object. */
	public boolean isMouseInsideHeader(int mX, int mY);
	/** Returns the objects with this highest z level under the mouse. */
	public IWindowObject getHighestZObjectUnderMouse();
	/** Returns a list of all objects underneath the mouse. */
	public EArrayList<IWindowObject> getAllObjectsUnderMouse();
	
	//-----
	//close
	//-----
	
	public ITopParent setEscapeStopper(IWindowObject obj);
	public IWindowObject getEscapeStopper();

}
