package org.qunix.bitset;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * COLLECTION IMPLEMENTATION NOT COMPLETE!!!
 * 
 * @author bsarac
 *
 */
public class BitSet implements Iterable<Boolean>, IBitSet {

	/**
	 * 
	 */
	private static final long ONE_SET = 1L;
	/**
	 * 
	 */
	private static final long NONE_SET = 0l;
	/**
	 * 
	 */
	private static final long ALL_SET = -1l;
	private static final int DEFAULT_CAPACITY = 64;
	private static final transient long[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

	/**
	 * The maximum size of array to allocate. Some VMs reserve some header words in
	 * an array. Attempts to allocate larger arrays may result in OutOfMemoryError:
	 * Requested array size exceeds VM limit
	 */
	private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

	private static final int LOG_64 = 6;
	private static final int LONG_SIZE = 1 << LOG_64;
	private static final int MOD = LONG_SIZE - 1;

	protected transient long[] bucket;
	protected int actualCapacity;
	protected int size;
	protected int capacity;

	public BitSet() {
		this.capacity = DEFAULT_CAPACITY;
		this.actualCapacity = getActaulCapacity(this.capacity);
		this.bucket = new long[actualCapacity];
	}

	public BitSet(int capacity) {
		if (capacity < 0) {
			throw new IllegalArgumentException("Invalid capacity " + capacity);
		}
		this.capacity = capacity;
		this.actualCapacity = getActaulCapacity(this.capacity);
		this.bucket = new long[actualCapacity];
	}

	public Boolean get(int p) {
		return ifValidPosition(p, () -> ((bucket[p >> LOG_64] & (ONE_SET << (p & (MOD))))));
	}

	public byte getByte(int p) {
		return (byte) (get(p) ? 1 : 0);
	}

	public Boolean flip(int p) {
		return ifValidPosition(p, () -> ((bucket[p >> LOG_64] ^= (ONE_SET << (p & (MOD))))));
	}

	public Boolean onOff(int p, boolean on) {
		return on ? on(p) : off(p);
	}

	public Boolean on(int p) {
		return ifValidPosition(p, () -> ((bucket[p >> LOG_64] |= (ONE_SET << (p & (MOD))))));
	}

	public void on(int startInclusive, int offset) {
		int endInclusive = startInclusive + offset;
		if (startInclusive < 0 || startInclusive >= this.size || endInclusive > this.size) {
			throw new IndexOutOfBoundsException();
		}
		if (offset == 0) {
			on(startInclusive);
		} else if (offset < 0) {
			on(endInclusive + 1, -offset);
		} else {
			int index, endIndex;

			if ((index = startInclusive >> LOG_64) == (endIndex = endInclusive - 1 >> LOG_64)) {

				bucket[index] |= ALL_SET >>> (LONG_SIZE - offset) << startInclusive;

			} else {
				bucket[index] |= ALL_SET << (startInclusive & MOD);
				for (int i = index + 1; i < endIndex; i++) {
					bucket[i] = ALL_SET;
				}
				bucket[endIndex] |= ALL_SET >>> (LONG_SIZE - (endInclusive & MOD));
			}
		}

	}

	public void off(int startInclusive, int offset) {
		int endInclusive = startInclusive + offset;
		if (startInclusive < 0 || startInclusive >= this.size || endInclusive > this.size) {
			throw new IndexOutOfBoundsException();
		}
		if (offset == 0) {
			off(startInclusive);
		} else if (offset < 0) {
			off(endInclusive + 1, -offset);
		} else {
			int index, endIndex;

			if ((index = startInclusive >> LOG_64) == (endIndex = endInclusive - 1 >> LOG_64)) {
				bucket[index] &= ~(ALL_SET >>> (LONG_SIZE - offset) << startInclusive);
			} else {
				bucket[index] &= ~(ALL_SET << (startInclusive & MOD));
				for (int i = index + 1; i < endIndex; i++) {
					bucket[i] = NONE_SET;
				}
				bucket[endIndex] &= ~(ALL_SET >>> (LONG_SIZE - (endInclusive & MOD)));
			}
		}
	}

	public Boolean off(int p) {
		return ifValidPosition(p, () -> ((bucket[p >> LOG_64] &= ~(ONE_SET << (p & (MOD))))));
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
			func.accept((bucket[i >> LOG_64] & (ONE_SET << (i & (MOD)))) != 0, i);
		}
	}

