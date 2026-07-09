package lessons.welcome.bat.bool2;

import static plm.core.ValueSerializer.deserialize;
import static plm.core.ValueSerializer.serialize;

import plm.universe.bat.BatEntity;

public class RedTicketEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(redTicket((Integer)param[0], (Integer)param[1], (Integer)param[2])));
    }
  }

  /* BEGIN TEMPLATE */
  int redTicket(int a, int b, int c)
  {
    /* BEGIN SOLUTION */
    if (a == b && b == c && c == 2)
      return 10;
    else if (a == b && b == c)
      return 5;
    else if (b != a && c != a)
      return 1;
    else
      return 0;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
