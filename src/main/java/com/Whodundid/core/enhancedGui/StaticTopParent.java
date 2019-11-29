package com.Whodundid.core.enhancedGui;

import com.Whodundid.core.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiUtil.EGui;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventFocus;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventKeyboard;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventMouse;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.FocusType;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.KeyboardType;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.MouseType;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.ObjectModifyType;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedTopParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.events.emcEvents.ModCalloutEvent;
import com.Whodundid.core.renderer.RendererRCM;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubModType;
import com.Whodundid.core.util.miscUtil.EMouseHelper;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import java.util.Deque;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

public class StaticTopParent extends EGui {

	//basic inputs
	public static void mousePressed(IEnhancedTopParent objIn, int mX, int mY, int button, Deque<EventFocus> focusQueue) {
		objIn.postEvent(new EventMouse(objIn, mX, mY, button, MouseType.Pressed));
		IEnhancedGuiObject underMouse = objIn.getHighestZObjectUnderMouse();
		
		if (objIn.getFocusLockObject() != null) {
			if (underMouse != null) {
				if (underMouse.equals(objIn.getFocusLockObject()) || underMouse.isChildOfObject(objIn.getFocusLockObject()) || underMouse instanceof EGuiHeader) {
					focusQueue.add(new EventFocus(objIn, underMouse, FocusType.MousePress, button, mX, mY));
				} else { objIn.getFocusLockObject().bringToFront(); objIn.getFocusLockObject().drawFocusLockBorder(); }
			} else { objIn.getFocusLockObject().bringToFront(); objIn.getFocusLockObject().drawFocusLockBorder(); }
		}
		else if (underMouse != null) {
			if (underMouse.equals(objIn.getFocusedObject())) { objIn.getFocusedObject().mousePressed(mX, mY, button); }
			else { focusQueue.add(new EventFocus(objIn, underMouse, FocusType.MousePress, button, mX, mY)); }
		}
		else {
			objIn.clearFocusedObject();
			if (button == 1) {
				objIn.addObject(new RendererRCM(objIn, mX, mY));
			}
		}
	}
	public static void mouseReleased(IEnhancedTopParent objIn, int mX, int mY, int button) {
		objIn.postEvent(new EventMouse(objIn, mX, mY, button, MouseType.Released));
		if (objIn.getModifyingObject() != null && objIn.getModifyType() == ObjectModifyType.MoveAlreadyClicked) {
			objIn.setModifyingObject(objIn.getModifyingObject(), ObjectModifyType.None);
		}
		if (objIn.getFocusedObject() != null && objIn.getFocusedObject() != objIn) { objIn.getFocusedObject().mouseReleased(mX, mY, button); }
		if (objIn.isResizing()) { objIn.clearModifyingObject(); }
		if (objIn.getDefaultFocusObject() != null) { objIn.getDefaultFocusObject().requestFocus(); }
	}
	public static void mouseDragged(IEnhancedTopParent objIn, int mX, int mY, int button, long timeSinceLastClick) {
		IEnhancedGuiObject fo = objIn.getFocusedObject();
		if (fo != null && fo != objIn) { fo.mouseDragged(mX, mY, button, timeSinceLastClick); }
	}
	public static void mouseScrolled(IEnhancedTopParent objIn, int mX, int mY, int change) {
		objIn.postEvent(new EventMouse(objIn, mX, mY, -1, MouseType.Scrolled));
		if (objIn.isMouseInsideObject(mX, mY)) {
			for (IEnhancedGuiObject o : objIn.getImmediateChildren()) {
				if (o.isMouseInside(mX, mY) && o.checkDraw()) { o.mouseScrolled(change); }
			}
		} else {
			if (RegisteredSubMods.isModRegistered(SubModType.ENHANCEDCHAT)) {
				ModCalloutEvent callout = new ModCalloutEvent(objIn, "has chat window"); // I AM NOT SURE IF THIS ACTUALLY ACCOMPLISHED ANYTHING!
				if (MinecraftForge.EVENT_BUS.post(callout)) {
					if (!isShiftKeyDown()) { change *= 7; }
					mc.ingameGUI.getChatGUI().scroll(change);
				}
			}
		}
	}
	public static void keyPressed(IEnhancedTopParent objIn, char typedChar, int keyCode) {
		IEnhancedGuiObject fo = objIn.getFocusedObject();
		objIn.postEvent(new EventKeyboard(objIn, typedChar, keyCode, KeyboardType.Pressed));
		if (fo != null && fo != objIn) { fo.keyPressed(Keyboard.getEventCharacter(), Keyboard.getEventKey()); }
		if (keyCode == 1) {
			objIn.removeUnpinnedObjects();
			mc.displayGuiScreen(null);
			mc.setIngameFocus();
		}
	}
	public static void keyReleased(IEnhancedTopParent objIn, char typedChar, int keyCode) {
		IEnhancedGuiObject fo = objIn.getFocusedObject();
		objIn.postEvent(new EventKeyboard(objIn, typedChar, keyCode, KeyboardType.Released));
		if (fo != null && fo != objIn) { fo.keyReleased(Keyboard.getEventCharacter(), Keyboard.getEventKey()); }
	}
	
