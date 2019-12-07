package com.Whodundid.core.enhancedGui.types;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiHeader;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.util.renderUtil.CenterType;
import java.util.Stack;
import net.minecraft.client.gui.GuiScreen;

//Last edited: Jan 9, 2019
//First Added: Sep 14, 2018
//Author: Hunter Bragg

public abstract class WindowParent extends EnhancedGuiObject implements IWindowParent {
	
	public WindowParent guiInstance;
	protected EGuiHeader header;
	protected boolean moveWithParent = false;
	protected boolean pinned = false;
	protected boolean pinnable = true;
	protected Stack<Object> guiHistory = new Stack();
	protected Object oldObject = null;
	public static int defaultWidth = 220, defaultHeight = 255;
	
	public WindowParent() { this(EnhancedMCRenderer.getInstance(), null); }
	public WindowParent(Object oldGuiIn) { this(EnhancedMCRenderer.getInstance(), oldGuiIn); }
	public WindowParent(int xPos, int yPos) { guiInstance = this; }
	public WindowParent(int xPos, int yPos, Object oldGuiIn) { guiInstance = this; setHistory(oldGuiIn);}
	public WindowParent(IEnhancedGuiObject parentIn) { this(parentIn, null); }
	public WindowParent(IEnhancedGuiObject parentIn, Object oldGuiIn) { initDefaultPos(parentIn); setHistory(oldGuiIn); }
	public WindowParent(IEnhancedGuiObject parentIn, int xPos, int yPos) { this(parentIn, xPos, yPos, null); }
	public WindowParent(IEnhancedGuiObject parentIn, int xPos, int yPos, Object oldGuiIn) { initDefaultDims(parentIn, xPos, yPos); setHistory(oldGuiIn); }
	public WindowParent(IEnhancedGuiObject parentIn, int xIn, int yIn, int widthIn, int heightIn) { this(parentIn, xIn, yIn, widthIn, heightIn, null); }
	public WindowParent(IEnhancedGuiObject parentIn, int xIn, int yIn, int widthIn, int heightIn, Object oldGuiIn) { 
		init(parentIn, xIn, yIn, widthIn, heightIn);
		setHistory(oldGuiIn);
		guiInstance = this;
	}
	
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
	protected void defaultHeader(IEnhancedGuiObject in) { setHeader(new EGuiHeader(in)); }
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		bringToFront();
		super.mousePressed(mXIn, mYIn, button);
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
	
	@Override public boolean isPinned() { return pinned; }
	@Override public boolean isPinnable() { return pinnable; }
	@Override public WindowParent setPinned(boolean val) { pinned = val; return this; }
	@Override public WindowParent setPinnable(boolean val) { pinnable = val; return this; }
	
	@Override
	public Stack<Object> getGuiHistory() { return guiHistory; }
	@Override
	public WindowParent setGuiHistory(Stack<Object> historyIn) {
		guiHistory = historyIn;
		if (header != null) { header.updateButtonVisibility(); }
		return this;
	}
	
	public void fileUpAndClose() {
		if (!guiHistory.isEmpty() && guiHistory.peek() != null) {
			Object oldGuiPass = guiHistory.pop();
			if (oldGuiPass instanceof WindowParent) {
				try {
					WindowParent newGui = ((WindowParent) Class.forName(oldGuiPass.getClass().getName()).getConstructor().newInstance());
					newGui.setGuiHistory(((WindowParent) oldGuiPass).getGuiHistory());
					EnhancedMC.displayEGui(newGui, this, true, false, CenterType.object);
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
		super.close();
	}
	
	public WindowParent enableHeader(boolean val) {
		if (header != null) { header.setEnabled(val); }
		return this;
	}
	
	public boolean movesWithParent() { return moveWithParent; }
	public WindowParent setMoveWithParent(boolean val) { moveWithParent = val; return this; }
	public EGuiHeader getHeader() { return header; }
	public Object getOldObject() { return oldObject; }
}
