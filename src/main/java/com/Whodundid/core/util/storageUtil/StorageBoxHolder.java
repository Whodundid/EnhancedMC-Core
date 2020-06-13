package com.Whodundid.core.util.storageUtil;

import com.Whodundid.core.util.EUtil;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

//Author: Hunter Bragg

/** Essentially an ArrayList implementation that holds StorageBoxes. */
public class StorageBoxHolder<Obj, Val> implements Iterable<StorageBox<Obj, Val>> {
	
	List<StorageBox<Obj, Val>> createdList = new EArrayList();
	public boolean allowDuplicates = false;
	
	/** Creates a new StorageBoxHolder that does allow duplicate object entries. */
	public StorageBoxHolder() { this(true); }
	
	/** Creates a new StorageBoxHolder in which duplicates can be allowed or not. */
	public StorageBoxHolder(boolean allowDuplicatesIn) {
		allowDuplicates = allowDuplicatesIn;
	}
	
	public StorageBoxHolder(Obj obj, Val val) {
		this(true);
		add(obj, val);
	}
	
	public StorageBoxHolder(boolean allowDuplicatesIn, Obj obj, Val val) {
		this(allowDuplicatesIn);
		add(obj, val);
	}
	
	/** Creates a new StorageBox with the given object and value and then adds it to the specified position of this holder. */
	public void add(int pos, Obj obj, Val value) {
		if (allowDuplicates || !contains(obj)) { createdList.add(pos, new StorageBox<Obj, Val>(obj, value)); }
	}
	
	public boolean add() { return add(null, null); }
	
	/** Creates a new StorageBox with the given object and value and then adds it to the end of this holder. */
	public boolean add(Obj obj, Val value) {
		return (allowDuplicates || !contains(obj)) ? createdList.add(new StorageBox<Obj, Val>(obj, value)) : false;
	}
	
	/** Adds the specified box if it is not null to this StorageBoxHolder. */
	public boolean add(StorageBox<Obj, Val> boxIn) {
		return (boxIn != null && (allowDuplicates || !contains(boxIn))) ? createdList.add(boxIn) : false;
	}
	
	/** Adds all the boxes from one StorageBoxHolder into this one. */
	public StorageBoxHolder<Obj, Val> addAll(StorageBoxHolder<Obj, Val> in) {
		if (in != null) { in.forEach(b -> add(b)); }
		return this;
	}
	
	/** Adds all of the boxes from a list into this StorageBoxHolder. */
	public StorageBoxHolder<Obj, Val> addAll(List<StorageBox<Obj, Val>> listIn) {
		if (listIn != null) { listIn.forEach(b -> add(b)); }
		return this;
	}
	
	/** Adds if the object does not already exist, or updates the value of the existing object. */
	public void put(Obj obj, Val value) {
		StorageBox<Obj, Val> box = this.getBoxWithObj(obj);
		if (box != null) { box.setValue(value); }
		else { add(obj, value); }
	}
	
	/** Removes every box that contains the given object. */
	public List<StorageBox<Obj, Val>> removeBoxesContainingObj(Obj obj) {
		List<StorageBox<Obj, Val>> returnList = new EArrayList();
		Iterator<StorageBox<Obj, Val>> i = createdList.iterator();
		while (i.hasNext()) {
			StorageBox<Obj, Val> getBox = i.next();
			if (getBox.contains(obj)) { returnList.add(getBox); i.remove(); }
		}
		return returnList;
	}
	
	/** Removes every box that has the exact object and value. */
	public List<StorageBox<Obj, Val>> removeBoxesWithSaidValues(Obj obj1, Val obj2) {
		List<StorageBox<Obj, Val>> returnList = new EArrayList();
		Iterator<StorageBox<Obj, Val>> i = createdList.iterator();
		while (i.hasNext()) {
			StorageBox<Obj, Val> getBox = i.next();
			if (getBox.compare(obj1, obj2)) { returnList.add(getBox); i.remove(); }
		}
		return returnList;
	}
	
	/** Retrieves the first box that contains the specified object */
	public StorageBox<Obj, Val> getBoxWithObj(Obj objIn) {
		for (StorageBox<Obj, Val> getBox : createdList) {
			if (getBox.containsObject(objIn)) { return getBox; }
		}
		return null;
	}
	
	/** Retrieves all boxes that contain the specified object */
	public List<StorageBox<Obj, Val>> getAllBoxesWithObj(Obj obj) {
		return createdList.stream().filter(b -> b.getObject().equals(obj)).collect(Collectors.toList());
	}
	
	/** Replaces a value in a box with the given object with the specified new value.
	 *  If the box does not exist, nothing is added and nothing is modified. */
	public StorageBoxHolder<Obj, Val> setValueInBox(Obj obj, Val newVal) {
		StorageBox<Obj, Val> box = getBoxWithObj(obj);
		if (box != null) { box.setValue(newVal); }
		return this;
	}
	
	/** Replaces an in a box with the given object with the specified new object.
	 *  If the box does not exist, nothing is added and nothing is modified. */
	public StorageBoxHolder<Obj, Val> setObjectInBox(Obj obj, Obj newObj) {
		StorageBox<Obj, Val> box = getBoxWithObj(obj);
		if (box != null) { box.setObject(newObj); }
		return this;
	}
	
	/** Returns true if this holder has any box with the specified object. */
	public boolean contains(Obj obj) {
		for (StorageBox<Obj, Val> getBox : createdList) {
			if (getBox.contains(obj)) { return true; }
		}
		return false;
	}
	
