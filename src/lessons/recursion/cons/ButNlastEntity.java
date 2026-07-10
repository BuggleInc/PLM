package lessons.recursion.cons;

import static plm.core.ValueSerializer.*;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;

public class ButNlastEntity extends ConsEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(butNlast(RecList.fromArray((int[]) param[0]), (Integer)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  RecList butNlast(RecList seq, int n)
  {
    /* BEGIN SOLUTION */
    if (seq == null || seq.plmInsiderLength() <= n)
      return null;
    return cons(seq.head, butNlast(seq.tail, n));
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
