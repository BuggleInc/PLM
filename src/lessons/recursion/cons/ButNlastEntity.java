package lessons.recursion.cons;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;
import plm.universe.bat.BatTest;

public class ButNlastEntity extends ConsEntity {

  public void run(BatTest t)
  {
    t.setResult(butNlast(RecList.fromArray((int[])t.getParameter(0)), (Integer)t.getParameter(1)));
  }

  /* BEGIN TEMPLATE */
  RecList butNlast(RecList seq, int n)
  {
    /* BEGIN SOLUTION */
    if (seq == null || seq.plmInsiderLength() <= n)
      return null;
    return cons(seq.head, butNlast(seq.tail, n));
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
