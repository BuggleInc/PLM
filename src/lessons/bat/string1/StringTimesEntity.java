package lessons.bat.string1;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class StringTimesEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(stringTimes((String)param[0], (Integer)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  String stringTimes(String str, int n)
  {
    /* BEGIN SOLUTION */
    String result = "";
    for (int i = 0; i < n; i++) {
      result = result + str; // could use += here
    }
    return result;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