	//drawing
	public static void drawDebugInfo(IEnhancedTopParent objIn) {
		drawStringWithShadow("TopParent: " + objIn.getTopParent(), 3, 2, 0x70f3ff);
		if (objIn.getFocusedObject() instanceof EGuiButton) {
			drawStringWithShadow("FocuedObject: " + (((EGuiButton) objIn.getFocusedObject()).getDisplayString().isEmpty() ? objIn.getFocusedObject() : "EGuiButton: " +
													((EGuiButton) objIn.getFocusedObject()).getDisplayString()), 3, 12, 0x70f3ff);
		}
		else { drawStringWithShadow("FocuedObject: " + objIn.getFocusedObject(), 3, 12, 0x70f3ff); }
		if (objIn.getFocusLockObject() instanceof EGuiButton) {
			drawStringWithShadow("FocusLockObject: " + (((EGuiButton) objIn.getFocusLockObject()).getDisplayString().isEmpty() ? objIn.getFocusedObject() : "EGuiButton: " +
													((EGuiButton) objIn.getFocusLockObject()).getDisplayString()), 3, 22, 0x70f3ff);
		}
		else { drawStringWithShadow("FocusLockObject: " + objIn.getFocusLockObject(), 3, 22, 0x70f3ff); }
		drawStringWithShadow("objs: " + objIn.getImmediateChildren(), 3, 32, 0x70f3ff);
		drawStringWithShadow("ModifyingObject & type: (" + objIn.getModifyingObject() + " : " + objIn.getModifyType() + ")", 3, 42, 0x70f3ff);
		IEnhancedGuiObject ho = objIn.getHighestZObjectUnderMouse();
		drawStringWithShadow("Object under mouse: " + ho + " " + (ho != null ? ho.getZLevel() : -1), 3, 52, 0xffbb00);
		
		drawStringWithShadow("(" + EMouseHelper.mX + ", " + EMouseHelper.mY + ")", 3, 62, 0xffbb00);
	}
	
	//objects
	public static IEnhancedGuiObject getHighestZLevelObject(IEnhancedTopParent objIn) {
		EArrayList<IEnhancedGuiObject> objs = objIn.getImmediateChildren();
		if (objs.isNotEmpty()) {
			IEnhancedGuiObject highest = objs.get(0);
			for (IEnhancedGuiObject o : objIn.getImmediateChildren()) {
				if (o.getZLevel() > highest.getZLevel()) { highest = o; }
			}
			return highest;
		}
		return null;
	}
	public static IEnhancedTopParent removeUnpinnedObjects(IEnhancedTopParent objIn) {
		for (IEnhancedGuiObject o : EArrayList.combineLists(objIn.getImmediateChildren(), objIn.getObjectsToBeAdded())) {
			if (o instanceof IWindowParent) {
				if (!((IWindowParent) o).isPinned()) { objIn.removeObject(o); }
			}
			else { objIn.removeObject(o); }
		}
		return objIn;
	}
	public static boolean hasPinnedObjects(IEnhancedTopParent objIn) {
		for (IEnhancedGuiObject o : EArrayList.combineLists(objIn.getImmediateChildren(), objIn.getObjectsToBeAdded())) {
			if (o instanceof IWindowParent) {
				if (((IWindowParent) o).isPinned()) { return true; }
			}
		}
		return false;
	}
	
