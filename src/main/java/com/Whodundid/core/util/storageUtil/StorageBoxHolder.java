package com.Whodundid.core.util.storageUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//Last edited: Jan 23, 2019
//First Added: May 14, 2018
//Author: Hunter Bragg

public class StorageBoxHolder<Obj, Val> implements Iterable<StorageBox<Obj, Val>> {
	
	List<StorageBox<Obj, Val>> createdList = new ArrayList();
	public boolean allowDuplicates = false;
	
	public synchronized int size() { return this.createdList.size(); }
	public synchronized StorageBox<Obj, Val> get(int pointNumber) { return createdList.get(pointNumber); }
	public synchronized boolean isEmpty() { return createdList.isEmpty(); }
	public synchronized boolean isNotEmpty() { return !createdList.isEmpty(); }
	public synchronized void clear() { this.createdList.clear(); }
	public synchronized boolean remove(int pointNumber) { return createdList.remove(pointNumber) != null; }
	public synchronized Obj getObject(int pointNumber) { return createdList.get(pointNumber).getObject(); }
	public synchronized Val getValue(int pointNumber) { return createdList.get(pointNumber).getValue(); }
	
	@Override public Iterator<StorageBox<Obj, Val>> iterator() { return createdList.iterator(); }
	
	public synchronized boolean add(Obj obj, Val value) {
		return (allowDuplicates || !contains(obj)) ? createdList.add(new StorageBox<Obj, Val>(obj, value)) : false;
	}
	
	public synchronized void put(Obj obj, Val value) {
		StorageBox<Obj, Val> box = this.getBoxWithObj(obj);
		if (box != null) { box.setValue(value); }
		else { this.add(obj, value); }
	}
	
	public synchronized List<StorageBox<Obj, Val>> removeBoxesContainingObj(Obj obj) {
		List<StorageBox<Obj, Val>> returnList = new ArrayList();
		Iterator<StorageBox<Obj, Val>> i = createdList.iterator();
		while (i.hasNext()) {
			StorageBox<Obj, Val> getBox = i.next();
			if (getBox.contains(obj)) { returnList.add(getBox); i.remove(); }
		}
		return returnList;
	}
	
	public synchronized List<StorageBox<Obj, Val>> removeBoxesWithSaidValues(Obj obj1, Val obj2) {
		List<StorageBox<Obj, Val>> returnList = new ArrayList();
		Iterator<StorageBox<Obj, Val>> i = createdList.iterator();
		while (i.hasNext()) {
			StorageBox<Obj, Val> getBox = i.next();
			if (getBox.compareContents(obj1, obj2)) { returnList.add(getBox); i.remove(); }
		}
		return returnList;
	}
	
	/** Retrieves the first box that contains the specified object */
	public synchronized StorageBox<Obj, Val> getBoxWithObj(Obj obj) {
		for (StorageBox<Obj, Val> getBox : createdList) {
			if (getBox.getObject().equals(obj)) { return getBox; }
		}
		return null;
	}
	
	/** Retrieves all boxes that contain the specified object */
	public synchronized EArrayList<StorageBox<Obj, Val>> getAllBoxesWithObj(Obj obj) {
		EArrayList<StorageBox<Obj, Val>> returnList = new EArrayList();
		for (StorageBox<Obj, Val> getBox : createdList) {
			if (getBox.getObject().equals(obj)) { returnList.add(getBox); }
		}
		return returnList;
	}
	
	public synchronized StorageBoxHolder<Obj, Val> setValueInBox(Obj obj, Val newVal) {
		StorageBox<Obj, Val> box = getBoxWithObj(obj);
		if (box != null) { box.setValue(newVal); }
		return this;
	}
	
	public synchronized StorageBoxHolder<Obj, Val> setObjectInBox(Obj obj, Obj newObj) {
		StorageBox<Obj, Val> box = getBoxWithObj(obj);
		if (box != null) { box.setObject(newObj); }
		return this;
	}
	
	public synchronized boolean contains(Obj obj) {
		for (StorageBox<Obj, Val> getBox : createdList) {
			if (getBox.contains(obj)) { return true; }
		}
		return false;
	}
	
	public synchronized boolean containsABoxWithBoth(Obj obj1, Val obj2) {
		for (StorageBox<Obj, Val> getBox : createdList) {
			if (getBox.compareContents(obj1, obj2)) { return true; }
		}
		return false;
	}
	
	public StorageBoxHolder<Obj, Val> setAllowDuplicates(boolean val) { allowDuplicates = val; return this; }
	
	public static <thing1, thing2> StorageBoxHolder<thing1, thing2> createBox(EArrayList<thing1> objectsIn, EArrayList<thing2> valuesIn) {
		if (objectsIn != null && valuesIn != null) {
			if (objectsIn.size() == valuesIn.size()) {
				StorageBoxHolder<thing1, thing2> newHolder = new StorageBoxHolder();
				for (int i = 0; i < objectsIn.size(); i++) { newHolder.add(objectsIn.get(i), valuesIn.get(i)); }
				return newHolder;
			}
		}
		return null;
	}
	
	public EArrayList<Obj> getObjects() {
		EArrayList<Obj> objects = new EArrayList();
		for (StorageBox<Obj, Val> b : createdList) {
			objects.add(b.getObject());
		}
		return objects;
	}
	
	public EArrayList<Val> getValues() {
		EArrayList<Val> values = new EArrayList();
		for (StorageBox<Obj, Val> b : createdList) {
			values.add(b.getValue());
		}
		return values;
	}
	
	public EArrayList<StorageBox<Obj, Val>> getBoxes() {
		EArrayList<StorageBox<Obj, Val>> boxes = new EArrayList(createdList);
		return boxes;
	}
	
	@Override
	public String toString() {
		String returnString = "[";
		for (int i = 0; i < createdList.size(); i++) {
			returnString += (this.getValue(i) + " " + this.getObject(i));
		}
		returnString += "]";
		return returnString;
	}
}
