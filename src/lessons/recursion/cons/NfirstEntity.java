package lessons.recursion.cons;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;
import plm.universe.bat.BatTest;

public class NfirstEntity extends ConsEntity {

  public void run(BatTest t)
  {
    t.setResult(nfirst(RecList.fromArray((int[])t.getParameter(0)), (Integer)t.getParameter(1)));
  }

  /* BEGIN TEMPLATE */
  RecList nfirst(RecList seq, int n)
  {
    /* BEGIN SOLUTION */
    if (n == 0)
      return null;
    return cons(seq.head, nfirst(seq.tail, n - 1));
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
