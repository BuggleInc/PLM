package lessons.welcome.bat.bool2;

import static plm.core.ValueSerializer.deserialize;
import static plm.core.ValueSerializer.serialize;

import plm.universe.bat.BatEntity;

public class BlueTicketEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(blueTicket((Integer)param[0], (Integer)param[1], (Integer)param[2])));
    }
  }

  /* BEGIN TEMPLATE */
  int blueTicket(int a, int b, int c)
  {
    /* BEGIN SOLUTION */
    int ab = a + b;
    int ac = a + c;
    int bc = b + c;

    if (ab == 10 || ac == 10 || bc == 10)
      return 10;
    else if (ab == (bc + 10) || ab == (ac + 10))
      return 5;
    else
      return 0;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
