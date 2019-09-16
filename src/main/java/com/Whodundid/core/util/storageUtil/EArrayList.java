package com.Whodundid.core.util.storageUtil;

import java.lang.reflect.Field;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

//Last edited: Dec 12, 2018
//First Added: Dec 12, 2018
//Author: Hunter Bragg

public class EArrayList<E> extends AbstractList<E> {
	
	protected transient Object[] elementData;
	private int size;
	private boolean allowDuplicates = true;
	protected static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
	protected static final Object[] EMPTY_ELEMENTDATA = {};
	protected static final int DEFAULT_CAPACITY = 10;
	protected static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
	
	public EArrayList() { elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA; }
	
	public EArrayList(int initialCapacity) {
		if (initialCapacity > 0) { elementData = new Object[initialCapacity]; }
		else if (initialCapacity == 0) { elementData = EMPTY_ELEMENTDATA; }
		else { throw new IllegalArgumentException("Illegal Capacity: "+ initialCapacity); }
	}
	
	public EArrayList(Collection<? extends E> c) {
		elementData = c.toArray();
        if ((size = elementData.length) != 0) {
            if (elementData.getClass() != Object[].class) { elementData = Arrays.copyOf(elementData, size, Object[].class); }
        } else {  elementData = EMPTY_ELEMENTDATA; }
	}
	
	public EArrayList(E... objs) {
		this(objs.length);
		for (E e : objs) { add(e); }
	}
	
	/** Sets whether this list will allow duplicate entries or not. If no, the list removes duplicates. */
	public EArrayList<E> setAllowDuplicates(boolean val) {
		if (!val) {
			synchronized (this) {
				List<E> nonDuplicates = this.stream().distinct().collect(Collectors.toList());
				this.clear();
				this.addAll(nonDuplicates);
			}
		}
		return this;
	}
	
	public boolean allowsDuplicates() { return allowDuplicates; }
	
	public void trimToSize() {
        modCount++;
        if (size < elementData.length) { elementData = (size == 0) ? EMPTY_ELEMENTDATA : Arrays.copyOf(elementData, size); }
    }
	
	public void ensureCapacity(int minCapacity) {
        int minExpand = (elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA) ? 0 : DEFAULT_CAPACITY;
        if (minCapacity > minExpand) { ensureExplicitCapacity(minCapacity); }
    }
	
