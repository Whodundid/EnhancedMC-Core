package com.Whodundid.core.windowLibrary.windowTypes;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.coreEvents.emcEvents.WindowClosedEvent;
import com.Whodundid.core.debug.DebugFunctions;
import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.renderer.taskView.TaskBar;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.resourceUtil.EResource;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.header.WindowHeader;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowParent;
import java.util.Stack;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;

//Author: Hunter Bragg

public class WindowParent extends WindowObject implements IWindowParent, Comparable<WindowParent> {
	
	public static int defaultWidth = 220, defaultHeight = 255;
	
	public WindowParent windowInstance;
	protected WindowHeader header;
	protected boolean moveWithParent = false;
	protected boolean pinned = false;
	protected boolean pinnable = false;
	protected ScreenLocation maximized = ScreenLocation.out;
	protected boolean minimizable = true;
	protected boolean minimized = false;
	protected boolean maximizable = false;
	protected boolean drawMinimized = false;
	protected boolean drawDefaultBackground = false;
	protected Stack<Object> WindowHistory = new Stack();
	protected Object oldObject = null;
	protected EArrayList<String> aliases = new EArrayList();
	protected EResource windowIcon = EMCResources.windowIcon;
	protected EDimension preMaxFull = new EDimension();
	protected EDimension preMaxSide = new EDimension();
	protected boolean showInTaskBar = true;
	protected long initTime = 0l;
	
	public WindowParent() { this(EnhancedMCRenderer.getInstance(), null); }
	public WindowParent(Object oldGuiIn) { this(EnhancedMCRenderer.getInstance(), oldGuiIn); }
	public WindowParent(int xPos, int yPos) { windowInstance = this; initTime = System.currentTimeMillis(); }
	public WindowParent(int xPos, int yPos, Object oldGuiIn) { initTime = System.currentTimeMillis(); windowInstance = this; pullHistoryFrom(oldGuiIn); }
	public WindowParent(IWindowObject parentIn) { this(parentIn, null); }
	public WindowParent(IWindowObject parentIn, Object oldGuiIn) { initTime = System.currentTimeMillis(); initDefaultPos(parentIn); pullHistoryFrom(oldGuiIn); }
	public WindowParent(IWindowObject parentIn, int xPos, int yPos) { this(parentIn, xPos, yPos, null); }
	public WindowParent(IWindowObject parentIn, int xPos, int yPos, Object oldGuiIn) { initTime = System.currentTimeMillis(); initDefaultDims(parentIn, xPos, yPos); pullHistoryFrom(oldGuiIn); }
	public WindowParent(IWindowObject parentIn, int xIn, int yIn, int widthIn, int heightIn) { this(parentIn, xIn, yIn, widthIn, heightIn, null); }
	public WindowParent(IWindowObject parentIn, int xIn, int yIn, int widthIn, int heightIn, Object oldGuiIn) {
		initTime = System.currentTimeMillis();
		init(parentIn, xIn, yIn, widthIn, heightIn);
		pullHistoryFrom(oldGuiIn);
		windowInstance = this;
		preMaxFull = new EDimension(xIn, yIn, widthIn, heightIn);
	}
	
	//--------------------
	//Comparable Overrides
	//--------------------
	
	@Override public int compareTo(WindowParent p) { return Long.compare(initTime, p.getInitTime()); }
	
