package com.Whodundid.core.renderer.taskView;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.enhancedGui.guiObjects.utilityObjects.EGuiRightClickMenu;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EMCGuiSelectionList;
import com.Whodundid.core.enhancedGui.types.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.client.gui.ScaledResolution;

public class TaskBar extends EnhancedGuiObject {
	
	public static int drawSize = 24;
	public static EArrayList<WindowParent> typeOrder = new EArrayList();
	public static EArrayList<TaskButton> buttons = new EArrayList();
	protected static EArrayList<WindowParent> toAdd = new EArrayList();
	protected static EArrayList<WindowParent> toRemove = new EArrayList();
	ScreenLocation drawSide = ScreenLocation.top;
	
	//--------------------
	//TaskBar Constructors
	//--------------------
	
	public TaskBar() {
		reorient();
	}
	
	//----------------------
	//TaskBar Static Methods
	//----------------------
	
	public static synchronized void windowOpened(WindowParent window) { toAdd.add(window); }
	public static synchronized void windowClosed(WindowParent window) { toRemove.add(window); }
	
	//----------------------------
	//IEnhancedGuiObject Overrides
	//----------------------------
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		
		//validate buttons
		//update();
		updateLists();
		
		//draw background
		drawRect(0xff2d2d2d);
		drawHRect(startX - 1, startY, endX + 1, endY, 1, 0xff000000);
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		if (button == 1) {
			EGuiRightClickMenu menu = new EGuiRightClickMenu();
			menu.setTitle("Taskbar");
			menu.setActionReciever(this);
			menu.setStorredObject(this);
			menu.addOption("Open Window..", EMCResources.plusButton);
			EnhancedMC.displayWindow(menu, CenterType.cursorCorner);
		}
		
		super.mousePressed(mXIn, mYIn, button);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object instanceof TaskButton) {
			TaskButton b = (TaskButton) object;
			
			if (args.length > 0 && args[0] instanceof WindowParent) {
				WindowParent window = (WindowParent) args[0];
				
				window.bringToFront();
				window.requestFocus();
			}
		}
		if (object instanceof EGuiRightClickMenu) {
			EGuiRightClickMenu rcm = (EGuiRightClickMenu) object;
			
			if (rcm.getStorredObject() == this) {
				if (rcm.getSelectedObject().equals("Open Window..")) {
					EnhancedMC.displayWindow(new EMCGuiSelectionList(), CenterType.screen);
				}
			}
			else if (rcm.getStorredObject() instanceof WindowParent) {
				if (rcm.getSelectedObject() == "Pin" ) {
					System.out.println("pinnging: " + rcm.getStorredObject().getClass().getSimpleName());
				}
				if (rcm.getSelectedObject() == "New Window") {
					try {
						WindowParent w = (WindowParent) rcm.getStorredObject();
						if (w != null) {
							WindowParent n = w.getClass().newInstance();
							
							WindowParent old = null;
							
							EArrayList<WindowParent> windows = (EArrayList<WindowParent>) EnhancedMC.getAllWindowInstances(n.getClass());
							if (windows != null && windows.isNotEmpty()) { old = windows.get(windows.size() - 1); }
							
							EnhancedMC.displayWindow(n, old, true, false, false, (old != null && !old.isMaximized()) ? CenterType.objectIndent : CenterType.screen);
						}
					}
					catch (Exception e) { e.printStackTrace(); }
					
				}
				if (rcm.getSelectedObject().equals("Close")) {
					WindowParent w = (WindowParent) rcm.getStorredObject();
					w.close();
				}
			}
			
		}
	}
	
	@Override
	public void close() {
		buttons.clear();
		toAdd.clear();
		toRemove.clear();
		super.close();
	}
	
	//---------------
	//TaskBar Getters
	//---------------
	
	public boolean isVertical() { return drawSide == ScreenLocation.left || drawSide == ScreenLocation.right; }
	
	//---------------
	//TaskBar Setters
	//---------------
	
	public void setDrawSide(ScreenLocation sideIn) {
		if (sideIn != ScreenLocation.out) {
			drawSide = sideIn;
			reorient();
			reInitObjects();
		}
	}
	
	//------------------------
	//TaskBar Internal Methods
	//------------------------
	
	private void reorient() {
		ScaledResolution res = new ScaledResolution(mc);
		int w = res.getScaledWidth();
		int h = res.getScaledHeight();
		
		//change draw dimensions
		switch (drawSide) {
		case top: setDimensions(0, 0, w, drawSize); break;
		case left: setDimensions(0, 0, drawSize, h); break;
		case right: setDimensions(w - drawSize, 0, drawSize, h); break;
		case bot: setDimensions(0, h - drawSize, w, drawSize); break;
		default: break;
		}
		
		repositionButtons();
	}
	
	private void updateLists() {
		try {
			//process objects to be added
			EUtil.ifForEach(toAdd.isNotEmpty(), toAdd, p -> addButton(p));
			toAdd.clear();
			
			//process objects to be removed
			if (toRemove.isNotEmpty()) {
				EArrayList<TaskButton> removing = new EArrayList();
				
				for (WindowParent p : toRemove) {
					for (TaskButton b : buttons) {
						if (b.getWindowType().getClass() == p.getClass()) {
							//only remove if there are no more
							if (b.getTotal() == 0) {
								removing.add(b);
							}
						}
					}
				}
				
				//remove old ones
				buttons.removeAll(removing);
				removing.forEach(b -> removeObject(b));
				
				//update the remaining
				buttons.forEach(b -> b.update());
				repositionButtons();
				
				toRemove.clear();
			}
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	private void addButton(WindowParent window) {
		if (window.showInTaskBar()) {
			if (!typeExists(window)) {
				TaskButton b = new TaskButton(this, window);
				
				int sX = 2;
				int sY = 2;
				int w = drawSize - 4;
				int h = drawSize - 4;
				boolean v = isVertical();
				
				if (v) { sY += (w * buttons.size()) + (6 * buttons.size()); }
				else { sX += (w * buttons.size()) + (6 * buttons.size()); }
				
				b.setDimensions(sX, sY, w, h);
				
				//add to bar
				buttons.add(b);
				addObject(b);
			}
			else {
				for (TaskButton b : buttons) {
					if (b.getWindowType().getClass() == window.getClass()) {
						b.update();
					}
				}
			} //else
		}
	}
	
	private void repositionButtons() {
		for (int i = 0; i < buttons.size(); i++) {
			TaskButton b = buttons.get(i);
			
			int sX = 2;
			int sY = 2;
			int w = drawSize - 4;
			int h = drawSize - 4;
			boolean v = isVertical();
			
			if (v) { sY += (w * i) + (6 * i); }
			else { sX += (w * i) + (6 * i); }
			
			b.setDimensions(sX, sY, w, h);
		}
	}
	
	private boolean typeExists(WindowParent testIn) {
		if (testIn != null) {
			if (buttons.isEmpty()) { return false; }
			
			for (TaskButton b : buttons) {
				if (b.getWindowType().getClass() == testIn.getClass()) { return true; }
			}
		}
		return false;
	}
	
}
