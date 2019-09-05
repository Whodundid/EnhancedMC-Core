package com.Whodundid.core.enhancedGui;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjectUtil.EObjectGroup;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiFocusLockBorder;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiLinkConfirmationDialogueBox;
import com.Whodundid.core.enhancedGui.guiObjects.InnerEnhancedGui;
import com.Whodundid.core.enhancedGui.guiUtil.EGui;
import com.Whodundid.core.enhancedGui.guiUtil.HeaderAlreadyExistsException;
import com.Whodundid.core.enhancedGui.guiUtil.ObjectEventHandler;
import com.Whodundid.core.enhancedGui.guiUtil.ObjectInitException;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventAction;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventFocus;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventKeyboard;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventModify;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventMouse;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventObjects;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventRedraw;
import com.Whodundid.core.enhancedGui.guiUtil.events.ObjectEvent;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.FocusType;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.KeyboardType;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.MouseType;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.ObjectEventType;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.ObjectModifyType;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedTopParent;
import com.Whodundid.core.util.miscUtil.EFontRenderer;
import com.Whodundid.core.util.miscUtil.ScreenLocation;
import com.Whodundid.core.util.renderUtil.CursorHelper;
import com.Whodundid.core.util.renderUtil.Resources;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.stream.GuiTwitchUserMode;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityList;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.ClientCommandHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import tv.twitch.chat.ChatUserInfo;

//Jan 3, 2019
//Jan 24, 2019 : added objsToBeAdded
//Jan 25, 2019 : fixed logic behind adding objects with drawing delays, implemented fix for adding objects when moving
//Jan 26, 2019 : implemented a fix for zLevel drawing using drawOrder and newDrawOrder which are updated thorugh updateZLevel
//Last edited: Mar 26, 2019
//Edit note: implemented listeners
//First Added: Sep 19, 2018
//Author: Hunter Bragg

public abstract class EnhancedGuiObject extends EGui implements IEnhancedGuiObject {
	
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Set<String> PROTOCOLS = Sets.newHashSet(new String[] {"http", "https"});
	public static final Splitter NEWLINE_SPLITTER = Splitter.on('\n');
	public EnhancedGuiObject objectInstance;
	protected ScaledResolution res;
	protected EFontRenderer fontRenderer = EnhancedMC.getFontRenderer();
	protected IEnhancedGuiObject parent, movingObject, focusObjectOnClose;
	protected EDimension boundaryDimension;
	protected EGuiFocusLockBorder border;
	protected EArrayList<IEnhancedGuiObject> guiObjects = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> newDrawOrder = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> drawOrder = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> objsToBeRemoved = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> objsToBeAdded = new EArrayList();
	protected ObjectEventHandler eventHandler = new ObjectEventHandler(this);
	protected StorageBox<Integer, Integer> mousePos = new StorageBox(0, 0);
	protected ScreenLocation oldArea = ScreenLocation.out;
	protected EObjectGroup objectGroup;
	public URI clickedLinkURI;
	protected boolean hasBeenInitialized = false;
	protected boolean enabled = true;
	protected boolean visible = true;
	public boolean isMouseHover = false;
	public boolean mouseEntered = false;
	protected boolean positionLocked = false;
	protected boolean hasFocus = false;
	protected boolean focusLock = false;
	protected boolean isVanillaParent = false;
	protected boolean persistent = false;
	protected boolean resizeable = false;
	protected int minWidth = 0;
	protected int minHeight = 0;
	protected int maxWidth = 0;
	protected int maxHeight = 0;
	public int objZLevel = 0;
	public int objectId = -1;
	public boolean drawHoverText = false;
	public String hoverText = "";
	public int startXPos, startYPos, startWidth, startHeight;
	public int startX, startY, endX, endY;
	public int width, height;
	public int midX, midY;
	public int mX, mY;
	
	public void init(IEnhancedGuiObject objIn, int xIn, int yIn) {
		parent = objIn;
		startX = xIn;
		startY = yIn;
		startXPos = startX;
		startYPos = startY;
		objectInstance = this;
	}
	
	public void init(IEnhancedGuiObject objIn, int xIn, int yIn, int widthIn, int heightIn) {
		init(objIn, xIn, yIn, widthIn, heightIn, -1);
	}
	
	public void init(IEnhancedGuiObject objIn, int xIn, int yIn, int widthIn, int heightIn, int objectIdIn) {
		parent = objIn;
		objectId = objectIdIn;
		startXPos = xIn;
		startYPos = yIn;
		startWidth = widthIn;
		startHeight = heightIn;
		setDimensions(xIn, yIn, widthIn, heightIn);
		objectInstance = this;
	}
	
