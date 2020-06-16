package com.Whodundid.core.windowLibrary;

import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.renderUtil.CursorHelper;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.header.WindowHeader;
import com.Whodundid.core.windowLibrary.windowTypes.EnhancedGui;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.ITopParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowParent;
import com.Whodundid.core.windowLibrary.windowUtil.EGui;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.KeyboardType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.MouseType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.ObjectEventType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventKeyboard;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventModify;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventMouse;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventObjects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

//Author: Hunter Bragg

public class WindowObjectS extends EGui {
	
	private static volatile long windowPID = 0l;
	
	/** Returns the next pid that will be assigned to a requesting object. */
	public static synchronized long getPID() { return windowPID++; }
	
	//main draw
	public static void updateCursorImage(IWindowObject obj) {
		
		//make sure that the window isn't maximized
		if (obj instanceof IWindowParent && ((IWindowParent) obj).getMaximizedPosition() == ScreenLocation.center) {
			CursorHelper.updateCursor(null);
			return;
		}
		
		if (obj != null && obj.isResizeable() && obj.getTopParent().getModifyType() != ObjectModifyType.Resize) {
			int rStartY = obj.hasHeader() ? obj.getHeader().startY : obj.getDimensions().startY;
			if (!Mouse.isButtonDown(0)) {
				
				switch (obj.getEdgeAreaMouseIsOn()) {
				case top: case bot: CursorHelper.updateCursor(EMCResources.cursorResizeNS); break;
				case left: case right: CursorHelper.updateCursor(EMCResources.cursorResizeEW); break;
				case topRight: case botLeft: CursorHelper.updateCursor(EMCResources.cursorResizeDL); break;
				case topLeft: case botRight: CursorHelper.updateCursor(EMCResources.cursorResizeDR); break;
				default: CursorHelper.setCursor(null); break;
				}
			}
		}
		else { CursorHelper.updateCursor(null); }
	}
	
	public static void onMouseHover(IWindowObject obj, int mX, int mY, String hoverText, int textColor) {
		if (hoverText != null && !hoverText.isEmpty()) {
			ScaledResolution res = new ScaledResolution(mc);
			int strWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(hoverText);
			int sX = mX + 8;
			int sY = mY - 7;
			
			sX = sX < 0 ? 1 : sX;
			sY = (sY - 7) < 2 ? 2 + 7 : sY;
			if (sX + strWidth + 10 > res.getScaledWidth()) {
				sX = -1 + sX - (sX + strWidth + 10 - res.getScaledWidth() + 6);
				sY -= 10;
			}
			sY = sY + 16 > res.getScaledHeight() ? -2 + sY - (sY + 16 - res.getScaledHeight() + 6) : sY;
			
			int eX = sX + strWidth + 10;
			int eY = sY + 16;
			
			drawRect(sX, sY, sX + strWidth + 10, sY + 16, EColors.black);
			drawRect(sX + 1, sY + 1, eX - 1, eY - 1, EColors.steel);
			drawStringWithShadow(hoverText, sX + 5, sY + 4, textColor);
		}
	}
	
	//size
	/** Returns true if the object has an EGuiHeader. */
	public static boolean hasHeader(IWindowObject obj) { return getHeader(obj) != null; }
	
	/** Returns this objects WindowHeader, if there is one. */
	public static WindowHeader getHeader(IWindowObject obj) {
		for (IWindowObject o : EArrayList.combineLists(obj.getObjects(), obj.getAddingObjects())) {
			if (o instanceof WindowHeader) { return (WindowHeader) o; }
		}
		return null;
	}
	
