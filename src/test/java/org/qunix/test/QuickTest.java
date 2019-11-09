package org.qunix.test;

import java.util.concurrent.ThreadLocalRandom;
import org.qunix.LazyBitSet;

public class QuickTest {

  public static void main(String[] args) {
    int rows = 5;
    int cols = 5;
    LazyBitSet bitSet = new LazyBitSet(rows * cols);
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        bitSet.onOff(i * cols + j, ThreadLocalRandom.current().nextBoolean());
      }
    }

    bitSet.merge();


    print(bitSet, cols, rows);

    int days = 0;
    while (!bitSet.allSet()) {
      days++;
      for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
          boolean current = bitSet.get(i * cols + j);
          if (current) {
            if (i > 0) {
              bitSet.on((i - 1) * cols + j);
            }
            if (i < rows - 1) {
              bitSet.on((i + 1) * cols + j);
            }
            if (j > 0) {
              bitSet.on((i) * cols + (j - 1));
            }
            if (j < cols - 1) {
              bitSet.on((i) * cols + (j + 1));
            }
          }

        }
      }
      printDiff(bitSet, cols, rows);
      bitSet.merge();

      print(bitSet, cols, rows);
    }

    System.out.println("Finished! in " + days + " days");

  }

  private static final void print(LazyBitSet bitSet, int cols, int rows) {
    System.out.println();
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        System.out.print(bitSet.getByte(i * cols + j) + "\t");
      }
      System.out.println();
    }
    System.out.println();
  }

  private static final void printDiff(LazyBitSet bitSet, int cols, int rows) {
    int[] diff = bitSet.diff();
    System.out.println();
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        System.out.print(diff[i * cols + j] + "\t");
      }
      System.out.println();
    }
    System.out.println();
  }
}
