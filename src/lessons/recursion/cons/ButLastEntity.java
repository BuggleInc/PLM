package lessons.recursion.cons;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;
import plm.universe.bat.BatTest;

public class ButLastEntity extends ConsEntity {

  public void run(BatTest t) { t.setResult(butLast(RecList.fromArray((int[])t.getParameter(0)))); }

  /* BEGIN TEMPLATE */
  RecList butLast(RecList seq)
  {
    /* BEGIN SOLUTION */
    if (seq.tail == null)
      return null;
    return cons(seq.head, butLast(seq.tail));
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
