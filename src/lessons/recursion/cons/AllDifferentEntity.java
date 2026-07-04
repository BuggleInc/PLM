package lessons.recursion.cons;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;
import plm.universe.bat.BatTest;

public class AllDifferentEntity extends ConsEntity {

  public void run(BatTest t) { t.setResult(allDifferent(RecList.fromArray((int[])t.getParameter(0)))); }

  /* BEGIN TEMPLATE */
  boolean allDifferent(RecList seq)
  {
    /* BEGIN SOLUTION */
    if (seq == null)
      return true;
    /* inline compute isMember */
    RecList ptr = seq.tail;
    while (ptr != null && ptr.head != seq.head)
      ptr = ptr.tail;
    if (ptr != null)
      return false;
    /* end isMember */
    return allDifferent(seq.tail);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
