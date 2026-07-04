package lessons.recursion.cons;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;
import plm.universe.bat.BatTest;

public class OccurrenceEntity extends ConsEntity {

  public void run(BatTest t)
  {
    t.setResult(occurences(RecList.fromArray((int[])t.getParameter(0)), (int)t.getParameter(1)));
  }

  /* BEGIN TEMPLATE */
  int occurences(RecList seq, int val)
  {
    /* BEGIN SOLUTION */
    if (seq == null)
      return 0;
    if (seq.head == val)
      return 1 + occurences(seq.tail, val);
    return occurences(seq.tail, val);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
