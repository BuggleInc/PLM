package lessons.recursion.cons;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;
import plm.universe.bat.BatTest;

public class RemoveEntity extends ConsEntity {

  public void run(BatTest t)
  {
    t.setResult(remove(RecList.fromArray((int[])t.getParameter(0)), (Integer)t.getParameter(1)));
  }

  /* BEGIN TEMPLATE */
  RecList remove(RecList seq, int v)
  {
    /* BEGIN SOLUTION */
    if (seq == null)
      return null;
    if (seq.head == v)
      return remove(seq.tail, v);
    return cons(seq.head, remove(seq.tail, v));
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
