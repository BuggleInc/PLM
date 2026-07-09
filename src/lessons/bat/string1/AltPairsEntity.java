package lessons.bat.string1;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class AltPairsEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(altPairs((String)param[0])));
    }
  }

  /* BEGIN TEMPLATE */
  public String altPairs(String str)
  {
    /* BEGIN SOLUTION */
    String res = "";
    for (int i = 0; i < str.length(); i += 4)
      res += str.substring(i, Math.min(i + 2, str.length()));
    return res;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}