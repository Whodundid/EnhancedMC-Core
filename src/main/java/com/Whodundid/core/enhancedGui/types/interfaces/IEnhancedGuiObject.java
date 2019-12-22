package com.Whodundid.core.enhancedGui.types.interfaces;

import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiUtil.EObjectGroup;
import com.Whodundid.core.enhancedGui.objectEvents.EventFocus;
import com.Whodundid.core.enhancedGui.objectEvents.ObjectEvent;
import com.Whodundid.core.enhancedGui.objectEvents.ObjectEventHandler;
import com.Whodundid.core.enhancedGui.objectExceptions.ObjectInitException;
import com.Whodundid.core.util.chatUtil.ITabCompleteListener;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.StorageBox;

/** An interface outlining behavior for Enhanced Gui Objects. */
public interface IEnhancedGuiObject extends ITabCompleteListener {
	
	//init
	
	/** Returns true if this object has been fully initialized with all of its values and children. */
	public boolean isInit();
	/** Returns true if initObjects has been fully completed. */
	public boolean isObjectInit();
	/** Internal method used to denote that this object and all of its children have been fully initialized. */
	public void completeInit();
	/** Event fired from the top parent upon being fully added to the parent so that this object can safely initialize all of it's own children. */
	public void initObjects() throws ObjectInitException;
	/** Removes all children and re-runs the initObjects method. */
	public void reInitObjects() throws ObjectInitException;
	/** Event called when this object has actually been added to its parent. */
	public void onAdded();
	
	//main draw
	
	/** Event fired from the top parent to draw this object. */
	public void drawObject(int mX, int mY, float ticks);
	/** Event fired from the top parent when the object is drawn for the first time. */
	public void onFirstDraw();
	/** Returns true if this object has been drawn at least once. */
	public boolean hasFirstDraw();
	/** Event fired from this object's pre draw setup to perform cursorImage changes. */
	public void updateCursorImage();
	/** Event fired from the top parent when the mouse has been hovering over this object for a short period of time. */
	public void onMouseHover(int mX, int mY);
	/** Sets generic mouse hovering background with specified text. */
	public IEnhancedGuiObject setHoverText(String textIn);
	/** Sets hover text color. */
	public IEnhancedGuiObject setHoverTextColor(int colorIn);
	
	//obj ids
	
	/** Returns this object's set ID number. */
	public int getObjectID();
	/** Designates this object with the specified ID number. Useful for ordering objects and referencing objects by shorthand calls. */
	public IEnhancedGuiObject setObjectID(int idIn);
	/** Returns the name of this object. */
	public String getObjectName();
	/** Sets the name of this object. */
	public IEnhancedGuiObject setObjectName(String nameIn);
	
	//drawing checks
	
	/** Returns true if this object is elligible be drawn on the next draw cycle. */
	public boolean checkDraw();
	/** Returns true if this object is currently enabled. */
	public boolean isEnabled();
	/** Returns true if this object is visible. */
	public boolean isVisible();
	/** Returns true if this object will be drawn regardless of it being visible or enabled. */
	public boolean isPersistent();
	/** Returns true if this object's mouse checks are enforced by a boundary. */
	public boolean isBoundaryEnforced();
	/** Returns true if this object is resizing. */
	public boolean isResizing();
	/** Returns true if this object is moving. */
	public boolean isMoving();
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
	public int getMinWidth();
	/** Returns the minimum height that this object can have. */
	public int getMinHeight();
	/** Returns the maximum width that this object can have. */
	public int getMaxWidth();
	/** Returns the maximum height that this object can have. */
	public int getMaxHeight();
	/** Sets both the minimum width and height for this object. */
	public IEnhancedGuiObject setMinDims(int widthIn, int heightIn);
	/** Sets both the maximum width and height for this object. */
	public IEnhancedGuiObject setMaxDims(int widthIn, int heightIn);
	/** Sets the minimum width for this object when resizing. */
	public IEnhancedGuiObject setMinWidth(int widthIn);
	/** Sets the minimum height for this object when resizing. */
	public IEnhancedGuiObject setMinHeight(int heightIn);
	/** Sets the maximum width for this object when resizing. */
	public IEnhancedGuiObject setMaxWidth(int widthIn);
	/** Sets the maximum height for this object when resizing. */
	public IEnhancedGuiObject setMaxHeight(int heightIn);
	/** Sets whether this object can be resized or not. */
	public IEnhancedGuiObject setResizeable(boolean val);
	/** Resizes this object by an amount in both the x and y axies, specified by the given Direction. */
	public IEnhancedGuiObject resize(int xIn, int yIn, ScreenLocation areaIn);
	