	/** Generic object resizing algorithm. */
	public static void resize(IWindowObject obj, int xIn, int yIn, ScreenLocation areaIn) {
		obj.postEvent(new EventModify(obj, obj, ObjectModifyType.Resize)); //post an event
		if (xIn != 0 || yIn != 0) { //make sure that there is actually a change in the cursor position
			EDimension d = obj.getDimensions();
			//EDimension c = obj.getClickableArea();
			int x = 0, y = 0, w = 0, h = 0;
			boolean e = false, s = false;
			//perform resizing on different sides depending on the side that's being resized
			switch (areaIn) {
			case top:
				x = d.startX; y = d.startY + yIn; w = d.width; h = d.height - yIn;
				//obj.setClickableArea(c.startX, c.startY + yIn, c.width, c.height - yIn);
				break;
			case bot:
				x = d.startX; y = d.startY; w = d.width; h = d.height + yIn;
				//obj.setClickableArea(c.startX, c.startY, c.width, c.height + yIn);
				break;
			case right:
				x = d.startX; y = d.startY; w = d.width + xIn; h = d.height;
				//obj.setClickableArea(c.startX, c.startY, c.width + xIn, c.height);
				break;
			case left:
				x = d.startX + xIn; y = d.startY; w = d.width - xIn; h = d.height;
				//obj.setClickableArea(c.startX + xIn, c.startY, c.width - xIn, c.height);
				break;
			case topRight:
				x = d.startX; y = d.startY + yIn; w = d.width + xIn; h = d.height - yIn;
				//obj.setClickableArea(c.startX, c.startY + yIn, c.width + xIn, c.height - yIn);
				break;
			case botRight:
				x = d.startX; y = d.startY; w = d.width + xIn; h = d.height + yIn;
				//obj.setClickableArea(c.startX, c.startY, c.width + xIn, c.height + yIn);
				break;
			case topLeft:
				x = d.startX + xIn; y = d.startY + yIn; w = d.width - xIn; h = d.height - yIn;
				//obj.setClickableArea(c.startX + xIn, c.startY + yIn, c.width - xIn, c.height - yIn);
				break;
			case botLeft:
				x = d.startX + xIn; y = d.startY; w = d.width - xIn; h = d.height + yIn;
				//obj.setClickableArea(c.startX + xIn, c.startY, c.width - xIn, c.height + yIn);
				break;
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
			
			//set the dimensions of the object to the resized dimensions
			obj.setDimensions(x, y, w, h);
			
			//(lazy approach) remake all the children based on the resized dimensions
			obj.reInitObjects();
			if (obj.getTopParent() != null) { obj.getTopParent().setFocusedObject(obj); }
		}
	}
	
	//position
	/** Translates the specified object by a given x and y amount. */
	public static void move(IWindowObject obj, int newX, int newY) {
		obj.postEvent(new EventModify(obj, obj, ObjectModifyType.Move)); //post an event
		
		if (obj.isMoveable()) { //only allow the object to be moved if it's not locked in place
			//get all of the children in the object that aren't locked in place
			for (IWindowObject o : EArrayList.combineLists(obj.getObjects(), obj.getAddingObjects())) {
				if (o.isMoveable()) { //only move the child if it's not locked in place
					if (o instanceof WindowParent) { //only move the window if it moves with the parent
						if (((WindowParent) o).movesWithParent()) { o.move(newX, newY); }
					}
					else { o.move(newX, newY); }
				}
			}
			EDimension d = obj.getDimensions();
			obj.setDimensions(d.startX + newX, d.startY + newY, d.width, d.height); //offset the original position by the specified offset
			//obj.getClickableArea().move(newX, newY);
			if (obj.isBoundaryEnforced()) { //also move the boundary enforcer, if there is one
				EDimension b = obj.getBoundaryEnforcer();
				obj.getBoundaryEnforcer().setPosition(b.startX + newX, b.startY + newY);
			}
		}
	}
	
	/** Moves the object to the specified x and y coordinates. */
	public static void setPosition(IWindowObject obj, int newX, int newY) {
		//only move this object and its children if it is moveable
		if (obj.isMoveable()) {
			EDimension d = obj.getDimensions();
			//EDimension c = obj.getClickableArea();
			
			//the object's current position and relative clickArea for shorter code
			StorageBox<Integer, Integer> loc = new StorageBox(d.startX, d.startY);
			//StorageBox<Integer, Integer> relLoc = new StorageBox(d.startX - c.startX, d.startY - c.startY);
			
			//holder to store each object and their relative child locations
			StorageBoxHolder<IWindowObject, StorageBox<Integer, Integer>> previousLocations = new StorageBoxHolder();
			
			//grab all immediate objects
			EArrayList<IWindowObject> objs = EArrayList.combineLists(obj.getObjects(), obj.getAddingObjects());
			
			//get each of the object's children's relative positions and clickable areas relative to each child
			for (IWindowObject o : objs) {
				previousLocations.add(o, new StorageBox(o.getDimensions().startX - loc.getObject(), o.getDimensions().startY - loc.getValue()));
				//new StorageBox(o.getClickableArea().startX - o.getDimensions().startX, o.getClickableArea().startY - o.getDimensions().startY))
			}
			
			//apply the new location to parent
			obj.setDimensions(newX, newY, d.width, d.height); //move the object to the new position
			//obj.setClickableArea(newX + relLoc.getObject(), newY + relLoc.getValue(), c.width, c.height);
			
			//apply the new location to each child
			for (IWindowObject o : objs) {
				//don't move the child if its position is locked
				if (o.isMoveable()) {
					StorageBox<Integer, Integer> oldLoc = previousLocations.getBoxWithObj(o).getValue();
					//StorageBox<Integer, Integer> oldClick = previousLocations.getBoxWithObj(o).getValue().getValue();
					
					//move the child to the new location with the parent's offest
					o.setPosition(newX + oldLoc.getObject(), newY + oldLoc.getValue()); 
					//o.setClickableArea(o.getDimensions().startX + oldClick.getObject(), o.getDimensions().startY + oldClick.getObject(), o.getClickableArea().width, o.getClickableArea().height);
				}
			}
		}
	}
	
	/** Centers the object and all of its children in the middle of the screen with the specified dimensions. */
	public static void centerObjectWithSize(IWindowObject obj, int widthIn, int heightIn) {
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft()); //get the screen size
		int sWidth = res.getScaledWidth();
		int sHeight = res.getScaledHeight();
		int startX, startY, width, height;
		
		if (sWidth >= widthIn) { //check if the screen width is larger than the desired object width
			//if it is, set the xPos so that it will be in the middle of the screen
			startX = (sWidth - widthIn) / 2;
			width = widthIn;
		}
		else { //otherwise, restrict the object's width to the screen's width
			startX = 0;
			width = sWidth;
		}
		
		if (sHeight >= heightIn) { //check if the screen height is larger than the desired object height
			//if it is, set the yPos so that it will be in the middle of the screen
			startY = (sHeight - heightIn) / 2;
			height = heightIn;
		}
		else { //otherwise, restrict the object's width to the screen's height
			startY = 0;
			height = sHeight;
		}
		
		obj.setDimensions(startX, startY, width, height); //apply the dimensions to the object
		//obj.setClickableArea(startX, startY, startX + width, startY + height); //aplly the dimension to the clickableArea
	}
	
