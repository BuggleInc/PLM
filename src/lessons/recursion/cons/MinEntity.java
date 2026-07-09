package lessons.recursion.cons;

import static plm.core.ValueSerializer.*;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;

public class MinEntity extends ConsEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(min(RecList.fromArray(toPrimitive(toArrayOfType(param[0], Integer.class))))));
    }
  }

  /* BEGIN TEMPLATE */
  int min(RecList seq)
  {
    /* BEGIN SOLUTION */
    int v       = seq.head;
    RecList ptr = seq;
    while (ptr != null) {
      if (ptr.head < v)
        v = ptr.head;
      ptr = ptr.tail;
    }
    return v;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