	public boolean contains(StorageBox<Obj, Val> boxIn) {
		for (StorageBox<Obj, Val> b : createdList) {
			if (b.compare(boxIn)) { return true; }
		}
		return false;
	}
	
	/** Returns true if this holder has any box with both the specified object and value pair. */
	public boolean containsABoxWithBoth(Obj obj1, Val obj2) {
		for (StorageBox<Obj, Val> getBox : createdList) {
			if (getBox.compare(obj1, obj2)) { return true; }
		}
		return false;
	}
	
	/** Returns a list of every object in each box. */
	public List<Obj> getObjects() {
		return createdList.stream().map(StorageBox::getObject).collect(Collectors.toList());
	}
	
	/** Returns a list of every value in each box. */
	public List<Val> getValues() {
		return createdList.stream().map(StorageBox::getValue).collect(Collectors.toList());
	}
	
	/** Returns a list containing every box in this holder. */
	public EArrayList<StorageBox<Obj, Val>> getBoxes() {
		return new EArrayList<StorageBox<Obj, Val>>(createdList);
	}
	
	/** Returns the boxes of this holder within an array of StorageBox objects with the corresponding parameters. */
	public StorageBox<Obj, Val>[] getBoxesAsArray() {
		return (StorageBox<Obj, Val>[]) EUtil.asArray(getBoxes().toArray(new StorageBox[0]));
	}
	
	/** Returns the value of a box with the specified object. */
	public Val getValueInBox(Obj objIn) {
		StorageBox<Obj, Val> b = getBoxWithObj(objIn);
		return b != null ? b.getValue() : null;
	}
	
	//static methods
	
	/** Static method used to create a new StorageBoxHolder parametized with the given object and value types for each list.
	 *  If values are to be passed, they must be passed in list objects, and each list must have the same size. If both lists
	 *  are null, an empty parametized holder with be returned. If one list is null and the other is not, nothing is returned. */
	public static <thing1, thing2> StorageBoxHolder<thing1, thing2> createBox(List<thing1> objectsIn, List<thing2> valuesIn) {
		if (objectsIn != null && valuesIn != null) {
			if (objectsIn.size() == valuesIn.size()) {
				StorageBoxHolder<thing1, thing2> newHolder = new StorageBoxHolder();
				for (int i = 0; i < objectsIn.size(); i++) { newHolder.add(objectsIn.get(i), valuesIn.get(i)); }
				return newHolder;
			}
		}
		else if (objectsIn == null && valuesIn == null) {
			return new StorageBoxHolder<thing1, thing2>();
		}
		return null;
	}
	
	/** Static method used to create a new StorageBoxHolder parametized with the given object and value types.
	 *  A variable sized list of argument is passed to initialize the values of this holder. For every argument
	 *  passed, a corresponding value must also be passed along with it so that is adheres to the <Object, Value>
	 *  relationship. */
	public static <T, V> StorageBoxHolder<T, V> of(Class<T> tType, Class<V> vType, Object... dataIn) {
		if (dataIn.length % 2 == 0) {
			StorageBoxHolder<T, V> newHolder = new StorageBoxHolder();
			for (int i = 0; i < dataIn.length; i += 2) { newHolder.add((T) dataIn[i], (V) dataIn[i + 1]); }
			return newHolder;
		}
		return null;
	}
	
	/** Sets this box to not have duplicates and procedes to purge any and all duplicates from this holder. */
	public StorageBoxHolder<Obj, Val> noDuplicates() { allowDuplicates = false; purgeDuplicates(this); return this; }
	
	/** Sets this box to have duplicates or not. If no, all duplicates are purged from this holder. */
	public StorageBoxHolder<Obj, Val> setAllowDuplicates(boolean val) { allowDuplicates = val; if (!allowDuplicates) { purgeDuplicates(this); } return this; }
	
	/** Internal function used to remove duplicates from a specified holder. */
	private static void purgeDuplicates(StorageBoxHolder holderIn) {
		if (holderIn != null) {
			EArrayList<StorageBox> noDups = new EArrayList();
			
			Iterator<StorageBox> it = holderIn.iterator();
			while (it.hasNext()) {
				StorageBox box = it.next();
				if (box != null) {
					boolean contains = false;
					for (StorageBox b : noDups) { if (StorageBox.compare(box, b)) { contains = true; break; } }
					if (!contains) { noDups.add(box); }
				}
				it.remove();
			}
			
			holderIn.addAll(noDups);
		}
	}
	
	/** Returns the total number of boxes in this holder. */
	public int size() { return this.createdList.size(); }
	
	/** Returns the box at the specified point number. */
	public StorageBox<Obj, Val> get(int pointNumber) { return createdList.get(pointNumber); }
	
	/** Returns true if this holder does not contain any boxes. */
	public boolean isEmpty() { return createdList.isEmpty(); }
	
	/** Returns true if this holder does contain boxes. */
	public boolean isNotEmpty() { return !createdList.isEmpty(); }
	
	/** Removes every box, along with the contents of each box, from this holder. */
	public void clear() { this.createdList.clear(); }
	
	/** Removes the box at the specified point number. */
	public boolean remove(int pointNumber) { return createdList.remove(pointNumber) != null; }
	
	/** Returns the object from the box at the specified point number. */
	public Obj getObject(int pointNumber) { return createdList.get(pointNumber).getObject(); }
	
	/** Returns the value from the box at the specified point number. */
	public Val getValue(int pointNumber) { return createdList.get(pointNumber).getValue(); }
	
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
