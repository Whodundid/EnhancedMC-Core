package com.Whodundid.core.util.storageUtil;

import java.util.Collection;

//Last edited: Jan 26, 2019
//First Added: Jan 26, 2019
//Author: Hunter Bragg

public class CheckForChangeEArrayList<E> extends EArrayList<E> {
	
	protected boolean hasBeenChanged = false;
	
	public CheckForChangeEArrayList() { super(); }
	public CheckForChangeEArrayList(int initialCapacity) { super(initialCapacity); }
	public CheckForChangeEArrayList(Collection<? extends E> c) { super(c); }
	public CheckForChangeEArrayList(E... objs) { super(objs); }
	
	@Override
	public E set(int index, E element) {
		hasBeenChanged = true;
		return super.set(index, element);
	}
	
	@Override
	public EArrayList add(E... e) {
		hasBeenChanged = true;
		return super.add(e);
	}
	
	@Override
	public boolean add(E e) {
		hasBeenChanged = true;
        return super.add(e);
    }
	
	@Override
	public void add(int index, E element) {
		hasBeenChanged = true;
        super.add(index, element);
    }
	
	@Override
	public E remove(int index) {
		hasBeenChanged = true;
        return super.remove(index);
    }
	
	@Override
	public boolean remove(Object o) {
		hasBeenChanged = true;
        return super.remove(o);
    }
	
	@Override
	public void clear() {
		hasBeenChanged = true;
        super.clear();
    }
	
	@Override
	public EArrayList addAll(E... e) {
		hasBeenChanged = true;
		return super.addAll(e);
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		hasBeenChanged = true;
        return super.addAll(c);
    }
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		hasBeenChanged = true;
        return super.addAll(index, c);
    }
	
	@Override
	public boolean removeAll(Collection<?> c) {
		hasBeenChanged = true;
        return super.removeAll(c);
    }
	
	@Override
	public boolean retainAll(Collection<?> c) {
		hasBeenChanged = true;
        return super.retainAll(c);
    }
	
	public CheckForChangeEArrayList resetChangeFlag() { hasBeenChanged = false; return this; }
	public boolean checkForChange() { return hasBeenChanged; }
}
