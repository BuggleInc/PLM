package lessons.welcome.array.search;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class SecondMaxValueEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(max2Value((int[])param[0])));
    }
  }

  /* BEGIN TEMPLATE */
  // computes the index of the second maximum of the values contained in tab variable
  public int max2Value(int[] tab)
  {
    /* BEGIN SOLUTION */
    int max = Integer.MIN_VALUE;
    int sec = Integer.MIN_VALUE;
    for (int i = 0; i < tab.length; i++)
      if (tab[i] > max) {
        sec = max;
        max = tab[i];
      } else if (tab[i] > sec) {
        sec = tab[i];
      }

    return sec;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
