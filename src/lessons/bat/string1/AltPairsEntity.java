package lessons.bat.string1;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class AltPairsEntity extends BatEntity {

  public void run(BatTest t) { t.setResult(altPairs((String)t.getParameter(0))); }

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