package com.Whodundid.core.enhancedGui.guiObjects.basicObjects;

import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.objectEvents.EventModify;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.ObjectModifyType;
import com.Whodundid.core.enhancedGui.objectExceptions.HeaderAlreadyExistsException;
import com.Whodundid.core.enhancedGui.objectExceptions.ObjectInitException;
import com.Whodundid.core.enhancedGui.types.EnhancedGui;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import java.util.Iterator;

public class EGuiContainerList extends EGuiContainer {
	
	EGuiContainer container;
	EGuiScrollList list;
	String title = "noname";
	protected EArrayList<IEnhancedGuiObject> containerContents = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> containerObjsToBeRemoved = new EArrayList();
	protected EArrayList<IEnhancedGuiObject> containerObjsToBeAdded = new EArrayList();
	protected boolean centerTitle = false;

	public EGuiContainerList(IEnhancedGuiObject parentIn, int posX, int posY, int widthIn, int heightIn, String titleIn) {
		super(parentIn, posX, posY, widthIn, heightIn);
		title = titleIn;
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
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
		super.drawObject(mXIn, mYIn, ticks);
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
	public EGuiContainerList setPosition(int newX, int newY) {
		EDimension d = getDimensions();
		StorageBox<Integer, Integer> loc = new StorageBox(d.startX, d.startY);
		StorageBoxHolder<IEnhancedGuiObject, StorageBox<Integer, Integer>> previousLocations = new StorageBoxHolder();
		EArrayList<IEnhancedGuiObject> objs = EArrayList.combineLists(getObjects(), getAddingObjects());
		for (IEnhancedGuiObject o : objs) {
			previousLocations.add(o, new StorageBox(o.getDimensions().startX - loc.getObject(), o.getDimensions().startY - loc.getValue()));
		}
		setDimensions(newX, newY, d.width, d.height);
		for (IEnhancedGuiObject o : objs) {
			if (!o.isPositionLocked()) {
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
	
	public EGuiContainerList addObjectToContainer(IEnhancedGuiObject... objsIn) {
		for (IEnhancedGuiObject o : objsIn) {
			try {
				if (o != null && o != this) {
					if (o instanceof EnhancedGui) { continue; }
					if (o instanceof EGuiHeader && hasHeader()) {
						 throw new HeaderAlreadyExistsException(getHeader());
					}
					
					EDimension bounds = new EDimension(startX + 1, startY + titleAreaHeight, endX - 1, endY - 1);
					
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
					
					containerObjsToBeAdded.add(o);
					objsToBeAdded.add(o);
				}
			} catch (HeaderAlreadyExistsException e) { e.printStackTrace(); }
		}
		return this;
	}
	
	public EGuiContainerList removeObjectFromList(IEnhancedGuiObject... objsIn) {
		containerObjsToBeRemoved.addAll(objsIn);
		objsToBeRemoved.addAll(objsIn);
		return this;
	}
	
	@Override
	public EGuiContainerList removeObject(IEnhancedGuiObject... objsIn) {
		objsToBeRemoved.addAll(objsIn);
		containerObjsToBeRemoved.addAll(objsIn);
		return this;
	}
	
	public boolean drawsTitle() { return drawTitle; }
	public boolean drawsTitleCentered() { return centerTitle; }
	
	public EGuiContainerList setDrawTitleCentered(boolean val) { centerTitle = val; return this; }
}
