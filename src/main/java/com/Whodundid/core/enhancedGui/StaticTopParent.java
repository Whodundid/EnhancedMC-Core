package com.Whodundid.core.enhancedGui;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.coreApp.CoreApp;
import com.Whodundid.core.coreEvents.emcEvents.EMCAppCalloutEvent;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiUtil.EGui;
import com.Whodundid.core.enhancedGui.objectEvents.EventFocus;
import com.Whodundid.core.enhancedGui.objectEvents.EventKeyboard;
import com.Whodundid.core.enhancedGui.objectEvents.EventMouse;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.FocusType;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.KeyboardType;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.MouseType;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.ObjectModifyType;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedTopParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.renderer.renderUtil.RendererRCM;
import com.Whodundid.core.renderer.taskView.TaskBar;
import com.Whodundid.core.util.miscUtil.EMouseHelper;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import java.util.Deque;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.lwjgl.input.Keyboard;

//Author: Hunter Bragg

public class StaticTopParent extends EGui {

	//basic inputs
	/** Notify the focused object that a mouse button was just pressed. */
	public static void mousePressed(IEnhancedTopParent objIn, int mX, int mY, int button, Deque<EventFocus> focusQueue) {
		objIn.postEvent(new EventMouse(objIn, mX, mY, button, MouseType.Pressed)); //post an event
		IEnhancedGuiObject underMouse = objIn.getHighestZObjectUnderMouse(); //get the highest object under the mouse
		
		if (objIn.getFocusLockObject() != null) { //first check if there is a focusLock
			if (underMouse != null) { //if there is, then check if there is actually anything under the cursor
				//allow focus to be passed to the object under the cursor if it is the focusLockObject, a child of the focusLockObject or an EGuiHeader
				if (underMouse.equals(objIn.getFocusLockObject()) || underMouse.isChild(objIn.getFocusLockObject()) || underMouse instanceof EGuiHeader) {
					focusQueue.add(new EventFocus(objIn, underMouse, FocusType.MousePress, button, mX, mY));
				} else { objIn.getFocusLockObject().bringToFront(); objIn.getFocusLockObject().drawFocusLockBorder(); } //otherwise, annoy the user
			} else { objIn.getFocusLockObject().bringToFront(); objIn.getFocusLockObject().drawFocusLockBorder(); } //otherwise, annoy the user
		}
		else if (underMouse != null) { //if there is no lock, check if there was actually an object under the cursor
			//check if the object is the focused object, if it is, pass the event to it, otherwise, start a focus request
			if (underMouse.equals(objIn.getFocusedObject())) { objIn.getFocusedObject().mousePressed(mX, mY, button); }
			else { focusQueue.add(new EventFocus(objIn, underMouse, FocusType.MousePress, button, mX, mY)); }
		}
		else { //there was no lock and there was nothing under the cursor
			objIn.clearFocusedObject();
			if (button == 1) { //open a right click menu if the right mouse button was pressed
				EnhancedMC.displayWindow(new RendererRCM(), CenterType.cursorCorner);
			}
		}
	}
	/** Notify the focused object that a mouse button was just released. */
	public static void mouseReleased(IEnhancedTopParent objIn, int mX, int mY, int button) {
		objIn.postEvent(new EventMouse(objIn, mX, mY, button, MouseType.Released)); //post an event
		//clear the modifying object if it the modify type was MoveAlreadyClicked -- mainly used for headers to stop moving when the mouse is released
		if (objIn.getModifyingObject() != null && objIn.getModifyType() == ObjectModifyType.MoveAlreadyClicked) {
			objIn.setModifyingObject(objIn.getModifyingObject(), ObjectModifyType.None);
		}
		//pass the event to the focused object, if it exists and if it isn't the parent itself
		if (objIn.getFocusedObject() != null && objIn.getFocusedObject() != objIn) { objIn.getFocusedObject().mouseReleased(mX, mY, button); }
		if (objIn.getModifyType() == ObjectModifyType.Resize) { objIn.clearModifyingObject(); } //stop resizing windows when the mouse isn't pressed
		if (objIn.getDefaultFocusObject() != null) { objIn.getDefaultFocusObject().requestFocus(); } //transfer focus back to the defaultFocusObject, if it exists
	}
	/** Notify the focused object that the mouse was just dragged. */
	public static void mouseDragged(IEnhancedTopParent objIn, int mX, int mY, int button, long timeSinceLastClick) {
		IEnhancedGuiObject fo = objIn.getFocusedObject();
		if (fo != null && fo != objIn) { fo.mouseDragged(mX, mY, button, timeSinceLastClick); }
	}
	/** Notify any objects under the cursor that the mouse was just scrolled. */
	public static void mouseScrolled(IEnhancedTopParent objIn, int mX, int mY, int change) {
		objIn.postEvent(new EventMouse(objIn, mX, mY, -1, MouseType.Scrolled));
		if (objIn.getHighestZObjectUnderMouse() != null) { //if there are actually any objects under the mouse
			IEnhancedGuiObject obj = objIn.getHighestZObjectUnderMouse();
			IWindowParent p = obj.getWindowParent();
			//only scroll the top most window under the mouse
			if (p != null) { p.mouseScrolled(change); }
			else { obj.mouseScrolled(change); }
		} else { //if there were no objects under the mouse, scroll the chat
			if (RegisteredApps.isAppRegEn(AppType.ENHANCEDCHAT) && CoreApp.drawChatOnHud.get().equals("Full")) {
				EMCAppCalloutEvent callout = new EMCAppCalloutEvent(objIn, "EnhancedChat: has chat window"); // I AM NOT SURE IF THIS ACTUALLY ACCOMPLISHED ANYTHING!
				if (!MinecraftForge.EVENT_BUS.post(callout)) {
					if (!isShiftKeyDown()) { change *= 7; }
					mc.ingameGUI.getChatGUI().scroll(change);
				}
			}
		}
	}
	/** Notify the focused object that the keyboard just had a key pressed. */
	public static void keyPressed(IEnhancedTopParent objIn, char typedChar, int keyCode) {
		objIn.postEvent(new EventKeyboard(objIn, typedChar, keyCode, KeyboardType.Pressed)); //post a new event
		if (keyCode == 1 && objIn.getEscapeStopper() == null) { //check if the pressed key was escape in which case all unpinned objects will be removed and the proxy gui is closed
			objIn.removeUnpinnedObjects();
			mc.displayGuiScreen(null);
			mc.setIngameFocus();
		}
		if (keyCode == 61) { //f3 key for debug info
			Minecraft.getMinecraft().gameSettings.showDebugInfo = !Minecraft.getMinecraft().gameSettings.showDebugInfo;
		}
		else {
			IEnhancedGuiObject fo = objIn.getFocusedObject();
			if (fo != null && fo != objIn) { fo.keyPressed(Keyboard.getEventCharacter(), Keyboard.getEventKey()); }
			if (fo == null || fo == objIn) { if (RegisteredApps.isAppRegEn(AppType.ENHANCEDCHAT)) { RegisteredApps.getApp(AppType.ENHANCEDCHAT).keyEvent(new KeyInputEvent()); } }
		}
	}
	/** Notify the focused object that the keyboard just had a key released. */
	public static void keyReleased(IEnhancedTopParent objIn, char typedChar, int keyCode) {
		IEnhancedGuiObject fo = objIn.getFocusedObject();
		objIn.postEvent(new EventKeyboard(objIn, typedChar, keyCode, KeyboardType.Released)); //post a new event too
		if (fo != null && fo != objIn) { fo.keyReleased(Keyboard.getEventCharacter(), Keyboard.getEventKey()); }
	}
	
