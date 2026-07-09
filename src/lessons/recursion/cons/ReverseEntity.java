package lessons.recursion.cons;

import static plm.core.ValueSerializer.*;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;

public class ReverseEntity extends ConsEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(reverse(RecList.fromArray(toPrimitive(toArrayOfType(param[0], Integer.class))))));
    }
  }

  /* BEGIN TEMPLATE */
  RecList reverse(RecList seq)
  {
    /* BEGIN SOLUTION */
    RecList A = null;
    RecList B = seq;
    while (B != null) {
      A = cons(B.head, A);
      B = B.tail;
    }
    return A;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
