package lessons.welcome.array.search;

import static plm.core.ValueSerializer.*;

import plm.universe.bat.BatEntity;

public class MaxValueEntity extends BatEntity {

  public void run()
  {
    int count = getTestCount();
    for (int i = 0; i < count; i++) {
      Object[] param = (Object[])deserialize(getTest(i));
      setTestResult(i, serialize(maxValue((int[])param[0])));
    }
  }

  /* BEGIN TEMPLATE */
  // computes the index of the maximum of the values contained in tab variable
  public int maxValue(int[] tab)
  {
    /* BEGIN SOLUTION */
    int max = tab[0];
    for (int i = 1; i < tab.length; i++)
      if (tab[i] >= max)
        max = tab[i];

    return max;
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
