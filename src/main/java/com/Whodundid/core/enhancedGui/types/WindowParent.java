package com.Whodundid.core.enhancedGui.types;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.coreEvents.emcEvents.WindowClosedEvent;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.resourceUtil.EResource;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import java.util.Stack;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;

//Author: Hunter Bragg

public class WindowParent extends EnhancedGuiObject implements IWindowParent, Comparable<WindowParent> {
	
	public static int defaultWidth = 220, defaultHeight = 255;
	
	public WindowParent guiInstance;
	protected EGuiHeader header;
	protected boolean moveWithParent = false;
	protected boolean pinned = false;
	protected boolean pinnable = false;
	protected boolean maximized = false;
	protected boolean maximizable = false;
	protected boolean drawDefaultBackground = false;
	protected Stack<Object> guiHistory = new Stack();
	protected Object oldObject = null;
	protected EArrayList<String> aliases = new EArrayList();
	protected EResource windowIcon = EMCResources.windowIcon;
	protected EDimension preMaxDims = new EDimension();
	protected boolean showInTaskBar = true;
	protected long initTime = 0l;
	
	public WindowParent() { this(EnhancedMCRenderer.getInstance(), null); }
	public WindowParent(Object oldGuiIn) { this(EnhancedMCRenderer.getInstance(), oldGuiIn); }
	public WindowParent(int xPos, int yPos) { guiInstance = this; initTime = System.currentTimeMillis(); }
	public WindowParent(int xPos, int yPos, Object oldGuiIn) { initTime = System.currentTimeMillis(); guiInstance = this; setHistory(oldGuiIn); }
	public WindowParent(IEnhancedGuiObject parentIn) { this(parentIn, null); }
	public WindowParent(IEnhancedGuiObject parentIn, Object oldGuiIn) { initTime = System.currentTimeMillis(); initDefaultPos(parentIn); setHistory(oldGuiIn); }
	public WindowParent(IEnhancedGuiObject parentIn, int xPos, int yPos) { this(parentIn, xPos, yPos, null); }
	public WindowParent(IEnhancedGuiObject parentIn, int xPos, int yPos, Object oldGuiIn) { initTime = System.currentTimeMillis(); initDefaultDims(parentIn, xPos, yPos); setHistory(oldGuiIn); }
	public WindowParent(IEnhancedGuiObject parentIn, int xIn, int yIn, int widthIn, int heightIn) { this(parentIn, xIn, yIn, widthIn, heightIn, null); }
	public WindowParent(IEnhancedGuiObject parentIn, int xIn, int yIn, int widthIn, int heightIn, Object oldGuiIn) {
		initTime = System.currentTimeMillis();
		init(parentIn, xIn, yIn, widthIn, heightIn);
		setHistory(oldGuiIn);
		guiInstance = this;
		preMaxDims = new EDimension(xIn, yIn, widthIn, heightIn);
	}
	
	//--------------------
	//Comparable Overrides
	//--------------------
	
	@Override public int compareTo(WindowParent p) { return Long.compare(initTime, p.getInitTime()); }
	
