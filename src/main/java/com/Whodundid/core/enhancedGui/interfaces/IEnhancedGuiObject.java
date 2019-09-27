package com.Whodundid.core.enhancedGui.interfaces;

import com.Whodundid.core.enhancedGui.guiObjectUtil.EObjectGroup;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventFocus;
import com.Whodundid.core.enhancedGui.guiUtil.events.ObjectEvent;
import com.Whodundid.core.enhancedGui.guiUtil.events.ObjectEventHandler;
import com.Whodundid.core.enhancedGui.guiUtil.exceptions.ObjectInitException;
import com.Whodundid.core.util.miscUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.StorageBox;

//Jan 2, 2019
//Jan 11, 2019
//Mar 9, 2019 : Added some documentation.
//Mar 26, 2019 : implemented listeners
//Jun 5, 2019 : reimplemented listeners
//Jul 14, 2019 : rewrote listener system entirely
//Last edited: Aug 9, 2019
//Edit note: Added hovering text functionality, moved top level mouse checks to IEnhancedTopParent, finished documentation.
//First Added: Oct 2, 2018
//Author: Hunter Bragg

/** An interface outlining behavior for Enhanced Gui Objects. */
public interface IEnhancedGuiObject {
	
	//init
	
	/** Returns true if this object has been fully initialized with all of its values and children. */
	public boolean hasBeenInitialized();
	/** Internal method used to denote that this object and all of its children have been fully initialized. */
	public IEnhancedGuiObject completeInitialization();
	/** Event fired from the top parent upon being fully added to the parent so that this object can safely initialize all of it's own children. */
	public void initObjects() throws ObjectInitException;
	/** Removes all children and re-runs the initObjects method. */
	public void reInitObjects() throws ObjectInitException;
	/** Event called when this object has actually been added to its parent. */
	public void onObjectAddedToParent();
	
	//main draw
	
	/** Event fired from the top parent to draw this object. */
	public void drawObject(int mX, int mY, float ticks);
	/** Event fired from this object's pre draw setup to perform cursorImage changes. */
	public void updateCursorImage();
	/** Set this object to draw a text box at the mosue's location if the mouse has hovered for a short period of time. */
	public IEnhancedGuiObject setDrawHoverText(boolean val);
	/** Returns true if this object will draw hovering text at the mouse's location upon hovering for a short period of time. */
	public boolean drawsHoverText();
	/** Sets the text that will be drawn when the mouse has hovered over this object for a short period of time. */
	public IEnhancedGuiObject setHoverText(String textIn);
	/** Returns the text that will be drawn when the mouse hovers over this object for a short period of time. */
	public String getHoverText();
	
	//obj ids
	
	/** Returns this object's set ID number. */
	public int getObjectID();
	/** Designates this object with the specified ID number. Useful for ordering objects and referencing objects by shorthand calls. */
	public IEnhancedGuiObject setObjectID(int idIn);
	
	//drawing checks
	
	/** Returns true if this object will be drawn on the next draw cycle. */
	public boolean checkDraw();
	/** Returns true if this object is currently enabled. */
	public boolean isEnabled();
	/** Returns true if this object is visible. */
	public boolean isVisible();
	/** Returns true if this object will be drawn regardless of it being visible or enabled. */
	public boolean isPersistent();
	/** Returns true if this object's mouse checks are enforced by a boundary. */
	public boolean isBoundaryEnforced();
	/** Set this object's enabled state. */
	public IEnhancedGuiObject setEnabled(boolean val);
	/** Set this object's visibility. A non-visible object can still run actions if it is still enabled. */
	public IEnhancedGuiObject setVisible(boolean val);
	/** Sets this object to be drawn regardless of it being visible or enabled. */
	public IEnhancedGuiObject setPersistent(boolean val);
	/** Specifies a region that this object will adhere to for mouse checks. */
	public IEnhancedGuiObject setBoundaryEnforcer(EDimension dimIn);
	/** Returns an EDimension object containing the boundary this object is bounded by */
	public EDimension getBoundaryEnforcer();
	
	//size
	
