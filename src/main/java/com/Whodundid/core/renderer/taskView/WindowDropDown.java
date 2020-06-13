package com.Whodundid.core.renderer.taskView;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.dropDownList.DropDownListEntry;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.dropDownList.WindowDropDownList;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.header.WindowHeader;
import com.Whodundid.core.windowLibrary.windowObjects.windows.RightClickMenu;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.FocusType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventFocus;
import net.minecraft.util.MathHelper;

public class WindowDropDown extends WindowDropDownList {

	private TaskButton parentButton;
	private DropDownListEntry last = null;
	private RightClickMenu rcm = null;
	
	public WindowDropDown(TaskButton taskButtonIn, int x, int y, int entryHeightIn, boolean useGlobalAction) {
		super(taskButtonIn, x, y, entryHeightIn, useGlobalAction);
		parentButton = taskButtonIn;
		setAlwaysOpen(true);
		setDrawTop(false);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		super.drawObject(mXIn, mYIn);
		
		if (rcm == null) {
			DropDownListEntry entry = getHoveringEntry(mXIn, mYIn);
			if (entry != null) {
				if (entry != last) {
					last = entry;
					EArrayList<WindowParent> windows = EnhancedMC.getAllActiveWindows();
					EnhancedMC.getRenderer().revealHiddenObjects();
					
					for (WindowParent w : windows) {
						w.setDrawWhenMinimized(true);
					}
					
					if (entry.getEntryObject() instanceof WindowParent) {
						WindowParent p = (WindowParent) entry.getEntryObject();
						
						for (WindowParent w : windows) {
							if (w != p) {
								w.setHidden(true);
								w.setDrawWhenMinimized(false);
							}
						}
					}
				} //if entry != last
			}
		} //rcm == null
		
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		if (button == 0) {
			DropDownListEntry entry = getHoveringEntry(mXIn, mYIn);
			if (entry != null) {
				if (entry.getEntryObject() instanceof WindowParent) {
					WindowParent p = (WindowParent) entry.getEntryObject();
					parentButton.performAction(p);
					parentButton.destroyList();
					if (rcm != null) { rcm.close(); rcm = null; }
				}
			}
		}
		else if (button == 1) {
			openRCM(mXIn, mYIn);
		}
		
	}
	
	@Override
	public void onFocusGained(EventFocus eventIn) {
		if (eventIn.getFocusType().equals(FocusType.MousePress)) {
			if (eventIn.getActionCode() == 0) {
				DropDownListEntry entry = getHoveringEntry(eventIn.getMX(), eventIn.getMY());
				if (entry != null) {
					if (entry.getEntryObject() instanceof WindowParent) {
						WindowParent p = (WindowParent) entry.getEntryObject();
						parentButton.destroyList();
						parentButton.performAction(p);
					}
				}
			}
			else if (eventIn.getActionCode() == 1) {
				openRCM(eventIn.getMX(), eventIn.getMY());
			} //mouse 1
		}
	}
		
	@Override
	public void mouseExited(int mXIn, int mYIn) {
		if (rcm == null) {
			if (!parentButton.isMouseInside(mXIn, mYIn)) {
				parentButton.destroyList();
			}
			else {
				EnhancedMC.getRenderer().revealHiddenObjects();
				
				for (WindowParent w : EnhancedMC.getAllActiveWindows()) {
					w.setDrawWhenMinimized(false);
				}
				
				last = null;
			}
		}
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == rcm) {
			if (rcm.getStoredObject() instanceof WindowParent) {
				WindowParent p = (WindowParent) rcm.getStoredObject();
				
				switch ((String) rcm.getSelectedObject()) {
				case "Close":
					parentButton.destroyList();
					p.close();
					break;
				case "Maximize":
					parentButton.destroyList();
					p.setPreMax(p.getDimensions());
					p.setMaximized(ScreenLocation.center);
					p.maximize();
					EnhancedMC.getRenderer().setFocusedObject(p);
					p.bringToFront();
					break;
				case "Minimize":
					parentButton.destroyList();
					p.setMaximized(ScreenLocation.out);
					p.miniaturize();
					EnhancedMC.getRenderer().setFocusedObject(p);
					p.bringToFront();
					break;
				case "Recenter":
					parentButton.destroyList();
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
					break;
				}
			} //if
		}
	}
	
	@Override
	public void close() {
		super.close();
		if (rcm != null) { rcm.close(); rcm = null; }
	}

	private void openRCM(int mXIn, int mYIn) {
		if (rcm != null) { rcm.close(); rcm = null; }
		
		DropDownListEntry entry = getHoveringEntry(mXIn, mYIn);
		if (entry != null) {
			if (entry.getEntryObject() instanceof WindowParent) {
				WindowParent p = (WindowParent) entry.getEntryObject();
				
				int total = 0;
				if (p.isCloseable()) { total++; }
				if (p.isMaximizable()) { total++; }
				if (p.isMoveable()) { total++; }
				
				if (total > 0) {
					rcm = new RightClickMenu() {
						@Override
						public void close() {
							super.close();
							rcm = null;
							parentButton.destroyList();
						}
					};
					
					rcm.setTitle(p.getObjectName());
					rcm.setActionReceiver(this);
					rcm.setStoredObject(p);
					
					if (p.isCloseable()) { rcm.addOption("Close", EMCResources.guiCloseButton); }
					if (p.isMaximizable()) {
						if (p.isMaximized()) { rcm.addOption("Minimize", EMCResources.guiMinButton); }
						else { rcm.addOption("Maximize", EMCResources.guiMaxButton); }
					}
					if (p.isMoveable()) {
						rcm.addOption("Recenter", EMCResources.guiMoveButton);
					}
				}
				
				EnhancedMC.displayWindow(rcm, CenterType.cursorCorner);
			}
		}
	}
	
}
