package com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.scrollList;

import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.core.windowLibrary.WindowObjectS;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowScrollBar;
import com.Whodundid.core.windowLibrary.windowTypes.EnhancedGui;
import com.Whodundid.core.windowLibrary.windowTypes.WindowObject;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.ObjectEventType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventModify;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventObjects;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import java.util.Iterator;
import java.util.stream.Collectors;
import org.lwjgl.input.Keyboard;

//Author: Hunter Bragg

public class WindowScrollList extends WindowObject {
	
	protected EArrayList<IWindowObject> listContents = new EArrayList();
	protected EArrayList<IWindowObject> drawnListObjects = new EArrayList();
	protected WindowScrollBar verticalScroll, horizontalScroll;
	protected WindowButton reset;
	protected int scrollableHeight = 0;
	protected int scrollableWidth = 0;
	protected EArrayList<IWindowObject> listObjsToBeRemoved = new EArrayList();
	protected EArrayList<IWindowObject> listObjsToBeAdded = new EArrayList();
	protected EArrayList<IWindowObject> ignoreList = new EArrayList();
	protected int backgroundColor = 0xff4D4D4D;
	protected int borderColor = 0xff000000;
	protected int heightToBeSet = 0, widthToBeSet = 0;
	protected boolean vScrollVis = true;
	protected boolean hScrollVis = true;
	protected boolean resetVis = false;
	protected boolean allowScrolling = true;
	protected boolean drawListObjects = true;
	
	protected WindowScrollList() {}
	public WindowScrollList(IWindowObject parentIn, int xIn, int yIn, int widthIn, int heightIn) {
		init(parentIn, xIn, yIn, widthIn, heightIn);
		scrollableWidth = widthIn - 2;
		scrollableHeight = heightIn - 2;
	}
	
