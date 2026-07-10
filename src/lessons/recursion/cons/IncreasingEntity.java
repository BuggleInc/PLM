package lessons.recursion.cons;

import static plm.core.ValueSerializer.*;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;

public class IncreasingEntity extends ConsEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(increasing(RecList.fromArray((int[]) param[0]))));
    }
  }

  /* BEGIN TEMPLATE */
  boolean increasing(RecList seq)
  {
    /* BEGIN SOLUTION */
    if (seq == null || seq.tail == null)
      return true;
    if (seq.head > seq.tail.head)
      return false;
    return increasing(seq.tail);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
