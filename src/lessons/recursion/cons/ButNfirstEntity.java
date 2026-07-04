package lessons.recursion.cons;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;
import plm.universe.bat.BatTest;

public class ButNfirstEntity extends ConsEntity {

  public void run(BatTest t)
  {
    t.setResult(butNfirst(RecList.fromArray((int[])t.getParameter(0)), (Integer)t.getParameter(1)));
  }

  /* BEGIN TEMPLATE */
  RecList butNfirst(RecList seq, int n)
  {
    /* BEGIN SOLUTION */
    if (seq == null || n == 0)
      return seq;
    return butNfirst(seq.tail, n - 1);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
