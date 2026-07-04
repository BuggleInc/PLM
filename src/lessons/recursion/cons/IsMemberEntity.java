package lessons.recursion.cons;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;
import plm.universe.bat.BatTest;

public class IsMemberEntity extends ConsEntity {

  public void run(BatTest t)
  {
    t.setResult(isMember(RecList.fromArray((int[])t.getParameter(0)), (int)t.getParameter(1)));
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
