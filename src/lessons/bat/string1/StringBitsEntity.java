package lessons.bat.string1;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class StringBitsEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(stringBits((String)param[0])));
    }
  }

  /* BEGIN TEMPLATE */
  String stringBits(String str)
  {
    /* BEGIN SOLUTION */
    String result = "";
    // Note: the loop increments i by 2
    for (int i = 0; i < str.length(); i += 2) {
      result = result + str.substring(i, i + 1);
      // Alternately could use str.charAt(i)
    }
    return result;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
