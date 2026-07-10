package lessons.recursion.cons;

import static plm.core.ValueSerializer.*;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;

public class PlusOneEntity extends ConsEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(plusOne(RecList.fromArray((int[]) param[0]))));
    }
  }

  /* BEGIN TEMPLATE */
  RecList plusOne(RecList seq)
  {
    /* BEGIN SOLUTION */
    if (seq == null)
      return null;
    return cons(seq.head + 1, plusOne(seq.tail));
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
