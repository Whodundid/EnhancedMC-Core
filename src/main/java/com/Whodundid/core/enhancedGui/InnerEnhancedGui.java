package com.Whodundid.core.enhancedGui;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiHeader;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.renderer.EnhancedMCRenderer;
import java.util.Stack;
import net.minecraft.client.gui.GuiScreen;

//Last edited: Jan 9, 2019
//First Added: Sep 14, 2018
//Author: Hunter Bragg

public abstract class InnerEnhancedGui extends EnhancedGuiObject {
	
	public InnerEnhancedGui guiInstance;
	protected EGuiHeader header;
	protected boolean moveWithParent = false;
	protected Stack<Object> guiHistory = new Stack();
	protected boolean closeAndRecenter = false;
	public boolean useCustomPosition = false;
	public Object oldObject = null;
	public static int defaultWidth = 220, defaultHeight = 255;
	
	public InnerEnhancedGui() { this(EnhancedMCRenderer.getInstance(), null); }
	public InnerEnhancedGui(Object oldGuiIn) { this(EnhancedMCRenderer.getInstance(), oldGuiIn); }
	public InnerEnhancedGui(int xPos, int yPos) { guiInstance = this; }
	public InnerEnhancedGui(int xPos, int yPos, Object oldGuiIn) { guiInstance = this; setHistory(oldGuiIn);}
	public InnerEnhancedGui(IEnhancedGuiObject parentIn) { this(parentIn, null); }
	public InnerEnhancedGui(IEnhancedGuiObject parentIn, Object oldGuiIn) { initDefaultPos(parentIn); setHistory(oldGuiIn); }
	public InnerEnhancedGui(IEnhancedGuiObject parentIn, int xPos, int yPos) { this(parentIn, xPos, yPos, null); }
	public InnerEnhancedGui(IEnhancedGuiObject parentIn, int xPos, int yPos, Object oldGuiIn) { initDefaultDims(parentIn, xPos, yPos); setHistory(oldGuiIn); }
	public InnerEnhancedGui(IEnhancedGuiObject parentIn, int xIn, int yIn, int widthIn, int heightIn) { this(parentIn, xIn, yIn, widthIn, heightIn, null); }
	public InnerEnhancedGui(IEnhancedGuiObject parentIn, int xIn, int yIn, int widthIn, int heightIn, Object oldGuiIn) { 
		init(parentIn, xIn, yIn, widthIn, heightIn);
		setHistory(oldGuiIn);
		guiInstance = this;
	}
	
	private void initDefaultPos(IEnhancedGuiObject parentIn) {
		init(parentIn);
		guiInstance = this;
	}
	private void initDefaultDims(IEnhancedGuiObject parentIn, int xPos, int yPos) {
		useCustomPosition = true;
		init(parentIn, xPos, yPos, defaultWidth, defaultHeight);
		guiInstance = this;
	}
	
	private void setHistory(Object guiIn) {
		if (guiIn != null) {
			oldObject = guiIn;
			if (guiIn instanceof InnerEnhancedGui) {
				guiHistory = ((InnerEnhancedGui) guiIn).getGuiHistory();
				guiHistory.push(guiIn);
			}
		}
	}
	
	public void initGui() {
		//setHeader(new EGuiHeader(this));
		//header.setPersistent(true);
		//header.updateFileUpButtonVisibility();
	}
	
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
	
	public InnerEnhancedGui setHeader(EGuiHeader headerIn) {
		if (header != null) { removeObject(header); }
		header = headerIn;
		if (header != null) { header.updateFileUpButtonVisibility(); }
		addObject(headerIn);
		return this;
	}
	
	public Stack<Object> getGuiHistory() { return guiHistory; }
	public InnerEnhancedGui setGuiHistory(Stack<Object> historyIn) {
		guiHistory = historyIn;
		if (header != null) { header.updateFileUpButtonVisibility(); }
		return this;
	}
	
	public void openNewGui(InnerEnhancedGui guiIn) {
		EnhancedMC.getRenderer().addObject(guiIn);
		super.close();
	}
	
	@Override
	public void close() {
		if (!guiHistory.isEmpty() && guiHistory.peek() != null) {
			try {
				Object oldGuiPass = guiHistory.pop();
				if (oldGuiPass instanceof InnerEnhancedGui) {
					InnerEnhancedGui newGui = ((InnerEnhancedGui) Class.forName(oldGuiPass.getClass().getName()).getConstructor(IEnhancedGuiObject.class).newInstance(this));
					newGui.setGuiHistory(((InnerEnhancedGui) oldGuiPass).getGuiHistory());
					EnhancedMC.displayEGui(newGui);
					if (!closeAndRecenter) {
						newGui.useCustomPosition = true;
						newGui.setPosition(startX, startY);
					}
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
				super.close();
				return;
			} catch (Exception e) { e.printStackTrace(); }
		} else {
			super.close();
		}
	}
	public InnerEnhancedGui setCloseAndRecenter(boolean val) { closeAndRecenter = val; return this; }
	
	public InnerEnhancedGui enableHeader(boolean val) {
		header.setEnabled(val);
		return this;
	}
	
	public InnerEnhancedGui setUseCustomPosition(boolean val) { useCustomPosition = val; return this; }
	public boolean movesWithParent() { return moveWithParent; }
	public InnerEnhancedGui setMoveWithParent(boolean val) { moveWithParent = val; return this; }
	public EGuiHeader getHeader() { return header; }
}
