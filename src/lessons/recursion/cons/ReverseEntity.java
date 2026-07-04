package lessons.recursion.cons;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;
import plm.universe.bat.BatTest;

public class ReverseEntity extends ConsEntity {

  public void run(BatTest t) { t.setResult(reverse(RecList.fromArray((int[])t.getParameter(0)))); }

  /* BEGIN TEMPLATE */
  RecList reverse(RecList seq)
  {
    /* BEGIN SOLUTION */
    RecList A = null;
    RecList B = seq;
    while (B != null) {
      A = cons(B.head, A);
      B = B.tail;
    }
    return A;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