	//----------------
	//Object Overrides
	//----------------
	
	@Override public String toString() { return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()); }
	
	//----------------------------
	//IEnhancedGuiObject Overrides
	//----------------------------
	
	//init
	@Override public boolean hasBeenInitialized() { return hasBeenInitialized; }
	@Override public EnhancedGuiObject completeInitialization() { hasBeenInitialized = true; return this; }
	@Override public void initObjects() throws ObjectInitException {}
	@Override
	public void reInitObjects() throws ObjectInitException {
		hasBeenInitialized = false;
		IEnhancedTopParent p = getTopParent();
		EArrayList<IEnhancedGuiObject> children = getAllChildren();
		if (!p.isResizing()) {
			if (children.contains(p.getFocusedObject())) { p.clearFocusedObject(); }
			if (children.contains(p.getFocusLockObject())) { p.clearFocusLockObject(); }
			if (children.contains(p.getModifyingObject())) { p.clearModifyingObject(); }
		}
		//for (IEnhancedGuiObject o : children) { o.getImmediateChildren().clear(); }
		guiObjects.clear();
		
		initObjects();
		hasBeenInitialized = true;
	}
	@Override public void onObjectAddedToParent() {}
	
	//main draw
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		updateBeforeNextDraw(mXIn, mYIn);
		try {
			if (checkDraw()) {
				GlStateManager.pushMatrix();
				GlStateManager.enableBlend();
				for (IEnhancedGuiObject o : guiObjects) {
					if (o.checkDraw()) {
	    				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	    	        	o.drawObject(mXIn, mYIn, ticks);
	    			}
				}
				GlStateManager.popMatrix();
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	@Override
	public void updateCursorImage() {
		if (isResizeable()) {
			int rStartY = hasHeader() ? getHeader().startY : startY;
			boolean inside = (mX >= startX && mX <= endX && mY >= rStartY && mY <= endY);
			ScreenLocation newArea = getEdgeAreaMouseIsOn();
			if (newArea != oldArea) {
				if (!Mouse.isButtonDown(0)) {
					oldArea = newArea;
					switch (newArea) {
					case top:
					case bot: if (inside) { CursorHelper.setCursor(CursorHelper.createCursorFromResourceLocation(Resources.mouseResizeNS)); } break;
					case left:
					case right: if (inside) { CursorHelper.setCursor(CursorHelper.createCursorFromResourceLocation(Resources.mouseResizeEW)); } break;
					case topRight:
					case botLeft: if (inside) { CursorHelper.setCursor(CursorHelper.createCursorFromResourceLocation(Resources.mouseResizeDL)); } break;
					case topLeft:
					case botRight: if (inside) { CursorHelper.setCursor(CursorHelper.createCursorFromResourceLocation(Resources.mouseResizeDR)); } break;
					default: CursorHelper.setCursor(null); break;
					}
				} else { CursorHelper.setCursor(null); }				
			}
		}
	}
	@Override public EnhancedGuiObject setDrawHoverText(boolean val) { drawHoverText = val; return this; }
	@Override public boolean drawsHoverText() { return drawHoverText; }
	@Override public EnhancedGuiObject setHoverText(String textIn) { hoverText = textIn; return this; }
	@Override public String getHoverText() { return hoverText; }
	
	//obj ids
	@Override public int getObjectID() { return objectId; }
	@Override public EnhancedGuiObject setObjectID(int idIn) { objectId = idIn; return this; }
	
	//drawing checks
	@Override public boolean checkDraw() { return persistent || visible; }
	@Override public boolean isEnabled() { return enabled; }
	@Override public boolean isVisible() { return visible; }
	@Override public boolean isPersistent() { return persistent; }
	@Override public boolean isBoundaryEnforced() { return boundaryDimension != null; }
	@Override public EnhancedGuiObject setEnabled(boolean val) { enabled = val; return this; }
	@Override public EnhancedGuiObject setVisible(boolean val) { visible = val; return this; }
	@Override public EnhancedGuiObject setPersistent(boolean val) { persistent = val; return this; }
	@Override public EnhancedGuiObject setBoundaryEnforcer(EDimension dimIn) { boundaryDimension = dimIn; return this; }
	
	//size
	@Override public boolean hasHeader() { for (IEnhancedGuiObject o : guiObjects) { if (o instanceof EGuiHeader) { return true; } } return false; }
	@Override public boolean isResizeable() { return resizeable; }
	@Override public EGuiHeader getHeader() { for (IEnhancedGuiObject o : guiObjects) { if (o instanceof EGuiHeader) { return (EGuiHeader) o; } } return null; }
	@Override public int getMinimumWidth() { return minWidth; }
	@Override public int getMinimumHeight() { return minHeight; }
	@Override public int getMaximumWidth() { return maxWidth; }
	@Override public int getMaximumHeight() { return maxHeight; }
	@Override public EnhancedGuiObject setMinimumWidth(int widthIn) { minWidth = widthIn; return this; }
	@Override public EnhancedGuiObject setMinimumHeight(int heightIn) { minHeight = heightIn; return this; }
	@Override public EnhancedGuiObject setMaximumWidth(int widthIn) { maxWidth = widthIn; return this; }
	@Override public EnhancedGuiObject setMaximumHeight(int heightIn) { maxHeight = heightIn; return this; }
	@Override public EnhancedGuiObject setResizeable(boolean val) { resizeable = val; return this; }
	@Override
	public EnhancedGuiObject resize(int xIn, int yIn, ScreenLocation areaIn) {
		if (eventHandler != null) { eventHandler.processEvent(new EventModify(this, this, ObjectModifyType.Resize)); }
		if (xIn != 0 || yIn != 0) {
			int x = 0, y = 0, w = 0, h = 0;
			boolean e = false, s = false;
			switch (areaIn) {
			case top: x = startX; y = startY + yIn; w = width; h = height - yIn; break;
			case bot: x = startX; y = startY; w = width; h = height + yIn; break;
			case right: x = startX; y = startY; w = width + xIn; h = height; break;
			case left: x = startX + xIn; y = startY; w = width - xIn; h = height; break;
			case topRight: x = startX; y = startY + yIn; w = width + xIn; h = height - yIn; break;
			case botRight: x = startX; y = startY; w = width + xIn; h = height + yIn; break;
			case topLeft: x = startX + xIn; y = startY + yIn; w = width - xIn; h = height - yIn; break;
			case botLeft: x = startX + xIn; y = startY; w = width - xIn; h = height + yIn; break;
			default: break;
			}
			if (w < getMinimumWidth()) {
				w = getMinimumWidth();
				switch (areaIn) {
				case right: case topRight: case botRight: x = startX; break;
				case left: case topLeft: case botLeft: x = endX - w; break;
				default: break;
				}
			}
			if (h < getMinimumHeight()) {
				h = getMinimumHeight();
				switch (areaIn) {
				case top: case topRight: case topLeft: y = endY - h; break;
				case bot: case botRight: case botLeft: y = startY; break;
				default: break;
				}
			}
			setDimensions(x, y, w, h);
			try {
				reInitObjects();
			} catch (ObjectInitException q) { q.printStackTrace(); }
		}
		return this;
	}
	
	//position
	@Override
	public void move(int newX, int newY) {
		if (eventHandler != null) { eventHandler.processEvent(new EventModify(this, this, ObjectModifyType.Move)); }
		if (!positionLocked) {
			EArrayList<IEnhancedGuiObject> objs = new EArrayList(guiObjects);
			objs.addAll(objsToBeAdded);
			Iterator<IEnhancedGuiObject> it = objs.iterator();
			while (it.hasNext()) {
				IEnhancedGuiObject o = it.next();
				if (!o.isPositionLocked()) {
					if (o instanceof InnerEnhancedGui) {
						if (((InnerEnhancedGui) o).movesWithParent()) { o.move(newX, newY); }
					} else { o.move(newX, newY); }
				}
			}
			startX += newX;
			startY += newY;
			if (boundaryDimension != null) {
				boundaryDimension.setPosition(boundaryDimension.startX += newX, boundaryDimension.startY += newY);
			}
			setDimensions(startX, startY, width, height);
		}
	}
	@Override public boolean isPositionLocked() { return positionLocked; }
	@Override
	public EnhancedGuiObject resetPosition() {
		setDimensions(startXPos, startYPos, startWidth, startHeight);
		for (IEnhancedGuiObject o : guiObjects) { o.resetPosition(); }
		return this;
	}
	@Override
	public EnhancedGuiObject setPosition(int newX, int newY) {
		StorageBox<Integer, Integer> loc = new StorageBox(startX, startY);
		StorageBoxHolder<IEnhancedGuiObject, StorageBox<Integer, Integer>> previousLocations = new StorageBoxHolder();
		for (IEnhancedGuiObject o : getImmediateChildren()) {
			previousLocations.add(o, new StorageBox(o.getDimensions().startX - loc.getObject(), o.getDimensions().startY - loc.getValue()));
		}
		setDimensions(newX, newY, width, height);
		for (IEnhancedGuiObject o : getImmediateChildren()) {
			StorageBox<Integer, Integer> oldLoc = previousLocations.getBoxWithObj(o).getValue();
			o.setPosition(newX + oldLoc.getObject(), newY + oldLoc.getValue());
		}
		return this;
	}
	@Override public EnhancedGuiObject setPositionLocked(boolean val) { positionLocked = val; return this; }
	@Override public EnhancedGuiObject setDimensions(EDimension dimIn) { return setDimensions(dimIn.startX, dimIn.startY, dimIn.width, dimIn.height); }
	@Override public EnhancedGuiObject setDimensions(int startXIn, int startYIn, int widthIn, int heightIn) {
		startX = startXIn;
		startY = startYIn;
		width = widthIn;
		height = heightIn;
		endX = startX + widthIn;
		endY = startY + heightIn;
		midX = startX + width / 2;
		midY = startY + height / 2;
		return this;
	}
	@Override public EnhancedGuiObject setInitialPosition(int startXIn, int startYIn) { startXPos = startXIn; startYPos = startYIn; return this; }
	@Override public StorageBox<Integer, Integer> getInitialPosition() { return new StorageBox<Integer, Integer>(startXPos, startYPos); }
	@Override public EDimension getDimensions() { return new EDimension(startX, startY, endX, endY); }
	
	//objects
	@Override
	public boolean isChildOfObject(IEnhancedGuiObject objIn) {
		IEnhancedGuiObject curObj = this;
		while (curObj != null && curObj.getParent() != null) {
			if (curObj.getParent().equals(curObj)) { return false; }
			if (curObj.getParent().equals(objIn)) { return true; }
			curObj = curObj.getParent();
		}
		return false;
	}
	@Override
	public EnhancedGuiObject addObject(IEnhancedGuiObject... objsIn) {
		for (IEnhancedGuiObject o : objsIn) {
			if (o != null) {
				if (o != this) {
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
						if (o instanceof InnerEnhancedGui) { ((InnerEnhancedGui) o).initGui(); }
						o.completeInitialization();
					} catch (ObjectInitException e) { e.printStackTrace(); }
				}
			}
		}
		//getTopParent().addObject(objsIn);
		objsToBeAdded.addAll(objsIn);
		return this;
	}
	@Override
	public EnhancedGuiObject removeObject(IEnhancedGuiObject... objsIn) {
		//getTopParent().removeObject(objsIn);
		objsToBeRemoved.addAll(objsIn);
		return this;
	}
	@Override public EObjectGroup getObjectGroup() { return objectGroup; }
	@Override public EnhancedGuiObject setObjectGroup(EObjectGroup groupIn) { objectGroup = groupIn; return this; }
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
		EArrayList<IEnhancedGuiObject> foundObjs = getAllChildren();
		EArrayList<IEnhancedGuiObject> underMouse = new EArrayList();
		for (IEnhancedGuiObject o : foundObjs) {
			if (o.checkDraw() && o.isMouseInside(mX, mY)) {
				underMouse.add(o);
			}
		}
		return underMouse;
	}
	
	//parents
	@Override public IEnhancedGuiObject getParent() { return parent; }
	@Override public EnhancedGuiObject setParent(IEnhancedGuiObject parentIn) { parent = parentIn; return this; }
	@Override
	public IEnhancedTopParent getTopParent() {
		IEnhancedGuiObject parentObj = getParent();
		while (parentObj != null) {
			if (parentObj instanceof IEnhancedTopParent) { return (IEnhancedTopParent) parentObj; }
			if (parentObj.getParent() != null) { parentObj = parentObj.getParent(); }
		}
		return null;
	}
	
	//zLevel
	@Override
	public int getZLevel() {
		int zLevel = new Integer(objZLevel);
		IEnhancedGuiObject lastObj = getParent();
		if (lastObj != null && !lastObj.equals(this)) {
			while (lastObj != null && lastObj.getParent() != lastObj) {
				zLevel += lastObj.getZLevel();
				lastObj = lastObj.getParent();
			}
		}
		return zLevel;
	}
	@Override public EnhancedGuiObject setZLevel(int zLevelIn) { objZLevel = zLevelIn; return this; }
	@Override
	public EnhancedGuiObject bringToFront() {
		getTopParent().bringObjectToFront(this);
		return this;
	}
	@Override
	public EnhancedGuiObject sendToBack() {
		getTopParent().sendObjectToBack(this);
		return this;
	}
	
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
		if (eventHandler != null) { eventHandler.processEvent(new EventFocus(this, this, FocusType.Gained)); }
		if (eventIn.getFocusType().equals(FocusType.MousePress)) { mousePressed(eventIn.getMouseX(), eventIn.getMouseY(), eventIn.getActionCode()); }
	}
	@Override
	public void onFocusLost(EventFocus eventIn) {
		if (eventHandler != null) { eventHandler.processEvent(new EventFocus(this, this, FocusType.Lost)); }
	}
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
		if (checkDraw() && border == null) {
			if (hasHeader() && getHeader().isEnabled()) {
				addObject(border = new EGuiFocusLockBorder(this, getHeader().startX, getHeader().startY, width, height + getHeader().height));
			} else { addObject(border = new EGuiFocusLockBorder(this)); }
		}
	}
	@Override public EnhancedGuiObject requestFocus() {
		if (!hasFocus()) { getTopParent().setObjectRequestingFocus(this); }
		return this;
	}
	
	//mouse checks
	@Override public boolean isMouseOnObjEdge(int mX, int mY) { return checkDraw() && !getEdgeAreaMouseIsOn().equals(ScreenLocation.out); }
	@Override
	public ScreenLocation getEdgeAreaMouseIsOn() {
		boolean left = false, right = false, top = false, bottom = false;
		int rStartY = hasHeader() ? getHeader().startY : startY;
		if (mX >= startX && mX <= startX + 1) { left = true; }
		if (mX <= endX && mX >= endX - 1) { right = true; }
		if (mY >= rStartY && mY <= rStartY + 1) { top = true; }
		if (mY <= endY && mY >= endY - 1) { bottom = true; }
		if (checkDraw() && !(left || right || top || bottom)) { return ScreenLocation.out; }
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
	@Override
	public void mouseEntered(int mX, int mY) {
		if (eventHandler != null) { eventHandler.processEvent(new EventMouse(this, mX, mY, -1, MouseType.Entered)); }
	}
	@Override
	public void mouseExited(int mX, int mY) {
		if (eventHandler != null) { eventHandler.processEvent(new EventMouse(this, mX, mY, -1, MouseType.Exited)); }
	}
	@Override
	public boolean isMouseInside(int mX, int mY) {
		if (isBoundaryEnforced()) {
			EDimension b = boundaryDimension;
			return mX >= startX && mX >= b.startX && mX <= endX && mX <= b.endX && mY >= startY && mY >= b.startY && mY <= endY && mY <= b.endY;
		}
		return mX >= startX && mX <= endX && mY >= startY && mY <= endY;
	}
	
	//basic inputs
	@Override public void parseMousePosition(int mX, int mY) {}
	@Override
	public void mousePressed(int mX, int mY, int button) {
		if (eventHandler != null) { eventHandler.processEvent(new EventMouse(this, mX, mY, button, MouseType.Pressed)); }
		if (isMouseHover) { requestFocus(); }
		if (button == 0 && isResizeable() && !getEdgeAreaMouseIsOn().equals(ScreenLocation.out)) {
			getTopParent().setResizingDir(getEdgeAreaMouseIsOn());
			getTopParent().setModifyMousePos(mX, mY);
			getTopParent().setModifyingObject(this, ObjectModifyType.Resize);
		}
	}
	@Override
	public void mouseReleased(int mX, int mY, int button) {
		if (eventHandler != null) { eventHandler.processEvent(new EventMouse(this, mX, mY, button, MouseType.Released)); }
		if (getTopParent().isResizing()) { getTopParent().clearModifyingObject(); }
		if (getTopParent().getDefaultFocusObject() != null) { getTopParent().getDefaultFocusObject().requestFocus(); }
	}
	@Override public void mouseDragged(int mX, int mY, int button, long timeSinceLastClick) {}
	@Override
	public void mouseScrolled(int change) {
		if (eventHandler != null) { eventHandler.processEvent(new EventMouse(this, mX, mY, -1, MouseType.Scrolled)); }
	}
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (eventHandler != null) { eventHandler.processEvent(new EventKeyboard(this, typedChar, keyCode, KeyboardType.Pressed)); }
		if (getTopParent() != null && keyCode == 15) {
			if (guiObjects != null) {
				if (guiObjects.isEmpty()) {
					int thisObjPos = 0;
					for (int i = 0; i < getTopParent().getImmediateChildren().size(); i++) {
						if (getTopParent().getImmediateChildren().get(i).equals(this)) { thisObjPos = i; }
					}
					if (thisObjPos < getTopParent().getImmediateChildren().size() - 1) {
						getTopParent().getImmediateChildren().get(thisObjPos + 1).requestFocus();
					}
				} else {
					EnhancedGuiObject selectedChild = null;
					for (IEnhancedGuiObject o : getTopParent().getImmediateChildren()) {
						if (guiObjects.contains(o) && o instanceof EnhancedGuiObject) { selectedChild = (EnhancedGuiObject) o; }
					}
					if (selectedChild != null) {
						int childPos = 0;
						for (int i = 0; i < guiObjects.size(); i++) {
							if (selectedChild.equals(guiObjects.get(i))) { childPos = i; }
						}
						if (childPos < guiObjects.size() - 1) { guiObjects.get(childPos + 1).requestFocus(); }
					}
				}
			}
		}
	}
	@Override
	public void keyReleased(char typedChar, int keyCode) {
		if (eventHandler != null) { eventHandler.processEvent(new EventKeyboard(this, typedChar, keyCode, KeyboardType.Released)); }
	}
	
	//updateScreen
	@Override
	public void updateScreen() {
		for (IEnhancedGuiObject o : guiObjects) { o.updateScreen(); }
	}
	
	//events
	@Override public ObjectEventHandler getEventHandler() { return eventHandler; }
	@Override public EnhancedGuiObject registerListener(IEnhancedGuiObject objIn) { if (eventHandler != null) { eventHandler.registerObject(objIn); } return this; }
	@Override public EnhancedGuiObject unregisterListener(IEnhancedGuiObject objIn) { if (eventHandler != null) { eventHandler.unregisterObject(objIn); } return this; }
	@Override public void onListen(ObjectEvent e) {}
	
	//action object
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (eventHandler != null) { eventHandler.processEvent(new EventAction(this, object)); }
	}
	
	//close object
	@Override public void close() {
		//System.out.println(this + " " + parent);
		if (eventHandler != null) { eventHandler.processEvent(new EventObjects(this, this, ObjectEventType.Close)); }
		if (getTopParent().doesFocusLockExist() && getTopParent().getFocusLockObject().equals(this)) { getTopParent().clearFocusLockObject(); }
		if (getTopParent().getFocusedObject().equals(this)) { relinquishFocus(); }
		if (focusObjectOnClose != null) { focusObjectOnClose.requestFocus(); }
		parent.removeObject(this);
	}
	@Override public EnhancedGuiObject setFocusedObjectOnClose(IEnhancedGuiObject objIn) { focusObjectOnClose = objIn; return this; }
	
	//-------------------------
	//EnhancedGuiObject methods
	//-------------------------
	
	protected void updateBeforeNextDraw(int mXIn, int mYIn) {
		if (eventHandler != null) { eventHandler.processEvent(new EventRedraw(this)); }
		res = new ScaledResolution(mc);
		mX = mXIn; mY = mYIn;
		isMouseHover = isMouseInside(mXIn, mYIn) && getTopParent().getHighestZObjectUnderMouse() != null && getTopParent().getHighestZObjectUnderMouse().equals(this);
		if (!mouseEntered && isMouseHover) { mouseEntered = true; mouseEntered(mX, mY); }
		if (mouseEntered && !isMouseHover) { mouseEntered = false; mouseExited(mX, mY); }
		if (!objsToBeRemoved.isEmpty()) { removeObjects(); }
		if (!objsToBeAdded.isEmpty()) { addObjects(); }
		if (!newDrawOrder.isEmpty()) { drawOrder = new EArrayList(newDrawOrder); newDrawOrder.clear(); }
		updateCursorImage();
	}
	
	public void setText(String textIn, boolean overwrite) {}
	
	public void sendChatMessage(String msg) { sendChatMessage(msg, true); }
	public void sendChatMessage(String msg, boolean addToChat) {
		if (addToChat) { mc.ingameGUI.getChatGUI().addToSentMessages(msg); }
		if (msg.startsWith("/") && ClientCommandHandler.instance.executeCommand(mc.thePlayer, msg) != 0) { return; }
		mc.thePlayer.sendChatMessage(msg);
    }
	
	protected void removeObjects() {
		synchronized (guiObjects) {
			objsToBeRemoved.forEach(o -> {
				if (o != null) {
					Iterator it = guiObjects.iterator();
					while (it.hasNext()) {
						if (o.equals(it.next())) {
							if (!o.equals(getTopParent().getFocusedObject())) {
								for (IEnhancedGuiObject child : o.getImmediateChildren()) {
									if (child.equals(getTopParent().getFocusedObject())) { child.relinquishFocus(); }
								}
							} else { o.relinquishFocus(); }
							if (o instanceof InnerEnhancedGui) { ((InnerEnhancedGui) o).onInnerGuiClose(); }
							if (o.equals(border)) { border = null; }
							if (eventHandler != null) { eventHandler.processEvent(new EventObjects(this, o, ObjectEventType.ObjectRemoved)); }
							it.remove();
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
	
	protected void renderToolTip(ItemStack stack, int x, int y) {
        List<String> list = stack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);

        for (int i = 0; i < list.size(); i++) {
            if (i == 0) { list.set(i, stack.getRarity().rarityColor + list.get(i)); }
            else { list.set(i, EnumChatFormatting.GRAY + list.get(i)); }
        }
        drawHoveringText(list, x, y);
    }
	
    protected void drawCreativeTabHoveringText(String tabName, int mX, int mY) {
        drawHoveringText(Arrays.<String>asList(new String[] {tabName}), mX, mY);
    }

    protected void drawHoveringText(List<String> textLines, int mX, int mY) {
    	if (!textLines.isEmpty()) {
            //GlStateManager.disableRescaleNormal();
            //RenderHelper.disableStandardItemLighting();
            //GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int i = 0;

            for (String s : textLines) {
                int j = fontRenderer.getStringWidth(s);
                if (j > i) { i = j; } //find longest string
            }

            int l1 = mX + 12; //x offset
            int i2 = mY - 12; //y offset
            int k = 8; //initial height offset

            if (textLines.size() > 1) { k += 2 + (textLines.size() - 1) * 10; } //calculate height
            ScaledResolution res = new ScaledResolution(mc);
            if (l1 + i + 4 > res.getScaledWidth()) { l1 = res.getScaledWidth() - i - 4; } //clamp width to screen
            if (mY + k - 7 > res.getScaledHeight()) { i2 = res.getScaledHeight() - k - 5; } //clamp height to screen

            zLevel = 300.0F;
            renderItem.zLevel = 300.0F;
            int l = -267386864;
            drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, l, l); //top
            drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, l, l); //bottom
            drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, l, l); //background
            drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, l, l); //left
            drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, l, l); //right
            int i1 = 1347420415;
            int j1 = (i1 & 16711422) >> 1 | i1 & -16777216;
            drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, i1, j1); //inner left
            drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, i1, j1); //inner right
            drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, i1, i1); //inner top
            drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, j1, j1); //inner bottom

            for (int k1 = 0; k1 < textLines.size(); k1++) {
                String s1 = textLines.get(k1);
                drawStringWithShadow(s1, l1, i2, -1);
                if (k1 == 0) { i2 += 2; }
                i2 += 10;
            }

            zLevel = 0.0F;
            renderItem.zLevel = 0.0F;
            //GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            //RenderHelper.enableStandardItemLighting();
            //GlStateManager.enableRescaleNormal();
        }
    }
	
	protected void handleComponentHover(IChatComponent componentIn, int mX, int mY)  {
		if (componentIn != null) {
			if (componentIn.getChatStyle().getChatHoverEvent() != null) {
	            HoverEvent hoverevent = componentIn.getChatStyle().getChatHoverEvent();
	            
	            if (hoverevent.getAction() == HoverEvent.Action.SHOW_ITEM) {
	                ItemStack itemstack = null;
	                
	                try {
	                    NBTBase nbtbase = JsonToNBT.getTagFromJson(hoverevent.getValue().getUnformattedText());
	                    if (nbtbase instanceof NBTTagCompound) { itemstack = ItemStack.loadItemStackFromNBT((NBTTagCompound)nbtbase); }
	                } catch (NBTException var11) { var11.printStackTrace(); }

	                if (itemstack != null) { renderToolTip(itemstack, mX, mY); }
	                else {
	                	drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Item!", mX, mY);
	                }
	            }
	            else if (hoverevent.getAction() == HoverEvent.Action.SHOW_ENTITY) {
	                if (this.mc.gameSettings.advancedItemTooltips) {
	                    try {
	                        NBTBase nbtbase1 = JsonToNBT.getTagFromJson(hoverevent.getValue().getUnformattedText());

	                        if (nbtbase1 instanceof NBTTagCompound) {
	                            List<String> list1 = Lists.<String>newArrayList();
	                            NBTTagCompound nbttagcompound = (NBTTagCompound)nbtbase1;
	                            list1.add(nbttagcompound.getString("name"));

	                            if (nbttagcompound.hasKey("type", 8)) {
	                                String s = nbttagcompound.getString("type");
	                                list1.add("Type: " + s + " (" + EntityList.getIDFromString(s) + ")");
	                            }

	                            list1.add(nbttagcompound.getString("id"));
	                            drawHoveringText(list1, mX, mY);
	                        }
	                        else {
	                            drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Entity!", mX, mY);
	                        }
	                    }
	                    catch (NBTException var10) {
	                        drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Entity!", mX, mY);
	                    }
	                }
	            }
	            else if (hoverevent.getAction() == HoverEvent.Action.SHOW_TEXT) {
	            	String text = hoverevent.getValue().getFormattedText();
	            	drawHoveringText(NEWLINE_SPLITTER.splitToList(text), mX, mY);
	            }
	            else if (hoverevent.getAction() == HoverEvent.Action.SHOW_ACHIEVEMENT) {
	                StatBase statbase = StatList.getOneShotStat(hoverevent.getValue().getUnformattedText());

	                if (statbase != null) {
	                    IChatComponent ichatcomponent = statbase.getStatName();
	                    IChatComponent ichatcomponent1 = new ChatComponentTranslation("stats.tooltip.type." + (statbase.isAchievement() ? "achievement" : "statistic"), new Object[0]);
	                    ichatcomponent1.getChatStyle().setItalic(Boolean.valueOf(true));
	                    String s1 = statbase instanceof Achievement ? ((Achievement)statbase).getDescription() : null;
	                    List<String> list = Lists.newArrayList(new String[] {ichatcomponent.getFormattedText(), ichatcomponent1.getFormattedText()});

	                    if (s1 != null) { list.addAll(fontRenderer.listFormattedStringToWidth(s1, 150)); }

	                    drawHoveringText(list, mX, mY);
	                }
	                else { drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid statistic/achievement!", mX, mY); }
	            }

	            GlStateManager.disableLighting();
	        }
		}
    }
	
	protected boolean handleComponentClick(IChatComponent componentIn) {
        if (componentIn == null) { return false; }
		ClickEvent clickevent = componentIn.getChatStyle().getChatClickEvent();
		if (clickevent != null && clickevent.getAction() != null) {
			if (clickevent.getAction() == ClickEvent.Action.OPEN_URL) {
			    if (!mc.gameSettings.chatLinks) { return false; }
			    try {
			        URI uri = new URI(clickevent.getValue());
			        String s = uri.getScheme();
			        
			        if (s == null) { throw new URISyntaxException(clickevent.getValue(), "Missing protocol"); }
			        
			        if (!PROTOCOLS.contains(s.toLowerCase())) {
			            throw new URISyntaxException(clickevent.getValue(), "Unsupported protocol: " + s.toLowerCase());
			        }
			        
			        if (mc.gameSettings.chatLinksPrompt) { addObject(new EGuiLinkConfirmationDialogueBox(this, clickevent.getValue())); }
			        else { openWebLink(clickevent.getValue()); }
			    }
			    catch (URISyntaxException urisyntaxexception) {
			        LOGGER.error("Can\'t open url for " + clickevent, urisyntaxexception);
			    }
			}
			else if (clickevent.getAction() == ClickEvent.Action.OPEN_FILE) {
			    openWebLink(clickevent.getValue());
			}
			else if (clickevent.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
				System.out.println("suggesting command: " + clickevent.getValue());
			    setText(clickevent.getValue(), true);
			}
			else if (clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
				System.out.println("running command: " + clickevent.getValue());
			    sendChatMessage(clickevent.getValue(), false);
			}
			else if (clickevent.getAction() == ClickEvent.Action.TWITCH_USER_INFO) {
			    ChatUserInfo chatuserinfo = this.mc.getTwitchStream().func_152926_a(clickevent.getValue());
			    if (chatuserinfo != null) {
			        mc.displayGuiScreen(new GuiTwitchUserMode(mc.getTwitchStream(), chatuserinfo));
			    }
			    else { LOGGER.error("Tried to handle twitch user but couldn\'t find them!"); }
			}
			else { LOGGER.error("Don\'t know how to handle " + clickevent); }

			return true;
		}
		return false;
    }
	
	protected void openWebLink(String linkIn) {
		if (linkIn != null && !linkIn.isEmpty()) {
			try {
				URI uri = new URI(linkIn);
	            Class<?> oclass = Class.forName("java.awt.Desktop");
	            Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
	            oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {uri});
	        }
	        catch (Throwable throwable) { LOGGER.error("Couldn\'t open link", throwable); }
		}
    }
	
	public static boolean isCtrlKeyDown() { return Minecraft.isRunningOnMac ? Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220) : Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157); }
    public static boolean isShiftKeyDown() { return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54); }
    public static boolean isAltKeyDown() { return Keyboard.isKeyDown(56) || Keyboard.isKeyDown(184); }
}
