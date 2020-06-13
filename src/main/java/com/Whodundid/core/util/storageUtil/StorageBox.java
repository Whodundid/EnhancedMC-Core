package com.Whodundid.core.util.storageUtil;

//Author: Hunter Bragg

public class StorageBox<Obj, Val> {
	
	private Obj storedObj;
	private Val storedVal;
	
	//constructor
	public StorageBox() { this(null, null); }
	public StorageBox(Obj objIn, Val valueIn) { storedObj = objIn; storedVal = valueIn; }
	
	public boolean contains(Object obj) {
		if (obj == null) { return storedObj == null || storedVal == null; }
		return ((storedObj != null ? storedObj.equals(obj) : false) || (storedVal != null ? storedVal.equals(obj) : false));
	}
	
	public void setValues(StorageBox<Obj, Val> boxIn) { storedObj = (boxIn != null) ? boxIn.storedObj : null; storedVal = (boxIn != null) ? boxIn.storedVal : null; }
	public void setValues(Obj obj, Val val) { storedObj = obj; storedVal = val; }
	public void setObject(Obj obj) { storedObj = obj; }	
	public void setValue(Val obj) { storedVal = obj; }
	public void clear() { storedObj = null; storedVal = null; }
	
	public Obj getObject() { return storedObj; }	
	public Val getValue() { return storedVal; }
	
	public boolean containsObject(Object obj) { return (obj == null ) ? storedObj == null : obj.equals(storedObj); }
	public boolean containsValue(Object val) { return (val == null) ? storedVal == null : val.equals(storedVal); }
	
	public boolean compare(StorageBox<?, ?> boxIn) { return compare(boxIn.getObject(), boxIn.getValue()); }
	public boolean compare(Object inObj, Object inVal) { return (storedObj.equals(inObj) && storedVal.equals(inVal)); }
	
	public static boolean compare(StorageBox<?, ?> box1, StorageBox<?, ?> box2) { return (box1 != null && box2 != null) ? box1.compare(box2) : false; }
	
	@Override public String toString() { return "[" + storedObj + ", " + storedVal + "]"; }
	
}
