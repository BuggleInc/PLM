package lessons.welcome.bat.bool2;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatTest;

public class InOrderEntity extends BatEntity {

  public void run(BatTest t)
  {
    t.setResult(inOrder((Integer)t.getParameter(0), (Integer)t.getParameter(1), (Integer)t.getParameter(2),
                        (Boolean)t.getParameter(3)));
  }

  /* BEGIN TEMPLATE */
  boolean inOrder(int a, int b, int c, boolean bOk)
  {
    /* BEGIN SOLUTION */
    return (bOk || (b > a)) && (c > b);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
