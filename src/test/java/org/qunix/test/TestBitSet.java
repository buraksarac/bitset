/**
 * 
 */
package org.qunix.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;
import org.qunix.bitset.BitSet;

/**
 *
 * tests for {@link BitSet}
 *
 * @author bsarac types 2019-11-23 22:37:09 +0100
 */
public class TestBitSet {

	
	@Test
	public void testToBinaryString() {

		BitSet set = new BitSet(64);
		assertEquals("", set.toBinaryString());

	}

	@Test
	public void testToBinaryString1() {

		BitSet set = new BitSet(64);
		set.resize(64);
		assertEquals("0000000000000000000000000000000000000000000000000000000000000000", set.toBinaryString());

	}

	@Test
	public void testToBinaryString2() {

		BitSet set = new BitSet(64);
		set.resize(64);
		set.on(63);
		set.on(0);
		assertEquals("1000000000000000000000000000000000000000000000000000000000000001", set.toBinaryString());

	}

	@Test
	public void testToBinaryString3() {

		BitSet set = new BitSet(64);
		set.resize(65);
		set.on(63);
		set.on(0);
		assertEquals("01000000000000000000000000000000000000000000000000000000000000001", set.toBinaryString());

	}

	@Test
	public void testToBinaryString4() {

		BitSet set = new BitSet(64);
		set.add(true);
		assertEquals("1", set.toBinaryString());
		set.add(false);
		assertEquals("01", set.toBinaryString());
		set.add(true);
		assertEquals("101", set.toBinaryString());

	}

