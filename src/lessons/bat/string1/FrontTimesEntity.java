package lessons.bat.string1;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class FrontTimesEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(frontTimes((String)param[0], (Integer)param[1])));
    }
  }

  /* BEGIN TEMPLATE */
  String frontTimes(String str, int n)
  {
    /* BEGIN SOLUTION */
    int frontLen = 3;
    if (frontLen > str.length()) {
      frontLen = str.length();
    }
    String front = str.substring(0, frontLen);

    String result = "";
    for (int i = 0; i < n; i++) {
      result = result + front;
    }
    return result;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
