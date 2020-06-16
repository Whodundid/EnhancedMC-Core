package com.Whodundid.core.windowLibrary.windowTypes;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreApp.CoreApp;
import com.Whodundid.core.util.chatUtil.EChatUtil;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.windowLibrary.WindowObjectS;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.header.WindowHeader;
import com.Whodundid.core.windowLibrary.windowObjects.utilityObjects.FocusLockBorder;
import com.Whodundid.core.windowLibrary.windowObjects.windows.LinkConfirmationWindow;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.ITopParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowParent;
import com.Whodundid.core.windowLibrary.windowUtil.EGui;
import com.Whodundid.core.windowLibrary.windowUtil.EObjectGroup;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.ObjectEvent;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.ObjectEventHandler;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.FocusType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.MouseType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.ObjectEventType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventAction;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventFirstDraw;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventFocus;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventMouse;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventObjects;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventRedraw;
import java.net.URI;
import java.net.URISyntaxException;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;

//Author: Hunter Bragg

public abstract class WindowObject extends EGui implements IWindowObject {
	
	public WindowObject objectInstance;
	protected IWindowObject parent, focusObjectOnClose, defaultFocusObject;
	protected EDimension boundaryDimension;
	protected EArrayList<IWindowObject> windowObjects = new EArrayList();
	protected EArrayList<IWindowObject> objsToBeRemoved = new EArrayList();
	protected EArrayList<IWindowObject> objsToBeAdded = new EArrayList();
	protected ObjectEventHandler eventHandler = new ObjectEventHandler(this);
	protected ScreenLocation oldArea = ScreenLocation.out;
	protected EObjectGroup objectGroup;
	protected String objectName = "noname";
	protected String hoverText = null;
	protected int objZLevel = 0;
	protected long objectId = -1;
	protected boolean enabled = true;
	protected boolean visible = true;
	protected boolean hidden = false;
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
	protected boolean closesWithHud = false;
	protected boolean hasCustomHoverColor = false;
	protected int hoverTextColor = EColors.lime.intVal;
	private boolean hasBeenInitialized = false;
	private boolean objectInit = false;
	private boolean beingRemoved = false;
	
	public void init(IWindowObject objIn) {
		parent = objIn;
		objectInstance = this;
		res = new ScaledResolution(mc);
	}
	
	public void init(IWindowObject objIn, int xIn, int yIn) {
		parent = objIn;
		startX = xIn;
		startY = yIn;
		startXPos = startX;
		startYPos = startY;
		objectInstance = this;
		res = new ScaledResolution(mc);
	}
	
	public void init(IWindowObject objIn, int xIn, int yIn, int widthIn, int heightIn) { init(objIn, xIn, yIn, widthIn, heightIn, -1); }
	public void init(IWindowObject objIn, int xIn, int yIn, int widthIn, int heightIn, int objectIdIn) {
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
			
			if (mc.gameSettings.chatLinksPrompt) { addObject(new LinkConfirmationWindow(this, event.getValue())); }
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
		ITopParent p = getTopParent();
		EArrayList<IWindowObject> children = getAllChildren();
		if (!(p.getModifyType() == ObjectModifyType.Resize)) {
			if (children.contains(p.getFocusedObject())) { p.clearFocusedObject(); }
			if (children.contains(p.getFocusLockObject())) { p.clearFocusLockObject(); }
			if (children.contains(p.getModifyingObject())) { p.clearModifyingObject(); }
		}
		windowObjects.clear();
		objsToBeAdded.clear();
		
		preReInit();
		initObjects();
		postReInit();
		objectInit = true;
	}
	@Override public void preReInit() {}
	@Override public void postReInit() {}
	@Override public void onAdded() {}
	
