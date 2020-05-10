package com.Whodundid.core.enhancedGui.types;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.StaticEGuiObject;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiObjects.utilityObjects.EGuiFocusLockBorder;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiLinkConfirmationDialogueBox;
import com.Whodundid.core.enhancedGui.guiUtil.EGui;
import com.Whodundid.core.enhancedGui.guiUtil.EObjectGroup;
import com.Whodundid.core.enhancedGui.objectEvents.EventAction;
import com.Whodundid.core.enhancedGui.objectEvents.EventFirstDraw;
import com.Whodundid.core.enhancedGui.objectEvents.EventFocus;
import com.Whodundid.core.enhancedGui.objectEvents.EventMouse;
import com.Whodundid.core.enhancedGui.objectEvents.EventObjects;
import com.Whodundid.core.enhancedGui.objectEvents.EventRedraw;
import com.Whodundid.core.enhancedGui.objectEvents.ObjectEvent;
import com.Whodundid.core.enhancedGui.objectEvents.ObjectEventHandler;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.FocusType;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.MouseType;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.ObjectEventType;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.ObjectModifyType;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedTopParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.util.chatUtil.EChatUtil;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.StorageBox;
import java.net.URI;
import java.net.URISyntaxException;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;

//Author: Hunter Bragg

public abstract class EnhancedGuiObject extends EGui implements IEnhancedGuiObject {
	
	public EnhancedGuiObject objectInstance;
	protected IEnhancedGuiObject parent, focusObjectOnClose, defaultFocusObject;
	protected EDimension boundaryDimension;
	protected EArrayList<IEnhancedGuiObject> guiObjects = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> objsToBeRemoved = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> objsToBeAdded = new EArrayList();
	protected ObjectEventHandler eventHandler = new ObjectEventHandler(this);
	protected ScreenLocation oldArea = ScreenLocation.out;
	protected EObjectGroup objectGroup;
	protected String objectName = "noname";
	protected String hoverText = "";
	protected int objZLevel = 0;
	protected int objectId = -1;
	protected boolean enabled = true;
	protected boolean visible = true;
	protected boolean mouseEntered = false;
	protected boolean moveable = true;
	protected boolean hasFocus = false;
	protected boolean focusLock = false;
	protected boolean persistent = false;
	protected boolean alwaysOnTop = false;
	protected boolean resizeable = false;
	protected boolean clickable = true;
	protected boolean firstDraw = false;
	protected boolean closeable = true;
	protected boolean closed = false;
	protected int hoverTextColor = 0xff00d1ff;
	private boolean hasBeenInitialized = false;
	private boolean objectInit = false;
	private boolean beingRemoved = false;
	
	public void init(IEnhancedGuiObject objIn) {
		parent = objIn;
		objectInstance = this;
		res = new ScaledResolution(mc);
	}
	
	public void init(IEnhancedGuiObject objIn, int xIn, int yIn) {
		parent = objIn;
		startX = xIn;
		startY = yIn;
		startXPos = startX;
		startYPos = startY;
		objectInstance = this;
		res = new ScaledResolution(mc);
	}
	
	public void init(IEnhancedGuiObject objIn, int xIn, int yIn, int widthIn, int heightIn) { init(objIn, xIn, yIn, widthIn, heightIn, -1); }
	public void init(IEnhancedGuiObject objIn, int xIn, int yIn, int widthIn, int heightIn, int objectIdIn) {
		parent = objIn;
		objectId = objectIdIn;
		startXPos = xIn;
		startYPos = yIn;
		startWidth = widthIn;
		startHeight = heightIn;
		setDimensions(xIn, yIn, widthIn, heightIn);
		objectInstance = this;
		res = new ScaledResolution(mc);
	}
	
	//----------------
	//Object Overrides
	//----------------
	
