package com.Whodundid.core.enhancedGui;

import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.objectEvents.EventKeyboard;
import com.Whodundid.core.enhancedGui.objectEvents.EventModify;
import com.Whodundid.core.enhancedGui.objectEvents.EventMouse;
import com.Whodundid.core.enhancedGui.objectEvents.EventObjects;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.KeyboardType;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.MouseType;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.ObjectEventType;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.ObjectModifyType;
import com.Whodundid.core.enhancedGui.objectExceptions.HeaderAlreadyExistsException;
import com.Whodundid.core.enhancedGui.objectExceptions.ObjectInitException;
import com.Whodundid.core.enhancedGui.types.EnhancedGui;
import com.Whodundid.core.enhancedGui.types.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedTopParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class StaticEGuiObject {
	
	//size
	/** Returns true if the object has an EGuiHeader. */
	public static boolean hasHeader(IEnhancedGuiObject obj) { return getHeader(obj) != null; }
	/** Returns this objects EGuiHeader, if there is one. */
	public static EGuiHeader getHeader(IEnhancedGuiObject obj) {
		for (IEnhancedGuiObject o : EArrayList.combineLists(obj.getObjects(), obj.getAddingObjects())) {
			if (o instanceof EGuiHeader) { return (EGuiHeader) o; }
		}
		return null;
	}
	/** Generic object resizing algorithm. */
	public static void resize(IEnhancedGuiObject obj, int xIn, int yIn, ScreenLocation areaIn) {
		obj.postEvent(new EventModify(obj, obj, ObjectModifyType.Resize)); //post an event
		if (xIn != 0 || yIn != 0) { //make sure that there is actually a change in the cursor position
			EDimension d = obj.getDimensions();
			int x = 0, y = 0, w = 0, h = 0;
			boolean e = false, s = false;
			//perform resizing on different sides depending on the side that's being resized
			switch (areaIn) {
			case top: x = d.startX; y = d.startY + yIn; w = d.width; h = d.height - yIn; break;
			case bot: x = d.startX; y = d.startY; w = d.width; h = d.height + yIn; break;
			case right: x = d.startX; y = d.startY; w = d.width + xIn; h = d.height; break;
			case left: x = d.startX + xIn; y = d.startY; w = d.width - xIn; h = d.height; break;
			case topRight: x = d.startX; y = d.startY + yIn; w = d.width + xIn; h = d.height - yIn; break;
			case botRight: x = d.startX; y = d.startY; w = d.width + xIn; h = d.height + yIn; break;
			case topLeft: x = d.startX + xIn; y = d.startY + yIn; w = d.width - xIn; h = d.height - yIn; break;
			case botLeft: x = d.startX + xIn; y = d.startY; w = d.width - xIn; h = d.height + yIn; break;
			default: break;
			}
			//restrict the object to its allowed minimum width
			if (w < obj.getMinWidth()) {
				w = obj.getMinWidth();
				switch (areaIn) {
				case right: case topRight: case botRight: x = d.startX; break;
				case left: case topLeft: case botLeft: x = d.endX - w; break;
				default: break;
				}
			}
			//restrict the object to its allowed maximum width
			if (w > obj.getMaxWidth()) {
				w = obj.getMaxWidth();
				switch (areaIn) {
				case right: case topRight: case botRight: x = d.startX; break;
				case left: case topLeft: case botLeft: x = d.endX - w; break;
				default: break;
				}
			}
			//restrict the object to its allowed minimum height
			if (h < obj.getMinHeight()) {
				h = obj.getMinHeight();
				switch (areaIn) {
				case top: case topRight: case topLeft: y = d.endY - h; break;
				case bot: case botRight: case botLeft: y = d.startY; break;
				default: break;
				}
			}
			//restrict the object to its allowed maximum height
			if (h > obj.getMaxHeight()) {
				h = obj.getMaxHeight();
				switch (areaIn) {
				case top: case topRight: case topLeft: y = d.endY - h; break;
				case bot: case botRight: case botLeft: y = d.startY; break;
				default: break;
				}
			}
			obj.setDimensions(x, y, w, h); //set the dimensions of the object to the resized dimensions
			try {
				//(lazy approach) remake all the children based on the resized dimensions
				obj.reInitObjects();
			} catch (ObjectInitException q) { q.printStackTrace(); }
		}
	}
	
	//position
	/** Translates the specified object by a given x and y amount. */
	public static void move(IEnhancedGuiObject obj, int newX, int newY) {
		obj.postEvent(new EventModify(obj, obj, ObjectModifyType.Move)); //post an event
		if (!obj.isMoveable()) { //only allow the object to be moved if it's not locked in place
			//get all of the children in the object
			for (IEnhancedGuiObject o : EArrayList.combineLists(obj.getObjects(), obj.getAddingObjects())) {
				if (!o.isMoveable()) { //only move the child if it's not locked in place
					if (o instanceof WindowParent) { //only move the window if it moves with the parent
						if (((WindowParent) o).movesWithParent()) { o.move(newX, newY); }
					} else { o.move(newX, newY); }
				}
			}
			EDimension d = obj.getDimensions();
			obj.setDimensions(d.startX + newX, d.startY + newY, d.width, d.height); //offset the original position by the specified offset
			if (obj.isBoundaryEnforced()) { //also move the boundary enforcer, if there is one
				EDimension b = obj.getBoundaryEnforcer();
				obj.getBoundaryEnforcer().setPosition(b.startX + newX, b.startY + newY);
			}
		}
	}
	/** Moves the object to the specified x and y coordinates. */
	public static void setPosition(IEnhancedGuiObject obj, int newX, int newY) {
		EDimension d = obj.getDimensions();
		StorageBox<Integer, Integer> loc = new StorageBox(d.startX, d.startY); //the object's current position for shorter code
		StorageBoxHolder<IEnhancedGuiObject, StorageBox<Integer, Integer>> previousLocations = new StorageBoxHolder();
		EArrayList<IEnhancedGuiObject> objs = EArrayList.combineLists(obj.getObjects(), obj.getAddingObjects());
		for (IEnhancedGuiObject o : objs) { //get each of the object's children's relative positions
			previousLocations.add(o, new StorageBox(o.getDimensions().startX - loc.getObject(), o.getDimensions().startY - loc.getValue()));
		}
		obj.setDimensions(newX, newY, d.width, d.height); //move the object to the new position
		for (IEnhancedGuiObject o : objs) {
			if (!o.isMoveable()) { //don't move the child if its position is locked
				StorageBox<Integer, Integer> oldLoc = previousLocations.getBoxWithObj(o).getValue();
				o.setPosition(newX + oldLoc.getObject(), newY + oldLoc.getValue()); //move the child to the new location with the parent's offest
			}
		}
	}
	/** Centers the object and all of its children in the middle of the screen with the specified dimensions. */
	public static void centerObjectWithSize(IEnhancedGuiObject obj, int widthIn, int heightIn) {
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft()); //get the screen size
		int sWidth = res.getScaledWidth();
		int sHeight = res.getScaledHeight();
		int startX, startY, width, height;
		if (sWidth >= widthIn) { //check if the screen width is larger than the desired object width
			//if it is, set the xPos so that it will be in the middle of the screen
			startX = (sWidth - widthIn) / 2;
			width = widthIn;
		} else { //otherwise, restrict the object's width to the screen's width
			startX = 0;
			width = sWidth;
		}
		if (sHeight >= heightIn) { //check if the screen height is larger than the desired object height
			//if it is, set the yPos so that it will be in the middle of the screen
			startY = (sHeight - heightIn) / 2;
			height = heightIn;
		} else { //otherwise, restrict the object's width to the screen's height
			startY = 0;
			height = sHeight;
		}
		obj.setDimensions(startX, startY, width, height); //apply the dimensions to the object
	}
	
	//objects
	/** Returns true if the given object is a child of the specified parent. */
	public static boolean isChildOfObject(IEnhancedGuiObject child, IEnhancedGuiObject parent) {
		IEnhancedGuiObject curObj = child;
		//recursively check if the child's lineage contain's the specified parent
		while (curObj != null && curObj.getParent() != null) {
			if (curObj.getParent().equals(curObj)) { return false; }
			if (curObj.getParent().equals(parent)) { return true; }
			curObj = curObj.getParent();
		}
		return false;
	}
	/** Start the process of adding a child to this object. Children are fully added on the next draw cycle. */
	public static void addObject(IEnhancedGuiObject parent, IEnhancedGuiObject... objsIn) {
		for (IEnhancedGuiObject o : objsIn) {
			try {
				if (o != null) { //only add if the object isn't null
					//don't let the object be added to itself, or if the object is already in the object
					if (o != parent && parent.getObjects().notContains(o) && parent.getAddingObjects().notContains(o)) {
						if (o instanceof EnhancedGui) { continue; } //don't add GuiScreens
						if (o instanceof EGuiHeader && parent.hasHeader()) { 
							throw new HeaderAlreadyExistsException(parent.getHeader()); //remove this exception -- it's pointless
						}
						try {
							if (o instanceof WindowParent) { ((WindowParent) o).initGui(); } //if it's a window, do it's init
							o.setParent(parent).initObjects(); //initialize all of the children's children
							o.setZLevel(parent.getZLevel() + o.getZLevel() + 1); //increment the child's z layer based off of the parent
							//if the parent has a boundary enforcer, apply it to the child as well
							if (parent.isBoundaryEnforced()) { o.setBoundaryEnforcer(parent.getBoundaryEnforcer()); }
							o.completeInit(); //tell the child that it has been fully initialized and that it is ready to be added on the next draw cycle
						} catch (ObjectInitException e) { e.printStackTrace(); }
						parent.getAddingObjects().add(o); //give the processed child to the parent so that it will be added
					}
				}
			} catch (HeaderAlreadyExistsException e) { e.printStackTrace(); }
		}
	}
	/** Start the process of removing a child from this object. Children are fully removed on the next draw cycle. */
	public static void removeObject(IEnhancedGuiObject parent, IEnhancedGuiObject... objsIn) {
		parent.getRemovingObjects().addAll(objsIn);
	}
	/** Returns a list containing every single child from every object in the specified object. */
	public static EArrayList<IEnhancedGuiObject> getAllChildren(IEnhancedGuiObject obj) {
		EArrayList<IEnhancedGuiObject> foundObjs = new EArrayList();
		EArrayList<IEnhancedGuiObject> objsWithChildren = new EArrayList();
		EArrayList<IEnhancedGuiObject> workList = new EArrayList();
		
		//grab all immediate children and add them to foundObjs, then check if any have children of their own
		obj.getObjects().forEach(o -> { foundObjs.add(o); if (!o.getObjects().isEmpty()) { objsWithChildren.add(o); } });
		//same as above but now check from objects that are going to be added on the next draw cycle
		obj.getAddingObjects().forEach(o -> { foundObjs.add(o); if (!o.getAddingObjects().isEmpty()) { objsWithChildren.add(o); } });
		//load the workList with every child found on each object
		objsWithChildren.forEach(c -> workList.addAll(c.getObjects()));
		objsWithChildren.forEach(c -> workList.addAll(c.getAddingObjects()));
		
		//only work as long as there are still child layers to process
		while (workList.isNotEmpty()) {
			//update the foundObjs
			foundObjs.addAll(workList);
			
			//for the current layer, find all objects that have children
			objsWithChildren.clear();
			workList.stream().filter(o -> !o.getObjects().isEmpty()).forEach(objsWithChildren::add);
			workList.stream().filter(o -> !o.getAddingObjects().isEmpty()).forEach(objsWithChildren::add);
			
			//put all children on the next layer into the work list
			workList.clear();
			objsWithChildren.forEach(c -> workList.addAll(c.getObjects()));
			objsWithChildren.forEach(c -> workList.addAll(c.getAddingObjects()));
		}
		return foundObjs;
	}
	/** Returns a list of all children currently under the cursor. */
	public static EArrayList<IEnhancedGuiObject> getAllChildrenUnderMouse(IEnhancedGuiObject obj, int mX, int mY) {
		EArrayList<IEnhancedGuiObject> l = new EArrayList();
		//only add objects if they are visible and if the cursor is over them.
		obj.getAllChildren().stream().filter(o -> o.checkDraw() && o.isMouseInside(mX, mY)).forEach(l::add);
		return l;
	}
	
	//parents
	/* Returns the topParent for the specified object. */
	public static IEnhancedTopParent getTopParent(IEnhancedGuiObject obj) {
		IEnhancedGuiObject parentObj = obj.getParent();
		//recursively check through the object's parent lineage to see if that parent is a topParent
		while (parentObj != null) {
			if (parentObj instanceof IEnhancedTopParent) { return (IEnhancedTopParent) parentObj; }
			if (parentObj.getParent() != null) { parentObj = parentObj.getParent(); }
		}
		return obj instanceof IEnhancedTopParent ? (IEnhancedTopParent) obj : null;
	}
	/** Returns the parent window for the specified object, if there is one. */
	public static IWindowParent getWindowParent(IEnhancedGuiObject obj) {
		IEnhancedGuiObject parentObject = obj.getParent();
		//recursively check through the object's parent lineage to see if that parent is a window
		while (parentObject != null && !(parentObject instanceof IEnhancedTopParent)) {
			if (parentObject instanceof IWindowParent) { return (IWindowParent) parentObject; }
			if (parentObject.getParent() != null) { parentObject = parentObject.getParent(); }
		}
		return obj instanceof IWindowParent ? (IWindowParent) obj : null;
	}
	
	//mouse checks
	public static ScreenLocation getEdgeAreaMouseIsOn(IEnhancedGuiObject objIn, int mX, int mY) {
		boolean left = false, right = false, top = false, bottom = false;
		EDimension d = objIn.getDimensions();
		int rStartY = objIn.hasHeader() ? objIn.getHeader().startY : d.startY;
		if (mX >= d.startX - 2 && mX <= d.endX + 1 && mY >= rStartY - 1 && mY <= d.endY + 1) {
			if (mX >= d.startX - 2 && mX <= d.startX) { left = true; }
			if (mX >= d.endX - 1 && mX <= d.endX + 1) { right = true; }
			if (mY >= rStartY - 1 && mY <= rStartY) { top = true; }
			if (mY >= d.endY - 1 && mY <= d.endY + 1) { bottom = true; }
			if (left) {
				if (top) { return ScreenLocation.topLeft; }
				else if (bottom) { return ScreenLocation.botLeft; }
				else { return ScreenLocation.left; }
			}
			else if (right) {
				if (top) { return ScreenLocation.topRight; }
				else if (bottom) { return ScreenLocation.botRight; }
				else { return ScreenLocation.right; }
			} 
			else if (top) { return ScreenLocation.top; }
			else if (bottom) { return ScreenLocation.bot; }
		}
		return ScreenLocation.out;
	}
	
	//basic inputs
	public static void parseMousePosition(IEnhancedGuiObject objIn, int mX, int mY) { objIn.getObjects().stream().filter(o -> o.isMouseInside(mX, mY)).forEach(o -> o.parseMousePosition(mX, mY)); }
	public static void mousePressed(IEnhancedGuiObject objIn, int mX, int mY, int button) {
		objIn.postEvent(new EventMouse(objIn, mX, mY, button, MouseType.Pressed));
		if (!objIn.hasFocus() && objIn.isMouseOver(mX, mY)) { objIn.requestFocus(); }
		if (button == 0 && objIn.isResizeable() && !objIn.getEdgeAreaMouseIsOn().equals(ScreenLocation.out)) {
			objIn.getTopParent().setResizingDir(objIn.getEdgeAreaMouseIsOn());
			objIn.getTopParent().setModifyMousePos(mX, mY);
			objIn.getTopParent().setModifyingObject(objIn, ObjectModifyType.Resize);
		}
	}
	public static void mouseReleased(IEnhancedGuiObject objIn, int mX, int mY, int button) {
		objIn.postEvent(new EventMouse(objIn, mX, mY, button, MouseType.Released));
		if (objIn.getTopParent().getModifyType() == ObjectModifyType.Resize) { objIn.getTopParent().clearModifyingObject(); }
		if (objIn.getTopParent().getDefaultFocusObject() != null) { objIn.getTopParent().getDefaultFocusObject().requestFocus(); }
	}
	public static void mouseDragged(IEnhancedGuiObject objIn, int mX, int mY, int button, long timeSinceLastClick) {}
	public static void mouseScolled(IEnhancedGuiObject objIn, int mX, int mY, int change) {
		objIn.postEvent(new EventMouse(objIn, mX, mY, -1, MouseType.Scrolled));
		for (IEnhancedGuiObject o : objIn.getObjects()) {
			if (o.isMouseInside(mX, mY) && o.checkDraw()) { o.mouseScrolled(change); }
		}
	}
	public static void keyPressed(IEnhancedGuiObject objIn, char typedChar, int keyCode) {
		objIn.postEvent(new EventKeyboard(objIn, typedChar, keyCode, KeyboardType.Pressed));
		if (objIn.getTopParent() != null && keyCode == 15) {
			EArrayList<IEnhancedGuiObject> objs = objIn.getObjects();
			EArrayList<IEnhancedGuiObject> pObjs = objIn.getTopParent().getObjects();
			//I have no idea if this code even works
			if (objs != null) {
				if (objs.isEmpty()) {
					int thisObjPos = 0;
					for (int i = 0; i < pObjs.size(); i++) {
						if (pObjs.get(i).equals(objIn)) { thisObjPos = i; }
					}
					if (thisObjPos < pObjs.size() - 1) { pObjs.get(thisObjPos + 1).requestFocus(); }
				} else {
					EnhancedGuiObject selectedChild = null;
					for (IEnhancedGuiObject o : objIn.getTopParent().getObjects()) {
						if (objs.contains(o) && o instanceof EnhancedGuiObject) { selectedChild = (EnhancedGuiObject) o; }
					}
					if (selectedChild != null) {
						int childPos = 0;
						for (int i = 0; i < objs.size(); i++) {
							if (selectedChild.equals(objs.get(i))) { childPos = i; }
						}
						if (childPos < objs.size() - 1) { objs.get(childPos + 1).requestFocus(); }
					}
				}
			}
		}
	}
	public static void keyReleased(IEnhancedGuiObject objIn, char typedChar, int keyCode) {
		objIn.postEvent(new EventKeyboard(objIn, typedChar, keyCode, KeyboardType.Released));
	}
	
	public static void addObjects(IEnhancedGuiObject objIn, EArrayList<IEnhancedGuiObject> toBeAdded) {
		for (IEnhancedGuiObject o : toBeAdded) {
			if (o != null) {
				if (o != objIn) {
					objIn.getObjects().add(o);
					o.onAdded();
					objIn.postEvent(new EventObjects(objIn, o, ObjectEventType.ObjectAdded));
				}
			}
		}
		toBeAdded.clear();
	}

	public static void removeObjects(IEnhancedGuiObject objIn, EArrayList<IEnhancedGuiObject> toBeRemoved) {
		for (IEnhancedGuiObject o : toBeRemoved) {
			if (o != null) {
				if (o != objIn) {
					if (objIn.getObjects().contains(o)) {
						objIn.onClosed();
						objIn.getObjects().remove(o);
						objIn.postEvent(new EventObjects(objIn, o, ObjectEventType.ObjectRemoved));
					}
				}
			}
		}
		toBeRemoved.clear();
	}
	
	public static void setEnabled(boolean val, IEnhancedGuiObject... objs) { for (IEnhancedGuiObject o : objs) { o.setEnabled(val); } }
	public static void setVisible(boolean val, IEnhancedGuiObject... objs) { for (IEnhancedGuiObject o : objs) { o.setVisible(val); } }
	public static void setPersistent(boolean val, IEnhancedGuiObject... objs) { for (IEnhancedGuiObject o : objs) { o.setPersistent(val); } }
}