	@Override
	public void initObjects() {
		verticalScroll = new WindowScrollBar(this, height - 2, scrollableHeight, ScreenLocation.right);
		horizontalScroll = new WindowScrollBar(this, width - 2, scrollableWidth, ScreenLocation.bot);
		
		reset = new WindowButton(this, endX - 5, endY - 5, 5, 5);
		
		verticalScroll.setVisible(vScrollVis);
		horizontalScroll.setVisible(hScrollVis);
		reset.setVisible(resetVis);
		
		verticalScroll.setZLevel(getZLevel() + 3);
		horizontalScroll.setZLevel(getZLevel() + 3);

		addObject(null, verticalScroll, horizontalScroll, reset);
		
		setListHeight(heightToBeSet).setListWidth(widthToBeSet);
		
		updateVisuals();
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
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
				scissor(startX + 1, startY + 1, endX - (isVScrollDrawn() ? verticalScroll.width + 2 : 1), endY - (isHScrollDrawn() ? horizontalScroll.height + 2 : 1));
				drawRect(startX + 1, startY + 1, endX - 1, endY - 1, backgroundColor); //draw background
				
				if (drawListObjects) {
					//only draw the objects that are actually in the viewable area
					for (IWindowObject o : drawnListObjects) {
						if (o.checkDraw()) {
							if (!o.hasFirstDraw()) { o.onFirstDraw(); o.onFirstDraw(); }
							GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
							EDimension d = o.getDimensions();
							o.drawObject(mXIn, mYIn);
						}
					}
				}
				endScissor();	
				
				//draw non list contents as normal (non scissored)
				for (IWindowObject o : windowObjects) {
					if (o.checkDraw() && listContents.notContains(o)) {
						if (!o.hasFirstDraw()) { o.onFirstDraw(); o.onFirstDraw(); }
	    				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	    	        	o.drawObject(mXIn, mYIn);
	    			}
				}
				
				GlStateManager.popMatrix();
			}
		}
		catch (Exception e) { e.printStackTrace(); }
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
			EArrayList<IWindowObject> objs = new EArrayList(windowObjects);
			objs.addAll(objsToBeAdded);
			Iterator<IWindowObject> it = objs.iterator();
			while (it.hasNext()) {
				IWindowObject o = it.next();
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
	public WindowScrollList setPosition(int newX, int newY) {
		if (isMoveable()) {
			EDimension d = getDimensions();
			StorageBox<Integer, Integer> loc = new StorageBox(d.startX, d.startY);
			StorageBoxHolder<IWindowObject, StorageBox<Integer, Integer>> previousLocations = new StorageBoxHolder();
			EArrayList<IWindowObject> objs = new EArrayList();
			objs.addAll(getObjects());
			objs.addAll(getAddingObjects());
			for (IWindowObject o : objs) {
				previousLocations.add(o, new StorageBox(o.getDimensions().startX - loc.getObject(), o.getDimensions().startY - loc.getValue()));
			}
			setDimensions(newX, newY, d.width, d.height);
			for (IWindowObject o : objs) {
				if (o.isMoveable()) {
					StorageBox<Integer, Integer> oldLoc = previousLocations.getBoxWithObj(o).getValue();
					o.setInitialPosition(newX + oldLoc.getObject(), newY + oldLoc.getValue());
					
					if (listContents.contains(o) || listObjsToBeAdded.contains(o)) {
						int eX = endX - (isVScrollDrawn() ? verticalScroll.width + 2 : 1);
						int eY = endY - (isHScrollDrawn() ? horizontalScroll.height - 4 : 1);
						
						EDimension bounds = new EDimension(startX + 1, startY + 1, eX, eY);
						
						o.setBoundaryEnforcer(getDimensions());
						for (IWindowObject q : o.getObjects()) { q.setBoundaryEnforcer(bounds); }
						for (IWindowObject q : o.getAddingObjects()) { q.setBoundaryEnforcer(bounds); }
					}
					
					o.setPosition(newX + oldLoc.getObject(), newY + oldLoc.getValue());
				}
			}
		}
		return this;
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == reset) {
			verticalScroll.reset();
			horizontalScroll.reset();
		}
		if (object == verticalScroll || object == horizontalScroll) {
			int vScrollPos = verticalScroll.getScrollPos() - verticalScroll.getVisibleAmount();
			int hScrollPos = horizontalScroll.getScrollPos() - horizontalScroll.getVisibleAmount();
			for (IWindowObject o : EArrayList.combineLists(listContents, listObjsToBeAdded)) {
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
		int w = (endX - (isVScrollDrawn() ? verticalScroll.width + 3 : 1)) - startX;
		int h = (endY - (isHScrollDrawn() ? horizontalScroll.height - 4 : 1)) - startY - 2;
		return new EDimension(0, 0, w, h);
	}
	
	public void resetScrollPos() {
		verticalScroll.setScrollBarPos(0);
		verticalScroll.setScrollBarPos(0);
	}
	
	@Override
	public void mouseScrolled(int change) {
		if (allowScrolling) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				if (scrollableWidth - (width - 2) > 0) { horizontalScroll.setScrollBarPos(horizontalScroll.getScrollPos() - change * 18); }
			}
			else if (scrollableHeight - (height - 2) > 0) { verticalScroll.setScrollBarPos(verticalScroll.getScrollPos() - change * 18); }
		}
		super.mouseScrolled(change);
	}
	
	public WindowScrollList fitItemsInList() { return fitItemsInList(0, 0); }
	public WindowScrollList fitItemsInList(int overShootX, int overShootY) {
		int right = 0;
		int down = 0;
		
		//get both the current list objects and those being added
		EArrayList<IWindowObject> objs = EArrayList.combineLists(listContents, listObjsToBeAdded);
		EArrayList<IWindowObject> aObjs = new EArrayList();
		EArrayList<IWindowObject> ignored = new EArrayList(ignoreList);
		
		for (IWindowObject o : objs) {
			if (listObjsToBeRemoved.notContains(o) && ignored.notContains(o)) { aObjs.add(o); }
		}
		
		//find right
		for (IWindowObject o : aObjs) {
			EDimension od = o.getDimensions();
			if (od.endX > right) { right = od.endX; }
		}
		
		//find down
		for (IWindowObject o : aObjs) {
			EDimension od = o.getDimensions();
			if (od.endY > down) { down = od.endY; }
		}
		
		//prevent negative values
		int w = MathHelper.clamp_int((right - startX) + overShootX, 0, Integer.MAX_VALUE);
		int h = MathHelper.clamp_int((down - startY) + overShootY, 0, Integer.MAX_VALUE);
		
		setListSize(w, h);
		
		return this;
	}
	
	public WindowScrollList setListSize(int widthIn, int heightIn) {
		setListWidth(widthIn);
		setListHeight(heightIn);
		return this;
	}
	public WindowScrollList setListWidth(int widthIn) {
		if (horizontalScroll != null) {
			scrollableWidth = widthIn;
			horizontalScroll.setHighVal(widthIn);
			updateVisuals();
		}
		else { widthToBeSet = widthIn; }
		return this;
	}
	public WindowScrollList setListHeight(int heightIn) {
		if (verticalScroll != null) {
			scrollableHeight = heightIn;
			verticalScroll.setHighVal(heightIn);
			updateVisuals();
		}
		else { heightToBeSet = heightIn; }
		return this;
	}
	public WindowScrollList growList(int amount) {
		growListWidth(amount);
		growListHeight(amount);
		return this;
	}
	public WindowScrollList growListWidth(int amount) {
		if (horizontalScroll != null) { setListWidth(getListWidth() + amount); updateVisuals(); }
		else { widthToBeSet += amount; }
		return this;
	}
	public WindowScrollList growListHeight(int amount) {
		if (verticalScroll != null) { setListHeight(getListHeight() + amount); updateVisuals(); }
		else { heightToBeSet += amount; }
		return this;
	}
	
	public WindowScrollList addAndIgnore(IWindowObject... objsIn) { addToIgnoreList(objsIn); addObjectToList(objsIn); return this; }
	
	public WindowScrollList addObjectToList(IWindowObject... objsIn) { return addObjectToList(true, objsIn); }
	public WindowScrollList addObjectToList(boolean useRelativeCoords, IWindowObject... objsIn) {
		for (IWindowObject o : objsIn) {
			try {
				if (o != null && o != this) {
					if (o instanceof EnhancedGui) { continue; }
					
					int eX = endX - (isVScrollDrawn() ? verticalScroll.width + 2 : 2);
					int eY = endY - (isHScrollDrawn() ? horizontalScroll.height - 4 : 1);
					
					EDimension bounds = new EDimension(startX + 1, startY + 1, eX, eY);
					
					//apply offset to all added objects so their location is relative to this scrollList
					EDimension dims = o.getDimensions();
					if (useRelativeCoords) {
						o.setDimensions(startX + dims.startX, startY + dims.startY, dims.width, dims.height);
					}
					
					o.setParent(this).initObjects();
					o.setZLevel(getZLevel() + 1);
					if (o instanceof WindowParent) { ((WindowParent) o).initWindow(); }
					o.completeInit();
					
					//limit the boundary of each object to the list's boundary
					o.setBoundaryEnforcer(bounds);
					for (IWindowObject q : o.getObjects()) { q.setBoundaryEnforcer(bounds); }
					for (IWindowObject q : o.getAddingObjects()) { q.setBoundaryEnforcer(bounds); }
					
					//replace the original intial position coordinates with the relative ones
					o.setInitialPosition(o.getDimensions().startX, o.getDimensions().startY);
					
					listObjsToBeAdded.add(o);
					objsToBeAdded.add(o);
				}
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		return this;
	}
	
	public WindowScrollList removeObjectFromList(IWindowObject... objsIn) {
		listObjsToBeRemoved.addAll(objsIn);
		objsToBeRemoved.addAll(objsIn);
		return this;
	}
	
	@Override
	public WindowScrollList removeObject(IWindowObject obj, IWindowObject... additional) {
		objsToBeRemoved.addAll(additional);
		listObjsToBeRemoved.addAll(additional);
		return this;
	}
	
	@Override
	protected void updateBeforeNextDraw(int mXIn, int mYIn) {
		res = new ScaledResolution(mc);
		mX = mXIn; mY = mYIn;
		if (!mouseEntered && isMouseOver(mX, mY)) { mouseEntered = true; mouseEntered(mX, mY); }
		if (mouseEntered && !isMouseOver(mX, mY)) { mouseEntered = false; mouseExited(mX, mY); }
		if (!objsToBeRemoved.isEmpty()) { WindowObjectS.removeObjects(this, objsToBeRemoved); }
		if (!objsToBeAdded.isEmpty()) { WindowObjectS.addObjects(this, objsToBeAdded); }
		if (!listObjsToBeRemoved.isEmpty()) { removeListObjects(); }
		if (!listObjsToBeAdded.isEmpty()) { addListObjects(); }
	}
	
	protected void removeListObjects() {
		for (IWindowObject o : listObjsToBeRemoved ) {
			if (o != null) {
				Iterator it = listContents.iterator();
				while (it.hasNext()) {
					if (o.equals(it.next())) {
						if (!o.equals(getTopParent().getFocusedObject())) {
							for (IWindowObject child : o.getObjects()) {
								if (child.equals(getTopParent().getFocusedObject())) { child.relinquishFocus(); }
							}
						}
						else { o.relinquishFocus(); }
						o.onClosed();
						if (eventHandler != null) { eventHandler.processEvent(new EventObjects(this, o, ObjectEventType.ObjectRemoved)); }
						it.remove();
					}
				} //while
			}
		}
		listObjsToBeRemoved.clear();
		updateDrawnObjects();
	}
	
	protected void addListObjects() {
		for (IWindowObject o : listObjsToBeAdded) {
			listContents.add(o);
		}
		listObjsToBeAdded.clear();
		updateDrawnObjects();
	}
	
	protected void updateVisuals() {
		if (isVScrollDrawn() && !isHScrollDrawn()) {
			EDimension v = verticalScroll.getDimensions();
			verticalScroll.setDimensions(v.startX, v.startY, v.width, height - 2 - (isResetDrawn() ? 4 : 0));
		}
		if (isHScrollDrawn() && isVScrollDrawn()) {
			EDimension h = horizontalScroll.getDimensions();
			EDimension v = verticalScroll.getDimensions();
			
			horizontalScroll.setDimensions(h.startX, h.startY, width - verticalScroll.width - 3, h.height);
			verticalScroll.setDimensions(v.startX, v.startY, v.width, height - 2 - (isResetDrawn() ? 4 : 0));
		}
	}
	
	public void clearList() {
		drawnListObjects.clear();
		listContents.clear();
		listObjsToBeAdded.clear();
		
		reInitObjects();
	}
	
	public WindowScrollList clearIgnoreList() { ignoreList = new EArrayList(); return this; }
	public WindowScrollList setIgnoreList(IWindowObject... objects) {
		ignoreList = new EArrayList<IWindowObject>().addA(objects);
		return this;
	}
	
	public WindowScrollList addToIgnoreList(IWindowObject... objects) {
		if (ignoreList == null) { ignoreList = new EArrayList<IWindowObject>(); }
		ignoreList.addA(objects);
		return this;
	}
	
	public WindowScrollList setDrawListObjects(boolean val) { drawListObjects = val; return this; }
	public WindowScrollList setAllowScrolling(boolean val) { allowScrolling = val; return this; }
	public WindowScrollList setBackgroundColor(EColors colorIn) { return setBackgroundColor(colorIn.intVal); }
	public WindowScrollList setBackgroundColor(int colorIn) { backgroundColor = colorIn; return this; }
	public WindowScrollList setBorderColor(EColors colorIn) { return setBorderColor(colorIn.intVal); }
	public WindowScrollList setBorderColor(int colorIn) { borderColor = colorIn; return this; }
	public WindowScrollList setVScrollDrawn(boolean valIn) { vScrollVis = valIn; if (verticalScroll != null) { verticalScroll.setVisible(valIn); } updateVisuals(); return this; }
	public WindowScrollList setHScrollDrawn(boolean valIn) { hScrollVis = valIn; if (horizontalScroll != null) { horizontalScroll.setVisible(valIn); } updateVisuals(); return this; }
	public WindowScrollList setResetDrawn(boolean valIn) { resetVis = valIn; if (reset != null) { reset.setVisible(valIn); } updateVisuals(); return this; }
	public WindowScrollList renderVScrollBarThumb(boolean val) { if (verticalScroll != null) { verticalScroll.setRenderThumb(val); } else { vScrollVis = val; } return this; }
	public WindowScrollList renderHScrollBarThumb(boolean val) { if (horizontalScroll != null) { horizontalScroll.setRenderThumb(val); } else { hScrollVis = val; } return this; }
	
	public int getListHeight() { return scrollableHeight - (isHScrollDrawn() ? horizontalScroll.getDimensions().height : 0); }
	public int getListWidth() { return scrollableWidth - (isVScrollDrawn() ? verticalScroll.getDimensions().width : 0); }
	public boolean getDrawListObjects() { return drawListObjects; }
	public boolean isVScrollDrawn() { return vScrollVis && (verticalScroll != null ? verticalScroll.getHighVal() > verticalScroll.getVisibleAmount() : false); }
	public boolean isHScrollDrawn() { return hScrollVis && (horizontalScroll != null ? horizontalScroll.getHighVal() > horizontalScroll.getVisibleAmount() : false); }
	public boolean isResetDrawn() { return resetVis && (isVScrollDrawn() || isHScrollDrawn()); }
	public WindowScrollBar getVScrollBar() { return verticalScroll; }
	public WindowScrollBar getHScrollBar() { return horizontalScroll; }
	public EArrayList<IWindowObject> getDrawnObjects() { return drawnListObjects; }
	public EArrayList<IWindowObject> getListObjects() { return listContents; }
	public EArrayList<IWindowObject> getAddingListObjects() { return listObjsToBeAdded; }
	
}