	//drawing
	/** Debug method used to display topParent information in the top left corner of the screen. */
	public static void drawDebugInfo(IEnhancedTopParent objIn) {
		
		int yPos = objIn.getObjects().containsInstanceOf(TaskBar.class) ? 27 : 2;
		IEnhancedGuiObject ho = objIn.getHighestZObjectUnderMouse();
		String out = "null";
		
		String topParent = "TopParent: " + objIn.getTopParent();
		String focusedObject = "";
		String focusLockObject = "";
		//String objects = "objs: " + objIn.getObjects();
		String modifyType = "ModifyingObject & type: (" + objIn.getModifyingObject() + " : " + objIn.getModifyType() + ")";
		String underMouse = "Object under mouse: " + out + " " + (ho != null ? ho.getZLevel() : -1);
		String mousePos = "(" + EMouseHelper.mX + ", " + EMouseHelper.mY + ")";
		
		if (objIn.getFocusedObject() instanceof EGuiButton) {
			focusedObject = "FocuedObject: " + (((EGuiButton) objIn.getFocusedObject()).getDisplayString().isEmpty() ? objIn.getFocusedObject() : "EGuiButton: " +
					   		((EGuiButton) objIn.getFocusedObject()).getDisplayString());
		}
		else { focusedObject = "FocuedObject: " + objIn.getFocusedObject(); }
		
		if (objIn.getFocusLockObject() instanceof EGuiButton) {
			focusLockObject = "FocusLockObject: " + (((EGuiButton) objIn.getFocusLockObject()).getDisplayString().isEmpty() ? objIn.getFocusedObject() : "EGuiButton: " +
							  ((EGuiButton) objIn.getFocusLockObject()).getDisplayString());
		}
		else { focusLockObject = "FocusLockObject: " + objIn.getFocusLockObject(); }
		
		if (ho != null) { out = ho.getClass().getName(); }
		
		int longestX = mc.fontRendererObj.getStringWidth(topParent);
		FontRenderer fr = mc.fontRendererObj;
		
		if (fr.getStringWidth(focusedObject) > longestX) { longestX = fr.getStringWidth(focusedObject); }
		if (fr.getStringWidth(focusLockObject) > longestX) { longestX = fr.getStringWidth(focusLockObject); }
		//if (fr.getStringWidth(objects) > longestX) { longestX = fr.getStringWidth(objects); }
		if (fr.getStringWidth(modifyType) > longestX) { longestX = fr.getStringWidth(modifyType); }
		if (fr.getStringWidth(underMouse) > longestX) { longestX = fr.getStringWidth(underMouse); }
		if (fr.getStringWidth(mousePos) > longestX) { longestX = fr.getStringWidth(mousePos); }
		
		longestX += 3;
		
		drawRect(0, yPos - 4, longestX + 1, yPos + 61, EColors.black);
		drawRect(1, yPos - 3, longestX, yPos + 60, EColors.dgray);
		
		//draw what the topParent is
		drawStringWithShadow(topParent, 2, yPos, 0x70f3ff);
		//draw the currently focused object - if it's a button, show that too
		drawStringWithShadow(focusedObject, 2, yPos + 10, 0x70f3ff);
		//draw the current focusLockObject - if it's a button, show that too
		drawStringWithShadow(focusLockObject, 2, yPos + 20, 0x70f3ff);
		//draw the topParent's current immediate children
		//drawStringWithShadow(objects, 2, yPos + 30, 0x70f3ff);
		//draw the topParent's current modifying object and type
		drawStringWithShadow(modifyType, 2, yPos + 30, 0x70f3ff);
		//draw the highest object currently under the mouse
		drawStringWithShadow(underMouse, 2, yPos + 40, 0xffbb00);
		//draw the current mouse position
		drawStringWithShadow(mousePos, 2, yPos + 50, 0xffbb00);
		
		//draw escape stopper
		//drawStringWithShadow("EscapeStopper: " + objIn.getEscapeStopper(), 2, 72, 0x70f3ff);
	}
	
