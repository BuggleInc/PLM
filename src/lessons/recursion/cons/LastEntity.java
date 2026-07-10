package lessons.recursion.cons;

import static plm.core.ValueSerializer.*;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;

public class LastEntity extends ConsEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(last(RecList.fromArray((int[]) param[0]))));
    }
  }

  /* BEGIN TEMPLATE */
  int last(RecList seq)
  {
    /* BEGIN SOLUTION */
    if (seq.tail == null)
      return seq.head;
    return last(seq.tail);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
