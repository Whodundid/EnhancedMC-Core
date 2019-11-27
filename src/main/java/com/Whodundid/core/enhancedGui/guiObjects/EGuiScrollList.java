package com.Whodundid.core.enhancedGui.guiObjects;

import com.Whodundid.core.enhancedGui.StaticEGuiObject;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventModify;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventObjects;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.ObjectEventType;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.ObjectModifyType;
import com.Whodundid.core.enhancedGui.guiUtil.exceptions.HeaderAlreadyExistsException;
import com.Whodundid.core.enhancedGui.guiUtil.exceptions.ObjectInitException;
import com.Whodundid.core.enhancedGui.types.EnhancedGui;
import com.Whodundid.core.enhancedGui.types.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

//Last edited: Oct 22, 2018
//First Added: Oct 12, 2018
//Author: Hunter Bragg

public class EGuiScrollList extends EnhancedGuiObject {
	
	public EArrayList<IEnhancedGuiObject> listContents = new EArrayList();
	EGuiScrollBar verticalScroll, horizontalScroll;
	EGuiButton reset;
	int scrollableHeight = 0;
	int scrollableWidth = 0;
	protected EArrayList<IEnhancedGuiObject> listObjsToBeRemoved = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> listObjsToBeAdded = new EArrayList();
	protected int backgroundColor = 0xff4D4D4D;
	protected int borderColor = 0xff000000;
	protected int heightToBeSet = 0, widthToBeSet = 0;
	boolean vScrollVis = true;
	boolean hScrollVis = true;
	boolean resetVis = false;
	
	protected EGuiScrollList() {}
	public EGuiScrollList(IEnhancedGuiObject parentIn, int xIn, int yIn, int widthIn, int heightIn) {
		init(parentIn, xIn, yIn, widthIn, heightIn);
		scrollableWidth = widthIn - 2;
		scrollableHeight = heightIn - 2;
	}
	
