package com.Whodundid.core.enhancedGui.types;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.debug.DebugFunctions;
import com.Whodundid.core.enhancedGui.StaticEGuiObject;
import com.Whodundid.core.enhancedGui.guiObjectUtil.EObjectGroup;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiFocusLockBorder;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventAction;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventFocus;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventKeyboard;
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
import com.Whodundid.core.enhancedGui.guiUtil.exceptions.ObjectInitException;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedTopParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.util.chatUtil.EChatUtil;
import com.Whodundid.core.util.miscUtil.EFontRenderer;
import com.Whodundid.core.util.miscUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
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

//Jan 4, 2019
//Jan 21, 2019
//Jan 24, 2019 : added objsToBeAdded
//Jan 25, 2019 : fixed logic behind adding objects with drawing delays, implemented fix for adding objects when moving
//Jan 27, 2019 : removed gWidth/height -> width/height, shadowed GuiScreen width/height with sWidth/height
//Last edited: Mar 26, 2019
//Edit note: implemented listeners
//First Added: Sep 7, 2018
//Author: Hunter Bragg

public abstract class EnhancedGui extends GuiScreen implements IEnhancedTopParent, IWindowParent {
	
	public static final Set<String> PROTOCOLS = Sets.newHashSet(new String[] {"http", "https"});
	public static final Splitter NEWLINE_SPLITTER = Splitter.on('\n');
	public EnhancedGui guiInstance;
	public GuiScreen oldGui;
	protected IEnhancedGuiObject parent, modifyingObject;
	public URI clickedLinkURI;
	protected EGuiFocusLockBorder border;
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
	protected boolean enabled = true;
	protected boolean visible = true;
	public boolean isMouseHover = false;
	public boolean mouseEntered = false;
	protected boolean resizeable = false;
	protected boolean positionLocked = false;
	protected boolean persistent = false;
	protected boolean closeAndRecenter = false;
	protected int minWidth = 0;
	protected int minHeight = 0;
	protected int maxWidth = 0;
	protected int maxHeight = 0;
	public int objZLevel = 0;
	public int objectId = -1;
	protected String objectName = "noname";
	public boolean drawHoverText = false;
	public String hoverText = "";
	public long mouseHoverTime = 0l;
	public long hoverRefTime = 0l;
	public StorageBox<Integer, Integer> oldMousePos = new StorageBox(0, 0);
	protected Stack<EnhancedGui> guiHistory = new Stack();
	protected Deque<EventFocus> focusQueue = new ArrayDeque();
	public EFontRenderer fontRenderer;
	public RenderItem itemRenderer;
	protected IEnhancedGuiObject focusedObject, defaultFocusObject, focusLockObject, focusObjectOnClose;
	protected IEnhancedGuiObject toFront, toBack;
	protected IEnhancedGuiObject hoveringTextObject;
	protected EArrayList<IEnhancedGuiObject> guiObjects = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> objsToBeRemoved = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> objsToBeAdded = new EArrayList();
	protected ObjectEventHandler eventHandler = new ObjectEventHandler(this);
	protected StorageBox<Integer, Integer> mousePos = new StorageBox(0, 0);
	protected ScreenLocation oldArea = ScreenLocation.out, resizingDir;
	protected ObjectModifyType modifyType = ObjectModifyType.None;
	protected StorageBoxHolder<Integer, Integer> originalButtonPositions = new StorageBoxHolder();
	protected EObjectGroup objectGroup;
	protected EGuiHeader header;
	protected ScaledResolution res;
	protected String guiName = getClass().getSimpleName();
	protected boolean isSpawned = false;
	
