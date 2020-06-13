package com.Whodundid.core.windowLibrary.windowObjects.basicObjects;

import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.header.WindowHeader;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.scrollList.WindowScrollList;
import com.Whodundid.core.windowLibrary.windowTypes.EnhancedGui;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventModify;
import java.util.Iterator;

//Author: Hunter Bragg

public class WindowContainerList extends WindowContainer {
	
	WindowContainer container;
	WindowScrollList list;
	String title = "noname";
	protected EArrayList<IWindowObject> containerContents = new EArrayList();
	protected EArrayList<IWindowObject> containerObjsToBeRemoved = new EArrayList();
	protected EArrayList<IWindowObject> containerObjsToBeAdded = new EArrayList();
	protected boolean centerTitle = false;

	public WindowContainerList(IWindowObject parentIn, int posX, int posY, int widthIn, int heightIn, String titleIn) {
		super(parentIn, posX, posY, widthIn, heightIn);
		title = titleIn;
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		if (drawBorder) { drawRect(startX, startY, endX, endY, borderColor); } //border
		if (drawBackground) { drawRect(startX + 1, startY + 1, endX - 1, endY - 1, backgroundColor); } //inner
		if (drawTitle) {
			titleAreaHeight = height >= 18 ? 18 : height;
			int drawWidth = titleWidth + 6;
			if (useCustomWidth) { drawWidth = titleAreaWidth; }
			if (drawTitleFullWidth) { drawWidth = width - 1; }
			drawRect(startX + 1, startY + 1, startX + drawWidth + 1, startY + titleAreaHeight, titleBorderColor);
			drawRect(startX + 1, startY + 1, startX + drawWidth, startY + titleAreaHeight - 1, titleBackgroundColor);
			drawStringWithShadow(title, startX + 4, startY + 5, titleColor);
		}
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void move(int newX, int newY) {
		if (eventHandler != null) { eventHandler.processEvent(new EventModify(this, this, ObjectModifyType.Move)); }
		if (!moveable) {
			EArrayList<IWindowObject> objs = new EArrayList(windowObjects);
			objs.addAll(objsToBeAdded);
			Iterator<IWindowObject> it = objs.iterator();
			while (it.hasNext()) {
				IWindowObject o = it.next();
				if (!o.isMoveable()) {
					if (o instanceof WindowParent) {
						if (((WindowParent) o).movesWithParent()) { o.move(newX, newY); }
					}
					else {
						if (containerContents.contains(o)) { o.setInitialPosition(o.getInitialPosition().getObject() + newX, o.getInitialPosition().getValue() + newY); }
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
	public WindowContainerList setPosition(int newX, int newY) {
		EDimension d = getDimensions();
		StorageBox<Integer, Integer> loc = new StorageBox(d.startX, d.startY);
		StorageBoxHolder<IWindowObject, StorageBox<Integer, Integer>> previousLocations = new StorageBoxHolder();
		EArrayList<IWindowObject> objs = EArrayList.combineLists(getObjects(), getAddingObjects());
		for (IWindowObject o : objs) {
			previousLocations.add(o, new StorageBox(o.getDimensions().startX - loc.getObject(), o.getDimensions().startY - loc.getValue()));
		}
		setDimensions(newX, newY, d.width, d.height);
		for (IWindowObject o : objs) {
			if (!o.isMoveable()) {
				StorageBox<Integer, Integer> oldLoc = previousLocations.getBoxWithObj(o).getValue();
				o.setInitialPosition(newX + oldLoc.getObject(), newY + oldLoc.getValue());
				o.setPosition(newX + oldLoc.getObject(), newY + oldLoc.getValue());
			}
		}
		return this;
	}
	
	public EDimension getListDimensions() {
		int w = (endX - 1) + (startX + 1);
		int h = (endY - 1) + (startY + titleAreaHeight);
		return new EDimension(0, 0, w, h);
	}
	
	public WindowContainerList addObjectToContainer(IWindowObject... objsIn) {
		for (IWindowObject o : objsIn) {
			try {
				if (o != null && o != this) {
					if (o instanceof EnhancedGui) { continue; }
					if (o instanceof WindowHeader && hasHeader()) { continue; }
					
					EDimension bounds = new EDimension(startX + 1, startY + titleAreaHeight, endX - 1, endY - 1);
					
					//apply offset to all added objects so their location is relative to this scrollList
					EDimension dims = o.getDimensions();
					o.setDimensions(startX + dims.startX, startY + dims.startY, dims.width, dims.height);
					
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
					
					containerObjsToBeAdded.add(o);
					objsToBeAdded.add(o);
				}
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		return this;
	}
	
	public WindowContainerList removeObjectFromList(IWindowObject... objsIn) {
		containerObjsToBeRemoved.addAll(objsIn);
		objsToBeRemoved.addAll(objsIn);
		return this;
	}
	
	@Override
	public WindowContainerList removeObject(IWindowObject obj, IWindowObject... additional) {
		objsToBeRemoved.addAll(additional);
		containerObjsToBeRemoved.addAll(additional);
		return this;
	}
	
	public boolean drawsTitle() { return drawTitle; }
	public boolean drawsTitleCentered() { return centerTitle; }
	
	public WindowContainerList setDrawTitleCentered(boolean val) { centerTitle = val; return this; }
	
}