	//main draw
	@Override
	public void drawObject(int mXIn, int mYIn) {
		updateBeforeNextDraw(mXIn, mYIn);
		try {
			if (checkDraw()) {
				GlStateManager.pushMatrix();
				GlStateManager.enableBlend();
				
				//draw all child objects
				for (IWindowObject o : windowObjects) {
					
					if (o.checkDraw() && !o.isHidden()) {
						GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
						
						if (!o.hasFirstDraw()) { o.onFirstDraw(); }
						o.drawObject(mX, mY);
						
						//draw greyed out overlay over everything if a focus lock object is present
						IWindowObject f = getTopParent().getFocusLockObject();
						if (f != null && o instanceof WindowHeader && (!o.equals(f) && !f.getAllChildren().contains(o))) {
							if (o.isVisible()) {
								EDimension d = o.getDimensions();
								drawRect(d.startX, d.startY, d.endX, d.endY, 0x77000000);
							}
						}
						
					}
				}

				GlStateManager.popMatrix();
			}
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	@Override public void onFirstDraw() { postEvent(new EventFirstDraw(this)); firstDraw = true; }
	@Override public boolean hasFirstDraw() { return firstDraw; }
	@Override public void updateCursorImage() { WindowObjectS.updateCursorImage(this); }
	@Override public void onMouseHover(int mX, int mY) { WindowObjectS.onMouseHover(this, mX, mY, hoverText, hasCustomHoverColor ? hoverTextColor : CoreApp.hoverTextColor.get()); }
	@Override public boolean isDrawingHover() { return getTopParent() != null && this.equals(getTopParent().getHoveringObject()); }
	@Override public WindowObject setHoverText(String textIn) { hoverText = textIn; return this; }
	@Override public WindowObject setHoverTextColor(int colorIn) { hoverTextColor = colorIn; hasCustomHoverColor = true; return this; }
	@Override public String getHoverText() { return hoverText; }
	
	//obj ids
	@Override public long getObjectID() { return objectId; }
	@Override public WindowObject setObjectID(long idIn) { objectId = idIn; return this; }
	@Override public String getObjectName() { return objectName; }
	@Override public WindowObject setObjectName(String nameIn) { objectName = nameIn; return this; }
	
	//drawing checks
	@Override public boolean checkDraw() { return persistent || visible; }
	@Override public boolean isEnabled() { return enabled; }
	@Override public boolean isVisible() { return visible; }
	@Override public boolean isHidden() { return hidden; }
	@Override public boolean isPersistent() { return persistent; }
	@Override public boolean isBoundaryEnforced() { return boundaryDimension != null; }
	@Override public boolean isResizing() { return getTopParent().getModifyingObject() == this && getTopParent().getModifyType() == ObjectModifyType.Resize; }
	@Override public boolean isMoving() { return getTopParent().getModifyingObject() == this && getTopParent().getModifyType() == ObjectModifyType.Move; }
	@Override public boolean isAlwaysOnTop() { return alwaysOnTop; }
	@Override public WindowObject setEnabled(boolean val) { enabled = val; return this; }
	@Override public WindowObject setVisible(boolean val) { visible = val; return this; }
	@Override public WindowObject setPersistent(boolean val) { persistent = val; return this; }
	@Override public WindowObject setAlwaysOnTop(boolean val) { alwaysOnTop = val; return this; }
	@Override public WindowObject setHidden(boolean val) { hidden = val; return this; }
	
	//size
	@Override public boolean hasHeader() { return WindowObjectS.hasHeader(this); }
	@Override public boolean isResizeable() { return resizeable; }
	@Override public WindowHeader getHeader() { return WindowObjectS.getHeader(this); }
	@Override public int getMinWidth() { return minWidth; }
	@Override public int getMinHeight() { return minHeight; }
	@Override public int getMaxWidth() { return maxWidth; }
	@Override public int getMaxHeight() { return maxHeight; }
	@Override public WindowObject setMinDims(int widthIn, int heightIn) { setMinWidth(widthIn).setMinHeight(heightIn); return this; }
	@Override public WindowObject setMaxDims(int widthIn, int heightIn) { setMaxWidth(widthIn).setMaxHeight(heightIn); return this; }
	@Override public WindowObject setMinWidth(int widthIn) { minWidth = widthIn; return this; }
	@Override public WindowObject setMinHeight(int heightIn) { minHeight = heightIn; return this; }
	@Override public WindowObject setMaxWidth(int widthIn) { maxWidth = widthIn; return this; }
	@Override public WindowObject setMaxHeight(int heightIn) { maxHeight = heightIn; return this; }
	@Override public WindowObject setResizeable(boolean val) { resizeable = val; return this; }
	@Override public IWindowObject resize(int xIn, int yIn, ScreenLocation areaIn) { WindowObjectS.resize(this, xIn, yIn, areaIn); return this; }
	
	//position
	@Override public void move(int newX, int newY) { WindowObjectS.move(this, newX, newY); }
	@Override public boolean isMoveable() { return moveable; }
	@Override
	public WindowObject resetPosition() {
		setDimensions(startXPos, startYPos, startWidth, startHeight);
		windowObjects.forEach(o -> o.resetPosition());
		return this;
	}
	@Override public WindowObject setPosition(int newX, int newY) { WindowObjectS.setPosition(this, newX, newY); return this; }
	@Override public WindowObject setMoveable(boolean val) { moveable = val; return this; }
	@Override public WindowObject setDimensions(EDimension dimIn) { return setDimensions(dimIn.startX, dimIn.startY, dimIn.width, dimIn.height); }
	@Override public WindowObject setDimensions(int widthIn, int heightIn) { return setDimensions(startX, startY, widthIn, heightIn); }
	@Override
	public WindowObject setDimensions(int startXIn, int startYIn, int widthIn, int heightIn) {
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
	@Override public WindowObject setInitialPosition(int startXIn, int startYIn) { startXPos = startXIn; startYPos = startYIn; return this; }
	@Override public StorageBox<Integer, Integer> getInitialPosition() { return new StorageBox<Integer, Integer>(startXPos, startYPos); }
	@Override public WindowObject centerObjectWithSize(int widthIn, int heightIn) { WindowObjectS.centerObjectWithSize(this, widthIn, heightIn); return this; }
	@Override public EDimension getDimensions() { return new EDimension(startX, startY, endX, endY); }
	
	//objects
	@Override public boolean isChild(IWindowObject objIn) { return WindowObjectS.isChildOfObject(this, objIn); }
	@Override public WindowObject addObject(IWindowObject obj, IWindowObject... additional) { WindowObjectS.addObject(this, obj, additional); return this; }
	@Override public WindowObject removeObject(IWindowObject obj, IWindowObject... additional) { WindowObjectS.removeObject(this, obj, additional); return this; }
	@Override public EObjectGroup getObjectGroup() { return objectGroup; }
	@Override public WindowObject setObjectGroup(EObjectGroup groupIn) { objectGroup = groupIn; return this; }
	@Override public void onGroupNotification(ObjectEvent e) {}
	@Override public EArrayList<IWindowObject> getObjects() { return windowObjects; }
	@Override public EArrayList<IWindowObject> getAddingObjects() { return objsToBeAdded; }
	@Override public EArrayList<IWindowObject> getRemovingObjects() { return objsToBeRemoved; }
	@Override public EArrayList<IWindowObject> getAllChildren() { return WindowObjectS.getAllChildren(this); }
	@Override public EArrayList<IWindowObject> getAllChildrenUnderMouse() { return WindowObjectS.getAllChildrenUnderMouse(this, mX, mY); }
	@Override public boolean containsObject(IWindowObject object) { return getCombinedChildren().contains(object); }
	@Override public <T> boolean containsObject(Class<T> objIn) { return objIn != null ? getAllChildren().stream().anyMatch(o -> objIn.isInstance(o)) : false; }
	@Override public EArrayList<IWindowObject> getCombinedChildren() { return EArrayList.combineLists(windowObjects, objsToBeAdded); }
	
	//parents
	@Override public IWindowObject getParent() { return parent; }
	@Override public WindowObject setParent(IWindowObject parentIn) { parent = parentIn; return this; }
	@Override public ITopParent getTopParent() { return WindowObjectS.getTopParent(this); }
	@Override public IWindowParent getWindowParent() { return WindowObjectS.getWindowParent(this); }
	
	//zLevel
	@Override public int getZLevel() { return WindowObjectS.getZLevel(this, objZLevel); }
	@Override public WindowObject setZLevel(int zLevelIn) { objZLevel = zLevelIn; return this; }
	@Override public WindowObject bringToFront() { getTopParent().bringObjectToFront(this); return this; }
	@Override public WindowObject sendToBack() { getTopParent().sendObjectToBack(this); return this; }
	
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
		if (eventIn.getFocusType().equals(FocusType.MousePress)) {
			mousePressed(eventIn.getMX(), eventIn.getMY(), eventIn.getActionCode());
			
			if (getTopParent() != null) {
				
				//check if elligible for a double click event
				if (eventIn.getActionCode() == 0) {
					IWindowObject lastClicked = getTopParent().getLastClickedObject();
					if (lastClicked == this) {
						long clickTime = getTopParent().getLastClickTime();
						
						if (System.currentTimeMillis() - clickTime <= 400) {
							onDoubleClick();
						}
					}
				}
				
				getTopParent().setLastClickedObject(this);
				getTopParent().setLastClickTime(System.currentTimeMillis());
			}
		}
		if (defaultFocusObject != null) { defaultFocusObject.requestFocus(); }
	}
	@Override public void onFocusLost(EventFocus eventIn) { postEvent(new EventFocus(this, this, FocusType.Lost)); }
	@Override
	public void transferFocus(IWindowObject objIn) {
		if (getTopParent().doesFocusLockExist() && getTopParent().getFocusLockObject().equals(this)) {
			if (objIn != null) {
				getTopParent().clearFocusLockObject();
				getTopParent().setObjectRequestingFocus(objIn);
			}
		}
		else if (objIn != null) { getTopParent().setObjectRequestingFocus(objIn); }
	}
	@Override
	public void drawFocusLockBorder() {
		if (checkDraw() && windowObjects.containsNoInstanceOf(FocusLockBorder.class)) {
			if (hasHeader() && getHeader().isEnabled()) {
				addObject(new FocusLockBorder(this, getHeader().startX, getHeader().startY, width, height + getHeader().height));
			}
			else { addObject(new FocusLockBorder(this)); }
		}
	}
	@Override
	public WindowObject requestFocus() {
		//System.out.println(this + " is requesting focus");
		getTopParent().setObjectRequestingFocus(this);
		return this;
	}
	@Override public IWindowObject getDefaultFocusObject() { return defaultFocusObject; }
	@Override public WindowObject setDefaultFocusObject(IWindowObject objIn) { defaultFocusObject = objIn; return this; }
	
	//mouse checks
	@Override public boolean isMouseOnObjEdge(int mX, int mY) { return checkDraw() && getEdgeAreaMouseIsOn() != ScreenLocation.out; }
	@Override public ScreenLocation getEdgeAreaMouseIsOn() { return WindowObjectS.getEdgeAreaMouseIsOn(this, mX, mY); }
	@Override public void mouseEntered(int mX, int mY) { postEvent(new EventMouse(this, mX, mY, -1, MouseType.Entered)); }
	@Override public void mouseExited(int mX, int mY) { postEvent(new EventMouse(this, mX, mY, -1, MouseType.Exited)); }
	@Override public boolean isMouseInside(int mX, int mY) { return WindowObjectS.isMouseInside(this, mX, mY); }
	@Override public boolean isMouseOver(int mX, int mY) { return isMouseInside(mX, mY) && this.equals(getTopParent().getHighestZObjectUnderMouse()); }
	@Override public WindowObject setBoundaryEnforcer(EDimension dimIn) { boundaryDimension = new EDimension(dimIn); return this; }
	@Override public EDimension getBoundaryEnforcer() { return boundaryDimension; }
	@Override public boolean isClickable() { return clickable; }
	@Override public WindowObject setClickable(boolean valIn) { clickable = valIn; return this; }
	@Override public WindowObject setEntiretyClickable(boolean val) { WindowObjectS.setEntiretyClickable(this, val); return this; }
	
	//basic inputs
	@Override public void parseMousePosition(int mX, int mY) { WindowObjectS.parseMousePosition(this, mX, mY); }
	@Override public void mousePressed(int mX, int mY, int button) { WindowObjectS.mousePressed(this, mX, mY, button); }
	@Override public void mouseReleased(int mX, int mY, int button) { WindowObjectS.mouseReleased(this, mX, mY, button); }
	@Override public void mouseDragged(int mX, int mY, int button, long timeSinceLastClick) {}
	@Override public void mouseScrolled(int change) { WindowObjectS.mouseScolled(this, mX, mY, change); }
	@Override public void onDoubleClick() {}
	@Override public void keyPressed(char typedChar, int keyCode) { WindowObjectS.keyPressed(this, typedChar, keyCode); }
	@Override public void keyReleased(char typedChar, int keyCode) { WindowObjectS.keyReleased(this, typedChar, keyCode); }
	
	//events
	@Override
	public void sendArgs(Object... args) {
		if (args.length == 1) {
			if (args[0] instanceof String) {
				String msg = (String) args[0];
				if (msg.equals("Reload")) {
					boolean any = false;
					for (IWindowObject o : getAllChildren()) {
						if (o.hasFocus()) { any = true; break; }
					}
					reInitObjects();
					if (any) { requestFocus(); }
				}
			}
		}
	}
	
	@Override public ObjectEventHandler getEventHandler() { return eventHandler; }
	@Override public WindowObject registerListener(IWindowObject objIn) { if (eventHandler != null) { eventHandler.registerObject(objIn); } return this; }
	@Override public WindowObject unregisterListener(IWindowObject objIn) { if (eventHandler != null) { eventHandler.unregisterObject(objIn); } return this; }
	@Override public WindowObject postEvent(ObjectEvent e) { if (eventHandler != null) { eventHandler.processEvent(e); } return this; }
	@Override public void onEvent(ObjectEvent e) {}
	
	//action object
	@Override public void actionPerformed(IActionObject object, Object... args) { postEvent(new EventAction(this, object, args)); }
	
	//close object
	@Override public void close() { close(true); }
	@Override
	public void close(boolean recursive) {
		if (closeable) {
			postEvent(new EventObjects(this, this, ObjectEventType.Close));
			if (recursive) { for (IWindowObject o : getAllChildren()) { o.close(false); } }
			if (getTopParent().doesFocusLockExist() && getTopParent().getFocusLockObject().equals(this)) { getTopParent().clearFocusLockObject(); }
			//if (getTopParent().getFocusedObject().equals(this)) { relinquishFocus(); }
			if (focusObjectOnClose != null) { focusObjectOnClose.requestFocus(); }
			parent.removeObject(this);
			closed = true;
		}
	}
	@Override public boolean closesWithHud() { return closesWithHud; }
	@Override public WindowObject setClosesWithHud(boolean val) { closesWithHud = val; return this; }
	@Override public boolean isCloseable() { return closeable; }
	@Override public boolean isClosed() { return closed; }
	@Override public WindowObject setCloseable(boolean val) { closeable = val; return this; }
	@Override public void onClosed() {}
	@Override public WindowObject setFocusedObjectOnClose(IWindowObject objIn) { focusObjectOnClose = objIn; return this; }
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
		if (!objsToBeRemoved.isEmpty()) { WindowObjectS.removeObjects(this, objsToBeRemoved); }
		if (!objsToBeAdded.isEmpty()) { WindowObjectS.addObjects(this, objsToBeAdded); }
	}
	
}
