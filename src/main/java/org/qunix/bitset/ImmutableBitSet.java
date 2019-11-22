package org.qunix.bitset;

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
  public Boolean off(int p) {
    throw new UnsupportedOperationException();
  }
  
  @Override
	public boolean add(Boolean e) {
	  throw new UnsupportedOperationException();
	}
  
}
