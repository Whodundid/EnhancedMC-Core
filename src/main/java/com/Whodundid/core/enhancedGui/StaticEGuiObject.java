package com.Whodundid.core.enhancedGui;

import com.Whodundid.core.enhancedGui.guiObjects.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventKeyboard;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventModify;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventMouse;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventObjects;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.KeyboardType;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.MouseType;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.ObjectEventType;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.ObjectModifyType;
import com.Whodundid.core.enhancedGui.guiUtil.exceptions.HeaderAlreadyExistsException;
import com.Whodundid.core.enhancedGui.guiUtil.exceptions.ObjectInitException;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedTopParent;
import com.Whodundid.core.util.miscUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class StaticEGuiObject {
	
	//size
	public static void resize(IEnhancedGuiObject obj, int xIn, int yIn, ScreenLocation areaIn) {
		obj.postEvent(new EventModify(obj, obj, ObjectModifyType.Resize));
		if (xIn != 0 || yIn != 0) {
			EDimension d = obj.getDimensions();
			int x = 0, y = 0, w = 0, h = 0;
			boolean e = false, s = false;
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
			if (w < obj.getMinimumWidth()) {
				w = obj.getMinimumWidth();
				switch (areaIn) {
				case right: case topRight: case botRight: x = d.startX; break;
				case left: case topLeft: case botLeft: x = d.endX - w; break;
				default: break;
				}
			}
			if (w > obj.getMaximumWidth()) {
				w = obj.getMaximumWidth();
				switch (areaIn) {
				case right: case topRight: case botRight: x = d.startX; break;
				case left: case topLeft: case botLeft: x = d.endX - w; break;
				default: break;
				}
			}
			if (h < obj.getMinimumHeight()) {
				h = obj.getMinimumHeight();
				switch (areaIn) {
				case top: case topRight: case topLeft: y = d.endY - h; break;
				case bot: case botRight: case botLeft: y = d.startY; break;
				default: break;
				}
			}
			if (h > obj.getMaximumHeight()) {
				h = obj.getMaximumHeight();
				switch (areaIn) {
				case top: case topRight: case topLeft: y = d.endY - h; break;
				case bot: case botRight: case botLeft: y = d.startY; break;
				default: break;
				}
			}
			obj.setDimensions(x, y, w, h);
			try {
				obj.reInitObjects();
			} catch (ObjectInitException q) { q.printStackTrace(); }
		}
	}
	
	//position
	public static void move(IEnhancedGuiObject obj, int newX, int newY) {
		obj.postEvent(new EventModify(obj, obj, ObjectModifyType.Move));
		if (!obj.isPositionLocked()) {
			EArrayList<IEnhancedGuiObject> objs = new EArrayList(obj.getImmediateChildren());
			objs.addAll(obj.getObjectsToBeAdded());
			Iterator<IEnhancedGuiObject> it = objs.iterator();
			while (it.hasNext()) {
				IEnhancedGuiObject o = it.next();
				if (!o.isPositionLocked()) {
					if (o instanceof InnerEnhancedGui) {
						if (((InnerEnhancedGui) o).movesWithParent()) { o.move(newX, newY); }
					} else { o.move(newX, newY); }
				}
			}
			EDimension d = obj.getDimensions();
			obj.setDimensions(d.startX + newX, d.startY + newY, d.width, d.height);
			if (obj.isBoundaryEnforced()) {
				EDimension b = obj.getBoundaryEnforcer();
				obj.getBoundaryEnforcer().setPosition(b.startX + newX, b.startY + newY);
			}
		}
	}
	public static void setPosition(IEnhancedGuiObject obj, int newX, int newY) {
		EDimension d = obj.getDimensions();
		StorageBox<Integer, Integer> loc = new StorageBox(d.startX, d.startY);
		StorageBoxHolder<IEnhancedGuiObject, StorageBox<Integer, Integer>> previousLocations = new StorageBoxHolder();
		EArrayList<IEnhancedGuiObject> objs = new EArrayList();
		objs.addAll(obj.getImmediateChildren());
		objs.addAll(obj.getObjectsToBeAdded());
		for (IEnhancedGuiObject o : objs) {
			previousLocations.add(o, new StorageBox(o.getDimensions().startX - loc.getObject(), o.getDimensions().startY - loc.getValue()));
		}
		obj.setDimensions(newX, newY, d.width, d.height);
		for (IEnhancedGuiObject o : objs) {
			StorageBox<Integer, Integer> oldLoc = previousLocations.getBoxWithObj(o).getValue();
			o.setPosition(newX + oldLoc.getObject(), newY + oldLoc.getValue());
		}
	}
	public static void centerObjectWithSize(IEnhancedGuiObject obj, int widthIn, int heightIn) {
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
		int sWidth = res.getScaledWidth();
		int sHeight = res.getScaledHeight();
		int startX, startY, width, height;
		if (sWidth >= widthIn) {
			startX = (sWidth - widthIn) / 2;
			width = widthIn;
		} else {
			startX = 0;
			width = sWidth;
		};
		if (sHeight >= heightIn) {
			startY = (sHeight - heightIn) / 2;
			height = heightIn;
		} else {
			startY = 0;
			height = sHeight;
		}
		obj.setDimensions(startX, startY, width, height);
	}
	
	//objects
	public static boolean isChildOfObject(IEnhancedGuiObject child, IEnhancedGuiObject parent) {
		IEnhancedGuiObject curObj = child;
		while (curObj != null && curObj.getParent() != null) {
			if (curObj.getParent().equals(curObj)) { return false; }
			if (curObj.getParent().equals(parent)) { return true; }
			curObj = curObj.getParent();
		}
		return false;
	}
	public static void addObject(IEnhancedGuiObject parent, IEnhancedGuiObject... objsIn) {
		EArrayList<IEnhancedGuiObject> addingObjects = new EArrayList();
		for (IEnhancedGuiObject o : objsIn) {
			try {
				if (o != null) {
					if (o != parent && parent.getImmediateChildren().notContains(o) && parent.getObjectsToBeAdded().notContains(o)) {
					//if (o != parent && parent.getImmediateChildren().notContains(o)) {
						if (o instanceof EnhancedGui) { continue; }
						if (o instanceof EGuiHeader && parent.hasHeader()) { 
							throw new HeaderAlreadyExistsException(parent.getHeader());
						}
						try {
							if (o instanceof InnerEnhancedGui) { ((InnerEnhancedGui) o).initGui(); }
							o.setParent(parent).initObjects();
							o.setZLevel(parent.getZLevel() + o.getZLevel() + 1);
							if (parent.isBoundaryEnforced()) { o.setBoundaryEnforcer(parent.getBoundaryEnforcer()); }
							o.completeInitialization();
						} catch (ObjectInitException e) { e.printStackTrace(); }
						addingObjects.add(o);
						//parent.getImmediateChildren().add(o);
						//o.onAdded();
						//parent.postEvent(new EventObjects(parent, o, ObjectEventType.ObjectAdded));
					}
				}
			} catch (HeaderAlreadyExistsException e) { e.printStackTrace(); }
		}
		parent.getObjectsToBeAdded().addAll(addingObjects);
	}
	public static void removeObject(IEnhancedGuiObject parent, IEnhancedGuiObject... objsIn) {
		//for (IEnhancedGuiObject o : objsIn) {
		//	if (o != null && parent.getImmediateChildren().contains(o)) {
		//		o.onClosed();
		//		o.getImmediateChildren().remove(o);
		//		o.postEvent(new EventObjects(parent, o, ObjectEventType.ObjectRemoved));
		//	}
		//}
		
		parent.getObjectsToBeRemoved().addAll(objsIn);
	}
	public static EArrayList<IEnhancedGuiObject> getAllChildren(IEnhancedGuiObject obj) {
		EArrayList<IEnhancedGuiObject> foundObjs = new EArrayList();
		EArrayList<IEnhancedGuiObject> objsWithChildren = new EArrayList();
		EArrayList<IEnhancedGuiObject> workList = new EArrayList();
		
		//grab all immediate children and add them to foundObjs, then check if any have children of their own
		obj.getImmediateChildren().forEach(o -> { foundObjs.add(o); if (!o.getImmediateChildren().isEmpty()) { objsWithChildren.add(o); } });
		//same as above but now check from objects that are going to be added on the next draw cycle
		obj.getObjectsToBeAdded().forEach(o -> { foundObjs.add(o); if (!o.getObjectsToBeAdded().isEmpty()) { objsWithChildren.add(o); } });
		//load the workList with every child found on each object
		objsWithChildren.forEach(c -> workList.addAll(c.getImmediateChildren()));
		objsWithChildren.forEach(c -> workList.addAll(c.getObjectsToBeAdded()));
		
		while (true) {
			if (!workList.isEmpty()) {
				foundObjs.addAll(workList);
				objsWithChildren.clear();
				workList.stream().filter(o -> !o.getImmediateChildren().isEmpty()).forEach(objsWithChildren::add);
				workList.stream().filter(o -> !o.getObjectsToBeAdded().isEmpty()).forEach(objsWithChildren::add);
				workList.clear();
				objsWithChildren.forEach(c -> workList.addAll(c.getImmediateChildren()));
				objsWithChildren.forEach(c -> workList.addAll(c.getObjectsToBeAdded()));
			} else { break; }
		}
		
		return foundObjs;
	}
	public static EArrayList<IEnhancedGuiObject> getAllChildrenUnderMouse(IEnhancedGuiObject obj, int mX, int mY) {
		EArrayList<IEnhancedGuiObject> l = new EArrayList();
		obj.getAllChildren().stream().filter(o -> o.checkDraw() && o.isMouseInside(mX, mY)).forEach(l::add);
		return l;
	}
	
	//parents
	public static IEnhancedTopParent getTopParent(IEnhancedGuiObject obj) {
		IEnhancedGuiObject parentObj = obj.getParent();
		while (parentObj != null) {
			if (parentObj instanceof IEnhancedTopParent) { return (IEnhancedTopParent) parentObj; }
			if (parentObj.getParent() != null) { parentObj = parentObj.getParent(); }
		}
		return obj instanceof IEnhancedTopParent ? (IEnhancedTopParent) obj : null;
	}
	public static IEnhancedGuiObject getWindowParent(IEnhancedGuiObject obj) {
		IEnhancedGuiObject parentObject = obj.getParent();
		while (parentObject != null && !(parentObject instanceof IEnhancedTopParent)) {
			if (parentObject instanceof InnerEnhancedGui) { return parentObject; }
			if (parentObject.getParent() != null) { parentObject = parentObject.getParent(); }
		}
		return obj instanceof InnerEnhancedGui ? obj : obj.getTopParent();
	}
	
	//mouse checks
	public static ScreenLocation getEdgeAreaMouseIsOn(IEnhancedGuiObject objIn, int mX, int mY) {
		boolean left = false, right = false, top = false, bottom = false;
		EDimension d = objIn.getDimensions();
		int rStartY = objIn.hasHeader() ? objIn.getHeader().startY : d.startY;
		if (mX >= d.startX && mX <= d.startX + 1) { left = true; }
		if (mX <= d.endX && mX >= d.endX - 1) { right = true; }
		if (mY >= rStartY && mY <= rStartY + 1) { top = true; }
		if (mY <= d.endY && mY >= d.endY - 1) { bottom = true; }
		if (objIn.checkDraw() && !(left || right || top || bottom)) { return ScreenLocation.out; }
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
		else { return ScreenLocation.bot; }
	}
	
	//basic inputs
	public static void parseMousePosition(IEnhancedGuiObject objIn, int mX, int mY) { objIn.getImmediateChildren().stream().filter(o -> o.isMouseInside(mX, mY)).forEach(o -> o.parseMousePosition(mX, mY)); }
	public static void mousePressed(IEnhancedGuiObject objIn, int mX, int mY, int button) {
		objIn.postEvent(new EventMouse(objIn, mX, mY, button, MouseType.Pressed));
		if (objIn.isMouseHover(mX, mY)) { objIn.requestFocus(); }
		if (button == 0 && objIn.isResizeable() && !objIn.getEdgeAreaMouseIsOn().equals(ScreenLocation.out)) {
			objIn.getTopParent().setResizingDir(objIn.getEdgeAreaMouseIsOn());
			objIn.getTopParent().setModifyMousePos(mX, mY);
			objIn.getTopParent().setModifyingObject(objIn, ObjectModifyType.Resize);
		}
		IEnhancedGuiObject window = objIn.getWindowParent();
		if (window instanceof InnerEnhancedGui) { window.bringToFront(); }
	}
	public static void mouseReleased(IEnhancedGuiObject objIn, int mX, int mY, int button) {
		objIn.postEvent(new EventMouse(objIn, mX, mY, button, MouseType.Released));
		if (objIn.getTopParent().isResizing()) { objIn.getTopParent().clearModifyingObject(); }
		if (objIn.getTopParent().getDefaultFocusObject() != null) { objIn.getTopParent().getDefaultFocusObject().requestFocus(); }
	}
	public static void mouseDragged(IEnhancedGuiObject objIn, int mX, int mY, int button, long timeSinceLastClick) {}
	public static void mouseScolled(IEnhancedGuiObject objIn, int mX, int mY, int change) {
		objIn.postEvent(new EventMouse(objIn, mX, mY, -1, MouseType.Scrolled));
		objIn.getImmediateChildren().forEach(o -> o.mouseScrolled(change));
	}
	public static void keyPressed(IEnhancedGuiObject objIn, char typedChar, int keyCode) {
		objIn.postEvent(new EventKeyboard(objIn, typedChar, keyCode, KeyboardType.Pressed));
		if (objIn.getTopParent() != null && keyCode == 15) {
			EArrayList<IEnhancedGuiObject> objs = objIn.getImmediateChildren();
			EArrayList<IEnhancedGuiObject> pObjs = objIn.getTopParent().getImmediateChildren();
			if (objs != null) {
				if (objs.isEmpty()) {
					int thisObjPos = 0;
					for (int i = 0; i < pObjs.size(); i++) {
						if (pObjs.get(i).equals(objIn)) { thisObjPos = i; }
					}
					if (thisObjPos < pObjs.size() - 1) { pObjs.get(thisObjPos + 1).requestFocus(); }
				} else {
					EnhancedGuiObject selectedChild = null;
					for (IEnhancedGuiObject o : objIn.getTopParent().getImmediateChildren()) {
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
					objIn.getImmediateChildren().add(o);
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
					if (objIn.getImmediateChildren().contains(o)) {
						objIn.onClosed();
						objIn.getImmediateChildren().remove(o);
						objIn.postEvent(new EventObjects(objIn, o, ObjectEventType.ObjectRemoved));
					}
				}
			}
		}
		toBeRemoved.clear();
	}
	
	public static void setEnabled(boolean val, IEnhancedGuiObject... objs) { for (IEnhancedGuiObject o : objs) { o.setEnabled(val); } }
	public static void setVisibility(boolean val, IEnhancedGuiObject... objs) { for (IEnhancedGuiObject o : objs) { o.setVisible(val); } }
	public static void setPersistence(boolean val, IEnhancedGuiObject... objs) { for (IEnhancedGuiObject o : objs) { o.setPersistent(val); } }
}
