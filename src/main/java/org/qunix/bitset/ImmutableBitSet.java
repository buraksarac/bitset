package org.qunix.bitset;

import java.util.Collection;

/**
 *
 * Immutable bitset
 *
 * @author bsarac types 2019-11-25 13:43:19 +0100
 */
public final class ImmutableBitSet extends BitSet implements IBitSet {

	/**
	 * Default constructor
	 */
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