	//objects
	/** Returns the object with the highest z level, this could even be the topParent itself. */
	public static IEnhancedGuiObject getHighestZLevelObject(IEnhancedTopParent objIn) {
		EArrayList<IEnhancedGuiObject> objs = objIn.getObjects();
		if (objs.isNotEmpty()) {
			IEnhancedGuiObject highest = objs.get(0);
			if (objIn.getZLevel() > highest.getZLevel()) { highest = objIn; }
			for (IEnhancedGuiObject o : objIn.getObjects()) {
				if (o.getZLevel() > highest.getZLevel()) { highest = o; }
			}
			return highest;
		}
		return objIn;
	}
	/** Removes all windows from the topParent that are not pinned. */
	public static IEnhancedTopParent removeUnpinnedWindows(IEnhancedTopParent objIn) {
		//check in both the current objects and the objects that will be added
		
		EArrayList<IWindowParent> windows = new EArrayList();
		
		for (IEnhancedGuiObject o : EArrayList.combineLists(objIn.getObjects(), objIn.getAddingObjects())) {
			if (o instanceof IWindowParent) { //only windows can be pinned
				if (!((IWindowParent) o).isPinned()) {
					windows.add((IWindowParent) o);
				}
			}
			else {
				EArrayList<IEnhancedGuiObject> childObjects = objIn.getAllChildren();
				for (int i = childObjects.size() - 1; i >= 0; i--) {
					IEnhancedGuiObject u = childObjects.get(i);
					u.close();
				}
			}
		}
		
		for (IWindowParent p : windows) {
			EArrayList<IEnhancedGuiObject> childObjects = p.getAllChildren();
			for (int i = childObjects.size() - 1; i >= 0; i--) {
				childObjects.get(i).close();
			}
			p.close();
		}
		
		return objIn;
	}
	/** Returns true if there are any pinned windows within the specified topParent. */
	public static boolean hasPinnedWindows(IEnhancedTopParent objIn) {
		//check in both the current objects and the objects that will be added
		for (IEnhancedGuiObject o : EArrayList.combineLists(objIn.getObjects(), objIn.getAddingObjects())) {
			if (o instanceof IWindowParent) { //only windows can be pinned
				if (((IWindowParent) o).isPinned()) { return true; }
			}
		}
		return false;
	}
	
