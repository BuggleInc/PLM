package lessons.recursion.cons;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;
import plm.universe.bat.BatTest;

public class LengthEntity extends ConsEntity {

  public void run(BatTest t) { t.setResult(length(RecList.fromArray((int[])t.getParameter(0)))); }

  /* BEGIN TEMPLATE */
  int length(RecList seq)
  {
    /* BEGIN SOLUTION */
    if (seq == null)
      return 0;
    return 1 + length(seq.tail);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
