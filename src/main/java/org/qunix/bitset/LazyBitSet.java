package org.qunix.bitset;

import java.util.Collection;

public class LazyBitSet extends BitSet implements IBitSet {

	private BitSet backed;

	public LazyBitSet(int capacity) {
		super(capacity);
		backed = new BitSet(capacity);
	}

	public LazyBitSet(BitSet bitSet) {
		super(bitSet.capacity);
		backed = bitSet;
	}

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

	public int[] diff() {
		int[] diff = new int[this.size];
		for (int i = 0; i < size; i++) {
			diff[i] = Boolean.compare(this.get(i), backed.get(i));
		}
		return diff;
	}

}