	//focus
	/** Removes any object that possesses a focus lock on the specified topParent. */
	public static void clearFocusLockObject(IEnhancedTopParent objIn) {
		if (objIn.getFocusLockObject() != null) { objIn.getFocusLockObject().onFocusLost(new EventFocus(objIn, objIn.getFocusLockObject(), FocusType.Lost)); }
		objIn.setFocusLockObject(null); 
	}
	/** Forces the currently focused object, if there is one, to return focus to either the defaultFocusObject, if there is one, or the topParent. */
	public static void clearFocusedObject(IEnhancedTopParent objIn) {
		IEnhancedGuiObject fo = objIn.getFocusedObject();
		if (fo != null && fo != objIn) {
			fo.onFocusLost(new EventFocus(objIn, fo, FocusType.Lost));
			//transfer focus back to the defaultFocusObject or the topParent
			if (objIn.getDefaultFocusObject() != null) { objIn.setFocusedObject(objIn.getDefaultFocusObject()); }
			else { objIn.setFocusedObject(objIn); }
		}
		else {
			//if there is not a focused object, transfer focus to either the defaultFocusObject or the topParent
			if (objIn.getDefaultFocusObject() != null) { objIn.setFocusedObject(objIn.getDefaultFocusObject()); }
			else { objIn.setFocusedObject(objIn); }
		}
	}
	/** Method used to process focus events that are present in the specified topParent. */
	public static void updateFocus(IEnhancedTopParent objIn, Deque<EventFocus> focusQueue) {
		IEnhancedGuiObject mod = objIn.getModifyingObject();
		ObjectModifyType mType = objIn.getModifyType();
		
		//remove any lingering focused objects if they are no longer within in the parent
		EArrayList<IEnhancedGuiObject> children = objIn.getAllChildren();
		if (objIn.getFocusedObject() != null && !children.contains(objIn.getFocusedObject())) { objIn.clearFocusedObject(); }
		if (objIn.getFocusLockObject() != null && !children.contains(objIn.getFocusLockObject())) { objIn.clearFocusLockObject(); }
		if (objIn.getDefaultFocusObject() != null && !children.contains(objIn.getDefaultFocusObject())) { objIn.setDefaultFocusObject(null); }
		
		//don't process any events if there aren't any to process
		if (!focusQueue.isEmpty()) {
			EventFocus event = focusQueue.pop();
			if (event.getFocusObject() != null) {
				IEnhancedGuiObject obj = event.getFocusObject();
				if (children.contains(obj)) { //only allow object which are a part of the parent to request focus from the parent
					if (objIn.doesFocusLockExist()) { //check for a focus lock and, if it exists, only allow focus to transfer to headers or the focusLockObject
						if (obj.equals(objIn.getFocusLockObject()) || obj.isChild(objIn.getFocusLockObject()) || obj instanceof EGuiHeader) {
							passFocus(objIn, objIn.getFocusedObject(), obj, event);
						}
						else if (objIn.getFocusedObject() != objIn.getFocusLockObject()) {
							passFocus(objIn, objIn.getFocusedObject(), objIn.getFocusLockObject(), new EventFocus(objIn, objIn.getFocusLockObject(), FocusType.DefaultFocusObject));
						}
					} else {
						//if there is already an object in focus, transfer the focus to the requesting object
						if (objIn.getFocusedObject() != null) { passFocus(objIn, objIn.getFocusedObject(), obj, event); }
						else {
							//otherwise, pass focus to a defaultFocusObject, if there is one, else pass it back to the topParent
							if (objIn.getDefaultFocusObject() != null) { passFocus(objIn, objIn.getFocusedObject(), objIn.getDefaultFocusObject(), event); }
							else { passFocus(objIn, objIn.getFocusedObject(), objIn, event); }
						}
					}
				} else { //the object is the topParent itself
					passFocus(objIn, objIn.getFocusedObject(), objIn, event);
				}
			}
		} else if (objIn.getFocusedObject() == null) {
			passFocus(objIn, objIn.getFocusedObject(), objIn, new EventFocus(objIn, objIn, FocusType.DefaultFocusObject));
		}
	}
	/** Internal method used to quickly transfer focus from old object to new object. */
	private static void passFocus(IEnhancedTopParent par, IEnhancedGuiObject from, IEnhancedGuiObject to, EventFocus event) {
		if (from != null) { from.onFocusLost(event); }
		par.setFocusedObject(to);
		to.onFocusGained(event);
	}
	