	/** Returns true if this object has a header. */
	public boolean hasHeader();
	/** Returns true if this object is resizeable. */
	public boolean isResizeable();
	/** If this object has a header, returns the header object, otherwise returns null. */
	public EGuiHeader getHeader();
	/** Returns the minimum width that this object can have. */
	public int getMinimumWidth();
	/** Returns the minimum height that this object can have. */
	public int getMinimumHeight();
	/** Returns the maximum width that this object can have. */
	public int getMaximumWidth();
	/** Returns the maximum height that this object can have. */
	public int getMaximumHeight();
	/** Sets the minimum width for this object when resizing. */
	public IEnhancedGuiObject setMinimumWidth(int widthIn);
	/** Sets the minimum height for this object when resizing. */
	public IEnhancedGuiObject setMinimumHeight(int heightIn);
	/** Sets the maximum width for this object when resizing. */
	public IEnhancedGuiObject setMaximumWidth(int widthIn);
	/** Sets the maximum height for this object when resizing. */
	public IEnhancedGuiObject setMaximumHeight(int heightIn);
	/** Sets whether this object can be resized or not. */
	public IEnhancedGuiObject setResizeable(boolean val);
	/** Resizes this object by an amount in both the x and y axies, specified by the given Direction. */
	public IEnhancedGuiObject resize(int xIn, int yIn, ScreenLocation areaIn);
	
	//position
	
	/** Moves the object by the specified x and y values. Does not move the object to specified coordinates however. Use setPosition() instead. */
	public void move(int newX, int newY);
	/** Returns true if this object's position cannot be modified. */
	public boolean isPositionLocked();
	/** Moves this object back to it's initial position that it had upon its creation. */
	public IEnhancedGuiObject resetPosition();
	/** Move this object and all of its children to the specified x and y coordinates. The specified position represents the top left corner of this object.
	    All children will remain in their original positions relative to the parent object. */
	public IEnhancedGuiObject setPosition(int newX, int newY);
	/** Sets this object's position as unmodifiable. */
	public IEnhancedGuiObject setPositionLocked(boolean val);
	/** Specifies this objects position, width, and height using an EDimension object. */
	public IEnhancedGuiObject setDimensions(EDimension dimIn);
	/** Specifies this objects position, width, and height. (x, y, width, height) */
	public IEnhancedGuiObject setDimensions(int startXIn, int startYIn, int widthIn, int heightIn);
	public IEnhancedGuiObject setInitialPosition(int startXIn, int startYIn);
	public StorageBox<Integer, Integer> getInitialPosition();
	public IEnhancedGuiObject centerObjectWithSize(int widthIn, int heightIn);
	/** Returns the current dimensions of this object. */
	public EDimension getDimensions();
	
	//objects
	
	/** Checks if this object is a child of the specified object. */
	public boolean isChildOfObject(IEnhancedGuiObject objIn);
	/** Adds a child IEnhancedGuiObject to this object. The object is added before the next draw cycle. */
	public IEnhancedGuiObject addObject(IEnhancedGuiObject... objsIn);
	/** Removes a child IEnhancedGuiObject to this object. If this object does not contain the specified child, no action is performed. The object is removed before the next draw cycle. */
	public IEnhancedGuiObject removeObject(IEnhancedGuiObject... objsIn);
	/** Returns this object's object group, if any. */
	public EObjectGroup getObjectGroup();
	/** Sets this object's object group. */
	public IEnhancedGuiObject setObjectGroup(EObjectGroup groupIn);
	/** Event fired when any object within the object group fires an event. */
	public void onGroupNotification(ObjectEvent e);
	/** Returns a list of all objects that are directly children of this object. */
	public EArrayList<IEnhancedGuiObject> getImmediateChildren();
	/** Returns a list of all objects that are going to be added on the next draw cycle */
	public EArrayList<IEnhancedGuiObject> getObjectsToBeAdded();
	/** Returns a list of all objects that are going to be removed on the next draw cycle */
	public EArrayList<IEnhancedGuiObject> getObjectsToBeRemoved();
	/** Returns a list of all objects that descend from this parent. */
	public EArrayList<IEnhancedGuiObject> getAllChildren();
	/** Returns a list of all children from 'getAllChildren()' that are currently under the mouse. */
	public EArrayList<IEnhancedGuiObject> getAllChildrenUnderMouse();
	
	//parents
	
	/** Returns this object's direct parent object. */
	public IEnhancedGuiObject getParent();
	/** Sets this object's parent. */
	public IEnhancedGuiObject setParent(IEnhancedGuiObject parentIn);
	/** Returns the top most parent object in the parent chain. */
	public IEnhancedTopParent getTopParent();
	
	//zLevel
	
