package com.Whodundid.core.renderer;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.EnhancedGui;
import com.Whodundid.core.enhancedGui.InnerEnhancedGui;
import com.Whodundid.core.enhancedGui.guiObjectUtil.EObjectGroup;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiUtil.EGui;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventAction;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventFocus;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventKeyboard;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventModify;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventMouse;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventObjects;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventRedraw;
import com.Whodundid.core.enhancedGui.guiUtil.events.ObjectEvent;
import com.Whodundid.core.enhancedGui.guiUtil.events.ObjectEventHandler;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.FocusType;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.KeyboardType;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.MouseType;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.ObjectEventType;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.ObjectModifyType;
import com.Whodundid.core.enhancedGui.guiUtil.exceptions.HeaderAlreadyExistsException;
import com.Whodundid.core.enhancedGui.guiUtil.exceptions.ObjectInitException;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedTopParent;
import com.Whodundid.core.events.emcEvents.ModCalloutEvent;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubModType;
import com.Whodundid.core.util.miscUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.StorageBox;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.input.Keyboard;

//Last edited: Apr 10, 2019
//First Added: Apr 10, 2019
//Author: Hunter Bragg

public class EnhancedMCRenderer extends EGui implements IEnhancedTopParent {
	
	protected static Minecraft mc = Minecraft.getMinecraft();
	public EnhancedMCRenderer instance;
	protected ScaledResolution res;
	protected IEnhancedGuiObject modifyingObject;
	protected IEnhancedGuiObject objectRequestingFocus, focusedObject, defaultFocusObject, focusLockObject;
	protected IEnhancedGuiObject toFront, toBack;
	protected IEnhancedGuiObject hoveringTextObject;
	public boolean drawHoverText = false;
	public String hoverText = "";
	public long mouseHoverTime = 0l;
	public long hoverRefTime = 0l;
	public StorageBox<Integer, Integer> oldMousePos = new StorageBox();
	protected EArrayList<IEnhancedGuiObject> guiObjects = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> objsToBeAdded = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> objsToBeRemoved = new EArrayList();
	protected ObjectEventHandler eventHandler = new ObjectEventHandler(this);
	protected StorageBox<Integer, Integer> mousePos = new StorageBox(0, 0);
	protected Deque<EventFocus> focusQueue = new ArrayDeque();
	protected ObjectModifyType modifyType = ObjectModifyType.None;
	protected ScreenLocation resizingDir = ScreenLocation.out;
	protected EObjectGroup objectGroup;
	protected boolean enabled = true;
	protected boolean visible = true;
	public int mX = 0, mY = 0;
	protected boolean hasProxy = false;
	protected IRendererProxy proxy;
	
	public EnhancedMCRenderer() {
		res = new ScaledResolution(mc);
		initObjects();
	}
	
	private void updateMouse() {
		//mX = (Mouse.getEventX() * res.getScaledWidth() / mc.displayWidth);
        //mY = (res.getScaledHeight() - Mouse.getEventY() * res.getScaledHeight() / mc.displayHeight - 1);
	}
	
	public void onRenderTick(RenderGameOverlayEvent e) {
		drawObject(0, 0, e.partialTicks);
	}
	
	//----------------
	//Object Overrides
	//----------------
	
