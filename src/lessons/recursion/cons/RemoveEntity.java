package lessons.recursion.cons;

import static plm.core.ValueSerializer.*;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;

public class RemoveEntity extends ConsEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(remove(RecList.fromArray((int[])param[0]), (Integer)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  RecList remove(RecList seq, int v)
  {
    /* BEGIN SOLUTION */
    if (seq == null)
      return null;
    if (seq.head == v)
      return remove(seq.tail, v);
    return cons(seq.head, remove(seq.tail, v));
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