	/** Returns this object's Z level added on top of all of it's combined parent's Z levels.*/
	public int getZLevel();
	/** Sets this object's Z Level, this value is added to the combination of all of its parent's Z levels. */
	public IEnhancedGuiObject setZLevel(int zLevelIn);
	/** Signals the top parent to bring this object and it's children to the very front of the draw order on the next draw cycle. */
	public IEnhancedGuiObject bringToFront();
	/** Signals the top parent to bring this object and it's children to the very back of the draw order on the next draw cycle. */
	public IEnhancedGuiObject sendToBack();
	
	//focus
	
	/** Returns true if this object is the current focus owner in it's top parent object. */
	public boolean hasFocus();
	/** Signals the top parent to transfer focus from this object to the top parent's default focus object on the next draw cycle.
	    If this object has a focus lock set, the lock will be removed and focus will be transfered to the top parent's default focus object on the next draw cycle. */
	public boolean relinquishFocus();
	/** Focus event that is called when this object is given focus from its top parent. */
	public void onFocusGained(EventFocus eventIn);
	/** Focus event that is called when this object loses focus in any way. */
	public void onFocusLost(EventFocus eventIn);
	/** Signals the top parent to transfer focus from this object to the object specified on the next draw cycle. */
	public void transferFocus(IEnhancedGuiObject objIn);
	/** Used to draw a visible border around an object whose focus is locked. A focus lock does not need to be in place in order for this to be called however. */
	public void drawFocusLockBorder();
	/** Signals the top parent to try transfering focus to this object on the next draw cycle. If another object has a focus lock, this object will not receive focus */ 
	public IEnhancedGuiObject requestFocus();
	
	//mouse checks
	
	public boolean isMouseOnObjEdge(int mX, int mY);
	public ScreenLocation getEdgeAreaMouseIsOn();
	/** Event fired upon the mouse entering this object. */
	public void mouseEntered(int mX, int mY);
	/** Event fired upon the mouse exiting this object. */
	public void mouseExited(int mX, int mY);
	/** Returns true if the mouse is currently inside this object. If a boundary enforcer is set, this method will return true if the mouse is inside of the the specified boundary. */
	public boolean isMouseInside(int mX, int mY);
	/** Returns true if the mouse is currently inside this object and that this is the top most object insideo fthe parent. */
	public boolean isMouseHover(int mX, int mY);
	
	//basic inputs
	
	/** Event fired on every new MouseEvent processed from the topParent's 'handleMouseInput' method which indicates the current position of the mouse on screen. */
	public void parseMousePosition(int mX, int mY);
	/** Event fired when a mouse button is pressed on this object. */
	public void mousePressed(int mX, int mY, int button);
	/** Event fired when the pressed mouse button is released on this object. */
	public void mouseReleased(int mX, int mY, int button);
	/** Event fired evertime the mouse is moved if it had been pressed on this object. */ 
	public void mouseDragged(int mX, int mY, int button, long timeSinceLastClick);
	/** Event fired when the mouse scrolls over this object. The object must be hovering over the object in order for this event to be called. */
	public void mouseScrolled(int change);
	/** Event fired when a key is pressed on this object. */
	public void keyPressed(char typedChar, int keyCode);
	/** Event fired when a pressed key is released on this object. */
	public void keyReleased(char typedChar, int keyCode);
	
	//updateScreen
	
	/** Passing the updateScreen event from GuiScreen. */
	public void updateScreen();
	
	//events
	
	/** Gets the EventHandler. */
	public ObjectEventHandler getEventHandler();
	/** Register an object that listens to this object's events. */
	public IEnhancedGuiObject registerListener(IEnhancedGuiObject objIn);
	/** Unregister a listener Object. */
	public IEnhancedGuiObject unregisterListener(IEnhancedGuiObject objIn);
	/** Broadcasts an ObjectEvent on this object. */
	public IEnhancedGuiObject postEvent(ObjectEvent e);
	/** Called on ObjectEvents. */
	public void onListen(ObjectEvent e);
	
	//action
	
	/** Event called whenever a child IEnhancedActionObject's action is triggered. */
	public void actionPerformed(IEnhancedActionObject object);
	
	//close object
	
	/** Removes this object and all of it's children from the immeadiate a parent. Removes any present focus locks on this object and returns focus back to the top parent. */
	public void close();
	/** Upon closing, this object will attempt to transfer it's focus to the specified object if possible. */
	public IEnhancedGuiObject setFocusedObjectOnClose(IEnhancedGuiObject objIn);
}
