package com.Whodundid.core.util.storageUtil;

public class StorageBox<Obj, Val> {
	
	private Obj storedObj;
	private Val storedVal;
	
	//constructor
	public StorageBox() { this(null, null); }
	public StorageBox(Obj objIn, Val valueIn) { storedObj = objIn; storedVal = valueIn; }
	
	public void setValues(StorageBox<Obj, Val> boxIn) { storedObj = (boxIn != null) ? boxIn.storedObj : null; storedVal = (boxIn != null) ? boxIn.storedVal : null; }
	public void setValues(Obj obj, Val val) { storedObj = obj; storedVal = val; }
	public void setObject(Obj obj) { storedObj = obj; }	
	public void setValue(Val obj) { storedVal = obj; }	
	public Obj getObject() { return storedObj; }	
	public Val getValue() { return storedVal; }
	public boolean contains(Object obj) {
		if (obj == null) { return storedObj == null || storedVal == null; }
		return ((storedObj != null ? storedObj.equals(obj) : false) || (storedVal != null ? storedVal.equals(obj) : false));
	}
	public void clear() { this.storedObj = null; storedVal = null; }	
	public boolean compareContents(Object inObj, Object inVal) { return (storedObj.equals(inObj) && storedVal.equals(inVal)); }
	public boolean compareContents(StorageBox<?, ?> boxIn) { return compareContents(boxIn.getObject(), boxIn.getValue()); }
	@Override public String toString() { return "[" + storedObj + ", " + storedVal + "]"; }
}
