package lessons.recursion.cons;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;
import plm.universe.bat.BatTest;

public class ButLastEntity extends ConsEntity {

  public void run(BatTest t) { t.setResult(last(RecList.fromArray((int[])t.getParameter(0)))); }

  /* BEGIN TEMPLATE */
  RecList last(RecList seq)
  {
    /* BEGIN SOLUTION */
    if (seq.tail == null)
      return null;
    return cons(seq.head, last(seq.tail));
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
