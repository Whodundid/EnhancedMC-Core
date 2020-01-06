package com.Whodundid.core.enhancedGui.guiObjects.basicObjects;

import com.Whodundid.core.enhancedGui.StaticEGuiObject;
import com.Whodundid.core.enhancedGui.objectEvents.EventModify;
import com.Whodundid.core.enhancedGui.objectEvents.EventObjects;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.ObjectEventType;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.ObjectModifyType;
import com.Whodundid.core.enhancedGui.objectExceptions.ObjectInitException;
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
import java.util.stream.Collectors;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

//Author: Hunter Bragg

public class EGuiScrollList extends EnhancedGuiObject {
	
	protected EArrayList<IEnhancedGuiObject> listContents = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> drawnListObjects = new EArrayList();
	protected EGuiScrollBar verticalScroll, horizontalScroll;
	protected EGuiButton reset;
	protected int scrollableHeight = 0;
	protected int scrollableWidth = 0;
	protected EArrayList<IEnhancedGuiObject> listObjsToBeRemoved = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> listObjsToBeAdded = new EArrayList();
	protected int backgroundColor = 0xff4D4D4D;
	protected int borderColor = 0xff000000;
	protected int heightToBeSet = 0, widthToBeSet = 0;
	protected boolean vScrollVis = true;
	protected boolean hScrollVis = true;
	protected boolean resetVis = false;
	
	protected EGuiScrollList() {}
	public EGuiScrollList(IEnhancedGuiObject parentIn, int xIn, int yIn, int widthIn, int heightIn) {
		init(parentIn, xIn, yIn, widthIn, heightIn);
		scrollableWidth = widthIn - 2;
		scrollableHeight = heightIn - 2;
	}
	