	//mouse checks
	/** Returns true if the mouse is over any object edge. */
	public static boolean isMouseOnObjEdge(IEnhancedTopParent objIn, int mX, int mY) {
		return getEdgeAreaMouseIsOn(objIn, mX, mY) != ScreenLocation.out;
	}
	/** Returns the ScreenLocation type of any object the mouse is currently over. */
	public static ScreenLocation getEdgeAreaMouseIsOn(IEnhancedTopParent objIn, int mX, int mY) {
		//check in both the objects on screen and the objects being added
		for (IEnhancedGuiObject o : EArrayList.combineLists(objIn.getObjects(), objIn.getAddingObjects())) {
			ScreenLocation loc = o.getEdgeAreaMouseIsOn();
			if (loc != ScreenLocation.out) { return loc; }
		}
		return ScreenLocation.out; //otherwise, return out becuse it wasn't under an object edge
	}
	/** Returns true if the cursor is currently inside an EGuiHeader object. */
	public static boolean isMouseInsideHeader(IEnhancedTopParent objIn, int mX, int mY) {
		for (IEnhancedGuiObject o : objIn.getAllChildrenUnderMouse()) { if (o instanceof EGuiHeader) { return true; } }
		return false;
	}
	public static EGuiHeader getHeaderUnderMouse(IEnhancedTopParent objIn, int mX, int mY) {
		for (IEnhancedGuiObject o : objIn.getAllChildrenUnderMouse()) { if (o instanceof EGuiHeader) { return (EGuiHeader) o; } }
		return null;
	}
	/** Returns the object that has the highest z level under the cursor. */
	public static IEnhancedGuiObject getHighestZObjectUnderMouse(IEnhancedTopParent objIn) {
		try {
			EArrayList<IEnhancedGuiObject> underMouse = objIn.getAllObjectsUnderMouse();
			StorageBoxHolder<IEnhancedGuiObject, EArrayList<IEnhancedGuiObject>> sortedByParent = new StorageBoxHolder();
			
			//first setup the sorted list
			for (int i = objIn.getObjects().size() - 1; i >= 0; i--) {
				sortedByParent.add(objIn.getObjects().get(i), new EArrayList());
			}
			
			//next iterate through each of the objects found under the mouse and add them to the corresponding parents
			for (IEnhancedGuiObject o : underMouse) {
				for (int i = 0; i < sortedByParent.size(); i++) {
					IEnhancedGuiObject parent = sortedByParent.getObject(i);
					if (o.equals(parent) || parent.getAllChildren().contains(o)) { sortedByParent.getValue(i).add(o); }
				}
			}
			
			//next iterate through each of the sorted parent's found objects to see if they are the highest object
			for (StorageBox<IEnhancedGuiObject, EArrayList<IEnhancedGuiObject>> box : sortedByParent) {
				if (box.getValue().isEmpty()) { continue; }
				
				IEnhancedGuiObject highest = null;
				
				//filter for the highest object
				for (IEnhancedGuiObject o : box.getValue()) {
					if (highest == null) { highest = o; }
					else if (o.getZLevel() > highest.getZLevel()) { highest = o; }
				}
				
				return highest;
			}
		} catch (Exception e) { e.printStackTrace(); }
		return null;
	}
	/** Returns a list of all objects that currently under the cursor. */
	public static EArrayList<IEnhancedGuiObject> getAllObjectsUnderMouse(IEnhancedTopParent objIn, int mX, int mY) {
		EArrayList<IEnhancedGuiObject> underMouse = new EArrayList();
		EArrayList<IEnhancedGuiObject> children = objIn.getAllChildren();
		//don't check if an object is being resized
		if (objIn.getModifyType() != ObjectModifyType.Resize) {
			for (IEnhancedGuiObject o : objIn.getAllChildren()) {
				//check if the object can even be selected
				if (o.isVisible() && (o.isClickable() || o.getHoverText() != null)) {
					//then check if the mouse is in or around the object if it's resizeable
					if (o.isMouseInside(mX, mY) || (o.isResizeable() && o.isMouseOnObjEdge(mX, mY))) { underMouse.add(o); }
				}
			}
		}
		return underMouse;
	}
 }
