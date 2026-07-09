package lessons.welcome.bat.bool2;

import static plm.core.ValueSerializer.deserialize;
import static plm.core.ValueSerializer.serialize;

import plm.universe.bat.BatEntity;

public class GreenTicketEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(greenTicket((Integer)param[0], (Integer)param[1], (Integer)param[2])));
    }
  }

  /* BEGIN TEMPLATE */
  int greenTicket(int a, int b, int c)
  {
    /* BEGIN SOLUTION */
    if (a == b && b == c)
      return 20;
    else if (a == b || b == c || a == c)
      return 10;
    else // (a != b && b != a && c != a)
      return 0;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
