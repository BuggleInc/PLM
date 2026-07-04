package lessons.recursion.cons;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;
import plm.universe.bat.BatTest;

public class NthEntity extends ConsEntity {

  public void run(BatTest t)
  {
    t.setResult(nth(RecList.fromArray((int[])t.getParameter(0)), (Integer)t.getParameter(1)));
  }

  /* BEGIN TEMPLATE */
  int nth(RecList seq, int n)
  {
    /* BEGIN SOLUTION */
    if (n == 1)
      return seq.head;
    return nth(seq.tail, n - 1);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