	@Override
	public void initObjects() throws ObjectInitException {
		verticalScroll = new EGuiScrollBar(this, height - 2, scrollableHeight, ScreenLocation.right);
		horizontalScroll = new EGuiScrollBar(this, width - 2, scrollableWidth, ScreenLocation.bot);
		
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
		
		int scale = res.getScaleFactor();
		try {
			if (checkDraw() && height > (isHScrollDrawn() ? 5 : 2) && width > (isVScrollDrawn() ? 5 : 2)) {
				GlStateManager.pushMatrix();
				GlStateManager.enableBlend();
				
				//draw list contents scissored
				GL11.glEnable(GL11.GL_SCISSOR_TEST);
				GL11.glScissor(
						((startX + 1) * scale),
						(Display.getHeight() - startY * scale) - (height - (isHScrollDrawn() ? horizontalScroll.height + 1 : 0) - 1) * scale,
						(width - (isVScrollDrawn() ? verticalScroll.width + 1 : 0) - 2) * scale,
						(height - (isHScrollDrawn() ? 6 : 2)) * scale);
				{ //scissor start
					//draw background
					drawRect(startX + 1, startY + 1, endX - 1, endY - 1, backgroundColor);
					
					//only draw the objects that are actually in the viewable area
					for (IEnhancedGuiObject o : this.drawnListObjects) {
						if (o.checkDraw()) {
							if (!o.hasFirstDraw()) { o.onFirstDraw(); o.onFirstDraw(); }
							GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
							EDimension d = o.getDimensions();
							//drawRect(d.startX, d.startY, d.endX, d.endY, 0xffff0000);
							o.drawObject(mXIn, mYIn, ticks);
						}
					}
				}
				GL11.glDisable(GL11.GL_SCISSOR_TEST);
				
				//draw non list contents as normal (non scissored)
				for (IEnhancedGuiObject o : guiObjects) {
					if (o.checkDraw() && listContents.notContains(o)) {
						if (!o.hasFirstDraw()) { o.onFirstDraw(); o.onFirstDraw(); }
	    				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	    	        	o.drawObject(mXIn, mYIn, ticks);
	    			}
				}
				
				GlStateManager.popMatrix();
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	@Override
	public void onFirstDraw() {
		super.onFirstDraw();
		updateDrawnObjects();
	}
	
	@Override
	public void move(int newX, int newY) {
		if (eventHandler != null) { eventHandler.processEvent(new EventModify(this, this, ObjectModifyType.Move)); }
		if (moveable) {
			EArrayList<IEnhancedGuiObject> objs = new EArrayList(guiObjects);
			objs.addAll(objsToBeAdded);
			Iterator<IEnhancedGuiObject> it = objs.iterator();
			while (it.hasNext()) {
				IEnhancedGuiObject o = it.next();
				if (o.isMoveable()) {
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
		if (isMoveable()) {
			EDimension d = getDimensions();
			StorageBox<Integer, Integer> loc = new StorageBox(d.startX, d.startY);
			StorageBoxHolder<IEnhancedGuiObject, StorageBox<Integer, Integer>> previousLocations = new StorageBoxHolder();
			EArrayList<IEnhancedGuiObject> objs = new EArrayList();
			objs.addAll(getObjects());
			objs.addAll(getAddingObjects());
			for (IEnhancedGuiObject o : objs) {
				previousLocations.add(o, new StorageBox(o.getDimensions().startX - loc.getObject(), o.getDimensions().startY - loc.getValue()));
			}
			setDimensions(newX, newY, d.width, d.height);
			for (IEnhancedGuiObject o : objs) {
				if (o.isMoveable()) {
					StorageBox<Integer, Integer> oldLoc = previousLocations.getBoxWithObj(o).getValue();
					o.setInitialPosition(newX + oldLoc.getObject(), newY + oldLoc.getValue());
					
					if (listContents.contains(o) || listObjsToBeAdded.contains(o)) {
						int eX = endX - (isVScrollDrawn() ? verticalScroll.width + 2 : 1);
						int eY = endY - (isHScrollDrawn() ? horizontalScroll.height - 4 : 1);
						
						EDimension bounds = new EDimension(startX + 1, startY + 1, eX, eY);
						
						o.setBoundaryEnforcer(getDimensions());
						for (IEnhancedGuiObject q : o.getObjects()) { q.setBoundaryEnforcer(bounds); }
						for (IEnhancedGuiObject q : o.getAddingObjects()) { q.setBoundaryEnforcer(bounds); }
					}
					
					o.setPosition(newX + oldLoc.getObject(), newY + oldLoc.getValue());
				}
			}
		}
		return this;
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == reset) {
			verticalScroll.reset();
			horizontalScroll.reset();
		}
		if (object == verticalScroll || object == horizontalScroll) {
			int vScrollPos = verticalScroll.getScrollPos() - verticalScroll.getVisibleAmount();
			int hScrollPos = horizontalScroll.getScrollPos() - horizontalScroll.getVisibleAmount();
			for (IEnhancedGuiObject o : EArrayList.combineLists(listContents, listObjsToBeAdded)) {
				o.setPosition(o.getInitialPosition().getObject() - hScrollPos, o.getInitialPosition().getValue() - vScrollPos);
			}
			updateDrawnObjects();
		}
	}
	
	protected void updateDrawnObjects() {
		drawnListObjects.clear();
		drawnListObjects.addAll(listContents.stream().filter(o -> objectInstance.getDimensions().contains(o.getDimensions())).collect(Collectors.toList()));
		drawnListObjects.addAll(listObjsToBeAdded.stream().filter(o -> objectInstance.getDimensions().contains(o.getDimensions())).collect(Collectors.toList()));
	}
	
	public EDimension getListDimensions() {
		int w = (endX - (isVScrollDrawn() ? verticalScroll.width + 3 : 2)) - startX;
		int h = (endY - (isHScrollDrawn() ? horizontalScroll.height - 4 : 1)) - startY - 2;
		return new EDimension(0, 0, w, h);
	}
	
	public void resetScrollPos() {
		verticalScroll.setScrollBarPos(0);
		verticalScroll.setScrollBarPos(0);
	}
	
	@Override
	public void mouseScrolled(int change) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			if (scrollableWidth - (width - 2) > 0) { horizontalScroll.setScrollBarPos(horizontalScroll.getScrollPos() - change * 18); }
		}
		else if (scrollableHeight - (height - 2) > 0) { verticalScroll.setScrollBarPos(verticalScroll.getScrollPos() - change * 18); }
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
			updateVisuals();
		} else { widthToBeSet = widthIn; }
		return this;
	}
	public EGuiScrollList setListHeight(int heightIn) {
		if (verticalScroll != null) {
			scrollableHeight = heightIn;
			verticalScroll.setHighVal(heightIn);
			updateVisuals();
		} else { heightToBeSet = heightIn; }
		return this;
	}
	public EGuiScrollList growList(int amount) {
		growListWidth(amount);
		growListHeight(amount);
		return this;
	}
	public EGuiScrollList growListWidth(int amount) {
		if (horizontalScroll != null) { setListWidth(getListWidth() + amount); updateVisuals(); }
		else { widthToBeSet += amount; }
		return this;
	}
	public EGuiScrollList growListHeight(int amount) {
		if (verticalScroll != null) { setListHeight(getListHeight() + amount); updateVisuals(); }
		else { heightToBeSet += amount; }
		return this;
	}
	
	public EGuiScrollList addObjectToList(IEnhancedGuiObject... objsIn) {
		for (IEnhancedGuiObject o : objsIn) {
			try {
				if (o != null && o != this) {
					if (o instanceof EnhancedGui) { continue; }
					
					int eX = endX - (isVScrollDrawn() ? verticalScroll.width + 2 : 2);
					int eY = endY - (isHScrollDrawn() ? horizontalScroll.height - 4 : 1);
					
					EDimension bounds = new EDimension(startX + 1, startY + 1, eX, eY);
					
					//apply offset to all added objects so their location is relative to this scrollList
					EDimension dims = o.getDimensions();
					o.setDimensions(startX + dims.startX, startY + dims.startY, dims.width, dims.height);
					
					try {
						o.setParent(this).initObjects();
						o.setZLevel(getZLevel() + 1);
						if (o instanceof WindowParent) { ((WindowParent) o).initGui(); }
						o.completeInit();
					} catch (ObjectInitException e) { e.printStackTrace(); }
					
					//limit the boundary of each object to the list's boundary
					o.setBoundaryEnforcer(bounds);
					for (IEnhancedGuiObject q : o.getObjects()) { q.setBoundaryEnforcer(bounds); }
					for (IEnhancedGuiObject q : o.getAddingObjects()) { q.setBoundaryEnforcer(bounds); }
					
					//replace the original intial position coordinates with the relative ones
					o.setInitialPosition(o.getDimensions().startX, o.getDimensions().startY);
					
					listObjsToBeAdded.add(o);
					objsToBeAdded.add(o);
				}
			} catch (Exception e) { e.printStackTrace(); }
		}
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
		if (!mouseEntered && isMouseOver(mX, mY)) { mouseEntered = true; mouseEntered(mX, mY); }
		if (mouseEntered && !isMouseOver(mX, mY)) { mouseEntered = false; mouseExited(mX, mY); }
		if (!objsToBeRemoved.isEmpty()) { StaticEGuiObject.removeObjects(this, objsToBeRemoved); }
		if (!objsToBeAdded.isEmpty()) { StaticEGuiObject.addObjects(this, objsToBeAdded); }
		if (!listObjsToBeRemoved.isEmpty()) { removeListObjects(); }
		if (!listObjsToBeAdded.isEmpty()) { addListObjects(); }
	}
	
	protected void removeListObjects() {
		for (IEnhancedGuiObject o : listObjsToBeRemoved ) {
			if (o != null) {
				Iterator it = listContents.iterator();
				while (it.hasNext()) {
					if (o.equals(it.next())) {
						if (!o.equals(getTopParent().getFocusedObject())) {
							for (IEnhancedGuiObject child : o.getObjects()) {
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
		updateDrawnObjects();
	}
	
	protected void addListObjects() {
		for (IEnhancedGuiObject o : listObjsToBeAdded) {
			listContents.add(o);
		}
		listObjsToBeAdded.clear();
		updateDrawnObjects();
	}
	
	private void updateVisuals() {
		if (isVScrollDrawn() && !isHScrollDrawn()) {
			EDimension v = verticalScroll.getDimensions();
			verticalScroll.setDimensions(v.startX, v.startY, v.width, height - 2 - (isResetDrawn() ? 4 : 0));
		}
		if (isHScrollDrawn() && isVScrollDrawn()) {
			EDimension h = horizontalScroll.getDimensions();
			horizontalScroll.setDimensions(h.startX, h.startY, width - verticalScroll.width - 3, h.height);
		}
	}
	
	public void clearList() { listContents.forEach(o -> removeObject(o)); updateDrawnObjects(); }
	
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
