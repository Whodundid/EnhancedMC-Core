package com.Whodundid.core.renderer.taskView;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.header.WindowHeader;
import com.Whodundid.core.windowLibrary.windowObjects.windows.EMCGuiSelectionList;
import com.Whodundid.core.windowLibrary.windowObjects.windows.RightClickMenu;
import com.Whodundid.core.windowLibrary.windowTypes.WindowObject;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import java.util.Collections;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;

public class TaskBar extends WindowObject {
	
	public static int drawSize = 24;
	public static EArrayList<TaskButton> buttons = new EArrayList();
	protected static EArrayList<WindowParent> toAdd = new EArrayList();
	protected static EArrayList<WindowParent> toRemove = new EArrayList();
	ScreenLocation drawSide = ScreenLocation.top;
	
	//--------------------
	//TaskBar Constructors
	//--------------------
	
	public TaskBar() { this(false); }
	public TaskBar(boolean fromScratch) {
		reorient();
		res = new ScaledResolution(Minecraft.getMinecraft());
		if (fromScratch) { buildFromScratch(); }
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
		updateLists();
		
		//draw background
		drawRect(startX, startY - 1, res.getScaledWidth(), endY, 0xff232323);
		drawRect(startX - 1, endY - 1, res.getScaledWidth(), endY, 0xff000000);
		
		if (buttons.isEmpty() && toAdd.isEmpty()) {
			drawString("No currently open windows..", startX + 5, midY - 4, EColors.lgray);
		}
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		if (button == 1) {
			RightClickMenu menu = new RightClickMenu();
			menu.setTitle("Taskbar");
			menu.setActionReceiver(this);
			menu.setStoredObject(this);
			menu.addOption("Open Window..", EMCResources.plusButton);
			EnhancedMC.displayWindow(menu, CenterType.cursorCorner);
		}
		
		super.mousePressed(mXIn, mYIn, button);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object instanceof TaskButton) {
			TaskButton b = (TaskButton) object;
			
			if (args.length > 0 && args[0] instanceof WindowParent) {
				WindowParent window = (WindowParent) args[0];
				
				if (window.isMinimized()) { window.setMinimized(false); }
				window.bringToFront();
				EnhancedMC.getRenderer().setFocusedObject(window);
			}
		}
		if (object instanceof RightClickMenu) {
			RightClickMenu rcm = (RightClickMenu) object;
			
			if (rcm.getStoredObject() == this) {
				if (rcm.getSelectedObject().equals("Open Window..")) {
					EnhancedMC.displayWindow(new EMCGuiSelectionList(), CenterType.screen);
				}
			}
			else if (rcm.getStoredObject() instanceof WindowParent) {
				if (rcm.getSelectedObject() == "Pin" ) {
					System.out.println("pinning: " + rcm.getStoredObject().getClass().getSimpleName());
				}
				if (rcm.getSelectedObject() == "New Window") {
					try {
						WindowParent w = (WindowParent) rcm.getStoredObject();
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
				if (rcm.getSelectedObject().equals("Close") || rcm.getSelectedObject().equals("Close All")) {
					WindowParent w = (WindowParent) rcm.getStoredObject();
					if (w != null) {
						Class c = w.getClass();
						EArrayList<WindowParent> windows = EnhancedMC.getAllWindowInstances(c);
						windows.forEach(p -> p.close());
					}
				}
				if (rcm.getSelectedObject().equals("Recenter")) {
					WindowParent w = (WindowParent) rcm.getStoredObject();
					if (w != null) {
						Class c = w.getClass();
						
						EArrayList<WindowParent> windows = EnhancedMC.getAllWindowInstances(c);
						
						if (windows.size() == 1) {
							WindowParent p = windows.get(0);
							WindowHeader h = p.getHeader();
							TaskBar b = EnhancedMC.getRenderer().getTaskBar();
							
							int hh = (h != null) ? h.height : 0;
							int bh = (b != null) ? b.height : 0;
							int oH = hh + bh;
							int maxW = MathHelper.clamp_int(p.width, 0, res.getScaledWidth() - 40);
							int maxH = MathHelper.clamp_int(p.height, 0, res.getScaledHeight() - 40 - oH);
							
							p.setDimensions(maxW, maxH);
							p.centerObjectWithSize(maxW, maxH);
							p.setPosition(p.startX, p.startY + hh);
							p.reInitObjects();
						}
					}
				} //recenter
			}
		} //instanceof rcm
	}
	
	@Override
	public void close() {
		buttons.clear();
		toAdd.clear();
		toRemove.clear();
		super.close();
	}
	
	//---------------
	//TaskBar Methods
	//---------------
	
	public void forceUpdate() {
		updateLists();
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
			
			EArrayList<IWindowObject> removeGhosts = new EArrayList();
			for (IWindowObject o : getObjects()) {
				if (o instanceof TaskButton) {
					if (!EnhancedMC.isEGuiOpen(((TaskButton) o).getWindowType().getClass())) { removeGhosts.add(o); }
				}
			}
			
			for (IWindowObject o : removeGhosts) {
				removeObject(null, o);
			}
			
			//check for ghost buttons
			for (TaskButton b : buttons) {
				if (!EnhancedMC.isEGuiOpen(b.getWindowType().getClass())) { toRemove.add(b.getWindowType()); }
			}
			
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
				removing.forEach(b -> removeObject(null, b));
				
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
				
				int sX = 0;
				int sY = 0;
				int w = drawSize + 2;
				int h = drawSize - 1;
				boolean v = isVertical();
				
				if (v) { sY += (w * buttons.size()) + (0 * buttons.size()); }
				else { sX += (w * buttons.size()) + (0 * buttons.size()); }
				
				b.setDimensions(sX, sY, w, h);
				
				//add to bar
				buttons.add(b);
				addObject(null, b);
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
			
			int sX = 0;
			int sY = 0;
			int w = drawSize + 2;
			int h = drawSize - 1;
			boolean v = isVertical();
			
			if (v) { sY += (w * i) + (0 * i); }
			else { sX += (w * i) + (0 * i); }
			
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
	
	private void buildFromScratch() {
		windowObjects.clear();
		objsToBeAdded.clear();
		objsToBeRemoved.clear();
		buttons.clear();
		toAdd.clear();
		toRemove.clear();
		
		EArrayList<WindowParent> windows = EnhancedMC.getAllActiveWindows();
		EArrayList<WindowParent> filtered = new EArrayList();
		EArrayList<TaskButton> toBuild = new EArrayList();
		
		for (WindowParent w : windows) {
			boolean contains = false;
			for (WindowParent f : filtered) {
				if (f.getClass() == w.getClass()) { contains = true; break; }
			}
			if (!contains) { filtered.add(w); }
		}
		
		filtered.forEach(w -> toBuild.add(new TaskButton(this, w)));
		Collections.sort(toBuild);
		
		for (TaskButton b : toBuild) {
			buttons.add(b);
			addObject(null, b);
		}
		
		repositionButtons();
	}
	
}