	@Override public String toString() { return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()); }
	
	//------------------------------
	//ITabCompleteListener Overrides
	//------------------------------
	
	@Override public void onTabCompletion(String[] result) {}
	@Override public void requestTabComplete(String checkWord, String upToCursor) { EChatUtil.registerTabListener(this, checkWord, upToCursor); }

	//--------------
	//EGui Overrides
	//--------------
	
	@Override
	public void componentURLClick(IChatComponent componentIn, ClickEvent event) {
		if (!mc.gameSettings.chatLinks) { return; }
		try {
			URI uri = new URI(event.getValue());
			String s = uri.getScheme();
			
			if (s == null) { throw new URISyntaxException(event.getValue(), "Missing protocol"); }
			if (!PROTOCOLS.contains(s.toLowerCase())) { throw new URISyntaxException(event.getValue(), "Unsupported protocol: " + s.toLowerCase()); }
			
			if (mc.gameSettings.chatLinksPrompt) { addObject(new EGuiLinkConfirmationDialogueBox(this, event.getValue())); }
			else { openWebLink(event.getValue()); }
		}
		catch (URISyntaxException urisyntaxexception) { EnhancedMC.error("Can\'t open url for " + event, urisyntaxexception); }
	}
	
	//----------------------------
	//IEnhancedGuiObject Overrides
	//----------------------------
	
	//init
	@Override public boolean isInit() { return hasBeenInitialized; }
	@Override public boolean isObjectInit() { return objectInit; }
	@Override public void completeInit() { hasBeenInitialized = true; objectInit = true; }
	@Override public void initObjects() {}
	@Override
	public void reInitObjects() {
		objectInit = false;
		IEnhancedTopParent p = getTopParent();
		EArrayList<IEnhancedGuiObject> children = getAllChildren();
		if (!(p.getModifyType() == ObjectModifyType.Resize)) {
			if (children.contains(p.getFocusedObject())) { p.clearFocusedObject(); }
			if (children.contains(p.getFocusLockObject())) { p.clearFocusLockObject(); }
			if (children.contains(p.getModifyingObject())) { p.clearModifyingObject(); }
		}
		guiObjects.clear();
		objsToBeAdded.clear();
		
		initObjects();
		objectInit = true;
	}
	@Override public void onAdded() {}
	
	//main draw
	@Override
	public void drawObject(int mXIn, int mYIn) {
		updateBeforeNextDraw(mXIn, mYIn);
		try {
			if (checkDraw()) {
				GlStateManager.pushMatrix();
				GlStateManager.enableBlend();
				guiObjects.stream().filter(o -> o.checkDraw()).forEach(o -> {
					if (!o.hasFirstDraw()) { o.onFirstDraw(); o.onFirstDraw(); }
					o.drawObject(mX, mY);
					IEnhancedGuiObject f = getTopParent().getFocusLockObject();
					if (f != null && o instanceof EGuiHeader && (!o.equals(f) && !f.getAllChildren().contains(o))) {
						if (o.isVisible()) {
							EDimension d = o.getDimensions();
							this.drawRect(d.startX, d.startY, d.endX, d.endY, 0x88000000);
						}
					}
				});
				GlStateManager.popMatrix();
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	@Override public void onFirstDraw() { postEvent(new EventFirstDraw(this)); firstDraw = true; }
	@Override public boolean hasFirstDraw() { return firstDraw; }
	@Override public void updateCursorImage() { StaticEGuiObject.updateCursorImage(this); }
	@Override public void onMouseHover(int mX, int mY) { StaticEGuiObject.onMouseHover(this, mX, mY, hoverText, hoverTextColor); }
	@Override public boolean isDrawingHover() { return getTopParent() != null && this.equals(getTopParent().getHoveringObject()); }
	@Override public IEnhancedGuiObject setHoverText(String textIn) { hoverText = textIn; return this; }
	@Override public IEnhancedGuiObject setHoverTextColor(int colorIn) { hoverTextColor = colorIn; return this; }
	
	//obj ids
	@Override public int getObjectID() { return objectId; }
	@Override public IEnhancedGuiObject setObjectID(int idIn) { objectId = idIn; return this; }
	@Override public String getObjectName() { return objectName; }
	@Override public IEnhancedGuiObject setObjectName(String nameIn) { objectName = nameIn; return this; }
	
	//drawing checks
	@Override public boolean checkDraw() { return persistent || visible; }
	@Override public boolean isEnabled() { return enabled; }
	@Override public boolean isVisible() { return visible; }
	@Override public boolean isPersistent() { return persistent; }
	@Override public boolean isBoundaryEnforced() { return boundaryDimension != null; }
	@Override public boolean isResizing() { return getTopParent().getModifyingObject() == this && getTopParent().getModifyType() == ObjectModifyType.Resize; }
	@Override public boolean isMoving() { return getTopParent().getModifyingObject() == this && getTopParent().getModifyType() == ObjectModifyType.Move; }
	@Override public boolean isAlwaysOnTop() { return alwaysOnTop; }
	@Override public IEnhancedGuiObject setEnabled(boolean val) { enabled = val; return this; }
	@Override public IEnhancedGuiObject setVisible(boolean val) { visible = val; return this; }
	@Override public IEnhancedGuiObject setPersistent(boolean val) { persistent = val; return this; }
	@Override public IEnhancedGuiObject setAlwaysOnTop(boolean val) { alwaysOnTop = val; return this; }
	
	//size
	@Override public boolean hasHeader() { return StaticEGuiObject.hasHeader(this); }
	@Override public boolean isResizeable() { return resizeable; }
	@Override public EGuiHeader getHeader() { return StaticEGuiObject.getHeader(this); }
	@Override public int getMinWidth() { return minWidth; }
	@Override public int getMinHeight() { return minHeight; }
	@Override public int getMaxWidth() { return maxWidth; }
	@Override public int getMaxHeight() { return maxHeight; }
	@Override public IEnhancedGuiObject setMinDims(int widthIn, int heightIn) { setMinWidth(widthIn).setMinHeight(heightIn); return this; }
	@Override public IEnhancedGuiObject setMaxDims(int widthIn, int heightIn) { setMaxWidth(widthIn).setMaxHeight(heightIn); return this; }
	@Override public IEnhancedGuiObject setMinWidth(int widthIn) { minWidth = widthIn; return this; }
	@Override public IEnhancedGuiObject setMinHeight(int heightIn) { minHeight = heightIn; return this; }
	@Override public IEnhancedGuiObject setMaxWidth(int widthIn) { maxWidth = widthIn; return this; }
	@Override public IEnhancedGuiObject setMaxHeight(int heightIn) { maxHeight = heightIn; return this; }
	@Override public IEnhancedGuiObject setResizeable(boolean val) { resizeable = val; return this; }
	@Override public IEnhancedGuiObject resize(int xIn, int yIn, ScreenLocation areaIn) { StaticEGuiObject.resize(this, xIn, yIn, areaIn); return this; }
	
	//position
	@Override public void move(int newX, int newY) { StaticEGuiObject.move(this, newX, newY); }
	@Override public boolean isMoveable() { return moveable; }
	@Override
	public IEnhancedGuiObject resetPosition() {
		setDimensions(startXPos, startYPos, startWidth, startHeight);
		guiObjects.forEach(o -> o.resetPosition());
		return this;
	}
	@Override public IEnhancedGuiObject setPosition(int newX, int newY) { StaticEGuiObject.setPosition(this, newX, newY); return this; }
	@Override public IEnhancedGuiObject setMoveable(boolean val) { moveable = val; return this; }
	@Override public IEnhancedGuiObject setDimensions(EDimension dimIn) { return setDimensions(dimIn.startX, dimIn.startY, dimIn.width, dimIn.height); }
	@Override public IEnhancedGuiObject setDimensions(int widthIn, int heightIn) { return setDimensions(startX, startY, widthIn, heightIn); }
	@Override public IEnhancedGuiObject setDimensions(int startXIn, int startYIn, int widthIn, int heightIn) {
		startX = startXIn;
		startY = startYIn;
		width = MathHelper.clamp_int(widthIn, minWidth, maxWidth);
		height = MathHelper.clamp_int(heightIn, minHeight, maxHeight);
		endX = startX + width;
		endY = startY + height;
		midX = startX + width / 2;
		midY = startY + height / 2;
		return this;
	}
	@Override public IEnhancedGuiObject setInitialPosition(int startXIn, int startYIn) { startXPos = startXIn; startYPos = startYIn; return this; }
	@Override public StorageBox<Integer, Integer> getInitialPosition() { return new StorageBox<Integer, Integer>(startXPos, startYPos); }
	@Override public IEnhancedGuiObject centerObjectWithSize(int widthIn, int heightIn) { StaticEGuiObject.centerObjectWithSize(this, widthIn, heightIn); return this; }
	@Override public EDimension getDimensions() { return new EDimension(startX, startY, endX, endY); }
	
	//objects
	@Override public boolean isChild(IEnhancedGuiObject objIn) { return StaticEGuiObject.isChildOfObject(this, objIn); }
	@Override public IEnhancedGuiObject addObject(IEnhancedGuiObject... objsIn) { StaticEGuiObject.addObject(this, objsIn); return this; }
	@Override public IEnhancedGuiObject removeObject(IEnhancedGuiObject... objsIn) { StaticEGuiObject.removeObject(this, objsIn); return this; }
	@Override public EObjectGroup getObjectGroup() { return objectGroup; }
	@Override public IEnhancedGuiObject setObjectGroup(EObjectGroup groupIn) { objectGroup = groupIn; return this; }
	@Override public void onGroupNotification(ObjectEvent e) {}
	@Override public EArrayList<IEnhancedGuiObject> getObjects() { return guiObjects; }
	@Override public EArrayList<IEnhancedGuiObject> getAddingObjects() { return objsToBeAdded; }
	@Override public EArrayList<IEnhancedGuiObject> getRemovingObjects() { return objsToBeRemoved; }
	@Override public EArrayList<IEnhancedGuiObject> getAllChildren() { return StaticEGuiObject.getAllChildren(this); }
	@Override public EArrayList<IEnhancedGuiObject> getAllChildrenUnderMouse() { return StaticEGuiObject.getAllChildrenUnderMouse(this, mX, mY); }
	@Override public boolean containsObject(IEnhancedGuiObject object) { return getCombinedChildren().contains(object); }
	@Override public <T> boolean containsObject(Class<T> objIn) { return objIn != null ? getAllChildren().stream().anyMatch(o -> objIn.isInstance(o)) : false; }
	@Override public EArrayList<IEnhancedGuiObject> getCombinedChildren() { return EArrayList.combineLists(guiObjects, objsToBeAdded); }
	
	//parents
	@Override public IEnhancedGuiObject getParent() { return parent; }
	@Override public IEnhancedGuiObject setParent(IEnhancedGuiObject parentIn) { parent = parentIn; return this; }
	@Override public IEnhancedTopParent getTopParent() { return StaticEGuiObject.getTopParent(this); }
	@Override public IWindowParent getWindowParent() { return StaticEGuiObject.getWindowParent(this); }
	
	//zLevel
	@Override public int getZLevel() { return StaticEGuiObject.getZLevel(this, objZLevel); }
	@Override public IEnhancedGuiObject setZLevel(int zLevelIn) { objZLevel = zLevelIn; return this; }
	@Override public IEnhancedGuiObject bringToFront() { getTopParent().bringObjectToFront(this); return this; }
	@Override public IEnhancedGuiObject sendToBack() { getTopParent().sendObjectToBack(this); return this; }
	
	//focus
	@Override public boolean hasFocus() { if (getTopParent().getFocusedObject() != null) { return getTopParent().getFocusedObject().equals(this); } return false; }
	@Override
	public boolean relinquishFocus() {
		if (getTopParent().doesFocusLockExist()) {
			if (getTopParent().getFocusLockObject().equals(this)) {
				getTopParent().setObjectRequestingFocus(getTopParent());
				return true;
			}
			return false;
		}
		getTopParent().setObjectRequestingFocus(getTopParent());
		return true;
	}
	@Override
	public void onFocusGained(EventFocus eventIn) {
		postEvent(new EventFocus(this, this, FocusType.Gained));
		if (eventIn.getFocusType().equals(FocusType.MousePress)) { mousePressed(eventIn.getMX(), eventIn.getMY(), eventIn.getActionCode()); }
		if (defaultFocusObject != null) { defaultFocusObject.requestFocus(); }
	}
	@Override public void onFocusLost(EventFocus eventIn) { postEvent(new EventFocus(this, this, FocusType.Lost)); }
	@Override
	public void transferFocus(IEnhancedGuiObject objIn) {
		if (getTopParent().doesFocusLockExist() && getTopParent().getFocusLockObject().equals(this)) {
			if (objIn != null) {
				getTopParent().clearFocusLockObject();
				getTopParent().setObjectRequestingFocus(objIn);
			}
		} else if (objIn != null) { getTopParent().setObjectRequestingFocus(objIn); }
	}
	@Override
	public void drawFocusLockBorder() {
		if (checkDraw() && guiObjects.containsNoInstanceOf(EGuiFocusLockBorder.class)) {
			if (hasHeader() && getHeader().isEnabled()) {
				addObject(new EGuiFocusLockBorder(this, getHeader().startX, getHeader().startY, width, height + getHeader().height));
			} else { addObject(new EGuiFocusLockBorder(this)); }
		}
	}
	@Override public IEnhancedGuiObject requestFocus() {
		//System.out.println(this + " is requesting focus");
		getTopParent().setObjectRequestingFocus(this);
		return this;
	}
	@Override public IEnhancedGuiObject getDefaultFocusObject() { return defaultFocusObject; }
	@Override public IEnhancedGuiObject setDefaultFocusObject(IEnhancedGuiObject objIn) { defaultFocusObject = objIn; return this; }
	
	//mouse checks
	@Override public boolean isMouseOnObjEdge(int mX, int mY) { return checkDraw() && getEdgeAreaMouseIsOn() != ScreenLocation.out; }
	@Override public ScreenLocation getEdgeAreaMouseIsOn() { return StaticEGuiObject.getEdgeAreaMouseIsOn(this, mX, mY); }
	@Override public void mouseEntered(int mX, int mY) { postEvent(new EventMouse(this, mX, mY, -1, MouseType.Entered)); }
	@Override public void mouseExited(int mX, int mY) { postEvent(new EventMouse(this, mX, mY, -1, MouseType.Exited)); }
	@Override public boolean isMouseInside(int mX, int mY) { return StaticEGuiObject.isMouseInside(this, mX, mY); }
	@Override public boolean isMouseOver(int mX, int mY) { return isMouseInside(mX, mY) && this.equals(getTopParent().getHighestZObjectUnderMouse()); }
	@Override public IEnhancedGuiObject setBoundaryEnforcer(EDimension dimIn) { boundaryDimension = new EDimension(dimIn); return this; }
	@Override public EDimension getBoundaryEnforcer() { return boundaryDimension; }
	@Override public boolean isClickable() { return clickable; }
	@Override public IEnhancedGuiObject setClickable(boolean valIn) { clickable = valIn; return this; }
	
	//basic inputs
	@Override public void parseMousePosition(int mX, int mY) { StaticEGuiObject.parseMousePosition(this, mX, mY); }
	@Override public void mousePressed(int mX, int mY, int button) { StaticEGuiObject.mousePressed(this, mX, mY, button); }
	@Override public void mouseReleased(int mX, int mY, int button) { StaticEGuiObject.mouseReleased(this, mX, mY, button); }
	@Override public void mouseDragged(int mX, int mY, int button, long timeSinceLastClick) {}
	@Override public void mouseScrolled(int change) { StaticEGuiObject.mouseScolled(this, mX, mY, change); }
	@Override public void keyPressed(char typedChar, int keyCode) { StaticEGuiObject.keyPressed(this, typedChar, keyCode); }
	@Override public void keyReleased(char typedChar, int keyCode) { StaticEGuiObject.keyReleased(this, typedChar, keyCode); }
	
	//events
	@Override public void sendArgs(Object... args) {}
	@Override public ObjectEventHandler getEventHandler() { return eventHandler; }
	@Override public IEnhancedGuiObject registerListener(IEnhancedGuiObject objIn) { if (eventHandler != null) { eventHandler.registerObject(objIn); } return this; }
	@Override public IEnhancedGuiObject unregisterListener(IEnhancedGuiObject objIn) { if (eventHandler != null) { eventHandler.unregisterObject(objIn); } return this; }
	@Override public IEnhancedGuiObject postEvent(ObjectEvent e) { if (eventHandler != null) { eventHandler.processEvent(e); } return this; }
	@Override public void onEvent(ObjectEvent e) {}
	
	//action object
	@Override public void actionPerformed(IEnhancedActionObject object, Object... args) { postEvent(new EventAction(this, object, args)); }
	
	//close object
	@Override public void close() {
		if (closeable) {
			postEvent(new EventObjects(this, this, ObjectEventType.Close));
			if (getTopParent().doesFocusLockExist() && getTopParent().getFocusLockObject().equals(this)) { getTopParent().clearFocusLockObject(); }
			if (getTopParent().getFocusedObject().equals(this)) { relinquishFocus(); }
			if (focusObjectOnClose != null) { focusObjectOnClose.requestFocus(); }
			parent.removeObject(this);
			closed = true;
		}
	}
	@Override public boolean isCloseable() { return closeable; }
	@Override public boolean isClosed() { return closed; }
	@Override public IEnhancedGuiObject setCloseable(boolean val) { closeable = val; return this; }
	@Override public void onClosed() {}
	@Override public IEnhancedGuiObject setFocusedObjectOnClose(IEnhancedGuiObject objIn) { focusObjectOnClose = objIn; return this; }
	@Override public void setBeingRemoved() { beingRemoved = true; }
	@Override public boolean isBeingRemoved() { return beingRemoved; }
	
	//-------------------------
	//EnhancedGuiObject methods
	//-------------------------
	
	protected void updateBeforeNextDraw(int mXIn, int mYIn) {
		postEvent(new EventRedraw(this));
		res = new ScaledResolution(mc);
		mX = mXIn; mY = mYIn;
		if (!mouseEntered && isMouseOver(mX, mY)) { mouseEntered = true; mouseEntered(mX, mY); }
		if (mouseEntered && !isMouseOver(mX, mY)) { mouseEntered = false; mouseExited(mX, mY); }
		if (!objsToBeRemoved.isEmpty()) { StaticEGuiObject.removeObjects(this, objsToBeRemoved); }
		if (!objsToBeAdded.isEmpty()) { StaticEGuiObject.addObjects(this, objsToBeAdded); }
	}
}