	//-----------------------
	//IWindowObject Overrides
	//-----------------------
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		if (drawDefaultBackground) { drawDefaultBackground(); }
		if (EnhancedMC.isDebugMode()) {
			if (!isMaximized()) {
				int y = hasHeader() ? getHeader().startY - 9 : startY - 9;
				int pos = 0;
				int half = -1;
				String draw = "";
				String time = String.valueOf(initTime);
				
				if (time.length() > 6) { time = time.substring(time.length() - 6); }
				
				if (DebugFunctions.drawWindowPID) {
					pos = mc.fontRendererObj.getStringWidth("PID: " + EnumChatFormatting.YELLOW + getObjectID());
					draw = EnumChatFormatting.AQUA + "PID: " + EnumChatFormatting.YELLOW + getObjectID();
					
					if (DebugFunctions.drawWindowInit) {
						half = pos + 6;
						pos += mc.fontRendererObj.getStringWidth(EnumChatFormatting.AQUA + "  InitTime: " + EnumChatFormatting.YELLOW + time);
						draw += EnumChatFormatting.AQUA + "  InitTime: " + EnumChatFormatting.YELLOW + time;
					}
				}
				else if (DebugFunctions.drawWindowInit) {
					pos = mc.fontRendererObj.getStringWidth("InitTime: " + EnumChatFormatting.YELLOW + time);
					draw += "InitTime: " + EnumChatFormatting.YELLOW + time;
				}
				
				drawRect(startX, y - 1, startX + pos + 5, y + mc.fontRendererObj.FONT_HEIGHT + 1, EColors.black);
				drawRect(startX + 1, y, startX + pos + 4, y + mc.fontRendererObj.FONT_HEIGHT, EColors.dgray);
				if (half > 0) { drawRect(startX + half, y, startX + half + 1, y + mc.fontRendererObj.FONT_HEIGHT, EColors.black); }
				drawString(draw, startX + 3, y + 1, EColors.cyan);
			}
		}
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		bringToFront();
		super.mousePressed(mXIn, mYIn, button);
	}
	
	@Override
	public void close() {
		MinecraftForge.EVENT_BUS.post(new WindowClosedEvent(this));
		super.close();
	}
	
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
					
					if (getMaximizedPosition() != ScreenLocation.out) {
						maximize();
					}
					
					if (any) { requestFocus(); }
				}
				else {
					super.sendArgs(args);
				}
			}
		}
	}
	
	//-----------------------
	//IWindowParent Overrides
	//-----------------------
	
	@Override public boolean isPinned() { return pinned; }
	@Override
	public boolean isMaximized() {
		return maximized == ScreenLocation.center || maximized == ScreenLocation.left || maximized == ScreenLocation.right ||
			   maximized == ScreenLocation.topLeft || maximized == ScreenLocation.topRight || maximized == ScreenLocation.botLeft || maximized == ScreenLocation.botRight;
	}
	@Override public boolean isMinimized() { return minimized; }
	@Override public boolean isPinnable() { return pinnable; }
	@Override public boolean isMaximizable() { return maximizable; }
	@Override public boolean isMinimizable() { return minimizable; }
	@Override public IWindowParent setPinned(boolean val) { pinned = val; return this; }
	@Override public IWindowParent setMaximized(ScreenLocation val) { if (maximizable) { maximized = val; } return this; }
	@Override public IWindowParent setMinimized(boolean val) { if (minimizable) { minimized = val; } return this; }
	@Override public ScreenLocation getMaximizedPosition() { return maximized; }
	@Override public IWindowParent setPinnable(boolean val) { pinnable = val; return this; }
	@Override public IWindowParent setMaximizable(boolean val) { maximizable = val; return this; }
	@Override public IWindowParent setMinimizable(boolean val) { minimizable = val; return this; }
	@Override public IWindowParent setDrawWhenMinimized(boolean val) { drawMinimized = val; return this; }
	@Override public boolean drawsWhileMinimized() { return drawMinimized; }
	
	@Override
	public void maximize() {
		EDimension screen = getTopParent().getDimensions();
		boolean hasTaskBar = EnhancedMC.getRenderer().getTaskBar() != null;
		
		int sw = screen.width;
		int sh = screen.height;
		int tb = TaskBar.drawSize - 1;
		int hh = header.height;
		
		if (maximized == ScreenLocation.center) {
			if (hasTaskBar) {
				setDimensions(0, hh + tb, sw, sh - (hh + tb));
			}
			else { setDimensions(0, hh, sw, sh - hh); }
		}
		else if (maximized == ScreenLocation.left) {
			if (hasTaskBar) {
				setDimensions(0, hh + tb, sw / 2 + 1, sh - (hh + tb));
			}
			else { setDimensions(0, hh, sw / 2 + 1, sh - hh); }
		}
		else if (maximized == ScreenLocation.right) {
			if (hasTaskBar) {
				setDimensions(sw / 2, hh + tb, sw / 2, sh - (hh + tb));
			}
			else { setDimensions(sw / 2, hh, sw / 2, sh - hh); }
		}
		else if (maximized == ScreenLocation.topLeft) {
			if (hasTaskBar) {
				setDimensions(0, hh + tb, sw / 2 + 1, (sh / 2) - (hh + tb));
			}
			else { setDimensions(0, hh, sw / 2 + 1, (sh / 2) - hh); }
		}
		else if (maximized == ScreenLocation.botLeft) {
			if (hasTaskBar) {
				setDimensions(0, screen.midY + hh - 1, sw / 2 + 1, ((sh - tb - (hh / 2)) / 2) - 1);
			}
			else { setDimensions(0, screen.midY + hh - 1, sw / 2 + 1, (sh / 2) - hh + 2); }
		}
		else if (maximized == ScreenLocation.topRight) {
			if (hasTaskBar) {
				setDimensions(sw / 2, hh + tb, sw / 2, (sh / 2) - (hh + tb));
			}
			else { setDimensions(sw / 2, hh, sw / 2, (sh / 2) - hh); }
		}
		else if (maximized == ScreenLocation.botRight) {
			if (hasTaskBar) {
				setDimensions(sw / 2, screen.midY + hh - 1, sw / 2, ((sh - tb - (hh / 2)) / 2) - 1);
			}
			else { setDimensions(sw / 2, screen.midY + hh - 1, sw / 2, (sh / 2) - hh + 2); }
		}
		
		reInitObjects();
	}
	
	@Override
	public void miniaturize() {
		setDimensions(getPreMax());
		
		TaskBar bar = EnhancedMC.getRenderer().getTaskBar();
		int tb = (bar != null) ? bar.height : 0;
		
		EDimension dims = getDimensions();
		int headerHeight = hasHeader() ? getHeader().height : 0;
		int sX = dims.startX;
		int sY = dims.startY;
		int w = dims.width;
		int h = dims.height;
		
		sX = sX < 0 ? 4 : sX;
		sY = (sY - headerHeight) < 2 ? tb + 4 + headerHeight : sY;
		sX = sX + w > res.getScaledWidth() ? -4 + sX - (sX + w - res.getScaledWidth()) : sX;
		sY = sY + h > res.getScaledHeight() ? -4 + sY - (sY + h - res.getScaledHeight()) : sY;
		setDimensions(sX, sY, w, h);
		
		reInitObjects();
	}
	
	@Override public EDimension getPreMax() { return preMaxFull; }
	@Override public IWindowParent setPreMax(EDimension dimIn) { preMaxFull = new EDimension(dimIn); return this; }
	
	@Override public boolean isOpWindow() { return false; }
	@Override public boolean isDebugWindow() { return false; }
	@Override public boolean showInLists() { return true; }
	
	@Override public Stack<Object> getWindowHistory() { return WindowHistory; }
	@Override
	public IWindowParent setWindowHistory(Stack<Object> historyIn) {
		WindowHistory = historyIn;
		if (header != null) { header.updateButtonVisibility(); }
		return this;
	}
	
	@Override public long getInitTime() { return initTime; }
	
	/** Not planned for 1.0 */
	@Override
	public void renderTaskBarPreview(int xPos, int yPos) {
		
	}
	
	@Override
	public void drawHighlightBorder() {
		WindowHeader header = getHeader();
		int sY = (header != null) ? header.startY : startY;
		drawRect(startX - 1, sY - 1, endX + 1, endY + 1, EColors.red);
	}
	
	@Override public EArrayList<String> getAliases() { return aliases; }
	
	//--------------------
	//WindowParent Methods
	//--------------------
	
	private void initDefaultPos(IWindowObject parentIn) {
		init(parentIn);
		windowInstance = this;
	}
	
	private void initDefaultDims(IWindowObject parentIn, int xPos, int yPos) {
		init(parentIn, xPos, yPos, defaultWidth, defaultHeight);
		windowInstance = this;
	}
	
	private void pullHistoryFrom(Object objectIn) {
		if (objectIn != null) {
			oldObject = objectIn;
			if (objectIn instanceof WindowParent) {
				WindowHistory = ((WindowParent) objectIn).getWindowHistory();
				WindowHistory.push(objectIn);
			}
		}
	}
	
	public void initWindow() {}
	
	protected void defaultDims() { setDimensions(startX, startY, defaultWidth, defaultHeight); }
	protected void defaultHeader(IWindowParent in) { setHeader(new WindowHeader(in)); }
	
	public void fileUpAndClose() {
		if (!WindowHistory.isEmpty() && WindowHistory.peek() != null) {
			Object oldGuiPass = WindowHistory.pop();
			if (oldGuiPass instanceof WindowParent) {
				try {
					WindowParent newGui = ((WindowParent) Class.forName(oldGuiPass.getClass().getName()).getConstructor().newInstance());
					newGui.setWindowHistory(((WindowParent) oldGuiPass).getWindowHistory());
					IWindowParent p = EnhancedMC.displayWindow(newGui, this, true, true, false, CenterType.object);
					p.setPinned(isPinned());
					if (isMaximized() && newGui.isMaximizable()) {
						newGui.setPreMax(getPreMax());
						newGui.setMaximized(getMaximizedPosition());
						newGui.maximize();
					}
					EnhancedMC.getRenderer().setFocusedObject(p);
				}
				catch (Exception e) { e.printStackTrace(); }
			}
			else if (oldGuiPass instanceof GuiScreen) {
				if (oldObject != null) {
					try {
						GuiScreen newGui = ((GuiScreen) Class.forName(oldObject.getClass().getName()).getConstructor().newInstance());
						mc.displayGuiScreen(newGui);
						return;
					}
					catch (Exception e) { e.printStackTrace(); }
				}
				else {
					mc.displayGuiScreen(null);
			        if (mc.currentScreen == null) { mc.setIngameFocus(); }
				}
			}
		}
		close();
	}
	
	public WindowParent enableHeader(boolean val) {
		if (header != null) { header.setEnabled(val); }
		return this;
	}
	
	public void drawDefaultBackground() {
		drawRect(startX, startY, endX, endY, 0xff000000);
		drawRect(startX + 1, startY + 1, endX - 1, endY - 1, 0xff383838);
		drawRect(startX + 2, startY + 2, endX - 2, endY - 2, 0xff3f3f3f);
		drawRect(startX + 3, startY + 3, endX - 3, endY - 3, 0xff424242);
	}
	
	public WindowParent setHeader(WindowHeader headerIn) {
		if (header != null) { removeObject(header); }
		header = headerIn;
		if (header != null) { header.updateButtonVisibility(); }
		addObject(headerIn);
		return this;
	}
	
	//--------------------
	//WindowParent Getters
	//--------------------
	
	public Object getOldObject() { return oldObject; }
	public EResource getWindowIcon() { return windowIcon; }
	public WindowHeader getHeader() { return header; }
	public boolean movesWithParent() { return moveWithParent; }
	public boolean showInTaskBar() { return showInTaskBar; }
	
	//--------------------
	//WindowParent Setters
	//--------------------	
	
	public WindowParent setDrawDefaultBackground(boolean val) { drawDefaultBackground = val; return this; }
	public WindowParent setMoveWithParent(boolean val) { moveWithParent = val; return this; }
	
}
