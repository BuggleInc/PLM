package lessons.recursion.cons;

import static plm.core.ValueSerializer.*;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;

public class OccurrenceEntity extends ConsEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(occurences(RecList.fromArray(toPrimitive(toArrayOfType(param[0], Integer.class))), (Integer)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  int occurences(RecList seq, int val)
  {
    /* BEGIN SOLUTION */
    if (seq == null)
      return 0;
    if (seq.head == val)
      return 1 + occurences(seq.tail, val);
    return occurences(seq.tail, val);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
