package lessons.recursion.cons;

import static plm.core.ValueSerializer.*;

import lessons.recursion.cons.universe.ConsEntity;
import lessons.recursion.cons.universe.RecList;

public class ConcatEntity extends ConsEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(RecList.toArray(concat(RecList.fromArray((int[])param[0]), RecList.fromArray((int[])param[1])))));
    }
  }

  /* BEGIN TEMPLATE */
  RecList concat(RecList seq1, RecList seq2)
  {
    /* BEGIN SOLUTION */
    // Revert seq1 into A
    RecList A = null;
    RecList B = seq1;
    while (B != null) {
      A = cons(B.head, A);
      B = B.tail;
    }
    // add A at front of seq2 in B
    B = seq2;
    while (A != null) {
      B = cons(A.head, B);
      A = A.tail;
    }
    return B;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
