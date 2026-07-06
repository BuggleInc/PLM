package lessons.bat.string1;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;
import plm.universe.bat.ValueFormatter;

public class StringTimesEntity extends BatEntity {

  @Override public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])ValueFormatter.deserialize(getTest(i));
      setTestResult(i, stringTimes((String)param[0], (Integer)param[1]));
    }
  }
  public void run(BatTest t) { t.setResult(stringTimes((String)t.getParameter(0), (Integer)t.getParameter(1))); }

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
