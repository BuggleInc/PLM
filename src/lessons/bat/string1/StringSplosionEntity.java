package lessons.bat.string1;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class StringSplosionEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(stringSplosion((String)param[0])));
    }
  }

  /* BEGIN TEMPLATE */
  String stringSplosion(String str)
  {
    /* BEGIN SOLUTION */
    String result = "";
    // On each iteration, add the substring of the chars 0..i
    for (int i = 0; i < str.length(); i++) {
      result = result + str.substring(0, i + 1);
    }
    return result;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
