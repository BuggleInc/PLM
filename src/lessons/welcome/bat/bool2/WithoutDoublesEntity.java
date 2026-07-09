package lessons.welcome.bat.bool2;

import static plm.core.ValueSerializer.deserialize;
import static plm.core.ValueSerializer.serialize;

import plm.universe.bat.BatEntity;

public class WithoutDoublesEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(withoutDoubles((Integer)param[0], (Integer)param[1], (Boolean)param[2])));
    }
  }

  /* BEGIN TEMPLATE */
  int withoutDoubles(int die1, int die2, boolean noDoubles)
  {
    /* BEGIN SOLUTION */
    if (noDoubles && (die1 == die2)) {
      if (die1 == 6)
        return 1 + die2;
      else
        return die1 + 1 + die2;
    } else
      return die1 + die2;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