	@Override public String toString() { return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()); }
	
	//----------------------------
	//IEnhancedGuiObject Overrides
	//----------------------------
	
	//init
	@Override public boolean hasBeenInitialized() { return true; }
	/** Effectively does nothing in the renderer. */
	@Override public EnhancedMCRenderer completeInitialization() { return this; }
	@Override
	public void initObjects() {
		//KeyOverlay keyOverlay = new KeyOverlay(this, 50, 50);
		//addObject(keyOverlay);
	}
	@Override
	public void reInitObjects() {
		guiObjects.clear();
		initObjects();
	}
	@Override public void onObjectAddedToParent() {}
	
	//main draw
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		checkForProxy();
		updateBeforeNextDraw(mXIn, mYIn);
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		synchronized (guiObjects) {
			for (IEnhancedGuiObject o : guiObjects) {
				if (o.checkDraw()) {
    				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    	        	o.drawObject(mX, mY, ticks);
    			}
			}
		}
		if (EnhancedMC.isDebugMode() && !mc.gameSettings.showDebugInfo) {
			drawStringWithShadow("TopParent: " + getTopParent(), 3, 2, 0x70f3ff);
			if (focusedObject instanceof EGuiButton) {
				drawStringWithShadow("FocuedObject: " + (((EGuiButton) focusedObject).getDisplayString().isEmpty() ? focusedObject : "EGuiButton: " +
														((EGuiButton) focusedObject).getDisplayString()), 3, 12, 0x70f3ff);
			}
			else { drawStringWithShadow("FocuedObject: " + focusedObject, 3, 12, 0x70f3ff); }
			drawStringWithShadow("objs: " + guiObjects, 3, 22, 0x70f3ff);
			drawStringWithShadow("ModifyingObject & type: (" + modifyingObject + " : " + modifyType + ")", 3, 32, 0x70f3ff);
		}
		GlStateManager.popMatrix();
	}
	@Override public void updateCursorImage() {}
	@Override public EnhancedMCRenderer setDrawHoverText(boolean val) { return this; }
	@Override public boolean drawsHoverText() { return false; }
	@Override public EnhancedMCRenderer setHoverText(String textIn) { return this; }
	@Override public String getHoverText() { return ""; }
	
	//obj ids;
	@Override public int getObjectID() { return 0; }
	@Override public EnhancedMCRenderer setObjectID(int idIn) { return this; }
	
	//drawing checks
	@Override public boolean checkDraw() { return visible; }
	@Override public boolean isEnabled() { return enabled; }
	@Override public boolean isVisible() { return visible; }
	@Override public boolean isPersistent() { return true; }
	@Override public boolean isBoundaryEnforced() { return false; }
	@Override public EnhancedMCRenderer setEnabled(boolean val) { enabled = val; return this; }
	@Override public EnhancedMCRenderer setVisible(boolean val) { visible = val; return this; }
	@Override public EnhancedMCRenderer setPersistent(boolean val) { return this; }
	@Override public EnhancedMCRenderer setBoundaryEnforcer(EDimension dimIn) { return this; }
	
	//size
	@Override public boolean hasHeader() { return false; }
	@Override public boolean isResizeable() { return false; }
	@Override public EGuiHeader getHeader() { return null; }
	@Override public int getMinimumWidth() { return res.getScaledWidth(); }
	@Override public int getMinimumHeight() { return res.getScaledHeight(); }
	@Override public int getMaximumWidth() { return res.getScaledWidth(); }
	@Override public int getMaximumHeight() { return res.getScaledHeight(); }
	@Override public EnhancedMCRenderer setMinimumWidth(int widthIn) { return this; }
	@Override public EnhancedMCRenderer setMinimumHeight(int heightIn) { return this; }
	@Override public EnhancedMCRenderer setMaximumWidth(int widthIn) { return this; }
	@Override public EnhancedMCRenderer setMaximumHeight(int heightIn) { return this; }
	@Override public EnhancedMCRenderer setResizeable(boolean val) { return this; }
	@Override
	public EnhancedMCRenderer resize(int xIn, int yIn, ScreenLocation areaIn) {
		if (eventHandler != null) { eventHandler.processEvent(new EventModify(this, this, ObjectModifyType.Resize)); }
		return this;
	}
	
	//position
	@Override
	public void move(int newX, int newY) {
		if (eventHandler != null) { eventHandler.processEvent(new EventModify(this, this, ObjectModifyType.Move)); }
	}
	@Override public boolean isPositionLocked() { return true; }
	@Override public EnhancedMCRenderer resetPosition() { return this; }
	@Override public EnhancedMCRenderer setPosition(int xIn, int yIn) { return this; }
	@Override public EnhancedMCRenderer setPositionLocked(boolean val) { return this; }
	@Override public EnhancedMCRenderer setDimensions(EDimension dimIn) { return this; }
	@Override public EnhancedMCRenderer setDimensions(int startXIn, int startYIn, int widthIn, int heightIn) { return this; }
	@Override public StorageBox<Integer, Integer> getInitialPosition() { return new StorageBox<Integer, Integer>(0, 0); }
	@Override public EnhancedMCRenderer setInitialPosition(int startXIn, int startYIn) { return this; }
	@Override public EDimension getDimensions() { return new EDimension(0, 0, res.getScaledWidth(), res.getScaledWidth()); }
	
	//objects
	@Override public boolean isChildOfObject(IEnhancedGuiObject objIn) { return false; }
	@Override
	public EnhancedMCRenderer addObject(IEnhancedGuiObject... objsIn) {
		EArrayList<IEnhancedGuiObject> addingObjects = new EArrayList();
		for (IEnhancedGuiObject o : objsIn) {
			if (o != null) {
				if (o != this && guiObjects.notContains(o) && objsToBeAdded.notContains(o)) {
					if (o instanceof EGuiHeader && hasHeader()) { 
						try { throw new HeaderAlreadyExistsException(getHeader()); } catch (HeaderAlreadyExistsException e) { e.printStackTrace(); }
					}
					if (o instanceof EnhancedGui) {
						ScaledResolution scaledresolution = new ScaledResolution(mc);
			            int i = scaledresolution.getScaledWidth();
			            int j = scaledresolution.getScaledHeight();
			            ((EnhancedGui) o).setWorldAndResolution(mc, i, i);
					}
					try {
						o.setParent(this).initObjects();
						o.setZLevel(getZLevel() + o.getZLevel() + 1);
						if (o instanceof InnerEnhancedGui) { ((InnerEnhancedGui) o).initGui(); }
						o.completeInitialization();
					} catch (ObjectInitException e) { e.printStackTrace(); }
					addingObjects.add(o);
				}
			}
		}
		objsToBeAdded.addAll(addingObjects);
		return this;
	}
	@Override
	public EnhancedMCRenderer removeObject(IEnhancedGuiObject... objsIn) {
		//prevent object from being added
		for (IEnhancedGuiObject o : objsIn) {
			if (objsToBeAdded.contains(o)) { objsToBeAdded.remove(o); }
		}
		objsToBeRemoved.addAll(objsIn);
		return this;
	}
	@Override public EObjectGroup getObjectGroup() { return objectGroup; }
	@Override public EnhancedMCRenderer setObjectGroup(EObjectGroup groupIn) { objectGroup = groupIn; return this; }
	@Override public void onGroupNotification(ObjectEvent e) {}
	@Override public EArrayList<IEnhancedGuiObject> getImmediateChildren() { return guiObjects; }
	@Override
	public EArrayList<IEnhancedGuiObject> getAllChildren() {
		EArrayList<IEnhancedGuiObject> foundObjs = new EArrayList();
		EArrayList<IEnhancedGuiObject> objsWithChildren = new EArrayList();
		EArrayList<IEnhancedGuiObject> workList = new EArrayList();
		
		getImmediateChildren().forEach((o) -> { foundObjs.add(o); if (!o.getImmediateChildren().isEmpty()) { objsWithChildren.add(o); } });
		objsWithChildren.forEach((c) -> { c.getImmediateChildren().forEach((q) -> { workList.add(q); }); });
		
		while (true) {
			if (!workList.isEmpty()) {
				foundObjs.addAll(workList);
				objsWithChildren.clear();
				workList.forEach((o) -> { if (!o.getImmediateChildren().isEmpty()) { objsWithChildren.add(o); } });
				workList.clear();
				objsWithChildren.forEach((c) -> { c.getImmediateChildren().forEach((q) -> { workList.add(q); }); });
			} else { break; }
		}
		
		return foundObjs;
	}
	@Override
	public EArrayList<IEnhancedGuiObject> getAllChildrenUnderMouse() {
		EArrayList<IEnhancedGuiObject> underMouse = new EArrayList();
		for (IEnhancedGuiObject o : getAllChildren()) { if (o.checkDraw() && o.isMouseInside(mX, mY)) { underMouse.add(o); } }
		return underMouse;
	}
	
	//parents
	@Override public IEnhancedGuiObject getParent() { return this; }
	@Override public EnhancedMCRenderer setParent(IEnhancedGuiObject parentIn) { return this; }
	@Override public IEnhancedTopParent getTopParent() { return this; }
	
	//zLevel
	@Override public int getZLevel() { return 0; }
	@Override public EnhancedMCRenderer setZLevel(int zLevelIn) { return this; }
	@Override public EnhancedMCRenderer bringToFront() { return this; }
	@Override public EnhancedMCRenderer sendToBack() { return this; }
	
	//focus
	@Override public boolean hasFocus() { return getFocusedObject().equals(this); }
	@Override public boolean relinquishFocus() {
		if (doesFocusLockExist() && getFocusLockObject().equals(this)) { clearFocusLockObject(); }
		else if (hasFocus()) {
			if (!equals(this)) { setObjectRequestingFocus(this); return true; }
		}
		return false;
	}
	@Override
	public void onFocusGained(EventFocus eventIn) {
		if (eventHandler != null) { eventHandler.processEvent(new EventFocus(this, this, FocusType.Gained)); }
	}
	@Override
	public void onFocusLost(EventFocus eventIn) {
		if (eventHandler != null) { eventHandler.processEvent(new EventFocus(this, this, FocusType.Lost)); }
	}
	@Override
	public void transferFocus(IEnhancedGuiObject objIn) {
		if (!doesFocusLockExist() && objIn != null) {
			relinquishFocus();
			setObjectRequestingFocus(objIn);
		}
	}
	@Override public void drawFocusLockBorder() {}
	@Override
	public EnhancedMCRenderer requestFocus() {
		if (!hasFocus() && !doesFocusLockExist()) { setObjectRequestingFocus(this); }
		return this;
	}
	
	//mouse checks
	@Override public boolean isMouseOnObjEdge(int mX, int mY) { return false; }
	@Override public ScreenLocation getEdgeAreaMouseIsOn() { return ScreenLocation.out; }
	@Override
	public void mouseEntered(int mX, int mY) {
		if (eventHandler != null) { eventHandler.processEvent(new EventMouse(this, mX, mY, -1, MouseType.Entered)); }
	}
	@Override
	public void mouseExited(int mX, int mY) {
		if (eventHandler != null) { eventHandler.processEvent(new EventMouse(this, mX, mY, -1, MouseType.Exited)); }
	}
	@Override public boolean isMouseInside(int mX, int mY) { return false; }
	
	//basic inputs
	@Override
	public void parseMousePosition(int mXIn, int mYIn) {
		for (IEnhancedGuiObject o : guiObjects) { if (o.isMouseInside(mX, mY)) { o.parseMousePosition(mX, mY); } }
	}
	@Override
	public void mousePressed(int mX, int mY, int button) {
		if (eventHandler != null) { eventHandler.processEvent(new EventMouse(this, mX, mY, button, MouseType.Pressed)); }
		IEnhancedGuiObject underMouse = getHighestZObjectUnderMouse();
		if (focusLockObject != null) {
			if (underMouse != null) {
				if (underMouse.equals(focusLockObject) || underMouse.isChildOfObject(focusLockObject) || underMouse instanceof EGuiHeader) {
					focusQueue.add(new EventFocus(this, underMouse, FocusType.MousePress, button, mX, mY));
				} else {
					focusLockObject.drawFocusLockBorder();
				}
			} else { focusLockObject.drawFocusLockBorder(); }
		}
		else if (underMouse != null) {
			if (underMouse.equals(focusedObject)) {
				focusedObject.mousePressed(mX, mY, button);
			} else {
				focusQueue.add(new EventFocus(this, underMouse, FocusType.MousePress, button, mX, mY));
			}
		}
		else { clearFocusedObject(); }
	}
	@Override
	public void mouseReleased(int mX, int mY, int button) {
		if (eventHandler != null) { eventHandler.processEvent(new EventMouse(this, mX, mY, button, MouseType.Released)); }
		if (modifyingObject != null && modifyType == ObjectModifyType.MoveAlreadyClicked) { modifyType = ObjectModifyType.None; }
		if (focusedObject != null && focusedObject != this) { focusedObject.mouseReleased(mX, mY, button); }
		if (isResizing()) { clearModifyingObject(); }
		if (getDefaultFocusObject() != null) { getDefaultFocusObject().requestFocus(); }
	}
	@Override public void mouseDragged(int mX, int mY, int button, long timeSinceLastClick) {
		if (focusedObject != null && focusedObject != this) { focusedObject.mouseDragged(mX, mY, button, timeSinceLastClick); }
	}
	@Override
	public void mouseScrolled(int change) {
		if (eventHandler != null) { eventHandler.processEvent(new EventMouse(this, mX, mY, -1, MouseType.Scrolled)); }
		if (isMouseInsideObject(mX, mY)) {
			for (IEnhancedGuiObject o : guiObjects) {
				if (o.isMouseInside(mX, mY) && o.checkDraw()) { o.mouseScrolled(change); }
			}
		} else {
			if (RegisteredSubMods.isModRegistered(SubModType.ENHANCEDCHAT)) {
				ModCalloutEvent callout = new ModCalloutEvent(this, "has chat window"); // I AM NOT SURE IF THIS ACTUALLY ACCOMPLISHED ANYTHING!
				if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(callout)) {
					if (!isShiftKeyDown()) { change *= 7; }
					mc.ingameGUI.getChatGUI().scroll(change);
				}
			}
		}
	}
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (eventHandler != null) { eventHandler.processEvent(new EventKeyboard(this, typedChar, keyCode, KeyboardType.Pressed)); }
		if (keyCode == 41) {
			if (focusedObject instanceof EGuiButton) {
				System.out.println("FocuedObject: " + (((EGuiButton) focusedObject).getDisplayString().isEmpty() ? focusedObject : "EGuiButton: " + ((EGuiButton) focusedObject).getDisplayString()));
			}
			else { System.out.println("FocuedObject: " + focusedObject); }
			System.out.println("ModifyingObject & type: " + modifyingObject + " " + modifyType);
		}
		if (focusedObject != null && focusedObject != this) { focusedObject.keyPressed(Keyboard.getEventCharacter(), Keyboard.getEventKey()); }
	}
	@Override public void keyReleased(char typedChar, int keyCode) {
		if (eventHandler != null) { eventHandler.processEvent(new EventKeyboard(this, typedChar, keyCode, KeyboardType.Released)); }
		if (focusedObject != null && focusedObject != this) { focusedObject.keyReleased(Keyboard.getEventCharacter(), Keyboard.getEventKey()); }
	}
	
	//updateScreen
	@Override public void updateScreen() { for (IEnhancedGuiObject o : guiObjects) { o.updateScreen(); } }
	
	//events
	@Override public ObjectEventHandler getEventHandler() { return eventHandler; }
	@Override public EnhancedMCRenderer registerListener(IEnhancedGuiObject objIn) { if (eventHandler != null) { eventHandler.registerObject(objIn); } return this; }
	@Override public EnhancedMCRenderer unregisterListener(IEnhancedGuiObject objIn) { if (eventHandler != null) { eventHandler.unregisterObject(objIn); } return this; }
	@Override public void onListen(ObjectEvent e) {}
	
	//action object
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (eventHandler != null) { eventHandler.processEvent(new EventAction(this, object)); }
	}
	
	//close object
	@Override public void close() {
		if (eventHandler != null) { eventHandler.processEvent(new EventObjects(this, this, ObjectEventType.Close)); }
	}
	@Override public EnhancedMCRenderer setFocusedObjectOnClose(IEnhancedGuiObject objIn) { System.out.println("FOOL! Dagoth Ur cannot be closed, I am a god!"); return this; }
	
	//-------------------------
	//IEnhancedTopGui Overrides
	//-------------------------
	
	//history
	@Override public Stack<EnhancedGui> getGuiHistory() { return null; }
	@Override public EnhancedMCRenderer sendGuiHistory(Stack<EnhancedGui> historyIn) { return this; }
	
	//draw order
	@Override public EnhancedMCRenderer bringObjectToFront(IEnhancedGuiObject objIn) { toFront = objIn; return this; }
	@Override public EnhancedMCRenderer sendObjectToBack(IEnhancedGuiObject objIn) { toBack = objIn; return this; }
	
	//hovering text
	@Override public IEnhancedTopParent setObjectWithHoveringText(IEnhancedGuiObject objIn) { hoveringTextObject = objIn; return this; }
	@Override public IEnhancedGuiObject getObjectWithHoveringText() { return hoveringTextObject; }
	
	//focus
	@Override public IEnhancedGuiObject getDefaultFocusObject() { return defaultFocusObject; }
	@Override public EnhancedMCRenderer setDefaultFocusObject(IEnhancedGuiObject objIn) { defaultFocusObject = objIn; return this; }
	@Override public IEnhancedGuiObject getFocusedObject() { return focusedObject; }
	@Override public EnhancedMCRenderer setObjectRequestingFocus(IEnhancedGuiObject objIn) { focusQueue.add(new EventFocus(this, objIn, FocusType.Transfer)); return this; }
	@Override public IEnhancedGuiObject getFocusLockObject() { return focusLockObject; }
	@Override public EnhancedMCRenderer setFocusLockObject(IEnhancedGuiObject objIn) { focusLockObject = objIn; transferFocus(focusLockObject); return this; }
	@Override public EnhancedMCRenderer clearFocusLockObject() { focusLockObject = null; return this; }
	@Override public boolean doesFocusLockExist() { return focusLockObject != null; }
	@Override
	public void clearFocusedObject() {
		if (focusedObject != null && focusedObject != this) { focusedObject.relinquishFocus(); }
		focusedObject = this;
	}
	@Override
	public void updateFocus() {
		if (modifyingObject != null && !modifyingObject.isResizeable() && modifyType.equals(ObjectModifyType.Resize)) { modifyType = ObjectModifyType.None; }
		EArrayList<IEnhancedGuiObject> children = getAllChildren();
		if (!children.contains(focusedObject)) { clearFocusedObject(); }
		if (!children.contains(focusLockObject)) { clearFocusLockObject(); }
		
		if (!focusQueue.isEmpty()) {
			EventFocus event = focusQueue.pop();
			if (event.getFocusObject() != null) {
				IEnhancedGuiObject obj = event.getFocusObject();
				if (doesFocusLockExist()) {
					if (obj.equals(focusLockObject) || obj.isChildOfObject(focusLockObject) || obj instanceof EGuiHeader) {
						focusedObject.onFocusLost(event);
						focusedObject = obj;
						focusedObject.onFocusGained(event);
					}
					else if (focusedObject != focusLockObject) {
						focusedObject.onFocusLost(new EventFocus(this, focusLockObject, FocusType.DefaultFocusObject));
						focusedObject = focusLockObject;
						focusedObject.onFocusGained(new EventFocus(this, focusLockObject, FocusType.DefaultFocusObject));
					}
				} else {
					if (focusedObject != null) {
						focusedObject.onFocusLost(event);
						focusedObject = obj;
						focusedObject.onFocusGained(event);
					} else {
						if (focusedObject != null && obj != this) {
							focusedObject.onFocusLost(event);
							focusedObject = obj;
							focusedObject.onFocusGained(event);
						} else {
							if (defaultFocusObject != null) {
								focusedObject = defaultFocusObject;
								focusedObject.onFocusGained(event);
							} else {
								focusedObject = this;
								focusedObject.onFocusGained(event);
							}
						}
					}
				}
			}
		} else if (focusedObject == null) {
			focusedObject = this;
			focusedObject.onFocusGained(new EventFocus(this, this, FocusType.DefaultFocusObject));
		}
	}
	
	//object modification
	@Override public boolean isMoving() { return modifyType.equals(ObjectModifyType.Move); }
	@Override public boolean isResizing() { return modifyType.equals(ObjectModifyType.Resize); }
	@Override public ObjectModifyType getModifyType() { return modifyType; }
	@Override public EnhancedMCRenderer setModifyingObject(IEnhancedGuiObject objIn, ObjectModifyType typeIn) { modifyingObject = objIn; modifyType = typeIn; return this; }
	@Override public EnhancedMCRenderer setResizingDir(ScreenLocation areaIn) { resizingDir = areaIn; return this; }
	@Override public EnhancedMCRenderer setModifyMousePos(int mX, int mY) { mousePos.setValues(mX, mY); return this; }
	@Override public IEnhancedGuiObject getModifyingObject() { return modifyingObject; }
	@Override public EnhancedMCRenderer clearModifyingObject() { modifyingObject = null; modifyType = ObjectModifyType.None; return this; }
	
	//mouse checks
	@Override public boolean isMouseInsideObject(int mX, int mY) { return getHighestZObjectUnderMouse() != null; }
	@Override
	public boolean isMouseInsideHeader(int mX, int mY) {
		EArrayList<IEnhancedGuiObject> objects = getAllChildrenUnderMouse();
		boolean under = false;
		for (IEnhancedGuiObject o : objects) {
			if (o instanceof EGuiHeader) { under = true; }
		}
		return under;
	}
	@Override
	public IEnhancedGuiObject getHighestZObjectUnderMouse() {
		EArrayList<IEnhancedGuiObject> foundObjs = getAllChildren();
		EArrayList<IEnhancedGuiObject> mouseIn = new EArrayList();
		//System.out.println(mX + " " + mY);
		if (!foundObjs.isEmpty()) {
			for (IEnhancedGuiObject o : foundObjs) {
				if (o.checkDraw() && o.isMouseInside(mX, mY)) { mouseIn.add(o); }
			}
			if (!mouseIn.isEmpty()) {
				IEnhancedGuiObject highestZObj = null;
				for (IEnhancedGuiObject o : mouseIn) {
					if (highestZObj != null) {
						if (o.getZLevel() > highestZObj.getZLevel()) { highestZObj = o; }
					} else { highestZObj = o; }
				}
				return highestZObj;
			}
			if (checkDraw() && isMouseInside(mX, mY)) { return this; }
		}
		
		return null;
	}
	
	//close
	@Override public void closeGui(boolean fullClose) {}
	@Override public EnhancedMCRenderer setCloseAndRecenter(boolean val) { return this; }
	
	//-------------------------
	//RenderManager methods
	//-------------------------
	
	protected void updateBeforeNextDraw(int mXIn, int mYIn) {
		if (eventHandler != null) { eventHandler.processEvent(new EventRedraw(this)); }
		res = new ScaledResolution(mc);
		if (proxy != null) { mX = proxy.getMX(); mY = proxy.getMY(); }
		else if (mc.currentScreen instanceof EnhancedGui) { 
			EnhancedGui gui = (EnhancedGui) mc.currentScreen;
			mX = gui.mX; mY = gui.mY;
		}
		else { mX = mXIn; mY = mYIn; }
		checkMouseHover();
		oldMousePos.setValues(mXIn, mYIn);
		if (!objsToBeRemoved.isEmpty()) { removeObjects(); }
		if (!objsToBeAdded.isEmpty()) { addObjects(); }
		updateZLayers();
		updateFocus();
		if (modifyingObject != null) {
			switch (modifyType) {
			case Move:
			case MoveAlreadyClicked: modifyingObject.move(mX - mousePos.getObject(), mY - mousePos.getValue()); mousePos.setValues(mX, mY); break;
			case Resize: modifyingObject.resize(mX - mousePos.getObject(), mY - mousePos.getValue(), resizingDir); mousePos.setValues(mX, mY); break;
			default: break;
			}
		}
	}
	
	public void checkMouseHover() {
		if (getTopParent().getHighestZObjectUnderMouse() != null) {
			if (mX == oldMousePos.getObject() && mY == oldMousePos.getValue()) {
				mouseHoverTime = (System.currentTimeMillis() - hoverRefTime);
				if (mouseHoverTime >= 1000) {
					getTopParent().setObjectWithHoveringText(getTopParent().getHighestZObjectUnderMouse());
				}
			}
			else {
				mouseHoverTime = 0l;
				hoverRefTime = System.currentTimeMillis();
			}
		}
		else if (mouseHoverTime > 0l) {
			mouseHoverTime = 0l;
		}
	}
	
	protected void checkForProxy() {
		hasProxy = (mc.currentScreen instanceof IRendererProxy);
		if (hasProxy) { proxy = (IRendererProxy) mc.currentScreen; }
		else { proxy = null; }
	}
	
	protected void updateZLayers() {
		if (toFront != null) {
			if (guiObjects.contains(toFront)) {
				guiObjects.remove(toFront);
				guiObjects.add(toFront);
			}
			toFront = null;
		}
		
		if (toBack != null) {
			if (guiObjects.contains(toBack)) {
				EArrayList<IEnhancedGuiObject> objects = new EArrayList();
				guiObjects.remove(toFront);
				objects.addAll(guiObjects);
				guiObjects.clear();
				guiObjects.add(toFront);
				guiObjects.addAll(objects);
			}
			toBack = null;
		}
	}
	
	protected void removeObjects() {
		synchronized (guiObjects) {
			objsToBeRemoved.forEach(o -> {
				if (o != null) {
					if (o != this) {
						if (guiObjects.contains(o)) {
							if (o instanceof InnerEnhancedGui) { ((InnerEnhancedGui) o).onInnerGuiClose(); }
							guiObjects.remove(o);
							if (eventHandler != null) { eventHandler.processEvent(new EventObjects(this, o, ObjectEventType.ObjectRemoved)); }
						}
					}
				}
			});
			objsToBeRemoved.clear();
		}
	}
	
	protected void addObjects() {
		objsToBeAdded.forEach(o -> {
			if (o != null) {
				if (o != this) {
					guiObjects.add(o);
					o.onObjectAddedToParent();
					if (eventHandler != null) { eventHandler.processEvent(new EventObjects(this, o, ObjectEventType.ObjectAdded)); }
				}
			}
		});
		objsToBeAdded.clear();
	}
	
	public void windowResized(int newWidth, int newHeight) {
		guiObjects.clear();
		objsToBeAdded.clear();
		objsToBeRemoved.clear();
		toFront = null;
		toBack = null;
		clearFocusedObject();
		setObjectRequestingFocus(null);
		reInitObjects();
	}
	
	public EArrayList<IEnhancedGuiObject> getObjectsToBeAdded() { return objsToBeAdded; }
	public EArrayList<IEnhancedGuiObject> getObjectsToBeRemoved() { return objsToBeRemoved; }
	
	public static boolean isCtrlKeyDown() { return Minecraft.isRunningOnMac ? Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220) : Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157); }
    public static boolean isShiftKeyDown() { return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54); }
    public static boolean isAltKeyDown() { return Keyboard.isKeyDown(56) || Keyboard.isKeyDown(184); }
}
