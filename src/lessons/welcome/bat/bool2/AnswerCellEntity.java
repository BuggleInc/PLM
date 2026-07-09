package lessons.welcome.bat.bool2;

import static plm.core.ValueSerializer.deserialize;
import static plm.core.ValueSerializer.serialize;

import plm.universe.bat.BatEntity;

public class AnswerCellEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(answerCell((Boolean)param[0], (Boolean)param[1], (Boolean)param[2])));
    }
  }

  /* BEGIN TEMPLATE */
  boolean answerCell(boolean isMorning, boolean isMom, boolean isAsleep)
  {
    /* BEGIN SOLUTION */
    return (!isAsleep) && !(isMorning && !isMom);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
