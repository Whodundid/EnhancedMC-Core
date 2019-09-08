package com.Whodundid.core.enhancedGui.guiObjects;

import com.Whodundid.core.enhancedGui.EnhancedGui;
import com.Whodundid.core.enhancedGui.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.InnerEnhancedGui;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventModify;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventObjects;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.ObjectEventType;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.ObjectModifyType;
import com.Whodundid.core.enhancedGui.guiUtil.exceptions.HeaderAlreadyExistsException;
import com.Whodundid.core.enhancedGui.guiUtil.exceptions.ObjectInitException;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.playerUtil.Direction;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Iterator;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

//Last edited: Oct 22, 2018
//First Added: Oct 12, 2018
//Author: Hunter Bragg

public class EGuiScrollList extends EnhancedGuiObject {
	
	public EArrayList<IEnhancedGuiObject> listContents = new EArrayList();
	public boolean isVertical = true;
	EGuiScrollBar scrollBar = null;
	int scrollableHeight = 0;
	int scrollableWidth = 0;
	int scrollPos = 0;
	protected EArrayList<IEnhancedGuiObject> listObjsToBeRemoved = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> listObjsToBeAdded = new EArrayList();
	int backgroundColor = 0xff4D4D4D;
	
	public EGuiScrollList(IEnhancedGuiObject parentIn, int xIn, int yIn, int widthIn, int heightIn) { this(parentIn, xIn, yIn, widthIn, heightIn, true); }
	public EGuiScrollList(IEnhancedGuiObject parentIn, int xIn, int yIn, int widthIn, int heightIn, boolean verticalIn) {
		init(parentIn, xIn, yIn, widthIn, heightIn);
		isVertical = verticalIn;
		scrollableWidth = widthIn - 2;
		scrollableHeight = heightIn - 2;
		scrollBar = new EGuiScrollBar(this, height - 2, height - 2, true, isVertical ? Direction.E : Direction.S);
		//scrollBar.setVisible(isVertical ? scrollableHeight - (height - 2) > 0 : scrollableWidth - (width - 2) > 0);
		addObject(scrollBar);
	}
	