	//position
	
	/** Moves the object by the specified x and y values. Does not move the object to specified coordinates however. Use setPosition() instead. */
	public void move(int newX, int newY);
	/** Returns true if this object's position cannot be modified. */
	public boolean isMoveable();
	/** Moves this object back to it's initial position that it had upon its creation. */
	public IEnhancedGuiObject resetPosition();
	/** Move this object and all of its children to the specified x and y coordinates. The specified position represents the top left corner of this object.
	    All children will remain in their original positions relative to the parent object. */
	public IEnhancedGuiObject setPosition(int newX, int newY);
	/** Sets this object's position as unmodifiable. */
	public IEnhancedGuiObject setMoveable(boolean val);
	/** Specifies this objects position, width, and height using an EDimension object. */
	public IEnhancedGuiObject setDimensions(EDimension dimIn);
	/** Specifies this object's width and height based on the current starting position. */
	public IEnhancedGuiObject setDimensions(int widthIn, int heightIn);
	/** Specifies this objects position, width, and height. (x, y, width, height) */
	public IEnhancedGuiObject setDimensions(int startXIn, int startYIn, int widthIn, int heightIn);
	/** Specifies the position this object will relocate to when its' position is reset. */
	public IEnhancedGuiObject setInitialPosition(int startXIn, int startYIn);
	/** Returns the position this object will relocate to when reset. */
	public StorageBox<Integer, Integer> getInitialPosition();
	/** Centers the object around the center of the screen with proper dimensions. */
	public IEnhancedGuiObject centerObjectWithSize(int widthIn, int heightIn);
	/** Returns the current dimensions of this object. */
	public EDimension getDimensions();
	
	//objects
	
	/** Checks if this object is a child of the specified object. */
	public boolean isChild(IEnhancedGuiObject objIn);
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
	public EArrayList<IEnhancedGuiObject> getObjects();
	/** Returns a list of all objects that are going to be added on the next draw cycle */
	public EArrayList<IEnhancedGuiObject> getAddingObjects();
	/** Returns a list of all objects that are going to be removed on the next draw cycle */
	public EArrayList<IEnhancedGuiObject> getRemovingObjects();
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
	/** Returns the first instance of an InnerEnhancedGui in the parent chain. */
	public IEnhancedGuiObject getWindowParent();
	
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
	/** Returns the object that will recieve foucs by default when the base object has foucs transfered to it. */
	public IEnhancedGuiObject getDefaultFocusObject();
	/** Sets a default focus object for this object. When the main object recieves focus, the top parent will attempt to transfer focus to the specified default focus object. */
	public IEnhancedGuiObject setDefaultFocusObject(IEnhancedGuiObject objectIn);
	
	//mouse checks
	
	/** Returns true if the mouse is on the edge of an object. */
	public boolean isMouseOnObjEdge(int mX, int mY);
	/** Returns the edge type that the mouse is currently hovering over, if any. */
	public ScreenLocation getEdgeAreaMouseIsOn();
	/** Event fired upon the mouse entering this object. */
	public void mouseEntered(int mX, int mY);
	/** Event fired upon the mouse exiting this object. */
	public void mouseExited(int mX, int mY);
	/** Returns true if the mouse is currently inside this object. If a boundary enforcer is set, this method will return true if the mouse is inside of the the specified boundary. */
	public boolean isMouseInside(int mX, int mY);
	/** Returns true if the mouse is currently inside this object and that this is the top most object inside of the parent. */
	public boolean isMouseOver(int mX, int mY);
	/** Returns true if this object can be clicked on. */
	public boolean isClickable();
	/** Specifies if this object can be clicked on. */
	public IEnhancedGuiObject setClickable(boolean valIn);
	
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
	public void actionPerformed(IEnhancedActionObject object, Object... args);
	
	//close object
	
	/** Returns whether this object can be closed or not. */
	public boolean isCloseable();
	/** Sets whether this object can be closed or not. */
	public IEnhancedGuiObject setCloseable(boolean val);
	/** Removes this object and all of it's children from the immeadiate a parent. Removes any present focus locks on this object and returns focus back to the top parent. */
	public void close();
	/** Event fired when object is closed. */
	public void onClosed();
	/** Upon closing, this object will attempt to transfer it's focus to the specified object if possible. */
	public IEnhancedGuiObject setFocusedObjectOnClose(IEnhancedGuiObject objIn);
}