	//----------------------------
	//IEnhancedGuiObject Overrides
	//----------------------------
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		if (drawDefaultBackground) { drawDefaultBackground(); }
		if (EnhancedMC.isDebugMode()) {
			int y = hasHeader() ? getHeader().startY - 9 : startY - 9;
			int pos = mc.fontRendererObj.getStringWidth("InitTime: " + EnumChatFormatting.YELLOW + initTime);
			drawRect(startX, y - 1, startX + pos + 5, y + mc.fontRendererObj.FONT_HEIGHT + 1, EColors.black);
			drawRect(startX + 1, y, startX + pos + 4, y + mc.fontRendererObj.FONT_HEIGHT, EColors.dgray);
			drawString("InitTime: " + EnumChatFormatting.YELLOW + initTime, startX + 3, y + 1, EColors.cyan);
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
	
	//-----------------------
	//IWindowParent Overrides
	//-----------------------
	
	@Override public boolean isPinned() { return pinned; }
	@Override public boolean isMaximized() { return maximized; }
	@Override public boolean isPinnable() { return pinnable; }
	@Override public boolean isMaximizable() { return maximizable; }
	@Override public IWindowParent setPinned(boolean val) { pinned = val; return this; }
	@Override public IWindowParent setMaximized(boolean val) { maximized = val; return this; }
	@Override public IWindowParent setPinnable(boolean val) { pinnable = val; return this; }
	@Override public IWindowParent setMaximizable(boolean val) { maximizable = val; return this; }
	
	@Override public void maximize() {}
	@Override public void miniturize() {}
	
	@Override public EDimension getPreMax() { return preMaxDims; }
	@Override public IWindowParent setPreMax(EDimension dimIn) { preMaxDims = new EDimension(dimIn); return this; }
	
	@Override public boolean isOpWindow() { return false; }
	@Override public boolean isDebugWindow() { return false; }
	@Override public boolean showInLists() { return true; }
	
	@Override public Stack<Object> getGuiHistory() { return guiHistory; }
	@Override
	public IWindowParent setGuiHistory(Stack<Object> historyIn) {
		guiHistory = historyIn;
		if (header != null) { header.updateButtonVisibility(); }
		return this;
	}
	
	@Override public long getInitTime() { return initTime; }
	
	@Override public EArrayList<String> getAliases() { return aliases; }
	
	//--------------------
	//WindowParent Methods
	//--------------------
	
	private void initDefaultPos(IEnhancedGuiObject parentIn) {
		init(parentIn);
		guiInstance = this;
	}
	
	private void initDefaultDims(IEnhancedGuiObject parentIn, int xPos, int yPos) {
		init(parentIn, xPos, yPos, defaultWidth, defaultHeight);
		guiInstance = this;
	}
	
	private void setHistory(Object guiIn) {
		if (guiIn != null) {
			oldObject = guiIn;
			if (guiIn instanceof WindowParent) {
				guiHistory = ((WindowParent) guiIn).getGuiHistory();
				guiHistory.push(guiIn);
			}
		}
	}
	
	public void initGui() {}
	
	protected void defaultPos() { centerObjectWithSize(defaultWidth, defaultHeight); }
	protected void defaultDims() { setDimensions(startX, startY, defaultWidth, defaultHeight); }
	protected void defaultHeader(IWindowParent in) { setHeader(new EGuiHeader(in)); }
	
	public void fileUpAndClose() {
		if (!guiHistory.isEmpty() && guiHistory.peek() != null) {
			Object oldGuiPass = guiHistory.pop();
			if (oldGuiPass instanceof WindowParent) {
				try {
					WindowParent newGui = ((WindowParent) Class.forName(oldGuiPass.getClass().getName()).getConstructor().newInstance());
					newGui.setGuiHistory(((WindowParent) oldGuiPass).getGuiHistory());
					IWindowParent p = EnhancedMC.displayWindow(newGui, this, true, true, false, CenterType.object);
					p.setPinned(this.isPinned());
				} catch (Exception e) { e.printStackTrace(); }
			}
			else if (oldGuiPass instanceof GuiScreen) {
				if (oldObject != null) {
					try {
						GuiScreen newGui = ((GuiScreen) Class.forName(oldObject.getClass().getName()).getConstructor().newInstance());
						mc.displayGuiScreen(newGui);
						return;
					} catch (Exception e) { e.printStackTrace(); }
				} else {
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
	
	public WindowParent setHeader(EGuiHeader headerIn) {
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
	public EGuiHeader getHeader() { return header; }
	public boolean movesWithParent() { return moveWithParent; }
	public boolean showInTaskBar() { return showInTaskBar; }
	
	//--------------------
	//WindowParent Setters
	//--------------------	
	
	public WindowParent setDrawDefaultBackground(boolean val) { drawDefaultBackground = val; return this; }
	public WindowParent setMoveWithParent(boolean val) { moveWithParent = val; return this; }
	
}
