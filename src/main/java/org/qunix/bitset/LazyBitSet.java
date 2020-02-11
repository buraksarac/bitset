package org.qunix.bitset;

import java.util.Collection;

/**
 *
 * Lazy bit set using another bitset as backed array and for any
 * write/update/delete and it updates backed one, for read operations it returns
 * its actaul value, to make changes effective call {@link LazyBitSet#merge()}
 * method so this set will be same as backed, use diff method to retrieve
 * changes
 *
 * @author bsarac types 2019-11-25 13:44:13 +0100
 */
public class LazyBitSet extends BitSet implements IBitSet {

	private BitSet backed;

	/**
	 * 
	 * Returns an empty bitset with default capacity
	 * 
	 * @param capacity constructor param
	 */
	public LazyBitSet() {
		super();
		backed = new BitSet();
	}

	/**
	 * 
	 * Returns an empty bitset with given capacity
	 * 
	 * @param capacity constructor param
	 */
	public LazyBitSet(int capacity) {
		super(capacity);
		backed = new BitSet(capacity);
	}

	/**
	 * @param bitSet constructor param
	 */
	public LazyBitSet(BitSet bitSet) {
		super(bitSet.capacity);
		backed = bitSet;
	}

	/**
	 *
	 * merge method: Merges all pending changes and updates its size according to
	 * changes
	 *
	 * 
	 *
	 * 
	 */
	public void merge() {
		for (int i = 0; i < actualCapacity; i++) {
			this.bucket[i] = this.backed.bucket[i];
		}
		this.size = this.backed.size;
		this.actualCapacity = this.backed.actualCapacity;
		this.capacity = this.backed.capacity;
	}

	@Override
	public Boolean flip(int p) {
		return backed.flip(p);
	}

	@Override
	public Boolean onOff(int p, boolean on) {
		return backed.onOff(p, on);
	}

	@Override
	public Boolean on(int p) {
		return backed.on(p);
	}

	@Override
	public void on(int startInclusive, int offset) {
		backed.on(startInclusive, offset);
	}

	@Override
	public Boolean off(int p) {
		return backed.off(p);
	}

	@Override
	public void off(int startInclusive, int offset) {
		backed.off(startInclusive, offset);
	}

	@Override
	public boolean add(boolean e) {
		return backed.add(e);
	}

	@Override
	public void reset() {
		backed.reset();
	}

	@Override
	public void addAll(Collection<? extends Boolean> c) {
		backed.addAll(c);
	}

	@Override
	public void allOff() {
		backed.allOff();
	}

	@Override
	public void allOn() {
		backed.allOn();
	}

	@Override
	public void remove(int index) {
		backed.remove(index);
	}
	
	@Override
	public void resize(int newSize) {
		backed.resize(newSize);
	}
	
	@Override
	public int size() {
		return backed.size;
	}

	/**
	 *
	 * diff method: returns an array containing pending changes
	 * 
	 *  the value 0 if x == y; a value less than 0 if !x && y; and a value greater than 0 if x && !y
	 *
	 * 
	 *
	 *
	 * @return int[]
	 */
	public int[] diff() {
		int[] diff = new int[this.size];
		for (int i = 0; i < size; i++) {
			diff[i] = Boolean.compare(this.get(i), backed.get(i));
		}
		return diff;
	}

}
