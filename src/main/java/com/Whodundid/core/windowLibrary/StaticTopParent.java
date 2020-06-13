package com.Whodundid.core.windowLibrary;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.coreApp.CoreApp;
import com.Whodundid.core.coreEvents.emcEvents.EMCAppCalloutEvent;
import com.Whodundid.core.debug.DebugFunctions;
import com.Whodundid.core.renderer.renderUtil.RendererRCM;
import com.Whodundid.core.renderer.taskView.TaskBar;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.miscUtil.EMouseHelper;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.header.WindowHeader;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.ITopParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowParent;
import com.Whodundid.core.windowLibrary.windowUtil.EGui;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.FocusType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.KeyboardType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.MouseType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventFocus;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventKeyboard;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventMouse;
import java.util.Deque;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

//Author: Hunter Bragg

public class StaticTopParent extends EGui {

	//-----------------------------------
	//StaticTopParent Basic Input Methods
	//-----------------------------------
	
	/** Notify the focused object that a mouse button was just pressed. */
	public static void mousePressed(ITopParent objIn, int mX, int mY, int button, Deque<EventFocus> focusQueue) {
		objIn.postEvent(new EventMouse(objIn, mX, mY, button, MouseType.Pressed)); //post an event
		IWindowObject underMouse = objIn.getHighestZObjectUnderMouse(); //get the highest object under the mouse
		
		if (objIn.getFocusLockObject() != null) { //first check if there is a focusLock
			if (underMouse != null) { //if there is, then check if there is actually anything under the cursor
				//allow focus to be passed to the object under the cursor if it is the focusLockObject, a child of the focusLockObject or an EGuiHeader
				if (underMouse.equals(objIn.getFocusLockObject()) || underMouse.isChild(objIn.getFocusLockObject()) || underMouse instanceof WindowHeader) {
					focusQueue.add(new EventFocus(objIn, underMouse, FocusType.MousePress, button, mX, mY));
				}
				else { objIn.getFocusLockObject().bringToFront(); objIn.getFocusLockObject().drawFocusLockBorder(); } //otherwise, annoy the user
			}
			else { objIn.getFocusLockObject().bringToFront(); objIn.getFocusLockObject().drawFocusLockBorder(); } //otherwise, annoy the user
		}
		else if (underMouse != null) { //if there is no lock, check if there was actually an object under the cursor
			
			//check if the object is the focused object, if it is, pass the event to it, otherwise, start a focus request
			if (underMouse.equals(objIn.getFocusedObject())) {
				
				IWindowObject focused = objIn.getFocusedObject();
				focused.mousePressed(mX, mY, button);
				
				//check if the object was recently pressed and is elligible for a double click event
				if (button == 0) {
					IWindowObject lastClicked = objIn.getLastClickedObject();
					if (lastClicked == focused) {
						long clickTime = objIn.getLastClickTime();
						
						if (System.currentTimeMillis() - clickTime <= 400) {
							focused.onDoubleClick();
						}
					}
				}
				
				objIn.setLastClickedObject(focused);
				objIn.setLastClickTime(System.currentTimeMillis());
			}
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
	public static void mouseReleased(ITopParent objIn, int mX, int mY, int button) {
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
	public static void mouseDragged(ITopParent objIn, int mX, int mY, int button, long timeSinceLastClick) {
		IWindowObject fo = objIn.getFocusedObject();
		if (fo != null && fo != objIn) { fo.mouseDragged(mX, mY, button, timeSinceLastClick); }
	}
	
	/** Notify any objects under the cursor that the mouse was just scrolled. */
	public static void mouseScrolled(ITopParent objIn, int mX, int mY, int change) {
		objIn.postEvent(new EventMouse(objIn, mX, mY, -1, MouseType.Scrolled));
		
		if (objIn.getHighestZObjectUnderMouse() != null) { //if there are actually any objects under the mouse
			IWindowObject obj = objIn.getHighestZObjectUnderMouse();
			IWindowParent p = obj.getWindowParent();
			
			//only scroll the top most window under the mouse
			if (p != null) { p.mouseScrolled(change); }
			else { obj.mouseScrolled(change); }
		}
		else { //if there were no objects under the mouse, scroll the chat
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
	public static void keyPressed(ITopParent objIn, char typedChar, int keyCode) {
		objIn.postEvent(new EventKeyboard(objIn, typedChar, keyCode, KeyboardType.Pressed)); //post a new event
		//(keyCode == EnhancedMC.openHud.getKeyCode())
		if ((keyCode == 1 && objIn.getEscapeStopper() == null)) { //check if the pressed key was escape in which case all unpinned objects will be removed and the proxy gui is closed
			
			if (CoreApp.hudCloseMethod.get().equals("hide")) { objIn.hideUnpinnedObjects(); }
			else if (CoreApp.hudCloseMethod.get().equals("close")) { objIn.removeUnpinnedObjects(); }
			else { objIn.removeAllObjects(); }
			
			mc.displayGuiScreen(null);
			mc.setIngameFocus();
		}
		
		if (keyCode == 61) { //f3 key for debug info
			Minecraft.getMinecraft().gameSettings.showDebugInfo = !Minecraft.getMinecraft().gameSettings.showDebugInfo;
		}
		else {
			IWindowObject fo = objIn.getFocusedObject();
			if (fo != null && fo != objIn) { fo.keyPressed(Keyboard.getEventCharacter(), Keyboard.getEventKey()); }
			if (fo == null || fo == objIn) {
				if (keyCode == EnhancedMC.openHud.getKeyCode()) {
					
					if (CoreApp.hudCloseMethod.get().equals("hide")) { objIn.hideUnpinnedObjects(); }
					else if (CoreApp.hudCloseMethod.get().equals("close")) { objIn.removeUnpinnedObjects(); }
					else { objIn.removeAllObjects(); }
					
					mc.displayGuiScreen(null);
					mc.setIngameFocus();
				}
			}
		}
	}
	
	/** Notify the focused object that the keyboard just had a key released. */
	public static void keyReleased(ITopParent objIn, char typedChar, int keyCode) {
		IWindowObject fo = objIn.getFocusedObject();
		objIn.postEvent(new EventKeyboard(objIn, typedChar, keyCode, KeyboardType.Released)); //post a new event too
		if (fo != null && fo != objIn) { fo.keyReleased(Keyboard.getEventCharacter(), Keyboard.getEventKey()); }
	}
	
	//-------------------------------
	//StaticTopParent Drawing Methods
	//-------------------------------

	/** Debug method used to display topParent information in the top left corner of the screen. */
	public static void drawDebugInfo(ITopParent objIn) {
		if (DebugFunctions.drawInfo) {
			int yPos = CoreApp.drawHudBorder.get() ? 5 : 3;
			int xPos = CoreApp.drawHudBorder.get() ? 1 : 0;
			
			TaskBar b = EnhancedMC.getRenderer().getTaskBar();
			if (b != null && !b.isHidden() && b.checkDraw()) {
				yPos = b.endY + 4;
			}
			
			IWindowObject ho = objIn.getHighestZObjectUnderMouse();
			String out = "null";
			
			String topParent = "TopParent: " + objIn.getTopParent();
			String focusedObject = "";
			String focusLockObject = "";
			//String objects = "objs: " + objIn.getObjects();
			String modifyType = "ModifyingObject & type: (" + objIn.getModifyingObject() + " : " + objIn.getModifyType() + ")";
			String underMouse = "Object under mouse: " + (ho != null ? ho : out) + " " + (ho != null ? ho.getZLevel() : -1);
			String lastClicked = "Last clicked object: " + objIn.getLastClickedObject() + " : " + objIn.getLastClickTime();
			String mousePos = "Mouse pos: (" + EMouseHelper.mX + ", " + EMouseHelper.mY + ")";
			
			if (objIn.getFocusedObject() instanceof WindowButton) {
				focusedObject = "FocuedObject: " + (((WindowButton) objIn.getFocusedObject()).getString().isEmpty() ? objIn.getFocusedObject() : "EGuiButton: " +
						   		((WindowButton) objIn.getFocusedObject()).getString());
			}
			else { focusedObject = "FocuedObject: " + objIn.getFocusedObject(); }
			
			if (objIn.getFocusLockObject() instanceof WindowButton) {
				focusLockObject = "FocusLockObject: " + (((WindowButton) objIn.getFocusLockObject()).getString().isEmpty() ? objIn.getFocusedObject() : "EGuiButton: " +
								  ((WindowButton) objIn.getFocusLockObject()).getString());
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
			if (fr.getStringWidth(lastClicked) > longestX) { longestX = fr.getStringWidth(lastClicked); }
			if (fr.getStringWidth(mousePos) > longestX) { longestX = fr.getStringWidth(mousePos); }
			
			longestX += 5;
			
			//draw background
			drawRect(xPos, yPos - 3, longestX + 1, yPos + 71, EColors.black);
			drawRect(xPos + 1, yPos - 2, longestX, yPos + 70, EColors.dgray);
			
			//--------------------------------------------------------------------------------
			
			//draw what the topParent is
			drawStringS(topParent, xPos + 3, yPos, EColors.pink);
			
			//draw the currently focused object - if it's a button, show that too
			drawStringS(focusedObject, xPos + 3, yPos + 10, EColors.cyan);
			
			//draw the current focusLockObject - if it's a button, show that too
			drawStringS(focusLockObject, xPos + 3, yPos + 20, EColors.yellow);
			
			//draw the topParent's current immediate children
			//drawStringWithShadow(objects, 2, yPos + 30, 0x70f3ff);
			
			//draw the topParent's current modifying object and type
			drawStringS(modifyType, xPos + 3, yPos + 30, EColors.lime);
			
			//draw the highest object currently under the mouse
			drawStringS(underMouse, xPos + 3, yPos + 40, 0xffbb00);
			
			//draw the last clicked object
			drawStringS(lastClicked, xPos + 3, yPos + 50, EColors.seafoam);
			
			//draw the current mouse position
			drawStringS(mousePos, xPos + 3, yPos + 60, EColors.lgray);
			
			//draw escape stopper
			//drawStringWithShadow("EscapeStopper: " + objIn.getEscapeStopper(), 2, 72, 0x70f3ff);
			
			//--------------------------------------------------------------------------------
			
		}
	}
	
	//------------------------------
	//StaticTopParent Object Methods
	//------------------------------
	
	/** Returns the object with the highest z level, this could even be the topParent itself. */
	public static IWindowObject getHighestZLevelObject(ITopParent objIn) {
		EArrayList<IWindowObject> objs = objIn.getObjects();
		
		if (objs.isNotEmpty()) {
			IWindowObject highest = objs.get(0);
			
			if (objIn.getZLevel() > highest.getZLevel()) { highest = objIn; }
			for (IWindowObject o : objIn.getObjects()) {
				if (o.getZLevel() > highest.getZLevel()) { highest = o; }
			}
			
			return highest;
		}
		return objIn;
	}
	
	/** Hides all objects that are not pinned to the hud. */
	public static ITopParent hideUnpinnedObjects(ITopParent objIn) {
		for (IWindowObject o : EArrayList.combineLists(objIn.getObjects(), objIn.getAddingObjects())) {
			if (o.closesWithHud()) { o.close(); }
			else if (o instanceof IWindowParent) {
				if (!((IWindowParent) o).isPinned()) {
					o.setHidden(true);
				}
			}
			else { o.setHidden(true); }
		}
		return objIn;
	}
	
	/** Hides all objects except for the specified ones. */
	public static ITopParent hideAllExcept(ITopParent topIn, IWindowObject exception, IWindowObject... additional) {
		for (IWindowObject o : EArrayList.combineLists(topIn.getObjects(), topIn.getAddingObjects())) {
			for (IWindowObject e : EUtil.add(exception, additional)) {
				if (o != e || o.isChild(e)) {
					o.setHidden(true);
				}
			}
		}
		return topIn;
	}
	
	/** Makes all objects that were hidden now visible. */
	public static ITopParent revealHiddenObjects(ITopParent objIn) {
		EUtil.filterDo(o -> o.isHidden(), o -> o.setHidden(false), objIn.getAllChildren());
		return objIn;
	}
	
	/** Removes all windows from the topParent that are not pinned. */
	public static ITopParent removeUnpinnedWindows(ITopParent objIn) {
		
		//check in both the current objects and the objects that will be added
		EArrayList<IWindowParent> windows = new EArrayList();
		
		for (IWindowObject o : EArrayList.combineLists(objIn.getObjects(), objIn.getAddingObjects())) {
			if (o instanceof IWindowParent) { //only windows can be pinned
				if (!((IWindowParent) o).isPinned()) {
					windows.add((IWindowParent) o);
				}
			}
			else {
				EArrayList<IWindowObject> childObjects = objIn.getAllChildren();
				for (int i = childObjects.size() - 1; i >= 0; i--) {
					IWindowObject u = childObjects.get(i);
					u.close();
				}
			}
		}
		
		for (IWindowParent p : windows) {
			EArrayList<IWindowObject> childObjects = p.getAllChildren();
			for (int i = childObjects.size() - 1; i >= 0; i--) {
				childObjects.get(i).close();
			}
			p.close();
		}
		
		return objIn;
	}
	
	public static ITopParent removeAllObjects(ITopParent objIn) {
		
		//check in both the current objects and the objects that will be added
		EArrayList<IWindowObject> objects = EArrayList.combineLists(objIn.getObjects(), objIn.getAddingObjects());
		
		for (IWindowObject p : objects) {
			EArrayList<IWindowObject> childObjects = p.getAllChildren();
			for (int i = childObjects.size() - 1; i >= 0; i--) {
				childObjects.get(i).close();
			}
			p.close();
		}
		
		return objIn;
	}
	
	/** Returns true if there are any pinned windows within the specified topParent. */
	public static boolean hasPinnedWindows(ITopParent objIn) {
		//check in both the current objects and the objects that will be added
		for (IWindowObject o : EArrayList.combineLists(objIn.getObjects(), objIn.getAddingObjects())) {
			if (o instanceof IWindowParent) { //only windows can be pinned
				if (((IWindowParent) o).isPinned()) { return true; }
			}
		}
		return false;
	}
	
	//-----------------------------
	//StaticTopParent Focus Methods
	//-----------------------------
	
	/** Removes any object that possesses a focus lock on the specified topParent. */
	public static void clearFocusLockObject(ITopParent objIn) {
		if (objIn.getFocusLockObject() != null) { objIn.getFocusLockObject().onFocusLost(new EventFocus(objIn, objIn.getFocusLockObject(), FocusType.Lost)); }
		objIn.setFocusLockObject(null); 
	}
	
	/** Forces the currently focused object, if there is one, to return focus to either the defaultFocusObject, if there is one, or the topParent. */
	public static void clearFocusedObject(ITopParent objIn) {
		IWindowObject fo = objIn.getFocusedObject();
		
		if (fo != null && fo != objIn) {
			fo.onFocusLost(new EventFocus(objIn, fo, FocusType.Lost));
			//transfer focus back to the defaultFocusObject or the topParent
			if (objIn.getDefaultFocusObject() != null) { objIn.setFocusedObject(objIn.getDefaultFocusObject()); }
			else { objIn.setFocusedObject(objIn); }
		}
		else {
			//if there is not a focused object, transfer focus to either the defaultFocusObject or the topParent
			if (objIn.getDefaultFocusObject() != null) { objIn.setFocusedObject(objIn.getDefaultFocusObject()); }
			else if (fo != objIn) { objIn.setFocusedObject(objIn); }
		}
	}
	
	/** Method used to process focus events that are present in the specified topParent. */
	public static void updateFocus(ITopParent objIn, Deque<EventFocus> focusQueue) {
		IWindowObject mod = objIn.getModifyingObject();
		ObjectModifyType mType = objIn.getModifyType();
		
		//remove any lingering focused objects if they are no longer within in the parent
		EArrayList<IWindowObject> children = objIn.getAllChildren();
		if (objIn.getFocusedObject() != null && !children.contains(objIn.getFocusedObject())) { objIn.clearFocusedObject(); }
		if (objIn.getFocusLockObject() != null && !children.contains(objIn.getFocusLockObject())) { objIn.clearFocusLockObject(); }
		if (objIn.getDefaultFocusObject() != null && !children.contains(objIn.getDefaultFocusObject())) { objIn.setDefaultFocusObject(null); }
		
		//don't process any events if there aren't any to process
		if (!focusQueue.isEmpty()) {
			EventFocus event = focusQueue.pop();
			
			if (event.getFocusObject() != null) {
				IWindowObject obj = event.getFocusObject();
				if (children.contains(obj)) { //only allow object which are a part of the parent to request focus from the parent
					
					if (objIn.doesFocusLockExist()) { //check for a focus lock and, if it exists, only allow focus to transfer to headers or the focusLockObject
						if (obj.equals(objIn.getFocusLockObject()) || obj.isChild(objIn.getFocusLockObject()) || obj instanceof WindowHeader) {
							passFocus(objIn, objIn.getFocusedObject(), obj, event);
						}
						else if (objIn.getFocusedObject() != objIn.getFocusLockObject()) {
							passFocus(objIn, objIn.getFocusedObject(), objIn.getFocusLockObject(), new EventFocus(objIn, objIn.getFocusLockObject(), FocusType.DefaultFocusObject));
						}
					}
					else {
						//if there is already an object in focus, transfer the focus to the requesting object
						if (objIn.getFocusedObject() != null) { passFocus(objIn, objIn.getFocusedObject(), obj, event); }
						else {
							//otherwise, pass focus to a defaultFocusObject, if there is one, else pass it back to the topParent
							if (objIn.getDefaultFocusObject() != null) { passFocus(objIn, objIn.getFocusedObject(), objIn.getDefaultFocusObject(), event); }
							else { passFocus(objIn, objIn.getFocusedObject(), objIn, event); }
						}
					}
				}
				else { //the object is the topParent itself
					passFocus(objIn, objIn.getFocusedObject(), objIn, event);
				}
			}
		}
		else if (objIn.getFocusedObject() == null) {
			passFocus(objIn, objIn.getFocusedObject(), objIn, new EventFocus(objIn, objIn, FocusType.DefaultFocusObject));
		}
	}
	
	/** Internal method used to quickly transfer focus from old object to new object. */
	private static void passFocus(ITopParent par, IWindowObject from, IWindowObject to, EventFocus event) {
		if (from != null) { from.onFocusLost(event); }
		par.setFocusedObject(to);
		to.onFocusGained(event);
	}
	
	//----------------------------
	//StaticTopParent Mouse Checks
	//----------------------------
	
	//mouse checks
	/** Returns true if the mouse is over any object edge. */
	public static boolean isMouseOnObjEdge(ITopParent objIn, int mX, int mY) {
		return getEdgeAreaMouseIsOn(objIn, mX, mY) != ScreenLocation.out;
	}
	
	/** Returns the ScreenLocation type of any object the mouse is currently over. */
	public static ScreenLocation getEdgeAreaMouseIsOn(ITopParent objIn, int mX, int mY) {
		//check in both the objects on screen and the objects being added
		for (IWindowObject o : EArrayList.combineLists(objIn.getObjects(), objIn.getAddingObjects())) {
			ScreenLocation loc = o.getEdgeAreaMouseIsOn();
			if (loc != ScreenLocation.out) { return loc; }
		}
		return ScreenLocation.out; //otherwise, return out becuse it wasn't under an object edge
	}
	
	/** Returns true if the cursor is currently inside an EGuiHeader object. */
	public static boolean isMouseInsideHeader(ITopParent objIn, int mX, int mY) {
		return EUtil.forEachTest(objIn.getAllChildrenUnderMouse(), o -> (o instanceof WindowHeader), true, false);
	}
	
	public static WindowHeader getHeaderUnderMouse(ITopParent objIn, int mX, int mY) {
		return (WindowHeader) EUtil.getFirst(objIn.getAllChildrenUnderMouse(), o -> (o instanceof WindowHeader));
	}
	
	/** Returns the object that has the highest z level under the cursor. */
	public static IWindowObject getHighestZObjectUnderMouse(ITopParent objIn) {
		try {
			EArrayList<IWindowObject> underMouse = objIn.getAllObjectsUnderMouse();
			StorageBoxHolder<IWindowObject, EArrayList<IWindowObject>> sortedByParent = new StorageBoxHolder();
			
			//first setup the sorted list
			for (int i = objIn.getObjects().size() - 1; i >= 0; i--) {
				sortedByParent.add(objIn.getObjects().get(i), new EArrayList());
			}
			
			//next iterate through each of the objects found under the mouse and add them to the corresponding parents
			for (IWindowObject o : underMouse) {
				for (int i = 0; i < sortedByParent.size(); i++) {
					IWindowObject parent = sortedByParent.getObject(i);
					if (o.equals(parent) || parent.getAllChildren().contains(o)) { sortedByParent.getValue(i).add(o); }
				}
			}
			
			//next iterate through each of the sorted parent's found objects to see if they are the highest object
			for (StorageBox<IWindowObject, EArrayList<IWindowObject>> box : sortedByParent) {
				if (box.getValue().isEmpty()) { continue; }
				
				IWindowObject highest = null;
				
				//filter for the highest object
				for (IWindowObject o : box.getValue()) {
					if (highest == null) { highest = o; }
					else if (o.getZLevel() > highest.getZLevel()) { highest = o; }
				}
				
				return highest;
			}
		}
		catch (Exception e) { e.printStackTrace(); }
		return null;
	}
	
	/** Returns a list of all objects that currently under the cursor. */
	public static EArrayList<IWindowObject> getAllObjectsUnderMouse(ITopParent objIn, int mX, int mY) {
		EArrayList<IWindowObject> underMouse = new EArrayList();
		EArrayList<IWindowObject> children = objIn.getAllChildren();
		
		//don't check if an object is being resized
		if (objIn.getModifyType() != ObjectModifyType.Resize) {
			
			for (IWindowObject o : objIn.getAllChildren()) {
				//check if the object can even be selected
				if (o.isVisible() && (o.isClickable() || o.getHoverText() != null)) {
					if (o instanceof IWindowParent) {
						IWindowParent wp = (IWindowParent) o;
						
						//then check if the mouse is in or around the object if it's resizeable
						if (o.isMouseInside(mX, mY) || ((o.isResizeable() && o.isMouseOnObjEdge(mX, mY)) && !(wp.isMinimized() || wp.getMaximizedPosition() == ScreenLocation.center))) {
							underMouse.add(o);
						}
					}
					else {
						//then check if the mouse is in or around the object if it's resizeable
						if (o.isMouseInside(mX, mY) || (o.isResizeable() && o.isMouseOnObjEdge(mX, mY))) {
							IWindowParent wp = o.getWindowParent();
							if (wp != null) {
								if (!wp.isMinimized()) { underMouse.add(o); }
							}
							else { underMouse.add(o); }
						}
					}
				}
			} //for
		}
		
		return underMouse;
	}
	
 }
