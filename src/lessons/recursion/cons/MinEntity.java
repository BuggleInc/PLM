package lessons.recursion.cons;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;
import plm.universe.bat.BatTest;

public class MinEntity extends ConsEntity {

  public void run(BatTest t) { t.setResult(min(RecList.fromArray((int[])t.getParameter(0)))); }

  /* BEGIN TEMPLATE */
  int min(RecList seq)
  {
    /* BEGIN SOLUTION */
    int v       = seq.head;
    RecList ptr = seq;
    while (ptr != null) {
      if (ptr.head < v)
        v = ptr.head;
      ptr = ptr.tail;
    }
    return v;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