	@Override
	public void initObjects() throws ObjectInitException {
		verticalScroll = new EGuiScrollBar(this, height - 2, height - 2, ScreenLocation.right);
		horizontalScroll = new EGuiScrollBar(this, width - 2, width - 2, ScreenLocation.bot);
		
		reset = new EGuiButton(this, endX - 5, endY - 5, 5, 5);
		
		verticalScroll.setVisible(vScrollVis);
		horizontalScroll.setVisible(hScrollVis);
		reset.setVisible(resetVis);
		
		verticalScroll.setZLevel(getZLevel() + 3);
		horizontalScroll.setZLevel(getZLevel() + 3);

		addObject(verticalScroll, horizontalScroll, reset);
		
		setListHeight(heightToBeSet).setListWidth(widthToBeSet);
		
		updateVisuals();
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		updateBeforeNextDraw(mXIn, mYIn);
		drawRect(startX, startY, endX, endY, borderColor);
		
		verticalScroll.setVisible(isVScrollDrawn());
		horizontalScroll.setVisible(isHScrollDrawn());
		reset.setVisible(isResetDrawn());
		
		int vScrollPos = verticalScroll.getScrollPos() - verticalScroll.getVisibleAmount();
		int hScrollPos = horizontalScroll.getScrollPos() - horizontalScroll.getVisibleAmount();
		
		int scale = res.getScaleFactor();
		try {
			if (checkDraw()) {
				GlStateManager.pushMatrix();
				GlStateManager.enableBlend();
				
				//draw list contents scissored
				GL11.glEnable(GL11.GL_SCISSOR_TEST);
				GL11.glScissor(
						((startX + 1) * scale),
						(Display.getHeight() - startY * scale) - (height - (isHScrollDrawn() ? horizontalScroll.height + 1 : 0) - 1) * scale,
						(width - (isVScrollDrawn() ? verticalScroll.width + 1 : 0) - 2) * scale,
						(height - (isHScrollDrawn() ? 6 : 2)) * scale);
				
				//draw background
				drawRect(startX + 1, startY + 1, endX - 1, endY - 1, backgroundColor);
				
				synchronized (listContents) {
					for (IEnhancedGuiObject o : listContents) {
						if (o.checkDraw()) {
		    				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		    				EDimension d = o.getDimensions();
		    				o.setPosition(o.getInitialPosition().getObject() - hScrollPos, o.getInitialPosition().getValue() - vScrollPos);
		    	        	o.drawObject(mXIn, mYIn, ticks);
		    			}
					}
				}
				GL11.glDisable(GL11.GL_SCISSOR_TEST);
				
				//draw non list contents as normal (non scissored)
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
					if (o instanceof WindowParent) {
						if (((WindowParent) o).movesWithParent()) { o.move(newX, newY); }
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
	
	@Override
	public EGuiScrollList setPosition(int newX, int newY) {
		EDimension d = getDimensions();
		StorageBox<Integer, Integer> loc = new StorageBox(d.startX, d.startY);
		StorageBoxHolder<IEnhancedGuiObject, StorageBox<Integer, Integer>> previousLocations = new StorageBoxHolder();
		EArrayList<IEnhancedGuiObject> objs = new EArrayList(getAllChildren());
		for (IEnhancedGuiObject o : objs) {
			if (listContents.contains(o) || listObjsToBeAdded.contains(o)) {
				previousLocations.add(o, new StorageBox(o.getDimensions().startX, o.getDimensions().startY));
			} else {
				previousLocations.add(o, new StorageBox(o.getDimensions().startX - loc.getObject(), o.getDimensions().startY - loc.getValue()));
			}
		}
		setDimensions(newX, newY, d.width, d.height);
		for (IEnhancedGuiObject o : objs) {
			if (listContents.contains(o) || listObjsToBeAdded.contains(o)) {
				StorageBox<Integer, Integer> oldLoc = previousLocations.getBoxWithObj(o).getValue();
				o.setPosition((newX + oldLoc.getObject()) - startX, (newY + oldLoc.getValue()) - startY);
			} else {
				StorageBox<Integer, Integer> oldLoc = previousLocations.getBoxWithObj(o).getValue();
				o.setPosition(newX + oldLoc.getObject(), newY + oldLoc.getValue());
			}
		}
		return this;
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object == reset) {
			verticalScroll.reset();
			horizontalScroll.reset();
		}
	}
	
	public EDimension getListDimensions() {
		int w = (endX - (isVScrollDrawn() ? verticalScroll.width + 2 : 2)) - startX - 1;
		int h = (endY - (isHScrollDrawn() ? horizontalScroll.height - 4 : 1)) - startY - 1;
		return new EDimension(0, 0, w, h);
	}
	
	public void resetScrollPos() {
		verticalScroll.setScrollBarPos(0);
		verticalScroll.setScrollBarPos(0);
	}
	
	@Override
	public void mouseScrolled(int change) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			if (scrollableWidth - (width - 2) > 0) { horizontalScroll.setScrollBarPos(horizontalScroll.getScrollPos() - change * 17); }
		}
		else if (scrollableHeight - (height - 2) > 0) { verticalScroll.setScrollBarPos(verticalScroll.getScrollPos() - change * 17); }
		super.mouseScrolled(change);
	}
	
	public EGuiScrollList setListSize(int widthIn, int heightIn) {
		setListWidth(widthIn);
		setListHeight(heightIn);
		return this;
	}
	public EGuiScrollList setListWidth(int widthIn) {
		if (horizontalScroll != null) {
			scrollableWidth = widthIn;
			horizontalScroll.setHighVal(widthIn);
		} else { widthToBeSet = widthIn; }
		return this;
	}
	public EGuiScrollList setListHeight(int heightIn) {
		if (verticalScroll != null) {
			scrollableHeight = heightIn;
			verticalScroll.setHighVal(heightIn);
		} else { heightToBeSet = heightIn; }
		return this;
	}
	public EGuiScrollList growList(int amount) {
		growListWidth(amount);
		growListHeight(amount);
		return this;
	}
	public EGuiScrollList growListWidth(int amount) {
		if (horizontalScroll != null) { setListWidth(getListWidth() + amount); }
		else { widthToBeSet += amount; }
		return this;
	}
	public EGuiScrollList growListHeight(int amount) {
		if (verticalScroll != null) { setListHeight(getListHeight() + amount); }
		else { heightToBeSet += amount; }
		return this;
	}
	
	public EGuiScrollList addObjectToList(IEnhancedGuiObject... objsIn) {
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
		listObjsToBeRemoved.addAll(objsIn);
		return this;
	}
	
	@Override
	protected void updateBeforeNextDraw(int mXIn, int mYIn) {
		res = new ScaledResolution(mc);
		mX = mXIn; mY = mYIn;
		if (!mouseEntered && isMouseHover(mX, mY)) { mouseEntered = true; mouseEntered(mX, mY); }
		if (mouseEntered && !isMouseHover(mX, mY)) { mouseEntered = false; mouseExited(mX, mY); }
		if (!objsToBeRemoved.isEmpty()) { StaticEGuiObject.removeObjects(this, objsToBeRemoved); }
		if (!objsToBeAdded.isEmpty()) { StaticEGuiObject.addObjects(this, objsToBeAdded); }
		if (!listObjsToBeRemoved.isEmpty()) { removeListObjects(); }
		if (!listObjsToBeAdded.isEmpty()) { addListObjects(); }
		updateCursorImage();
	}
	
	protected void removeListObjects() {
		for (IEnhancedGuiObject o : listObjsToBeRemoved ) {
			if (o != null) {
				Iterator it = listContents.iterator();
				while (it.hasNext()) {
					if (o.equals(it.next())) {
						if (!o.equals(getTopParent().getFocusedObject())) {
							for (IEnhancedGuiObject child : o.getImmediateChildren()) {
								if (child.equals(getTopParent().getFocusedObject())) { child.relinquishFocus(); }
							}
						} else { o.relinquishFocus(); }
						o.onClosed();
						if (eventHandler != null) { eventHandler.processEvent(new EventObjects(this, o, ObjectEventType.ObjectRemoved)); }
						it.remove();
					}
				}
			}
		}
		listObjsToBeRemoved.clear();
	}
	
	protected void addListObjects() {
		for (IEnhancedGuiObject o : listObjsToBeAdded) {
			try {
				if (o != null && o != this) {
					if (o instanceof EnhancedGui) { continue; }
					if (o instanceof EGuiHeader && hasHeader()) { 
						 throw new HeaderAlreadyExistsException(getHeader());
					}
					
					int eX = endX - (isVScrollDrawn() ? verticalScroll.width + 2 : 2);
					int eY = endY - (isHScrollDrawn() ? horizontalScroll.height - 4 : 1);
					
					EDimension bounds = new EDimension(startX + 1, startY + 1, eX, eY);
					
					//apply offset to all added objects so their location is relative to this scrollList
					EDimension dims = o.getDimensions();
					o.setDimensions(startX + dims.startX, startY + dims.startY, dims.width, dims.height);
					
					//limit the boundary of each object to the list's boundary
					o.setBoundaryEnforcer(bounds);
					for (IEnhancedGuiObject q : o.getImmediateChildren()) { q.setBoundaryEnforcer(bounds); }
					for (IEnhancedGuiObject q : o.getObjectsToBeAdded()) { q.setBoundaryEnforcer(bounds); }
					
					//replace the original intial position coordinates with the relative ones
					o.setInitialPosition(o.getDimensions().startX, o.getDimensions().startY);
					
					try {
						o.setParent(this).initObjects();
						o.setZLevel(getZLevel() + 1);
						if (o instanceof WindowParent) { ((WindowParent) o).initGui(); }
						o.completeInit();
					} catch (ObjectInitException e) { e.printStackTrace(); }
					listContents.add(o);
					o.onAdded();
					postEvent(new EventObjects(this, o, ObjectEventType.ObjectAdded));
				}
			} catch (HeaderAlreadyExistsException e) { e.printStackTrace(); }
		}
		listObjsToBeAdded.clear();
	}
	
	private void updateVisuals() {
		if (isVScrollDrawn()) {
			EDimension v = verticalScroll.getDimensions();
			verticalScroll.setDimensions(v.startX, v.startY, v.width, v.height - (isResetDrawn() ? 4 : 0));
		}
		if (isHScrollDrawn() && isVScrollDrawn()) {
			EDimension h = horizontalScroll.getDimensions();
			horizontalScroll.setDimensions(h.startX, h.startY, h.width - verticalScroll.width - 1, h.height);
		}
	}
	
	public void clearList() { listContents.forEach(o -> removeObject(o)); }
	
	public EGuiScrollList setBackgroundColor(int colorIn) { backgroundColor = colorIn; return this; }
	public EGuiScrollList setBorderColor(int colorIn) { borderColor = colorIn; return this; }
	public EGuiScrollList setVScrollDrawn(boolean valIn) { vScrollVis = valIn; if (verticalScroll != null) { verticalScroll.setVisible(valIn); } updateVisuals(); return this; }
	public EGuiScrollList setHScrollDrawn(boolean valIn) { hScrollVis = valIn; if (horizontalScroll != null) { horizontalScroll.setVisible(valIn); } updateVisuals(); return this; }
	public EGuiScrollList setResetDrawn(boolean valIn) { resetVis = valIn; if (reset != null) { reset.setVisible(valIn); } updateVisuals(); return this; }
	public EGuiScrollList renderVScrollBarThumb(boolean val) { if (verticalScroll != null) { verticalScroll.setRenderThumb(val); } else { vScrollVis = val; } return this; }
	public EGuiScrollList renderHScrollBarThumb(boolean val) { if (horizontalScroll != null) { horizontalScroll.setRenderThumb(val); } else { hScrollVis = val; } return this; }
	
	public int getListHeight() { return scrollableHeight - (isHScrollDrawn() ? horizontalScroll.getDimensions().height : 0); }
	public int getListWidth() { return scrollableWidth - (isVScrollDrawn() ? verticalScroll.getDimensions().width : 0); }
	public boolean isVScrollDrawn() { return vScrollVis && (verticalScroll != null ? verticalScroll.getHighVal() > verticalScroll.getVisibleAmount() : false); }
	public boolean isHScrollDrawn() { return hScrollVis && (horizontalScroll != null ? horizontalScroll.getHighVal() > horizontalScroll.getVisibleAmount() : false); }
	public boolean isResetDrawn() { return resetVis && (isVScrollDrawn() || isHScrollDrawn()); }
	public EGuiScrollBar getVScrollBar() { return verticalScroll; }
	public EGuiScrollBar getHScrollBar() { return horizontalScroll; }
}
