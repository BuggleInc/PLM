package lessons.recursion.cons;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;
import plm.universe.bat.BatTest;

public class IncreasingEntity extends ConsEntity {

  public void run(BatTest t) { t.setResult(increasing(RecList.fromArray((int[])t.getParameter(0)))); }

  /* BEGIN TEMPLATE */
  boolean increasing(RecList seq)
  {
    /* BEGIN SOLUTION */
    if (seq == null || seq.tail == null)
      return true;
    if (seq.head > seq.tail.head)
      return false;
    return increasing(seq.tail);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
