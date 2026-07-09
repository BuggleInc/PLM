package lessons.recursion.cons;

import static plm.core.ValueSerializer.*;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;

public class IsMemberEntity extends ConsEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(isMember(RecList.fromArray((int[])param[0]), (Integer)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  Boolean isMember(RecList seq, int val)
  {
    /* BEGIN SOLUTION */
    if (seq == null)
      return false;
    if (seq.head == val)
      return true;
    return isMember(seq.tail, val);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