	protected EnhancedGui() { guiInstance = this; }
	protected EnhancedGui(int posX, int posY) { this(posX, posY, null); }
	protected EnhancedGui(GuiScreen oldGuiIn) {
		guiInstance = this;
		if (oldGuiIn != null) {
			oldGui = oldGuiIn;
			if (oldGuiIn instanceof EnhancedGui) {
				guiHistory = ((EnhancedGui) oldGuiIn).getGuiHistory();
				guiHistory.push((EnhancedGui) oldGuiIn);
			}
		}
	}
	protected EnhancedGui(int posX, int posY, GuiScreen oldGuiIn) {
		startX = posX;
		startY = posY;
		useCustomPosition = true;
		guiInstance = this;
		if (oldGuiIn != null) {
			if (oldGuiIn instanceof EnhancedGui) {
				guiHistory = ((EnhancedGui) oldGuiIn).getGuiHistory();
				guiHistory.push((EnhancedGui) oldGuiIn);
			}
		}
	}
	
	//----------------
	//Object Overrides
	//----------------
	
	@Override public String toString() { return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()); }
	
	//-------------------
	//GuiScreen Overrides
	//-------------------
	
	@Override
	public void initGui() {
		res = new ScaledResolution(mc);
		sWidth = res.getScaledWidth();
		sHeight = res.getScaledHeight();
		fontRenderer = EnhancedMC.getFontRenderer();
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
		
		header = new EGuiHeader(this);
		header.setPersistent(true);
		addObject(header);
		header.updateFileUpButtonVisibility();
		
		try {
			initObjects();
		} catch (ObjectInitException e) { e.printStackTrace(); }
		hasBeenInitialized = true;
	}
	
	/**Place at end of initGui(). Only required if vanilla GuiButton is used.*/
	public void finishInit() {
		for (GuiButton b : buttonList) { originalButtonPositions.add(b.xPosition, b.yPosition); }
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn, float ticks) {
		updateBeforeNextDraw(mXIn, mYIn);
		if (checkDraw()) {
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			drawObject(mXIn, mYIn, ticks);
			GlStateManager.popMatrix();
			if (EnhancedMC.isDebugMode() && !mc.gameSettings.showDebugInfo) {
				drawStringWithShadow("TopParent: " + getTopParent(), 3, 52, 0xffffff);
				if (focusedObject instanceof EGuiButton) {
					drawStringWithShadow("FocuedObject: " + (((EGuiButton) focusedObject).getDisplayString().isEmpty() ? focusedObject : "EGuiButton: " +
															((EGuiButton) focusedObject).getDisplayString()), 3, 62, 0xffffff);
				}
				else { drawStringWithShadow("FocuedObject: " + focusedObject, 3, 62, 0xffffff); }
				drawStringWithShadow("GuiHistory: " + guiHistory, 3, 72, 0xffffff);
				drawStringWithShadow("ModifyingObject & type: (" + modifyingObject + " : " + modifyType + ")", 3, 82, 0xffffff);
				drawStringWithShadow("Object under mouse: " + getHighestZObjectUnderMouse(), 3, 92, 0xffbb00);
			}
		}
	}
	
	//basic inputs
	@Override
	protected void mouseClicked(int mX, int mY, int button) throws IOException {
		IEnhancedGuiObject underMouse = getHighestZObjectUnderMouse();
		
		if (underMouse != null) {
			if (focusedObject == this) {
				if (underMouse != this) { focusQueue.add(new EventFocus(this, underMouse, FocusType.MousePress, button, mX, mY)); }
				else { mousePressed(mX, mY, button); }
			}
			else if (focusedObject != null && focusedObject != this) {
				if (focusLockObject != null) {
					if (underMouse.equals(focusLockObject) || underMouse.isChildOfObject(focusLockObject) || underMouse instanceof EGuiHeader) {
						focusQueue.add(new EventFocus(this, underMouse, FocusType.MousePress, button, mX, mY));
					} else { focusLockObject.drawFocusLockBorder(); }
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
		if (isResizing()) { modifyType = ObjectModifyType.None; modifyingObject = null; }
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
        } else if (button != -1) {
        	lastMouseButton = -1;
        	mouseClicked = false;
        	leftClick = false;
        	rightClick = false;
        	middleClick = false;
            mouseUnclicked(mX, mY, button);
        } else if (lastMouseButton != -1 && this.lastMouseEvent > 0L) {
        	timeSinceLastClick = Minecraft.getSystemTime() - lastMouseEvent;
            mouseClickMove(mX, mY, lastMouseButton, timeSinceLastClick);
        }
	}
	@Override
	public void handleKeyboardInput() throws IOException {
		if (Keyboard.getEventKeyState()) {
			if (Keyboard.getEventKey() == EnhancedMC.debugCommand.getKeyCode()) {
				DebugFunctions.runDebugFunction(0);
			}
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
		fontRenderer = EnhancedMC.getFontRenderer();
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
	@Override public EnhancedGui completeInit() { hasBeenInitialized = true; return this; }
	@Override public void initObjects() throws ObjectInitException {}
	@Override
	public void reInitObjects() throws ObjectInitException {
		IEnhancedTopParent p = getTopParent();
		EArrayList<IEnhancedGuiObject> children = getAllChildren();
		if (!p.isResizing()) {
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
	public void drawObject(int mXIn, int mYIn, float ticks) {
		try {
			if (checkDraw()) {
				GlStateManager.pushMatrix();
				GlStateManager.enableBlend();
				//EArrayList<IEnhancedGuiObject> instanceObjects = new EArrayList(guiObjects);
				guiObjects.stream().filter(o -> o.checkDraw()).forEach(o -> { GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F); o.drawObject(mX, mY, ticks); });
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
				} else {
					//CursorHelper.setCursor(null);
				}				
			}
		}
	}
	@Override public EnhancedGui setDrawHoverText(boolean val) { drawHoverText = val; return this; }
	@Override public boolean drawsHoverText() { return drawHoverText; }
	@Override public EnhancedGui setHoverText(String textIn) { hoverText = textIn; return this; }
	@Override public String getHoverText() { return hoverText; }
	
	//obj ids
	@Override public int getObjectID() { return objectId; }
	@Override public EnhancedGui setObjectID(int idIn) { objectId = idIn; return this; }
	@Override public String getObjectName() { return objectName; }
	@Override public EnhancedGui setObjectName(String nameIn) { objectName = nameIn; return this; }
	
	//drawing checks
	@Override public boolean checkDraw() { return persistent || visible; }
	@Override public boolean isEnabled() { return enabled; }
	@Override public boolean isVisible() { return visible; }
	@Override public boolean isPersistent() { return persistent; }
	@Override public boolean isBoundaryEnforced() { return false; }
	@Override public EnhancedGui setEnabled(boolean val) { enabled = val; return this; }
	@Override public EnhancedGui setVisible(boolean val) { visible = val; return this; }
	@Override public EnhancedGui setPersistent(boolean val) { return this; }
	@Override public EnhancedGui setBoundaryEnforcer(EDimension dimIn) { return this; }
	@Override public EDimension getBoundaryEnforcer() { return getDimensions(); }
	
	//size
	@Override public boolean hasHeader() { for (IEnhancedGuiObject o : guiObjects) { if (o instanceof EGuiHeader) { return true; } } return false; }
	@Override public boolean isResizeable() { return resizeable; }
	@Override public EGuiHeader getHeader() { for (IEnhancedGuiObject o : guiObjects) { if (o instanceof EGuiHeader) { return (EGuiHeader) o; } } return null; }
	@Override public int getMinimumWidth() { return minWidth; }
	@Override public int getMinimumHeight() { return minHeight; }
	@Override public int getMaximumWidth() { return maxWidth; }
	@Override public int getMaximumHeight() { return maxHeight; }
	@Override public EnhancedGui setMinimumWidth(int widthIn) { minWidth = widthIn; return this; }
	@Override public EnhancedGui setMinimumHeight(int heightIn) { minHeight = heightIn; return this; }
	@Override public EnhancedGui setMaximumWidth(int widthIn) { maxWidth = widthIn; return this; }
	@Override public EnhancedGui setMaximumHeight(int heightIn) { maxHeight = heightIn; return this; }
	@Override public EnhancedGui setResizeable(boolean val) { resizeable = val; return this; }
	@Override public EnhancedGui resize(int xIn, int yIn, ScreenLocation areaIn) { StaticEGuiObject.resize(this, xIn, yIn, areaIn); return this; }
	
	//position
	@Override public void move(int newX, int newY) { StaticEGuiObject.move(this, newX, newY); }
	@Override public boolean isPositionLocked() { return positionLocked; }
	@Override public EnhancedGui resetPosition() { setPosition(startXPos, startYPos); return this; }
	@Override
	public EnhancedGui setPosition(int xIn, int yIn) {
		startX = xIn;
		startY = yIn;
		setWorldAndResolution(Minecraft.getMinecraft(), sWidth, sHeight);
		return this;
	}
	@Override public EnhancedGui setPositionLocked(boolean val) { positionLocked = val; return this; }
	@Override public EnhancedGui setDimensions(EDimension dimIn) { return setDimensions(dimIn.startX, dimIn.startY, dimIn.width, dimIn.height); }
	@Override public EnhancedGui setDimensions(int startXIn, int startYIn, int widthIn, int heightIn) {
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
	@Override public EnhancedGui setInitialPosition(int startXIn, int startYIn) { startXPos = startXIn; startYPos = startYIn; return this; }
	@Override
	public EnhancedGui centerObjectWithSize(int widthIn, int heightIn) {
		ScaledResolution res = new ScaledResolution(mc);
		int sWidth = res.getScaledWidth();
		int sHeight = res.getScaledHeight();
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
		useCustomPosition = true;
		setCloseAndRecenter(true);
		return this;
	}
	@Override public EDimension getDimensions() { return new EDimension(startX, startY, endX, endY); }
	
	//objects
	@Override public boolean isChildOfObject(IEnhancedGuiObject objIn) { return StaticEGuiObject.isChildOfObject(this, objIn); }
	@Override public EnhancedGui addObject(IEnhancedGuiObject... objsIn) { StaticEGuiObject.addObject(this, objsIn); return this; }
	@Override public EnhancedGui removeObject(IEnhancedGuiObject... objsIn) { objsToBeRemoved.addAll(objsIn); return this; }
	@Override public EObjectGroup getObjectGroup() { return objectGroup; }
	@Override public EnhancedGui setObjectGroup(EObjectGroup groupIn) { objectGroup = groupIn; return this; }
	@Override public void onGroupNotification(ObjectEvent e) {}
	@Override public EArrayList<IEnhancedGuiObject> getImmediateChildren() { return guiObjects; }
	@Override public EArrayList<IEnhancedGuiObject> getObjectsToBeAdded() { return objsToBeAdded; }
	@Override public EArrayList<IEnhancedGuiObject> getObjectsToBeRemoved() { return objsToBeRemoved; }
	@Override public EArrayList<IEnhancedGuiObject> getAllChildren() { return StaticEGuiObject.getAllChildren(this); }
	@Override public EArrayList<IEnhancedGuiObject> getAllChildrenUnderMouse() { return StaticEGuiObject.getAllChildrenUnderMouse(this, mX, mY); }
	
	//parents
	@Override public IEnhancedGuiObject getParent() { return parent; }
	@Override public EnhancedGui setParent(IEnhancedGuiObject parentIn) { parent = parentIn; return this; }
	@Override public IEnhancedTopParent getTopParent() { return StaticEGuiObject.getTopParent(this); }
	@Override public IEnhancedGuiObject getWindowParent() { return StaticEGuiObject.getWindowParent(this); }
	
	//zLevel
	@Override public int getZLevel() { return objZLevel; }
	@Override public EnhancedGui setZLevel(int zLevelIn) { objZLevel = zLevelIn; return this; }
	@Override public EnhancedGui bringToFront() { getTopParent().bringObjectToFront(this); return this; }
	@Override public EnhancedGui sendToBack() { getTopParent().sendObjectToBack(this); return this; }
	
	//focus
	@Override public boolean hasFocus() { return getTopParent().getFocusedObject().equals(this); }
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
		if (eventIn.getFocusType().equals(FocusType.MousePress)) { mousePressed(eventIn.getMouseX(), eventIn.getMouseY(), eventIn.getActionCode()); }
	}
	@Override public void onFocusLost(EventFocus eventIn) { postEvent(new EventFocus(this, this, FocusType.Lost)); }
	@Override
	public void transferFocus(IEnhancedGuiObject objIn) {
		if (!doesFocusLockExist() && objIn != null) {
			relinquishFocus();
			getTopParent().setObjectRequestingFocus(objIn);
		}
	}
	@Override
	public void drawFocusLockBorder() {
		if (checkDraw() && border == null) {
			if (hasHeader() && header.isEnabled()) {
				addObject(border = new EGuiFocusLockBorder(this, header.startX, header.startY, width, height + header.height));
			} else { addObject(border = new EGuiFocusLockBorder(this)); }
		}
	}
	@Override
	public EnhancedGui requestFocus() {
		if (!hasFocus()) {
			if (doesFocusLockExist()) {
				//getTopParent().setObjectRequestingFocus(this);
			} else { getTopParent().setObjectRequestingFocus(this); }
		}
		return this;
	}
	
	//mouse checks
	@Override public boolean isMouseOnObjEdge(int mX, int mY) { return checkDraw() && !getEdgeAreaMouseIsOn().equals(ScreenLocation.out); }
	@Override public ScreenLocation getEdgeAreaMouseIsOn() { return StaticEGuiObject.getEdgeAreaMouseIsOn(this, mX, mY); }
	@Override public void mouseEntered(int mX, int mY) { postEvent(new EventMouse(this, mX, mY, -1, MouseType.Entered)); }
	@Override public void mouseExited(int mX, int mY) { postEvent(new EventMouse(this, mX, mY, -1, MouseType.Exited)); }
	@Override public boolean isMouseInside(int mX, int mY) { return mX >= startX && mX <= endX && mY >= startY && mY <= endY; }
	@Override public boolean isMouseHover(int mX, int mY) { return isMouseInside(mX, mY) && this.equals(getTopParent().getHighestZObjectUnderMouse()); }
	
	//basic inputs
	@Override public void parseMousePosition(int mX, int mY) { guiObjects.stream().filter(o -> o.isMouseInside(mX, mY)).forEach(o -> o.parseMousePosition(mX, mY)); }
	@Override public void mousePressed(int mX, int mY, int button) { StaticEGuiObject.mousePressed(this, mX, mY, button); }
	@Override public void mouseReleased(int mX, int mY, int button) { StaticEGuiObject.mouseReleased(this, mX, mY, button); }
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
	@Override public ObjectEventHandler getEventHandler() { return eventHandler; }
	@Override public EnhancedGui registerListener(IEnhancedGuiObject objIn) { if (eventHandler != null) { eventHandler.registerObject(objIn); } return this; }
	@Override public EnhancedGui unregisterListener(IEnhancedGuiObject objIn) { if (eventHandler != null) { eventHandler.unregisterObject(objIn); } return this; }
	@Override public EnhancedGui postEvent(ObjectEvent e) { if (eventHandler != null) { eventHandler.processEvent(e); } return this; }
	@Override public void onListen(ObjectEvent e) {}
	
	//action object
	@Override public void actionPerformed(IEnhancedActionObject object) { postEvent(new EventAction(this, object)); }
	
	//close object
	@Override public void close() { postEvent(new EventObjects(this, this, ObjectEventType.Close)); closeGui(true); }
	@Override public void closeFull() { closeGui(true); }
	@Override public void onClosed() {}
	@Override public EnhancedGui setFocusedObjectOnClose(IEnhancedGuiObject objIn) { focusObjectOnClose = objIn; return this; }
	
	//-------------------------
	//IEnhancedTopGui Overrides
	//-------------------------
	
	//history
	@Override public Stack<EnhancedGui> getGuiHistory() { return guiHistory; }
	@Override
	public EnhancedGui sendGuiHistory(Stack<EnhancedGui> historyIn) {
		guiHistory = historyIn;
		if (header != null) { header.updateFileUpButtonVisibility(); }
		return this;
	}
	
	//draw order
	@Override public EnhancedGui bringObjectToFront(IEnhancedGuiObject objIn) { toFront = objIn; return this; }
	@Override public EnhancedGui sendObjectToBack(IEnhancedGuiObject objIn) { toBack = objIn; return this; }
	
	//hovering text
	@Override public IEnhancedTopParent setObjectWithHoveringText(IEnhancedGuiObject objIn) { hoveringTextObject = objIn; return this; }
	@Override public IEnhancedGuiObject getObjectWithHoveringText() { return hoveringTextObject; }
	
	//objects
	public IEnhancedGuiObject getHighestZLevelObject() {
		EArrayList<IEnhancedGuiObject> objs = getImmediateChildren();
		if (objs.isNotEmpty()) {
			IEnhancedGuiObject highest = objs.get(0);
			for (IEnhancedGuiObject o : getImmediateChildren()) {
				if (o.getZLevel() > highest.getZLevel()) { highest = o; }
			}
			return highest;
		}
		return null;
	}
	
	//focus
	@Override public IEnhancedGuiObject getDefaultFocusObject() { return defaultFocusObject; }
	@Override public EnhancedGui setDefaultFocusObject(IEnhancedGuiObject objIn) { defaultFocusObject = objIn; return this; }
	@Override public IEnhancedGuiObject getFocusedObject() { return focusedObject; }
	@Override public EnhancedGui setObjectRequestingFocus(IEnhancedGuiObject objIn) { focusQueue.add(new EventFocus(this, objIn, FocusType.Transfer)); return this; }
	@Override public IEnhancedGuiObject getFocusLockObject() { return focusLockObject; }
	@Override public EnhancedGui setFocusLockObject(IEnhancedGuiObject objIn) {
		focusLockObject = objIn;
		transferFocus(focusLockObject);
		return this;
	}
	@Override public EnhancedGui clearFocusLockObject() { focusLockObject = null; return this; }
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
						if (obj != focusedObject) {
							focusedObject.onFocusLost(event);
							focusedObject = obj;
							focusedObject.onFocusGained(event);
						}
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
	@Override public EnhancedGui setModifyingObject(IEnhancedGuiObject objIn, ObjectModifyType typeIn) { modifyingObject = objIn; modifyType = typeIn; return this; }
	@Override public EnhancedGui setResizingDir(ScreenLocation areaIn) { resizingDir = areaIn; return this; }
	@Override public EnhancedGui setModifyMousePos(int mX, int mY) { mousePos.setValues(mX, mY); return this; }
	@Override public IEnhancedGuiObject getModifyingObject() { return modifyingObject; }
	@Override public EnhancedGui clearModifyingObject() { modifyingObject = null; modifyType = ObjectModifyType.None; return this; }
	
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
		
		if (!foundObjs.isEmpty()) {
			for (int i = foundObjs.size() - 1; i >= 0; i--) {
				IEnhancedGuiObject o = foundObjs.get(i);
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
	@Override
	public void closeGui(boolean fullClose) {
		if (fullClose) {
			mc.displayGuiScreen(null);
			if (mc.currentScreen == null) { mc.setIngameFocus(); }
		}
		else if (isSpawned()) {
			if (oldGui != null) {
				try {
					GuiScreen newGui = ((GuiScreen) Class.forName(oldGui.getClass().getName()).getConstructor().newInstance());
					mc.displayGuiScreen(newGui);
					return;
				} catch (Exception e) { e.printStackTrace(); }
			} else {
				mc.displayGuiScreen(null);
		        if (mc.currentScreen == null) { mc.setIngameFocus(); }
			}
		}
		else if (backwardsTraverseable && !guiHistory.isEmpty() && guiHistory.peek() != null) {
			try {
				EnhancedGui oldGuiPass = guiHistory.pop();
				EnhancedGui newGui = ((EnhancedGui) Class.forName(oldGuiPass.getClass().getName()).getConstructor().newInstance());
				if (!closeAndRecenter) {
					newGui.useCustomPosition = true;
					newGui.setPosition(startX, startY);
				}
				newGui.sendGuiHistory(oldGuiPass.getGuiHistory());
				mc.displayGuiScreen(newGui);
				return;
			} catch (Exception e) { e.printStackTrace(); }
		} else {
			mc.displayGuiScreen(null);
			if (mc.currentScreen == null) { mc.setIngameFocus(); }
		}
	}
	@Override public EnhancedGui setCloseAndRecenter(boolean val) { closeAndRecenter = val; return this; }
	
	//-------------------
	//EnhancedGui methods
	//-------------------
	
	protected void updateBeforeNextDraw(int mXIn, int mYIn) {
		if (eventHandler != null) { eventHandler.processEvent(new EventRedraw(this)); }
		res = new ScaledResolution(mc);
		mX = mXIn; mY = mYIn;
		checkMouseHover();
		oldMousePos.setValues(mXIn, mYIn);
		if (!mouseEntered && isMouseHover(mX, mY)) { mouseEntered = true; mouseEntered(mX, mY); }
		if (mouseEntered && !isMouseHover(mX, mY)) { mouseEntered = false; mouseExited(mX, mY); }
		if (!objsToBeRemoved.isEmpty()) { StaticEGuiObject.removeObjects(this, objsToBeRemoved); }
		if (!objsToBeAdded.isEmpty()) { StaticEGuiObject.addObjects(this, objsToBeAdded); }
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
		updateCursorImage();
	}
	
	public void checkMouseHover() {
		if (getTopParent().getHighestZObjectUnderMouse() != null) {
			if (oldMousePos == null) { oldMousePos = new StorageBox<Integer, Integer>(0, 0); }
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
	
	protected int drawString(String text, int x, int y, int color) { return fontRenderer.drawStringI(text, x, y, color); }
    protected int drawCenteredString(String text, int x, int y, int color) { return fontRenderer.drawStringI(text, x - fontRenderer.getStringWidth(text) / 2, y, color); }
	protected int drawStringWithShadow(String text, int x, int y, int color) { return fontRenderer.drawStringWithShadowI(text, x, y, color); }
	protected int drawCenteredStringWithShadow(String text, int x, int y, int color) { return fontRenderer.drawStringWithShadowI(text, x - fontRenderer.getStringWidth(text) / 2, y, color); }
	protected int drawString(String text, int x, int y, int color, float height) { return fontRenderer.drawStringIH(text, x, y, color, height); }
    protected int drawCenteredString(String text, int x, int y, int color, float height) { return fontRenderer.drawStringIH(text, x - fontRenderer.getStringWidth(text) / 2, y, color, height); }
	protected int drawStringWithShadow(String text, int x, int y, int color, float height) { return fontRenderer.drawStringWithShadowIH(text, x, y, color, height); }
	protected int drawCenteredStringWithShadow(String text, int x, int y, int color, float height) { return fontRenderer.drawStringWithShadowIH(text, x - fontRenderer.getStringWidth(text) / 2, y, color, height); }
	
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
	public EnhancedGui setSpawned(boolean val) { isSpawned = val; return this; }
	public boolean isSpawned() { return isSpawned; }
	public String getGuiName() { return guiName; }
}