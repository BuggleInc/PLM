package lessons.recursion.cons;

import static plm.core.ValueSerializer.*;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;

public class NthEntity extends ConsEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(nth(RecList.fromArray(toPrimitive(toArrayOfType(param[0], Integer.class))), (Integer)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  int nth(RecList seq, int n)
  {
    /* BEGIN SOLUTION */
    if (n == 1)
      return seq.head;
    return nth(seq.tail, n - 1);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