	private static int calculateCapacity(Object[] elementData, int minCapacity) {
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) { return Math.max(DEFAULT_CAPACITY, minCapacity); }
        return minCapacity;
    }
	
	private void ensureCapacityInternal(int minCapacity) {
        ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
    }
	
	private void ensureExplicitCapacity(int minCapacity) {
        modCount++;
        if (minCapacity - elementData.length > 0) { grow(minCapacity); }
    }
	
	private void grow(int minCapacity) {
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0) { newCapacity = minCapacity; }
        if (newCapacity - MAX_ARRAY_SIZE > 0) { newCapacity = hugeCapacity(minCapacity); }
        elementData = Arrays.copyOf(elementData, newCapacity);
    }
	
	private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) { throw new OutOfMemoryError(); }
        return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
    }
	
	public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
            	if (elementData[i]==null) { return i; }
            }
        } else {
            for (int i = 0; i < size; i++) {
            	if (o.equals(elementData[i])) { return i; }
            }
        }
        return -1;
    }
	
	public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size-1; i >= 0; i--) {
            	if (elementData[i]==null) { return i; }
            }
        } else {
            for (int i = size-1; i >= 0; i--) {
            	if (o.equals(elementData[i])) { return i; }
            }
        }
        return -1;
    }
	
	public Object clone() {
        try {
            ArrayList<?> v = (ArrayList<?>) super.clone();
            
            Class c = v.getClass();
            Field f1 = c.getDeclaredField("elementData");
            f1.setAccessible(true);
			f1.set(v, Arrays.copyOf(elementData, size));
            Field f2 = c.getDeclaredField("modCount");
            f2.setAccessible(true);
			f2.set(v, 0);
            f1.setAccessible(false);
            f2.setAccessible(false);
            
            return v;
        } catch (Exception e) { throw new InternalError(e); }
    }
	
	public <T> T[] toArray(T[] a) {
        if (a.length < size) { return (T[]) Arrays.copyOf(elementData, size, a.getClass()); }
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size) { a[size] = null; }
        return a;
    }
	
	protected E getElementData(int index) { return (E) elementData[index]; }
	
	public int size() { return size; }
	public boolean isEmpty() { return size == 0; }
	public boolean isNotEmpty() { return size > 0; }
	public boolean contains(Object o) { return indexOf(o) >= 0; }
	public boolean notContains(Object o) { return !contains(o); }
	public Object[] toArray() { return Arrays.copyOf(elementData, size); }

	public E get(int index) {
        rangeCheck(index);
        return this.getElementData(index);
    }
	
	public E set(int index, E element) {
        rangeCheck(index);
        E oldValue = this.getElementData(index);
        elementData[index] = element;
        return oldValue;
    }
	
	//Add methods
	
	public EArrayList add(E... e) {
		if (e == null) { 
			if (contains(null)) {
				if (allowDuplicates) {
					addE(null);
			        return this;
				}
				return this;
			}	
			addE(null);
		}
		else {
			for (int i = 0; i < e.length; i++) { add(e[i]); }
		}
		return this;
	}
	
	public EArrayList addA(E[] e) {
		add(e);
		return this;
	}
	
	public boolean add(E e) {
		if (contains(e)) {
			if (allowDuplicates) {
				addE(e);
		        return true;
			}
			return false;
		}	
		addE(e);
        return true;
    }
	
	private void addE(E e) {
		ensureCapacityInternal(size + 1);
        elementData[size++] = e;
	}
	
	public void add(int index, E element) {
		if (contains(element)) {
			if (allowDuplicates) { addIE(index, element); }
			return;
		}
		addIE(index, element);
    }
	
	private void addIE(int i, E e) {
		rangeCheckForAdd(i);
		ensureCapacityInternal(size + 1);
		System.arraycopy(elementData, i, elementData, i + 1, size - i);
		elementData[i] = e;
		size++;
	}
	
	public void addIfNotContains(E... e) {
		for (E entry : e) {
			if (!contains(entry)) { add(entry); }
		}
	}
	
	public void addIfNotNullAndNotContains(E... e) {
		for (E entry : e) {
			if (entry != null && !contains(entry)) { add(entry); }
		}
	}
	
	public EArrayList addAll(E... e) {
		if (e != null) { for (E entry : e) { add(entry); } }
		return this;
	}
	
	public boolean addAll(Collection<? extends E> c) {
		if (c != null) {
			Object[] a = c.toArray();
	        int numNew = a.length;
	        ensureCapacityInternal(size + numNew);  // Increments modCount
	        System.arraycopy(a, 0, elementData, size, numNew);
	        size += numNew;
	        return numNew != 0;
		}
		return false;
    }
	
	public boolean addAll(int index, Collection<? extends E> c) {
		if (c != null) {
			rangeCheckForAdd(index);
	        Object[] a = c.toArray();
	        int numNew = a.length;
	        ensureCapacityInternal(size + numNew);
	        int numMoved = size - index;
	        if (numMoved > 0) { System.arraycopy(elementData, index, elementData, index + numNew, numMoved); }
	        System.arraycopy(a, 0, elementData, index, numNew);
	        size += numNew;
	        return numNew != 0;
		}
		return false;
    }
	
	//Remove methods
	
	public E remove(int index) {
        rangeCheck(index);
        modCount++;
        E oldValue = getElementData(index);
        int numMoved = size - index - 1;
        if (numMoved > 0) { System.arraycopy(elementData, index+1, elementData, index, numMoved); }
        elementData[--size] = null;
        return oldValue;
    }
	
	public boolean remove(Object o) {
        if (o == null) {
            for (int index = 0; index < size; index++)
                if (elementData[index] == null) {
                    fastRemove(index);
                    return true;
                }
        } else {
            for (int index = 0; index < size; index++)
                if (o.equals(elementData[index])) {
                    fastRemove(index);
                    return true;
                }
        }
        return false;
    }
	
	public void removeIfPresent(E... e) {
		for (E entry : e) {
			if (contains(entry)) { remove(entry); }
		}
	}
	
	public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        return batchRemove(c, false);
    }
	
	public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        return batchRemove(c, true);
    }

    private boolean batchRemove(Collection<?> c, boolean complement) {
        final Object[] elementData = this.elementData;
        int r = 0, w = 0;
        boolean modified = false;
        try {
            for (; r < size; r++) { if (c.contains(elementData[r]) == complement) { elementData[w++] = elementData[r]; } }
        } finally {
            if (r != size) {
                System.arraycopy(elementData, r, elementData, w, size - r);
                w += size - r;
            }
            if (w != size) {
                for (int i = w; i < size; i++) { elementData[i] = null; }
                modCount += size - w;
                size = w;
                modified = true;
            }
        }
        return modified;
    }
	
	private void fastRemove(int index) {
        modCount++;
        int numMoved = size - index - 1;
        if (numMoved > 0) { System.arraycopy(elementData, index+1, elementData, index, numMoved); }
        elementData[--size] = null;
    }
	
	public void clear() {
        modCount++;
        for (int i = 0; i < size; i++) { elementData[i] = null; }
        size = 0;
    }
	
	protected void removeRange(int fromIndex, int toIndex) {
        modCount++;
        int numMoved = size - toIndex;
        System.arraycopy(elementData, toIndex, elementData, fromIndex, numMoved);
        int newSize = size - (toIndex-fromIndex);
        for (int i = newSize; i < size; i++) { elementData[i] = null; }
        size = newSize;
    }
	
	private void rangeCheck(int index) {
        if (index >= size) { throw new IndexOutOfBoundsException(outOfBoundsMsg(index)); }
    }
	
	private void rangeCheckForAdd(int index) {
        if (index > size || index < 0) { throw new IndexOutOfBoundsException(outOfBoundsMsg(index)); }
    }
	
	private String outOfBoundsMsg(int index) { return "Index: " + index + ", Size: " + size; }
    
    private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
    	int expectedModCount = modCount;
        s.defaultWriteObject();
        s.writeInt(size);
        for (int i = 0; i < size; i++) { s.writeObject(elementData[i]); }
        if (modCount != expectedModCount) { throw new ConcurrentModificationException(); }
    }
    
    private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
    	elementData = EMPTY_ELEMENTDATA;
        s.defaultReadObject();
        s.readInt();
        if (size > 0) {
            int capacity = calculateCapacity(elementData, size);
            ensureCapacityInternal(size);
            Object[] a = elementData;
            for (int i = 0; i < size; i++) { a[i] = s.readObject(); }
        }
    }
    
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size) { throw new IndexOutOfBoundsException("Index: " + index); }
        return new ListItr(index);
    }
    
    public ListIterator<E> listIterator() { return new ListItr(0); }
    public Iterator<E> iterator() { return new Itr(); }
    
    private class Itr implements Iterator<E> {
        int cursor, lastRet = -1;
        int expectedModCount = modCount;

        Itr() {}

        public boolean hasNext() { return cursor != size; }

        public E next() {
            checkForComodification();
            int i = cursor;
            if (i >= size) { throw new NoSuchElementException(); }
            Object[] elementData = EArrayList.this.elementData;
            if (i >= elementData.length) { throw new ConcurrentModificationException(); }
            cursor = i + 1;
            return (E) elementData[lastRet = i];
        }

        public void remove() {
            if (lastRet < 0) { throw new IllegalStateException(); }
            checkForComodification();
            try {
                EArrayList.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) { throw new ConcurrentModificationException(); }
        }

        @Override
        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            final int size = EArrayList.this.size;
            int i = cursor;
            if (i >= size) { return; }
            final Object[] elementData = EArrayList.this.elementData;
            if (i >= elementData.length) { throw new ConcurrentModificationException(); }
            while (i != size && modCount == expectedModCount) { consumer.accept((E) elementData[i++]); }
            cursor = i;
            lastRet = i - 1;
            checkForComodification();
        }

        final void checkForComodification() {
            if (modCount != expectedModCount) { throw new ConcurrentModificationException(); }
        }
    }
    
    private class ListItr extends Itr implements ListIterator<E> {
        ListItr(int index) {
            super();
            cursor = index;
        }

        public boolean hasPrevious() { return cursor != 0; }
        public int nextIndex() { return cursor; }
        public int previousIndex() { return cursor - 1; }

        public E previous() {
            checkForComodification();
            int i = cursor - 1;
            if (i < 0) { throw new NoSuchElementException(); }
            Object[] elementData = EArrayList.this.elementData;
            if (i >= elementData.length) { throw new ConcurrentModificationException(); }
            cursor = i;
            return (E) elementData[lastRet = i];
        }

        public void set(E e) {
            if (lastRet < 0) { throw new IllegalStateException(); }
            checkForComodification();
            try {
                EArrayList.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) { throw new ConcurrentModificationException(); }
        }

        public void add(E e) {
            checkForComodification();
            try {
                int i = cursor;
                EArrayList.this.add(i, e);
                cursor = i + 1;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) { throw new ConcurrentModificationException(); }
        }
    }
    
    public List<E> subList(int fromIndex, int toIndex) {
        subListRangeCheck(fromIndex, toIndex, size);
        return new SubList(this, 0, fromIndex, toIndex);
    }

    static void subListRangeCheck(int fromIndex, int toIndex, int size) {
        if (fromIndex < 0) { throw new IndexOutOfBoundsException("fromIndex = " + fromIndex); }
        if (toIndex > size) { throw new IndexOutOfBoundsException("toIndex = " + toIndex); }
        if (fromIndex > toIndex) { throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")"); }
    }

    private class SubList extends AbstractList<E> implements RandomAccess {
        private final AbstractList<E> parent;
        private final int parentOffset;
        private final int offset;
        int size;

        SubList(AbstractList<E> parent, int offset, int fromIndex, int toIndex) {
            this.parent = parent;
            this.parentOffset = fromIndex;
            this.offset = offset + fromIndex;
            this.size = toIndex - fromIndex;
            this.modCount = EArrayList.this.modCount;
        }

        public E set(int index, E e) {
            rangeCheck(index);
            checkForComodification();
            E oldValue = EArrayList.this.getElementData(offset + index);
            EArrayList.this.elementData[offset + index] = e;
            return oldValue;
        }

        public E get(int index) {
            rangeCheck(index);
            checkForComodification();
            return EArrayList.this.getElementData(offset + index);
        }

        public int size() {
            checkForComodification();
            return this.size;
        }

        public void add(int index, E e) {
            rangeCheckForAdd(index);
            checkForComodification();
            parent.add(parentOffset + index, e);
            try {
            	Field f = parent.getClass().getDeclaredField("modCount");
                f.setAccessible(true);
                this.modCount = f.getInt(parent);
                f.setAccessible(false);
            } catch (Exception q) { q.printStackTrace(); }
            this.size++;
        }

        public E remove(int index) {
            rangeCheck(index);
            checkForComodification();
            E result = parent.remove(parentOffset + index);
            try {
            	Field f = parent.getClass().getDeclaredField("modCount");
                f.setAccessible(true);
                this.modCount = f.getInt(parent);
                f.setAccessible(false);
            } catch (Exception q) { q.printStackTrace(); }
            this.size--;
            return result;
        }

        protected void removeRange(int fromIndex, int toIndex) {
            checkForComodification();
            ListIterator<E> it = listIterator(parentOffset + fromIndex);
            for (int i = 0, n = (parentOffset + toIndex) - (parentOffset + fromIndex); i < n; i++) {
            	it.next();
            	it.remove();
            }
            try {
            	Field f = parent.getClass().getDeclaredField("modCount");
                f.setAccessible(true);
                this.modCount = f.getInt(parent);
                f.setAccessible(false);
            } catch (Exception q) { q.printStackTrace(); }
            this.size -= toIndex - fromIndex;
        }

        public boolean addAll(Collection<? extends E> c) { return addAll(this.size, c); }

        public boolean addAll(int index, Collection<? extends E> c) {
            rangeCheckForAdd(index);
            int cSize = c.size();
            if (cSize == 0) { return false; }
            checkForComodification();
            parent.addAll(parentOffset + index, c);
            try {
            	Field f = parent.getClass().getDeclaredField("modCount");
                f.setAccessible(true);
                this.modCount = f.getInt(parent);
                f.setAccessible(false);
            } catch (Exception q) { q.printStackTrace(); }
            this.size += cSize;
            return true;
        }

        public Iterator<E> iterator() { return listIterator(); }

        public ListIterator<E> listIterator(final int index) {
            checkForComodification();
            rangeCheckForAdd(index);
            final int offset = this.offset;

            return new ListIterator<E>() {
                int cursor = index;
                int lastRet = -1;
                int expectedModCount = EArrayList.this.modCount;

                public boolean hasNext() { return cursor != SubList.this.size; }

                public E next() {
                    checkForComodification();
                    int i = cursor;
                    if (i >= SubList.this.size) { throw new NoSuchElementException(); }
                    Object[] elementData = EArrayList.this.elementData;
                    if (offset + i >= elementData.length) { throw new ConcurrentModificationException(); }
                    cursor = i + 1;
                    return (E) elementData[offset + (lastRet = i)];
                }

                public E previous() {
                    checkForComodification();
                    int i = cursor - 1;
                    if (i < 0) { throw new NoSuchElementException(); }
                    Object[] elementData = EArrayList.this.elementData;
                    if (offset + i >= elementData.length) { throw new ConcurrentModificationException(); }
                    cursor = i;
                    return (E) elementData[offset + (lastRet = i)];
                }

                public void forEachRemaining(Consumer<? super E> consumer) {
                    Objects.requireNonNull(consumer);
                    final int size = SubList.this.size;
                    int i = cursor;
                    if (i >= size) { return; }
                    final Object[] elementData = EArrayList.this.elementData;
                    if (offset + i >= elementData.length) { throw new ConcurrentModificationException(); }
                    while (i != size && modCount == expectedModCount) { consumer.accept((E) elementData[offset + (i++)]); }
                    lastRet = cursor = i;
                    checkForComodification();
                }

                public void remove() {
                    if (lastRet < 0) { throw new IllegalStateException(); }
                    checkForComodification();
                    try {
                        SubList.this.remove(lastRet);
                        cursor = lastRet;
                        lastRet = -1;
                        expectedModCount = EArrayList.this.modCount;
                    } catch (IndexOutOfBoundsException ex) { throw new ConcurrentModificationException(); }
                }

                public void set(E e) {
                    if (lastRet < 0) { throw new IllegalStateException(); }
                    checkForComodification();
                    try {
                        EArrayList.this.set(offset + lastRet, e);
                    } catch (IndexOutOfBoundsException ex) { throw new ConcurrentModificationException(); }
                }

                public void add(E e) {
                    checkForComodification();
                    try {
                        int i = cursor;
                        SubList.this.add(i, e);
                        cursor = i + 1;
                        lastRet = -1;
                        expectedModCount = EArrayList.this.modCount;
                    } catch (IndexOutOfBoundsException ex) { throw new ConcurrentModificationException(); }
                }

                final void checkForComodification() {
                    if (expectedModCount != EArrayList.this.modCount) { throw new ConcurrentModificationException(); }
                }
                
                public boolean hasPrevious() { return cursor != 0; }
                public int nextIndex() { return cursor; }
                public int previousIndex() { return cursor - 1; }
            };
        }

        public List<E> subList(int fromIndex, int toIndex) {
            subListRangeCheck(fromIndex, toIndex, size);
            return new SubList(this, offset, fromIndex, toIndex);
        }

        private void rangeCheck(int index) { if (index < 0 || index >= this.size) { throw new IndexOutOfBoundsException(outOfBoundsMsg(index)); } }
        private void rangeCheckForAdd(int index) { if (index < 0 || index > this.size) { throw new IndexOutOfBoundsException(outOfBoundsMsg(index)); } }
        private String outOfBoundsMsg(int index) { return "Index: " + index + ", Size: " + this.size; }
        private void checkForComodification() { if (EArrayList.this.modCount != this.modCount) { throw new ConcurrentModificationException(); } }

        public Spliterator<E> spliterator() {
            checkForComodification();
            return new ArrayListSpliterator<E>(EArrayList.this, offset, offset + this.size, this.modCount);
        }
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        final int expectedModCount = modCount;
        final E[] elementData = (E[]) this.elementData;
        final int size = this.size;
        for (int i = 0; modCount == expectedModCount && i < size; i++) {
            action.accept(elementData[i]);
        }
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }
    
    @Override
    public Spliterator<E> spliterator() {
        return new ArrayListSpliterator(this, 0, -1, 0);
    }
    
    static final class ArrayListSpliterator<E> implements Spliterator<E> {
    	
        private final EArrayList<E> list;
        private int index, fence, expectedModCount;
        
        ArrayListSpliterator(EArrayList<E> listIn, int origin, int fenceIn, int expectedModCountIn) {
            list = listIn;
            index = origin;
            fence = fenceIn;
            expectedModCount = expectedModCountIn;
        }

        private int getFence() {
            int hi;
            EArrayList<E> lst;
            if ((hi = fence) < 0) {
                if ((lst = list) == null) { hi = fence = 0; }
                else {
                    expectedModCount = lst.modCount;
                    hi = fence = lst.size;
                }
            }
            return hi;
        }

        public ArrayListSpliterator<E> trySplit() {
            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
            return (lo >= mid) ? null : new ArrayListSpliterator<E>(list, lo, index = mid, expectedModCount);
        }

        public boolean tryAdvance(Consumer<? super E> action) {
            if (action == null) { throw new NullPointerException(); }
            int hi = getFence(), i = index;
            if (i < hi) {
                index = i + 1;
                E e = (E)list.elementData[i];
                action.accept(e);
                if (list.modCount != expectedModCount) { throw new ConcurrentModificationException(); }
                return true;
            }
            return false;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            int i, hi, mc;
            EArrayList<E> lst;
            Object[] a;
            
            if (action == null) { throw new NullPointerException(); }
            if ((lst = list) != null && (a = lst.elementData) != null) {
                if ((hi = fence) < 0) {
                    mc = lst.modCount;
                    hi = lst.size;
                } else { mc = expectedModCount; }
                if ((i = index) >= 0 && (index = hi) <= a.length) {
                    for (; i < hi; ++i) {
                        E e = (E) a[i];
                        action.accept(e);
                    }
                    if (lst.modCount == mc) { return; }
                }
            }
            throw new ConcurrentModificationException();
        }

        public long estimateSize() { return getFence() - index; }

        public int characteristics() { return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED; }
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        int removeCount = 0;
        final BitSet removeSet = new BitSet(size);
        final int expectedModCount = modCount;
        final int size = this.size;
        for (int i = 0; modCount == expectedModCount && i < size; i++) {
            final E element = (E) elementData[i];
            if (filter.test(element)) {
                removeSet.set(i);
                removeCount++;
            }
        }
        
        if (modCount != expectedModCount) { throw new ConcurrentModificationException(); }
        
        final boolean anyToRemove = removeCount > 0;
        if (anyToRemove) {
            final int newSize = size - removeCount;
            for (int i = 0, j = 0; (i < size) && (j < newSize); i++, j++) {
                i = removeSet.nextClearBit(i);
                elementData[j] = elementData[i];
            }
            for (int k = newSize; k < size; k++) { elementData[k] = null; }
            this.size = newSize;
            if (modCount != expectedModCount) { throw new ConcurrentModificationException(); }
            modCount++;
        }

        return anyToRemove;
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        Objects.requireNonNull(operator);
        final int expectedModCount = modCount;
        final int size = this.size;
        for (int i = 0; modCount == expectedModCount && i < size; i++) { elementData[i] = operator.apply((E) elementData[i]); }
        if (modCount != expectedModCount) { throw new ConcurrentModificationException(); }
        modCount++;
    }

    @Override
    public void sort(Comparator<? super E> c) {
        final int expectedModCount = modCount;
        Arrays.sort((E[]) elementData, 0, size, c);
        if (modCount != expectedModCount) { throw new ConcurrentModificationException(); }
        modCount++;
    }
}
