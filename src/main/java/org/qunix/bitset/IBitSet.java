package org.qunix.bitset;

import java.util.Iterator;

public interface IBitSet {

  public Boolean get(int p);

  public byte getByte(int p);

  public Boolean flip(int p);

  public Boolean onOff(int p, boolean on);

  public Boolean on(int p);

  public Boolean off(int p);

  public IBitSet immutableCopy();

  public boolean allSet();

  public Iterator<Boolean> iterator();
}