	@Override
	public void initObjects() throws ObjectInitException {
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		updateBeforeNextDraw(mXIn, mYIn);
		drawRect(startX, startY, endX, endY, 0xff000000);
		scrollPos = scrollBar.getScrollPos() - scrollBar.getVisibleAmount();
		int scale = res.getScaleFactor();
		try {
			if (checkDraw()) {
				GlStateManager.pushMatrix();
				GlStateManager.enableBlend();
				
				//draw list contents scissored
				GL11.glEnable(GL11.GL_SCISSOR_TEST);
				GL11.glScissor(
						((startX + 1) * scale),
						(Display.getHeight() - startY * scale) - (height - 1) * scale,
						(width - scrollBar.width - 3) * scale,
						(height - 2) * scale);
				//draw background
				
				drawRect(startX + 1, startY + 1, endX - 1, endY - 1, backgroundColor);
				synchronized (listContents) {
					for (IEnhancedGuiObject o : listContents) {
						if (o.checkDraw()) {
		    				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		    				EDimension d = o.getDimensions();
		    				if (isVertical) { o.setPosition(d.startX, o.getInitialPosition().getValue() - scrollPos); }
		    				else { o.setPosition(o.getInitialPosition().getObject() - scrollPos, d.startY); }
		    				
		    	        	o.drawObject(mXIn, mYIn, ticks);
		    			}
					}
				}
				GL11.glDisable(GL11.GL_SCISSOR_TEST);
				
				//draw non list contents as normal
				synchronized (guiObjects) {
					for (IEnhancedGuiObject o : guiObjects) {
						if (o.checkDraw() && listContents.notContains(o)) {
		    				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		    	        	o.drawObject(mXIn, mYIn, ticks);
		    			}
					}
				}
				GlStateManager.popMatrix();
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	@Override
	public void move(int newX, int newY) {
		if (eventHandler != null) { eventHandler.processEvent(new EventModify(this, this, ObjectModifyType.Move)); }
		if (!positionLocked) {
			EArrayList<IEnhancedGuiObject> objs = new EArrayList(guiObjects);
			objs.addAll(objsToBeAdded);
			Iterator<IEnhancedGuiObject> it = objs.iterator();
			while (it.hasNext()) {
				IEnhancedGuiObject o = it.next();
				if (!o.isPositionLocked()) {
					if (o instanceof InnerEnhancedGui) {
						if (((InnerEnhancedGui) o).movesWithParent()) { o.move(newX, newY); }
					} else {
						if (listContents.contains(o)) { o.setInitialPosition(o.getInitialPosition().getObject() + newX, o.getInitialPosition().getValue() + newY); }
						o.move(newX, newY);
					}
				}
			}
			startX += newX;
			startY += newY;
			if (boundaryDimension != null) { boundaryDimension.move(newX, newY); }
			setDimensions(startX, startY, width, height);
		}
	}
	
	public void clearList() {
		for (IEnhancedGuiObject o : listContents) { removeObject(o); }
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		super.mousePressed(mX, mY, button);
	}
	
	public int getListHeight() { return scrollableHeight; }
	public int getListWidth() { return scrollableWidth; }
	
	public EDimension getListDimensions() {
		return new EDimension(0, 0, width - scrollBar.width - 2, scrollableHeight);
	}
	
	//public void resetScrollPos() {
	//	scrollBar.setScrollBarPos(0);
	//}
	
	@Override
	public void mouseScrolled(int change) {
		if (scrollableHeight - (height - 2) > 0) { scrollBar.setScrollBarPos(scrollBar.getScrollPos() - change * 17); }
		super.mouseScrolled(change);
	}
	
	public EGuiScrollBar getScrollBar() { return scrollBar; }
	public EGuiScrollList renderScrollBarThumb(boolean val) { scrollBar.setRenderThumb(val); return this; }
	
	public EGuiScrollList setListWidth(int widthIn) {
		scrollableWidth = widthIn;
		scrollBar.setHighVal(widthIn);
		//scrollBar.setVisible(isVertical ? scrollableHeight - (height - 2) > 0 : scrollableWidth - (width - 2) > 0);
		return this;
	}
	public EGuiScrollList setListHeight(int heightIn) {
		scrollableHeight = heightIn;
		scrollBar.setHighVal(heightIn);
		//scrollBar.setVisible(isVertical ? scrollableHeight - (height - 2) > 0 : scrollableWidth - (width - 2) > 0);
		return this;
	}
	
	public EGuiScrollList addObjectToList(IEnhancedGuiObject... objsIn) {
		for (IEnhancedGuiObject o : objsIn) {
			if (o != null) {
				if (o != this) {
					if (o instanceof EGuiHeader && hasHeader()) { 
						try { throw new HeaderAlreadyExistsException(getHeader()); } catch (HeaderAlreadyExistsException e) { e.printStackTrace(); }
					}
					if (o instanceof EnhancedGui) {
						ScaledResolution scaledresolution = new ScaledResolution(mc);
			            int i = scaledresolution.getScaledWidth();
			            int j = scaledresolution.getScaledHeight();
			            ((EnhancedGui) o).setWorldAndResolution(mc, i, i);
					}
					
					//apply offset to all added objects so their location is relative to this scrollList
					EDimension dims = o.getDimensions();
					o.setDimensions(startX + 1 + dims.startX, startY + 1 + dims.startY, dims.width, dims.height);
					//limit the boundary of each object to the list's boundary
					EDimension bounds = new EDimension(startX + 1, startY + 1, endX - scrollBar.width - 1, endY - 1);
					o.setBoundaryEnforcer(bounds);
					//replace the original intial position coordinates with the relative ones
					o.setInitialPosition(o.getDimensions().startX, o.getDimensions().startY);
					
					try {
						o.setParent(this).initObjects();
						o.setZLevel(getZLevel() + 1);
						if (o instanceof InnerEnhancedGui) { ((InnerEnhancedGui) o).initGui(); }
						o.completeInitialization();
					} catch (ObjectInitException e) { e.printStackTrace(); }
				}
			}
		}
		listObjsToBeAdded.addAll(objsIn);
		objsToBeAdded.addAll(objsIn);
		return this;
	}
	
	public EGuiScrollList removeObjectFromList(IEnhancedGuiObject... objsIn) {
		listObjsToBeRemoved.addAll(objsIn);
		objsToBeRemoved.addAll(objsIn);
		return this;
	}
	
	@Override
	public EGuiScrollList removeObject(IEnhancedGuiObject... objsIn) {
		objsToBeRemoved.addAll(objsIn);
		for (IEnhancedGuiObject o : objsIn) {
			if (listContents.contains(o)) { listObjsToBeRemoved.add(o); }
		}
		return this;
	}
	
	@Override
	protected void updateBeforeNextDraw(int mXIn, int mYIn) {
		res = new ScaledResolution(mc);
		mX = mXIn; mY = mYIn;
		isMouseHover = isMouseInside(mXIn, mYIn) && getTopParent().getHighestZObjectUnderMouse() != null && getTopParent().getHighestZObjectUnderMouse().equals(this);
		if (!mouseEntered && isMouseHover) { mouseEntered = true; mouseEntered(mX, mY); }
		if (mouseEntered && !isMouseHover) { mouseEntered = false; mouseExited(mX, mY); }
		if (!objsToBeRemoved.isEmpty()) { removeObjects(); }
		if (!objsToBeAdded.isEmpty()) { addObjects(); }
		if (!listObjsToBeRemoved.isEmpty()) { removeListObjects(); }
		if (!listObjsToBeAdded.isEmpty()) { addListObjects(); }
		updateCursorImage();
	}
		
	protected void removeListObjects() {
		listObjsToBeRemoved.forEach(o -> {
			if (o != null) {
				Iterator it = listContents.iterator();
				while (it.hasNext()) {
					if (o.equals(it.next())) {
						if (!o.equals(getTopParent().getFocusedObject())) {
							for (IEnhancedGuiObject child : o.getImmediateChildren()) {
								if (child.equals(getTopParent().getFocusedObject())) { child.relinquishFocus(); }
							}
						} else { o.relinquishFocus(); }
						if (o instanceof InnerEnhancedGui) { ((InnerEnhancedGui) o).onInnerGuiClose(); }
						if (o.equals(border)) { border = null; }
						if (eventHandler != null) { eventHandler.processEvent(new EventObjects(this, o, ObjectEventType.ObjectRemoved)); }
						it.remove();
					}
				}
			}
		});
		listObjsToBeRemoved.clear();
	}
	
	protected void addListObjects() {
		listObjsToBeAdded.forEach(o -> {
			if (o != null) {
				if (o != this) {
					listContents.add(o);
					o.onObjectAddedToParent();
					if (eventHandler != null) { eventHandler.processEvent(new EventObjects(this, o, ObjectEventType.ObjectAdded)); }
				}
			}
		});
		listObjsToBeAdded.clear();
	}
	
	public EGuiScrollList setBackgroundColor(int colorIn) { backgroundColor = colorIn; return this; }
}
