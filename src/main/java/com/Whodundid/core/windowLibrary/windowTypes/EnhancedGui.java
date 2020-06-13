package com.Whodundid.core.windowLibrary.windowTypes;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreApp.CoreApp;
import com.Whodundid.core.renderer.renderUtil.IRendererProxy;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.chatUtil.EChatUtil;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.CursorHelper;
import com.Whodundid.core.util.renderUtil.GLObject;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.core.windowLibrary.StaticTopParent;
import com.Whodundid.core.windowLibrary.WindowObjectS;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.header.WindowHeader;
import com.Whodundid.core.windowLibrary.windowObjects.utilityObjects.FocusLockBorder;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.ITopParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowParent;
import com.Whodundid.core.windowLibrary.windowUtil.EObjectGroup;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.ObjectEvent;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.ObjectEventHandler;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.FocusType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.KeyboardType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.MouseType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.ObjectEventType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventAction;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventFocus;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventKeyboard;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventMouse;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventObjects;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventRedraw;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;
import java.util.Stack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

//Author: Hunter Bragg

public abstract class EnhancedGui extends GuiScreen implements ITopParent, IWindowParent, IRendererProxy {
	
	public static final Set<String> PROTOCOLS = Sets.newHashSet(new String[] {"http", "https"});
	public static final Splitter NEWLINE_SPLITTER = Splitter.on('\n');
	public EnhancedGui guiInstance;
	public GuiScreen oldGui;
	protected EDimension boundaryDimension;
	protected IWindowObject parent, modifyingObject;
	public URI clickedLinkURI;
	public int startXPos, startYPos, startWidth, startHeight;
	public int sWidth = 0, sHeight = 0;
	public int midX = 0, midY = 0;
	public int startX, startY, endX, endY;
	public int width = 220, height = 255;
	protected long lastMouseEvent;
	protected long timeSinceLastClick;
	public int mX, mY;
	protected int lastMouseButton = -1, lastScrollChange = 0;
	public boolean mouseClicked = false, leftClick = false, rightClick = false, middleClick = false;
	public boolean useCustomPosition = false;
	protected boolean backwardsTraverseable = true;
	protected boolean hasBeenInitialized = false;
	protected boolean objectInit = false;
	protected boolean enabled = true;
	protected boolean visible = true;
	protected boolean hidden = false;
	public boolean isMouseHover = false;
	public boolean mouseEntered = false;
	protected boolean resizeable = false;
	protected boolean clickable = true;
	protected boolean moveable = false;
	protected boolean persistent = false;
	protected boolean pinned = false;
	protected ScreenLocation maximized = ScreenLocation.out;
	protected boolean pinnable = false;
	protected boolean maximizable = false;
	protected boolean closeAndRecenter = false;
	protected boolean closesWithHud = false;
	protected boolean firstDraw = false;
	protected boolean closeable = true;
	protected boolean closed = false;
	protected int minWidth = 0;
	protected int minHeight = 0;
	protected int maxWidth = 0;
	protected int maxHeight = 0;
	public int objZLevel = 0;
	public long objectId = -1;
	protected String objectName = "noname";
	protected String hoverText = null;
	protected int hoverTextColor = 0xff00d1ff;
	public long mouseHoverTime = 0l;
	public long hoverRefTime = 0l;
	public StorageBox<Integer, Integer> oldMousePos = new StorageBox(0, 0);
	protected Stack<Object> guiHistory = new Stack();
	protected Deque<EventFocus> focusQueue = new ArrayDeque();
	public RenderItem itemRenderer;
	protected IWindowObject focusedObject, defaultFocusObject, focusLockObject, focusObjectOnClose;
	protected IWindowObject toFront, toBack;
	protected IWindowObject hoveringTextObject;
	protected IWindowObject escapeStopper;
	protected EArrayList<IWindowObject> guiObjects = new EArrayList();
	protected EArrayList<IWindowObject> objsToBeRemoved = new EArrayList();
	protected EArrayList<IWindowObject> objsToBeAdded = new EArrayList();
	protected EArrayList<String> aliases = new EArrayList();
	protected ObjectEventHandler eventHandler = new ObjectEventHandler(this);
	protected StorageBox<Integer, Integer> mousePos = new StorageBox(0, 0);
	protected ScreenLocation oldArea = ScreenLocation.out, resizingDir;
	protected ObjectModifyType modifyType = ObjectModifyType.None;
	protected StorageBoxHolder<Integer, Integer> originalButtonPositions = new StorageBoxHolder();
	protected EObjectGroup objectGroup;
	protected WindowHeader header;
	protected ScaledResolution res;
	protected String guiName = getClass().getSimpleName();
	protected boolean isSpawned = false;
	protected long initTime = 0l;
	protected EDimension preMaxDims = new EDimension();
	protected IWindowObject lastClickedObject = null;
	protected long lastClickTime = 0l;
	protected long doubleClickThreshold = 500l;
	
	protected EnhancedGui() { initTime = System.currentTimeMillis(); guiInstance = this; }
	protected EnhancedGui(int posX, int posY) { this(posX, posY, null); }
	protected EnhancedGui(GuiScreen oldGuiIn) {
		initTime = System.currentTimeMillis();
		guiInstance = this;
		if (oldGuiIn != null) {
			oldGui = oldGuiIn;
			if (oldGuiIn instanceof EnhancedGui) {
				guiHistory = ((EnhancedGui) oldGuiIn).getWindowHistory();
				guiHistory.push(oldGuiIn);
			}
		}
	}
	protected EnhancedGui(int posX, int posY, GuiScreen oldGuiIn) {
		initTime = System.currentTimeMillis();
		startX = posX;
		startY = posY;
		useCustomPosition = true;
		guiInstance = this;
		if (oldGuiIn != null) {
			if (oldGuiIn instanceof EnhancedGui) {
				guiHistory = ((EnhancedGui) oldGuiIn).getWindowHistory();
				guiHistory.push(oldGuiIn);
			}
		}
	}
	