	//focus
	public static void clearFocusLockObject(IEnhancedTopParent objIn) {
		if (objIn.getFocusLockObject() != null) { objIn.getFocusLockObject().onFocusLost(new EventFocus(objIn, objIn.getFocusLockObject(), FocusType.Lost)); }
		objIn.setFocusLockObject(null); 
	}
	public static void clearFocusedObject(IEnhancedTopParent objIn) {
		IEnhancedGuiObject fo = objIn.getFocusedObject();
		if (fo != null && fo != objIn) {
			fo.onFocusLost(new EventFocus(objIn, fo, FocusType.Lost));
			fo.relinquishFocus();
		}
		objIn.setFocusedObject(objIn);
	}
	public static void updateFocus(IEnhancedTopParent objIn, Deque<EventFocus> focusQueue) {
		IEnhancedGuiObject mod = objIn.getModifyingObject();
		ObjectModifyType mType = objIn.getModifyType();
		
		EArrayList<IEnhancedGuiObject> children = objIn.getAllChildren();
		if (objIn.getFocusedObject() != null && !children.contains(objIn.getFocusedObject())) { objIn.clearFocusedObject(); }
		if (objIn.getFocusLockObject() != null && !children.contains(objIn.getFocusLockObject())) { objIn.clearFocusLockObject(); }
		if (objIn.getDefaultFocusObject() != null && !children.contains(objIn.getDefaultFocusObject())) { objIn.setDefaultFocusObject(null); }
		
		if (!focusQueue.isEmpty()) {
			EventFocus event = focusQueue.pop();
			if (event.getFocusObject() != null) {
				IEnhancedGuiObject obj = event.getFocusObject();
				//System.out.println("new focus event: " + obj + " " + event.getFocusType());
				if (children.contains(obj)) { //only allow object which are a part of the parent to request focus from the parent
					if (objIn.doesFocusLockExist()) {
						if (obj.equals(objIn.getFocusLockObject()) || obj.isChildOfObject(objIn.getFocusLockObject()) || obj instanceof EGuiHeader) {
							passFocus(objIn, objIn.getFocusedObject(), obj, event);
						}
						else if (objIn.getFocusedObject() != objIn.getFocusLockObject()) {
							passFocus(objIn, objIn.getFocusedObject(), objIn.getFocusLockObject(), new EventFocus(objIn, objIn.getFocusLockObject(), FocusType.DefaultFocusObject));
						}
					} else {
						if (objIn.getFocusedObject() != null) { passFocus(objIn, objIn.getFocusedObject(), obj, event); }
						else {
							if (objIn.getFocusedObject() != null && obj != objIn) { passFocus(objIn, objIn.getFocusedObject(), obj, event); }
							else {
								if (objIn.getDefaultFocusObject() != null) { passFocus(objIn, objIn.getFocusedObject(), objIn.getDefaultFocusObject(), event); }
								else { passFocus(objIn, objIn.getFocusedObject(), objIn, event); }
							}
						}
					}
				}
			}
		} else if (objIn.getFocusedObject() == null) {
			passFocus(objIn, objIn.getFocusedObject(), objIn, new EventFocus(objIn, objIn, FocusType.DefaultFocusObject));
		}
	}
	private static void passFocus(IEnhancedTopParent par, IEnhancedGuiObject from, IEnhancedGuiObject to, EventFocus event) {
		//System.out.println("FOCUS PASS EVENT: from: " + from + " to: " + to + " event: " + event.getFocusType());
		if (from != null) { from.onFocusLost(event); }
		par.setFocusedObject(to);
		to.onFocusGained(event);
	}
	
	//mouse checks
	public static boolean isMouseOnObjEdge(IEnhancedTopParent objIn, int mX, int mY) {
		return getEdgeAreaMouseIsOn(objIn, mX, mY) != ScreenLocation.out;
	}
	public static ScreenLocation getEdgeAreaMouseIsOn(IEnhancedTopParent objIn, int mX, int mY) {
		for (IEnhancedGuiObject o : objIn.getImmediateChildren()) {
			ScreenLocation loc = o.getEdgeAreaMouseIsOn();
			if (loc != ScreenLocation.out) { return loc; }
		}
		return ScreenLocation.out;
	}
	public static boolean isMouseInsideHeader(IEnhancedTopParent objIn, int mX, int mY) {
		for (IEnhancedGuiObject o : objIn.getAllChildrenUnderMouse()) { if (o instanceof EGuiHeader) { return true; } }
		return false;
	}
	public static IEnhancedGuiObject getHighestZObjectUnderMouse(IEnhancedTopParent objIn) {
		try {
			EArrayList<IEnhancedGuiObject> underMouse = objIn.getAllObjectsUnderMouse();
			StorageBoxHolder<IEnhancedGuiObject, EArrayList<IEnhancedGuiObject>> sortedByParent = new StorageBoxHolder();
			
			//first setup the sorted list
			for (int i = objIn.getImmediateChildren().size() - 1; i >= 0; i--) {
				sortedByParent.add(objIn.getImmediateChildren().get(i), new EArrayList());
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
				
				for (IEnhancedGuiObject o : box.getValue()) {
					if (highest == null) { highest = o; }
					else if (o.getZLevel() > highest.getZLevel()) { highest = o; }
				}
				
				return highest;
			}
		} catch (Exception e) { e.printStackTrace(); }
		return null;
	}
	public static EArrayList<IEnhancedGuiObject> getAllObjectsUnderMouse(IEnhancedTopParent objIn, int mX, int mY) {
		EArrayList<IEnhancedGuiObject> underMouse = new EArrayList();
		for (IEnhancedGuiObject o : objIn.getAllChildren()) {
			if (o.isVisible() && o.isClickable()) {
				if (o.isMouseInside(mX, mY) || (o.isResizeable() && o.isMouseOnObjEdge(mX, mY))) { underMouse.add(o); }
			}
		}
		return underMouse;
	}
 }
