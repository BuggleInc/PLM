package lessons.recursion.cons;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;
import plm.universe.bat.BatTest;

public class LastEntity extends ConsEntity {

  public void run(BatTest t) { t.setResult(last(RecList.fromArray((int[])t.getParameter(0)))); }

  /* BEGIN TEMPLATE */
  int last(RecList seq)
  {
    /* BEGIN SOLUTION */
    if (seq.tail == null)
      return seq.head;
    return last(seq.tail);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
