package com.Whodundid.core.util.storageUtil;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

//Last edited: Jan 23, 2019
//First Added: May 14, 2018
//Author: Hunter Bragg

public class StorageBoxHolder<Obj, Val> implements Iterable<StorageBox<Obj, Val>> {
	
	List<StorageBox<Obj, Val>> createdList = new EArrayList();
	public boolean allowDuplicates = false;
	
	public StorageBoxHolder() { this(false); }
	public StorageBoxHolder(boolean allowDuplicatesIn) {
		allowDuplicates = allowDuplicatesIn;
	}
	
	public void add(int pos, Obj obj, Val value) {
		if (allowDuplicates || !contains(obj)) { createdList.add(pos, new StorageBox<Obj, Val>(obj, value)); }
	}
	
	public boolean add(Obj obj, Val value) {
		return (allowDuplicates || !contains(obj)) ? createdList.add(new StorageBox<Obj, Val>(obj, value)) : false;
	}
	
	public StorageBoxHolder<Obj, Val> addAll(StorageBoxHolder<Obj, Val> in) {
		for (StorageBox<Obj, Val> box : in) {
			add(box.getObject(), box.getValue());
		}
		return this;
	}
	
	public void put(Obj obj, Val value) {
		StorageBox<Obj, Val> box = this.getBoxWithObj(obj);
		if (box != null) { box.setValue(value); }
		else { add(obj, value); }
	}
	
	public List<StorageBox<Obj, Val>> removeBoxesContainingObj(Obj obj) {
		List<StorageBox<Obj, Val>> returnList = new EArrayList();
		Iterator<StorageBox<Obj, Val>> i = createdList.iterator();
		while (i.hasNext()) {
			StorageBox<Obj, Val> getBox = i.next();
			if (getBox.contains(obj)) { returnList.add(getBox); i.remove(); }
		}
		return returnList;
	}
	
	public List<StorageBox<Obj, Val>> removeBoxesWithSaidValues(Obj obj1, Val obj2) {
		List<StorageBox<Obj, Val>> returnList = new EArrayList();
		Iterator<StorageBox<Obj, Val>> i = createdList.iterator();
		while (i.hasNext()) {
			StorageBox<Obj, Val> getBox = i.next();
			if (getBox.compareContents(obj1, obj2)) { returnList.add(getBox); i.remove(); }
		}
		return returnList;
	}
	
	/** Retrieves the first box that contains the specified object */
	public StorageBox<Obj, Val> getBoxWithObj(Obj objIn) {
		for (StorageBox<Obj, Val> getBox : createdList) {
			if (getBox.compareObject(objIn)) { return getBox; }
		}
		return null;
	}
	
	/** Retrieves all boxes that contain the specified object */
	public List<StorageBox<Obj, Val>> getAllBoxesWithObj(Obj obj) {
		return createdList.stream().filter(b -> b.getObject().equals(obj)).collect(Collectors.toList());
	}
	
	public StorageBoxHolder<Obj, Val> setValueInBox(Obj obj, Val newVal) {
		StorageBox<Obj, Val> box = getBoxWithObj(obj);
		if (box != null) { box.setValue(newVal); }
		return this;
	}
	
	public StorageBoxHolder<Obj, Val> setObjectInBox(Obj obj, Obj newObj) {
		StorageBox<Obj, Val> box = getBoxWithObj(obj);
		if (box != null) { box.setObject(newObj); }
		return this;
	}
	
	public boolean contains(Obj obj) {
		for (StorageBox<Obj, Val> getBox : createdList) {
			if (getBox.contains(obj)) { return true; }
		}
		return false;
	}
	
	public boolean containsABoxWithBoth(Obj obj1, Val obj2) {
		for (StorageBox<Obj, Val> getBox : createdList) {
			if (getBox.compareContents(obj1, obj2)) { return true; }
		}
		return false;
	}
	
	public StorageBoxHolder<Obj, Val> noDuplicates() { allowDuplicates = false; return this; }
	public StorageBoxHolder<Obj, Val> setAllowDuplicates(boolean val) { allowDuplicates = val; return this; }
	
	public List<Obj> getObjects() {
		EArrayList<Obj> objects = new EArrayList();
		createdList.forEach(b -> objects.add(b.getObject()));
		return objects;
	}
	
	public List<Val> getValues() {
		EArrayList<Val> values = new EArrayList();
		createdList.forEach(b -> values.add(b.getValue()));
		return values;
	}
	
	public EArrayList<StorageBox<Obj, Val>> getBoxes() {
		EArrayList<StorageBox<Obj, Val>> boxes = new EArrayList(createdList);
		return boxes;
	}
	
	public int size() { return this.createdList.size(); }
	public StorageBox<Obj, Val> get(int pointNumber) { return createdList.get(pointNumber); }
	public boolean isEmpty() { return createdList.isEmpty(); }
	public boolean isNotEmpty() { return !createdList.isEmpty(); }
	public void clear() { this.createdList.clear(); }
	public boolean remove(int pointNumber) { return createdList.remove(pointNumber) != null; }
	public Obj getObject(int pointNumber) { return createdList.get(pointNumber).getObject(); }
	public Val getValue(int pointNumber) { return createdList.get(pointNumber).getValue(); }
	
	public Val getValueInBox(Obj objIn) {
		StorageBox<Obj, Val> b = getBoxWithObj(objIn);
		return b != null ? b.getValue() : null;
	}
	
	//static methods
	
	public static <thing1, thing2> StorageBoxHolder<thing1, thing2> createBox(List<thing1> objectsIn, List<thing2> valuesIn) {
		if (objectsIn != null && valuesIn != null) {
			if (objectsIn.size() == valuesIn.size()) {
				StorageBoxHolder<thing1, thing2> newHolder = new StorageBoxHolder();
				for (int i = 0; i < objectsIn.size(); i++) { newHolder.add(objectsIn.get(i), valuesIn.get(i)); }
				return newHolder;
			}
		}
		return null;
	}
	
	public static <T, V> StorageBoxHolder<T, V> createBox(int size, Object... dataIn) {
		if (size % 2 == 0) {
			StorageBoxHolder<T, V> newHolder = new StorageBoxHolder();
			for (int i = 0; i < size; i++) { newHolder.add((T) dataIn[i], (V) dataIn[i + 1]); }
			return newHolder;
		}
		return null;
	}
	
	//object overrides
	
	@Override public Iterator<StorageBox<Obj, Val>> iterator() { return createdList.iterator(); }
	
	@Override
	public String toString() {
		String returnString = "[";
		for (int i = 0; i < createdList.size(); i++) {
			returnString += ("(" + getObject(i) + ", " + getValue(i) + (i == createdList.size() - 1 ? ")" : "), "));
		}
		returnString += "]";
		return returnString;
	}
}
