package lessons.recursion.cons;

import static plm.core.ValueSerializer.*;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;

public class AllDifferentEntity extends ConsEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(allDifferent(RecList.fromArray(toPrimitive(toArrayOfType(param[0], Integer.class))))));
    }
  }

  /* BEGIN TEMPLATE */
  boolean allDifferent(RecList seq)
  {
    /* BEGIN SOLUTION */
    if (seq == null)
      return true;
    /* inline compute isMember */
    RecList ptr = seq.tail;
    while (ptr != null && ptr.head != seq.head)
      ptr = ptr.tail;
    if (ptr != null)
      return false;
    /* end isMember */
    return allDifferent(seq.tail);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
