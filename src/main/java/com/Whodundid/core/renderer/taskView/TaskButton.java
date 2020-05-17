package com.Whodundid.core.renderer.taskView;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiRightClickMenu;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class TaskButton extends EGuiButton implements Comparable<TaskButton> {
	
	TaskBar parentBar;
	private WindowParent base;
	private boolean pressed = false;
	private int total = 0;
	protected boolean pinned = false;
	
	public TaskButton(TaskBar barIn, WindowParent baseIn) {
		super(barIn);
		parentBar = barIn;
		base = baseIn;
		
		//setDrawBackground(true);
		//setBackgroundColor(EColors.black);
		
		//Set the hover text -- use the class name if an object name is not set.
		if (base != null) {
			String name = base.getObjectName();
			setHoverText(name.equals("noname") ? base.getClass().getSimpleName() : name);
		}
		
		//get number of instances
		update();
		
		setImage();
	}
	
	@Override
	public int compareTo(TaskButton b) {
		return Long.compare(base.getInitTime(), b.getWindowType().getInitTime());
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		//draw a highlight overlay if the mouse is over the button
		if (isMouseOver(mXIn, mYIn)) {
			drawRect(0xaab2b2b2, -1);
		}
		
		super.drawObject(mXIn, mYIn);
		
		//debug string
		//drawString(base.getClass().getSimpleName(), midX, midY, EColors.magenta);
		
		//draw number of windows
		drawTotal();
		
		if (isDrawingHover()) {
			drawWindows();
		}
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		if (enabled && checkDraw()) {
			pressedButton = button;
			if (runActionOnPress) { onPress(); }
			else if (button == 0) {
				playPressSound();
				
				if (total > 0) {
					
					//by defualt just grab the first instance for now
					performAction(getWindows().get(0));
				}
			}
			else if (button == 1) {
				EGuiRightClickMenu pinOption = new EGuiRightClickMenu();
				pinOption.setTitle(hoverText);
				pinOption.setActionReceiver(parentBar);
				pinOption.setStoredObject(base);
				//pinOption.addOption("Pin", EMCResources.guiPinButton);
				pinOption.addOption("Close", EMCResources.guiCloseButton);
				if (base.showInLists()) { pinOption.addOption("New Window", EMCResources.plusButton); }
				EnhancedMC.displayWindow(pinOption, CenterType.cursorCorner);
			}
		}
	}
	
	@Override
	public void mouseExited(int mXIn, int mYIn) {
		if (pressed) { pressed = false; }
		super.mouseExited(mXIn, mYIn);
	}
	
	/** Sets the image of the window that this button represents. */
	private void setImage() {
		if (base != null) {
			setTextures(base.getWindowIcon(), base.getWindowIcon());
		}
		else {
			setButtonTexture(EMCResources.guiProblem);
		}
	}
	
	/** Updates the visible number of window instances represented by this button. */
	public void update() { total = getTotal(); }
	
	private void drawTotal() {
		//only draw if there is more than 1 instance
		if (total > 1) {
			drawCenteredString(total, midX + 1, endY - 7, EColors.lime);
		}
	}
	
	/** Draws a small visual for each instance. */
	private void drawWindows() {
		
		//not working as intended..
		
		/*
		EArrayList<WindowParent> windows = getWindows();
		for (WindowParent w : windows) {
			double scaleFactor = 0.5;
			int distX = this.startX - w.startX;
			
			System.out.println(distX / (distX * scaleFactor));
			
			GL11.glPushMatrix();
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
			GL11.glTranslated(distX / (distX * scaleFactor), 0, 0);
			GL11.glScaled(scaleFactor, scaleFactor, scaleFactor);
			
			w.drawObject(-1, -1);
			for (IEnhancedGuiObject o : w.getObjects()) {
				if (o.checkDraw()) {
					o.drawObject(-1, -1);
					if (o instanceof EGuiHeader) {
						for (IEnhancedGuiObject c : o.getAllChildren()) {
							if (c.checkDraw()) {
								c.drawObject(-1, -1);
							}
						}
					}
				}
			}
			GL11.glPopMatrix();
		}
		*/
	}
	
	/** Returns the total number of window instances that this button represents. */
	public int getTotal() { return EnhancedMC.getAllWindowInstances(base.getClass()).size(); }
	
	/** Returns a list of all current window instances of the same type that this button represents. */
	public EArrayList<WindowParent> getWindows() {
		return (EArrayList<WindowParent>) EnhancedMC.getAllWindowInstances(base.getClass());
	}
	
	public WindowParent getWindowType() { return base; }
	public TaskButton setPinned(boolean val) { pinned = val; return this; }
	public boolean isPinned() { return pinned; }
}
