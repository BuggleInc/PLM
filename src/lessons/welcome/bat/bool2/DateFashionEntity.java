package lessons.welcome.bat.bool2;

import static plm.core.ValueSerializer.deserialize;
import static plm.core.ValueSerializer.serialize;

import plm.universe.bat.BatEntity;

public class DateFashionEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(dateFashion((Integer)param[0], (Integer)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  int dateFashion(int you, int date)
  {
    /* BEGIN SOLUTION */
    if (you <= 2 || date <= 2)
      return 0;
    else if (you >= 8 || date >= 8)
      return 2;
    else
      return 1;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
