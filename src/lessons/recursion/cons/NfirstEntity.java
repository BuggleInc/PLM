package lessons.recursion.cons;

import static plm.core.ValueSerializer.*;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;

public class NfirstEntity extends ConsEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(RecList.toArray(nfirst(RecList.fromArray((int[])param[0]), (Integer)param[1]))));
    }
  }

  /* BEGIN TEMPLATE */
  RecList nfirst(RecList seq, int n)
  {
    /* BEGIN SOLUTION */
    if (n == 0)
      return null;
    return cons(seq.head, nfirst(seq.tail, n - 1));
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
