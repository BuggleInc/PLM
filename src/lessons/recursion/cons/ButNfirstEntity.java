package lessons.recursion.cons;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;

import static plm.core.ValueSerializer.*;

public class ButNfirstEntity extends ConsEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(butNfirst(RecList.fromArray((int[]) param[0]), (Integer)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  RecList butNfirst(RecList seq, int n)
  {
    /* BEGIN SOLUTION */
    if (seq == null || n == 0)
      return seq;
    return butNfirst(seq.tail, n - 1);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
