package lessons.recursion.cons;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;
import plm.universe.bat.BatTest;

public class PlusOneEntity extends ConsEntity {

  public void run(BatTest t) { t.setResult(plusOne(RecList.fromArray((int[])t.getParameter(0)))); }

  /* BEGIN TEMPLATE */
  RecList plusOne(RecList seq)
  {
    /* BEGIN SOLUTION */
    if (seq == null)
      return null;
    return cons(seq.head + 1, plusOne(seq.tail));
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
