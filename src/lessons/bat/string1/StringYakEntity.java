package lessons.bat.string1;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class StringYakEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(stringYak((String)param[0])));
    }
  }

  /* BEGIN TEMPLATE */
  String stringYak(String str)
  {
    /* BEGIN SOLUTION */
    String result = "";

    for (int i = 0; i < str.length(); i++) {
      // Look for i starting a "yak" -- advance i in that case
      if (i + 2 < str.length() && str.charAt(i) == 'y' && str.charAt(i + 2) == 'k') {
        i = i + 2;
      } else { // Otherwise do the normal append
        result = result + str.charAt(i);
      }
    }

    return result;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