	//----------------
	//Object Overrides
	//----------------
	
	@Override public String toString() { return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()); }
	
	//------------------------------
	//ITabCompleteListener Overrides
	//------------------------------
		
	@Override public void onTabCompletion(String[] result) {}
	@Override public void requestTabComplete(String in1, String in2) { EChatUtil.registerTabListener(this, in1, in2); }
	
	//-------------------
	//GuiScreen Overrides
	//-------------------
	
	@Override
	public void initGui() {
		res = new ScaledResolution(mc);
		sWidth = res.getScaledWidth();
		sHeight = res.getScaledHeight();
		itemRenderer = itemRender;
		startXPos = (sWidth / 2) - (width / 2);
		startYPos = (sHeight / 2) - (height / 2);
		startWidth = width;
		startHeight = height;
		if (!useCustomPosition) {
			startX = startXPos;
			startY = startYPos;
		}
		updatePosition();
		originalButtonPositions.allowDuplicates = true;
		
		header = new WindowHeader(this);
		header.setPersistent(true);
		addObject(null, header);
		header.updateButtonVisibility();
		
		initObjects();
		objectInit = true;
		
		hasBeenInitialized = true;
	}
	
	/**Place at end of initGui(). Only required if vanilla GuiButton is used.*/
	public void finishInit() {
		for (GuiButton b : buttonList) { originalButtonPositions.add(b.xPosition, b.yPosition); }
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn, float ticks) {
		drawObject(mXIn, mYIn);
	}
	
	//basic inputs
	@Override
	protected void mouseClicked(int mX, int mY, int button) throws IOException {
		IWindowObject underMouse = getHighestZObjectUnderMouse();
		
		if (underMouse != null) {
			if (focusedObject == this) {
				if (underMouse != this) { focusQueue.add(new EventFocus(this, underMouse, FocusType.MousePress, button, mX, mY)); }
				else { mousePressed(mX, mY, button); }
			}
			else if (focusedObject != null && focusedObject != this) {
				if (focusLockObject != null) {
					if (underMouse.equals(focusLockObject) || underMouse.isChild(focusLockObject) || underMouse instanceof WindowHeader) {
						focusQueue.add(new EventFocus(this, underMouse, FocusType.MousePress, button, mX, mY));
					}
					else { focusLockObject.drawFocusLockBorder(); }
				}
				else {
					if (underMouse.equals(focusedObject)) { focusedObject.mousePressed(mX, mY, button); }
					else { focusQueue.add(new EventFocus(this, underMouse, FocusType.MousePress, button, mX, mY)); }
				}
			}
			else { clearFocusedObject(); }
		}
		else {
			EnhancedMC.getRenderer().mousePressed(mX, mY, button);
		}
		super.mouseClicked(mX, mY, button);
	}
	protected void mouseUnclicked(int mX, int mY, int button) {
		if (modifyingObject != null && modifyType == ObjectModifyType.MoveAlreadyClicked) { modifyType = ObjectModifyType.None; modifyingObject = null; }
		if (modifyType == ObjectModifyType.Resize) { modifyType = ObjectModifyType.None; modifyingObject = null; }
		mouseReleased(mX, mY, button);
		EnhancedMC.getRenderer().mouseReleased(mX, mY, button);
		if (focusedObject != null && focusedObject != this) { focusedObject.mouseReleased(mX, mY, button); }
		super.mouseReleased(mX, mY, button);
	}
	
	@Override
	public void mouseClickMove(int mX, int mY, int button, long timeSinceLastClick) {
		mouseDragged(mX, mY, button, timeSinceLastClick);
		if (focusedObject != null) { focusedObject.mouseDragged(mX, mY, button, timeSinceLastClick); }
		super.mouseClickMove(mX, mY, button, timeSinceLastClick);
	}
	
	@Override protected void keyTyped(char typedChar, int keyCode) throws IOException { if (keyCode == 1) { closeGui(false); } }
	
	//basic input handlers
	@Override
	public void handleMouseInput() throws IOException {
		mX = (Mouse.getEventX() * sWidth / mc.displayWidth);
        mY = (sHeight - Mouse.getEventY() * sHeight / mc.displayHeight - 1);
        parseMousePosition(mX, mY);
        
		int button = Mouse.getEventButton();
        
        if (Mouse.hasWheel()) {
        	lastScrollChange = Integer.signum(Mouse.getEventDWheel());
        	if (lastScrollChange != 0) { mouseScrolled(lastScrollChange); }
        }
        
        if (Mouse.getEventButtonState()) {
        	lastMouseEvent = Minecraft.getSystemTime();
        	lastMouseButton = button;
        	mouseClicked = true;
        	leftClick = (button == 0);
        	rightClick = (button == 1);
        	middleClick = (button == 2);
            mouseClicked(mX, mY, lastMouseButton);
        }
        else if (button != -1) {
        	lastMouseButton = -1;
        	mouseClicked = false;
        	leftClick = false;
        	rightClick = false;
        	middleClick = false;
            mouseUnclicked(mX, mY, button);
        }
        else if (lastMouseButton != -1 && this.lastMouseEvent > 0L) {
        	timeSinceLastClick = Minecraft.getSystemTime() - lastMouseEvent;
            mouseClickMove(mX, mY, lastMouseButton, timeSinceLastClick);
        }
	}
	@Override
	public void handleKeyboardInput() throws IOException {
		if (Keyboard.getEventKeyState()) {
			keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
			if (focusedObject != null) {
				focusedObject.keyPressed(Keyboard.getEventCharacter(), Keyboard.getEventKey());
				EnhancedMC.getRenderer().keyPressed(Keyboard.getEventCharacter(), Keyboard.getEventKey());
			}
		} else {
			if (focusedObject != null) {
				focusedObject.keyReleased(Keyboard.getEventCharacter(), Keyboard.getEventKey());
				EnhancedMC.getRenderer().keyReleased(Keyboard.getEventCharacter(), Keyboard.getEventKey());
			}
		}
		mc.dispatchKeypresses();
	}
	
	//send chat
	@Override public void sendChatMessage(String msg) { sendChatMessage(msg, true); }
	@Override public void sendChatMessage(String msg, boolean addToChat) {
        if (addToChat) { mc.ingameGUI.getChatGUI().addToSentMessages(msg); }
        if (msg != null && msg.startsWith("/") && ClientCommandHandler.instance.executeCommand(mc.thePlayer, msg) != 0) { return; }
        EChatUtil.sendLongerChatMessage(msg);
    }
	
	//defaultBackground
	@Override
	public void drawDefaultBackground() {
		drawRect(startX, startY, endX, endY, 0xff000000);
		drawRect(startX + 1, startY + 1, endX - 1, endY - 1, 0xff383838);
		drawRect(startX + 2, startY + 2, endX - 2, endY - 2, 0xff3f3f3f);
		drawRect(startX + 3, startY + 3, endX - 3, endY - 3, 0xff424242);
	}
	
	//displayGuiScreen call
	@Override
	public void setWorldAndResolution(Minecraft mc, int width, int height) {
		this.mc = mc;
		itemRender = mc.getRenderItem();
		fontRendererObj = mc.fontRendererObj;
		sWidth = width;
		sHeight = height;
		if (!MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.InitGuiEvent.Pre(this, this.buttonList))) {
			buttonList.clear();
			guiObjects.clear();
			objsToBeAdded.clear();
			objsToBeRemoved.clear();
			toFront = null;
			toBack = null;
			clearFocusedObject();
			clearFocusLockObject();
			clearModifyingObject();
			setObjectRequestingFocus(null);
			initGui();
		}
		MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.InitGuiEvent.Post(this, this.buttonList));
	}
	
	//resize
	@Override
	public void onResize(Minecraft mcIn, int width, int height) {
		useCustomPosition = false;
		sWidth = width;
		sHeight = height;
		setPosition(startX, startY);
	}
	
	@Override public boolean doesGuiPauseGame() { return false; }
	
	//----------------------------
	//IEnhancedGuiObject Overrides
	//----------------------------
	
	//init
	@Override public boolean isInit() { return hasBeenInitialized; }
	@Override public boolean isObjectInit() { return objectInit; }
	@Override public void completeInit() { hasBeenInitialized = true; }
	@Override public void preReInit() {}
	@Override public void initObjects() {}
	@Override public void postReInit() {}
	@Override
	public void reInitObjects() {
		ITopParent p = getTopParent();
		EArrayList<IWindowObject> children = getAllChildren();
		if (!(p.getModifyType() == ObjectModifyType.Resize)) {
			if (children.contains(p.getFocusedObject())) { p.clearFocusedObject(); }
			if (children.contains(p.getFocusLockObject())) { p.clearFocusLockObject(); }
			if (children.contains(p.getModifyingObject())) { p.clearModifyingObject(); }
		}
		guiObjects.clear();
		
		initObjects();
	}
	@Override public void onAdded() {}
	
	//main draw
	/** Call this super to draw objects when overriding! */
	@Override
	public void drawObject(int mXIn, int mYIn) {
		updateBeforeNextDraw(mXIn, mYIn);
		if (checkDraw()) {
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			guiObjects.stream().filter(o -> o.checkDraw()).forEach(o -> {
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				if (!o.hasFirstDraw()) { o.onFirstDraw(); }
				o.drawObject(mX, mY);
				if (focusLockObject != null && !o.equals(focusLockObject)) {
					if (o.isVisible()) {
						EDimension d = o.getDimensions();
						drawRect(d.startX, d.startY, d.endX, d.endY, 0x77000000);
					}
				}
			});
			if (EnhancedMC.isDebugMode() && !mc.gameSettings.showDebugInfo) { drawDebugInfo(); }
			GlStateManager.popMatrix();
		}
	}
	@Override public void onFirstDraw() {}
	@Override public boolean hasFirstDraw() { return firstDraw; }
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
					//case bot: if (inside) { CursorHelper.setCursor(CursorHelper.createCursorFromResourceLocation(Resources.mouseResizeNS)); } break;
					case left:
					//case right: if (inside) { CursorHelper.setCursor(CursorHelper.createCursorFromResourceLocation(Resources.mouseResizeEW)); } break;
					case topRight:
					//case botLeft: if (inside) { CursorHelper.setCursor(CursorHelper.createCursorFromResourceLocation(Resources.mouseResizeDL)); } break;
					case topLeft:
					//case botRight: if (inside) { CursorHelper.setCursor(CursorHelper.createCursorFromResourceLocation(Resources.mouseResizeDR)); } break;
					default:
						//CursorHelper.setCursor(null);
					break;
					}
				}
				else {
					//CursorHelper.setCursor(null);
				}				
			}
		}
	}
	@Override public void onMouseHover(int mX, int mY) {}
	@Override public boolean isDrawingHover() { return false; }
	@Override public IWindowObject setHoverText(String textIn) { hoverText = textIn; return this; }
	@Override public IWindowObject setHoverTextColor(int colorIn) { hoverTextColor = colorIn; return this; }
	@Override public String getHoverText() { return hoverText; }
	
	//obj ids
	@Override public long getObjectID() { return objectId; }
	@Override public IWindowObject setObjectID(long idIn) { objectId = idIn; return this; }
	@Override public String getObjectName() { return objectName; }
	@Override public IWindowObject setObjectName(String nameIn) { objectName = nameIn; return this; }
	
	//drawing checks
	@Override public boolean checkDraw() { return persistent || visible; }
	@Override public boolean isEnabled() { return enabled; }
	@Override public boolean isVisible() { return visible; }
	@Override public boolean isHidden() { return hidden; }
	@Override public boolean isPersistent() { return persistent; }
	@Override public boolean isBoundaryEnforced() { return false; }
	@Override public boolean isResizing() { return getTopParent().getModifyingObject() == this && getTopParent().getModifyType() == ObjectModifyType.Resize; }
	@Override public boolean isMoving() { return getTopParent().getModifyingObject() == this && getTopParent().getModifyType() == ObjectModifyType.Move; }
	@Override public boolean isAlwaysOnTop() { return true; }
	@Override public IWindowObject setEnabled(boolean val) { enabled = val; return this; }
	@Override public IWindowObject setVisible(boolean val) { visible = val; return this; }
	@Override public IWindowObject setPersistent(boolean val) { return this; }
	@Override public IWindowObject setAlwaysOnTop(boolean val) { return this; }
	@Override public IWindowObject setHidden(boolean val) { hidden = val; return this; }
	
	//size
	@Override public boolean hasHeader() { return WindowObjectS.hasHeader(this); }
	@Override public boolean isResizeable() { return resizeable; }
	@Override public WindowHeader getHeader() { return WindowObjectS.getHeader(this); }
	@Override public int getMinWidth() { return minWidth; }
	@Override public int getMinHeight() { return minHeight; }
	@Override public int getMaxWidth() { return maxWidth; }
	@Override public int getMaxHeight() { return maxHeight; }
	@Override public IWindowObject setMinDims(int widthIn, int heightIn) { setMinWidth(widthIn).setMinHeight(heightIn); return this; }
	@Override public IWindowObject setMaxDims(int widthIn, int heightIn) { setMaxWidth(widthIn).setMaxHeight(heightIn); return this; }
	@Override public IWindowObject setMinWidth(int widthIn) { minWidth = widthIn; return this; }
	@Override public IWindowObject setMinHeight(int heightIn) { minHeight = heightIn; return this; }
	@Override public IWindowObject setMaxWidth(int widthIn) { maxWidth = widthIn; return this; }
	@Override public IWindowObject setMaxHeight(int heightIn) { maxHeight = heightIn; return this; }
	@Override public IWindowObject setResizeable(boolean val) { resizeable = val; return this; }
	@Override public IWindowObject resize(int xIn, int yIn, ScreenLocation areaIn) { WindowObjectS.resize(this, xIn, yIn, areaIn); return this; }
	
	//position
	@Override public void move(int newX, int newY) { WindowObjectS.move(this, newX, newY); }
	@Override public boolean isMoveable() { return moveable; }
	@Override public IWindowObject resetPosition() { setPosition(startXPos, startYPos); return this; }
	@Override
	public IWindowObject setPosition(int xIn, int yIn) {
		startX = xIn;
		startY = yIn;
		setWorldAndResolution(Minecraft.getMinecraft(), sWidth, sHeight);
		return this;
	}
	@Override public IWindowObject setMoveable(boolean val) { moveable = val; return this; }
	@Override public IWindowObject setDimensions(EDimension dimIn) { return setDimensions(dimIn.startX, dimIn.startY, dimIn.width, dimIn.height); }
	@Override public IWindowObject setDimensions(int widthIn, int heightIn) { return setDimensions(startX, startY, widthIn, heightIn); }
	@Override public IWindowObject setDimensions(int startXIn, int startYIn, int widthIn, int heightIn) {
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
	@Override public StorageBox<Integer, Integer> getInitialPosition() { return new StorageBox<Integer, Integer>(startXPos, startYPos); }
	@Override public IWindowObject setInitialPosition(int startXIn, int startYIn) { startXPos = startXIn; startYPos = startYIn; return this; }
	@Override
	public IWindowObject centerObjectWithSize(int widthIn, int heightIn) {
		ScaledResolution res = new ScaledResolution(mc);
		int sWidth = res.getScaledWidth();
		int sHeight = res.getScaledHeight();
		if (sWidth >= widthIn) {
			startX = (sWidth - widthIn) / 2;
			width = widthIn;
		}
		else {
			startX = 0;
			width = sWidth;
		};
		if (sHeight >= heightIn) {
			startY = (sHeight - heightIn) / 2;
			height = heightIn;
		}
		else {
			startY = 0;
			height = sHeight;
		}
		useCustomPosition = true;
		setCloseAndRecenter(true);
		return this;
	}
	@Override public EDimension getDimensions() { return new EDimension(startX, startY, endX, endY); }
	
	//objects
	@Override public boolean isChild(IWindowObject objIn) { return WindowObjectS.isChildOfObject(this, objIn); }
	@Override public IWindowObject addObject(IWindowObject obj, IWindowObject... additional) { WindowObjectS.addObject(this, obj, additional); return this; }
	@Override public IWindowObject removeObject(IWindowObject obj, IWindowObject... additional) { objsToBeRemoved.addA(EUtil.add(obj, additional)); return this; }
	@Override public EObjectGroup getObjectGroup() { return objectGroup; }
	@Override public IWindowObject setObjectGroup(EObjectGroup groupIn) { objectGroup = groupIn; return this; }
	@Override public void onGroupNotification(ObjectEvent e) {}
	@Override public EArrayList<IWindowObject> getObjects() { return guiObjects; }
	@Override public EArrayList<IWindowObject> getAddingObjects() { return objsToBeAdded; }
	@Override public EArrayList<IWindowObject> getRemovingObjects() { return objsToBeRemoved; }
	@Override public EArrayList<IWindowObject> getAllChildren() { return WindowObjectS.getAllChildren(this); }
	@Override public EArrayList<IWindowObject> getAllChildrenUnderMouse() { return WindowObjectS.getAllChildrenUnderMouse(this, mX, mY); }
	@Override public boolean containsObject(IWindowObject object) { return getCombinedChildren().contains(object); }
	@Override public <T> boolean containsObject(Class<T> objIn) { return objIn != null ? getAllChildren().stream().anyMatch(o -> objIn.isInstance(o)) : false; }
	@Override public EArrayList<IWindowObject> getCombinedChildren() { return EArrayList.combineLists(guiObjects, objsToBeAdded); }
	
	//parents
	@Override public IWindowObject getParent() { return parent; }
	@Override public IWindowObject setParent(IWindowObject parentIn) { parent = parentIn; return this; }
	@Override public ITopParent getTopParent() { return WindowObjectS.getTopParent(this); }
	@Override public IWindowParent getWindowParent() { return WindowObjectS.getWindowParent(this); }
	
	//zLevel
	@Override public int getZLevel() { return objZLevel; }
	@Override public IWindowObject setZLevel(int zLevelIn) { objZLevel = zLevelIn; return this; }
	@Override public IWindowObject bringToFront() { getTopParent().bringObjectToFront(this); return this; }
	@Override public IWindowObject sendToBack() { getTopParent().sendObjectToBack(this); return this; }
	
	//focus
	@Override public boolean hasFocus() { return this.equals(this.getFocusedObject()); }
	@Override public boolean relinquishFocus() {
		if (doesFocusLockExist() && getFocusLockObject().equals(this)) { clearFocusLockObject(); }
		else if (hasFocus()) {
			System.out.println("ive got focus");
			if (!getTopParent().equals(this)) { getTopParent().setObjectRequestingFocus(getTopParent()); return true; }
		}
		return false;
	}
	@Override
	public void onFocusGained(EventFocus eventIn) {
		postEvent(new EventFocus(this, this, FocusType.Gained));
		if (eventIn.getFocusType().equals(FocusType.MousePress)) { mousePressed(eventIn.getMX(), eventIn.getMY(), eventIn.getActionCode()); }
		if (defaultFocusObject != null) { defaultFocusObject.requestFocus(); }
	}
	@Override public void onFocusLost(EventFocus eventIn) { postEvent(new EventFocus(this, this, FocusType.Lost)); }
	@Override
	public void transferFocus(IWindowObject objIn) {
		if (!doesFocusLockExist() && objIn != null) {
			relinquishFocus();
			getTopParent().setObjectRequestingFocus(objIn);
		}
	}
	@Override
	public void drawFocusLockBorder() {
		if (checkDraw() && guiObjects.containsNoInstanceOf(FocusLockBorder.class)) {
			if (hasHeader() && getHeader().isEnabled()) {
				addObject(null, new FocusLockBorder(this, getHeader().startX, getHeader().startY, width, height + getHeader().height));
			}
			else { addObject(null, new FocusLockBorder(this)); }
		}
	}
	@Override
	public IWindowObject requestFocus() {
		if (!hasFocus()) {
			if (doesFocusLockExist()) {
				//getTopParent().setObjectRequestingFocus(this);
			}
			else { getTopParent().setObjectRequestingFocus(this); }
		}
		return this;
	}
	@Override public IWindowObject getDefaultFocusObject() { return defaultFocusObject; }
	@Override public IWindowObject setDefaultFocusObject(IWindowObject objIn) { defaultFocusObject = objIn; return this; }
	
	//mouse checks
	@Override public void mouseEntered(int mX, int mY) { postEvent(new EventMouse(this, mX, mY, -1, MouseType.Entered)); }
	@Override public void mouseExited(int mX, int mY) { postEvent(new EventMouse(this, mX, mY, -1, MouseType.Exited)); }
	@Override
	public boolean isMouseInside(int mX, int mY) {
		if (isBoundaryEnforced()) {
			EDimension b = boundaryDimension;
			return mX >= startX && mX >= b.startX && mX <= endX && mX <= b.endX && mY >= startY && mY >= b.startY && mY <= endY && mY <= b.endY;
		}
		return mX >= startX && mX <= endX && mY >= startY && mY <= endY;
	}
	@Override public boolean isMouseOver(int mX, int mY) { return isMouseInside(mX, mY) && this.equals(getTopParent().getHighestZObjectUnderMouse()); }
	@Override public IWindowObject setBoundaryEnforcer(EDimension dimIn) { boundaryDimension = new EDimension(dimIn); return this; }
	@Override public EDimension getBoundaryEnforcer() { return boundaryDimension; }
	@Override public boolean isClickable() { return clickable; }
	@Override public IWindowObject setClickable(boolean valIn) { clickable = valIn; return this; }
	@Override public IWindowObject setEntiretyClickable(boolean val) { WindowObjectS.setEntiretyClickable(this, val); return this; }
	
	//basic inputs
	@Override public void parseMousePosition(int mX, int mY) { guiObjects.stream().filter(o -> o.isMouseInside(mX, mY)).forEach(o -> o.parseMousePosition(mX, mY)); }
	@Override public void mousePressed(int mX, int mY, int button) { WindowObjectS.mousePressed(this, mX, mY, button); }
	@Override public void mouseReleased(int mX, int mY, int button) { WindowObjectS.mouseReleased(this, mX, mY, button); }
	@Override public void mouseDragged(int mX, int mY, int button, long timeSinceLastClick) {}
	@Override
	public void mouseScrolled(int change) {
		postEvent(new EventMouse(this, mX, mY, -1, MouseType.Scrolled));
		if (isMouseInsideObject(mX, mY)) {
			getAllChildrenUnderMouse().stream().filter(o -> o.isMouseInside(mX, mY) && o.checkDraw()).forEach(o -> o.mouseScrolled(change));
		}
		else { EnhancedMC.getRenderer().mouseScrolled(change); }
	}
	@Override public void keyPressed(char typedChar, int keyCode) { postEvent(new EventKeyboard(this, typedChar, keyCode, KeyboardType.Pressed)); }
	@Override public void keyReleased(char typedChar, int keyCode) { postEvent(new EventKeyboard(this, typedChar, keyCode, KeyboardType.Released)); }
	
	//events
	@Override public void sendArgs(Object... args) {}
	@Override public ObjectEventHandler getEventHandler() { return eventHandler; }
	@Override public IWindowObject registerListener(IWindowObject objIn) { if (eventHandler != null) { eventHandler.registerObject(objIn); } return this; }
	@Override public IWindowObject unregisterListener(IWindowObject objIn) { if (eventHandler != null) { eventHandler.unregisterObject(objIn); } return this; }
	@Override public IWindowObject postEvent(ObjectEvent e) { if (eventHandler != null) { eventHandler.processEvent(e); } return this; }
	@Override public void onEvent(ObjectEvent e) {}
	
	//action object
	@Override public void actionPerformed(IActionObject object, Object... args) { postEvent(new EventAction(this, object, args)); }
	
	//close object
	@Override public boolean isCloseable() { return closeable; }
	@Override public boolean isClosed() { return closed; }
	@Override public IWindowObject setCloseable(boolean val) { closeable = val; return this; }
	@Override public void close() { if (closeable) { postEvent(new EventObjects(this, this, ObjectEventType.Close)); closeGui(true); closed = true; } }
	@Override public void close(boolean recursive) { close(); }
	@Override public boolean closesWithHud() { return closesWithHud; }
	@Override public IWindowObject setClosesWithHud(boolean val) { closesWithHud = val; return this; }
	@Override public void onClosed() {}
	@Override public IWindowObject setFocusedObjectOnClose(IWindowObject objIn) { focusObjectOnClose = objIn; return this; }
	@Override public void setBeingRemoved() {}
	@Override public boolean isBeingRemoved() { return false; }
	
	//-------------------------
	//IWindowParent Overrides
	//-------------------------
	
	@Override public boolean isPinned() { return pinned; }
	@Override public boolean isMaximized() { return maximized == ScreenLocation.center; }
	@Override public boolean isPinnable() { return pinnable; }
	@Override public boolean isMaximizable() { return maximizable; }
	@Override public IWindowParent setPinned(boolean val) { pinned = val; return this; }
	@Override public IWindowParent setMaximized(ScreenLocation position) { maximized = position; return this; }
	@Override public ScreenLocation getMaximizedPosition() { return maximized; }
	@Override public IWindowParent setPinnable(boolean val) { pinnable = val; return this; }
	@Override public IWindowParent setMaximizable(boolean val) { maximizable = val; return this; }
	
	@Override public void maximize() {}
	@Override public void miniaturize() {}
	
	@Override public EDimension getPreMax() { return preMaxDims; }
	@Override public IWindowParent setPreMax(EDimension dimIn) { preMaxDims = new EDimension(dimIn); return this; }
	
	@Override public boolean isOpWindow() { return false; }
	@Override public boolean isDebugWindow() { return false; }
	
	@Override public Stack<Object> getWindowHistory() { return guiHistory; }
	@Override
	public IWindowParent setWindowHistory(Stack<Object> historyIn) {
		guiHistory = historyIn;
		if (header != null) { header.updateButtonVisibility(); }
		return this;
	}
	
	@Override public long getInitTime() { return initTime; }
	
	@Override public EArrayList<String> getAliases() { return aliases; }
	
	//-------------------------
	//IEnhancedTopGui Overrides
	//-------------------------
	
	//drawing
	@Override
	public void drawDebugInfo() {
		drawStringWithShadow("TopParent: " + getTopParent(), 3, 72, 0xffffff);
		if (focusedObject instanceof WindowButton) {
			drawStringWithShadow("FocuedObject: " + (((WindowButton) focusedObject).getString().isEmpty() ? focusedObject : "EGuiButton: " +
													((WindowButton) focusedObject).getString()), 3, 82, 0xffffff);
		}
		else { drawStringWithShadow("FocuedObject: " + focusedObject, 3, 82, 0xffffff); }
		drawStringWithShadow("GuiHistory: " + guiHistory, 3, 92, 0xffffff);
		drawStringWithShadow("ModifyingObject & type: (" + modifyingObject + " : " + modifyType + ")", 3, 102, 0xffffff);
		drawStringWithShadow("Object under mouse: " + getHighestZObjectUnderMouse(), 3, 112, 0xffbb00);
	}
	
	//draw order
	@Override public ITopParent bringObjectToFront(IWindowObject objIn) { toFront = objIn; return this; }
	@Override public ITopParent sendObjectToBack(IWindowObject objIn) { toBack = objIn; return this; }
	
	//hovering text
	@Override public ITopParent setHoveringObject(IWindowObject objIn) { hoveringTextObject = objIn; return this; }
	@Override public IWindowObject getHoveringObject() { return hoveringTextObject; }
	
	//double click
	@Override public ITopParent setLastClickedObject(IWindowObject objectIn) { lastClickedObject = objectIn; return this; }
	@Override public IWindowObject getLastClickedObject() { return lastClickedObject; }
	@Override public ITopParent setLastClickTime(long timeIn) { lastClickTime = timeIn; return this; }
	@Override public long getLastClickTime() { return lastClickTime; }
	
	//objects
	@Override public IWindowObject getHighestZLevelObject() { return StaticTopParent.getHighestZLevelObject(this); }
	@Override public ITopParent removeUnpinnedObjects() { return StaticTopParent.removeUnpinnedWindows(this); }
	@Override public boolean hasPinnedObjects() { return StaticTopParent.hasPinnedWindows(this); }
	
	//focus
	@Override public IWindowObject getFocusedObject() { return focusedObject; }
	@Override public ITopParent setFocusedObject(IWindowObject objIn) { focusedObject = objIn; return this; }
	@Override public ITopParent setObjectRequestingFocus(IWindowObject objIn) { focusQueue.add(new EventFocus(this, objIn, FocusType.Transfer)); return this; }
	@Override public IWindowObject getFocusLockObject() { return focusLockObject; }
	@Override public ITopParent setFocusLockObject(IWindowObject objIn) {
		focusLockObject = objIn;
		transferFocus(focusLockObject);
		return this;
	}
	@Override public ITopParent clearFocusLockObject() { focusLockObject = null; return this; }
	@Override public boolean doesFocusLockExist() { return focusLockObject != null; }
	@Override
	public void clearFocusedObject() {
		if (focusedObject != null && focusedObject != this) { focusedObject.relinquishFocus(); }
		if (parent == null || parent == this) { focusedObject = this; }
		else { transferFocus(parent); }
	}
	@Override
	public void updateFocus() {
		if (modifyingObject != null && !modifyingObject.isResizeable() && modifyType.equals(ObjectModifyType.Resize)) { modifyType = ObjectModifyType.None; }
		EArrayList<IWindowObject> children = getAllChildren();
		if (!children.contains(focusedObject)) { clearFocusedObject(); }
		if (!children.contains(focusLockObject)) { clearFocusLockObject(); }
		if (!children.contains(defaultFocusObject)) { defaultFocusObject = null; }
		
		if (!focusQueue.isEmpty()) {
			EventFocus event = focusQueue.pop();
			if (event.getFocusObject() != null) {
				IWindowObject obj = event.getFocusObject();
				if (doesFocusLockExist()) {
					if (obj.equals(focusLockObject) || obj.isChild(focusLockObject) || obj instanceof WindowHeader) {
						focusedObject.onFocusLost(event);
						focusedObject = obj;
						focusedObject.onFocusGained(event);
					}
					else if (focusedObject != focusLockObject) {
						focusedObject.onFocusLost(new EventFocus(this, focusLockObject, FocusType.DefaultFocusObject));
						focusedObject = focusLockObject;
						focusedObject.onFocusGained(new EventFocus(this, focusLockObject, FocusType.DefaultFocusObject));
					}
				}
				else {
					if (focusedObject != null) {
						if (obj != focusedObject) {
							focusedObject.onFocusLost(event);
							focusedObject = obj;
							focusedObject.onFocusGained(event);
						}
					}
					else {
						if (focusedObject != null && obj != this) {
							focusedObject.onFocusLost(event);
							focusedObject = obj;
							focusedObject.onFocusGained(event);
						}
						else {
							if (defaultFocusObject != null) {
								focusedObject = defaultFocusObject;
								focusedObject.onFocusGained(event);
							}
							else {
								focusedObject = this;
								focusedObject.onFocusGained(event);
							}
						}
					}
					
				}
			}
		}
		else if (focusedObject == null) {
			focusedObject = this;
			focusedObject.onFocusGained(new EventFocus(this, this, FocusType.DefaultFocusObject));
		}
	}
	
	//object modification
	@Override public ObjectModifyType getModifyType() { return modifyType; }
	@Override public ITopParent setModifyingObject(IWindowObject objIn, ObjectModifyType typeIn) { modifyingObject = objIn; modifyType = typeIn; return this; }
	@Override public ITopParent setResizingDir(ScreenLocation areaIn) { resizingDir = areaIn; return this; }
	@Override public ITopParent setModifyMousePos(int mX, int mY) { mousePos.setValues(mX, mY); return this; }
	@Override public IWindowObject getModifyingObject() { return modifyingObject; }
	@Override public ITopParent clearModifyingObject() { modifyingObject = null; modifyType = ObjectModifyType.None; return this; }
	
	//mouse checks
	@Override public boolean isMouseOnObjEdge(int mX, int mY) { return checkDraw() && !getEdgeAreaMouseIsOn().equals(ScreenLocation.out); }
	@Override public ScreenLocation getEdgeAreaMouseIsOn() { return WindowObjectS.getEdgeAreaMouseIsOn(this, mX, mY); }
	@Override public boolean isMouseInsideObject(int mX, int mY) { return getHighestZObjectUnderMouse() != null; }
	@Override
	public boolean isMouseInsideHeader(int mX, int mY) {
		EArrayList<IWindowObject> objects = getAllChildrenUnderMouse();
		boolean under = false;
		for (IWindowObject o : objects) {
			if (o instanceof WindowHeader) { under = true; }
		}
		return under;
	}
	@Override
	public IWindowObject getHighestZObjectUnderMouse() {
		try {
			EArrayList<IWindowObject> underMouse = getAllObjectsUnderMouse();
			StorageBoxHolder<IWindowObject, EArrayList<IWindowObject>> sortedByParent = new StorageBoxHolder();
			
			//first setup the sorted list
			for (int i = guiObjects.size() - 1; i >= 0; i--) {
				sortedByParent.add(guiObjects.get(i), new EArrayList());
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
	@Override
	public EArrayList<IWindowObject> getAllObjectsUnderMouse() {
		EArrayList<IWindowObject> underMouse = new EArrayList();
		for (IWindowObject o : getAllChildren()) {
			if (o.isMouseInside(mX, mY) && o.isVisible() && o.isClickable()) { underMouse.add(o); }
		}
		return underMouse;
	}
	
	//close
	public void closeGui(boolean fullClose) {
		if (fullClose) {
			mc.displayGuiScreen(null);
			if (mc.currentScreen == null) { mc.setIngameFocus(); }
		}
		else if (backwardsTraverseable && !guiHistory.isEmpty() && guiHistory.peek() != null) {
			try {
				Object oldGuiPass = guiHistory.pop();
				if (oldGuiPass instanceof WindowParent) {
					WindowParent newGui = ((WindowParent) Class.forName(oldGuiPass.getClass().getName()).getConstructor().newInstance());
					newGui.setWindowHistory(((WindowParent) oldGuiPass).getWindowHistory());
					EnhancedMC.displayWindow(newGui, this, CenterType.object);
				}
				else if (oldGuiPass instanceof GuiScreen) {
					try {
						GuiScreen newGui = ((GuiScreen) Class.forName(oldGuiPass.getClass().getName()).getConstructor().newInstance());
						mc.displayGuiScreen(newGui);
						return;
					}
					catch (Exception e) { e.printStackTrace(); }
				}
				return;
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		else {
			mc.displayGuiScreen(null);
			if (mc.currentScreen == null) { mc.setIngameFocus(); }
		}
	}
	public EnhancedGui setCloseAndRecenter(boolean val) { closeAndRecenter = val; return this; }
	@Override public EnhancedGui setEscapeStopper(IWindowObject obj) { if (obj != this) { escapeStopper = obj; } return this; }
	@Override public IWindowObject getEscapeStopper() { return escapeStopper; }
	
	//------------------------
	//IRendererProxy Overrides
	//------------------------
	
	@Override public int getMX() { return mX; }
	@Override public int getMY() { return mY; }
	
	//-------------------
	//EnhancedGui methods
	//-------------------
	
	protected void updateBeforeNextDraw(int mXIn, int mYIn) {
		if (eventHandler != null) { eventHandler.processEvent(new EventRedraw(this)); }
		res = new ScaledResolution(mc);
		mX = mXIn; mY = mYIn;
		
		//handle cursor stuff for highest obj
		if (CoreApp.customCursors.get()) {
			if (getHighestZObjectUnderMouse() != null) { getHighestZObjectUnderMouse().updateCursorImage(); }
			else { updateCursorImage(); }
		}
		if (!CursorHelper.isNormalCursor() && getHighestZObjectUnderMouse() == null && modifyType != ObjectModifyType.Resize) { CursorHelper.reset(); }
		
		checkMouseHover();
		oldMousePos.setValues(mXIn, mYIn);
		if (!mouseEntered && isMouseOver(mX, mY)) { mouseEntered = true; mouseEntered(mX, mY); }
		if (mouseEntered && !isMouseOver(mX, mY)) { mouseEntered = false; mouseExited(mX, mY); }
		if (!objsToBeRemoved.isEmpty()) { WindowObjectS.removeObjects(this, objsToBeRemoved); }
		if (!objsToBeAdded.isEmpty()) { WindowObjectS.addObjects(this, objsToBeAdded); }
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
			if (oldMousePos == null) { oldMousePos = new StorageBox<Integer, Integer>(0, 0); }
			if (mX == oldMousePos.getObject() && mY == oldMousePos.getValue()) {
				mouseHoverTime = (System.currentTimeMillis() - hoverRefTime);
				if (mouseHoverTime >= 1000) {
					getTopParent().setHoveringObject(getTopParent().getHighestZObjectUnderMouse());
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
				EArrayList<IWindowObject> objects = new EArrayList();
				guiObjects.remove(toFront);
				objects.addAll(guiObjects);
				guiObjects.clear();
				guiObjects.add(toFront);
				guiObjects.addAll(objects);
			}
			toBack = null;
		}
	}
	
	public EnhancedGui enableHeader(boolean val) {
		header.setEnabled(val);
		return this;
	}
	
	protected EnhancedGui updatePosition() {
		midX = startX + width / 2;
		midY = startY + height /2;
		endX = startX + width;
		endY = startY + height;
		return this;
	}
	
	public void drawMenuGradient() { drawGradientRect(0, 0, sWidth, sHeight, -1072689136, -804253680); }
	
	protected int drawString(String text, int x, int y, int color) { return GLObject.drawString(text, x, y, color); }
    protected int drawCenteredString(String text, int x, int y, int color) { return GLObject.drawString(text, x, y, color); }
	protected int drawStringWithShadow(String text, int x, int y, int color) { return GLObject.drawStringWithShadow(text, x, y, color); }
	protected int drawCenteredStringWithShadow(String text, int x, int y, int color) { return GLObject.drawStringWithShadow(text, x, y, color); }
	
	public static void drawCustomSizedTexture(int x, int y, double u, double v, double width, double height, double textureWidth, double textureHeight) {
        double f = 1.0 / textureWidth;
        double f1 = 1.0 / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0.0D).tex(u * f, (v + height) * f1).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0D).tex((u + width) * f, (v + height) * f1).endVertex();
        worldrenderer.pos(x + width, y, 0.0D).tex((u + width) * f, v * f1).endVertex();
        worldrenderer.pos(x, y, 0.0D).tex(u * f, v * f1).endVertex();
        tessellator.draw();
    }
	
	public EnhancedGui setGuiWidthAndHeight(int widthIn, int heightIn) { width = widthIn; height = heightIn; updatePosition(); return this; }
	public EnhancedGui setGuiName(String nameIn) { guiName = nameIn; return this; }
	public String getGuiName() { return guiName; }
	
}
