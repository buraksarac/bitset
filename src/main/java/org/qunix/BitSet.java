package org.qunix;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class BitSet implements Iterable<Boolean>, IBitSet, Collection<Boolean> {


  private static final int DEFAULT_CAPACITY = 64;
  private static final transient long[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

  /**
   * The maximum size of array to allocate. Some VMs reserve some header words in an array. Attempts
   * to allocate larger arrays may result in OutOfMemoryError: Requested array size exceeds VM limit
   */
  private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

  private static final int LOG_64 = 6;
  private static final int MOD = (1 << LOG_64) - 1;
  protected transient int modCount = 0;

  protected transient long[] bucket;
  protected int actualCapacity;
  protected int size;



  public BitSet() {
    this.size = DEFAULT_CAPACITY;
    this.actualCapacity = 1;
    this.bucket = new long[actualCapacity];
  }

  public BitSet(int size) {
    if (size < 0) {
      throw new IllegalArgumentException("Invalid size " + size);
    }
    this.size = size;
    this.actualCapacity = getActaulCapacity(size);
    this.bucket = new long[actualCapacity];
  }

  public Boolean get(int p) {
    return ifValidPosition(p, () -> ((bucket[p >> LOG_64] & (1L << (p & (MOD))))));
  }

  public byte getByte(int p) {
    return (byte) (get(p) ? 1 : 0);
  }

  public Boolean flip(int p) {
    return ifValidPosition(p, () -> ((bucket[p >> LOG_64] ^= (1L << (p & (MOD))))));
  }

  public Boolean onOff(int p, boolean on) {
    return on ? on(p) : off(p);
  }

  public Boolean on(int p) {
    return ifValidPosition(p, () -> ((bucket[p >> LOG_64] |= (1L << (p & (MOD))))));
  }

  public Boolean off(int p) {
    return ifValidPosition(p, () -> ((bucket[p >> LOG_64] &= ~(1L << (p & (MOD))))));
  }

  public <T> Optional<T> ifOn(int position, Supplier<T> supplier) {
    if (get(position)) {
      return Optional.of(supplier.get());
    }
    return Optional.ofNullable(null);
  }

  public <T> Optional<T> ifOff(int position, Supplier<T> supplier) {
    if (!get(position)) {
      return Optional.of(supplier.get());
    }
    return Optional.ofNullable(null);
  }

  public <T> void forEach(BiConsumer<Boolean, Integer> func) {
    for (int i = 0; i < this.size; i++) {
      func.accept((bucket[i >> LOG_64] & (1L << (i & (MOD)))) != 0, i);
    }
  }


  private Boolean ifValidPosition(int position, Supplier<Long> supplier) {
    if (position < 0 || position >= this.size) {
      throw new ArrayIndexOutOfBoundsException();
    }
    return supplier.get() != 0;
  }

  public IBitSet immutableCopy() {
    return new ImmutableBitSet(this);
  }

  public boolean allSet() {
    for (int i = 0; i < actualCapacity - 1; i++) {
      if (bucket[i] != -1) {
        return false;
      }
    }


    long val = bucket[actualCapacity - 1];
    int count = val == ~0 ? 1 << LOG_64 : 0;
    if (count == 0) {
      do {
        count++;
      } while ((val &= (val - 1)) > 0);
    }
    int totalSet = ((actualCapacity - 1) << LOG_64) + count;
    return totalSet == this.size;

  }

  @Override
  public Iterator<Boolean> iterator() {
    return new Iterator<Boolean>() {

      private int position = 0;

      @Override
      public boolean hasNext() {
        return position < (actualCapacity - 1);
      }

      @Override
      public Boolean next() {
        return get(position++);
      }
    };
  }

  @Override
  public int size() {
    return this.size;
  }

  @Override
  public boolean isEmpty() {
    return this.size != 0;
  }

  @Override
  public boolean contains(Object o) {
    if (o == null || !(o instanceof Boolean)) {
      return false;
    }
    boolean other = (boolean) o;
    return this.stream().filter(b -> b == other).findFirst().isPresent();
  }

  @Override
  public Object[] toArray() {
    return this.toArray(null);
  }

  @Override
  public <T> T[] toArray(T[] a) {
    if (this.size == 0) {
      return a;
    }
    Object[] array = a == null || a.length < this.size ? new Object[this.size] : a;

    Class<?> klazz = a.getClass().getComponentType();

    if (klazz.isPrimitive()) {
      this.forEach((b, i) -> array[i] = b ? 1 : 0);
    } else if (klazz.isAssignableFrom(Boolean.class)) {
      this.forEach((b, i) -> array[i] = b);
    } else {
      throw new UnsupportedOperationException();
    }

    return a;
  }


  @Override
  public boolean add(Boolean e) {
    if (e == null) {
      throw new NullPointerException("Can not add null value");
    }
    ensureCapacityInternal(size + 1);
    return onOff(size++, e);
  }


  @Override
  public boolean containsAll(Collection<?> c) {
    if(c == null || c.isEmpty()) {
      return false;
    }

    return true;
  }

  @Override
  public boolean addAll(Collection<? extends Boolean> c) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void clear() {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean remove(Object o) {
    // TODO Auto-generated method stub
    return false;
  }

  private static int calculateCapacity(long[] bucket, int minCapacity) {
    if (bucket == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
      return Math.max(DEFAULT_CAPACITY, minCapacity);
    }
    return minCapacity;
  }

  private void ensureCapacityInternal(int minCapacity) {
    ensureExplicitCapacity(calculateCapacity(bucket, minCapacity));
  }

  private void ensureExplicitCapacity(int minCapacity) {
    modCount++;

    // overflow-conscious code
    if (minCapacity - this.size > 0)
      grow(minCapacity);
  }

  private void grow(int minCapacity) {
    // overflow-conscious code
    int oldCapacity = this.size;
    int newCapacity = oldCapacity + (oldCapacity >> 1);
    if (newCapacity - minCapacity < 0)
      newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)
      newCapacity = hugeCapacity(minCapacity);
    actualCapacity = getActaulCapacity(newCapacity);
    // minCapacity is usually close to size, so this is a win:
    bucket = Arrays.copyOf(bucket, actualCapacity);
  }

  private static int hugeCapacity(int minCapacity) {
    if (minCapacity < 0) // overflow
      throw new OutOfMemoryError();
    return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
  }

  private static final int getActaulCapacity(int size) {
    int diff = size & MOD;
    int actualCapacity = (size - diff) >>> LOG_64;
    actualCapacity += Math.min(1, diff);
    return actualCapacity;
  }


}
