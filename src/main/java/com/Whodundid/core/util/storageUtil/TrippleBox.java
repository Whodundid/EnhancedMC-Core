package com.Whodundid.core.util.storageUtil;

public class TrippleBox<Obj1, Obj2, Obj3> {

	public Obj1 o1;
	public Obj2 o2;
	public Obj3 o3;
	
	public TrippleBox() { this(null, null, null); }
	public TrippleBox(Obj1 one, Obj2 two, Obj3 three) { set(one, two, three); }
	
	public TrippleBox set(Obj1 one, Obj2 two, Obj3 three) {
		o1 = one;
		o2 = two;
		o3 = three;
		return this;
	}
	
	@Override public String toString() { return "[" + o1 + ", " + o2 + ", " + o3 + "]"; }
	
}