	//objects
	/** Returns true if the given object is a child of the specified parent. */
	public static boolean isChildOfObject(IWindowObject child, IWindowObject parent) {
		IWindowObject parentObj = child.getParent();
		
		//recursively check through the object's parent lineage to see if that parent is the possible parent
		while (parentObj != null) {
			if (parentObj == parent) { return true; }
			parentObj = parentObj.getParent();
		}
		
		return false;
	}
	
	/** Start the process of adding a child to this object. Children are fully added on the next draw cycle. 
	 *  There is an issue where a child of a child can be added to the parent again. */
	public static void addObject(IWindowObject parent, IWindowObject obj, IWindowObject... objsIn) {
		for (IWindowObject o : EUtil.add(obj, objsIn)) {
			try {
				if (o != null) { //only add if the object isn't null
					//don't let the object be added to itself, or if the object is already in the object - this only goes 1 layer deep however
					if (o != parent && parent.getObjects().notContains(o) && parent.getAddingObjects().notContains(o)) {
						if (o instanceof EnhancedGui) { continue; } //don't add GuiScreens
						if (o instanceof WindowHeader && parent.hasHeader()) { continue; } //prevent multiple headers being added
						if (o instanceof WindowParent) { ((WindowParent) o).initWindow(); } //if it's a window, do it's init
						o.setParent(parent).initObjects(); //initialize all of the children's children
						o.setZLevel(parent.getZLevel() + o.getZLevel() + 1); //increment the child's z layer based off of the parent
						//if the parent has a boundary enforcer, apply it to the child as well
						if (parent.isBoundaryEnforced()) { o.setBoundaryEnforcer(parent.getBoundaryEnforcer()); }
						o.completeInit(); //tell the child that it has been fully initialized and that it is ready to be added on the next draw cycle
						parent.getAddingObjects().add(o); //give the processed child to the parent so that it will be added
					}
					else {
						System.out.println(parent + " already contains " + o + "!");
					}
				}
			}
			catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	/** Start the process of removing a child from this object. Children are fully removed on the next draw cycle. */
	public static void removeObject(IWindowObject parent, IWindowObject obj, IWindowObject... objsIn) {
		IWindowObject[] objs = EUtil.add(obj, objsIn);
		EUtil.filterNullDo(o -> o.setBeingRemoved(), objs);
		parent.getRemovingObjects().addAll(objs);
	}
	
	/** Returns a list containing every single child from every object in the specified object. */
	public static EArrayList<IWindowObject> getAllChildren(IWindowObject obj) {
		EArrayList<IWindowObject> foundObjs = new EArrayList();
		EArrayList<IWindowObject> objsWithChildren = new EArrayList();
		EArrayList<IWindowObject> workList = new EArrayList();
		
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
			EUtil.filterDo(o -> !o.getObjects().isEmpty(), objsWithChildren::add, workList);
			EUtil.filterDo(o -> !o.getAddingObjects().isEmpty(), objsWithChildren::add, workList);
			
			//put all children on the next layer into the work list
			workList.clear();
			objsWithChildren.forEach(c -> workList.addAll(c.getObjects()));
			objsWithChildren.forEach(c -> workList.addAll(c.getAddingObjects()));
		}
		
		//System.out.println(System.nanoTime() - start);
		
		return foundObjs;
	}
	
	/** Returns a list of all children currently under the cursor. */
	public static EArrayList<IWindowObject> getAllChildrenUnderMouse(IWindowObject obj, int mX, int mY) {
		EArrayList<IWindowObject> l = new EArrayList();
		//only add objects if they are visible and if the cursor is over them.
		EUtil.filterNullDo(o -> o.checkDraw() && o.isMouseInside(mX, mY), l::add, obj.getAllChildren());
		return l;
	}
	
	//parents
	/* Returns the topParent for the specified object. */
	public static ITopParent getTopParent(IWindowObject obj) {
		IWindowObject parentObj = obj.getParent();
		//recursively check through the object's parent lineage to see if that parent is a topParent
		while (parentObj != null) {
			if (parentObj instanceof ITopParent) { return (ITopParent) parentObj; }
			parentObj = parentObj.getParent();
		}
		
		return obj instanceof ITopParent ? (ITopParent) obj : null;
	}
	
	/** Returns the parent window for the specified object, if there is one. */
	public static IWindowParent getWindowParent(IWindowObject obj) {
		IWindowObject parentObject = obj.getParent();
		//recursively check through the object's parent lineage to see if that parent is a window
		while (parentObject != null && !(parentObject instanceof ITopParent)) {
			if (parentObject instanceof IWindowParent) { return (IWindowParent) parentObject; }
			parentObject = parentObject.getParent();
		}
		
		return obj instanceof IWindowParent ? (IWindowParent) obj : null;
	}
	
	//zLevel
	public static int getZLevel(IWindowObject obj, int objZLevel) {
		int zLevel = new Integer(objZLevel);
		IWindowObject lastObj = obj.getParent();
		if (lastObj != null && !lastObj.equals(obj)) {
			while (lastObj != null && lastObj.getParent() != lastObj) {
				zLevel += lastObj.getZLevel();
				lastObj = lastObj.getParent();
			}
		}
		return zLevel;
	}
	
	//mouse checks
	public static ScreenLocation getEdgeAreaMouseIsOn(IWindowObject objIn, int mX, int mY) {
		boolean left = false, right = false, top = false, bottom = false;
		EDimension d = objIn.getDimensions();
		int rStartY = objIn.hasHeader() ? objIn.getHeader().startY : d.startY;
		if (mX >= d.startX - 2 && mX <= d.endX + 2 && mY >= rStartY - 2 && mY <= d.endY + 1) {
			if (mX >= d.startX - 2 && mX <= d.startX) { left = true; }
			if (mX >= d.endX - 1 && mX <= d.endX + 2) { right = true; }
			if (mY >= rStartY - 3 && mY <= rStartY) { top = true; }
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
	
	public static boolean isMouseInside(IWindowObject obj, int mX, int mY) {
		if (obj != null) {
			EDimension d = obj.getDimensions();
			if (obj.isBoundaryEnforced()) {
				EDimension b = obj.getBoundaryEnforcer();
				return mX >= d.startX && mX >= b.startX && mX <= d.endX && mX <= b.endX && mY >= d.startY && mY >= b.startY && mY <= d.endY && mY <= b.endY;
			}
			return mX >= d.startX && mX <= d.endX && mY >= d.startY && mY <= d.endY;
		}
		return false;
	}
	
	//basic inputs
	public static void parseMousePosition(IWindowObject objIn, int mX, int mY) { objIn.getObjects().stream().filter(o -> o.isMouseInside(mX, mY)).forEach(o -> o.parseMousePosition(mX, mY)); }
	
	public static void mousePressed(IWindowObject objIn, int mX, int mY, int button) {
		objIn.postEvent(new EventMouse(objIn, mX, mY, button, MouseType.Pressed));
		IWindowParent p = objIn.getWindowParent();
		if (p != null) { p.bringToFront(); }
		if (!objIn.hasFocus() && objIn.isMouseOver(mX, mY)) { objIn.requestFocus(); }
		if (button == 0 && objIn.isResizeable() && !objIn.getEdgeAreaMouseIsOn().equals(ScreenLocation.out)) {
			objIn.getTopParent().setResizingDir(objIn.getEdgeAreaMouseIsOn());
			objIn.getTopParent().setModifyMousePos(mX, mY);
			objIn.getTopParent().setModifyingObject(objIn, ObjectModifyType.Resize);
		}
	}
	
	public static void mouseReleased(IWindowObject objIn, int mX, int mY, int button) {
		objIn.postEvent(new EventMouse(objIn, mX, mY, button, MouseType.Released));
		if (objIn.getTopParent().getDefaultFocusObject() != null) { objIn.getTopParent().getDefaultFocusObject().requestFocus(); }
		if (objIn.getTopParent().getModifyType() == ObjectModifyType.Resize) { objIn.getTopParent().clearModifyingObject(); }
	}
	
	public static void mouseDragged(IWindowObject objIn, int mX, int mY, int button, long timeSinceLastClick) {}
	
	public static void mouseScolled(IWindowObject objIn, int mX, int mY, int change) {
		objIn.postEvent(new EventMouse(objIn, mX, mY, -1, MouseType.Scrolled));
		EUtil.filterDo(o -> o.isMouseInside(mX, mY) && o.checkDraw(), o -> o.mouseScrolled(change), objIn.getObjects());
	}
	
	public static void keyPressed(IWindowObject objIn, char typedChar, int keyCode) {
		objIn.postEvent(new EventKeyboard(objIn, typedChar, keyCode, KeyboardType.Pressed));
		if (objIn.getTopParent() != null && keyCode == 15) {
			EArrayList<IWindowObject> objs = objIn.getObjects();
			EArrayList<IWindowObject> pObjs = objIn.getTopParent().getObjects();
			//I have no idea if this code even works
			if (objs != null) {
				if (objs.isEmpty()) {
					int thisObjPos = 0;
					for (int i = 0; i < pObjs.size(); i++) {
						if (pObjs.get(i).equals(objIn)) { thisObjPos = i; }
					}
					if (thisObjPos < pObjs.size() - 1) { pObjs.get(thisObjPos + 1).requestFocus(); }
				}
				else {
					IWindowObject selectedChild = null;
					for (IWindowObject o : objIn.getTopParent().getObjects()) {
						if (objs.contains(o) && o instanceof IWindowObject) { selectedChild = (IWindowObject) o; }
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
	
	public static void keyReleased(IWindowObject objIn, char typedChar, int keyCode) {
		objIn.postEvent(new EventKeyboard(objIn, typedChar, keyCode, KeyboardType.Released));
	}
	
	public static void setEntiretyClickable(IWindowObject objIn, boolean val) {
		objIn.getAllChildren().forEach(o -> o.setClickable(val));
		objIn.setClickable(val);
	}
	
	public static void addObjects(IWindowObject objIn, EArrayList<IWindowObject> toBeAdded) {
		for (IWindowObject o : toBeAdded) {
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

	public static void removeObjects(IWindowObject objIn, EArrayList<IWindowObject> toBeRemoved) {
		for (IWindowObject o : toBeRemoved) {
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
	
}