	@Test
	public void testOnRangeNegative() {
		BitSet set = new BitSet(64);
		set.resize(5);
		set.on(3, -2);
		assertTrue(set.get(2));
		assertTrue(set.get(3));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testOnRangeNegativeFail() {
		BitSet set = new BitSet(64);
		set.resize(64);
		set.on(3, -5);
	}

	@Test
	public void testOnRange() {
		BitSet set = new BitSet(64);
		set.resize(64);
		set.on(3, 0);
		assertTrue(set.get(3));
	}

	@Test
	public void testOnRange2() {
		BitSet set = new BitSet(64);
		set.resize(64);
		set.on(3, 2);
		assertFalse(set.get(2));
		assertTrue(set.get(3));
		assertTrue(set.get(4));
		assertFalse(set.get(5));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testOnRangeFail() {
		BitSet set = new BitSet(64);
		set.resize(5);
		set.on(3, 5);
	}

	@Test
	public void testOff() {
		BitSet set = new BitSet(64);
		set.resize(5);
		set.on(0, 5);
		set.off(2, 2);
		assertTrue(set.get(0));
		assertTrue(set.get(1));
		assertFalse(set.get(2));
		assertFalse(set.get(3));
		assertTrue(set.get(4));
	}

	@Test
	public void testOffRangeNegative() {
		BitSet set = new BitSet(64);
		set.resize(5);
		set.on(0, 5);
		set.off(3, -2);
		assertTrue(set.get(0));
		assertTrue(set.get(1));
		assertFalse(set.get(2));
		assertFalse(set.get(3));
		assertTrue(set.get(4));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testOffRangeNegativeFail() {
		BitSet set = new BitSet(64);
		set.resize(64);
		set.off(3, -5);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testOffRangeFail() {
		BitSet set = new BitSet(64);
		set.resize(5);
		set.off(3, 5);
	}

	@Test
	public void testBound() {
		BitSet set = new BitSet(64);
		set.resize(64);
		assertFalse(set.get(63));
		set.on(63);
		assertTrue(set.get(63));
	}

	@Test
	public void testBound2() {
		BitSet set = new BitSet(64);
		set.resize(129);

		set.allOn();
		assertEquals(129, set.onCount());
		assertTrue(set.allSet());
		set.allOff();
		assertEquals(129, set.offCount());
		set.off(0, 129);
		set.on(64, 64);
		assertFalse(set.get(63));
		assertFalse(set.get(128));
		for (int i = 64; i < 128; i++) {
			assertTrue(set.get(i));
		}

		assertEquals("01111111111111111111111111111111111111111111111111111111111111111"
				+ "0000000000000000000000000000000000000000000000000000000000000000", set.toBinaryString());

		set.on(1, 128);
		for (int i = 1; i < 129; i++) {
			assertTrue(set.get(i));
		}
		set.off(63, 65);
		for (int i = 63; i < 128; i++) {
			assertFalse("" + i, set.get(i));
		}
		assertTrue(set.get(128));

	}

	@Test
	public void testBound3() {
		BitSet set = new BitSet(64);
		set.resize(64);
		assertFalse(set.get(0));
		set.on(0);
		assertTrue(set.get(0));
	}

	@Test
	public void testAdd() {

		BitSet set = new BitSet();
		int newSize = ThreadLocalRandom.current().nextInt(100, 9999);

		IntStream.range(0, newSize).mapToObj(i -> i % 2 == 0).forEach(set::add);
		assertEquals(newSize, set.size());
		IntStream.range(0, newSize).forEach(i -> assertEquals(i % 2 == 0, set.get(i)));
	}
	
	@Test
	public void testAdd2() {

		BitSet set = new BitSet();
		int newSize = ThreadLocalRandom.current().nextInt(100, 9999);

		IntStream.range(0, newSize).mapToObj(i -> i % 2 != 0).forEach(set::add);
		assertEquals(newSize, set.size());
		IntStream.range(0, newSize).forEach(i -> assertEquals(i % 2 != 0, set.get(i)));
	}

	@Test
	public void testSize() {

		BitSet set = new BitSet();
		assertEquals(0, set.size());
		set.add(true);
		set.add(true);
		assertEquals(2, set.size());
		int newSize = ThreadLocalRandom.current().nextInt(100, 9999);
		set.resize(newSize);
		assertEquals(newSize, set.size());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testSize2() {

		BitSet set = new BitSet();
		assertEquals(0, set.size());
		set.resize(-1);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testSize3() {

		BitSet set = new BitSet();
		set.add(false);
		set.add(false);
		assertEquals(2, set.size());
		set.resize(1);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testSize4() {

		BitSet set = new BitSet();
		set.add(false);
		set.add(false);
		set.on(3);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testSize5() {

		BitSet set = new BitSet();
		set.on(-1);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testSize6() {

		BitSet set = new BitSet();
		set.on(0);
	}

	@Test
	public void testResize() {

		BitSet set = new BitSet();
		set.resize(64); // sets all values 0

		IntStream.range(0, 64).mapToObj(set::get).forEach(Assert::assertFalse);

	}

	@Test
	public void testResize2() {

		BitSet set = new BitSet();
		int newSize = ThreadLocalRandom.current().nextInt(100, 9999);
		set.resize(newSize);

		assertEquals(newSize, set.size());

	}

	@Test
	public void testStream() {
		BitSet set = new BitSet();
		int newSize = ThreadLocalRandom.current().nextInt(100, 9999);
		IntStream.range(0, newSize).mapToObj(i -> i % 2 == 0).forEach(set::add);
		AtomicInteger count = new AtomicInteger(0);
		set.stream().forEach(b -> assertEquals(count.get() % 2 == 0, set.get(count.getAndIncrement())));
	}

	@Test
	public void testAllSet() {
		BitSet set = new BitSet();
		int newSize = ThreadLocalRandom.current().nextInt(100, 9999);
		IntStream.range(0, newSize).mapToObj(i -> i % 2 == 0).forEach(set::add);
		set.off(0);
		assertFalse(set.allSet());
		IntStream.range(0, newSize).forEach(i -> set.on(i));
		assertTrue(set.allSet());

	}

	@Test
	public void testOnOff() {

		BitSet set = new BitSet();
		int newSize = ThreadLocalRandom.current().nextInt(100, 9999);
		set.resize(newSize);

		IntStream.range(0, newSize).peek(i -> set.onOff(i, i % 2 == 0))
				.forEach(i -> assertEquals(i % 2 == 0, set.get(i)));

	}
}