	public void resize(int newSize) {
		if (newSize < this.size) {
			throw new IndexOutOfBoundsException();
		}
		if (size < newSize) {
			ensureCapacityInternal(newSize);
			this.size = newSize;
		}

	}

	private Boolean ifValidPosition(int position, Supplier<Long> supplier) {
		if (position >= 0 && position < this.size) {
			return supplier.get() != 0;
		}
		throw new ArrayIndexOutOfBoundsException(position + " is not valid for size " + this.size);

	}

	public IBitSet immutable() {
		return new ImmutableBitSet(this);
	}

	public LazyBitSet lazy() {
		LazyBitSet lazy = new LazyBitSet(this);
		lazy.merge();
		return lazy;
	}

	public boolean allSet() {
		for (int i = 0; i < actualCapacity - 1; i++) {
			if (bucket[i] != ALL_SET) {
				return false;
			}
		}

		long val = bucket[actualCapacity - 1];
		int count = val == ALL_SET ? 1 << LOG_64 : 0;
		if (count == 0 && val != 0) {
			do {
				count++;
			} while ((val &= (val - 1)) > 0);
		}
		int totalSet = ((actualCapacity - 1) << LOG_64) + count;
		return totalSet == this.size;

	}

	public void reset() {
		for (int i = 0; i < actualCapacity; i++) {
			bucket[i] = 0;
		}
	}

	public int onCount() {
		int count = 0;
		for (int i = 0; i < actualCapacity; i++) {
			if (bucket[i] == ALL_SET) {
				count += LONG_SIZE;
			} else if (bucket[i] == 0) {
				// ignore
			} else {
				long val = bucket[i];
				do {
					count++;
				} while ((val &= (val - 1)) > 0);
			}

		}

		return count;

	}

	public int offCount() {
		return this.size - onCount();
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

	public Stream<Boolean> stream() {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(this.iterator(), Spliterator.DISTINCT), false);
	}

	public int size() {
		return this.size;
	}

	public boolean isEmpty() {
		return this.size == 0;
	}

	public boolean add(boolean e) {
		ensureCapacityInternal(this.size + 1);
		return onOff(this.size++, e);
	}

	/**
	 * 
	 * @author bsarac
	 *
	 */
	public void addAll(Collection<? extends Boolean> c) {
		c.forEach(this::add);
	}

	public void allOff() {
		off(0, this.size);
	}

	public void allOn() {
		on(0, this.size);
	}

	@Deprecated
	public void remove(int index) {

		if (index < 0 || index >= this.size) {
			throw new IndexOutOfBoundsException();
		}

		throw new UnsupportedOperationException();

	}

	public String toBinaryString() {
		if (this.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder(this.size);

		int indx = this.actualCapacity - 1;
		String val = null;
		int remaining = (this.size & MOD);
		if (remaining > 0) {
			val = Long.toBinaryString(this.bucket[indx--]);
			int diff = remaining - val.length();
			for (int i = 0; i < diff; i++) {
				sb.append('0');
			}
			sb.append(val);
		}
		while (indx >= 0) {
			val = Long.toBinaryString(this.bucket[indx--]);
			int diff = 64 - val.length();
			for (int i = 0; i < diff; i++) {
				sb.append('0');
			}
			sb.append(val);
		}

		return sb.toString();
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

		// overflow-conscious code
		if (minCapacity - this.capacity > 0)
			grow(minCapacity);
	}

	private void grow(int minCapacity) {
		// overflow-conscious code
		int oldCapacity = this.capacity;
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

	private static final int getActaulCapacity(int capacity) {
		int diff = capacity & MOD;
		int actualCapacity = (capacity - diff) >>> LOG_64;
		actualCapacity += Math.min(1, diff);
		return actualCapacity;
	}

}
