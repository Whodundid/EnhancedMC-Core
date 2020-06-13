package com.Whodundid.core.renderer.taskView;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.windows.RightClickMenu;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import net.minecraft.util.EnumChatFormatting;

public class TaskButton extends WindowButton implements Comparable<TaskButton> {
	
	TaskBar parentBar;
	private WindowParent base;
	private boolean pressed = false;
	private int total = 0;
	protected boolean pinned = false;
	private long earliest = 0l;
	private boolean drawingList = false;
	private boolean listMade = false;
	private WindowDropDown dropDown;
	
	public TaskButton(TaskBar barIn, WindowParent baseIn) {
		super(barIn);
		parentBar = barIn;
		base = baseIn;
		
		//Set the hover text -- use the class name if an object name is not set.
		if (base != null) {
			String name = base.getObjectName();
			setHoverText(name.equals("noname") ? base.getClass().getSimpleName() : name);
		}
		
		//get number of instances
		update();
		
		setDrawTextures(false);
		setImage();
	}
	
	@Override
	public int compareTo(TaskButton b) {
		return Long.compare(getEarliest(), b.getEarliest());
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		
		//draw a highlight overlay if the mouse is over the button
		if (isMouseOver(mXIn, mYIn)) {
			drawRect(0xaa505050);
			//getWindows().forEach(w -> w.drawHighlightBorder());
		}
		
		//draw highlight if the currently focused window is of the same type as this button's base
		IWindowObject o = EnhancedMC.getRenderer().getFocusedObject();
		if (base != null && o != null && o.getWindowParent() != null && o.getWindowParent().getClass() == base.getClass()) {
			drawRect(0xaa808080);
		}
		
		//draw icon
		drawTexture(startX + ((width - 20) / 2), startY + 1 + ((height - 20) / 2), 20, 20, btnTexture);
		
		super.drawObject(mXIn, mYIn);
		
		//draw number of windows
		drawTotal();
		
		if (!pressed && (isDrawingHover() || (dropDown != null && dropDown.isMouseInside(mXIn, mYIn)))) {
			if (!listMade) { bringToFront(); createList(); }
		}
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		if (enabled && checkDraw()) {
			pressedButton = button;
			if (runActionOnPress) { onPress(); }
			else if (button == 0) {
				playPressSound();
				
				pressed = true;
				
				if (total == 1) {
					destroyList();
					WindowParent p = getWindows().get(0);
					
					if (p.isMinimizable()) {
						//check if at front
						if (p == EnhancedMC.getAllActiveWindows().getLast()) {
							if (p.isMinimized()) { performAction(getWindows().get(0)); }
							else { p.setMinimized(true); }
						}
						else { performAction(getWindows().get(0)); }
					}
					else { performAction(getWindows().get(0)); }
					
				}
				else if (total >= 1) {
					if (!listMade) {
						bringToFront();
						createList();
					}
					else { destroyList(); }
				}
			}
			else if (button == 1) {
				EArrayList<WindowParent> windows = getWindows();
				
				RightClickMenu rcm = new RightClickMenu();
				rcm.setTitle(hoverText);
				rcm.setActionReceiver(parentBar);
				rcm.setStoredObject(base);
				
				rcm.addOption(total == 1 ? "Close" : "Close All", EMCResources.guiCloseButton);
				if (EnhancedMC.isDevMode() && total == 1 && windows.get(0).isPinnable()) {
					rcm.addOption(windows.get(0).isPinned() ? "Unpin" : "Pin", EMCResources.guiPinButton);
				}
				if (total == 1) { rcm.addOption("Recenter", EMCResources.guiMoveButton); }
				if (base.showInLists()) { rcm.addOption("New Window", EMCResources.plusButton); }
				
				EnhancedMC.displayWindow(rcm, CenterType.cursorCorner);
			}
		}
	}
	
	@Override
	public void mouseExited(int mXIn, int mYIn) {
		if (pressed) { pressed = false; }
		if (listMade && (dropDown != null && !dropDown.isMouseInside(mXIn, mYIn))) { destroyList(); }
		super.mouseExited(mXIn, mYIn);
	}
	
	@Override
	public void close() {
		super.close();
		destroyList();
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		System.out.println(object);
	}
	
	/** Sets the image of the window that this button represents. */
	private void setImage() {
		if (base != null) { setTextures(base.getWindowIcon(), base.getWindowIcon()); }
		else { setButtonTexture(EMCResources.guiProblem); }
	}
	
	/** Updates the visible number of window instances represented by this button. */
	public void update() { total = getTotal(); }
	
	private void drawTotal() {
		//only draw if there is more than 1 instance
		if (total > 1) {
			drawStringC(total, midX + 1, endY - 8, EColors.lime);
		}
	}
	
	private void createList() {
		dropDown = new WindowDropDown(this, startX, endY, 20, false);
		
		EArrayList<WindowParent> windows = getWindows();
		
		for (int i = 1; i <= windows.size(); i++) {
			WindowParent p = windows.get(i - 1);
			dropDown.addEntry(i + ": " + EnumChatFormatting.GREEN + p.getObjectName(), EColors.lorange, p);
		}
		
		EnhancedMC.getRenderer().addObject(null, dropDown);
		
		listMade = true;
	}
	
	public void destroyList() {
		if (dropDown != null) {
			dropDown.close();
			dropDown = null;
			EnhancedMC.getRenderer().revealHiddenObjects();
			for (WindowParent w : EnhancedMC.getAllActiveWindows()) {
				w.setDrawWhenMinimized(false);
			}
		}
		listMade = false;
	}
	
	/** Returns the total number of window instances that this button represents. */
	public int getTotal() {
		EArrayList<WindowParent> windows = (EArrayList<WindowParent>) EnhancedMC.getAllWindowInstances(base.getClass());
		
		if (total != windows.size()) {
			earliest = Long.MAX_VALUE;
			for (WindowParent w : windows) {
				if (earliest < w.getInitTime()) { earliest = w.getInitTime(); }
			}
		}
		
		return windows.size();
	}
	
	/** Returns a list of all current window instances of the same type that this button represents. */
	public EArrayList<WindowParent> getWindows() {
		return (EArrayList<WindowParent>) EnhancedMC.getAllWindowInstances(base.getClass());
	}
	
	public WindowParent getWindowType() { return base; }
	public TaskButton setPinned(boolean val) { pinned = val; return this; }
	public boolean isPinned() { return pinned; }
	public long getEarliest() { return earliest; }

}
