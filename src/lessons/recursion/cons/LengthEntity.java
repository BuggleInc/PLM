package lessons.recursion.cons;

import static plm.core.ValueSerializer.*;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;

public class LengthEntity extends ConsEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(length(RecList.fromArray((int[]) param[0]))));
    }
  }

  /* BEGIN TEMPLATE */
  int length(RecList seq)
  {
    /* BEGIN SOLUTION */
    if (seq == null)
      return 0;
    return 1 + length(seq.tail);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
