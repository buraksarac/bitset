package org.qunix;

public class LazyBitSet extends BitSet implements IBitSet {

  private BitSet backed;

  public LazyBitSet(int size) {
    super(size);
    backed = new BitSet(size);
  }

  public void merge() {
    for (int i = 0; i < actualCapacity; i++) {
      this.bucket[i] = this.backed.bucket[i];
    }
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
  public Boolean off(int p) {
    return backed.off(p);
  }

  public int[] diff() {
    int[] diff = new int[this.size];
    for (int i = 0; i < size; i++) {
      diff[i] = Boolean.compare(this.get(i), backed.get(i));
    }
    return diff;
  }

}
