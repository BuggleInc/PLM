package lessons.bat.string1;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class StringXEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(stringX((String)param[0])));
    }
  }

  /* BEGIN TEMPLATE */
  String stringX(String str)
  {
    /* BEGIN SOLUTION */
    String result = "";
    for (int i = 0; i < str.length(); i++) {
      // Only append the char if it is not the "x" case
      if (!(i > 0 && i < (str.length() - 1) && str.substring(i, i + 1).equals("x"))) {
        result = result + str.substring(i, i + 1); // Could use str.charAt(i) here
      }
    }
    return result;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
