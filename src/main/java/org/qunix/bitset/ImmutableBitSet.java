package org.qunix.bitset;

import java.util.Collection;

public final class ImmutableBitSet extends BitSet implements IBitSet {

	ImmutableBitSet(BitSet bitset) {
		super(bitset.size);
		System.arraycopy(bitset.bucket, 0, this.bucket, 0, this.actualCapacity);
	}

	@Override
	public Boolean flip(int p) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Boolean onOff(int p, boolean on) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Boolean on(int p) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void on(int startInclusive, int offset) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Boolean off(int p) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void off(int startInclusive, int offset) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void resize(int newSize) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void reset() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean add(boolean e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addAll(Collection<? extends Boolean> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void allOff() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void allOn() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void remove(int index) {
		throw new UnsupportedOperationException();
	}

}
