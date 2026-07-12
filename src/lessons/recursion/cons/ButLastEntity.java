package lessons.recursion.cons;

import static plm.core.ValueSerializer.*;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;

public class ButLastEntity extends ConsEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(RecList.toArray(butLast(RecList.fromArray((int[])param[0])))));
    }
  }

  /* BEGIN TEMPLATE */
  RecList butLast(RecList seq)
  {
    /* BEGIN SOLUTION */
    if (seq.tail == null)
      return null;
    return cons(seq.head, butLast(seq.tail));
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
