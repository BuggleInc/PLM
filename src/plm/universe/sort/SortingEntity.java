package plm.universe.sort;

import plm.core.lang.primitives.EntityPrimitives;
import plm.universe.Entity;

@EntityPrimitives(SortingEntityPrimitives.class)
public class SortingEntity extends Entity implements SortingEntityPrimitives {

  public SortingEntity() { super(); }

  @Override public void copy(int from, int to)
  {
    ((SortingWorld)this.world).copy(from, to);
    stepUI();
  }

  @Override public int getValue(int i) { return ((SortingWorld)this.world).getValue(i); }

  @Override public int getValueCount() { return ((SortingWorld)this.world).getValueCount(); }

  @Override public boolean isSmaller(int i, int j) { return ((SortingWorld)this.world).isSmaller(i, j); }

  @Override public boolean isSmallerThan(int i, int value) { return ((SortingWorld)this.world).isSmallerThan(i, value); }

  public void run()
  {
    // Child implement this
  }

  @Override public void setValue(int i, int value)
  {
    ((SortingWorld)this.world).setValue(i, value);
    stepUI();
  }

  @Override public void swap(int i, int j)
  {
    ((SortingWorld)this.world).swap(i, j);
    stepUI();
  }
}
