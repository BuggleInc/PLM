package lessons.recursion.cons;

import static plm.core.ValueSerializer.*;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;

public class NlastEntity extends ConsEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(nlast(RecList.fromArray(toPrimitive(toArrayOfType(param[0], Integer.class))), (Integer)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  RecList nlast(RecList seq, int n)
  {
    /* BEGIN SOLUTION */
    if (seq == null || seq.plmInsiderLength() <= n)
      return seq;
    return nlast(seq.tail, n);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
